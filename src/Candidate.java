package application;
import java.io.Serializable;

public interface Candidate extends Serializable {
    void incrementVote();
    void resetVoteCount();
    void addObserver(Observer o);
    void notifyObservers();
    String getName();
    String getDepartment();
    String getDetails();
    String getDetailsUrl();
    String getImagePath();
    int getVoteCount();
    void setVoteCount(int voteCount);
}