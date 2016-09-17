package ua.test.services.export;

import org.springframework.beans.factory.annotation.Autowired;
import ua.test.exceptions.BusinessLogicException;
import ua.test.services.backup.file.FileBackupService;
import ua.test.wrapper.ProcessWrapper;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Describe common methods for all ExportService classes
 *
 * @author victorzagnitko
 */
public abstract class AbstractExportService {

    @Autowired
    private FileBackupService fileBackupService;

    /**
     * Check if exist backup file in storage
     *
     * @param backupId to check
     * @return true if exists file, otherwise false
     * @throws BusinessLogicException if cannot check if exist backupid
     */
    protected boolean isExistBackup(@Nonnull String backupId) throws BusinessLogicException {
        List<ProcessWrapper> backups = fileBackupService.collectBackupResult();
        for (ProcessWrapper processWrapper : backups) {
            if (processWrapper.getBackupId().equals(backupId)) {
                return true;
            }
        }
        return false;
    }

}
