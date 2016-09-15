package ua.test.domains;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.io.Serializable;
import java.util.Optional;

/**
 * This class describe user todo entity
 *
 * @author vzagnitko
 */
public class UserTodo implements Serializable {

    private long id;

    private String subject;

    private String dueDate;

    private boolean done;

    public UserTodo() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    @Override
    public boolean equals(Object obj) {
        if (!Optional.ofNullable(obj).isPresent()) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UserTodo userTodo = (UserTodo) obj;
        return Objects.equal(this.id, userTodo.id) &&
                Objects.equal(this.subject, userTodo.subject) &&
                Objects.equal(this.dueDate, userTodo.dueDate) &&
                Objects.equal(this.done, userTodo.done);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.id, this.subject, this.dueDate, this.done);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("id", this.id).add("subject", this.subject).add("dueDate", this.dueDate).
                add("done", this.done).toString();
    }

}
