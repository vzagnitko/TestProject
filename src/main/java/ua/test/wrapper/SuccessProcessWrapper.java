package ua.test.wrapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import ua.test.domains.User;
import ua.test.services.backup.file.Status;

import java.util.Date;
import java.util.List;

/**
 * This class use to wrap success response
 *
 * @author vzagnitko
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SuccessProcessWrapper extends ProcessWrapper {

    private List<User> users;

    public SuccessProcessWrapper() {

    }

    public SuccessProcessWrapper(ProcessWrapper processWrapper, List<User> users) {
        super(processWrapper.getBackupId(), processWrapper.getDate(), Status.OK);
        this.users = users;
    }

    public SuccessProcessWrapper(String backupId, Date date, List<User> users) {
        super(backupId, date, Status.OK);
        this.users = users;
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

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

}
