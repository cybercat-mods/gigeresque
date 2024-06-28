package mods.cybercat.gigeresque.platform;

import java.util.ServiceLoader;

public final class GigServices {

    public static final CommonRegistry COMMON_REGISTRY = load(CommonRegistry.class);

    private GigServices() {
        throw new UnsupportedOperationException();
    }

    public static <T> T load(Class<T> clazz) {
        return ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
    }
}
