public class Assignment {
    private Submission submission;
    private String evaluatorName;

    public Assignment(Submission submission, String evaluatorName) {
        this.submission = submission;
        this.evaluatorName = evaluatorName;
    }

    public Submission getSubmission() {
        return submission;
    }

    public String getEvaluatorName() {
        return evaluatorName;
    }
    
    // Helper to display in lists if needed
    @Override
    public String toString() {
        return submission.getTitle() + " (Evaluator: " + evaluatorName + ")";
    }
}