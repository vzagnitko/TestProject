package ua.test.services.backup;

import ua.test.exceptions.BusinessLogicException;
import ua.test.wrapper.ProcessWrapper;

import java.util.List;

/**
 * Describe methods to backup
 *
 * @author vzagnitko
 */
public interface BackupService {

    /**
     * Backup data from server
     *
     * @return id generated file
     * @throws BusinessLogicException if cannot save a file
     */
    String backupData() throws BusinessLogicException;

    /**
     * Get backuping result
     *
     * @return list of backupping result
     * @throws BusinessLogicException if cannot save a file
     */
    List<ProcessWrapper> collectBackupResult() throws BusinessLogicException;

}
