package systems.reformcloud.reformcloud2.executor.api.common.application.unsafe;

import systems.reformcloud.reformcloud2.executor.api.common.application.ApplicationConfig;

import javax.annotation.Nonnull;

public interface ApplicationUnsafe {

    static void checkIfUnsafe(@Nonnull ApplicationConfig config) {
        if (!config.implementedVersion().equalsIgnoreCase(System.getProperty("reformcloud.runner.version"))) {
            System.err.println(config.getName() + " is using api-version " + config.implementedVersion()
                    + " instead of current version " + System.getProperty("reformcloud.runner.version"));
        }
    }
}
