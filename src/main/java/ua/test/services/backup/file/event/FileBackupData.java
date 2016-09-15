package ua.test.services.backup.file.event;

/**
 * This class save generated backupId
 *
 * @author victorzagnitko
 */
public class FileBackupData {

    private String backupId;

    public FileBackupData(String backupId) {
        this.backupId = backupId;
    }

    public String getBackupId() {
        return backupId;
    }

    public void setBackupId(String backupId) {
        this.backupId = backupId;
    }
}
