package login;

import android.content.Intent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class Register {

    final private String strURL = "http://ec2-13-209-22-178.ap-northeast-2.compute.amazonaws.com:8888/";
    private String postData;

    public void registerEvent(String event_name, String reward, String user_id){

        postData = "event_name=" + event_name + "&" + "reward=" + reward + "&" + "user_id=" + user_id;
        register(postData, "insert_event/");
    }

    public void registerPlace(String place_name, String address, String information){

        postData = "place_name=" + place_name + "&" + "address=" + address + "&" + "explanation=" + information;
        register(postData, "insert_place/");
    }

    public String participate(String user_ID, int event_ID){
        String result;
        postData = "user_id=" + user_ID + "&" + "event_id=" + event_ID;
        return register(postData, "participate_event/");

    }
    public void registerUser(String user_id, String user_information)
    {

       postData = "user_id="+user_id+"&"+"user_information="+user_information;
        register(postData,"insert_user/");
    }
    public void registerUser(String user_id,String password, String user_information)
    {

        postData = "user_id="+user_id+"&"+"user_password="+password+"&"+"user_information="+user_information;
        register(postData,"insert_user_direct/");
    }
    public  String idDuplicateCheck(String user_id)
    {
        postData = "user_id="+user_id;
        return register(postData,"id_duplicate_check/");
    }
    public  String login(String user_id, String password)
    {
        postData = "user_id="+user_id+"&user_password="+password;
        return register(postData,"login/");
    }
    public String ranking(int event_id){
        postData = "event_id="+String.valueOf(event_id);
        return register(postData,"ranking/");
    }
    public String auth(int place_id, int auth_num,double latitude, double longitude)
    {
        postData = "place_id="+place_id+"&auth_num="+auth_num+"&latitude="+latitude+"&longitude="+longitude;
        return register(postData,"auth/");
    }

    //인증 DB로 보내는 부분 수정 GPS / QR / Beacon
    public String auth_info(int place_id, int auth_num, double latitude , double longitutde, String user_id, int event_id)
    {
        postData = "place_id="+place_id+"&auth_num="+auth_num+"&latitude="+latitude +"&longitude="+longitutde+"&user_id="+user_id+"&event_id="+event_id;
        System.out.println(postData);
        return register(postData,"auth/");
    }

    public String auth_info(int place_id, int auth_num, String qr_message, String user_id, int event_id)
    {
        postData = "place_id="+place_id+"&auth_num="+auth_num +"&qr_message="+qr_message+"&user_id="+user_id+"&event_id="+event_id;
        return register(postData,"auth/");
    }

    public String auth_info(int place_id, int auth_num, double beacon_distance, String user_id, int event_id)
    {
        postData = "place_id="+place_id+"&auth_num="+auth_num+"&beacon_distance="+beacon_distance+"&user_id="+user_id+"&event_id="+event_id;
        return register(postData,"auth/");
    }



    private String register(String postData, String _url) {

        try {
            URL url = new URL(strURL + _url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(5000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(postData.getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();

            //System.out.printf("반응코드: %d\n", conn.getResponseCode());
            String result;
            InputStream is = conn.getInputStream();
            StringBuilder builder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line;
            while((line = reader.readLine()) != null)
                builder.append(line + "\n");

            result = builder.toString();
            conn.disconnect();
            System.out.println(result);
            result = result.trim();
            return result;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String readStream(InputStream in) throws IOException {
        StringBuilder jsonHtml = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        String line = null;

        while((line = reader.readLine()) != null)
            jsonHtml.append(line);

        reader.close();
        return jsonHtml.toString();
    }
}
