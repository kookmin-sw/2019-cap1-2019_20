/*
 * Place 엔티티
 * */
package vt_object;

public class Place {

    private int place_id;
    private String place_name;
    private String address;
    private String explanation;
    private String qr_message;
    private String coordinate;


    public Place(){
        place_id = 0;
        place_name = null;
        address = null;
        explanation = null;
        qr_message = null;
        coordinate = null;
    }

    public int getId() {
        return place_id;
    }

    public String getName() {
        return place_name;
    }
}
