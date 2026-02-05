public class Evaluation {
    private String presenterName;
    private int problemClarity;
    private int methodology;
    private int results;
    private int presentation;
    private String comments;

    public Evaluation(String presenterName, int problemClarity, int methodology, int results, int presentation, String comments){
        this.presenterName = presenterName;
        this.problemClarity = problemClarity;
        this.methodology = methodology;
        this.results = results;
        this.presentation = presentation;
        this.comments = comments;
    }

    public int getTotalScore(){
        return problemClarity + methodology + results + presentation;
    }

    public String getPrenterName(){
        return presenterName;
    }
}
