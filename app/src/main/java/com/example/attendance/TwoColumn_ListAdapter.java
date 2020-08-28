package com.example.attendance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class TwoColumn_ListAdapter extends ArrayAdapter<User> {

    private LayoutInflater layoutInflater;
    private ArrayList<User> users;
    private int viewResourceId;

    public TwoColumn_ListAdapter(Context context, int textViewResourceId, ArrayList<User> users) {
        super(context, textViewResourceId, users);

        this.users = users;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        viewResourceId = textViewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parents) {

        convertView = layoutInflater.inflate(viewResourceId, null);

        User user = users.get(position);

        if(user != null) {

            TextView date = (TextView) convertView.findViewById(R.id.dateText);
            TextView present = (TextView) convertView.findViewById(R.id.presentText);
            date.setText(user.getDate());
            present.setText(user.getPresent());
        }

        return convertView;
    }
}
