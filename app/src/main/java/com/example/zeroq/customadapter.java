package com.example.zeroq;

;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class customadapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> names;
    private final ArrayList<String> prices;
    private final ArrayList<String> code;

    public customadapter(Activity context, ArrayList<String> name, ArrayList<String> price, ArrayList<String> code) {
        super(context, R.layout.mylayout, name);
        this.context = context;
        this.names = name;
        this.prices = price;
        this.code = code;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.mylayout, null, true);
        TextView tw1 = (TextView) rowView.findViewById(R.id.textView3);
        TextView tw2 = (TextView) rowView.findViewById(R.id.textView4);
        TextView tw3 = (TextView) rowView.findViewById(R.id.textView5);


        String p_name = names.get(position);
        tw1.setText(p_name);
        String p_price = prices.get(position);
        tw2.setText(p_price);
        String p_code = code.get(position);
        tw3.setText(p_code);

        return rowView;

    }

}
