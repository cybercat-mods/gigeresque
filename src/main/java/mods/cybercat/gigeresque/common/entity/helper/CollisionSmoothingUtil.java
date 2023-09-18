package mods.cybercat.gigeresque.common.entity.helper;

import java.util.List;
import java.util.function.Consumer;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;

public record CollisionSmoothingUtil() {
	private static float invSqrt(float x) {
		var xhalf = 0.5f * x;
		var i = Float.floatToIntBits(x);
		i = 0x5f3759df - (i >> 1);
		x = Float.intBitsToFloat(i);
		x *= (1.5f - xhalf * x * x);
		return x;
	}

	private static float sampleSdf(float[] erx, float[] ery, float[] erz, float[] ecx, float[] ecy, float[] ecz, int count, float px, float py, float pz, float pnx, float pny, float pnz, float x, float y, float z, float smoothingRange, float invSmoothingRange) {
		var sdfDst = 0.0f;
		var planeDst = pnx * (x - px) + pny * (y - py) + pnz * (z - pz);

		for (int i = 0; i < count; i++) {
			var rsx = x - ecx[i];
			var rsy = y - ecy[i];
			var rsz = z - ecz[i];
			// Ellipsoid SDF - https://www.iquilezles.org/www/articles/ellipsoids/ellipsoids.htm
			var prx = rsx * erx[i];
			var pry = rsy * ery[i];
			var prz = rsz * erz[i];
			var k1 = invSqrt(prx * prx + pry * pry + prz * prz);
			var ellipsoidDst = Mth.sqrt(rsx * rsx + rsy * rsy + rsz * rsz) * (1.0f - 1.0f * k1);

			// Smooth intersection - https://iquilezles.org/www/articles/distfunctions/distfunctions.htm
			var h = Mth.clamp(0.5f - 0.5f * (ellipsoidDst + planeDst) * invSmoothingRange, 0.0f, 1.0f);
			ellipsoidDst = ellipsoidDst + (-planeDst - ellipsoidDst) * h + smoothingRange * h * (1.0f - h);

			if (i == 0)
				sdfDst = ellipsoidDst;
			else {
				// Smooth min - https://www.iquilezles.org/www/articles/smin/smin.htm
				h = Mth.clamp(0.5f + 0.5f * (ellipsoidDst - sdfDst) * invSmoothingRange, 0.0f, 1.0f);
				sdfDst = ellipsoidDst + (sdfDst - ellipsoidDst) * h - smoothingRange * h * (1.0f - h);
			}
		}
		// Smooth intersection - https://iquilezles.org/www/articles/distfunctions/distfunctions.htm
		var h = Mth.clamp(0.5f - 0.5f * (sdfDst + planeDst) * invSmoothingRange, 0.0f, 1.0f);
		sdfDst = sdfDst + (-planeDst - sdfDst) * h + smoothingRange * h * (1.0f - h);

		return sdfDst;
	}

	@Nullable
	public static Pair<Vec3, Vec3> findClosestPoint(Consumer<Shapes.DoubleLineConsumer> consumer, Vec3 pp, Vec3 pn, float smoothingRange, float boxScale, float dx, int iters, float threshold, Vec3 p) {
		var boxConsumer = new BoxConsumer(p, boxScale);
		consumer.accept(boxConsumer);
		if (boxConsumer.size == 0)
			return null;
		return findClosestPoint(boxConsumer.erx, boxConsumer.ery, boxConsumer.erz, boxConsumer.ecx, boxConsumer.ecy, boxConsumer.ecz, boxConsumer.size, pp, pn, smoothingRange, boxScale, dx, iters, threshold, p);
	}

	@Nullable
	public static Pair<Vec3, Vec3> findClosestPoint(List<AABB> boxes, Vec3 pp, Vec3 pn, float smoothingRange, float boxScale, float dx, int iters, float threshold, Vec3 p) {
		if (boxes.isEmpty())
			return null;

		var erx = new float[boxes.size()];
		var ery = new float[boxes.size()];
		var erz = new float[boxes.size()];
		var ecx = new float[boxes.size()];
		var ecy = new float[boxes.size()];
		var ecz = new float[boxes.size()];
		var i = 0;

		for (var box : boxes) {
			erx[i] = 1.0f / ((float) (box.maxX - box.minX) / 2 * boxScale);
			ery[i] = 1.0f / ((float) (box.maxY - box.minY) / 2 * boxScale);
			erz[i] = 1.0f / ((float) (box.maxZ - box.minZ) / 2 * boxScale);
			ecx[i] = (float) ((box.minX + box.maxX) / 2 - p.x);
			ecy[i] = (float) ((box.minY + box.maxY) / 2 - p.y);
			ecz[i] = (float) ((box.minZ + box.maxZ) / 2 - p.z);
			i++;
		}

		return findClosestPoint(erx, ery, erz, ecx, ecy, ecz, boxes.size(), pp, pn, smoothingRange, boxScale, dx, iters, threshold, p);
	}

	@Nullable
	private static Pair<Vec3, Vec3> findClosestPoint(float[] erx, float[] ery, float[] erz, float[] ecx, float[] ecy, float[] ecz, int count, Vec3 pp, Vec3 pn, float smoothingRange, float boxScale, float dx, int iters, float threshold, Vec3 p) {
		var halfThreshold = threshold * 0.5f;
		var plx = (float) (pp.x - p.x);
		var ply = (float) (pp.y - p.y);
		var plz = (float) (pp.z - p.z);
		var pnx = (float) pn.x;
		var pny = (float) pn.y;
		var pnz = (float) pn.z;
		var px = 0.0f;
		var py = 0.0f;
		var pz = 0.0f;
		var invSmoothingRange = 1.0f / smoothingRange;

		for (int j = 0; j < iters; j++) {
			var dst = sampleSdf(erx, ery, erz, ecx, ecy, ecz, count, plx, ply, plz, pnx, pny, pnz, px, py, pz, smoothingRange, invSmoothingRange);
			var fx1 = sampleSdf(erx, ery, erz, ecx, ecy, ecz, count, plx, ply, plz, pnx, pny, pnz, px + dx, py, pz, smoothingRange, invSmoothingRange);
			var fy1 = sampleSdf(erx, ery, erz, ecx, ecy, ecz, count, plx, ply, plz, pnx, pny, pnz, px, py + dx, pz, smoothingRange, invSmoothingRange);
			var fz1 = sampleSdf(erx, ery, erz, ecx, ecy, ecz, count, plx, ply, plz, pnx, pny, pnz, px, py, pz + dx, smoothingRange, invSmoothingRange);
			var gx = dst - fx1;
			var gy = dst - fy1;
			var gz = dst - fz1;
			var m = invSqrt(gx * gx + gy * gy + gz * gz);
			gx *= m;
			gy *= m;
			gz *= m;

			if (Float.isNaN(gx) || Float.isNaN(gy) || Float.isNaN(gz) || Double.isNaN(px) || Double.isNaN(py) || Double.isNaN(pz))
				return null;
			var absDst = Math.abs(dst);
			var step = absDst >= halfThreshold ? dst : Math.signum(dst) * halfThreshold;
			px += gx * step;
			py += gy * step;
			pz += gz * step;
			if (absDst < threshold)
				return Pair.of(new Vec3(p.x + px, p.y + py, p.z + pz), new Vec3(-gx, -gy, -gz).normalize());
		}

		return null;
	}
}
