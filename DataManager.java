import java.util.ArrayList;

public class DataManager {
    public static ArrayList <Session> allSessions = new ArrayList<>();
    public static ArrayList <Submission> allSubmissions = new ArrayList<>();
    public static ArrayList <Evaluation> allEvaluations = new ArrayList<>();

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
}

