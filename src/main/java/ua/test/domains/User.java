package ua.test.domains;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * This class describe user entity
 *
 * @author vzagnitko
 */
public class User implements Serializable {

    private long id;

    private String username;

    private String email;

    private List<UserTodo> todos;

    public User() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<UserTodo> getTodos() {
        return todos;
    }

    public void setTodos(List<UserTodo> todos) {
        this.todos = todos;
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
        final User user = (User) obj;
        return Objects.equal(this.id, user.id) &&
                Objects.equal(this.username, user.username) &&
                Objects.equal(this.email, user.email) &&
                Objects.equal(this.todos, user.todos);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.id, this.username, this.email, this.todos);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("id", this.id).add("username", this.username).
                add("email", this.email).add("todos", this.todos).toString();
    }

}
