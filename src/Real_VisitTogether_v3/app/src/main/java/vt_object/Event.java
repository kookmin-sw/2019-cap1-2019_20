package vt_object;

public class Event {

    private int id;
    private String name;

    public Event() {
        id = 0;
        name = null;
    }

    public Event(int _id, String _name){
        id = _id;
        name = _name;
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
