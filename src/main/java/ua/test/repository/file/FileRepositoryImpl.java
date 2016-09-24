package ua.test.repository.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import ua.test.common.Links;
import ua.test.exceptions.RepositoryException;
import ua.test.repository.AbstractRepository;
import ua.test.util.file.FileUtils;
import ua.test.util.file.OperationSystem;
import ua.test.util.string.StringUtils;
import ua.test.wrapper.ProcessWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.List;

/**
 * This service contains methods to work with files
 *
 * @author victorzagnitko on 15.09.16.
 */
@Repository
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
    @Nullable
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
                String json = IOUtils.toString(FileUtils.fastReadFile(path), Charset.defaultCharset());
                ProcessWrapper report = objectMapper.readValue(json, ProcessWrapper.class);
                LOG.info("File {} is {}", FilenameUtils.getName(path), report.getStatus().name());
                reports.add(report);
            }
        } catch (Exception exc) {
            LOG.error("Cannot retrieve backupped data {}", exc);
            throw new RepositoryException(exc);
        }
        return reports;
    }

    /**
     * This method used to save object
     *
     * @param processWrapper to save
     * @throws RepositoryException if cannot save a object in repository
     */
    @Override
    public void saveBackupObject(@Nonnull ProcessWrapper processWrapper) throws RepositoryException {
        try {
            LOG.info("File name to save {}", processWrapper.getBackupId());
            try (FileChannel rwChannel = new RandomAccessFile(StringUtils.concatString(TEMP_DIRECTORY_PATH,
                    processWrapper.getBackupId(), ".json"), "rw").getChannel()) {
                rwChannel.force(true);
                try (InputStream is = new ByteArrayInputStream(objectMapper.writeValueAsBytes(processWrapper))) {
                    IOUtils.copy(new StringReader(IOUtils.toString(is, Charset.defaultCharset())),
                            Channels.newOutputStream(rwChannel), Charset.defaultCharset());
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
    private List<String> searchBackupReport() throws IOException {
        LOG.info("Start search json files");
        List<String> paths = Lists.newArrayList();
        File tempDirectoryFile = new File(TEMP_DIRECTORY_PATH);
        String[] extensions = {"json"};
        Collection<File> backyppedData = org.apache.commons.io.FileUtils.listFiles(tempDirectoryFile, extensions, false);
        backyppedData.forEach(file -> paths.add(file.getAbsolutePath()));
        return paths;
    }

}
