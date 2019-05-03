/*
* Event 엔티티와 Place 엔티티의 관계 엔티티
* */
package vt_object;

public class Imply {

    private int event_ID;
    private int place_ID;

    public Imply(){
        event_ID = 0;
        place_ID = 0;
    }

    public int getEvent_id() {
        return event_ID;
    }

    public int getPlace_id() {
        return place_ID;
    }
}
