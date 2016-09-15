package ua.test.controllers;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ua.test.exceptions.RestException;
import ua.test.services.rest.RestServiceImpl;

import javax.servlet.http.HttpServletResponse;

/**
 * Receive request from client to start create CSV report
 *
 * @author vzagnitko
 */
@RestController
@RequestMapping(value = "/exports/{backupId}")
public class ExportController {

    private static final Logger LOG = LoggerFactory.getLogger(ExportController.class);

    @Autowired
    private RestServiceImpl restService;

    @RequestMapping(method = RequestMethod.GET)
    public void downloadCsvReport(@PathVariable String backupId, HttpServletResponse response) throws RestException {
        if (Strings.isNullOrEmpty(backupId)) {
            LOG.info("Backup id parameter is wrong!");
            throw new IllegalArgumentException("Backup id parameter is wrong!");
        }
        LOG.info("Start create csv file by report: " + backupId);
        restService.createCsvFileReport(response, backupId);
    }

}
