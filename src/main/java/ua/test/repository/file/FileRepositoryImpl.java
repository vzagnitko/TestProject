package ua.test.repository.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ua.test.common.Links;
import ua.test.exceptions.RepositoryException;
import ua.test.repository.AbstractRepository;
import ua.test.util.file.FileUtils;
import ua.test.util.file.OperationSystem;
import ua.test.util.string.StringUtils;
import ua.test.wrapper.FailProcessWrapper;
import ua.test.wrapper.ProcessWrapper;

import javax.annotation.Nonnull;
import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.List;

/**
 * This service contains methods to work with files
 *
 * @author victorzagnitko on 15.09.16.
 */
@Service
public class FileRepositoryImpl extends AbstractRepository implements FileRepository {

    private static final Logger LOG = LoggerFactory.getLogger(FileRepositoryImpl.class);

    private static final String TEMP_DIRECTORY_PATH = OperationSystem.getTempPath();

    @Autowired
    private ObjectMapper objectMapper;

    @Value(value = "${main.host}")
    private String mainHost;

    @Value(value = "${fail.over.count}")
    private int failOverCount;

    /**
     * This method used to get object from repository
     *
     * @param processWrapper wrapper for data which will be stored
     * @param clazz          class of backupping file
     * @param <T>            type of backupping file
     * @return response object which will be backuped
     * @throws RepositoryException if cannot backup a data
     */
    @Override
    public <T> T retrieveBackupObject(@Nonnull ProcessWrapper processWrapper,
                                      @Nonnull Class<T> clazz) throws RepositoryException {
        try {
            T object = null;
            for (int i = 0; i < failOverCount; i++) {
                try {
                    if (object != null) {
                        LOG.info("Success retrieve object");
                        break;
                    }
                    ResponseEntity<T> response = sendRequest(StringUtils.concatString(mainHost, Links.USERS_LINK),
                            HttpMethod.GET, null, clazz);
                    object = response.getBody();
                    LOG.info("Take response, http status {}", response.getStatusCode());
                } catch (Exception exc) {
                    LOG.error("Cannot take a valid response {}", exc);
                }
            }
            if (object == null) {
                saveBackupObject(new FailProcessWrapper(processWrapper));
                throw new RepositoryException("Cannot retrieve object to backup!");
            }
            return object;
        } catch (Exception exc) {
            LOG.error("Error {}", exc);
            throw new RepositoryException(exc);
        }
    }

    /**
     * This method used to get all backup data results
     *
     * @return results of backup
     * @throws RepositoryException if cannot take a result
     */
    @Override
    public List<ProcessWrapper> retrieveBackupResults() throws RepositoryException {
        List<ProcessWrapper> reports = Lists.newArrayList();
        try {
            for (String path : searchBackupReport()) {
                String json = FileUtils.fastReadFile(path);
                ProcessWrapper report = objectMapper.readValue(json, ProcessWrapper.class);
                switch (report.getStatus()) {
                    case OK: {
                        LOG.info("File {} is OK", FilenameUtils.getName(path));
                        break;
                    }
                    case IN_PROGRESS: {
                        LOG.warn("File {} is IN_PROGRESS", FilenameUtils.getName(path));
                        break;
                    }
                    case FAILED: {
                        LOG.warn("File {} is FAILED", FilenameUtils.getName(path));
                        break;
                    }
                }
                reports.add(report);
            }
        } catch (Exception exc) {
            LOG.error("Cannot retrieve backupped data {}", exc);
            throw new RepositoryException(exc);
        }
        return reports;
    }

    @Override
    public void saveBackupObject(@Nonnull ProcessWrapper processWrapper) throws RepositoryException {
        try {
            LOG.info("File name to save {}", processWrapper.getBackupId());
            try (FileChannel rwGzipChannel = new RandomAccessFile(StringUtils.concatString(TEMP_DIRECTORY_PATH,
                    processWrapper.getBackupId(), ".json"), "rw").getChannel()) {
                rwGzipChannel.force(true);
                try (InputStream is = new ByteArrayInputStream(objectMapper.writeValueAsBytes(processWrapper))) {
                    IOUtils.copy(new StringReader(IOUtils.toString(is, Charset.defaultCharset())),
                            Channels.newOutputStream(rwGzipChannel), Charset.defaultCharset());
                }
            }
        } catch (Exception exc) {
            LOG.error("Error {}", exc);
            throw new RepositoryException(exc);
        }
    }

    /**
     * Search backupping report files
     *
     * @return list of backupping reports
     */
    private List<String> searchBackupReport() {
        LOG.info("Start search json files");
        List<String> paths = Lists.newArrayList();
        for (File file : new File(TEMP_DIRECTORY_PATH).listFiles((FileFilter) new WildcardFileFilter("*.json"))) {
            paths.add(file.getAbsolutePath());
        }
        return paths;
    }

}