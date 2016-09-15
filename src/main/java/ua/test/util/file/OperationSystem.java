package ua.test.util.file;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.SystemUtils;

/**
 * Define operation system to find correct temp folder
 *
 * @author vzagnitko
 */
public enum OperationSystem {

    OS_UNIX(SystemUtils.JAVA_IO_TMPDIR.concat("/")),
    OS_WINDOWS(SystemUtils.JAVA_IO_TMPDIR.concat("\\"));

    private String tempDirectoryPath;

    OperationSystem(String tempDirectoryPath) {
        this.tempDirectoryPath = tempDirectoryPath;
    }

    /**
     * Find temp directory path
     *
     * @return temp directory path
     */
    public static String getTempPath() {
        if (SystemUtils.IS_OS_WINDOWS) {
            return FilenameUtils.normalize(OS_WINDOWS.getTempDirectoryPath(), false);
        }
        return FilenameUtils.normalize(OS_UNIX.getTempDirectoryPath(), true);
    }

    public String getTempDirectoryPath() {
        return this.tempDirectoryPath;
    }

}
