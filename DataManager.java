import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class DataManager {
    // === CENTRAL DATA STORAGE ===
    public static ArrayList<Session> allSessions = new ArrayList<>();
    public static ArrayList<Submission> allSubmissions = new ArrayList<>();
    public static ArrayList<Evaluation> allEvaluations = new ArrayList<>();
    
    // Links between Students and Evaluators
    public static ArrayList<Assignment> allAssignments = new ArrayList<>(); 

    // === SESSION MANAGEMENT ===
    public static String currentUser = "Guest"; 
    
    // === MOCK DATA ===
    public static String[] mockEvaluators = {"Dr. Lim", "Dr. Tan", "Prof. Ahmed", "Dr. Sarah"};

    // === LOGIC METHODS ===

    // Helper: Find best presenter by type (Oral or Poster)
    public static Evaluation getBestPresenter(String type){
        Evaluation winner = null;
        int highestScore = -1;

        for (Evaluation eval : allEvaluations){
            // Find the submission type for this student
            String studentType = "";
            for(Submission s : allSubmissions) {
                if(s.getStudentName().equals(eval.getPresenterName())) {
                    studentType = s.getPresentationType();
                    break;
                }
            }

            // Check if types match (Oral vs Poster)
            if (studentType.contains(type) && eval.getTotalScore() > highestScore){
                highestScore = eval.getTotalScore();
                winner = eval;
            }
        }
        return winner;
    }

    public static String generateFullSeminarReport(){
        StringBuilder report = new StringBuilder();
        report.append("=========================================\n");
        report.append("   FCI SEMINAR MANAGEMENT SYSTEM REPORT  \n");
        report.append("=========================================\n\n");

        // --- SECTION 1: SYSTEM STATISTICS (ANALYTICS) ---
        report.append("--- 1. DATA ANALYTICS ---\n");
        
        int totalOral = 0;
        int totalPoster = 0;
        for(Submission s : allSubmissions) {
            if(s.getPresentationType().contains("Oral")) totalOral++;
            else totalPoster++;
        }
        
        report.append("Total Submissions: ").append(allSubmissions.size()).append("\n");
        report.append("   - Oral Presentations: ").append(totalOral).append("\n");
        report.append("   - Poster Presentations: ").append(totalPoster).append("\n");
        report.append("Total Evaluations Completed: ").append(allEvaluations.size()).append("\n");
        
        // Calculate Averages
        if(!allEvaluations.isEmpty()) {
            double avgScore = 0;
            double avgClarity = 0;
            double avgMethod = 0;
            
            for(Evaluation e : allEvaluations) {
                avgScore += e.getTotalScore();
                // We'd need getters for individual scores in Evaluation.java to do precise breakdown
                // For now, we assume total score average
            }
            avgScore /= allEvaluations.size();
            report.append(String.format("Average Overall Score: %.2f / 40\n", avgScore));
            
            // Pass/Fail Rate (Assuming Pass is >= 20/40)
            int passed = 0;
            for(Evaluation e : allEvaluations) {
                if(e.getTotalScore() >= 20) passed++;
            }
            double passRate = ((double)passed / allEvaluations.size()) * 100;
            report.append(String.format("Pass Rate: %.1f%% (%d passed, %d failed)\n", 
                passRate, passed, (allEvaluations.size() - passed)));
        } else {
            report.append("Average Score: N/A (No evaluations yet)\n");
        }

        // --- SECTION 2: AWARD WINNERS ---
        report.append("\n--- 2. AWARD NOMINATIONS ---\n");
        
        Evaluation bestOral = getBestPresenter("Oral");
        if (bestOral != null){
            report.append("[BEST ORAL PRESENTER]\n")
                  .append("   Winner: ").append(bestOral.getPresenterName())
                  .append("\n   Score: ").append(bestOral.getTotalScore()).append("/40\n");
        } else {
            report.append("Best Oral Presenter: N/A\n");
        }

        Evaluation bestPoster = getBestPresenter("Poster");
        if (bestPoster != null){
            report.append("[BEST POSTER PRESENTER]\n")
                  .append("   Winner: ").append(bestPoster.getPresenterName())
                  .append("\n   Score: ").append(bestPoster.getTotalScore()).append("/40\n");
        } else {
            report.append("Best Poster Presenter: N/A\n");
        }

        // --- SECTION 3: DETAILED LISTS ---
        report.append("\n--- 3. SESSION SCHEDULE ---\n");
        if(allSessions.isEmpty()) report.append("No sessions scheduled.\n");
        for (Session s : allSessions){
            report.append(s.toString()).append("\n");
        }

        report.append("\n--- 4. DETAILED SUBMISSION LIST ---\n");
        for (Submission sub : allSubmissions){
            report.append(String.format("- %s [%s]: \"%s\"\n", 
                sub.getStudentName(), sub.getPresentationType(), sub.getTitle()));
        }
        
        report.append("\n--- END OF REPORT ---\n");
        return report.toString(); 
    }

    public static void saveReportToFile(String content, String fileName) {
        try (FileWriter writer = new FileWriter(fileName)){
            writer.write(content);
            System.out.println("Report saved successfully to " + fileName);
        } catch (IOException e){
            System.err.println("Error saving report: " + e.getMessage());
        }
    }
}