package com.example.zeroq;

;
import android.app.Activity;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;

public class customadapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> names;
    private final ArrayList<String> prices;
    private final ArrayList<String> code;
    private  final ArrayList<String> quantity;

    public customadapter(Activity context, ArrayList<String> name, ArrayList<String> price, ArrayList<String> code, ArrayList<String> quantity) {
        super(context, R.layout.mylayout, name);
        this.context = context;
        this.names = name;
        this.prices = price;
        this.code = code;
        this.quantity = quantity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        dbhandler db = new dbhandler(getContext());
        View rowView = inflater.inflate(R.layout.mylayout, null, true);
        TextView tw1 = (TextView) rowView.findViewById(R.id.textView3);
        TextView tw2 = (TextView) rowView.findViewById(R.id.textView4);
        TextView tw3 = (TextView) rowView.findViewById(R.id.textView5);
        TextView tw4= (TextView) rowView.findViewById(R.id.textView6);

        String p_quantity=quantity.get(position);
        Integer Num_p_Quantity= Integer.parseInt(p_quantity);
        tw4.setText(p_quantity);
        String p_name = names.get(position);
        tw1.setText(p_name);
        String p_price =prices.get(position);
 Integer Num_p_price = Integer.parseInt(p_price);
 int total_amt=Num_p_price*Num_p_Quantity;
        tw2.setText(Integer.toString(total_amt));
        String p_code = code.get(position);
        tw3.setText(p_code);




        tw4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // AlertDialog.Builder builder= new AlertDialog.Builder(this);
                AlertDialog.Builder builder=new AlertDialog.Builder(v.getRootView().getContext());
                View dialogView= LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.dialog_productlist,null);
                EditText type_NoOfQuantity;
                type_NoOfQuantity=dialogView.findViewById(R.id.dialog_type_NoOfQuantity);
                builder.setView(dialogView);
                builder.setCancelable(false);


                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String After_p_price =prices.get(position);
                        Integer after_Num_p_price = Integer.parseInt(After_p_price);
                        String after_No_quantity=type_NoOfQuantity.getText().toString();

                        Integer noofq= Integer.parseInt(after_No_quantity);

                        tw4.setText(Integer.toString(noofq));
                        int after_total_amt   = noofq * after_Num_p_price;
                        tw2.setText(Integer.toString(after_total_amt));

                       db.addlist(p_code,p_name, String.valueOf(after_total_amt),after_No_quantity);
                        Toast.makeText(context, "Successfully quantity is changed", Toast.LENGTH_SHORT).show();
                    }

                });
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();

            }
        });




        return rowView;

    }

}
