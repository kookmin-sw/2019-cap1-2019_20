package data_fetcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Register {

    private String strURL = "http://ec2-13-209-22-178.ap-northeast-2.compute.amazonaws.com:8888/";
    private String postData;

    public void registerEvent(String event_name, String reward, String user_id){

        postData = "event_name=" + event_name + "&" + "reward=" + reward + "&" + "user_id=" + user_id;
        register(postData, "insert_event/");
    }

    public void registerPlace(String place_name, String address, String information){

        postData = "place_name=" + place_name + "&" + "address=" + address + "&" + "explanation=" + information;
        register(postData, "insert_place/");
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
