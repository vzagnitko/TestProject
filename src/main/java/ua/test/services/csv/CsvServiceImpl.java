package ua.test.services.csv;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.output.StringBuilderWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.test.domains.User;
import ua.test.domains.UserTodo;
import ua.test.exceptions.BusinessLogicException;
import ua.test.util.file.FileUtils;
import ua.test.util.file.OperationSystem;
import ua.test.util.string.StringUtils;
import ua.test.wrapper.SuccessProcessWrapper;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.List;

/**
 * This service for work with CSV
 *
 * @author vzagnitko
 */
@Service
public class CsvServiceImpl implements CsvService {

    private static final Logger LOG = LoggerFactory.getLogger(CsvServiceImpl.class);

    private static final String TEMP_DIRECTORY_PATH = OperationSystem.getTempPath();

    /**
     * CSV field which is shown in header of CSV file
     */
    private static final List<String> CSV_FIELDS = Lists.newArrayList("Username", "TodoItemId", "Subject", "DueDate", "Done");

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Create a CSV report
     *
     * @param backupId to find a CSV report
     * @return object of created file
     * @throws BusinessLogicException if cannot save a file
     */
    @Override
    public File createCsvReport(@Nonnull String backupId) throws BusinessLogicException {
        LOG.info("Start create a csv file");
        String csvFilePath = StringUtils.concatString(TEMP_DIRECTORY_PATH, "report_",
                String.valueOf(System.currentTimeMillis()), ".csv");
        try (StringBuilderWriter sbw = new StringBuilderWriter()) {
            try (CSVPrinter csvFilePrinter = new CSVPrinter(sbw, CSVFormat.EXCEL.withDelimiter(';'))) {
                String backupPath = StringUtils.concatString(TEMP_DIRECTORY_PATH, backupId, ".json");
                String json = FileUtils.fastReadFile(backupPath);
                SuccessProcessWrapper report = objectMapper.readValue(json, SuccessProcessWrapper.class);
                csvFilePrinter.printRecord(CSV_FIELDS);
                for (User user : report.getUsers()) {
                    for (UserTodo userTodo : user.getTodos()) {
                        csvFilePrinter.print(user.getUsername());
                        csvFilePrinter.print(userTodo.getId());
                        csvFilePrinter.print(userTodo.getSubject());
                        csvFilePrinter.print(userTodo.getDueDate());
                        csvFilePrinter.print(userTodo.isDone());
                        csvFilePrinter.println();
                    }
                }
            }
            LOG.info("Success create a csv file!");
            FileUtils.fastSaveFile(csvFilePath, sbw.toString());
        } catch (Exception exc) {
            LOG.error("Cannot create a CSV file {}", exc);
            throw new BusinessLogicException(exc);
        }
        return new File(csvFilePath);
    }

}
