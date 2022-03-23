package com.example.mano.sfa;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class RetailerActivity extends AppCompatActivity {
    Spinner spinner;
    TableLayout stk;
    String item;
    String routid = "";
    Button btn1;
    //	Button btn2;
    EditText Search;

    TextView invoiceid;

    Integer invid=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer);

        setTitle("Select Retailer");

        btn1= (Button)findViewById(R.id.button1);
//		btn2= (Button)findViewById(R.id.button2);
        Search=(EditText)findViewById(R.id.editText1);
        btn1.setFocusable(true);
        btn1.setFocusableInTouchMode(true);///add this line
        btn1.requestFocus();

        invoiceid=(TextView)findViewById(R.id .textView4);

        stk = (TableLayout) findViewById(R.id.table_main);
        spinner=(Spinner)findViewById(R.id.spinner1);




        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
//        db.ClearDatatable();
//        db.ClearValues();
        //  Toast.makeText(getApplicationContext(), "hhhhhhhhhhh",Toast.LENGTH_LONG).show();

        GetInvvNO();
        loadSpinnerData();

        Search.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if(!s.equals("") )
                { //do your work here }
                    stk.removeAllViews();
                    loadretailer(routid + " AND Ret_Name LIKE '"+Search.getText().toString()+"%'");
                    //	Toast.makeText(getApplicationContext(), routid,Toast.LENGTH_LONG).show();

                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s) {

            }
        });



        btn1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub


                Intent intent = new Intent(RetailerActivity.this, InvList.class);
                RetailerActivity.this.startActivity(intent);
            }
        });

        Search.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if(!s.equals("") )
                { //do your work here }
                    stk.removeAllViews();
                    loadretailer(routid + " AND Ret_Name LIKE '"+Search.getText().toString()+"%'");
                    //	Toast.makeText(getApplicationContext(), routid,Toast.LENGTH_LONG).show();

                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s) {

            }
        });





        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position,
                                       long id) {
                // On selecting a spinner item



                item = parent.getItemAtPosition(position).toString();
                //Toast.makeText(getApplicationContext(),item,Toast.LENGTH_LONG).show();
                // Showing selected spinner item


                DatabaseHandler db = new DatabaseHandler(getApplicationContext());

                Cursor cursor=db.GetC("select * from PDARoute where RouteName='"+item.toString()+"' ");
                // Toast.makeText(getApplicationContext(), cursor.getString(2).toString(),Toast.LENGTH_LONG).show();
                while(cursor.moveToNext())
                {
                    //labels.add(Cursor.getString(1));
                    //Toast.makeText(getApplicationContext(), cursor.getString(cursor.getColumnIndex("SP")), Toast.LENGTH_LONG).show();
                    routid=(cursor.getString(cursor.getColumnIndex("PDARouteID")));


                }

                stk.removeAllViews();
                //stk.removeAllViewsInLayout();

                stk.invalidate();
                stk.refreshDrawableState();
                loadretailer(routid);



            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });
    }


    // Disabled Back Button On 4n
    @Override
    public void onBackPressed() {
    }


    public void GetInvvNO()
    {
        invid=0;
        String STKID="";
        Integer LastInvNo=0;
        String REPID="";
        String date ="";
        String invoicePreFix ="";

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        date = df.format(Calendar.getInstance().getTime());
        //Toast.makeText(getApplicationContext(), "AAAAAAAAAAAAAA" ,Toast.LENGTH_LONG).show();
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        Cursor cursor2=db.GetC("select * from PDARep  Where LoginName<>''   ");
        if( cursor2.getCount() > 0)
        {
            if (cursor2.moveToFirst()){
                while(!cursor2.isAfterLast()){

                    REPID=cursor2.getString(0);
                    STKID=cursor2.getString(14);
                    if(cursor2.getString(15).length()>0)
                    {
//                        LastInvNo=Integer.valueOf(cursor2.getString(15).substring(3,cursor2.getString(15).length()) );
                        LastInvNo=Integer.valueOf(cursor2.getString(15) );
                    }
//                    invoicePreFix=cursor2.getString(15).substring(0,3);
                    cursor2.moveToNext();
                }
            }

        }

        List<Integer> INVOLIST = new ArrayList<Integer>();
        Cursor cursor=db.GetC("select InvoiceID  from PDAInvoice ORDER BY InvoiceID "); //Group by InvID ORDER BY InvID
        //Toast.makeText(getApplicationContext(),String.valueOf( cursor.getCount()),Toast.LENGTH_LONG).show();
        if( cursor.getCount()>0)
        {

            if (cursor.moveToFirst()){
                while(!cursor.isAfterLast()){

                    // String a=(cursor.getString(cursor.getColumnIndex("InvID")));
                    INVOLIST.add(Integer.valueOf(cursor.getString(cursor.getColumnIndex("InvoiceID"))));
//			 if (cursor.getString(cursor.getColumnIndex("InvoiceID")).length()>17)
//              {
                    //  INVOLIST.add(Integer.valueOf(cursor.getString(0).substring(17, cursor.getString(0).length())) );
                  //  INVOLIST.add(Integer.valueOf(cursor.getString(0).substring(3,cursor.getString(0).length())));
                    //  Toast.makeText(getApplicationContext(),cursor.getString(0).substring(17, cursor.getString(0).length()),Toast.LENGTH_SHORT).show();
                    // invid=Integer.parseInt(cursor.getString(0) );
                    // Toast.makeText(getApplicationContext(),"inv no  "  + cursor.getString(0),Toast.LENGTH_LONG).show();
//               }

                    cursor.moveToNext();

                }
            }


            Collections.sort(INVOLIST);
            invid=INVOLIST.get(INVOLIST.size() - 1) ;

            //Toast.makeText(getApplicationContext(),"inv no  "  + String.valueOf( invid),Toast.LENGTH_LONG).show();

            invid=invid+1;
            //MyGloble.InvoiceNo = STKID + REPID +date + String.valueOf(invid);
//            MyGloble.InvoiceNo = invoicePreFix+String.valueOf(invid);
//            MyGloble.Display_InvoiceNo =invoicePreFix+String.valueOf(invid);

            MyGloble.InvoiceNo = String.valueOf(invid);
            MyGloble.Display_InvoiceNo =STKID + REPID + date +String.valueOf(invid);

        }
        else
        {
            invid=LastInvNo+1;
            //MyGloble.InvoiceNo = STKID + REPID + date +String.valueOf(invid);
            MyGloble.InvoiceNo = String.valueOf(invid);
            MyGloble.Display_InvoiceNo =STKID + REPID + date +String.valueOf(invid);
        }


        //Toast.makeText(getApplicationContext(),"inv no  "  + MyGloble.InvoiceNo,Toast.LENGTH_LONG).show();
        invoiceid.setText(String.valueOf(MyGloble.Display_InvoiceNo));
        //copyFileNew(Environment.getExternalStorageDirectory()+"/DCIM/SFAD.sqlite",Environment.getExternalStorageDirectory()+"/DCIM/BACKUP/SFAD.sqlite");








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


    public void loadretailer(String a)
    {
        TableRow tbrow0 = new TableRow(this);
        TextView tv0 = new TextView(this);
        tv0.setText(" ");
        tv0.setTextColor(Color.WHITE);
        tv0.setGravity(Gravity.LEFT);
        tv0.setTypeface(null, Typeface.BOLD);
        tv0.setWidth(0);
        tbrow0.addView(tv0);

        TextView tv1 = new TextView(this);
        tv1.setText("Retailer Name                ");
        tv1.setTextColor(Color.WHITE);
        tv1.setGravity(Gravity.LEFT);
        tv1.setTypeface(null, Typeface.BOLD);
        //  tv1.setWidth(200);
        tbrow0.addView(tv1);

        TextView tv2 = new TextView(this);
        tv2.setText("Address");
        tv2.setTextColor(Color.WHITE);
        tv2.setGravity(Gravity.LEFT);
        tv2.setTypeface(null, Typeface.BOLD);
        //  tv2.setWidth(310);
        tbrow0.addView(tv2);


        tbrow0.setBackgroundColor(Color.BLACK);
        stk.addView(tbrow0);


        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        Cursor cursor=db.GetC("select * from PDARetailer WHERE PDARouteID="+a+" ORDER BY Ret_Name ");
        //Toast.makeText(getApplicationContext(),String.valueOf( cursor.getCount()),Toast.LENGTH_LONG).show();
        if( cursor.getCount() > 0)
        {
            if (cursor.moveToFirst()){
                while(!cursor.isAfterLast()){

                    // labels.add(cursor.getString(cursor.getColumnIndex("RName")));

                    TableRow tbrow = new TableRow(this);
                    TextView t1v = new TextView(this);
                    t1v.setText(cursor.getString(6));
                    //   t1v.setTextColor(Color.WHITE);
                    t1v.setGravity(Gravity.LEFT);
                    tbrow.addView(t1v);
                    t1v.setTextSize(0);
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
                    //   t3v.setTextColor(Color.WHITE);
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

                            if(MyGloble.RID.equals(""))
                            {
                                Toast.makeText(getApplicationContext(), "Please Select Retailer...???",Toast.LENGTH_LONG).show();
                            }else
                            {
                                Intent intent = new Intent(RetailerActivity.this, PrevinvoiceActivity.class);
                                RetailerActivity.this.startActivity(intent);
                            }



                        }
                    });


                    stk.addView(tbrow);

                    cursor.moveToNext();
                }
            }

        }

    }




    public static boolean copyFileNew(String s, String  d) {

        File source=new File(s);
        File dest=new File(d);

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        try {
            bis = new BufferedInputStream(new FileInputStream(source));
            bos = new BufferedOutputStream(new FileOutputStream(dest, false));

            byte[] buf = new byte[1024];
            bis.read(buf);

            do {
                bos.write(buf);
            } while(bis.read(buf) != -1);
        } catch (IOException e) {
            return false;
        } finally {
            try {
                if (bis != null) bis.close();
                if (bos != null) bos.close();
            } catch (IOException e) {
                return false;
            }
        }

        return true;
    }

//	public void loadgrid() {
//		List<String> labels = new ArrayList<String>();
//		DatabaseHandler db = new DatabaseHandler(getApplicationContext());
//		Cursor cursor=db.GetC("select * from PDARetailer ");
//		//Toast.makeText(getApplicationContext(),String.valueOf( cursor.getCount()),Toast.LENGTH_LONG).show();
//		if( cursor.getCount() > 0)
//		{
//			if (cursor.moveToFirst()){
//				   while(!cursor.isAfterLast()){
//
//					   labels.add(cursor.getString(cursor.getColumnIndex("RName")));
//
//				      cursor.moveToNext();
//				   }
//				}
//
//		}
//
//
//
//	       ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,labels);
//		       list.setAdapter(adapter);
//
//
//}



}

