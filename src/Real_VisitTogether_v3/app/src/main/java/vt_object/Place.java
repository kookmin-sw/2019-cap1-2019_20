/*
 * Place 엔티티
 * */
package vt_object;

public class Place {

    private int place_ID;
    private String place_name;
    private String address;
    private String explanation;
    private String QR_message;
    private String coordinate;


    public Place(){
        place_ID = 0;
        place_name = null;
        address = null;
        explanation = null;
        QR_message = null;
        coordinate = null;
    }

    public int getId() {
        return place_ID;
    }

    public String getName() {
        return place_name;
    }
}
