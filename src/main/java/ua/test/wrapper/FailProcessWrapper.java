package ua.test.wrapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import ua.test.services.backup.file.Status;

import java.util.Date;

/**
 * This class use to wrap fail response
 *
 * @author vzagnitko
 */
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class FailProcessWrapper extends ProcessWrapper {

    public FailProcessWrapper() {

    }

    public FailProcessWrapper(ProcessWrapper processWrapper) {
        super(processWrapper.getBackupId(), processWrapper.getDate(), Status.FAILED);
    }

    public FailProcessWrapper(String backupId, Date date) {
        super(backupId, date, Status.FAILED);
    }

    public String getBackupId() {
        return backupId;
    }

    public void setBackupId(String backupId) {
        this.backupId = backupId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

}
