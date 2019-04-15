package toolbar_menu.mypage;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import user.User;
import com.example.real_visittogether.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Ranking extends AppCompatActivity {
   // TextView rank = (TextView) findViewById(R.id.ranking_info);
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ranking);
      //  rank.setText("안녕하세요");
        //parseXML();
    }
    private void parseXML() {
        XmlPullParserFactory parserFactory;
        try {
            parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();
            InputStream is = getAssets().open("user.xml");
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(is, null);

            processParsing(parser);

        } catch (XmlPullParserException e) {

        } catch (IOException e) {
        }
    }
    private void processParsing(XmlPullParser parser) throws IOException, XmlPullParserException{
        ArrayList<User> users = new ArrayList<>();
        int eventType = parser.getEventType();
        User currentUser = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            String eltName = null;

            switch (eventType) {
                case XmlPullParser.START_TAG:
                    eltName = parser.getName();

                    if ("user".equals(eltName)) {
                        currentUser = new User();
                       users.add(currentUser);
                    } else if (currentUser != null) {
                        if ("name".equals(eltName)) {
                            currentUser.name = parser.nextText();
                        } else if ("age".equals(eltName)) {
                            currentUser.num = Integer.parseInt(parser.nextText());
                        }
                    }
                    break;
            }

            eventType = parser.next();
        }

        printUsers(users);
    }

    private void printUsers(ArrayList<User> users) {
        StringBuilder builder = new StringBuilder();

        for (User user : users) {
            builder.append(user.name).append("\n").
                    append(user.num).append("\n\n");

        }

       // rank.setText(builder.toString());
    }
}
