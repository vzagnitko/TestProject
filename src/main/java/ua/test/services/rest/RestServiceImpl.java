package ua.test.services.rest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.test.exceptions.BusinessLogicException;
import ua.test.exceptions.RestException;
import ua.test.services.backup.file.FileBackupService;
import ua.test.services.csv.CsvService;
import ua.test.util.string.StringUtils;
import ua.test.wrapper.ProcessWrapper;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.nio.file.Files;
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
    private CsvService csvService;

    /**
     * This method is backupped data from file repository
     *
     * @return backup id
     * @throws RestException if cannot process all operations
     */
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
    public List<ProcessWrapper> collectFileBackupResult() throws RestException {
        try {
            return fileBackupService.collectBackupResult();
        } catch (BusinessLogicException exc) {
            LOG.error("Error {}", exc);
            throw new RestException(exc);
        }
    }

    public void createCsvFileReport(HttpServletResponse response, @Nonnull String backupId) throws RestException {
        try {
            File csvFile = csvService.createCsvReport(backupId);
            response.setContentType(Files.probeContentType(csvFile.toPath()));
            response.setContentLength(new Long(csvFile.length()).intValue());
            response.setHeader("Content-Disposition", StringUtils.concatString("attachment; filename=", csvFile.getName()));
            IOUtils.copy(FileUtils.openInputStream(csvFile), response.getOutputStream());
        } catch (Exception exc) {
            LOG.error("Error {}", exc);
            throw new RestException(exc);
        }
    }

}
