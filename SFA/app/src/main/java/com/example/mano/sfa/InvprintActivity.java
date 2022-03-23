package com.example.mano.sfa;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class InvprintActivity extends AppCompatActivity {
    TableLayout stk;
    Button btnPrint;
    Button btnCancel;
    Button btnmktret;
    Button btnBack;

    CheckBox c1;

    EditText tot;
    EditText ret;
    EditText net;

    DatabaseHandler db;
    TextView name;
    TextView add;
    TextView ino;
    TextView invtypetxt;

    Boolean AllowPrint=false;
    private static final int BUFFER = 100000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invprint);
        setTitle("Final Invoice");

        db = new DatabaseHandler(getApplicationContext());

        stk = (TableLayout) findViewById(R.id.table_main);
        btnPrint=(Button)findViewById(R.id.button1);
        btnPrint.setFocusable(true);
        btnPrint.setFocusableInTouchMode(true);///add this line
        btnPrint.requestFocus();


        btnCancel=(Button)findViewById(R.id.button2);
        btnmktret=(Button)findViewById(R.id.button3);
        btnBack=(Button)findViewById(R.id.button4);

        c1=(CheckBox)findViewById(R.id.checkBox1);


        name=(TextView)findViewById(R.id.textView3);
        add=(TextView)findViewById(R.id.textView5);
        ino=(TextView)findViewById(R.id.textView7);
        invtypetxt=(TextView)findViewById(R.id.textView11);


        net=(EditText)findViewById(R.id.editText5);
        ret=(EditText)findViewById(R.id.editText4);
        tot=(EditText)findViewById(R.id.editText3);


        net. setKeyListener(null);
        ret. setKeyListener(null);
        tot. setKeyListener(null);


        btnPrint.setEnabled(true);

        datat() ;

        btnPrint.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //	startActivity(new Intent("com.example.mobile.PRINTDATA"));

                AllowPrint=false;
                btnPrint.setEnabled(false);

                if(MyGloble.InvoiceNo.equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Wait... untill printing proccess is Completed..!!",Toast.LENGTH_LONG).show();
                    return;

                }

                if(MyGloble.Total==0 && MyGloble.TotalMKTR==0)
                {
                    DialogBoxOneButton("You Cann't Print Empty Invoice..!!!","Print Invoice");
                    return;
                }

                Boolean ED=false;
                Boolean SD=false;
//                for (int i=0;i<MyGloble.DataTableMKTR.length;i++)
//                {
//                    if (MyGloble.DataTableMKTR[i][7].equals("E") || MyGloble.DataTableMKTR[i][7].equals("D")  )
//                    {
//                        ED=true;
//                    }
//                    if (MyGloble.DataTableMKTR[i][7].equals("S"))
//                    {
//                        SD=true;
//                    }
//
//                }
                Cursor cursor10=db.GetC("select PDAMarketReturn.ProductID,ProductName,PDAMarketReturn.Qty,PDAMarketReturn.FIQty, PDAMarketReturn.ReturnMethod,PDAMarketReturn.Discount,PDAMarketReturn.SubTot from PDAMarketReturn,PDAProduct WHERE PDAProduct.ProductID=PDAMarketReturn.ProductID AND    InvID='"+ MyGloble.InvoiceNo+"'   ");
                if( cursor10.getCount() > 0) {
                    if (cursor10.moveToFirst()) {
                        while (!cursor10.isAfterLast()) {
                            if(cursor10.getString(cursor10.getColumnIndex("ReturnMethod")).equals("E") || cursor10.getString(cursor10.getColumnIndex("ReturnMethod")).equals("D") )
                            {
                                ED=true;
                            }
                            if(cursor10.getString(cursor10.getColumnIndex("ReturnMethod")).equals("S"))
                            {
                                SD=true;
                            }

                            cursor10.moveToNext();
                        }
                    }
                }





                if(MyGloble.Total==0 && SD==false && ED)
                {
                    DialogBoxOneButton("You Cann't Print Only Expire & Damage Invoice..!!!","Print Invoice");
                    return;
                }


                //	btnPrint.setEnabled(false);

                MyGloble.BillCopy="";

                if(c1.isChecked()){
                    MyGloble.Vat = "Yes";
                }else
                {
                    MyGloble.Vat = "No";
                }

                DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                Cursor cursor22=db.GetC("select * from PDAInvoice WHERE InvoiceID='"+ MyGloble.InvoiceNo+"' AND Cancel = 'Yes' ");
                if( cursor22.getCount() > 0)
                {
                    DialogBoxOneButton("You Cann't Print Cancel Invoice..!!!","Print Invoice");
                    return;
                }

                Cursor cursor2=db.GetC("select * from PDAInvoice WHERE InvoiceID='"+ MyGloble.InvoiceNo+"' AND Cancel <> 'Yes' ");
                if( cursor2.getCount() > 0)
                {
                    // Toast.makeText(getApplicationContext(), "AAAAAAAAAAAAAAAAAAAA",Toast.LENGTH_LONG).show();
                    if (cursor2.moveToFirst()){
                        while(!cursor2.isAfterLast()){
                            if(cursor2.getString(cursor2.getColumnIndex("Printed")).equals("Not-Printed"))
                            {
                                //update("", MyGloble.InvoiceNo);

                                //	Toast.makeText(getApplicationContext(), "Not-Printed",Toast.LENGTH_LONG).show();
//                                Not_Printed_ADD_QTY();
//
//                                MyGloble.db.execSQL("Delete From PDAInvoice WHERE InvoiceID='"+MyGloble.InvoiceNo+"' " );
//                                MyGloble.db.execSQL("Delete From PDAInvoicedProduct WHERE InvoicedID='"+MyGloble.InvoiceNo+"' " );
//                                MyGloble.db.execSQL("Delete From PDAMarketReturn WHERE InvID='"+MyGloble.InvoiceNo+"' " );
//                                MyGloble.db.execSQL("Delete From PDAInvRedemption WHERE InvID='"+MyGloble.InvoiceNo+"' " );
//
//                                InvSave("");
                                AllowPrint=true;
                                MyGloble.db.execSQL("Update PDAInvoice SET Printed='Printed-1' WHERE InvoiceID = '"+MyGloble.InvoiceNo +"'" );


                            }
                            //Toast.makeText(getApplicationContext(),"ww+ "+cursor2.getString(cursor2.getColumnIndex("Printed") )  ,Toast.LENGTH_LONG).show();
                            if(cursor2.getString(cursor2.getColumnIndex("Printed")).equals("Printed-1"))
                            { //Toast.makeText(getApplicationContext(), "BBBBBBBBBBBBBBBBBBB",Toast.LENGTH_LONG).show();
                                //Toast.makeText(getApplicationContext(), "empty",Toast.LENGTH_LONG).show();

                                //update("Printed-2", MyGloble.InvoiceNo);
                                MyGloble.db.execSQL("Update PDAInvoice SET Printed='Printed-2' WHERE InvoiceID = '"+MyGloble.InvoiceNo +"'" );
                                MyGloble.BillCopy ="- Copy -";
                                AllowPrint=true;


                            }
                            if(cursor2.getString(cursor2.getColumnIndex("Printed")).equals(""))
                            { //Toast.makeText(getApplicationContext(), "BBBBBBBBBBBBBBBBBBB",Toast.LENGTH_LONG).show();
                                MyGloble.db.execSQL("Update PDAInvoice SET Printed='Printed-1' WHERE InvoiceID = '"+MyGloble.InvoiceNo +"'" );
                                AllowPrint=true;

                            }
                            if(cursor2.getString(cursor2.getColumnIndex("Printed")).equals("Printed-2"))
                            {
                                //Toast.makeText(getApplicationContext(), "yes",Toast.LENGTH_LONG).show();
                                // Toast.makeText(getApplicationContext(), "Sorry..!!! Already You have twice Printed Invoice!!!!",Toast.LENGTH_LONG).show();
                                DialogBoxOneButton("Sorry..!!! Already You have twice Printed Invoice!!!!","Print Invoice");
                                AllowPrint=false;
                            }

                            cursor2.moveToNext();
                        }
                    }

                }else{

                    try
                    {
                        MyGloble.db.execSQL("Update PDAInvoice SET Printed='Printed-1' WHERE InvoiceID = '"+MyGloble.InvoiceNo +"'" );
                        AllowPrint=true;
                   //     InvSave("");


                    }catch (SQLException e) {
                        // TODO: handle exception
                        Toast.makeText(getApplicationContext(), "No",Toast.LENGTH_LONG).show();
//		   				 Log.e( "getting exception "
//		   	                    + e.getLocalizedMessage().toString(), null);
                    }

                }


                //=================
                // startActivity(new Intent("com.example.mobile.PRINTDATA"));

//					 if(AllowPrint)
//               	    {
//							Toast.makeText(getApplicationContext(), "Printing Process Starting ,Please Wait...!!!!",Toast.LENGTH_LONG).show();
//  						startActivity(new Intent("com.example.mobile.PRINTDATA"));
//
//  					}


                //==================================================================

                Cursor cursor30=db.GetC("select * from PDAInvoice WHERE InvoiceID='"+ MyGloble.InvoiceNo+"'  ");
                if( cursor30.getCount() > 0)
                {
                    //Toast.makeText(getApplicationContext(), "Invoice Save proceess successfully completed !!!!",Toast.LENGTH_LONG).show();

//                    String filePath="";
//                    filePath = Environment.getExternalStorageDirectory()+"/Android/data/Android.Mobile.System/data/BACKUP/SFAD.zip";
//                    String[] s = new String[1];
//                    String inputPath=Environment.getExternalStorageDirectory()+"/Android/data/Android.Mobile.System/data/";
//                    // Type the path of the files in here
//                    s[0] = inputPath + "SFAD.sqlite";
//                    zip(s, filePath);

                    //	DialogBoxTwoButton("Do You Want to Print Invoice..??","Print Invoice","print");



                    if(AllowPrint)
                    {
                        Toast.makeText(getApplicationContext(), "Printing Process Starting ,Please Wait...!!!!",Toast.LENGTH_LONG).show();
                       //====================================== startActivity(new Intent("com.example.mobile.PRINTDATA"));
                        Intent intent = new Intent(InvprintActivity .this, PrintdataActivity .class);
                        InvprintActivity.this.startActivity(intent);
                        //btnPrint.setEnabled(true);
                    }

                }else
                {
                    Toast.makeText(getApplicationContext(), "Not Suceessfully, Invoice Save Process....??? Please Log Out And Try Again",Toast.LENGTH_LONG).show();
                }

                MyGloble.invedit="NO";



            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Boolean SD=false;
//                for (int i=0;i<MyGloble.DataTableMKTR.length;i++)
//                {
//                    if (MyGloble.DataTableMKTR[i][7].equals("S"))
//                    {
//                        SD=true;
//                    }
//
//                }


                Cursor cursor10=db.GetC("select ReturnMethod from PDAMarketReturn WHERE   InvID='"+ MyGloble.InvoiceNo+"'   ");
                if( cursor10.getCount() > 0) {
                    if (cursor10.moveToFirst()) {
                        while (!cursor10.isAfterLast()) {
                            if(cursor10.getString(cursor10.getColumnIndex("ReturnMethod")).equals("S"))
                            {
                                SD=true;
                            }

                            cursor10.moveToNext();
                        }
                    }
                }









                if(SD)
                {
                    DialogBoxOneButton("You Cann't Cancel Invoice, Within Sound Market Return..!!!","Print Invoice");
                    return;
                }

                DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                Cursor cursor2=db.GetC("select * from PDAInvoice WHERE InvoiceID='"+ MyGloble.InvoiceNo+"' AND Cancel='Yes'  ");
                if( cursor2.getCount() > 0)
                {
                    // Toast.makeText(getApplicationContext(), "Sorry..!!! Already You have Cancel this Invoice!!!!",Toast.LENGTH_LONG).show();
                    DialogBoxOneButton("Sorry..!!! Already You have Cancel this Invoice!!!!","Print Invoice");
                    return;
                }
//                Cursor cursor22=db.GetC("select * from PDAInvoice WHERE InvoiceID='"+ MyGloble.InvoiceNo+"' AND Printed='Not-Printed'  ");
//                if( cursor22.getCount() > 0)
//                {
//                    // Toast.makeText(getApplicationContext(), "Sorry..!!! Already You have Cancel this Invoice!!!!",Toast.LENGTH_LONG).show();
//                    DialogBoxOneButton("You Can't Cancel Finish-Invoice ,Before Print!!!!","Print Invoice");
//                    return;
//                }

                Cursor cursor23=db.GetC("select * from PDAInvoice WHERE InvoiceID='"+ MyGloble.InvoiceNo+"' AND Printed<>''  ");
                if( cursor23.getCount() > 0)
                {
                    if (cursor23.moveToFirst()) {
                        while (!cursor23.isAfterLast()) {
                            if (cursor23.getString(cursor23.getColumnIndex("Printed")).equals("Printed-1")) {
                                DialogBoxOneButton("You Can't Cancel, Printed Invoice!!!!","Print Invoice");
                                return;
                            }
                            if (cursor23.getString(cursor23.getColumnIndex("Printed")).equals("Printed-2")) {
                                DialogBoxOneButton("You Can't Cancel, Printed Invoice!!!!", "Print Invoice");
                                return;
                            }

                            cursor23.moveToNext();
                        }
                    }
                }

                //======================================time consider====================================

                String invtime="";
                Cursor cursor32=db.GetC("select * from PDAInvoice where InvoiceID='" + MyGloble.InvoiceNo+"' ");
                // Toast.makeText(getApplicationContext(),String.valueOf( cursor32.getCount()),Toast.LENGTH_LONG).show();
                if( cursor32.getCount() > 0)
                {
                    if (cursor32.moveToFirst())
                    {
                        while(!cursor32.isAfterLast())
                        {
                            invtime = cursor32.getString(cursor32.getColumnIndex("InvoDate")) ;
                            cursor32.moveToNext();
                        }
                    }
                }

                //Toast.makeText(getApplicationContext(),"time " + invtime ,Toast.LENGTH_LONG).show();
                java.util.Date d=null;
                SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd h:mm a");
                try {
                    d = format.parse(invtime);

                }  catch (java.text.ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                SimpleDateFormat df1 = new SimpleDateFormat("hh:mm:ss");
                String Inv_date1 = df1.format(d.getTime());
                //Toast.makeText(getApplicationContext(),"inv time format " + Inv_date1 ,Toast.LENGTH_LONG).show();


                SimpleDateFormat df3 = new SimpleDateFormat("hh:mm:ss");
                String Inv_date2  = df3.format(Calendar.getInstance().getTime());
                //Toast.makeText(getApplicationContext()," current time format " + Inv_date2 ,Toast.LENGTH_LONG).show();

                java.text.DateFormat df = new java.text.SimpleDateFormat("hh:mm:ss");
                java.util.Date date1 = null;
                try {
                    date1 = df.parse(Inv_date1);
                } catch (java.text.ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                java.util.Date date2 = null;
                try {
                    date2 = df.parse(Inv_date2);
                } catch (java.text.ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                long diff = date2.getTime() - date1.getTime();

                int timeInSeconds = (int) (diff / 1000);
                int hours, minutes, seconds;
                hours = timeInSeconds / 3600;
                timeInSeconds = timeInSeconds - (hours * 3600);
                minutes = timeInSeconds / 60;
                timeInSeconds = timeInSeconds - (minutes * 60);
                seconds = timeInSeconds;

//				        Toast.makeText(getApplicationContext(), String.valueOf(hours) ,Toast.LENGTH_LONG).show();
//				        Toast.makeText(getApplicationContext(), String.valueOf(minutes) ,Toast.LENGTH_LONG).show();

                if(hours < 1)
                {
                    DialogBoxTwoButton("Do You Want to Cancel Invoice..??","Cancel Invoice","Cancel");
                }else
                {
                    Toast.makeText(getApplicationContext(), "Sorry...!!! Time Over For Cancel Invoice..!",Toast.LENGTH_LONG).show();
                }


            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub MyGloble.invedit

                if( MyGloble.invedit.equals("NO") )
                {
                   // startActivity(new Intent("com.example.mobile.INVLIST"));
                    Intent intent = new Intent(InvprintActivity .this, InvList .class);
                    InvprintActivity.this.startActivity(intent);

                }
                else
                {
//                    startActivity(new Intent("com.example.mobile.INVTYPE"));
                    Intent intent = new Intent(InvprintActivity .this, InvtypeActivity .class);
                    InvprintActivity.this.startActivity(intent);
                }



            }
        });
        btnmktret.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub MyGloble.invedit
                Intent intent = new Intent(InvprintActivity .this, MktreturnsActivity .class);
                InvprintActivity.this.startActivity(intent);

            }
        });
    }


    // Disabled Back Button On 4n
    @Override
    public void onBackPressed() {
    }

    public void GetTotal()
    {
        MyGloble.Total=0.0;
        Cursor cursor4=db.GetC("select SubTot from PDAInvoicedProduct WHERE   InvoicedID='"+ MyGloble.InvoiceNo+"' ");
        if( cursor4.getCount() > 0) {
            if (cursor4.moveToFirst())
            {
                while (!cursor4.isAfterLast()) {

                    MyGloble.Total=MyGloble.Total+ Double.valueOf(cursor4.getString(cursor4.getColumnIndex("SubTot")));
                    cursor4.moveToNext();
                }
            }
        }

        MyGloble.TotalMKTR = 0.0;
        Cursor cursor5=db.GetC("select SubTot from PDAMarketReturn WHERE   InvID='"+ MyGloble.InvoiceNo+"' ");
        if( cursor5.getCount() > 0) {
            if (cursor5.moveToFirst())
            {
                while (!cursor5.isAfterLast()) {

                    MyGloble.TotalMKTR = MyGloble.TotalMKTR + Double.valueOf(cursor5.getString(cursor5.getColumnIndex("SubTot")));
                    cursor5.moveToNext();
                }
            }
        }

    }
    private void datat() {
        btnPrint.requestFocus();
        TableRow tbrow0 = new TableRow(this);
        TextView tv0 = new TextView(this);
        tv0.setText("Product                                      ");
        tv0.setTextColor(Color.BLACK );
        tv0.setGravity(Gravity.LEFT);
        tv0.setTextSize(12);
        tv0.setTypeface(null, Typeface.BOLD);
        // tv0.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 0.4f));
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(this);
        tv1.setText(" Qty");
        tv1.setTextColor(Color.BLACK);
        tv1.setGravity(Gravity.RIGHT);
        tv1.setTextSize(12);
        tv1.setTypeface(null, Typeface.BOLD);
        //  tv1.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 0.2f));
        tbrow0.addView(tv1);
        TextView tv2 = new TextView(this);
        tv2.setText(" F_Qty");
        tv2.setTextColor(Color.BLACK);
        tv2.setGravity(Gravity.RIGHT);
        tv2.setTextSize(12);
        tv2.setTypeface(null, Typeface.BOLD);
        //  tv2.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 0.2f));
        tbrow0.addView(tv2);
        TextView tv3 = new TextView(this);
        tv3.setText("        Dis.");
        tv3.setTextColor(Color.BLACK);
        tv3.setGravity(Gravity.RIGHT);
        tv3.setTextSize(12);
        tv3.setTypeface(null, Typeface.BOLD);
        // tv3.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 0.2f));
        tbrow0.addView(tv3);
        TextView tv4 = new TextView(this);
        tv4.setText("      S_Total");
        tv4.setTextColor(Color.BLACK);
        tv4.setGravity(Gravity.RIGHT);
        tv4.setTextSize(12);
        tv4.setTypeface(null, Typeface.BOLD);
        // tv4.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 0.2f));
        tbrow0.addView(tv4);
        stk.addView(tbrow0);


        Cursor cursor2=db.GetC("select ProductName,PDAInvoicedProduct.Quantity,FreeIssueQty,DisAmount,SubTot,Price from PDAInvoicedProduct,PDAProduct WHERE PDAProduct.ProductID=PDAInvoicedProduct.ProductID AND  InvoicedID='"+ MyGloble.InvoiceNo+"'   ");
        if( cursor2.getCount() > 0) {
            if (cursor2.moveToFirst())

            {
                while (!cursor2.isAfterLast()) {

                    TableRow tbrow = new TableRow(this);
                    TextView t1v = new TextView(this);
                    t1v.setText(cursor2.getString(cursor2.getColumnIndex("ProductName"))+"-" +cursor2.getString(cursor2.getColumnIndex("Price")));
                    t1v.setTextColor(Color.BLACK);
                    t1v.setGravity(Gravity.LEFT);
                    t1v.setTextSize(12);
                    tbrow.addView(t1v);
                    TextView t2v = new TextView(this);
                    t2v.setText(cursor2.getString(cursor2.getColumnIndex("Quantity")));
                    t2v.setTextColor(Color.BLACK);
                    t2v.setGravity(Gravity.RIGHT);
                    t2v.setTextSize(12);
                    tbrow.addView(t2v);
                    TextView t3v = new TextView(this);
                    t3v.setText(cursor2.getString(cursor2.getColumnIndex("FreeIssueQty")));
                    t3v.setTextColor(Color.BLACK);
                    t3v.setGravity(Gravity.RIGHT);
                    t3v.setTextSize(12);
                    tbrow.addView(t3v);

                    TextView t4v = new TextView(this);
                    t4v.setText(String.format("%.2f",Double .valueOf(cursor2.getString(cursor2.getColumnIndex("DisAmount")))));
                    t4v.setTextColor(Color.BLACK);
                    t4v.setGravity(Gravity.RIGHT);
                    t4v.setTextSize(12);
                    tbrow.addView(t4v);

                    TextView t5v = new TextView(this);
                    t5v.setText(String.format("%.2f",Double .valueOf(cursor2.getString(cursor2.getColumnIndex("SubTot")))));
                    t5v.setTextColor(Color.BLACK);
                    t5v.setGravity(Gravity.RIGHT);
                    t5v.setTextSize(12);
                    t5v.setTypeface(null, Typeface.BOLD);
                    tbrow.addView(t5v);

                    stk.addView(tbrow);
                    cursor2.moveToNext();

                }
            }
        }


        GetTotal();


//        for (int i=0;i<MyGloble.DataTable1.length;i++) {
//            if (!MyGloble.DataTable1[i][0].equals(""))
//            {
//                TableRow tbrow = new TableRow(this);
//                TextView t1v = new TextView(this);
//                t1v.setText(MyGloble.DataTable1[i][0]);
//                t1v.setTextColor(Color.BLACK);
//                t1v.setGravity(Gravity.LEFT);
//                t1v.setTextSize(12);
//                tbrow.addView(t1v);
//                TextView t2v = new TextView(this);
//                t2v.setText(MyGloble.DataTable1[i][2]);
//                t2v.setTextColor(Color.BLACK);
//                t2v.setGravity(Gravity.RIGHT);
//                t2v.setTextSize(12);
//                tbrow.addView(t2v);
//                TextView t3v = new TextView(this);
//                t3v.setText(MyGloble.DataTable1[i][5]);
//                t3v.setTextColor(Color.BLACK);
//                t3v.setGravity(Gravity.RIGHT);
//                t3v.setTextSize(12);
//                tbrow.addView(t3v);
//
//                TextView t4v = new TextView(this);
//                t4v.setText(MyGloble.DataTable1[i][3]);
//                t4v.setTextColor(Color.BLACK);
//                t4v.setGravity(Gravity.RIGHT);
//                t4v.setTextSize(12);
//                tbrow.addView(t4v);
//
//                TextView t5v = new TextView(this);
//                t5v.setText(MyGloble.DataTable1[i][4]);
//                t5v.setTextColor(Color.BLACK);
//                t5v.setGravity(Gravity.RIGHT);
//                t5v.setTextSize(12);
//                t5v.setTypeface(null, Typeface.BOLD);
//                tbrow.addView(t5v);
//
//                stk.addView(tbrow);
//            }
//
//        }

       // GetTotal();

        name.setText(": " + MyGloble.RName);
        add.setText(": " +MyGloble.Radd);
        ino.setText("Inv No: " + (MyGloble.Display_InvoiceNo));
        tot.setText(String.format("%.2f",MyGloble.Total));
        ret.setText(String.format("%.2f",MyGloble.TotalMKTR));
        net.setText(String.format("%.2f",MyGloble.Total-MyGloble.TotalMKTR));
        String billtype="";
        if(MyGloble.TCBill.equals("1") )
        {
            billtype=billtype+"TC Bill/";
        }
        if(MyGloble.CHEQUE.equals("1") )
        {
            billtype=billtype+"Cheque/";
        }
        if(MyGloble.CASH.equals("1") )
        {
            billtype=billtype+"Cash";
        }
        invtypetxt.setText(billtype);


        String vatRNo="";
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        Cursor cursor3=db.GetC("select * from PDARep   Where LoginName<>''");
        if( cursor3.getCount() > 0)
        {
            if (cursor3.moveToFirst()){
                while(!cursor3.isAfterLast()){
                    if (!cursor3.isNull(6))
                    {
                        vatRNo=cursor3.getString(6);
                    }

                    cursor3.moveToNext();
                }
            }
        }
        if(vatRNo.equals(""))
        {
            c1.setEnabled(false);
        }



    }

    public void AddViewCount(String chname)
    {
//   String selectQuery = "UPDATE  channel_login SET TimesViewed=TimesViewed+1 WHERE channelName='"+chname+"'";
//   SQLiteDatabase db = this.getWritableDatabase();
//   Cursor cursor = db.rawQuery(selectQuery, null);
//   System.out.print("Count"+cursor.getCount());

    }
    public void InvSave(String status) {


//		"yyyy.MM.dd G 'at' HH:mm:ss z" ---- 2001.07.04 AD at 12:08:56 PDT
//		"hh 'o''clock' a, zzzz" ----------- 12 o'clock PM, Pacific Daylight Time
//		"EEE, d MMM yyyy HH:mm:ss Z"------- Wed, 4 Jul 2001 12:08:56 -0700
//		"yyyy-MM-dd'T'HH:mm:ss.SSSZ"------- 2001-07-04T12:08:56.235-0700
//		"yyMMddHHmmssZ"-------------------- 010704120856-0700
//		"K:mm a, z" ----------------------- 0:08 PM, PDT
//		"h:mm a" -------------------------- 12:08 PM
//		"EEE, MMM d, ''yy" ---------------- Wed, Jul 4, '01

        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd h:mm a");
        String date = df.format(Calendar.getInstance().getTime());

        SimpleDateFormat dtf = new SimpleDateFormat("h:mm a");
        String time = dtf.format(Calendar.getInstance().getTime());


        for (int i=0;i<MyGloble.DataTable1.length;i++)
        {
            if (!MyGloble.DataTable1[i][0].equals(""))
            {
                // Toast.makeText(getApplicationContext(), MyGloble.DataTable1[i][0] +" = "+MyGloble.DataTable1[i][3] +" = "+ MyGloble.DataTable1[i][10] +"=" ,Toast.LENGTH_LONG).show();
                MyGloble.db.execSQL("insert into PDAInvoicedProduct values ('"+MyGloble.InvoiceNo+"' ,'"+ MyGloble.DataTable1[i][8] +"','"+MyGloble.DataTable1[i][2]+"','"+MyGloble.DataTable1[i][10]+"','"+MyGloble.DataTable1[i][6]+"','"+MyGloble.DataTable1[i][5]+"','"+MyGloble.DataTable1[i][7]+"')" );
                MyGloble.db.execSQL("Update PDAProduct SET Quantity=Quantity - '" + MyGloble.DataTable1[i][2] + "' WHERE ProductID = '"+MyGloble.DataTable1[i][8]+"'" );
                MyGloble.db.execSQL("Update PDAProduct SET Quantity=Quantity - '" + MyGloble.DataTable1[i][5] + "' WHERE ProductID = '"+MyGloble.DataTable1[i][6]+"' " );

            }
        }


        String RET="No";
        for (int i=0;i<MyGloble.DataTableMKTR.length;i++)
        {
            if (!MyGloble.DataTableMKTR[i][0].equals(""))
            {
                RET="Yes";
                MyGloble.db.execSQL("insert into PDAMarketReturn values ('"+MyGloble.InvoiceNo+"' ,'"+ MyGloble.DataTableMKTR[i][8] +"','"+MyGloble.DataTableMKTR[i][2]+"','"+MyGloble.DataTableMKTR[i][10]+"','"+MyGloble.DataTableMKTR[i][7]+"','"+MyGloble.DataTableMKTR[i][1]+"','"+MyGloble.DataTableMKTR[i][6]+"','"+MyGloble.DataTableMKTR[i][5]+"')" );
                if (MyGloble.DataTableMKTR[i][7].equals("S"))
                {
                    MyGloble.db.execSQL("Update PDAProduct SET Quantity=Quantity + '" + MyGloble.DataTableMKTR[i][2] + "' WHERE ProductID = '"+MyGloble.DataTableMKTR[i][8]+"'" );
                    MyGloble.db.execSQL("Update PDAProduct SET Quantity=Quantity + '" + MyGloble.DataTableMKTR[i][5] + "' WHERE ProductID = '"+MyGloble.DataTableMKTR[i][6]+"'" );
                }
            }
        }

        for (int i=0;i<MyGloble.DataTableREDEM.length;i++)
        {
            if (!MyGloble.DataTableREDEM[i][0].equals(""))
            {
                MyGloble.db.execSQL("insert into PDAInvRedemption values ('"+MyGloble.InvoiceNo+"' ,'"+ MyGloble.DataTableREDEM[i][6] +"','"+MyGloble.DataTableREDEM[i][2]+"','"+MyGloble.DataTableREDEM[i][8]+"','"+MyGloble.DataTableREDEM[i][5]+"')" );
                MyGloble.db.execSQL("Update PDAProduct SET Quantity=Quantity - '" + MyGloble.DataTableREDEM[i][2] + "' WHERE ProductID = '"+MyGloble.DataTableREDEM[i][6]+"'" );

            }
        }

        // MyGloble.db.execSQL("insert into PDAInvoice values ('"+MyGloble.invno+"' ,'"+ date +"','"+MyGloble.Total+"','"+MyGloble.RID+"','"+MyGloble.invtype+"','"+MyGloble.Repid+"','"+status+"')" );

        MyGloble.Total=0.0;
        for (int i=0;i<MyGloble.DataTable1.length;i++) {
            if (!MyGloble.DataTable1[i][0].equals(""))
            {

                MyGloble.Total=MyGloble.Total + (Double.valueOf(MyGloble.DataTable1[i][4]));
            }
        }


        MyGloble.TotalMKTR=0.0;
        for (int i=0;i<MyGloble.DataTableMKTR.length;i++) {
            if (!MyGloble.DataTableMKTR[i][0].equals(""))
            {
                MyGloble.TotalMKTR=MyGloble.TotalMKTR+ (Double.valueOf(MyGloble.DataTableMKTR[i][4]) ) ;
            }
        }


        MyGloble.db.execSQL("insert into PDAInvoice values ('"+MyGloble.InvoiceNo+"' ,'"+ MyGloble.RID +"','"+MyGloble.TCBill+"','"+MyGloble.CHEQUE +"','"+MyGloble.CASH+"','"+status+"','"+ MyGloble.TotalMKTR+"','"+date+"','No','"+RET+"','"+MyGloble.Total+"','"+MyGloble.Chqdate+"')" );


//		  DatabaseHandler db = new DatabaseHandler(getApplicationContext());
//		  db.ClearDatatable();
//		  db.ClearValues();


        //	 Toast.makeText(getApplicationContext(), "Invoice Save proceess successfully completed !!!!",Toast.LENGTH_LONG).show();

    }

    public void  Cancel_INV() {

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        Cursor cursor=db.GetC("select InvoiceID from PDAInvoice where InvoiceID='" + MyGloble.InvoiceNo+"' ");
        //Toast.makeText(getApplicationContext(),String.valueOf( cursor.getCount()),Toast.LENGTH_LONG).show();
        if( cursor.getCount() > 0)
        {
            DatabaseHandler db1 = new DatabaseHandler(getApplicationContext());
            Cursor cursor2=db1.GetC("select * from PDAInvoicedProduct WHERE InvoicedID='"+ MyGloble.InvoiceNo+"'   ");
            if( cursor2.getCount() > 0)
            {
                if (cursor2.moveToFirst())

                {
                    while(!cursor2.isAfterLast())
                    {

                        MyGloble.db.execSQL("Update PDAProduct SET Quantity=Quantity + '" +  cursor2.getString(cursor2.getColumnIndex("Quantity")) + "' WHERE ProductID = '"+ cursor2.getString(cursor2.getColumnIndex("ProductID")) +"'" ); //FreeIssueID
                        MyGloble.db.execSQL("Update PDAProduct SET Quantity=Quantity + '" +  cursor2.getString(cursor2.getColumnIndex("FreeIssueQty")) + "' WHERE ProductID = '"+  cursor2.getString(cursor2.getColumnIndex("FreeIssueID")) +"' " );

                        cursor2.moveToNext();
                    }
                }

            }

            DatabaseHandler db2 = new DatabaseHandler(getApplicationContext());
            Cursor cursor3=db2.GetC("select * from PDAMarketReturn WHERE InvID='"+ MyGloble.InvoiceNo+"' AND ReturnMethod='S'  ");
            if( cursor3.getCount() > 0)
            {
                if (cursor3.moveToFirst())

                {
                    while(!cursor3.isAfterLast())
                    {
                        MyGloble.db.execSQL("Update PDAProduct SET Quantity=Quantity - '" +  cursor3.getString(cursor3.getColumnIndex("Qty"))  + "' WHERE ProductID = '"+ cursor3.getString(cursor3.getColumnIndex("ProductID")) +"'" ); //FreeIssueID
                        MyGloble.db.execSQL("Update PDAProduct SET Quantity=Quantity - '" +  cursor3.getString(cursor3.getColumnIndex("FIQty"))  + "' WHERE ProductID = '"+  cursor3.getString(cursor3.getColumnIndex("FID")) +"' " );

                        cursor3.moveToNext();
                    }
                }

            }

            DatabaseHandler db3 = new DatabaseHandler(getApplicationContext());
            Cursor cursor4=db3.GetC("select * from PDAInvRedemption WHERE InvID='"+ MyGloble.InvoiceNo+"'  ");
            if( cursor4.getCount() > 0)
            {
                if (cursor4.moveToFirst())

                {
                    while(!cursor4.isAfterLast())
                    {
                        MyGloble.db.execSQL("Update PDAProduct SET Quantity=Quantity + '" +  cursor4.getString(cursor4.getColumnIndex("Qty")) + "' WHERE ProductID = '"+ cursor4.getString(cursor4.getColumnIndex("ProID"))  +"'" ); //FreeIssueID

                        cursor4.moveToNext();
                    }
                }

            }

            MyGloble.db.execSQL("Update PDAInvoice SET Cancel='Yes', Printed='Printed' WHERE InvoiceID = '"+MyGloble.InvoiceNo +"'" );

//            db.ClearDatatable();
//            db.ClearValues();

            Toast.makeText(getApplicationContext(), "Invoice Cancel Process success!!!",Toast.LENGTH_LONG).show();


        }


        else
        {
            startActivity(new Intent("com.example.mobile.RETAILER"));
        }

    }


    public void update(String a ,String b) {

        MyGloble.db.execSQL("Update PDAInvoice SET Printed='" + a + "' WHERE InvoiceID = '"+b +"'" );

//		  DatabaseHandler db = new DatabaseHandler(getApplicationContext());
//		  db.ClearDatatable();
//		  db.ClearValues();

        Toast.makeText(getApplicationContext(), "You Have Printed Copy Bill Of Invoice ",Toast.LENGTH_LONG).show();

    }

    public void  RemoveINV() {

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        Cursor cursor=db.GetC("select InvoiceID from PDAInvoice where InvoiceID='" + MyGloble.InvoiceNo+"' ");
        //Toast.makeText(getApplicationContext(),String.valueOf( cursor.getCount()),Toast.LENGTH_LONG).show();
        if( cursor.getCount() > 0)
        {
            for (int i=0;i<MyGloble.DataTable1.length;i++)
            {
                if (!MyGloble.DataTable1[i][0].equals(""))
                {
                    // Toast.makeText(getApplicationContext(), MyGloble.DataTable1[i][5] +" ="+ MyGloble.DataTable1[i][6] +"=" ,Toast.LENGTH_LONG).show();
                    MyGloble.db.execSQL("Update PDAProduct SET Quantity=Quantity + '" + MyGloble.DataTable1[i][2] + "' WHERE ProductID = '"+MyGloble.DataTable1[i][8]+"'" );
                    MyGloble.db.execSQL("Update PDAProduct SET Quantity=Quantity + '" + MyGloble.DataTable1[i][5] + "' WHERE ProductID = '"+MyGloble.DataTable1[i][6]+"' " );

                }
            }

            for (int i=0;i<MyGloble.DataTableMKTR.length;i++)
            {
                if (!MyGloble.DataTableMKTR[i][0].equals(""))
                {
                    if (MyGloble.DataTableMKTR[i][7].equals("S"))
                    {
                        MyGloble.db.execSQL("Update PDAProduct SET Quantity=Quantity - '" + MyGloble.DataTableMKTR[i][2] + "' WHERE ProductID = '"+MyGloble.DataTableMKTR[i][8]+"'" );
                        MyGloble.db.execSQL("Update PDAProduct SET Quantity=Quantity - '" + MyGloble.DataTableMKTR[i][5] + "' WHERE ProductID = '"+MyGloble.DataTableMKTR[i][6]+"'" );
                    }
                }
            }

            for (int i=0;i<MyGloble.DataTableREDEM.length;i++)
            {
                if (!MyGloble.DataTableREDEM[i][0].equals(""))
                {
                    MyGloble.db.execSQL("Update PDAProduct SET Quantity=Quantity + '" + MyGloble.DataTableREDEM[i][5] + "' WHERE ProductID = '"+MyGloble.DataTableREDEM[i][6]+"'" );

                }
            }

            MyGloble.db.execSQL("Update PDAInvoice SET Cancel='Yes' WHERE InvoiceID = '"+MyGloble.InvoiceNo +"'" );


//            db.ClearDatatable();
//            db.ClearValues();

            Toast.makeText(getApplicationContext(), "Invoice Cancel Process success!!!",Toast.LENGTH_LONG).show();
            startActivity(new Intent("com.example.mobile.RETAILER"));
        }


        else
        {
            startActivity(new Intent("com.example.mobile.RETAILER"));
        }

    }



    public void Not_Printed_ADD_QTY() {


        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        Cursor cursor2=db.GetC("select * from PDAInvoicedProduct WHERE InvoicedID='"+ MyGloble.InvoiceNo+"'   ");
        if( cursor2.getCount() > 0)
        {
            if (cursor2.moveToFirst())

            {
                while(!cursor2.isAfterLast())
                {
                    //  Toast.makeText(getApplicationContext(), cursor2.getString(cursor2.getColumnIndex("ProductID")),Toast.LENGTH_LONG).show();

                    MyGloble.db.execSQL("Update PDAProduct SET Quantity=Quantity + '" + cursor2.getString(cursor2.getColumnIndex("Quantity")) + "' WHERE ProductID = '"+ cursor2.getString(cursor2.getColumnIndex("ProductID")) +"'" ); //FreeIssueID
                    MyGloble.db.execSQL("Update PDAProduct SET Quantity=Quantity + '" +  cursor2.getString(cursor2.getColumnIndex("FreeIssueQty")) + "' WHERE ProductID = '"+cursor2.getString(cursor2.getColumnIndex("FreeIssueID"))+"' " );

                    cursor2.moveToNext();
                }
            }

        }


        DatabaseHandler db1 = new DatabaseHandler(getApplicationContext());
        Cursor cursor3=db1.GetC("select * from PDAMarketReturn WHERE InvID='"+ MyGloble.InvoiceNo+"' AND ReturnMethod='S'  ");
        if( cursor3.getCount() > 0)
        {
            if (cursor3.moveToFirst())

            {
                while(!cursor3.isAfterLast())
                {
                    MyGloble.db.execSQL("Update PDAProduct SET Quantity=Quantity - '" + cursor3.getString(cursor3.getColumnIndex("Qty"))+ "' WHERE ProductID = '"+ cursor3.getString(cursor3.getColumnIndex("ProductID"))+"'" ); //FreeIssueID
                    MyGloble.db.execSQL("Update PDAProduct SET Quantity=Quantity - '" +  cursor3.getString(cursor3.getColumnIndex("FIQty"))+ "' WHERE ProductID = '"+  cursor3.getString(cursor3.getColumnIndex("FID"))+"' " );

                    cursor3.moveToNext();
                }
            }

        }


        DatabaseHandler db2 = new DatabaseHandler(getApplicationContext());
        Cursor cursor4=db2.GetC("select * from PDAInvRedemption WHERE InvID='"+ MyGloble.InvoiceNo+"'  ");
        if( cursor4.getCount() > 0)
        {
            if (cursor4.moveToFirst())

            {
                while(!cursor4.isAfterLast())
                {
                    MyGloble.db.execSQL("Update PDAProduct SET Quantity=Quantity + '" + cursor4.getString(cursor4.getColumnIndex("Qty"))+ "' WHERE ProductID = '"+ cursor4.getString(cursor4.getColumnIndex("ProID")) +"'" ); //FreeIssueID

                    cursor4.moveToNext();
                }
            }

        }




//	  DatabaseHandler db = new DatabaseHandler(getApplicationContext());
//	  db.ClearDatatable();
//	  db.ClearValues();


        //  Toast.makeText(getApplicationContext(), "Invoice Save proceess successfully completed !!!!",Toast.LENGTH_LONG).show();
    }


    public void DialogBoxOneButton(String msg,String head )
    {
        AlertDialog alertDialog = new AlertDialog.Builder(InvprintActivity.this).create();

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

    public void DialogBoxTwoButton(String msg,String head,final String status )
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(InvprintActivity.this);

        // Setting Dialog Title
        alertDialog.setTitle(head);

        // Setting Dialog Message
        alertDialog.setMessage(msg);

        // Setting Icon to Dialog
        //   alertDialog.setIcon(R.drawable.delete);



        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        // Write your code here to execute after dialog
                        // AllowPrint=true;


//                    	    if(status.equals("print"))
//                    	    {
//                    	    	 if(AllowPrint)
//                         	    {
//            						 startActivity(new Intent("com.example.mobile.PRINTDATA"));
//            					}
//                    	    }


                        if(status.equals("Cancel"))
                        {
                            Cancel_INV();
                            Intent intent = new Intent(InvprintActivity.this, InvList.class);
                            InvprintActivity.this.startActivity(intent);
                        }

                        //  Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();

                    }
                });
        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        AllowPrint=false;
                        Toast.makeText(getApplicationContext(), "Invoice has been saved & not Printed", Toast.LENGTH_SHORT).show();


                        dialog.cancel();
                    }
                });

        // Showing Alert Message
        alertDialog.show();
    }
    public static void copyFileOrDirectory(String srcDir, String dstDir)
    {

        try {
            File src = new File(srcDir);
            File dst = new File(dstDir, src.getName());

            if (src.isDirectory()) {

                String files[] = src.list();
                int filesLength = files.length;
                for (int i = 0; i < filesLength; i++) {
                    String src1 = (new File(src, files[i]).getPath());
                    String dst1 = dst.getPath();
                    copyFileOrDirectory(src1, dst1);

                }
            } else {
                copyFile(src, dst);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void copyFile(File sourceFile, File destFile) throws IOException
    {
        if (!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs();

        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }
    private void importDB(String S,String D) {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();
            if (sd.canWrite()) {
                String currentDBPath = S;
                String backupDBPath = D; // From SD directory.
                File backupDB = new File(data, currentDBPath);
                File currentDB = new File(sd, backupDBPath);

                @SuppressWarnings("resource")
                FileChannel src = new FileInputStream(currentDB).getChannel();
                @SuppressWarnings("resource")
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                Toast.makeText(getApplicationContext(), "Import Successful!",
                        Toast.LENGTH_SHORT).show();

            }
        } catch (Exception e) {

            Toast.makeText(getApplicationContext(), "Import Failed!", Toast.LENGTH_SHORT)
                    .show();

        }
    }

    private void exportDB(String S,String D) {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = S;
                String backupDBPath = D;
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                Toast.makeText(getApplicationContext(), "Backup Successful!",
                        Toast.LENGTH_SHORT).show();

            }
        } catch (Exception e) {

            Toast.makeText(getApplicationContext(), "Backup Failed!", Toast.LENGTH_SHORT)
                    .show();

        }
    }

    public void File_copy(String srcDir, String dstDir) throws IOException {
        File src = new File(srcDir);
        File dst = new File(dstDir);

        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
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


    public void zip(String[] _files, String zipFileName) {
        try {
            BufferedInputStream origin = null;
            FileOutputStream dest = new FileOutputStream(zipFileName);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
                    dest));
            byte data[] = new byte[BUFFER];

            for (int i = 0; i < _files.length; i++) {
                Log.v("Compress", "Adding: " + _files[i]);
                FileInputStream fi = new FileInputStream(_files[i]);
                origin = new BufferedInputStream(fi, BUFFER);

                ZipEntry entry = new ZipEntry(_files[i].substring(_files[i].lastIndexOf("/") + 1));
                out.putNextEntry(entry);
                int count;

                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }

            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





}

