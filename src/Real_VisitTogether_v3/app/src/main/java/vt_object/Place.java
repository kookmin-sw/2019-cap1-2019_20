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
    private String picture;
    //private int event_id;

    private String latitude;
    private String longitude ;



    public Place(){
        place_id = 0;
        place_name = null;
        address = null;
        explanation = null;
        qr_message = null;
        coordinate = null;
        picture = null;

        latitude = null;
        longitude = null;

    }

    public int getId() {
        return place_id;
    }

    public String getName() {
        return place_name;
    }


    public String getPicture() { return picture; }

    public void setPlace_id(int place_id) { this.place_id = place_id; }

    public void setPicture(String picture) { this.picture = picture; }

    public String getAddress() {
        return address;
    }

    public String getExplanation() {
        return explanation;
    }

    public String getLongitude(){
        return longitude;
    }

    public String getLatitude(){
        return latitude;
    }

}

