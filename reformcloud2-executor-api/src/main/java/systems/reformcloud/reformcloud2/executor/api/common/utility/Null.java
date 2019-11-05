package systems.reformcloud.reformcloud2.executor.api.common.utility;

import systems.reformcloud.reformcloud2.executor.api.common.utility.system.SystemHelper;

import java.io.File;
import java.nio.file.Paths;

public class Null {

    private Null() {
        throw new UnsupportedOperationException();
    }

    static {
        SystemHelper.createDirectory(Paths.get("reformcloud/.bin/dev/null"));
    }

    private static final String devNull = new File("reformcloud/.bin/dev/null").getAbsolutePath();

    public static String devNull() {
        return devNull;
    }
}