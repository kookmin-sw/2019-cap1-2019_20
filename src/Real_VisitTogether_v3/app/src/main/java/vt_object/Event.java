/*
 * Event 엔티티
 * */
package vt_object;

public class Event {

    private int event_ID;
    private String event_name;
    private String reward;
    private String user_ID;

    public String getName() {
        return event_name;
    }
    public  int getEvent_ID(){
        return event_ID;
    }
}
