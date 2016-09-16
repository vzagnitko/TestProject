package ua.test.services.backup.file.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.test.domains.User;
import ua.test.exceptions.BusinessLogicException;
import ua.test.exceptions.RepositoryException;
import ua.test.repository.file.FileRepository;
import ua.test.services.backup.file.Status;
import ua.test.wrapper.FailProcessWrapper;
import ua.test.wrapper.ProcessWrapper;
import ua.test.wrapper.SuccessProcessWrapper;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;

/**
 * This class receive async event to star backup process
 *
 * @author victorzagnitko
 */
@Component
public class FileBackupSubscriber {

    private static final Logger LOG = LoggerFactory.getLogger(FileBackupSubscriber.class);

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private AsyncEventBus asyncEventBus;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Async event to backup data
     *
     * @param fileBackupReceiver contains information to backup
     * @throws BusinessLogicException if cannot backup a data
     */
    @Subscribe
    @AllowConcurrentEvents
    public void fileBackupEvent(FileBackupData fileBackupReceiver) throws BusinessLogicException {
        try {
            String backupId = fileBackupReceiver.getBackupId();
            ProcessWrapper processWrapper = new ProcessWrapper(backupId, new Date(), Status.IN_PROGRESS);

            fileRepository.saveBackupObject(processWrapper);
            List<?> response = fileRepository.retrieveBackupObject(processWrapper, List.class);
            if (response == null) {
                fileRepository.saveBackupObject(new FailProcessWrapper(processWrapper));
                throw new RepositoryException("Cannot retrieve object to backup!");
            }
            List<User> users = Lists.newArrayList();
            for (Object ob : response) {
                User user = objectMapper.convertValue(ob, User.class);
                users.add(user);
            }

            fileRepository.saveBackupObject(new SuccessProcessWrapper(processWrapper, users));
            LOG.info("Success save json, file id {}", backupId);
        } catch (Exception exc) {
            LOG.error("Fail backup data {}", exc);
            throw new BusinessLogicException(exc);
        }

    }

    @PostConstruct
    private void init() {
        asyncEventBus.register(this);
    }

}
