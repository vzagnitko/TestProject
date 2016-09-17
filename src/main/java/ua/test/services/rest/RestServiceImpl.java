package ua.test.services.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.test.exceptions.BusinessLogicException;
import ua.test.exceptions.RestException;
import ua.test.services.backup.file.FileBackupService;
import ua.test.services.export.ExportService;
import ua.test.wrapper.ProcessWrapper;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * This class contains main services to work with REST services
 *
 * @author vzagnitko
 */
@Service
public class RestServiceImpl {

    private static final Logger LOG = LoggerFactory.getLogger(RestServiceImpl.class);

    @Autowired
    private FileBackupService fileBackupService;

    @Autowired
    private ExportService csvService;

    /**
     * This method is backupped data from file repository
     *
     * @return backup id
     * @throws RestException if cannot process all operations
     */
    @Nonnull
    public String backupFileData() throws RestException {
        try {
            return fileBackupService.backupData();
        } catch (BusinessLogicException exc) {
            LOG.error("Error {}", exc);
            throw new RestException(exc);
        }
    }

    /**
     * This method is collected result of backup
     *
     * @return list backupped result
     * @throws RestException if cannot process all operations
     */
    @Nonnull
    public List<ProcessWrapper> collectFileBackupResult() throws RestException {
        try {
            return fileBackupService.collectBackupResult();
        } catch (BusinessLogicException exc) {
            LOG.error("Error {}", exc);
            throw new RestException(exc);
        }
    }

}
