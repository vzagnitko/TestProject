package ua.test.wrapper;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import ua.test.services.backup.file.Status;

import java.io.Serializable;
import java.util.Date;

/**
 * This class use to wrap process of backup response
 *
 * @author vzagnitko
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class ProcessWrapper implements Serializable {

    protected String backupId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date date;

    protected Status status;

    public ProcessWrapper() {

    }

    public ProcessWrapper(String backupId, Date date, Status status) {
        this.backupId = backupId;
        this.date = date;
        this.status = status;
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
