package login;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Register {

    final private String strURL = "http://ec2-13-209-22-178.ap-northeast-2.compute.amazonaws.com:8888/";
    private String postData;

    public Register(){
        postData = "";
    }

    public String registerEvent(String event_name, String reward, String user_id){

        postData = "event_name=" + event_name + "&" + "reward=" + reward + "&" + "user_id=" + user_id;
        return register(postData, "insert_event/");
    }

    public String registerPlace(String place_name, String address, String information){
        System.out.println("Register.registerPlace() 실행");
        postData = "place_name=" + place_name + "&" + "address=" + address + "&" + "explanation=" + information;
        return register(postData, "insert_place/");
    }

    public String registerImply(int event_id, int place_id){

        postData = "event_id=" + event_id + "&" + "place_id=" + place_id;
        return register(postData, "insert_imply/");
    }

    public void participate(String user_ID, int event_ID){
        postData = "user_id=" + user_ID + "&" + "event_id=" + event_ID;
        register(postData, "participate_event/");
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
    public  void idDuplicateCheck(String user_id)
    {
        postData = "user_id="+user_id;
        register(postData,"id_duplicate_check/");
    }

    public String registerImage(String imageString){
        System.out.println("Register.registerImage() 실행!!");
        postData = "picture=" + imageString;
        return register(postData, "insert_picture/");
    }

    public String requestPlaces(int event_id) {
        postData = "event_id=" + event_id;
        return register(postData, "place/");
    }

    public String requestPlaces(int[] tempPlaces) {
        postData = "temp_places_size=" + tempPlaces.length;
        for(int i = 0; i < tempPlaces.length; i++)
            postData += "&" + "temp_places_id" + i + "=" + tempPlaces[i];
        return register(postData, "place/");
    }

    private String register(String postData, String _url){

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

            System.out.printf("반응코드: %d\n", conn.getResponseCode());

            String result = readStream(conn.getInputStream());
            conn.disconnect();

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
