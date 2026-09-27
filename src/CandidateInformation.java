package application;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CandidateInformation implements Candidate {
    public String name;
    public String department;
    public String details;
    public String detailsUrl;
    public String imagePath;
    private int voteCount;
    private transient List<Observer> observers = new ArrayList<>();

    public CandidateInformation(String name, String department, String details, String detailsUrl, String imagePath) {
        this.name = name;
        this.department = department;
        this.details = details;
        this.detailsUrl = detailsUrl;
        this.imagePath = imagePath;
        this.voteCount = 0;
    }

    @Override
    public void incrementVote() {
        voteCount++;
        notifyObservers();
    }

    @Override
    public void resetVoteCount() {
        voteCount = 0;
        notifyObservers();
    }

    @Override
    public void addObserver(Observer o) {
        if (observers == null) observers = new ArrayList<>();
        observers.add(o);
    }

    @Override
    public void notifyObservers() {
        if (observers == null) observers = new ArrayList<>();
        for (Observer o : observers) o.update();
    }

    @Override
    public String getName() { return name; }
    @Override
    public String getDepartment() { return department; }
    @Override
    public String getDetails() { return details; }
    @Override
    public String getDetailsUrl() { return detailsUrl; }
    @Override
    public String getImagePath() { return imagePath; }
    @Override
    public int getVoteCount() { return voteCount; }
    @Override
    public void setVoteCount(int voteCount) { this.voteCount = voteCount; }
}