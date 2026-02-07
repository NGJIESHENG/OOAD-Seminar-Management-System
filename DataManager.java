import java.util.ArrayList;

public class DataManager {
   
    public static ArrayList<Session> allSessions = new ArrayList<>();
    public static ArrayList<Submission> allSubmissions = new ArrayList<>();
    public static ArrayList<Evaluation> allEvaluations = new ArrayList<>();
    public static ArrayList<Assignment> allAssignments = new ArrayList<>(); 

    public static String currentUser = "Guest"; 
    public static String[] mockEvaluators = {"Dr. Lim", "Dr. Tan", "Prof. Ahmed", "Dr. Sarah"};

    
    public static Evaluation getBestPresenter(String type){
        Evaluation winner = null;
        int highestScore = -1;

        for (Evaluation eval : allEvaluations){
            String studentType = "";
            for(Submission s : allSubmissions) {
                if(s.getStudentName().equals(eval.getPresenterName())) {
                    studentType = s.getPresentationType();
                    break;
                }
            }
            if (studentType != null && studentType.contains(type)) {
                if (eval.getTotalScore() > highestScore){
                    highestScore = eval.getTotalScore();
                    winner = eval;
                }
            }
        }
        return winner;
    }

    public Evaluation getBestOralPresenter() { return getBestPresenter("Oral"); }

   
    public static Submission getPeoplesChoiceWinner() {
        Submission winner = null;
        int maxVotes = -1;
        for (Submission s : allSubmissions) {
            if (s.getVoteCount() > maxVotes && s.getVoteCount() > 0) {
                maxVotes = s.getVoteCount();
                winner = s;
            }
        }
        return winner;
    }

    
    public static String generateAwardAgenda() {
        StringBuilder sb = new StringBuilder();
        sb.append("=========================================\n");
        sb.append("      FCI SEMINAR AWARD CEREMONY AGENDA  \n");
        sb.append("=========================================\n\n");
        sb.append("14:00 - Opening Speech by Dean\n");
        sb.append("14:15 - Keynote Session\n");
        sb.append("15:00 - Tea Break\n");
        sb.append("15:30 - AWARD PRESENTATION:\n\n");
        
        Evaluation bestOral = getBestPresenter("Oral");
        sb.append("   1. BEST ORAL PRESENTATION\n");
        if(bestOral != null) sb.append("      Winner: ").append(bestOral.getPresenterName()).append("\n");
        else sb.append("      Winner: To be announced\n");

        Evaluation bestPoster = getBestPresenter("Poster");
        sb.append("\n   2. BEST POSTER PRESENTATION\n");
        if(bestPoster != null) sb.append("      Winner: ").append(bestPoster.getPresenterName()).append("\n");
        else sb.append("      Winner: To be announced\n");

        Submission peopleChoice = getPeoplesChoiceWinner();
        sb.append("\n   3. PEOPLE'S CHOICE AWARD\n");
        if(peopleChoice != null) sb.append("      Winner: ").append(peopleChoice.getStudentName()).append("\n");
        else sb.append("      Winner: To be announced\n");
        
        sb.append("\n16:30 - Closing Ceremony & Photography\n");
        return sb.toString();
    }

    public static String generateFullSeminarReport(){
        StringBuilder report = new StringBuilder();
        report.append("=========================================\n");
        report.append("   FCI SEMINAR MANAGEMENT SYSTEM REPORT  \n");
        report.append("=========================================\n\n");

        report.append("--- 1. DATA ANALYTICS ---\n");
        int totalOral = 0, totalPoster = 0;
        for(Submission s : allSubmissions) {
            if(s.getPresentationType().contains("Oral")) totalOral++;
            else totalPoster++;
        }
        report.append("Total Submissions: ").append(allSubmissions.size()).append("\n");
        report.append("   - Oral: ").append(totalOral).append("\n");
        report.append("   - Poster: ").append(totalPoster).append("\n");
        
        report.append("\n--- 2. AWARD WINNERS ---\n");
        Evaluation bestOral = getBestPresenter("Oral");
        if(bestOral != null) report.append("Best Oral: ").append(bestOral.getPresenterName()).append("\n");
        
        Evaluation bestPoster = getBestPresenter("Poster");
        if(bestPoster != null) report.append("Best Poster: ").append(bestPoster.getPresenterName()).append("\n");

        Submission people = getPeoplesChoiceWinner();
        if(people != null) report.append("People's Choice: ").append(people.getStudentName()).append("\n");

        report.append("\n--- 3. DETAILED SUBMISSION LIST ---\n");
        for (Submission sub : allSubmissions){
            String extra = sub.getPresentationType().contains("Poster") ? " [Board: " + sub.getBoardID() + "]" : "";
            report.append(String.format("- %s [%s]%s: \"%s\"\n", 
                sub.getStudentName(), sub.getPresentationType(), extra, sub.getTitle()));
        }
        
        return report.toString(); 
    }

    public static String generateParticipantList(){
        if (allSubmissions.isEmpty()) return "No participants registered yet.";

        StringBuilder sb = new StringBuilder("=== REGISTERED PARTICIPANTS ===\\n\\n");
        int count = 1;
        for (Submission s : allSubmissions){
            sb.append(count++).append(". ")
                .append(s.getStudentName())
                .append(" - ").append(s.getTitle()).append("\n");
        }
        return sb.toString();
    }

    public static String generateSessionSchedule(){
        if (allSessions.isEmpty()) return "No sessions created yet.";
        
        StringBuilder sb = new StringBuilder ("=== SEMINAR SESSION SCHEDULE ===\\n\\n");
        for (Session s : allSessions){
            sb.append("Date: ").append(s.getDate())
                .append(" | Venue: ").append(s.getVenue())
                .append(" | Type: ").append(s.toString()).append("\n");
        }

        sb.append("Presenters:\n");
        boolean hasPresenters = false;
        for (Submission sub : allSubmissions) {
            if (sub.getPresentationType().contains(sb.toString().split("-")[1].split("\\(")[0])){
                sb.append("  -").append(sub.getStudentName()).append(": ").append(sub.getTitle()).append("\n");
                hasPresenters = true;
            }
            if (!hasPresenters) sb.append("  - (No presenters registered for this type yet)\n");
            sb.append("------------------------------------------------\n");
        }
        return sb.toString();
    }
}