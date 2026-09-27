package application;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Database {
    private List<User> users;
    private List<Candidate> candidates;

    public Database() {
        users = new ArrayList<>(Arrays.asList(
            new User("1001", "Shiban", "SE", false),
            new User("1002", "Khaled", "SE", false),
            new User("1003", "Ziad", "SE", false),
            new User("1004", "Ahmad", "SE", false),
            new User("1005", "Ali", "SE", false),
            new User("1006", "Eve", "EE", false),
            new User("1007", "Frank", "EE", false),
            new User("1008", "Grace", "EE", false),
            new User("1009", "Hannah", "ME", false),
            new User("1010", "Ian", "ME", false),
            new User("1011", "Jack", "ME", false)
        ));

        candidates = new ArrayList<>(Arrays.asList(
            // CS Candidates 
            new CandidateInformation("Özge YÜCEL", "SE", "Experienced in coding", "https://akademik.bahcesehir.edu.tr/web/ozgeyucelkasap", "https://akademik.bahcesehir.edu.tr/web/ozgeyucelkasap/tr/images/Bitmap.jpg"),
            new CandidateInformation("Derya BODUR", "SE", "Great team leader", "https://akademik.bahcesehir.edu.tr/web/deryabodur", "https://akademik.bahcesehir.edu.tr/web/deryabodur/tr/images/Bitmap.jpg"),
            new CandidateInformation("Duygu ÇAKIR", "SE", "Innovative thinker", "https://akademik.bahcesehir.edu.tr/web/duygucakir", "https://akademik.bahcesehir.edu.tr/web/duygucakir/tr/images/Bitmap.jpg"),
            new CandidateInformation("Merve ARITÜRK", "SE", "Strong communicator", "https://akademik.bahcesehir.edu.tr/web/merveariturk", "https://akademik.bahcesehir.edu.tr/web/merveariturk/tr/images/Bitmap.jpg"),
            new CandidateInformation("Simge AKAY", "SE", "Problem solver", "https://akademik.bahcesehir.edu.tr/web/simgeakaytemur", "https://akademik.bahcesehir.edu.tr/web/simgeakaytemur/tr/images/Bitmap.jpg"),
            new CandidateInformation("Tamer UÇAR", "SE", "Creative designer", "https://akademik.bahcesehir.edu.tr/web/tamerucar", "https://akademik.bahcesehir.edu.tr/web/tamerucar/tr/images/Bitmap.jpg"),
            // EE Candidates
            new CandidateInformation("Cand1_EE", "EE", "Circuit expert", "https://example.com/cand1_ee", "file:images/cand1_ee.jpg"),
            new CandidateInformation("Cand2_EE", "EE", "Power systems guru", "https://example.com/cand2_ee", "file:images/cand2_ee.jpg"),
            new CandidateInformation("Cand3_EE", "EE", "Hardware enthusiast", "https://example.com/cand3_ee", "file:images/cand3_ee.jpg"),
            new CandidateInformation("Cand4_EE", "EE", "Team motivator", "https://example.com/cand4_ee", "file:images/cand4_ee.jpg"),
            new CandidateInformation("Cand5_EE", "EE", "Analytical mind", "https://example.com/cand5_ee", "file:images/cand5_ee.jpg"),
            new CandidateInformation("Cand6_EE", "EE", "Project manager", "https://example.com/cand6_ee", "file:images/cand6_ee.jpg"),
            // ME Candidates
            new CandidateInformation("Cand1_ME", "ME", "Mechanical genius", "https://example.com/cand1_me", "file:images/cand1_me.jpg"),
            new CandidateInformation("Cand2_ME", "ME", "Design specialist", "https://example.com/cand2_me", "file:images/cand2_me.jpg"),
            new CandidateInformation("Cand3_ME", "ME", "Materials expert", "https://example.com/cand3_me", "file:images/cand3_me.jpg"),
            new CandidateInformation("Cand4_ME", "ME", "Efficient planner", "https://example.com/cand4_me", "file:images/cand4_me.jpg"),
            new CandidateInformation("Cand5_ME", "ME", "Practical innovator", "https://example.com/cand5_me", "file:images/cand5_me.jpg"),
            new CandidateInformation("Cand6_ME", "ME", "Strong leader", "https://example.com/cand6_me", "file:images/cand6_me.jpg")
        ));
    }

    public List<User> getUsers() { return users; }
    public List<Candidate> getCandidates() { return candidates; }
    public void updateUser(User updatedUser) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(updatedUser.getId())) {
                users.set(i, updatedUser);
                break;
            }
        }
    }
}