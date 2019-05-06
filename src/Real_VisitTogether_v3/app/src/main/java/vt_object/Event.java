/*
 * Event 엔티티
 * */
package vt_object;

public class Event {

    private int event_id;
    private String event_name;
    private String reward;
    private String user_id;

    public Event() {
        event_id = 0;
        event_name = null;
        reward = null;
        user_id = null;
    }

    public String getName() {
        return event_name;
    }
}
