package com.example.zeroq;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
         ArrayList<Boolean> mcheck = null;
        LayoutInflater inflater = context.getLayoutInflater();
        dbhandler db = new dbhandler(getContext());
        View rowView = inflater.inflate(R.layout.mylayout, null, true);
        TextView tw1 = (TextView) rowView.findViewById(R.id.textView3);
        TextView tw2 = (TextView) rowView.findViewById(R.id.textView4);
        TextView tw3 = (TextView) rowView.findViewById(R.id.textView5);
        TextView tw4= (TextView) rowView.findViewById(R.id.textView6);
        CheckBox c= (CheckBox) rowView.findViewById(R.id.checkBox);
        String p_quantity=quantity.get(position);
        Integer Num_p_Quantity= Integer.parseInt(p_quantity);
        tw4.setText(p_quantity);
        String p_name = names.get(position);
        tw1.setText(p_name);
        String p_price =prices.get(position);
 Integer Num_p_price = Integer.parseInt(p_price);
 int total_amt=Num_p_price*Num_p_Quantity;
       // tw2.setText(Integer.toString(total_amt));
        tw2.setText(p_price);
        String p_code = code.get(position);
        tw3.setText(p_code);
MainActivity2.remove.setVisibility(View.INVISIBLE);
//c.setText("Unselected");
        c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    MainActivity2.remove.setVisibility(View.VISIBLE);
                    MainActivity2.remove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                          //  Toast.makeText(context, "Removed "+position, Toast.LENGTH_SHORT).show();
                            if(c.isChecked()){
                              // Toast.makeText(context, ""+code.get(position), Toast.LENGTH_SHORT).show();
                                db.deleteline(Integer.parseInt(code.get(position)));
                                names.remove(position);
                                prices.remove(position);
                                code.remove(position);
                                quantity.remove(position);
                                notifyDataSetChanged();


                                Intent intent = new Intent(context, scan_screen.class);
                                context.startActivity(intent);
                            }else {
                                Toast.makeText(context, "Nothing is selected", Toast.LENGTH_SHORT).show();
                            }



                        }

                    });


                }else {
                   MainActivity2.remove.setVisibility(View.INVISIBLE);
                }
            }
        });





        tw4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder=new AlertDialog.Builder(v.getRootView().getContext());
                View dialogView= LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.dialog_productlist,null);
                EditText type_NoOfQuantity;
                type_NoOfQuantity=dialogView.findViewById(R.id.dialog_type_NoOfQuantity);
                builder.setView(dialogView);
                builder.setCancelable(false);
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int Original_price = Num_p_price/Num_p_Quantity;
                        String after_No_quantity=type_NoOfQuantity.getText().toString();
                        Integer noofq= Integer.parseInt(after_No_quantity);
                        tw4.setText(Integer.toString(noofq));
                        int after_total_amt   = noofq * Original_price;
                        tw2.setText(Integer.toString(after_total_amt));
                        boolean isUpdate = db.update(p_code,Integer.toString(after_total_amt),Integer.toString(noofq));

                        if(isUpdate == true) {
                            Toast.makeText(context, "Quantity Updated", Toast.LENGTH_LONG).show();
                            Intent mIntent = new Intent(context, MainActivity2.class);
                            context.startActivity(mIntent);
                        } else
                            Toast.makeText(context,"Quantity Not Updated",Toast.LENGTH_LONG).show();

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
