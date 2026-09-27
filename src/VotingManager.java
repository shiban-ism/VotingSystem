package application;
import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

public class VotingManager {
    private static VotingManager instance;
    private Database database;
    private List<String> departments;
    private int currentDepartmentIndex;
    private boolean loadSerializedState = true;

    private VotingManager() {
        database = new Database();
        departments = database.getUsers().stream()
                .map(User::getDepartment)
                .distinct()
                .collect(Collectors.toList());
        currentDepartmentIndex = 0;
        if (loadSerializedState) {
            loadVotingState();
        }
        System.out.println("CS Candidates after initialization:");
        database.getCandidates().stream()
                .filter(c -> c.getDepartment().equals("CS"))
                .forEach(c -> System.out.println(c.getName() + " (Votes: " + c.getVoteCount() + ")"));
        System.out.println("Users after initialization:");
        database.getUsers().stream()
                .forEach(u -> System.out.println(u.getName() + " (Department: " + u.getDepartment() + ", Has Voted: " + u.hasVoted() + ")"));
    }

    public static VotingManager getInstance() {
        if (instance == null) {
            instance = new VotingManager();
        }
        return instance;
    }

    public List<Candidate> getAllCandidates() {
        return database.getCandidates();
    }

    public List<Candidate> getCandidatesByDepartment(String department) {
        return database.getCandidates().stream()
                .filter(c -> c.getDepartment().equals(department))
                .collect(Collectors.toList());
    }

    public User authenticate(String id) {
        return database.getUsers().stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void vote(User user, Candidate candidate) {
        if (!user.hasVoted()) {
            user.setHasVoted(true);
            candidate.incrementVote();
            database.updateUser(user);
            saveVotingState();
            System.out.println("Vote recorded: " + user.getName() + " voted for " + candidate.getName());
            System.out.println("Candidate " + candidate.getName() + " now has " + candidate.getVoteCount() + " votes");
            System.out.println("User " + user.getName() + " hasVoted: " + user.hasVoted());
        } else {
            System.out.println("Vote rejected: " + user.getName() + " has already voted");
        }
    }

    public boolean haveAllUsersInDepartmentVoted(String department) {
        boolean allVoted = database.getUsers().stream()
                .filter(u -> u.getDepartment().equals(department))
                .allMatch(User::hasVoted);
        System.out.println("Checking if all users in " + department + " have voted: " + allVoted);
        return allVoted;
    }

    public String getCurrentDepartment() {
        if (currentDepartmentIndex < departments.size()) {
            return departments.get(currentDepartmentIndex);
        }
        return null;
    }

    public void moveToNextDepartment() {
        currentDepartmentIndex++;
        if (currentDepartmentIndex >= departments.size()) {
            resetAllVoting();
            currentDepartmentIndex = 0;
        }
    }

    public void resetDepartmentVoting(String department) {
        for (User user : database.getUsers()) {
            if (user.getDepartment().equals(department)) {
                user.setHasVoted(false);
            }
        }
        for (Candidate candidate : database.getCandidates()) {
            if (candidate.getDepartment().equals(department)) {
                candidate.resetVoteCount();
            }
        }
        saveVotingState();
        System.out.println("Reset voting for department: " + department);
    }

    public void resetAllVoting() {
        for (User user : database.getUsers()) {
            user.setHasVoted(false);
        }
        for (Candidate candidate : database.getCandidates()) {
            candidate.resetVoteCount();
        }
        saveVotingState();
        System.out.println("Reset voting for all departments");
    }

    public boolean haveAllDepartmentsCompleted() {
        return currentDepartmentIndex >= departments.size();
    }

    private void saveVotingState() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("voting_state.ser"))) {
            oos.writeObject(database.getUsers());
            oos.writeObject(database.getCandidates());
            oos.writeObject(departments);
            oos.writeObject(currentDepartmentIndex);
            System.out.println("Saved voting state with SE candidates:");
            database.getCandidates().stream()
                    .filter(c -> c.getDepartment().equals("SE"))
                    .forEach(c -> System.out.println(c.getName() + " (Votes: " + c.getVoteCount() + ")"));
        } catch (IOException e) {
            System.err.println("Failed to save voting state: " + e.getMessage());
        }
    }

    private void loadVotingState() {
        File file = new File("voting_state.ser");
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                List<User> users = (List<User>) ois.readObject();
                List<Candidate> candidates = (List<Candidate>) ois.readObject();
                departments = (List<String>) ois.readObject();
                currentDepartmentIndex = (int) ois.readObject();
                database = new Database();
                database.getUsers().clear();
                database.getUsers().addAll(users);
                database.getCandidates().clear();
                database.getCandidates().addAll(candidates);
                System.out.println("Loaded voting state with SE candidates:");
                database.getCandidates().stream()
                        .filter(c -> c.getDepartment().equals("SE"))
                        .forEach(c -> System.out.println(c.getName() + " (Votes: " + c.getVoteCount() + ")"));
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Failed to load voting state: " + e.getMessage());
                departments = database.getUsers().stream()
                        .map(User::getDepartment)
                        .distinct()
                        .collect(Collectors.toList());
                currentDepartmentIndex = 0;
            }
        }
    }
}
