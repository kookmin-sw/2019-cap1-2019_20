/*
* Event 엔티티와 Place 엔티티의 관계 엔티티
* */
package vt_object;

public class EventHasPlace {

    private int event_id;
    private int place_id;

    public EventHasPlace(){
        event_id = 0;
        place_id = 0;
    }

    public int getEvent_id() {
        return event_id;
    }

    public int getPlace_id() {
        return place_id;
    }
}
