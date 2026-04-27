package mods.cybercat.gigeresque.client.entity.render.helper;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * A tiny arithmetic expression evaluator used to express head-offset formulas in JSON.
 * <p>
 * Supported tokens:
 * <ul>
 * <li>Numeric literals: {@code 1}, {@code 2.5}, {@code 0.7}</li>
 * <li>Variables: {@code size_x}, {@code size_y}, {@code size_z}, {@code pivot_x}, {@code pivot_y}, {@code pivot_z},
 * {@code parasite_height}, {@code parasite_width}</li>
 * <li>Operators: {@code + - * /} with conventional precedence</li>
 * <li>Parentheses: {@code (} {@code )}</li>
 * <li>Unary minus: {@code -size_y}</li>
 * </ul>
 * <p>
 * Examples (these reproduce the original hard-coded formulas):
 *
 * <pre>
 *   cow vertical_offset:  -size_y
 *   cow face_offset:      size_z + (size_z / 2) + parasite_height
 *   horse face_offset:    size_z * 3 + size_z / 2
 *   wolf vertical_offset: -size_y - (size_y / 1.9)
 * </pre>
 */
public final class OffsetExpression {

    public static final Codec<OffsetExpression> CODEC = Codec.STRING.comapFlatMap(
        s -> {
            try {
                return DataResult.success(parse(s));
            } catch (IllegalArgumentException e) {
                return DataResult.error(() -> "Invalid offset expression '" + s + "': " + e.getMessage());
            }
        },
        OffsetExpression::source
    );

    private final String source;

    private final List<Token> rpn;

    private OffsetExpression(String source, List<Token> rpn) {
        this.source = source;
        this.rpn = rpn;
    }

    public String source() {
        return source;
    }

    public double evaluate(OffsetContext ctx) {
        Deque<Double> stack = new ArrayDeque<>();
        for (var t : rpn) {
            switch (t.kind) {
                case NUMBER -> stack.push(t.number);
                case VARIABLE -> stack.push(resolve(t.text, ctx));
                case OPERATOR -> {
                    double b = stack.pop();
                    double a = stack.isEmpty() ? 0.0 : stack.pop(); // unary minus support
                    stack.push(applyOp(t.text.charAt(0), a, b));
                }
                case UNARY_MINUS -> {
                    double v = stack.pop();
                    stack.push(-v);
                }
            }
        }
        return stack.isEmpty() ? 0.0 : stack.pop();
    }

    private static double resolve(String name, OffsetContext ctx) {
        return switch (name) {
            case "size_x" -> ctx.sizeX();
            case "size_y" -> ctx.sizeY();
            case "size_z" -> ctx.sizeZ();
            case "pivot_x" -> ctx.pivotX();
            case "pivot_y" -> ctx.pivotY();
            case "pivot_z" -> ctx.pivotZ();
            case "parasite_height" -> ctx.parasiteHeight();
            case "parasite_width" -> ctx.parasiteWidth();
            default -> throw new IllegalArgumentException("Unknown variable: " + name);
        };
    }

    private static double applyOp(char op, double a, double b) {
        return switch (op) {
            case '+' -> a + b;
            case '-' -> a - b;
            case '*' -> a * b;
            case '/' -> a / b;
            default -> throw new IllegalArgumentException("Unknown operator: " + op);
        };
    }

    public static OffsetExpression parse(String expr) {
        var tokens = tokenize(expr);
        List<Token> output = new ArrayList<>();
        Deque<Token> ops = new ArrayDeque<>();

        Token prev = null;
        for (var t : tokens) {
            switch (t.kind) {
                case NUMBER, VARIABLE -> output.add(t);
                case OPERATOR -> {
                    if (t.text.equals("-") && (prev == null || prev.kind == TokenKind.OPERATOR || prev.kind == TokenKind.LPAREN)) {
                        ops.push(new Token(TokenKind.UNARY_MINUS, "-", 0));
                    } else {
                        while (
                            !ops.isEmpty()
                                && (ops.peek().kind == TokenKind.OPERATOR || ops.peek().kind == TokenKind.UNARY_MINUS)
                                && precedence(ops.peek()) >= precedence(t)
                        ) {
                            output.add(ops.pop());
                        }
                        ops.push(t);
                    }
                }
                case LPAREN -> ops.push(t);
                case RPAREN -> {
                    while (!ops.isEmpty() && ops.peek().kind != TokenKind.LPAREN) {
                        output.add(ops.pop());
                    }
                    if (ops.isEmpty()) {
                        throw new IllegalArgumentException("Mismatched parentheses");
                    }
                    ops.pop();
                }
                default -> throw new IllegalArgumentException("Unexpected token: " + t.text);
            }
            prev = t;
        }
        while (!ops.isEmpty()) {
            Token t = ops.pop();
            if (t.kind == TokenKind.LPAREN) {
                throw new IllegalArgumentException("Mismatched parentheses");
            }
            output.add(t);
        }

        return new OffsetExpression(expr, output);
    }

    private static int precedence(Token t) {
        if (t.kind == TokenKind.UNARY_MINUS)
            return 3;
        return switch (t.text) {
            case "+", "-" -> 1;
            case "*", "/" -> 2;
            default -> 0;
        };
    }

    private static List<Token> tokenize(String expr) {
        List<Token> tokens = new ArrayList<>();
        var i = 0;
        var n = expr.length();
        while (i < n) {
            var c = expr.charAt(i);
            if (Character.isWhitespace(c)) {
                i++;
            } else if (Character.isDigit(c) || c == '.') {
                var start = i;
                while (i < n && (Character.isDigit(expr.charAt(i)) || expr.charAt(i) == '.')) {
                    i++;
                }
                tokens.add(
                    new Token(
                        TokenKind.NUMBER,
                        expr.substring(start, i),
                        Double.parseDouble(expr.substring(start, i))
                    )
                );
            } else if (Character.isLetter(c) || c == '_') {
                var start = i;
                while (i < n && (Character.isLetterOrDigit(expr.charAt(i)) || expr.charAt(i) == '_')) {
                    i++;
                }
                tokens.add(new Token(TokenKind.VARIABLE, expr.substring(start, i), 0));
            } else if (c == '+' || c == '-' || c == '*' || c == '/') {
                tokens.add(new Token(TokenKind.OPERATOR, String.valueOf(c), 0));
                i++;
            } else if (c == '(') {
                tokens.add(new Token(TokenKind.LPAREN, "(", 0));
                i++;
            } else if (c == ')') {
                tokens.add(new Token(TokenKind.RPAREN, ")", 0));
                i++;
            } else {
                throw new IllegalArgumentException("Unexpected character '" + c + "' at index " + i);
            }
        }
        return tokens;
    }

    private enum TokenKind {
        NUMBER,
        VARIABLE,
        OPERATOR,
        LPAREN,
        RPAREN,
        UNARY_MINUS
    }

    private record Token(
        TokenKind kind,
        String text,
        double number
    ) {}

    /**
     * Snapshot of the values an offset expression can reference. Built per-frame from the host's head data and the
     * parasite entity.
     */
    public record OffsetContext(
        double sizeX,
        double sizeY,
        double sizeZ,
        double pivotX,
        double pivotY,
        double pivotZ,
        double parasiteHeight,
        double parasiteWidth
    ) {}
}
