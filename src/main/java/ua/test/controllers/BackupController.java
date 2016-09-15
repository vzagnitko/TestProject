package ua.test.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ua.test.exceptions.RestException;
import ua.test.services.rest.RestServiceImpl;
import ua.test.wrapper.ProcessWrapper;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Receive request from client to start backupping process
 *
 * @author vzagnitko
 */
@RestController
@RequestMapping(value = "/backups")
public class BackupController {

    private static final Logger LOG = LoggerFactory.getLogger(BackupController.class);

    @Autowired
    private RestServiceImpl restService;

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Map<String, String>> backupData() throws RestException {
        LOG.info("Start backup data...");
        return ResponseEntity.ok().body(Collections.singletonMap("backupId", restService.backupFileData()));
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<ProcessWrapper>> getBackupData() throws RestException {
        LOG.info("Start backup data...");
        return ResponseEntity.ok().body(restService.collectFileBackupResult());
    }

}
