package user;

public class UserRanking {
    private  String user_id;
    private String event_id;
    private String number_of_visits;

    public UserRanking(String user_id, String event_id, String number_of_visits)
    {
        this.event_id = event_id;
        this.user_id = user_id;
        this.number_of_visits = number_of_visits;
    }
    public String getUser_id() {
        return user_id;
    }
    public String getEvent_id() {
        return event_id;
    }
    public String getNumber_of_visits() {
        return number_of_visits;
    }
    public void setUser_id(String user_id)
    {
        this.user_id = user_id;
    }

}
