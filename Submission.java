public class Submission {
    private String title;
    private String researchAbstract;
    private String supervisor;
    private String presentationType;
    private String studentName;
    
    // NEW FIELDS
    private String boardID;            // For Poster ID 
    private String presentationFilePath; // For uploaded file path 
    private int voteCount;             // For People's Choice Award 

    public Submission (String title, String researchAbstract, String supervisor, String presentationType, String studentName){
        this.title = title;
        this.researchAbstract = researchAbstract;
        this.supervisor = supervisor;
        this.presentationType = presentationType;
        this.studentName = studentName;
        this.boardID = "N/A";
        this.presentationFilePath = "Not Uploaded";
        this.voteCount = 0;
    }

    // Getters and Setters
    public String getTitle() { return title; }
    public String getPresentationType() { return presentationType; }
    public String getStudentName() { return studentName; }
    
    public String getBoardID() { return boardID; }
    public void setBoardID(String boardID) { this.boardID = boardID; }

    public String getPresentationFilePath() { return presentationFilePath; }
    public void setPresentationFilePath(String path) { this.presentationFilePath = path; }

    public int getVoteCount() { return voteCount; }
    public void addVote() { this.voteCount++; }

    @Override
    public String toString() {
        return title + " (" + studentName + ")";
    }
}