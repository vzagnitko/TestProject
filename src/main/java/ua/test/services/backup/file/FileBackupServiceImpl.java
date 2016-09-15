package ua.test.services.backup.file;

import com.google.common.eventbus.AsyncEventBus;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.test.exceptions.BusinessLogicException;
import ua.test.exceptions.RepositoryException;
import ua.test.repository.file.FileRepository;
import ua.test.services.backup.BackupService;
import ua.test.services.backup.file.event.FileBackupData;
import ua.test.wrapper.ProcessWrapper;

import java.util.List;

/**
 * This service backups a data from external repository
 *
 * @author vzagnitko
 */
@Service
public class FileBackupServiceImpl implements FileBackupService {

    private static final Logger LOG = LoggerFactory.getLogger(BackupService.class);

    private static final int ID_LONG = 32;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private AsyncEventBus asyncEventBus;

    /**
     * Backup data from server
     *
     * @return id generated file
     * @throws BusinessLogicException if cannot save a file
     */
    @Override
    public String backupData() throws BusinessLogicException {
        String backupId = RandomStringUtils.randomAlphanumeric(ID_LONG);
        try {
            asyncEventBus.post(new FileBackupData(backupId));
            LOG.info("Success save json, file id {}", backupId);
        } catch (Exception exc) {
            LOG.error("Fail backup data {}", exc);
            throw new BusinessLogicException(exc);
        }
        return backupId;
    }

    /**
     * Get backupping result
     *
     * @return list of backupping result
     */
    @Override
    public List<ProcessWrapper> collectBackupResult() throws BusinessLogicException {
        try {
            return fileRepository.retrieveBackupResults();
        } catch (RepositoryException exc) {
            LOG.error("Fail retrieve backup results {}", exc);
            throw new BusinessLogicException(exc);
        }
    }

}
