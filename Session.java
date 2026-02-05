public class Session {
    private String date;
    private String venue;
    private String type;
    private String timeSlot;
    private int maxPresenters;

    public Session (String date, String venue, String type, String timeSlot, int maxPresenters){
        this.date = date;
        this.venue = venue;
        this.type = type;
        this.timeSlot = timeSlot;
        this.maxPresenters = maxPresenters;
    }

    @Override
    public String toString (){
        return date + "-" + type + "(" + venue + ")";
    }
}
