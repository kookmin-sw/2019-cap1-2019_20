package user;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.real_visittogether.R;

import java.util.ArrayList;

public class UserAdaptor extends ArrayAdapter {

    public UserAdaptor(Context context, ArrayList users){
        super(context, 0, users);

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_user, parent, false);
        }

        // Get the data item for this position
        User user = (User)getItem(position);

        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvHome = (TextView) convertView.findViewById(R.id.tvHome);
        // Populate the data into the template view using the data object

        tvName.setText(user.getName());
        tvHome.setText(String.valueOf(user.getNum()));
        // Return the completed view to render on screen
        return convertView;
    }
}
