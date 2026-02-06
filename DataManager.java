import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class DataManager {
    public static ArrayList<Session> allSessions = new ArrayList<>();
    public static ArrayList<Submission> allSubmissions = new ArrayList<>();
    public static ArrayList<Evaluation> allEvaluations = new ArrayList<>();
    public static ArrayList<Assignment> allAssignments = new ArrayList<>(); // NEW: Stores assignments

    // NEW: Session management
    public static String currentUser = "Guest"; 
    
    // NEW: Mock list of evaluators for the Coordinator dropdown
    // In a real app, this would be a list of User objects with role="Evaluator"
    public static String[] mockEvaluators = {"Dr. Lim", "Dr. Tan", "Prof. Ahmed", "Dr. Sarah"};

    public static Evaluation getBestOralPresenter(){
        Evaluation winner = null;
        int highestScore = -1;

        for (Evaluation eval : allEvaluations){
            if (eval.getTotalScore() > highestScore){
                highestScore = eval.getTotalScore();
                winner = eval;
            }
        }
        return winner;
    }

    public static String generateFullSeminarReport(){
        StringBuilder report = new StringBuilder();
        report.append("=== Seminar Management System Report === \n\n");

        report.append("--- SESSIONS ---\n");
        for (Session s : allSessions){
            report.append("Date: ").append(s.getDate()).append(" | Venue: ").append(s.getVenue()).append("\n");
        }

        report.append("\n--- SUBMISSIONS ---\n");
        for (Submission sub : allSubmissions){
            report.append("Presenter: ").append(sub.getStudentName())
                  .append(" | Title: ").append(sub.getTitle()).append("\n");
        }
        
        report.append("\n--- ASSIGNMENTS ---\n");
        for (Assignment a : allAssignments){
            report.append("Presenter: ").append(a.getSubmission().getStudentName())
                  .append(" assigned to Evaluator: ").append(a.getEvaluatorName()).append("\n");
        }

        report.append("\n--- EVALUATIONS AND AWARDS ---\n");
        Evaluation winner = getBestOralPresenter();
        if (winner != null){
            report.append("Best Oral Presenter: ").append(winner.getPresenterName()) 
                    .append(" with a score of ").append(winner.getTotalScore()).append("/40\n");
        } else {
            report.append("No evaluation recorded yet.\n");
        }

        return report.toString(); 
    }

    public static void saveReportToFile(String content,String fileName) {
        try (FileWriter writer = new FileWriter(fileName)){
            writer.write(content);
            System.out.println("Report saved successfully to " + fileName);
        }catch (IOException e){
            System.err.println("Error saving report: " + e.getMessage());
        }
    }
}