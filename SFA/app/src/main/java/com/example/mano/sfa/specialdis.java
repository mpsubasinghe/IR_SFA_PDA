package com.example.mano.sfa;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class specialdis extends AppCompatActivity {
    String[][] DiscountSpecial = new String[50][7];

//    private static final String ALLOWED_CHARACTERS ="0123456789qwertyuiopasdfghjklzxcvbnm";
    private static final String ALLOWED_CHARACTERS ="0123456789";

    String[] nameArray = {"Aestro", "Blender", "Cupcake", "Donut", "Eclair", "Froyo", "GingerBread", "HoneyComb", "IceCream Sandwich", "JelliBean", "KitKat", "Lollipop", "MarshMallow"};
    Spinner spinnercate;
    Spinner spinnerPro;

    TextView textinvno;
    TextView textretname;

    Double RDtot = 0.00;
    Double RDDis = 0.00;

    Double tot = 0.00;
    Double Dis = 0.00;


    Button BtnAdd;
    Button BtnSend;
    Button BtnConform;
    Button Btnback;
    EditText textQty;
    EditText textDis,textpword;
    TableLayout stk;

    Integer Tcount = 0;


    String[] CateCode;
    String[] CateName;

    String[] ProCode;
    String[] ProName;

    DatabaseHandler db;

    String CategCode = "";
    String CategeoryName = "";
    String B_Mgr_Tp = "";
    String PCode = "";
    String PName = "";
    String MajCode = "";
    String fdis="";
    String pword = "";

    String msgtext="";


    Double Price = 0.0;
    Integer PQTY = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new DatabaseHandler(getApplicationContext());

        setContentView(R.layout.activity_specialdis);
        spinnercate = (Spinner) findViewById(R.id.spinnercate);
        spinnerPro = (Spinner) findViewById(R.id.spinnerpro);

        BtnAdd = (Button) findViewById(R.id.buttonadd);
        BtnSend = (Button) findViewById(R.id.buttonsend);
        BtnConform = (Button) findViewById(R.id.buttonconform);
        Btnback= (Button) findViewById(R.id.buttonback);
        textQty = (EditText) findViewById(R.id.editTextqty);
        textDis = (EditText) findViewById(R.id.editTextdis);
      //  textpword= (EditText) findViewById(R.id.editTextcode);
        stk = (TableLayout) findViewById(R.id.table_main);

        textinvno=(TextView) findViewById(R.id.textinvno);
        textretname=(TextView) findViewById(R.id.textViewretname);

        textinvno.setText(": " +MyGloble.Display_InvoiceNo);
        textretname.setText(MyGloble.RName);

        ClearTable();
        LoadCateSpinner();

        //  Loaddata();

        spinnercate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int arg2, long arg3) {

                //   String selected_val=spinner.getSelectedItem().toString();

                CategCode = CateCode[arg2].trim();
                CategeoryName = CateName[arg2].trim();
                LoadProSpinner(CategCode);
//                fqty=Integer.valueOf(CateName[arg2]);
                Cursor cursor2 = db.GetC("select * from PDASMSApp WHERE ProCatNo='"+CategCode+"'   Order by Discription");
                if (cursor2.getCount() > 0) {
                    if (cursor2.moveToFirst()) {
                        while (!cursor2.isAfterLast()) {
                            B_Mgr_Tp = cursor2.getString(2);
                            cursor2.moveToNext();
                        }
                    }
                }





            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });


        spinnerPro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int arg2, long arg3) {

                //   String selected_val=spinner.getSelectedItem().toString();
                PCode = "";
                PName = "";
                PQTY = 0;
                Price = 0.00;

                PCode = ProCode[arg2].trim();
                Cursor cursor = db.GetC("select * from PDAProduct where ProductID='" + PCode + "' ");
                // Toast.makeText(getApplicationContext(), cursor.getString(2).toString(),Toast.LENGTH_LONG).show();
                while (cursor.moveToNext()) {
                    Price = Double.valueOf(cursor.getString(cursor.getColumnIndex("Price")));
//	        		textQty.setText(cursor.getString(cursor.getColumnIndex("Quantity")));
                    PQTY = Integer.valueOf(cursor.getString(cursor.getColumnIndex("Quantity")));
                    PName = cursor.getString(cursor.getColumnIndex("ProductName"));
                    MajCode = cursor.getString(cursor.getColumnIndex("Maj"));
                   fdis=cursor.getString(cursor.getColumnIndex("Discount"));

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });


        BtnAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub


                if (textQty.getText().toString().equals("")) {
                    //  Toast.makeText(getApplicationContext(), "Please Enter QTY Value...?", Toast.LENGTH_LONG).show();
                    DialogBoxOneButton("Please Enter  QTY ...?", "Value");
                    return;
                }
                if (textDis.getText().toString().equals("")) {
                    //  Toast.makeText(getApplicationContext(), "Please Enter QTY Value...?", Toast.LENGTH_LONG).show();
                    DialogBoxOneButton("Please Enter Discount ...?", "Value");
                    return;
                }
                if ( Integer.parseInt(textQty.getText().toString())<= 0 )
                {
                    //	Toast.makeText(getApplicationContext(),"Please Enter the Qty..??", Toast.LENGTH_LONG).show();
                    DialogBoxOneButton("Not Allow For Zero Or Minus QTY Value...?","Value");
                    return;
                }
                if ( Integer.parseInt(textDis.getText().toString())<= 0 )
                {
                    //	Toast.makeText(getApplicationContext(),"Please Enter the Qty..??", Toast.LENGTH_LONG).show();
                    DialogBoxOneButton("Not Allow For Zero Or Minus discount...?","Value");
                    return;
                }
//                Integer invoicedqty =0;
//                for (int i = 0; i < DiscountSpecial.length; i++) {
//                    if (!isEmptyString(DiscountSpecial[i][0])) {
//                        if (DiscountSpecial[i][0].equals(PCode)) {
//                            invoicedqty=invoicedqty+ Integer.valueOf(DiscountSpecial[i][2]);
//                        }
//                    }
//                    }

                Integer checkinvqty=0;
                Cursor cursor12=db.GetC("select * from PDAProduct where ProductID='"+PCode+"' ");
                while(cursor12.moveToNext()) {
                    checkinvqty=Integer.valueOf(cursor12.getString(cursor12.getColumnIndex("Quantity")));
                }
                if (checkinvqty < Integer.valueOf(textQty.getText().toString()))
                {
                    //Toast.makeText(getApplicationContext(),"No Enough Stock Quantity for Free-Issue..??", Toast.LENGTH_LONG).show();
                    DialogBoxOneButton("No Enough Stock Quantity for Sale..??","Stock");
                    return;
                }



                    for (int i = 0; i < DiscountSpecial.length; i++) {
                        if (!isEmptyString(DiscountSpecial[i][0])) {
                            if (DiscountSpecial[i][0].equals(PCode)) {
                                DiscountSpecial[i][0] = "";
                                DiscountSpecial[i][1] = "";
                                DiscountSpecial[i][2] = "";
                                DiscountSpecial[i][3] = "";
                                DiscountSpecial[i][4] = "";
                                DiscountSpecial[i][5] = "";
                                DiscountSpecial[i][6] = "";

                            }
                        }
                    }


                    Tcount = Tcount + 1;
                    double stot = 0.00;
                    double dis = 0.00;
                    dis = Price * Double.valueOf(textQty.getText().toString()) * Double.valueOf(textDis.getText().toString()) / 100;
                    stot = Price * Double.valueOf(textQty.getText().toString()) - dis;
                    DiscountSpecial[Tcount][0] = PCode; //pname
                    DiscountSpecial[Tcount][1] = String.format("%.2f", Price);//price
                    DiscountSpecial[Tcount][2] = textQty.getText().toString();//qty
                    DiscountSpecial[Tcount][3] = textDis.getText().toString();//dis
                    DiscountSpecial[Tcount][4] = String.format("%.2f", dis);//subtot
                    DiscountSpecial[Tcount][5] = String.format("%.2f", stot); //fqty
                    DiscountSpecial[Tcount][6] = PName;


                stk.removeAllViews();
                Loaddata();

                textDis.setEnabled(false);
                spinnercate.setEnabled(false);

             //   disableEditText(textDis);

            }
        });

        BtnSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                    if(DiscountSpecial[1][0].equals(""))
                    {
                        DialogBoxOneButton("Please Enter Products...?","Products");
                        return;
                    }

                String stname;
                String repname;

                stname = "";
                repname = "";
                msgtext = "";
                Cursor cursor2 = db.GetC("select * from PDARep   Where LoginName<>''");
                if (cursor2.getCount() > 0) {
                    if (cursor2.moveToFirst()) {
                        while (!cursor2.isAfterLast()) {
                            stname = cursor2.getString(9);
                            repname = cursor2.getString(1);
                            cursor2.moveToNext();
                        }
                    }
                }
                 RDtot = 0.00;
                 RDDis = 0.00;
                Cursor cursor3 = db.GetC("select * from PDASaving   Where MajCode='" + MajCode + "'");
                if (cursor3.getCount() > 0) {
                    if (cursor3.moveToFirst()) {
                        while (!cursor3.isAfterLast()) {
                            RDtot = Double.valueOf(cursor3.getString(cursor3.getColumnIndex("TotalRD")));
                            RDDis = Double.valueOf(cursor3.getString(cursor3.getColumnIndex("TotalDiscount")));
                            cursor3.moveToNext();
                        }
                    }
                }

                 tot = 0.00;
                 Dis = 0.00;

                for (int i = 0; i < DiscountSpecial.length; i++) {
                    if (!isEmptyString(DiscountSpecial[i][0])) {
                        Dis = Dis + ((Double.valueOf(DiscountSpecial[i][1]) * Double.valueOf(DiscountSpecial[i][2])) * Double.valueOf(DiscountSpecial[i][3]) / 100);
                        tot = tot + (Double.valueOf(DiscountSpecial[i][1]) * Double.valueOf(DiscountSpecial[i][2]));
                    }
                }


                msgtext = msgtext + stname + "\n";
                msgtext = msgtext + repname + "\n";
                msgtext = msgtext + MyGloble.RName + "\n";
                msgtext = msgtext + "Total RD ( " + CategeoryName + " )- " + String.format("%.2f",RDtot )+ "\n";
                double disper =0.0;
                if(RDDis>0)
                {
                    disper=RDDis/RDtot*100;
                }
                msgtext = msgtext + "Total Discount ( " + CategeoryName + " )- " + String.format("%.2f",RDDis) + " ("+String.format("%.2f",disper )+"%)" +"\n";

                msgtext = msgtext + "Requesting Discount - Rs. " + String.format("%.2f",Dis) + " @ " + textDis.getText().toString() + "%" + "\n";
                msgtext = msgtext + "Requesting Invoice Value - Rs. " + String.format("%.2f",(tot - Dis) )+ " @ " + textDis.getText().toString() + "%" + "\n";
                msgtext = msgtext + "Requesting Products \n";

                for (int i = 0; i < DiscountSpecial.length; i++) {
                    if (!isEmptyString(DiscountSpecial[i][0])) {
                        msgtext = msgtext + DiscountSpecial[i][6] + " - " + DiscountSpecial[i][1] + " - " + DiscountSpecial[i][2] + " \n";
                    }
                }
                msgtext = msgtext + "Code : ";

                pword= getRandomString(10) ;
                msgtext = msgtext +pword;

//              sendMessage("772709723", msgtext.trim());
                sendMessage(B_Mgr_Tp, msgtext.trim());

                Toast.makeText(getApplicationContext(),pword, Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(),pword, Toast.LENGTH_LONG).show();

           //   deleteSMS("","784929579");
            //   deleteSMS();

               // getContentResolver().delete(Uri.parse("content://sms/sent"), "address = ? ", new String[] {"784929579"});

            }
        });
        BtnConform.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//                deleteSMS();
//                if (textpword.getText().toString().equals("") )
//                {
//                    //	Toast.makeText(getApplicationContext(),"Please Enter the Qty..??", Toast.LENGTH_LONG).show();
//                    DialogBoxOneButton("Please Enter the password...?","password");
//                    return;
//                }

              // if (!OrderConform("772709723", msgtext.trim())) {
                if (!OrderConform(B_Mgr_Tp, msgtext.trim())) {
                   DialogBoxOneButton("Please Wait... " + "\n" + "Until Conform Your Sales Order..? ","Conformation");
                   return;
               }

                    Cursor cursor1=db.GetC("select PDAInvoicedProduct.Quantity,PDAProduct.Price,PDAInvoicedProduct.ProductID,PDAInvoicedProduct.FreeIssueID,PDAInvoicedProduct.FreeIssueQty from PDAInvoicedProduct,PDAProduct WHERE PDAProduct.ProductID=PDAInvoicedProduct.ProductID AND  PDAProduct.ProCatNo='"+CategCode+"' AND   InvoicedID='"+ MyGloble.InvoiceNo+"'   ");
                    if( cursor1.getCount() > 0) {
                        if (cursor1.moveToFirst())
                        {
                            while (!cursor1.isAfterLast()) {

                                MyGloble.db.execSQL("Update PDAProduct SET Quantity=Quantity + '" + cursor1.getString(cursor1.getColumnIndex("Quantity")) + "' WHERE ProductID = '"+cursor1.getString(cursor1.getColumnIndex("ProductID"))+"'" );
                                MyGloble.db.execSQL("Update PDAProduct SET Quantity=Quantity + '" + cursor1.getString(cursor1.getColumnIndex("FreeIssueQty")) + "' WHERE ProductID = '"+cursor1.getString(cursor1.getColumnIndex("FreeIssueID"))+"' " );
                                MyGloble.db.execSQL("DELETE From  PDAInvoicedProduct  WHERE ProductID = '"+cursor1.getString(cursor1.getColumnIndex("ProductID"))+"' AND  InvoicedID='"+ MyGloble.InvoiceNo+"'  " );

                                cursor1.moveToNext();
                            }
                        }
                    }

                    for (int i = 0; i < DiscountSpecial.length; i++) {
                        if (!isEmptyString(DiscountSpecial[i][0])) {

                            MyGloble.db.execSQL("insert into PDAInvoicedProduct values ('"+MyGloble.InvoiceNo+"' ,'"+ DiscountSpecial[i][0] +"','"+DiscountSpecial[i][2]+"','"+DiscountSpecial[i][3]+"','','0','%','"+String.format("%.2f",Double.valueOf(DiscountSpecial[i][4]))+"','"+String.format("%.2f",Double.valueOf(DiscountSpecial[i][5]))+"','','YES')" );
                            MyGloble.db.execSQL("Update PDAProduct SET Quantity=Quantity - '" + DiscountSpecial[i][2] + "' WHERE ProductID = '"+DiscountSpecial[i][0] +"'" );


                        }
                    }


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
                    Cursor cursor8=db.GetC("select SubTot from PDAMarketReturn WHERE   InvID='"+ MyGloble.InvoiceNo+"' ");
                    if( cursor8.getCount() > 0) {
                        if (cursor8.moveToFirst())
                        {
                            while (!cursor8.isAfterLast()) {

                                MyGloble.TotalMKTR = MyGloble.TotalMKTR + Double.valueOf(cursor8.getString(cursor8.getColumnIndex("SubTot")));
                                cursor8.moveToNext();
                            }
                        }
                    }

                    String RET="No";
                    SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd h:mm a");
                    String date = df.format(Calendar.getInstance().getTime());
                    Cursor cursor6=db.GetC("select InvID from PDAMarketReturn WHERE InvID='"+ MyGloble.InvoiceNo+"' ");
                    if( cursor6.getCount() > 0) {
                        RET="Yes";
                    }


                    Cursor cursor7=db.GetC("select InvoiceID from PDAInvoice WHERE   InvoiceID='"+ MyGloble.InvoiceNo+"'   ");
                    if( cursor7.getCount() > 0) {
                        MyGloble.db.execSQL("Update PDAInvoice SET TotalValue='" + MyGloble.Total +"' WHERE InvoiceID='"+MyGloble.InvoiceNo+"' " );
                    }else {
                        MyGloble.db.execSQL("insert into PDAInvoice values ('"+MyGloble.InvoiceNo+"' ,'"+ MyGloble.RID +"','"+MyGloble.TCBill+"','"+MyGloble.CHEQUE+"','"+MyGloble.CASH+"','Not-Printed','"+ MyGloble.TotalMKTR+"','"+date+"','No','"+RET+"','"+MyGloble.Total+"','"+MyGloble.Chqdate+"')" );
                    }

                    MyGloble.db.execSQL("Update PDARetailer SET SPInv='Y' WHERE ID='"+MyGloble.RID+"' " );
                    MyGloble.db.execSQL("Update PDASaving SET TotalRD=TotalRD+'"+tot+"',TotalDiscount=TotalDiscount+'"+Dis+"' WHERE  MajCode='" + MajCode + "'" );


                    stk.removeAllViews();
                    Loaddata();

                Intent intent = new Intent(specialdis .this, InvmainActivity .class);
                specialdis.this.startActivity(intent);





            }
        });

        Btnback.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Intent intent = new Intent(specialdis .this, InvmainActivity .class);
                specialdis.this.startActivity(intent);


                //	GridView gv = (GridView) findViewById(R.id.gridView1);
//					grid.setBackgroundColor(Color.WHITE);
//					grid.setVerticalSpacing(2);
//					grid.setHorizontalSpacing(2);
//
            }
        });
    }

    // Disabled Back Button On 4n
    @Override
    public void onBackPressed() {
    }
    private void disableEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setEnabled(false);
        editText.setCursorVisible(false);
        editText.setKeyListener(null);
        editText.setBackgroundColor(Color.TRANSPARENT);
    }
    private void LoadCateSpinner() {
        // database handler


        Cursor C = db.GetC("select * from PDASMSApp  Order by Discription");
        List<String> labels = new ArrayList<String>();
        //Toast.makeText(getApplicationContext(),String.valueOf( C.getCount()), Toast.LENGTH_LONG).show();

        CateCode = new String[C.getCount()];
        CateName = new String[C.getCount()];


        Integer a = 0;
        while (C.moveToNext()) {


            CateCode[a] = C.getString(0);
            CateName[a] = C.getString(1);

            labels.add(C.getString(1));
            a = a + 1;

            //Toast.makeText(getApplicationContext(), C.getString(C.getColumnIndex("Ename")), Toast.LENGTH_LONG).show();
        }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, labels);

        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //attaching data adapter to spinner
        spinnercate.setAdapter(dataAdapter);
    }

    private void LoadProSpinner(String c) {


        Cursor C = db.GetC("select * from PDAProduct where ProCatNo='" + c + "' ");
        List<String> labels = new ArrayList<String>();
        //Toast.makeText(getApplicationContext(),String.valueOf( C.getCount()), Toast.LENGTH_LONG).show();

        ProCode = new String[C.getCount()];
        ProName = new String[C.getCount()];

        Integer a = 0;
        while (C.moveToNext()) {


            ProCode[a] = C.getString(0);
            ProName[a] = C.getString(6);

            labels.add(C.getString(6) + " - " + String.format("%.2f", C.getDouble(2)) + " - " + C.getString(3));
            a = a + 1;

            //Toast.makeText(getApplicationContext(), C.getString(C.getColumnIndex("Ename")), Toast.LENGTH_LONG).show();
        }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, labels);

        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //attaching data adapter to spinner
        spinnerPro.setAdapter(dataAdapter);
    }

    public void DialogBoxOneButton(String msg, String head) {
        AlertDialog alertDialog = new AlertDialog.Builder(specialdis.this).create();

        // Setting Dialog Title
        alertDialog.setTitle(head);

        // Setting Dialog Message
        alertDialog.setMessage(msg);

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.tick);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
                //   Toast.makeText(getApplicationContext(),"You clicked on OK", Toast.LENGTH_SHORT).show();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }


    private void ClearTable()
    {
        for (int i = 0; i < DiscountSpecial.length; i++) {

                    DiscountSpecial[i][0] = "";
                    DiscountSpecial[i][1] = "";
                    DiscountSpecial[i][2] = "";
                    DiscountSpecial[i][3] = "";
                    DiscountSpecial[i][4] = "";
                    DiscountSpecial[i][5] = "";
                    DiscountSpecial[i][6] = "";

        }
    }

    private void Loaddata() {

        TableRow tbrow0 = new TableRow(this);
        TextView tv0 = new TextView(this);
        tv0.setText("Product                    |");
        tv0.setTextColor(Color.BLACK);
        tv0.setGravity(Gravity.LEFT);
        tv0.setTypeface(null, Typeface.BOLD);
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(this);
        tv1.setText("         Price|");
        tv1.setTextColor(Color.BLACK);
        tv1.setGravity(Gravity.RIGHT);
        tv1.setTypeface(null, Typeface.BOLD);
        tbrow0.addView(tv1);
        TextView tv2 = new TextView(this);
        tv2.setText("     Qty|");
        tv2.setTextColor(Color.BLACK);
        tv2.setGravity(Gravity.RIGHT);
        tv2.setTypeface(null, Typeface.BOLD);
        tbrow0.addView(tv2);
        TextView tv3 = new TextView(this);
        tv3.setText("         Dis.");
        tv3.setTextColor(Color.BLACK);
        tv3.setGravity(Gravity.RIGHT);
        tv3.setTypeface(null, Typeface.BOLD);
        tbrow0.addView(tv3);

        TextView tv4 = new TextView(this);
        tv4.setText("      SubTot");
        tv4.setTextColor(Color.BLACK);
        tv4.setGravity(Gravity.RIGHT);
        tv4.setTypeface(null, Typeface.BOLD);
        tbrow0.addView(tv4);
        stk.addView(tbrow0);


        for (int i = 0; i < DiscountSpecial.length; i++) {
            if (!isEmptyString(DiscountSpecial[i][0])) {
                if (!DiscountSpecial[i][0].equals("")) {

                    TableRow tbrow = new TableRow(this);
                    TextView t1v = new TextView(this);
                    t1v.setText(DiscountSpecial[i][6]);
                    t1v.setTextColor(Color.BLACK);
                    t1v.setGravity(Gravity.LEFT);
                    tbrow.addView(t1v);
                    TextView t2v = new TextView(this);
                    t2v.setText(DiscountSpecial[i][1]);
                    t2v.setTextColor(Color.BLACK);
                    t2v.setGravity(Gravity.RIGHT);
                    tbrow.addView(t2v);
                    TextView t3v = new TextView(this);
                    t3v.setText(DiscountSpecial[i][2]);
                    t3v.setTextColor(Color.BLACK);
                    t3v.setGravity(Gravity.RIGHT);
                    tbrow.addView(t3v);
                    TextView t4v = new TextView(this);
                    t4v.setText(DiscountSpecial[i][3]);
                    t4v.setTextColor(Color.BLACK);
                    t4v.setGravity(Gravity.RIGHT);
                    tbrow.addView(t4v);

                    TextView t5v = new TextView(this);
                    t5v.setText(DiscountSpecial[i][4]);
                    t5v.setTextColor(Color.BLACK);
                    t5v.setGravity(Gravity.RIGHT);
                    tbrow.addView(t5v);

                    stk.addView(tbrow);


                }
            }


        }


    }

    public static boolean isEmptyString(String text) {
        return (text == null || text.trim().equals("null") || text.trim()
                .length() <= 0);
    }

    public boolean OrderConform(String phoneNo, String msg) {

      Boolean conform;
        conform=false;

       // String address="784929578";
      //  Cursor c = getApplicationContext().getContentResolver().query(Uri.parse("content://sms/sent"), new String[] { "_id", "thread_id", "address", "person", "date", "body" }, null, null, null);
        Cursor c = getApplicationContext().getContentResolver().query(Uri.parse("content://sms/inbox"), new String[] { "_id", "thread_id", "address", "person", "date", "body" }, null, null, null);

        try {
            while (c.moveToNext()) {
                int id = c.getInt(0);
                String address1 = c.getString(2);
                String body = c.getString(5);
                String date = c.getString(3);
                String add="";
                if(address1.length()>=9)
                {
                     add =  address1.substring(address1.length()-9, address1.length());
                }

                if(phoneNo.equals(add) && body.equals(msg) ){
                    //  getApplicationContext().getContentResolver().delete(Uri.parse("content://sms/" + id), null, null);
                    conform=true;
//                    Toast.makeText(getApplicationContext(),address1,Toast.LENGTH_LONG).show();
//                    Toast.makeText(getApplicationContext(),body,Toast.LENGTH_LONG).show();
                }else
                {
                  //  Toast.makeText(getApplicationContext(),address1,Toast.LENGTH_LONG).show();
                }

            }

        } catch(Exception e) {
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
        }
        return  conform;

    }

    private void sendSMSNew(String phoneNumber, String message) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
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

    protected void sendMessage(String pno, String message) {
        try {
            String phoneNumber = pno;
            SmsManager smsManager = SmsManager.getDefault();

            ArrayList<String> parts = smsManager.divideMessage(message);
            //smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            smsManager.sendMultipartTextMessage(phoneNumber, null, parts,
                    null, null);
            Toast.makeText(getApplicationContext(), "SMS Send !", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "SMS Failed !", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }

    private static String getRandomString(final int sizeOfRandomString)
    {
        final Random random=new Random();
        final StringBuilder sb=new StringBuilder(sizeOfRandomString);
        for(int i=0;i<sizeOfRandomString;++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }

    public void deleteSMS( String message, String number){
//        ContentResolver cr=getContentResolver();
//        Uri url=Uri.parse("content://sms/");
//        int num_deleted=cr.delete(url, null, null);
//        Toast.makeText(this, num_deleted+" items are deleted.", Toast.LENGTH_SHORT).show();


//=================================

        try {
            Uri uriSms = Uri.parse("content://sms/");
            Cursor c = this.getContentResolver().query(
                    uriSms,
                    null, null, null, null);

            Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(c));

            Toast.makeText(this,String.valueOf( c.getCount()), Toast.LENGTH_SHORT).show();

            this.getContentResolver().delete(
                    Uri.parse("content://sms/" ), null,
                    null);


            if (c != null && c.moveToFirst()) {
                do {
                    long id = c.getLong(0);
                    long threadId = c.getLong(1);
                    String address = c.getString(2);
                    String body = c.getString(5);
                    String date = c.getString(3);
                    Log.e("log>>>",
                            "0--->" + c.getString(0) + "1---->" + c.getString(1)
                                    + "2---->" + c.getString(2) + "3--->"
                                    + c.getString(3) + "4----->" + c.getString(4)
                                    + "5---->" + c.getString(5));
                    Log.e("log>>>", "date" + c.getString(0));

                    ContentValues values = new ContentValues();
                    values.put("read", true);
                    getContentResolver().update(Uri.parse("content://sms/"),
                            values, "_id=" + id, null);

                 //   if (message.equals(body) && address.equals(number)) {
                        // mLogger.logInfo("Deleting SMS with id: " + threadId);
                        this.getContentResolver().delete(
                                Uri.parse("content://sms/" + id), "address =?",
                                new String[] { c.getString(2) });
                        Log.e("log>>>", "Delete success.........");
                  //  }
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            Log.e("log>>>", e.toString());
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }

//===============================================






    }



}
