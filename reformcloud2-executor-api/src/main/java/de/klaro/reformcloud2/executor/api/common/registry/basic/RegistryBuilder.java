package de.klaro.reformcloud2.executor.api.common.registry.basic;

import com.google.gson.reflect.TypeToken;
import de.klaro.reformcloud2.executor.api.common.configuration.JsonConfiguration;
import de.klaro.reformcloud2.executor.api.common.registry.Registry;
import de.klaro.reformcloud2.executor.api.common.utility.system.SystemHelper;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;

public class RegistryBuilder {

    private RegistryBuilder() {
        throw new UnsupportedOperationException();
    }

    public static Registry newRegistry(Path operatingFolder) {
        return new RegistryImpl(operatingFolder);
    }

    private static final class RegistryImpl implements Registry {

        private RegistryImpl(Path folder) {
            this.folder = folder;
            SystemHelper.createDirectory(folder);
        }

        private final Path folder;

        @Override
        public <T> T createKey(String keyName, T t) {
            Path target = Paths.get(folder + "/" + keyName + ".json");
            if (Files.exists(target)) {
                return null;
            }

            new JsonConfiguration()
                    .add("key", t)
                    .write(target);
            return t;
        }

        @Override
        public <T> T getKey(String keyName) {
            Path target = Paths.get(folder + "/" + keyName + ".json");
            if (Files.exists(target)) {
                return null;
            }

            return JsonConfiguration.read(target).get("key", new TypeToken<T>() {});
        }

        @Override
        public void deleteKey(String key) {
            Path target = Paths.get(folder + "/" + key + ".json");
            SystemHelper.deleteFile(target.toFile());
        }

        @Override
        public <T> T updateKey(String key, T newValue) {
            Path target = Paths.get(folder + "/" + key + ".json");
            if (Files.exists(target)) {
                return null;
            }

            new JsonConfiguration()
                    .add("key", newValue)
                    .write(target);
            return newValue;
        }

        @Override
        public <T> Collection<T> readKeys(Function<JsonConfiguration, T> function) {
            if (!Files.exists(folder)) {
                return Collections.emptyList();
            }

            File[] files = folder.toFile().listFiles();
            if (files == null) {
                return Collections.emptyList();
            }

            final Collection<T> out = new ArrayList<>();
            Arrays.stream(files)
                    .map(file -> {
                        JsonConfiguration configuration = JsonConfiguration.read(file);
                        return function.apply(configuration);
                    }).forEach(out::add);
            return out;
        }
    }
}