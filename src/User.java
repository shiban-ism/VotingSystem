package application;
import java.io.Serializable;

public class User implements Serializable {
    public String id;
    public String name;
    public String department;
    private boolean hasVoted;

    public User(String id, String name, String department, boolean hasVoted) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.hasVoted = hasVoted;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDepartment() { return department; }
    public boolean hasVoted() { return hasVoted; }
    public void setHasVoted(boolean hasVoted) { this.hasVoted = hasVoted; }
}
