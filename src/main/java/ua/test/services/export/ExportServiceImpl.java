package ua.test.services.export;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.test.exceptions.ExportReportException;
import ua.test.util.string.StringUtils;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * This class contains methods to work with export files
 *
 * @author victorzagnitko
 */
@Service
public class ExportServiceImpl {

    private static final Logger LOG = LoggerFactory.getLogger(ExportServiceImpl.class);

    @Autowired
    private ExportService exportService;

    public void createCsvFileReport(@Nonnull HttpServletResponse response, @Nonnull String backupId) throws ExportReportException {
        try (InputStream is = exportService.createReport(backupId)) {
            response.setContentType(ExportMimeType.CSV.getMime());
            response.setContentLength(((ByteArrayInputStream) is).available());
            response.setHeader("Content-Disposition", StringUtils.concatString("attachment; filename=", backupId, ".csv"));
            IOUtils.copy(is, response.getOutputStream());
        } catch (Exception exc) {
            LOG.error("Error {}", exc);
            throw new ExportReportException(exc);
        }
    }

}
