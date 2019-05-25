package data_fetcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class RequestHttpConnection {

    private String strURL = "http://ec2-13-209-22-178.ap-northeast-2.compute.amazonaws.com:8888/";
    private String result;

    public String request(String _url){

        try{
            //연결 객체
            URL url = new URL(strURL + _url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //연결설정
            conn.setRequestMethod("GET");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setDefaultUseCaches(false);

            //읽어오기
            InputStream is = conn.getInputStream();
            StringBuilder builder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            String line;
            while((line = reader.readLine()) != null)
                builder.append(line + "\n");

            result = builder.toString();
            System.out.printf("\n응답코드: %d\n응답메세지: %s\n", conn.getResponseCode(), conn.getResponseMessage());


            //URL protocol의 형식이 잘못되었다는 예외. http://를 붙여줘야 한다.
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}

