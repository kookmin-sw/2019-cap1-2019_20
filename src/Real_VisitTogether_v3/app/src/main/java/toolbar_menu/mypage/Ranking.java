package toolbar_menu.mypage;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import user.User;
import user.UserAdaptor;

import com.example.real_visittogether.R;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;






public class Ranking extends AppCompatActivity {

    ArrayList<User> user = new ArrayList<>();
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ranking);

        ActionBar ab = getSupportActionBar() ;
        ab.setTitle("랭킹") ;

        // Attach the adapter to a ListView
        ListView listView = (ListView) findViewById(R.id.listView);


        xmlParsing();
        Collections.sort(user);
        makeRank();
        UserAdaptor adapter = new UserAdaptor(this, user);
        listView = (ListView)findViewById(R.id.listView);
        listView.setAdapter(adapter);


    }

    public void  xmlParsing(){

        String[] data = null;

        try {
            InputStream is = getResources().openRawResource(R.raw.user);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(is);

            NodeList studentList = doc.getElementsByTagName("user");
            NodeList nameList = doc.getElementsByTagName("name");
            NodeList ageList = doc.getElementsByTagName("num");

            data = new String[studentList.getLength()];

            for(int i = 0; i < studentList.getLength(); i++) {
                String name = nameList.item(i).getFirstChild().getNodeValue();
                name = name.trim();
                int age = Integer.parseInt(ageList.item(i).getFirstChild().getNodeValue());
                user.add(new User(name,age));

            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();

        }

    }
    public void makeRank()
    {
        int idx = 1;
        for(User k : user)
        {
            k.setName(String.valueOf(idx)+". "+k.getName()+" : ");
            idx++;
        }
    }


}