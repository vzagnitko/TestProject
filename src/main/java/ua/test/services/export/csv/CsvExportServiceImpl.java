package ua.test.services.export.csv;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.output.StringBuilderWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.test.exceptions.BusinessLogicException;
import ua.test.services.export.AbstractExportService;
import ua.test.services.export.ExportService;
import ua.test.util.file.FileUtils;
import ua.test.util.file.OperationSystem;
import ua.test.util.string.StringUtils;

import javax.annotation.Nonnull;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This service for work with CSV
 *
 * @author vzagnitko
 */
@Service
public class CsvExportServiceImpl extends AbstractExportService implements ExportService {

    private static final Logger LOG = LoggerFactory.getLogger(ExportService.class);

    private static final String TEMP_DIRECTORY_PATH = OperationSystem.getTempPath();

    /**
     * CSV field which is shown in header of CSV file
     */
    private static final List<String> CSV_FIELDS = Lists.newArrayList("Username", "TodoItemId", "Subject", "DueDate", "Done");

    /**
     * Create a CSV report
     *
     * @param backupId to find a CSV report
     * @return object of created file
     * @throws BusinessLogicException if cannot save a file
     */
    @Nonnull
    @Override
    public InputStream createReport(@Nonnull String backupId) throws BusinessLogicException {
        if (!isExistBackup(backupId)) {
            LOG.error("Cannot find backup with backup id {}", backupId);
            throw new IllegalArgumentException("Cannot find backup with backup id " + backupId);
        }
        LOG.info("Start create a csv file");
        try (StringBuilderWriter sbw = new StringBuilderWriter()) {
            try (CSVPrinter csvFilePrinter = new CSVPrinter(sbw, CSVFormat.EXCEL.withDelimiter(';'))) {
                String backupPath = StringUtils.concatString(TEMP_DIRECTORY_PATH, backupId, ".json");
                csvFilePrinter.printRecord(CSV_FIELDS);
                try (InputStream in = FileUtils.fastReadFile(backupPath)) {
                    Iterator it = new ObjectMapper().readValues(new JsonFactory().createParser(in), Map.class);
                    do {
                        Map<String, ?> userData = (Map<String, ?>) it.next();
                        List<Map<String, ?>> users = (List<Map<String, ?>>) userData.get("users");
                        for (Map<String, ?> user : users) {
                            List<Map<String, ?>> userTodos = (List<Map<String, ?>>) user.get("todos");
                            for (Map<String, ?> userTodo : userTodos) {
                                csvFilePrinter.print(user.get("username"));
                                csvFilePrinter.print(userTodo.get("id"));
                                csvFilePrinter.print(userTodo.get("subject"));
                                csvFilePrinter.print(userTodo.get("dueDate"));
                                csvFilePrinter.print(userTodo.get("done"));
                                csvFilePrinter.println();
                            }
                        }
                    } while (it.hasNext());
                }
            }
            LOG.info("Success create a csv file!");
            return new ByteArrayInputStream(sbw.toString().getBytes());
        } catch (Exception exc) {
            LOG.error("Cannot create a CSV file {}", exc);
            throw new BusinessLogicException(exc);
        }
    }

}
