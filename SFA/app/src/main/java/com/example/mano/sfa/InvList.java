package com.example.mano.sfa;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class  InvList extends AppCompatActivity {
    Button btn1;
    Button btn2;

    EditText daysale;
    EditText ppc;
    EditText LineCount;
    List<String> Invlist = new ArrayList<String>();
    private static final short SMS_PORT = 8998;
    TableLayout stk;

    private String[] spaceProbeHeaders={"Name","Propellant","Destination"};
    private String[][] spaceProbes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invlist);

        setTitle("Invoice List");


        //getActionBar().setTitle("Hello world App");

        //list=(ListView)findViewById(R.id.listView1);
        btn1= (Button)findViewById(R.id.button1);
        btn2= (Button)findViewById(R.id.button2);
        btn2.setFocusable(true);
        btn2.setFocusableInTouchMode(true);///add this line
        btn2.requestFocus();

        stk = (TableLayout) findViewById(R.id.table_main);

        daysale=(EditText)findViewById(R.id.editText1);
        ppc=(EditText)findViewById(R.id.editText2);
        LineCount=(EditText)findViewById(R.id.editText3);

        LineCount.setKeyListener(null);
        ppc.setKeyListener(null);
        daysale.setKeyListener(null);

        MyGloble.invedit="";

        loadlist();
        GetPPC();
        btn1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Intent intent = new Intent(InvList.this, MenucActivity.class);
                InvList.this.startActivity(intent);










               // getContentResolver().delete(Uri.parse("content://sms/conversations"), null, null);





//                try {
//                    String phoneNumber = "0784929578";
//                    SmsManager smsManager = SmsManager.getDefault();
//
//                    ArrayList<String> parts = smsManager.divideMessage("sudu manikaa3763434367");
//                    //smsManager.sendTextMessage(phoneNumber, null, message, null, null);
//                    smsManager.sendMultipartTextMessage(phoneNumber, "77000003", parts,
//                            null, null);
//                    Toast.makeText(getApplicationContext(), "SMS Send !", Toast.LENGTH_LONG).show();
//                } catch (Exception e) {
//                    Toast.makeText(getApplicationContext(), "SMS Failed !", Toast.LENGTH_LONG).show();
//                    e.printStackTrace();
//                }



















             //   deleteMessage();

//                SmsManager smsManager = SmsManager.getDefault();
//                byte[] messageInBytes = "AAAAAAAAAAAAA".getBytes();
//                smsManager.sendDataMessage("784929578", null, SMS_PORT, messageInBytes , null, null);
//


//                SmsManager smsManager = SmsManager.getDefault();
//                PendingIntent piSent = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent("SMS_SENT"), 0);
//                PendingIntent piDelivered = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent("SMS_DELIVERED"), 0);
//                smsManager.sendTextMessage("784929578", null,  " OFFfffffffffffffffffffff", piSent, piDelivered);

//                try {
//                    String phoneNumber = "775729215";
//                    SmsManager smsManager = SmsManager.getDefault();
//
//                    ArrayList<String> parts = smsManager.divideMessage("rrrrrrrrrrrrrrrrrrrrrr");
//                    //smsManager.sendTextMessage(phoneNumber, null, message, null, null);
//                    smsManager.sendMultipartTextMessage(phoneNumber, null, parts,
//                            null, null);
//                    Toast.makeText(getApplicationContext(), "SMS Send !", Toast.LENGTH_LONG).show();
//                } catch (Exception e) {
//                    Toast.makeText(getApplicationContext(),e.toString() , Toast.LENGTH_LONG).show();
//                    e.printStackTrace();
//                }


            //    sendSMS("784929578","dasdas");





//                TelephonyManager tm = (TelephonyManager)
//                        getSystemService(Context.TELEPHONY_SERVICE);
//                //---get the phone number---
//                String telNumber = tm.getLine1Number();
//                if (telNumber != null)
//                Toast.makeText(getApplicationContext(),"Phone number:"+telNumber,Toast.LENGTH_LONG).show();
//
//
//                TelephonyManager telemamanger = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//                String getSimSerialNumber = telemamanger.getSimSerialNumber();
//                String getSimNumber = telemamanger.getLine1Number();
//                Toast.makeText(getApplicationContext(),"11"+getSimNumber,Toast.LENGTH_LONG).show();


                //==================================================
//                String address="784929578";
//
//                Cursor c = getApplicationContext().getContentResolver().query(Uri.parse("content://sms/sent"), new String[] { "_id", "thread_id", "address", "person", "date", "body" }, null, null, null);
//
//                try {
//                    while (c.moveToNext()) {
//                        int id = c.getInt(0);
//                        String address1 = c.getString(2);
//                        String body = c.getString(5);
//                        String date = c.getString(3);
//                        String add =  address1.substring(address1.length()-9, address1.length());
//                        if(address.equals(add) && body.equals("I lve u too") ){
//                            //  getApplicationContext().getContentResolver().delete(Uri.parse("content://sms/" + id), null, null);
//                            Toast.makeText(getApplicationContext(),address1,Toast.LENGTH_LONG).show();
//                            Toast.makeText(getApplicationContext(),body,Toast.LENGTH_LONG).show();
//                            Toast.makeText(getApplicationContext(), Date.valueOf(date).toString() ,Toast.LENGTH_LONG).show();
//                        }
//
//                    }

                    //=================
 //                   c.moveToFirst();
//                    int id = c.getInt(0);
//                    String address1 = c.getString(2);
//                    String body = c.getString(5);
//                    String date = c.getString(3);
//                    String add =  address1.substring(address1.length()-9, address1.length());
//                    if(address.equals(add)){
//                        //  getApplicationContext().getContentResolver().delete(Uri.parse("content://sms/" + id), null, null);
//                        Toast.makeText(getApplicationContext(),address1,Toast.LENGTH_LONG).show();
//                        Toast.makeText(getApplicationContext(),body,Toast.LENGTH_LONG).show();
//                        Toast.makeText(getApplicationContext(), Date.valueOf(date).toString() ,Toast.LENGTH_LONG).show();
//                    }



//                } catch(Exception e) {
//
//                }
////===========================================
//             //   delete_thread("784929579");
//
//
////======================================================
////                Uri deleteUri = Uri.parse("content://sms");
////                SmsManager msg = (SmsManager)message.obj;
////
////                getContentResolver().delete(deleteUri, "address=? and date=?", new String[] {msg.getOriginatingAddress(), String.valueOf(msg.getTimestampMillis())});
////
//
//                //=======================
////                Uri inboxUri = Uri.parse("content://sms/");
////                int count = 0;
////                Cursor c = getBaseContext().getContentResolver().query(inboxUri , null, null, null, null);
////                Toast.makeText(getApplicationContext(),String.valueOf(c.getCount()),Toast.LENGTH_LONG).show();
////                while (c.moveToNext()) {
////                    try {
////                        // Delete the SMS
////                        String pid = c.getString(0); // Get id;
////                        String uri = "content://sms/" + pid;
////                        count = getBaseContext().getContentResolver().delete(Uri.parse(uri),
////                                null, null);
////                    } catch (Exception e) {
////
////                        Toast.makeText(getApplicationContext(),String.valueOf(e),Toast.LENGTH_LONG).show();
////                        Toast.makeText(getApplicationContext(),String.valueOf(e),Toast.LENGTH_LONG).show();
////                        Toast.makeText(getApplicationContext(),String.valueOf(e),Toast.LENGTH_LONG).show();
////
////                    }
////                }
////==================================
////                Uri uriSms = Uri.parse("content://sms/sent");
////                Cursor c = getContentResolver().query(uriSms, null,null,null,null);
////                int thread_id =c.getCount(); //get the thread_id
////                getContentResolver().delete(Uri.parse("content://sms/conversations/"+thread_id ),null,null);
//
//
//
//
////                int count = 0;
////                count =  getContentResolver().delete(Uri.parse("content://sms/conversations"),null, null);
////
////                Toast.makeText(getApplicationContext(),String.valueOf(count),Toast.LENGTH_LONG).show();
//
//
//
//
//              //  deleteSMS();


            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Intent intent = new Intent(InvList.this, RetailerActivity.class);
                InvList.this.startActivity(intent);


            }
        });
    }


    @Override
    public void onBackPressed() {
    }

    public void sendSMS(String phoneNumber, String message) {
        SmsManager smsManager = SmsManager.getDefault();


        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        SmsManager sms = SmsManager.getDefault();
        ArrayList<String> parts = sms.divideMessage(message);
        int messageCount = parts.size();

        Log.i("Message Count", "Message Count: " + messageCount);

        ArrayList<PendingIntent> deliveryIntents = new ArrayList<PendingIntent>();
        ArrayList<PendingIntent> sentIntents = new ArrayList<PendingIntent>();

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);

        for (int j = 0; j < messageCount; j++) {
            sentIntents.add(sentPI);
            deliveryIntents.add(deliveredPI);
        }

        // ---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS sent",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        // ---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {

                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));
        smsManager.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
        //  sms.sendMultipartTextMessage(phoneNumber, null, parts, sentIntents, deliveryIntents);
    }

    public void delete_thread(String thread)
    {
        Cursor c = getApplicationContext().getContentResolver().query(
                Uri.parse("content://sms/"),new String[] {
                        "_id", "thread_id", "address", "person", "date","body" }, null, null, null);

        try {
            while (c.moveToNext())
            {
                int id = c.getInt(0);
                String address = c.getString(2);
                if (address.equals(thread))
                {
                    getApplicationContext().getContentResolver().delete(
                            Uri.parse("content://sms/" + id), null, null);
                }

            }
        } catch (Exception e) {
            Toast.makeText(this,e.toString(), Toast.LENGTH_SHORT).show();
            Toast.makeText(this,e.toString(), Toast.LENGTH_SHORT).show();
            Toast.makeText(this,e.toString(), Toast.LENGTH_SHORT).show();
            Toast.makeText(this,e.toString(), Toast.LENGTH_SHORT).show();

        }
    }
 //   private long getThreadId(Context context) {
//        long threadId = 0;
//
//        String SMS_READ_COLUMN = "read";
//        String WHERE_CONDITION = SMS_READ_COLUMN + " = 0";
//        String SORT_ORDER = "date DESC";
//        int count = 0;
//
//        Cursor cursor = context.getContentResolver().query(
//                SMS_INBOX_CONTENT_URI,
//                new String[] { "_id", "thread_id", "address", "person", "date", "body" },
//                WHERE_CONDITION,
//                null,
//                SORT_ORDER);
//
//        if (cursor != null) {
//            try {
//                count = cursor.getCount();
//                if (count > 0) {
//                    cursor.moveToFirst();
//                    threadId = cursor.getLong(1);
//                }
//            } finally {
//                cursor.close();
//            }
//        }
//
//
//        return threadId;
  //  }
    private void deleteMessage()
    {
      //  Cursor c = getContentResolver().query(SMS_INBOX, null, null, null, null);
        //c.moveToFirst();
        Uri uriSms = Uri.parse("content://sms/sent");
        Cursor c = this.getContentResolver().query(
                uriSms,
                null, null, null, null);
        Toast.makeText(this,String.valueOf( c.getCount()), Toast.LENGTH_SHORT).show();

        while (c.moveToNext())
        {
            System.out.println("Inside if      loop");

            try
            {
                String address = c.getString(2);
              //  String MobileNumber = "784928579";

                String address1 = c.getString(1);
               String address2 = c.getString(3);
                String address3 = c.getString(4);
                String address5 = c.getString(5);
                String address4 = c.getString(6);
                String address6 = c.getString(7);
                String address7 = c.getString(8);
                String address8 = c.getString(9);

                //Log.i( LOGTAG, MobileNumber + "," + address );

             //   Log.i( LOGTAG, c.getString(2) );


              //  if ( address.trim().equals( MobileNumber ) )
             //   {
                    String pid = c.getString(1);
                    String uri = "content://sms/conversations/" + pid;
                    getContentResolver().delete(Uri.parse(uri), null, null);
                  //  stopSelf();
              //  }
            }
            catch (Exception e)
            {
                Toast.makeText(this,e.toString(), Toast.LENGTH_SHORT).show();
                Toast.makeText(this,e.toString(), Toast.LENGTH_SHORT).show();
                Toast.makeText(this,e.toString(), Toast.LENGTH_SHORT).show();
                Toast.makeText(this,e.toString(), Toast.LENGTH_SHORT).show();

                e.printStackTrace();
            }
        }
    }
    public void deleteSMS()
    {
        Uri inboxUri = Uri.parse("content://sms/inbox");
        int count = 0;
        Cursor c = getContentResolver().query(inboxUri , null, null, null, null);
        while (c.moveToNext()) {
            try {
                // Delete the SMS
                String pid = c.getString(0); // Get id;
                String uri = "content://sms/";
                count =getContentResolver().delete(Uri.parse(uri),
                        null, null);
             //   Toast.makeText(getApplicationContext(),"message deleted successfully..!"+count, 5000).show();
                Toast.makeText(getApplicationContext(),count,Toast.LENGTH_LONG).show();
            } catch (Exception e) {
             //   Toast.makeText(getApplicationContext(), e.toString(), 5000).show();
                Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
            }
        }}

    public void GetPPC()
    {
        Integer a=0;
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        Cursor cursor = db.GetC("select RetailerID from PDAInvoice WHERE Cancel='No' AND Printed<>'Not-Printed' GROUP BY RetailerID order bY RetailerID" );
        //	Toast.makeText(getApplicationContext(),String.valueOf( cursor.getCount()),Toast.LENGTH_LONG).show();
        if( cursor.getCount() > 0)
        {
            if (cursor.moveToFirst()){
                while(!cursor.isAfterLast()){
                    a=a+1;
                    cursor.moveToNext();
                }
            }
        }
        ppc.setText(a.toString());

        Double LastDaysale=0.0;
        Cursor cursor11 = db.GetC("select * from PDAProductst " );
        //	Toast.makeText(getApplicationContext(),String.valueOf( cursor.getCount()),Toast.LENGTH_LONG).show();
        if( cursor11.getCount() > 0)
        {
            if (cursor11.moveToFirst())
            {
                LastDaysale=Double.valueOf(cursor11.getString( cursor11.getColumnIndex("Status")))	;
                cursor11.moveToNext();
            }

        }




        double tot=0;
        Cursor cursor1 = db.GetC("select TotalValue,MktReturn from PDAInvoice WHERE Cancel='No' AND Printed<>'Not-Printed' " );
        //	Toast.makeText(getApplicationContext(),String.valueOf( cursor.getCount()),Toast.LENGTH_LONG).show();
        if( cursor1.getCount() > 0)
        {
            if (cursor1.moveToFirst()){
                while(!cursor1.isAfterLast()){

                    tot=tot+(Double.valueOf(cursor1.getString( cursor1.getColumnIndex("TotalValue"))) -Double.valueOf(cursor1.getString(cursor1.getColumnIndex("MktReturn"))));

                    cursor1.moveToNext();
                }
            }
        }

        daysale.setText(String .format("%.2f", tot+LastDaysale));


        Integer INVlineCount=0;
        Cursor cursorL = db.GetC("select PDAInvoicedProduct.InvoicedID from PDAInvoice,PDAInvoicedProduct WHERE PDAInvoice.InvoiceID= PDAInvoicedProduct.InvoicedID AND Cancel='No' AND Printed<>'Not-Printed' " );
        //	Toast.makeText(getApplicationContext(),String.valueOf( cursorL.getCount()),Toast.LENGTH_LONG).show();
        if( cursorL.getCount() > 0)
        {
            if (cursorL.moveToFirst()){
                while(!cursorL.isAfterLast()){
                    INVlineCount=INVlineCount+1;
                    cursorL.moveToNext();
                }
            }
        }
        Integer MKTlineCount=0;
        Cursor cursorLM = db.GetC("select PDAMarketReturn.InvID from PDAInvoice,PDAMarketReturn WHERE PDAInvoice.InvoiceID= PDAMarketReturn.InvID AND Cancel='No' AND Printed<>'Not-Printed' AND ReturnMethod='S' " );
        //	Toast.makeText(getApplicationContext(),String.valueOf( cursorLM.getCount()),Toast.LENGTH_LONG).show();
        if( cursorLM.getCount() > 0)
        {
            if (cursorLM.moveToFirst()){
                while(!cursorLM.isAfterLast()){
                    MKTlineCount=MKTlineCount+1;
                    cursorLM.moveToNext();
                }
            }
        }

        LineCount.setText(String.valueOf( INVlineCount-MKTlineCount));
    }

    public void loadlist() {

        btn2.requestFocus();

        TableRow tbrow0 = new TableRow(this);
    //    tbrow0.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        tbrow0.setGravity(Gravity.CENTER);
   //     tbrow0.setWeightSum(3); //total row weight

//        TableRow.LayoutParams imageParams = new TableRow.LayoutParams();
        TableRow.LayoutParams lp = new TableRow.LayoutParams();;
        lp.weight = 1; //column weight



        TextView tv0 = new TextView(this);
        tv0.setText("");
        tv0.setTextColor(Color.WHITE);
        tv0.setGravity(Gravity.LEFT);
        tv0.setTypeface(null, Typeface.BOLD);
   //     lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.2f);
        tv0.setLayoutParams(lp);
        //  tv0.setWidth(0);


        tbrow0.addView(tv0);


        TextView tv1 = new TextView(this);
        tv1.setText("Retailer                                                                   ");
        tv1.setTextColor(Color.WHITE);
        tv1.setGravity(Gravity.LEFT);
        tv1.setTypeface(null, Typeface.BOLD);
        //  tv1.setWidth(315);
  //      lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.5f);
        tv1.setLayoutParams(lp);
        tbrow0.addView(tv1);


        TextView tv2 = new TextView(this);
        tv2.setText("Status");
        tv2.setTextColor(Color.WHITE);
        tv2.setGravity(Gravity.LEFT);
        tv2.setTypeface(null, Typeface.BOLD);
        //  tv2.setWidth(200);
        tbrow0.setBackgroundColor(Color.BLACK);
   //     lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.3f);
        tv2.setLayoutParams(lp);
        tbrow0.addView(tv2);

//	      TextView tv3 = new TextView(this);
//	      tv3.setText("id");
//	      tv3.setTextColor(Color.WHITE);
//	      tv3.setGravity(Gravity.LEFT);
//	      tv3.setTextSize(5);
//	      tbrow0.addView(tv3);

        stk.addView(tbrow0);
        //   tbrow0.setBackgroundColor(Color.BLACK);

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        // Cursor cursor = db.GetC("select InvID,RName,Status,Total from PDAInvoice,PDARetailer WHERE PDAInvoice.RID=PDARetailer.RID  GROUP BY InvID,RName,Status,Total order bY InvID" );
        Cursor cursor = db.GetC("select InvoiceID,Ret_Name,Printed,Cancel from PDAInvoice,PDARetailer WHERE PDAInvoice.RetailerID=PDARetailer.ID  GROUP BY InvoiceID,Ret_Name,Printed ORDER BY InvoiceID ASC" );
        //	Toast.makeText(getApplicationContext(),String.valueOf( cursor.getCount()),Toast.LENGTH_LONG).show();

        List<String> labels = new ArrayList<String>();

        String InvType="";

        if( cursor.getCount() > 0)
        {
            if (cursor.moveToFirst()){
                while(!cursor.isAfterLast()){

//    					   if(cursor.getString(cursor.getColumnIndex("Printed")).equals("Not-Printed"))
//    					   {
//    						   InvType="Not-Printed";
//    					   }else
//    					   {
//    						   InvType="Printed";
//    					   }

                    InvType = cursor.getString(cursor.getColumnIndex("Printed"));

                    if(cursor.getString(cursor.getColumnIndex("Cancel")).equals("Yes"))
                    {
                        InvType="Cancel";
                    }


                    TableRow tbrow = new TableRow(this);
                    TextView t1v = new TextView(this);
                    t1v.setText(cursor.getString(cursor.getColumnIndex("InvoiceID")));
                    t1v.setTextColor(Color.BLACK);
                    t1v.setGravity(Gravity.LEFT);
                    t1v.setTextSize(0);
                    tbrow.addView(t1v);
                    TextView t2v = new TextView(this);
                    t2v.setText(cursor.getString(cursor.getColumnIndex("Ret_Name")));
                    t2v.setTextColor(Color.BLACK);
                    t2v.setGravity(Gravity.LEFT);
                    tbrow.addView(t2v);

                    TextView t3v = new TextView(this);
                    t3v.setText(InvType);
                    t3v.setTextColor(Color.BLACK);
                    t3v.setGravity(Gravity.LEFT);
                    tbrow.addView(t3v);
                    if(InvType.equals("Not-Printed"))
                    {
                        t3v.setBackgroundColor(Color.RED);
                        t2v.setBackgroundColor(Color.RED);
                    }

                    // hide the inv_no
//    			             TextView t4v = new TextView(this);
//    			             t4v.setText(cursor.getString(cursor.getColumnIndex("InvoiceID")));
//    			             t4v.setTextColor(Color.WHITE);
//    			             t4v.setGravity(Gravity.LEFT);
//    			             t4v.setWidth(10);
//    			             t4v.setTextSize(1);
//    			             tbrow.addView(t4v);


                    tbrow.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {


                            TableRow tr1=(TableRow)view;
                            TextView tv1= (TextView)tr1.getChildAt(0);
                            TextView tv2= (TextView)tr1.getChildAt(1);
                            //  TextView tv3= (TextView)tr1.getChildAt(3);

                            //    MyGloble.invnoView=Integer .parseInt(tv1.getText().toString());
                            //   Toast.makeText(getApplicationContext(),tv2.getText().toString(),Toast.LENGTH_SHORT).show();
                            loadInvView(tv1.getText().toString().trim(),tv2.getText().toString() );

                            // MyGloble.RName=(tv2.getText().toString());
                            // stk.removeAllViews();
                            // stk.removeView(tr1);
                            view.setBackgroundColor(Color.MAGENTA);


                        }
                    });

                    stk.addView(tbrow);



                    cursor.moveToNext();
                }
            }

        }


        //daysale.setText(String.format("%.2f",tot));
    }

    public void loadInvView(String a,String rname) {

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
//        db.ClearDatatable();
//        db.ClearValues();


        //======================For Display Invoice Number
        String STKID="";
        Integer LastInvNo=0;
        String REPID="";
        String date ="";

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        date = df.format(Calendar.getInstance().getTime());

//        Cursor cursor25=db.GetC("select * from PDARep  Where LoginName<>''   ");
//        if( cursor25.getCount() > 0)
//        {
//            if (cursor25.moveToFirst()){
//                while(!cursor25.isAfterLast()){
//
//                    REPID=cursor25.getString(0);
//                    STKID=cursor25.getString(14);
//                    LastInvNo=Integer.valueOf(cursor25.getString(15));
//
//                    cursor25.moveToNext();
//                }
//            }
//
//        }

        //==============================================================

        // Toast.makeText(getApplicationContext(),"lllllllllll   =" +a,Toast.LENGTH_LONG).show();

        MyGloble.InvoiceNo=a ;
        MyGloble.Display_InvoiceNo =a;



        // DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        //  Cursor cursor = db.GetC("select PDAInvoice.InvID,PDAInvoice.RID,Total,Status,InvType,PName,SPrice,Qty,FreeQty from PDAInvoice,PDAInvoicedProduct,PDAProduct WHERE PDAProduct.PID=PDAInvoicedProduct.PID AND PDAInvoice.InvID=PDAInvoicedProduct.InvID  AND PDAInvoice.InvoiceID='" + a +"'  " );
        // Cursor cursor = db.GetC("select PDAInvoice.InvoiceID,PDAInvoicedProduct.ProductID,ProductName,PDAInvoice.RetailerID,TotalValue,Printed,Price,PDAInvoicedProduct.Quantity,PDAInvoicedProduct.FreeIssueQty,PDAInvoicedProduct.Discount,PDAInvoicedProduct.FreeIssueID,PDAInvoicedProduct.DiscountMethod,TCbill,Cheque,Cash,IsMktReturn from PDAInvoice,PDAInvoicedProduct,PDAProduct WHERE PDAProduct.ProductID=PDAInvoicedProduct.ProductID AND PDAInvoice.InvoiceID=PDAInvoicedProduct.InvoicedID  AND PDAInvoice.InvoiceID LIKE'%" + a +"'  " );
        //
        Cursor cursor = db.GetC("select * from PDAInvoice WHERE  PDAInvoice.InvoiceID ='" + a +"'  " );

        //	Toast.makeText(getApplicationContext(),String.valueOf( cursor.getCount()),Toast.LENGTH_LONG).show();

        List<String> labels = new ArrayList<String>();
        if( cursor.getCount() > 0)
        {
            if (cursor.moveToFirst()){
                while(!cursor.isAfterLast()){

                    MyGloble.TCBill= "0";
                    MyGloble.CASH = "0";
                    MyGloble.CHEQUE = "0";

                    if(cursor.getString(cursor.getColumnIndex("TCbill")).equals("1"))
                    {
                        MyGloble.TCBill= "1";

                    }
                    if(cursor.getString(cursor.getColumnIndex("Cheque")).equals("1"))
                    {
                        MyGloble.CHEQUE = "1";
                    }
                    if(cursor.getString(cursor.getColumnIndex("Cash")).equals("1"))
                    {

                        MyGloble.CASH = "1";

                    }


//                    String fc ="";
//                    Cursor cursor2=db.GetC("select * from PDAProduct WHERE ProductID='"+ cursor.getString(cursor.getColumnIndex("ProductID"))+"' ");
//                    if( cursor2.getCount() > 0)
//                    {
//                        if (cursor2.moveToFirst()){
//                            while(!cursor2.isAfterLast()){
//                                fc=cursor2.getString(cursor2.getColumnIndex("FreeIssue"));
//                                cursor2.moveToNext();
//                            }
//                        }
//
//                    }
//                    // Toast.makeText(getApplicationContext(),"ssssssssss",Toast.LENGTH_SHORT).show();
//
//                    Double sb=0.0;
//                    Double dis=0.0;
//                    if (!cursor.getString(cursor.getColumnIndex("Discount")).equals(""))
//                    {
//
//                        Cursor cursor32=db.GetC("select * from PDAProduct,PDADiscountSpecial WHERE PDAProduct.Discount=PDADiscountSpecial.CatID AND PDAProduct.ProductID='"+ cursor.getString(cursor.getColumnIndex("ProductID")) +"' ");
//                        if( cursor32.getCount() > 0)
//                        {
//                            //Toast.makeText(getApplicationContext(),cursor.getString(cursor.getColumnIndex("DiscountMethod"))+"  =  "+cursor.getString(cursor.getColumnIndex("Discount")),Toast.LENGTH_SHORT).show();
//                            if (cursor32.moveToFirst()){
//                                while(!cursor32.isAfterLast()){
//                                    if(cursor32.getString(cursor32.getColumnIndex("DiscountMethod")).equals("%"))
//                                    {
//                                        //get for DiscountSpecial % value
//                                        dis=(Double.valueOf(cursor.getString(cursor.getColumnIndex("Discount")))/100)*(Double.valueOf(cursor.getString(cursor.getColumnIndex("Quantity")))*cursor.getDouble(cursor.getColumnIndex("Price")));
//                                    }else
//                                    {
//                                        //get for DiscountSpecial Rs value
//                                        dis=Double.valueOf(cursor.getString(cursor.getColumnIndex("Discount")));
//                                        //Toast.makeText(getApplicationContext(),"You must give Discount As a (Rs.) Value ....!!!", Toast.LENGTH_LONG).show();
//                                    }
//                                    cursor32.moveToNext();
//                                }
//                            }
//
//                        }
//                        else{
//                            //get for Discount Rs value
//                            dis=Double.valueOf(cursor.getString(cursor.getColumnIndex("Discount")));
//
//                        }
//
//
//                    }
//                    sb=(Double.valueOf(cursor.getString(7)) * Double.valueOf(cursor.getString(6))) - dis ;
//
//                    MyGloble.Dtcount=MyGloble.Dtcount+1;
//
//                    MyGloble.DataTable1[MyGloble.Dtcount][0] = cursor.getString(cursor.getColumnIndex("ProductName")) +" - " +String.format("%.2f",cursor.getDouble(cursor.getColumnIndex("Price")));
//                    MyGloble.DataTable1[MyGloble.Dtcount][1] = String.format("%.2f",cursor.getDouble(cursor.getColumnIndex("Price")));
//                    MyGloble.DataTable1[MyGloble.Dtcount][2] = cursor.getString(cursor.getColumnIndex("Quantity"));
//                    MyGloble.DataTable1[MyGloble.Dtcount][3] = String.format("%.2f",dis);//dis
//                    MyGloble.DataTable1[MyGloble.Dtcount][4] = String.format("%.2f",sb);//subtot
//                    MyGloble.DataTable1[MyGloble.Dtcount][5] = cursor.getString(cursor.getColumnIndex("FreeIssueQty"));//fqty
//                    MyGloble.DataTable1[MyGloble.Dtcount][6] = cursor.getString(cursor.getColumnIndex("FreeIssueID")); //Fpid
//                    MyGloble.DataTable1[MyGloble.Dtcount][7] = cursor.getString(cursor.getColumnIndex("DiscountMethod")); //disM
//                    MyGloble.DataTable1[MyGloble.Dtcount][8] = cursor.getString(cursor.getColumnIndex("ProductID")); //pid
//                    MyGloble.DataTable1[MyGloble.Dtcount][9] = fc; //fcate
//                    MyGloble.DataTable1[MyGloble.Dtcount][10] =cursor.getString(cursor.getColumnIndex("Discount"));
//                    //    MyGloble.Total=cursor.getDouble(2);



                    MyGloble.Repid="";
                    MyGloble.Total= Double.valueOf(cursor.getString(cursor.getColumnIndex("TotalValue")));
                    MyGloble.TotalMKTR= Double.valueOf(cursor.getString(cursor.getColumnIndex("MktReturn")));
                    MyGloble. Status=cursor.getString(cursor.getColumnIndex("Printed"));

                    MyGloble. Chqdate=0;


                    Cursor cursor1=db.GetC("select * from PDARetailer WHERE ID='"+cursor.getString(cursor.getColumnIndex("RetailerID"))+"' ");
                    //	Toast.makeText(getApplicationContext(),String.valueOf( cursor.getCount()),Toast.LENGTH_LONG).show();
                    if( cursor1.getCount() > 0)
                    {
                        if (cursor1.moveToFirst()){
                            while(!cursor1.isAfterLast()){

                                MyGloble.RID=cursor1.getString(cursor1.getColumnIndex("ID"));
                                MyGloble.RName=cursor1.getString(cursor1.getColumnIndex("Ret_Name"));
                                MyGloble.Radd=cursor1.getString(cursor1.getColumnIndex("Ret_Address"));

                                cursor1.moveToNext();
                            }
                        }

                    }


                    cursor.moveToNext();
                }
            }
        }


//        Cursor cursor21=db.GetC("select PDAMarketReturn.ProductID,PDAProduct.ProductName,PDAInvoice.MktReturn,PDAMarketReturn.Price,PDAMarketReturn.Qty,PDAMarketReturn.FIQty,PDAMarketReturn.Discount,PDAMarketReturn.FID,PDAMarketReturn.ReturnMethod from  PDAInvoice,PDAProduct, PDAMarketReturn WHERE  PDAProduct.ProductID=PDAMarketReturn.ProductID AND PDAInvoice.InvoiceID=PDAMarketReturn.InvID  AND PDAInvoice.InvoiceID='"+a+"' ");
//        //	Toast.makeText(getApplicationContext(),"RETURN      " +String.valueOf( cursor21.getCount()),Toast.LENGTH_LONG).show();
//        if( cursor21.getCount() > 0)
//        {
//            if (cursor21.moveToFirst()){
//                while(!cursor21.isAfterLast()){
//
////                    Double sb1=0.0;
////                    Double dis1=0.0;
////                    if (!cursor21.getString(cursor21.getColumnIndex("Discount")).equals(""))
////                    {
////                        Cursor cursor33=db.GetC("select * from PDAProduct,PDADiscountSpecial WHERE PDAProduct.Discount=PDADiscountSpecial.CatID AND PDAProduct.ProductID='"+ cursor21.getString(cursor21.getColumnIndex("ProductID")) +"' ");
////                        if( cursor33.getCount() > 0)
////                        {
////                            if (cursor33.moveToFirst()){
////                                while(!cursor33.isAfterLast()){
////                                    if(cursor33.getString(cursor33.getColumnIndex("DiscountMethod")).equals("%"))
////                                    {
////                                        //Toast.makeText(getApplicationContext(),cursor21.getDouble(cursor21.getColumnIndex("Discount"))+"  ETURN      " +cursor21.getString(cursor21.getColumnIndex("Qty")),Toast.LENGTH_LONG).show();
////                                        dis1=(Double.valueOf(cursor21.getString(cursor21.getColumnIndex("Discount")))/100)*(Double.valueOf(cursor21.getString(cursor21.getColumnIndex("Qty")))*cursor21.getDouble(cursor21.getColumnIndex("Price")));
////                                    }else
////                                    {
////                                        dis1=Double.valueOf(cursor21.getString(cursor21.getColumnIndex("Discount")));
////                                    }
////                                    cursor33.moveToNext();
////                                }
////                            }
////
////                        }else
////                        {
////                            dis1=Double.valueOf(cursor21.getString(cursor21.getColumnIndex("Discount")));
////                        }
////
////
////                        //dis1=Double.valueOf(cursor21.getString(cursor21.getColumnIndex("Discount")));
////                    }
////
////                    sb1=(Double.valueOf(cursor21.getString(3)) * Double.valueOf(cursor21.getString(4))) -dis1 ;
////
////
////                    MyGloble.DtcountMKTR=MyGloble.DtcountMKTR+1;
////
////                    MyGloble.DataTableMKTR[MyGloble.DtcountMKTR][0] = cursor21.getString(cursor21.getColumnIndex("ProductName")) +" - " +String.format("%.2f",cursor21.getDouble(cursor21.getColumnIndex("Price")))+" - "+cursor21.getString(cursor21.getColumnIndex("ReturnMethod"));
////                    MyGloble.DataTableMKTR[MyGloble.DtcountMKTR][1] = String.format("%.2f",cursor21.getDouble(cursor21.getColumnIndex("Price")));
////                    MyGloble.DataTableMKTR[MyGloble.DtcountMKTR][2] = cursor21.getString(cursor21.getColumnIndex("Qty"));
////                    MyGloble.DataTableMKTR[MyGloble.DtcountMKTR][3] = String.format("%.2f",dis1);//dis
////                    MyGloble.DataTableMKTR[MyGloble.DtcountMKTR][4] = String.format("%.2f",sb1);//subtot
////                    MyGloble.DataTableMKTR[MyGloble.DtcountMKTR][5] = cursor21.getString(cursor21.getColumnIndex("FIQty"));//fqty
////                    MyGloble.DataTableMKTR[MyGloble.DtcountMKTR][6] = cursor21.getString(cursor21.getColumnIndex("FID")); //Fpid
////                    MyGloble.DataTableMKTR[MyGloble.DtcountMKTR][7] = cursor21.getString(cursor21.getColumnIndex("ReturnMethod")); //disM
////                    MyGloble.DataTableMKTR[MyGloble.DtcountMKTR][8] = cursor21.getString(cursor21.getColumnIndex("ProductID")); //pid
////                    MyGloble.DataTableMKTR[MyGloble.DtcountMKTR][9] = ""; //fcate
////                    MyGloble.DataTableMKTR[MyGloble.DtcountMKTR][10] = cursor21.getString(cursor21.getColumnIndex("Discount")); //fcate
//
//                    MyGloble.TotalMKTR= Double.valueOf(cursor21.getString(cursor21.getColumnIndex("MktReturn")));
//
//                    cursor21.moveToNext();
//                }
//            }
//
//        }








//        Cursor cursor22=db.GetC("select PDAInvRedemption.ProID,PDAProduct.ProductName,PDAInvRedemption.Qty,PDAInvRedemption.RedProQty,PDAInvRedemption.RedProID,PDAProduct.Price from  PDAProduct, PDAInvRedemption WHERE  PDAProduct.ProductID=PDAInvRedemption.RedProID   AND PDAInvRedemption.InvID='"+a+"' ");
//        //	Toast.makeText(getApplicationContext(),"REDM    "+String.valueOf( cursor22.getCount()),Toast.LENGTH_LONG).show();
//        if( cursor22.getCount() > 0)
//        {
//            if (cursor22.moveToFirst()){
//                while(!cursor22.isAfterLast()){
//
//
//                    MyGloble.DtcountREDEM=MyGloble.DtcountREDEM+1;
//
//                    MyGloble.DataTableREDEM[MyGloble.DtcountREDEM][0] = cursor22.getString(cursor22.getColumnIndex("ProductName")) +"-" +String.format("%.2f",cursor22.getDouble(cursor22.getColumnIndex("Price")));
//                    MyGloble.DataTableREDEM[MyGloble.DtcountREDEM][1] = String.format("%.2f",cursor22.getDouble(cursor22.getColumnIndex("Price")));
//                    MyGloble.DataTableREDEM[MyGloble.DtcountREDEM][2] = cursor22.getString(cursor22.getColumnIndex("Qty"));
//                    MyGloble.DataTableREDEM[MyGloble.DtcountREDEM][3] = "";//dis
//                    MyGloble.DataTableREDEM[MyGloble.DtcountREDEM][4] ="";//subtot
//                    MyGloble.DataTableREDEM[MyGloble.DtcountREDEM][5] = cursor22.getString(cursor22.getColumnIndex("RedProQty"));//fqty
//                    MyGloble.DataTableREDEM[MyGloble.DtcountREDEM][6] =  cursor22.getString(cursor22.getColumnIndex("ProID")); //Fpid
//                    MyGloble.DataTableREDEM[MyGloble.DtcountREDEM][7] = ""; //disM
//                    MyGloble.DataTableREDEM[MyGloble.DtcountREDEM][8] =cursor22.getString(cursor22.getColumnIndex("RedProID")) ; //pid
//                    MyGloble.DataTableREDEM[MyGloble.DtcountREDEM][9] = ""; //fcate
//
//
//
//                    cursor22.moveToNext();
//                }
//            }
//
//        }


        //	Toast.makeText(getApplicationContext(), MyGloble. Status,Toast.LENGTH_SHORT).show();

        if( MyGloble.Status.equals("Not-Printed") )
        {
           // startActivity(new Intent("com.example.mobile.INVTYPE"));

            Intent intent = new Intent(InvList .this, InvtypeActivity .class);
            InvList.this.startActivity(intent);
            MyGloble.invedit="";
        }
        else
        {
         //   startActivity(new Intent("com.example.mobile.INVPRINT"));

            Intent intent = new Intent(InvList .this, InvprintActivity .class);
            InvList.this.startActivity(intent);
            MyGloble.invedit="NO";
        }



    }


}
