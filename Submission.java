public class Submission {
    private String title;
    private String researchAbstract;
    private String supervisor;
    private String presentationType;

    public Submission (String title, String researchAbstract, String supervisor, String presentationType){
        this.title = title;
        this.researchAbstract = researchAbstract;
        this.supervisor = supervisor;
        this.presentationType = presentationType;
    }

    public String getTitle() {return title;}
    public String getPresentationType() {return presentationType;}
}
