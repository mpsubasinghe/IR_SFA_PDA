package com.example.mano.sfa;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class RetailerAddActivity extends AppCompatActivity {
    Spinner spinner;
    TableLayout stk;
    String item;
    Button btn1;
    Button btn2;
    Button btn3;
    EditText name;
    EditText add;
    EditText tp;
    EditText VAT;
    String routid = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_add);
        setTitle("Add Retailers");

        name=(EditText)findViewById(R.id.editText1);
        add=(EditText)findViewById(R.id.editText2);
        tp=(EditText)findViewById(R.id.editText3);
        VAT=(EditText)findViewById(R.id.editText4);

        btn1= (Button)findViewById(R.id.button1);
        btn2= (Button)findViewById(R.id.button2);
        btn3= (Button)findViewById(R.id.button3);

        stk = (TableLayout) findViewById(R.id.table_main);
        spinner=(Spinner)findViewById(R.id.spinner1);


        name.setFocusable(true);
        name.setFocusableInTouchMode(true);///add this line
        name.requestFocus();


//        btn2.setEnabled(false);

        loadSpinnerData();

        btn1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub


                Intent intent = new Intent(RetailerAddActivity .this, MenucActivity .class);
                RetailerAddActivity.this.startActivity(intent);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub


                //sendSMS("0773403819","testing msg");
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (name.getText().toString().trim().equals("") || add.getText().toString().trim().equals(""))
                {
                    //Toast.makeText(getApplicationContext(), "Please Enter Retailer Information...???",Toast.LENGTH_LONG).show();
                    DialogBoxOneButton("Please Enter Retailer Information....???","Add Retailer");
                    return;
                }

//		if (name.getText().length()<31)
//		{
//			Toast.makeText(getApplicationContext(), "Please Enter Retailer Information...???",Toast.LENGTH_LONG).show();
//	   		return;
//		}
                DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                Cursor cursor2=db.GetC("select * from PDARetailer WHERE Ret_Name='"+name.getText().toString()+"' ");
                if( cursor2.getCount() > 0)
                {

                    //  Toast.makeText(getApplicationContext(), "Sorry..!!! Already Exsist This Retailer!!!!",Toast.LENGTH_LONG).show();
                    DialogBoxOneButton("Sorry..!!! Already Exsist This Retailer!!!!","Add Retailer");

                }else
                {
                    Integer id=0;
                    List<Integer> RETLIST = new ArrayList<Integer>();
                    Cursor cursor=db.GetC("select * from PDARetailer ORDER BY ID ASC ");
                    //Toast.makeText(getApplicationContext(),String.valueOf( cursor.getCount()),Toast.LENGTH_LONG).show();

                    if( cursor.getCount() > 0)
                    {

                        if (cursor.moveToFirst())

                        {
                            while(!cursor.isAfterLast())
                            {
                                RETLIST.add(Integer.valueOf(cursor.getString(cursor.getColumnIndex("ID"))));

                                cursor.moveToNext();
                            }
                        }
                    }

                    Collections.sort(RETLIST);
                    id=RETLIST.get(RETLIST.size() - 1) ;

                    //   Toast.makeText(getApplicationContext(),String.valueOf(id),Toast.LENGTH_LONG).show();

                    id=id+1;
                    MyGloble.db.execSQL("insert into PDARetailer values ('"+name.getText().toString()+"' ,'"+ add.getText().toString() +"','"+tp.getText().toString()+"','"+VAT.getText().toString()+"','','" +routid+"','"+id+"','-1','N','')" );
                    // MyGloble.db.execSQL("insert into PDARetailer values ('"+name.getText().toString()+"' ,'"+ add.getText().toString() +"','"+tp.getText().toString()+"','','','" +routid+"','"+id+"')" );
                    loadretailer(routid);
                    // Toast.makeText(getApplicationContext(),"New Retailer Successfully added...!!!",Toast.LENGTH_LONG).show();
                    DialogBoxOneButton("New Retailer is Successfully added...!!!","Add Retailer");

                    name.setText("");
                    add.setText("");
                    tp.setText("");
                    VAT.setText("");
                }

            }
        });
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int arg2, long arg3) {


                item = spinner.getSelectedItem().toString();

                DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                Cursor cursor=db.GetC("select * from PDARoute where RouteName='"+item.toString()+"' ");
                // Toast.makeText(getApplicationContext(), cursor.getString(2).toString(),Toast.LENGTH_LONG).show();
                while(cursor.moveToNext())
                {
                    routid=(cursor.getString(cursor.getColumnIndex("PDARouteID")));

                }

                stk.removeAllViews();
                //stk.removeAllViewsInLayout();

                stk.invalidate();
                stk.refreshDrawableState();
                loadretailer(routid);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

    }


    // Disabled Back Button On 4n
    @Override
    public void onBackPressed() {
    }


    public void loadretailer(String a)
    {
        stk.removeAllViews();
        TableRow tbrow0 = new TableRow(this);
        TextView tv0 = new TextView(this);
        tv0.setText("ID");
        tv0.setTextColor(Color.WHITE);
        tv0.setGravity(Gravity.LEFT);
        tv0.setTypeface(null, Typeface.BOLD);
        //     tv0.setWidth(0);
        tv0.setTextSize(0);
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(this);
        tv1.setText("Retailer Name                  ");
        tv1.setTextColor(Color.WHITE);
        tv1.setGravity(Gravity.LEFT);
        tv1.setTypeface(null, Typeface.BOLD);
        //      tv1.setWidth(200);
        tbrow0.addView(tv1);
        TextView tv2 = new TextView(this);
        tv2.setText("Address");
        tv2.setTextColor(Color.WHITE);
        tv2.setGravity(Gravity.LEFT);
        tv2.setTypeface(null, Typeface.BOLD);
        //      tv2.setWidth(300);
        //  tv2.setWidth(3);
        tbrow0.addView(tv2);

        tbrow0.setBackgroundColor(Color.BLACK);
        stk.addView(tbrow0);


        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        Cursor cursor=db.GetC("select * from PDARetailer WHERE PDARouteID='"+a+"' ORDER BY Ret_Name ");
        //Toast.makeText(getApplicationContext(),String.valueOf( cursor.getCount()),Toast.LENGTH_LONG).show();
        if( cursor.getCount() > 0)
        {
            if (cursor.moveToFirst()){
                while(!cursor.isAfterLast()){

                    // labels.add(cursor.getString(cursor.getColumnIndex("RName")));

                    TableRow tbrow = new TableRow(this);
                    TextView t1v = new TextView(this);
                    t1v.setText(cursor.getString(6));
                    t1v.setTextColor(Color.WHITE);
                    //  t1v.setGravity(Gravity.LEFT);
                    t1v.setTextSize(0);
                    tbrow.addView(t1v);
                    TextView t2v = new TextView(this);
                    t2v.setText(cursor.getString(0));
                    //  t2v.setTextColor(Color.WHITE);
                    t2v.setGravity(Gravity.LEFT);
                    tbrow.addView(t2v);
                    TextView t3v = new TextView(this);
                    // t3v.setText(cursor.getString(cursor.getColumnIndex("Ret_Address")));
                    if(cursor.getString(1).length()>20)
                    {
                        t3v.setText(cursor.getString(1).substring(0, 20));
                    }else{
                        t3v.setText(cursor.getString(1).substring(0, cursor.getString(1).length()));
                    }
                    //  t3v.setTextColor(Color.WHITE);
                    t3v.setGravity(Gravity.LEFT);
                    tbrow.addView(t3v);


                    tbrow.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            TableRow tr1=(TableRow)view;
                            TextView tv1= (TextView)tr1.getChildAt(0);
                            TextView tv2= (TextView)tr1.getChildAt(1);
                            TextView tv3= (TextView)tr1.getChildAt(2);

                            MyGloble.RID=(tv1.getText().toString());
                            MyGloble.RName=(tv2.getText().toString());
                            MyGloble.Radd=(tv3.getText().toString());

                            //   Toast.makeText(getApplicationContext(),tv2.getText().toString(),Toast.LENGTH_SHORT).show();
                            //   stk.removeAllViews();
                            // stk.removeView(tr1);
                            view.setBackgroundColor(Color.MAGENTA);


                        }
                    });


                    stk.addView(tbrow);

                    cursor.moveToNext();
                }
            }
        }

    }
    public void loadSpinnerData() {
        // database handler


        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        Cursor C = db.GetC("select * from PDARoute");
        List<String> labels = new ArrayList<String>();

        while(C.moveToNext())
        {
            labels.add(C.getString(1));
            //Toast.makeText(getApplicationContext(), C.getString(C.getColumnIndex("Ename")), Toast.LENGTH_LONG).show();
        }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, labels);

        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }
    public void sendSMS(String phoneNo, String msg) {
//        try {
//            SmsManager smsManager = SmsManager.getDefault();
//            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
//            Toast.makeText(getApplicationContext(), "Message Sent",
//                    Toast.LENGTH_LONG).show();
//        } catch (Exception ex) {
//            Toast.makeText(getApplicationContext(),ex.getMessage().toString(),
//                    Toast.LENGTH_LONG).show();
//            ex.printStackTrace();
//        }
    }
    public void DialogBoxOneButton(String msg,String head )
    {
        AlertDialog alertDialog = new AlertDialog.Builder(RetailerAddActivity.this).create();

        // Setting Dialog Title
        alertDialog.setTitle(head);

        // Setting Dialog Message
        alertDialog.setMessage(msg);

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.tick);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog,int which)
            {
                // Write your code here to execute after dialog closed
                //   Toast.makeText(getApplicationContext(),"You clicked on OK", Toast.LENGTH_SHORT).show();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
}
