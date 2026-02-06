public class Submission {
    private String title;
    private String researchAbstract;
    private String supervisor;
    private String presentationType;
    private String studentName; // Added field

    public Submission (String title, String researchAbstract, String supervisor, String presentationType, String studentName){
        this.title = title;
        this.researchAbstract = researchAbstract;
        this.supervisor = supervisor;
        this.presentationType = presentationType;
        this.studentName = studentName;
    }

    public String getTitle() { return title; }
    public String getPresentationType() { return presentationType; }
    public String getStudentName() { return studentName; }
}