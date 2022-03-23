package com.example.mano.sfa;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class InvtypeActivity extends AppCompatActivity {

    //	GridView gridview;
    EditText textcdate;
    Integer Itempossition;

    DatabaseHandler db;
    Button btn1;
    Button RemInv;
    Button Deleteitem;
    Button btnMktRet;
    Button btn7;
    Button Finish;
    Button Redum;



    CheckBox c1;
    CheckBox c2;
    CheckBox c3;

    TextView textname;
    TextView textinvno;


    String itemdelete;
    String Deletepid="";
    String DeleteFpid="";
    Integer DeleteFqty;
    String fcate= "";
    String fdis= "";
    String mix= "";

    TableLayout stk;
    TableLayout stk1;

    private static final int BUFFER = 100000;

    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invtype);


        db = new DatabaseHandler(getApplicationContext());

        setTitle("Edit Invoice");


        itemdelete="";
        stk = (TableLayout) findViewById(R.id.table_main);
        stk1 = (TableLayout) findViewById(R.id.table_main3);

        //	gridview=(GridView)findViewById(R.id.gridView1);
        textcdate=(EditText)findViewById(R.id .editText1);
        btn1=(Button)findViewById(R.id.button1);
        btn1.setFocusable(true);
        btn1.setFocusableInTouchMode(true);///add this line
        btn1.requestFocus();


        RemInv=(Button)findViewById(R.id.button2);
        Deleteitem=(Button)findViewById(R.id.button3);
        btnMktRet=(Button)findViewById(R.id.button4);
        btn7=(Button)findViewById(R.id.button7);
        Finish=(Button)findViewById(R.id.button6);
        Redum=(Button)findViewById(R.id.button5);

        c1=(CheckBox)findViewById(R.id.checkBox1);
        c2=(CheckBox)findViewById(R.id.checkBox2);
        c3=(CheckBox)findViewById(R.id.checkBox3);

        textinvno=(TextView)findViewById(R.id.textView2);
        textname=(TextView)findViewById(R.id.textView1);

//
       datat() ;
       datat_Free();

        textinvno.setText("Inv NO	:" + MyGloble.Display_InvoiceNo);
        textname.setText("Retailer	:" + MyGloble.RName.toString());


        Finish.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(MyGloble.Total==0)
                {
                    DialogBoxOneButton("You Cann't Finish Empty Invoice..!!!","Finish Invoice");
                    return;
                }

                if(MyGloble.TotalMKTR !=0)
                {
                    DialogBoxOneButton("You Cann't Finish Invoice Within Market Return..!!!","Finish Invoice");
                    return;
                }

                //Toast.makeText(getApplicationContext(),"ffffffffffffff",Toast.LENGTH_LONG).show();

                DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                Cursor cursor=db.GetC("select InvoiceID from PDAInvoice where InvoiceID='" + MyGloble.InvoiceNo+"' ");
                //	Toast.makeText(getApplicationContext(),String.valueOf( cursor.getCount()),Toast.LENGTH_LONG).show();
                if( cursor.getCount() > 0)
                {
//                    Not_Printed_ADD_QTY();
//                    MyGloble.db.execSQL("Delete From PDAInvoice WHERE InvoiceID='"+MyGloble.InvoiceNo+"' ");
//                    MyGloble.db.execSQL("Delete From PDAInvoicedProduct WHERE InvoicedID='"+MyGloble.InvoiceNo+"' ");
//                    MyGloble.db.execSQL("Delete From PDAMarketReturn WHERE InvID='"+MyGloble.InvoiceNo+"' " );
//                    MyGloble.db.execSQL("Delete From PDAInvRedemption WHERE InvID='"+MyGloble.InvoiceNo+"' " );
//
//                    InvSave("Not-Printed");
//                    startActivity(new Intent("com.example.mobile.INVLIST"));
                    MyGloble.db.execSQL("Update PDAInvoice SET Printed='Not-Printed' WHERE InvoiceID='"+MyGloble.InvoiceNo+"' " );
                    Intent intent = new Intent(InvtypeActivity .this, InvList.class);
                    InvtypeActivity.this.startActivity(intent);

                }
                else
                {

//                    InvSave("Not-Printed");
//                    //  DatabaseHandler db = new DatabaseHandler(getApplicationContext());
////                    db.ClearDatatable();
////                    db.ClearValues();
//                    startActivity(new Intent("com.example.mobile.INVLIST"));

                    DialogBoxOneButton("Please Enter the Items to Finish...?","Finish Invoice");

                }


                String filePath="";
                filePath = Environment.getExternalStorageDirectory()+"/Android/data/Android.Mobile.System/data/BACKUP/SFAD.zip";
                String[] s = new String[1];
                String inputPath=Environment.getExternalStorageDirectory()+"/Android/data/Android.Mobile.System/data/";
                // Type the path of the files in here
                s[0] = inputPath + "SFAD.sqlite";
                zip(s, filePath);


            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(InvtypeActivity .this, InvmainActivity .class);
                InvtypeActivity.this.startActivity(intent);
            }
        });
        Redum.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(InvtypeActivity .this,RedemviewActivity .class);
                InvtypeActivity.this.startActivity(intent);
            }
        });

        btnMktRet.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//                startActivity(new Intent("com.example.mobile.MKTRET"));
                Cursor cursor=db.GetC("select * from PDARetailer WHERE ID="+MyGloble.RID+" ORDER BY Ret_Name ");
                //Toast.makeText(getApplicationContext(),String.valueOf( cursor.getCount()),Toast.LENGTH_LONG).show();
                if( cursor.getCount() > 0) {
                    if (cursor.moveToFirst()) {
                        while (!cursor.isAfterLast()) {
                            if (!cursor.isNull(8))
                            {
                                if (cursor.getString(cursor.getColumnIndex("SPInv")).equals("Y"))
                                {
                                    Intent intent = new Intent(InvtypeActivity .this, SPMktRetActivity .class);
                                    InvtypeActivity.this.startActivity(intent);
                                }else
                                {
                                    Intent intent = new Intent(InvtypeActivity .this, MktretActivity .class);
                                    InvtypeActivity.this.startActivity(intent);
                                }

                            }

                            cursor.moveToNext();

                        }
                    }
                }




            }
        });
        RemInv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                openAlert(v);

            }
        });
        Deleteitem.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if(itemdelete.equals(""))
                {
                    DialogBoxOneButton("Please Select Item For Delete...?","Finish Invoice");

                }else{

                    DeleteItem(itemdelete);
                    stk.removeAllViews();
                    //stk.removeAllViewsInLayout();
                    stk.invalidate();
                    stk.refreshDrawableState();
//						int count = stk.getChildCount();
//						for (int i = 0; i < count; i++) {
//						    View child = stk.getChildAt(i);
//						    if (child instanceof TableRow) ((ViewGroup) child).removeAllViews();
//						}
//						stk.refreshDrawableState();

                    datat();
                    datat_Free();
                    itemdelete="";
                }




            }
        });


        btn7.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub




                MyGloble.TCBill= "0";
                MyGloble.CASH = "0";
                MyGloble.CHEQUE = "0";

                if(c1.isChecked()){
                    MyGloble.TCBill= "1";

                }
                if(c2.isChecked()){

                    MyGloble.CHEQUE = "1";

                    if(textcdate.getText().toString().equals(""))
                    {
                        //Toast.makeText(getApplicationContext(),"Please Enter Cheque Date...?",Toast.LENGTH_SHORT).show();
                        DialogBoxOneButton("Please Enter Cheque Date...?","Finish Invoice");
                        return;
                    }else{
                        MyGloble.Chqdate=Integer.valueOf(textcdate.getText().toString()) ;
                    }

                }
                if(c3.isChecked()){

                    MyGloble.CASH = "1";

                }


                if(MyGloble.TCBill.equals("0") && MyGloble.CASH.equals("0") && MyGloble.CHEQUE.equals("0") )
                {
                    //Toast.makeText(getApplicationContext(),"Please Select Invoice type...?",Toast.LENGTH_SHORT).show();
                    DialogBoxOneButton("Please Select Invoice type...?","Finish Invoice");
                    //break;


                }else
                {
                    //Toast.makeText(getApplicationContext(), "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa..!!",Toast.LENGTH_LONG).show();
                  // startActivity(new Intent("com.example.mobile.INVPRINT"));
                   MyGloble.db.execSQL("Update PDAInvoice SET TotalValue='" + MyGloble.Total +"',ChkD='" + MyGloble.Chqdate +"',TCbill='" + MyGloble.TCBill +"',Cheque='" + MyGloble.CHEQUE +"',Cash='" + MyGloble.CASH +"' WHERE InvoiceID='"+MyGloble.InvoiceNo+"' " );
                    Intent intent = new Intent(InvtypeActivity .this, InvprintActivity .class);
                    InvtypeActivity.this.startActivity(intent);

                }


            }
        });


    }
    // Disabled Back Button On 4n
    @Override
    public void onBackPressed() {
    }
    public void GetTotal()
    {
//        MyGloble.Total=0.0;
//        for (int i=0;i<MyGloble.DataTable1.length;i++) {
//            if (!MyGloble.DataTable1[i][0].equals(""))
//            {
//
//                MyGloble.Total=MyGloble.Total+ (Double.valueOf(MyGloble.DataTable1[i][4])) ;
//            }
//        }
//
//        //  MyGloble.Total = MyGloble.Total - MyGloble.TotalMKTR;

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
    public void Not_Printed_ADD_QTY() {


        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        Cursor cursor2=db.GetC("select * from PDAInvoicedProduct WHERE InvoicedID='"+ MyGloble.InvoiceNo+"'   ");
        if( cursor2.getCount() > 0)
        {
            if (cursor2.moveToFirst())

            {
                while(!cursor2.isAfterLast())
                {


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




//		  DatabaseHandler db = new DatabaseHandler(getApplicationContext());
//		  db.ClearDatatable();
//		  db.ClearValues();


        //  Toast.makeText(getApplicationContext(), "Invoice Save proceess successfully completed !!!!",Toast.LENGTH_LONG).show();
    }
    public void InvSave(String status) {

        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd h:mm a");
        String date = df.format(Calendar.getInstance().getTime());

        SimpleDateFormat dtf = new SimpleDateFormat("h:mm a");
        String time = dtf.format(Calendar.getInstance().getTime());

        for (int i=0;i<MyGloble.DataTable1.length;i++)
        {
            if (!MyGloble.DataTable1[i][0].equals(""))
            {
                // Toast.makeText(getApplicationContext(), MyGloble.DataTable1[i][5] +" ="+ MyGloble.DataTable1[i][6] +"=" ,Toast.LENGTH_LONG).show();
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

        MyGloble.db.execSQL("insert into PDAInvoice values ('"+MyGloble.InvoiceNo+"' ,'"+ MyGloble.RID +"','"+MyGloble.TCBill+"','"+MyGloble.CHEQUE+"','"+MyGloble.CASH+"','"+status+"','"+ MyGloble.TotalMKTR+"','"+date+"','No','"+RET+"','"+MyGloble.Total+"','"+MyGloble.Chqdate+"')" );


    }
    public void openAlert(View view) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(InvtypeActivity.this);

        //  alertDialogBuilder.setTitle(this.getTitle()+ " decision");
        alertDialogBuilder.setTitle( "Reset Current Invoice");

        alertDialogBuilder.setMessage("Are you sure?");

        // set positive button: Yes message

        alertDialogBuilder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog,int id) {

                dialog.cancel();
//                if( MyGloble.Status.equals("Not-Printed") )
//                {
//                    DialogBoxOneButton("You Cann't Finish Invoice Reset..!!!","Finish Invoice");
//                }else
//                {
//                    startActivity(new Intent("com.example.mobile.INVLIST"));
//                }

                Intent intent = new Intent(InvtypeActivity .this, InvList .class);
                InvtypeActivity.this.startActivity(intent);

//		                	DatabaseHandler db = new DatabaseHandler(getApplicationContext());
//		            		Cursor cursor=db.GetC("select InvoiceID from PDAInvoice where InvoiceID='" + MyGloble.InvoiceNo+"'   ");
//		            		//Toast.makeText(getApplicationContext(),String.valueOf( cursor.getCount()),Toast.LENGTH_LONG).show();
//		            		if( cursor.getCount() > 0)
//		            		{
//		            			Toast.makeText(getApplicationContext(), "Sorry..! Can't Remove..? You Can Only this Cancel.",Toast.LENGTH_LONG).show();
//			           			 startActivity(new Intent("com.example.mobile.RETAILER"));
//		            		}else
//		            		{
//		            		Toast.makeText(getApplicationContext(), "Current Invoice Remove Process success!!!",Toast.LENGTH_LONG).show();
//		           			 startActivity(new Intent("com.example.mobile.RETAILER"));
//		            		}
//


                //RemoveINV();

                // go to a new activity of the app

//		                    Intent positveActivity = new Intent(getApplicationContext(),
//		                            PositiveActivity.class);
//		                    startActivity(positveActivity);

            }

        });

        // set negative button: No message

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog,int id) {

                // cancel the alert box and put a Toast to the user

                dialog.cancel();

                Toast.makeText(getApplicationContext(), "You Can Continue Invoice...!!!!!",

                        Toast.LENGTH_LONG).show();

            }

        });

        // set neutral button: Exit the app message

        alertDialogBuilder.setNeutralButton("Exit the app",new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog,int id) {

                // exit the app and go to the HOME

                //    MainActivity.this.finish();

            }

        });



        AlertDialog alertDialog = alertDialogBuilder.create();

        // show alert

        alertDialog.show();

    }
    public void  RemoveINV() {

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        Cursor cursor=db.GetC("select InvoiceID from PDAInvoice where InvoiceID='" + MyGloble.InvoiceNo+"'  AND Cancel='No' ");
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
            Toast.makeText(getApplicationContext(), "Sorry..!!! Already You have Cancel OR Not Existing Invoice!!!!",Toast.LENGTH_LONG).show();
            startActivity(new Intent("com.example.mobile.RETAILER"));
        }

    }
    public void Delete_Selected_Row(String a)
    {
        for (int i=0;i<MyGloble.DataTable1.length;i++)
        {
            if (MyGloble.DataTable1[i][8] == a)
            {

                MyGloble.DataTable1[i][0]="";
                MyGloble.DataTable1[i][1]="";
                MyGloble.DataTable1[i][2]="";
                MyGloble.DataTable1[i][3]="";
                MyGloble.DataTable1[i][4]="";
                MyGloble.DataTable1[i][5]="";
                MyGloble.DataTable1[i][6]="";
                MyGloble.DataTable1[i][7]="";
                MyGloble.DataTable1[i][8]="";
                MyGloble.DataTable1[i][9]="";
                MyGloble.DataTable1[i][10]="";


            }
        }
    }
    public void DeleteItem(String a) {

        DeleteFpid="";
        Deletepid = "";
        DeleteFqty=0;
//        for (int i=0;i<MyGloble.DataTable1.length;i++)
//        {
//            if (MyGloble.DataTable1[i][8] == a)
//            {
//                DeleteFpid=MyGloble.DataTable1[i][6];
//
//                MyGloble.DataTable1[i][0]="";
//                MyGloble.DataTable1[i][1]="";
//                MyGloble.DataTable1[i][2]="";
//                MyGloble.DataTable1[i][3]="";
//                MyGloble.DataTable1[i][4]="";
//                MyGloble.DataTable1[i][5]="";
//                MyGloble.DataTable1[i][6]="";
//                MyGloble.DataTable1[i][7]="";
//                MyGloble.DataTable1[i][8]="";
//                MyGloble.DataTable1[i][9]="";
//                MyGloble.DataTable1[i][10]="";
//
//
//            }
//        }

        Cursor cursor5=db.GetC("select * from PDAInvoicedProduct WHERE ProductID = '"+a+"' AND   InvoicedID='"+ MyGloble.InvoiceNo+"'   ");
        if( cursor5.getCount() > 0) {
            if (cursor5.moveToFirst()){
                while (!cursor5.isAfterLast()) {
                    MyGloble.db.execSQL("Update PDAProduct SET Quantity=Quantity + '" + cursor5.getString(cursor5.getColumnIndex("Quantity")) + "' WHERE ProductID = '"+cursor5.getString(cursor5.getColumnIndex("ProductID"))+"'" );
                    MyGloble.db.execSQL("Update PDAProduct SET Quantity=Quantity + '" + cursor5.getString(cursor5.getColumnIndex("FreeIssueQty")) + "' WHERE ProductID = '"+cursor5.getString(cursor5.getColumnIndex("FreeIssueID"))+"' " );
                    DeleteFpid=cursor5.getString(cursor5.getColumnIndex("FreeIssueID"));
                    Deletepid =cursor5.getString(cursor5.getColumnIndex("ProductID"));
                  //  DeleteFqty= Integer.valueOf(cursor5.getColumnIndex("FreeIssueQty"));
                    cursor5.moveToNext();
                }

            }

        }

        Deletepid=a;
        MyGloble.db.execSQL("DELETE from PDAInvoicedProduct WHERE ProductID = '"+a+"' AND   InvoicedID='"+ MyGloble.InvoiceNo+"'" );


        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        Cursor c=db.GetC("select * from PDAProduct where   ProductID='"+Deletepid+"' ");
        if( c.getCount() > 0)
        {	//Toast.makeText(getApplicationContext(),String.valueOf( c.getCount()),Toast.LENGTH_LONG).show();
            if (c.moveToFirst()){
                //Toast.makeText(getApplicationContext(),c.getString(6),Toast.LENGTH_LONG).show();
                fcate =c.getString(5);
                fdis =c.getString(4);
                mix =c.getString(8);
                c.moveToNext();
            }
        }
        c.close();

        if(!fcate.equals("No") && !fcate.equals("FI"))
        {
            DeleteFreeSpecial(fcate);
        }

        if(fcate.equals("FI") && !mix.equals("NO"))
        {
            DeleteFreeMIX();
        }


        if(fdis.equals("No")){

        }else if(fdis.equals("Rs")){
            //Toast.makeText(getApplicationContext(),fcate,Toast.LENGTH_LONG).show();
        }else{
            DELETEDisCount(fdis);
        }

        MyGloble.db.execSQL("Update PDAInvoice SET TotalValue='" + MyGloble.Total +"' WHERE InvoiceID='"+MyGloble.InvoiceNo+"' " );

    }
    public void DeleteFreeMIX() {
        Integer Catefqty=0;

        if(!mix.equals("NO"))
        {
//            for (int i=0;i<MyGloble.DataTable1.length;i++)
//            {
//                if (!MyGloble.DataTable1[i][0].equals(""))
//                {
//                    if (MyGloble.DataTable1[i][8].equals(mix))
//                    {
//                        //Toast.makeText(getApplicationContext(),"fcate="+ MyGloble.DataTable1[i][9] +"="+ cate,Toast.LENGTH_LONG).show();
//                        //MyGloble.DataTable1[i][5]=null;
//                        // Toast.makeText(getApplicationContext(),"pid="+ MyGloble.DataTable1[i][8] +"="+ pid,Toast.LENGTH_LONG).show();
//                        if(DeleteFpid.equals(""))
//                        {
//                            DeleteFpid=MyGloble.DataTable1[i][6];
//                        }
//
//                        MyGloble.DataTable1[i][5]="0";
//                        MyGloble.DataTable1[i][6]="";
//
//                        if(!MyGloble.DataTable1[i][8].equals(Deletepid))
//                        {
//                            // Toast.makeText(getApplicationContext(),"fcate-----"+ cate +"-"+MyGloble.DataTable1[i][2],Toast.LENGTH_LONG).show();
//                            Catefqty=Catefqty+ Integer.valueOf(MyGloble.DataTable1[i][2]);
//                        }
//                    }
//
//                }
//
//            }

            Cursor cursor1=db.GetC("select PDAInvoicedProduct.Quantity,PDAInvoicedProduct.ProductID,PDAInvoicedProduct.FreeIssueID,PDAInvoicedProduct.FreeIssueQty from PDAInvoicedProduct WHERE ProductID = '"+mix+"' AND   InvoicedID='"+ MyGloble.InvoiceNo+"'   ");
            if( cursor1.getCount() > 0) {
                if (cursor1.moveToFirst())
                {
                    while (!cursor1.isAfterLast()) {

                        Catefqty=Catefqty+ Integer.valueOf(cursor1.getString(cursor1.getColumnIndex("Quantity")));

                        if(!cursor1.getString(cursor1.getColumnIndex("FreeIssueID")).equals(""))
                        {
                            DeleteFpid=cursor1.getString(cursor1.getColumnIndex("FreeIssueID"));
                            // DeleteFqty= Integer.valueOf(cursor1.getColumnIndex("FreeIssueQty"));
                        }

                        MyGloble.db.execSQL("Update PDAInvoicedProduct SET FreeIssueID='' , FreeIssueQty='0' WHERE ProductID = '"+mix+"' AND  InvoicedID='"+ MyGloble.InvoiceNo+"'  " );
                        MyGloble.db.execSQL("Update PDAProduct SET Quantity=Quantity + '" + cursor1.getString(cursor1.getColumnIndex("FreeIssueQty")) + "' WHERE ProductID = '"+cursor1.getString(cursor1.getColumnIndex("FreeIssueID"))+"' " );
                        cursor1.moveToNext();
                    }
                }
            }
        }

        //	Delete_Selected_Row(Deletepid);

        //	Toast.makeText(getApplicationContext(),"total fqty-" + String.valueOf(Catefqty),Toast.LENGTH_LONG).show();
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        Cursor cursor=db.GetC("select * from PDAFreeIssue where   ProductID='"+Deletepid+"' ORDER BY ConQty DESC ");
        //	Toast.makeText(getApplicationContext(),String.valueOf( cursor.getCount()),Toast.LENGTH_LONG).show();
        if( cursor.getCount() > 0)
        {
            if (cursor.moveToFirst()){
                while(!cursor.isAfterLast()){
                    //  String data = cursor.getString(cursor.getColumnIndex("data"));
                    if( Catefqty >= Integer.parseInt(cursor.getString(cursor.getColumnIndex("ConQty"))))
                    {
                        //  Toast.makeText(getApplicationContext(),String.valueOf(Catefqty)+"=" + cursor.getString(cursor.getColumnIndex("ConQty")),Toast.LENGTH_LONG).show();
//				    	  for (int i=0;i<2;i++)
//				    	  {
//				    	  }

//                        for (int i=0;i<MyGloble.DataTable1.length;i++)
//                        {
//                            if (!MyGloble.DataTable1[i][0].equals(""))
//                            {
//                                if (MyGloble.DataTable1[i][8].equals(mix))
//                                {
//                                    MyGloble.DataTable1[i][5]=cursor.getString(4);
//                                    MyGloble.DataTable1[i][6]=DeleteFpid;
//                                }
//                            }
//                        }

                        MyGloble.db.execSQL("Update PDAInvoicedProduct SET FreeIssueID='"+DeleteFpid+"' , FreeIssueQty='"+cursor.getString(4)+"' WHERE ProductID = '"+mix+"' AND InvoicedID='"+ MyGloble.InvoiceNo+"'  " );
                        MyGloble.db.execSQL("Update PDAProduct SET Quantity=Quantity - '" +  cursor.getString(4) + "' WHERE ProductID = '"+DeleteFpid+"' " );


                        break;
                    }
                    else
                    {
//                        for (int i=0;i<MyGloble.DataTable1.length;i++)
//                        {
//                            if (!MyGloble.DataTable1[i][0].equals(""))
//                            {
//                                if (MyGloble.DataTable1[i][8].equals(mix))
//                                {
//                                    MyGloble.DataTable1[i][5]="0";
//                                    MyGloble.DataTable1[i][6]="";
//
//                                }
//
//                            }
//
//                        }
                    }
                    //	fqty=  Integer.parseInt(a3)/Integer.parseInt(cursor.getString(cursor.getColumnIndex("Qty"))) * Integer.parseInt(cursor.getString(cursor.getColumnIndex("FQty")));
                    // do what ever you want here
                    cursor.moveToNext();
                }
            }
        }




    }
    public void  DeleteFreeSpecial(String cate) {
        Integer Catefqty=0;
        List<String> labels = new ArrayList<String>();
//        for (int i=0;i<MyGloble.DataTable1.length;i++)
//        {
//            if (!MyGloble.DataTable1[i][0].equals(""))
//            {
//                //	Toast.makeText(getApplicationContext(),"cate== "+cate,Toast.LENGTH_LONG).show();
//                if (MyGloble.DataTable1[i][9].equals(cate))
//                {
//                    if(DeleteFpid.equals(""))
//                    {
//                        DeleteFpid=MyGloble.DataTable1[i][6];
//                    }
//
//                    MyGloble.DataTable1[i][5]="0";
//                    MyGloble.DataTable1[i][6]="";
//
//
//                    if(!MyGloble.DataTable1[i][8].equals(Deletepid))
//                    {
//                        Catefqty=Catefqty+ Integer.valueOf(MyGloble.DataTable1[i][2]);
//                    }
//                }
//
//            }
//        }

        Cursor cursor1=db.GetC("select PDAInvoicedProduct.Quantity,PDAInvoicedProduct.ProductID,PDAInvoicedProduct.FreeIssueID,PDAInvoicedProduct.FreeIssueQty from PDAInvoicedProduct,PDAProduct WHERE PDAProduct.ProductID=PDAInvoicedProduct.ProductID AND FreeIssue='"+cate+"' AND  InvoicedID='"+ MyGloble.InvoiceNo+"'   ");
        if( cursor1.getCount() > 0) {
            if (cursor1.moveToFirst())
            {
                while (!cursor1.isAfterLast()) {

                    Catefqty=Catefqty+ Integer.valueOf(cursor1.getString(cursor1.getColumnIndex("Quantity")));

                    if(!cursor1.getString(cursor1.getColumnIndex("FreeIssueID")).equals(""))
                    {
                        DeleteFpid=cursor1.getString(cursor1.getColumnIndex("FreeIssueID"));
                       // DeleteFqty= Integer.valueOf(cursor1.getColumnIndex("FreeIssueQty"));
                    }

                    MyGloble.db.execSQL("Update PDAInvoicedProduct SET FreeIssueID='' , FreeIssueQty='0' WHERE ProductID = '"+cursor1.getString(cursor1.getColumnIndex("ProductID"))+"' AND  InvoicedID='"+ MyGloble.InvoiceNo+"'  " );
                    MyGloble.db.execSQL("Update PDAProduct SET Quantity=Quantity + '" + cursor1.getString(cursor1.getColumnIndex("FreeIssueQty")) + "' WHERE ProductID = '"+cursor1.getString(cursor1.getColumnIndex("FreeIssueID"))+"' " );
                    cursor1.moveToNext();
                }
            }
        }


        //  Toast.makeText(getApplicationContext(),"total fqty-" + String.valueOf(Catefqty),Toast.LENGTH_LONG).show();
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        Cursor cursor=db.GetC("select * from PDAFreeIssueSpecial where CatID='"+cate+"' ORDER BY ConQty DESC ");
        //	Toast.makeText(getApplicationContext(),String.valueOf( cursor.getCount()),Toast.LENGTH_LONG).show();
        if( cursor.getCount() > 0)
        {
            if (cursor.moveToFirst()){
                while(!cursor.isAfterLast()){
                    if( Catefqty >= Integer.parseInt(cursor.getString(cursor.getColumnIndex("ConQty"))))
                    {
//                        for (int i=0;i<MyGloble.DataTable1.length;i++)
//                        {
//                            if (!MyGloble.DataTable1[i][0].equals(""))
//                            {
//                                if (MyGloble.DataTable1[i][9].equals(cate))
//                                {
//                                    MyGloble.DataTable1[i][5]=cursor.getString(3);
//                                    MyGloble.DataTable1[i][6]=DeleteFpid;
//                                    datat() ;
//                                    break;
//                                }
//                            }
//                        }
                        Integer deletefreeqty;
                        deletefreeqty=0;
                        for (int i=0;i<37;i++) {       // Toast.makeText(getApplicationContext(),cursor.getString(i+2),Toast.LENGTH_LONG).show();
                            if (!cursor.isNull(i * 2 + 2)) {
                                if (!cursor.getString(i * 2 + 2).equals("")) {
                                        if (cursor.getString(i * 2 + 2).equals(DeleteFpid)) {
                                            deletefreeqty=Integer.valueOf(cursor.getString(i*2+3)) ;
                                        }
                                    }
                            }
                        }
                        Integer Freecatecount;
                        Freecatecount=0;
                        Cursor cursor2=db.GetC("select PDAInvoicedProduct.Quantity,PDAInvoicedProduct.ProductID from PDAInvoicedProduct,PDAProduct WHERE PDAProduct.ProductID=PDAInvoicedProduct.ProductID AND FreeIssue='"+cate+"' AND  InvoicedID='"+ MyGloble.InvoiceNo+"'   ");
                        if( cursor2.getCount() > 0) {
                            if (cursor2.moveToFirst()) {
                                while (!cursor2.isAfterLast()) {
                                    Freecatecount=Freecatecount+1;
                                    if( Freecatecount==1)
                                    {
                                        MyGloble.db.execSQL("Update PDAInvoicedProduct SET FreeIssueID='"+DeleteFpid+"' , FreeIssueQty='"+deletefreeqty+"' WHERE ProductID = '"+cursor2.getString(cursor2.getColumnIndex("ProductID"))+"' AND  InvoicedID='"+ MyGloble.InvoiceNo+"'  " );
                                        MyGloble.db.execSQL("Update PDAProduct SET Quantity=Quantity - '" +  deletefreeqty + "' WHERE ProductID = '"+DeleteFpid+"' " );

                                    }
                                    cursor2.moveToNext();
                                }
                            }

                        }
                        break;
                    }

                    cursor.moveToNext();
                }
            }
        }


    }
    public void DELETEDisCount(String Dcate) {
        Integer Catefqty=0;
//        for (int i=0;i<MyGloble.DataTable1.length;i++)
//        {
//            if (!MyGloble.DataTable1[i][0].equals(""))
//            {
//                if (MyGloble.DataTable1[i][7].equals(Dcate))
//                {
//                    MyGloble.DataTable1[i][3]="0.00";
//                    // MyGloble.DataTable1[i][4]="0.00";
//
//                    Catefqty=Catefqty+ Integer.valueOf(MyGloble.DataTable1[i][2]);
//
//                    if(!MyGloble.DataTable1[i][8].equals(Deletepid))
//                    {
//                        // Toast.makeText(getApplicationContext(),"fcate-----"+ cate +"-"+MyGloble.DataTable1[i][2],Toast.LENGTH_LONG).show();
//                        //  Catefqty=Catefqty+ Integer.valueOf(MyGloble.DataTable1[i][2]);
//                    }
//                }
//
//            }
//
//        }

        Cursor cursor1=db.GetC("select PDAInvoicedProduct.Quantity from PDAInvoicedProduct,PDAProduct WHERE PDAProduct.ProductID=PDAInvoicedProduct.ProductID AND  PDAProduct.Discount NOT IN('No','Rs') AND  PDAProduct.Discount='"+Dcate+"' AND  InvoicedID='"+ MyGloble.InvoiceNo+"'   ");
        if( cursor1.getCount() > 0) {
            if (cursor1.moveToFirst())
            {
                while (!cursor1.isAfterLast()) {

                    Catefqty=Catefqty+ Integer.valueOf(cursor1.getString(cursor1.getColumnIndex("Quantity")));
                    cursor1.moveToNext();
                }
            }
        }
        Cursor cursor2=db.GetC("select PDAInvoicedProduct.Quantity,PDAProduct.Price,PDAInvoicedProduct.ProductID from PDAInvoicedProduct,PDAProduct WHERE PDAProduct.ProductID=PDAInvoicedProduct.ProductID AND PDAProduct.Discount NOT IN('No','Rs') AND PDAProduct.Discount='"+Dcate+"' AND   InvoicedID='"+ MyGloble.InvoiceNo+"'   ");
        if( cursor2.getCount() > 0) {
            if (cursor2.moveToFirst())
            {
                while (!cursor2.isAfterLast()) {
                        MyGloble.db.execSQL("Update PDAInvoicedProduct SET Discount='0.00' , DisAmount='0.00',SubTot='" + String.format("%.2f",(Double .valueOf(cursor2.getString(cursor2.getColumnIndex("Quantity")))*Double .valueOf(cursor2.getString(cursor2.getColumnIndex("Price"))))) + "' WHERE ProductID = '"+cursor2.getString(cursor2.getColumnIndex("ProductID"))+"' AND  InvoicedID='"+ MyGloble.InvoiceNo+"'  " );
                        //	 Toast.makeText(getApplicationContext(),"dis="+ MyGloble.DataTable1[i][3] +"  sub="+ MyGloble.DataTable1[i][4],Toast.LENGTH_LONG).show();
                  //  Catefqty=Catefqty+ Integer.valueOf(cursor2.getString(cursor2.getColumnIndex("Quantity")));

                    cursor2.moveToNext();
                }
            }
        }


        //    Toast.makeText(getApplicationContext(),"total fqty-" + String.valueOf(Catefqty),Toast.LENGTH_LONG).show();

        //  Delete_Selected_Row(Deletepid);

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        Cursor cursor=db.GetC("select * from PDADiscountSpecial where CatID='"+Dcate+"' ORDER BY ConQty DESC ");
        //Toast.makeText(getApplicationContext(),String.valueOf( cursor.getCount()),Toast.LENGTH_LONG).show();

        if( cursor.getCount() > 0)
        {
            if (cursor.moveToFirst()){
                while(!cursor.isAfterLast()){
                    //  String data = cursor.getString(cursor.getColumnIndex("data"));
                    if( Catefqty >= Integer.parseInt(cursor.getString(cursor.getColumnIndex("ConQty"))))
                    {

                        if(cursor.getString(3).equals("Rs"))
                        {
//                            for (int q=0;q<MyGloble.DataTable1.length;q++)
//                            {
//                                if (MyGloble.DataTable1[q][7].equals(Dcate))
//                                {
//                                    MyGloble.DataTable1[q][3]="0.00";
//                                    MyGloble.DataTable1[q][4]=String.format("%.2f",(Double.valueOf(MyGloble.DataTable1[q][1]) * Double.valueOf( MyGloble.DataTable1[q][2])));;
//                                    MyGloble.DataTable1[q][10]="0.00";
//                                }
//
//                            }
//
//                            for (int i=0;i<MyGloble.DataTable1.length;i++)
//                            {
//
//                                if (!MyGloble.DataTable1[i][0].equals(""))
//                                {
//                                    if (MyGloble.DataTable1[i][7].equals(Dcate) && cursor.getString(3).equals("Rs") ) //Get item withut normal discount
//                                    {
//                                        // Toast.makeText(getApplicationContext(),String.valueOf( MyGloble.DataTable1[i][3])+ "  +  " + MyGloble.DataTable1[i][4],Toast.LENGTH_LONG).show();
//                                        MyGloble.DataTable1[i][3]=String.format("%.2f",(Double.valueOf(cursor.getString(2))));
//                                        MyGloble.DataTable1[i][4]=String.format("%.2f",(Double.valueOf(MyGloble.DataTable1[i][1]) * Double.valueOf( MyGloble.DataTable1[i][2])) - Double.valueOf(cursor.getString(2)));
//                                        MyGloble.DataTable1[i][10]=String.format("%.2f",(Double.valueOf(cursor.getString(2))));
//                                        break;
//                                    }
//                                    else{
//                                        // MyGloble.DataTable1[i][3]="0.00";
//                                    }
//
//                                }
//
//                            }
                            Integer disRsCount;
                            disRsCount =0;
                            Cursor cursor3=db.GetC("select PDAInvoicedProduct.Quantity,PDAProduct.Price,PDAInvoicedProduct.ProductID from PDAInvoicedProduct,PDAProduct WHERE PDAProduct.ProductID=PDAInvoicedProduct.ProductID AND PDAProduct.Discount NOT IN('No','Rs') AND PDAProduct.Discount='"+Dcate+"' AND   InvoicedID='"+ MyGloble.InvoiceNo+"'   ");
                            if( cursor3.getCount() > 0) {
                                if (cursor3.moveToFirst())
                                {
                                    while (!cursor3.isAfterLast()) {
                                        disRsCount=disRsCount+1;
                                        if (disRsCount==1)
                                        {
                                            Double temsubRs;
                                            temsubRs=0.0;
                                            temsubRs= (Double .valueOf(cursor3.getColumnIndex("Quantity"))*Double .valueOf(cursor3.getString(cursor3.getColumnIndex("Price")))) - Double.valueOf(cursor.getString(2)) ;
                                            MyGloble.db.execSQL("Update PDAInvoicedProduct SET Discount='"+String.format("%.2f",(Double.valueOf(cursor.getString(2))))+"' , DisAmount='" + String.format("%.2f",(Double.valueOf(cursor.getString(2)))) + "',SubTot='" + String.format("%.2f",temsubRs) + "' WHERE ProductID = '"+cursor3.getString(cursor3.getColumnIndex("ProductID"))+"' AND  InvoicedID='"+ MyGloble.InvoiceNo+"'  " );

                                        }else
                                        {
                                            MyGloble.db.execSQL("Update PDAInvoicedProduct SET Discount='0.00' , DisAmount='0.00',SubTot='" + String.format("%.2f",(Double .valueOf(cursor3.getColumnIndex("Quantity"))*Double .valueOf(cursor3.getString(cursor3.getColumnIndex("Price"))))) + "' WHERE ProductID = '"+cursor3.getString(cursor3.getColumnIndex("ProductID"))+"' AND  InvoicedID='"+ MyGloble.InvoiceNo+"'  " );
                                            //Toast.makeText(getApplicationContext(),"dis="+ MyGloble.DataTable1[i][3] +"  sub="+ MyGloble.DataTable1[i][4],Toast.LENGTH_LONG).show();

                                        }
                                      cursor3.moveToNext();
                                    }
                                }
                            }


                        }
                        else if(cursor.getString(3).equals("%"))
                        {
//                            for (int i=0;i<MyGloble.DataTable1.length;i++)
//                            {
//                                if (!MyGloble.DataTable1[i][0].equals(""))
//                                {
//                                    if (MyGloble.DataTable1[i][7].equals(Dcate) && !cursor.getString(3).equals("Rs") ) // Get item withut normal discount
//                                    {
//                                        MyGloble.DataTable1[i][3]=String.format("%.2f",(((Double.valueOf( MyGloble.DataTable1[i][1] )/100)* Double.valueOf(cursor.getString(2))) * Double.valueOf( MyGloble.DataTable1[i][2])));
//                                        MyGloble.DataTable1[i][4]=String.format("%.2f",(Double.valueOf( MyGloble.DataTable1[i][1]) * Double.valueOf( MyGloble.DataTable1[i][2])) -Double.valueOf((((Double.valueOf( MyGloble.DataTable1[i][1] )/100)* Double.valueOf(cursor.getString(2))) * Double.valueOf( MyGloble.DataTable1[i][2]))));
//                                        MyGloble.DataTable1[i][10]=String.format("%.2f",(Double.valueOf(cursor.getString(2))));
//                                    }
//                                    else
//                                    {
//                                        //  MyGloble.DataTable1[i][3]="0.00";
//                                    }
//
//                                }
//
//                            }


                            Cursor cursor4=db.GetC("select PDAInvoicedProduct.Quantity,PDAProduct.Price,PDAInvoicedProduct.ProductID from PDAInvoicedProduct,PDAProduct WHERE PDAProduct.ProductID=PDAInvoicedProduct.ProductID AND PDAProduct.Discount NOT IN('No','Rs') AND PDAProduct.Discount='"+Dcate+"' AND   InvoicedID='"+ MyGloble.InvoiceNo+"'   ");
                            if( cursor4.getCount() > 0) {
                                if (cursor4.moveToFirst())
                                {
                                    while (!cursor4.isAfterLast()) {

                                            Double temdis;
                                            Double temsub;
                                            temdis=0.0;
                                            temsub=0.0;
                                            temdis=(((Double.valueOf( cursor4.getString(cursor4.getColumnIndex("Price")))/100)*Double.valueOf(cursor.getString(2))) * Double.valueOf(cursor4.getString(cursor4.getColumnIndex("Quantity"))));
                                            temsub=((Double .valueOf(cursor4.getString(cursor4.getColumnIndex("Price")))*Double.valueOf(cursor4.getColumnIndex("Quantity")))-temdis);

                                            MyGloble.db.execSQL("Update PDAInvoicedProduct SET Discount='"+String.format("%.2f",(Double.valueOf(cursor.getString(2))))+"' , DisAmount='" + String.format("%.2f",temdis) + "',SubTot='" + String.format("%.2f",temsub) + "' WHERE ProductID = '"+cursor4.getString(cursor4.getColumnIndex("ProductID"))+"' AND  InvoicedID='"+ MyGloble.InvoiceNo+"'  " );
                                        cursor4.moveToNext();
                                    }
                                }
                            }


                        }

                        break;
                    }
                    else
                    {
//                        // Without Discount
//                        for (int i=0;i<MyGloble.DataTable1.length;i++)
//                        {
//                            if (!MyGloble.DataTable1[i][0].equals(""))
//                            {
//                                if (MyGloble.DataTable1[i][7].equals(Dcate))
//                                {
//                                    MyGloble.DataTable1[i][3]="0.00";
//                                    MyGloble.DataTable1[i][4]=String.format("%.2f",(Double.valueOf( MyGloble.DataTable1[i][1]) * Double.valueOf( MyGloble.DataTable1[i][2])));
//                                    MyGloble.DataTable1[i][10]="0.00";
//                                }
//                            }
//                        }


                    }
                    cursor.moveToNext();
                }


            }
        }
    }
    public void ReadTablelayout()
    {
        StringBuffer xpenses = new StringBuffer();

        //This will iterate through your table layout and get the total amount of cells.
        for(int i = 0; i < stk.getChildCount(); i++)
        {
            //Remember that .getChildAt() method returns a View, so you would have to cast a specific control.
            TableRow row = (TableRow) stk.getChildAt(i);
            //This will iterate through the table row.
            for(int j = 0; j < row.getChildCount(); j++)
            {
                //Button btn = (Button) row.getChildAt(j);

                // xpenses = xpenses.append(stk.getChildAt(i).toString());

                TextView mTextView = (TextView) row.getChildAt(j);

                Toast.makeText(getApplicationContext(),	mTextView.getText().toString(),Toast.LENGTH_SHORT).show();

            }
        }
    }
    public void rowclear(int i)
    {
        int deletep=0;
        int row_no=i/3;
        int col_no=i%3;

        deletep=row_no*3;

        for(int l=0; l<=2; l++){
            MyGloble.list.remove(deletep);
        }


        loadgrid("","","");

        Toast.makeText(getBaseContext(),
                "pic" + (i + 1) + " selected" + "ID" + row_no,
                Toast.LENGTH_SHORT).show();
    }
    public void ss()
    {
        for (int i = 0; i < 5; i++) {
            // Creation row
            final TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));

            // Creation textView
            final TextView text = new TextView(this);
            text.setText("Test" + i);
            text.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

            // Creation  button
            final Button button = new Button(this);
            button.setText("Delete");
            button.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final TableRow parent = (TableRow) v.getParent();
                    stk.removeView(parent);
                }
            });

            tableRow.addView(text);
            tableRow.addView(button);

            stk.addView(tableRow);
        }

    }
    public void loadgrid(String a1,String a2,String a3) {

        String[] letters = new String[10] ;

        Toast.makeText(getApplicationContext(), "New Item is Added Successfully..!",Toast.LENGTH_LONG).show();


//		if (MyGloble.list.size()==0)
//		{
//			MyGloble.list.add("000000000");
//			MyGloble.list.add("0000000000");
//			MyGloble.list.add("00000000000");
//		}
//
//		MyGloble.list.add(a1);
//		MyGloble.list.add(a2);
//		MyGloble.list.add(a3);




//	       ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, MyGloble.list);
//	       gridview.setAdapter(adapter);


    }
    private void datat() {

        TableRow tbrow0 = new TableRow(this);
        TextView tv0 = new TextView(this);
        tv0.setText("Product                ");
        tv0.setTextColor(Color.BLACK);
        tv0.setTypeface(null, Typeface.BOLD);
        tv0.setGravity(Gravity.LEFT);
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(this);
        tv1.setText("          Qty");
        tv1.setTextColor(Color.BLACK);
        tv1.setTypeface(null, Typeface.BOLD);
        tv1.setGravity(Gravity.RIGHT);
        tbrow0.addView(tv1);
        TextView tv2 = new TextView(this);
        tv2.setText("     F_Qty");
        tv2.setTextColor(Color.BLACK);
        tv2.setGravity(Gravity.RIGHT);
        tv2.setTypeface(null, Typeface.BOLD);
        tbrow0.addView(tv2);
        TextView tv3 = new TextView(this);
        tv3.setText("            Dis.");
        tv3.setTextColor(Color.BLACK);
        tv3.setTypeface(null, Typeface.BOLD);
        tv3.setGravity(Gravity.RIGHT);
        tbrow0.addView(tv3);

        TextView tv4 = new TextView(this);
        tv4.setText(" P_ID");
        tv4.setTextColor(Color.BLACK);
        tv4.setGravity(Gravity.RIGHT);
        tbrow0.addView(tv4);
        tv4.setTextSize(0);

        stk.addView(tbrow0);
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        Cursor cursor2=db.GetC("select PDAInvoicedProduct.ProductID,PDAProduct.Price,ProductName,PDAInvoicedProduct.Quantity,PDAInvoicedProduct.FreeIssueQty, PDAInvoicedProduct.DisAmount from PDAInvoicedProduct,PDAProduct WHERE PDAProduct.ProductID=PDAInvoicedProduct.ProductID AND  InvoicedID='"+ MyGloble.InvoiceNo+"'   ");
        if( cursor2.getCount() > 0)
        {
            if (cursor2.moveToFirst())

            {
                while(!cursor2.isAfterLast())
                {
                    TableRow tbrow = new TableRow(this);
                    TextView t1v = new TextView(this);
                    t1v.setText(cursor2.getString(cursor2.getColumnIndex("ProductName"))+"-" +cursor2.getString(cursor2.getColumnIndex("Price")));
                    t1v.setTextColor(Color.BLACK);
                    t1v.setGravity(Gravity.LEFT);
                    tbrow.addView(t1v);
                    TextView t2v = new TextView(this);
                    t2v.setText(cursor2.getString(cursor2.getColumnIndex("Quantity")));
                    t2v.setTextColor(Color.BLACK);
                    t2v.setGravity(Gravity.RIGHT);
                    tbrow.addView(t2v);
                    TextView t3v = new TextView(this);
                    t3v.setText(cursor2.getString(cursor2.getColumnIndex("FreeIssueQty")));
                    t3v.setTextColor(Color.BLACK);
                    t3v.setGravity(Gravity.RIGHT);
                    tbrow.addView(t3v);

                    TextView t4v = new TextView(this);
                    t4v.setText(String.format("%.2f",Double .valueOf(cursor2.getString(cursor2.getColumnIndex("DisAmount")))));
                    t4v.setTextColor(Color.BLACK);
                    t4v.setGravity(Gravity.RIGHT);
                    tbrow.addView(t4v);

                    TextView t5v = new TextView(this);
                    t5v.setText(cursor2.getString(cursor2.getColumnIndex("ProductID")));
                    t5v.setTextColor(Color.BLACK);
                    t5v.setGravity(Gravity.RIGHT);
                    tbrow.addView(t5v);
                    t5v.setTextSize(0);


//                    TableRow.LayoutParams params = new TableRow.LayoutParams(
//                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//                    params.height = 40;
//                    params.width = 500;
//                    params.setMargins(0, 0, 0, 0);
//                    tbrow.setLayoutParams(params);



//                    tbrow.setClickable(true);
//                 tbrow.setFocusable(true);
//                    tbrow.setFocusableInTouchMode(true);
              //      tbrow.setFocusableInTouchMode(true);

                  //  tbrow.setBackgroundResource(R.drawable.gradient_bg_hover);



                    tbrow.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {


                            TableRow tr1=(TableRow)view;
                            TextView tv4= (TextView)tr1.getChildAt(4);

                            //    MyGloble.invnoView=Integer .parseInt(tv1.getText().toString());
                            //  Toast.makeText(getApplicationContext(),tv4.getText().toString(),Toast.LENGTH_SHORT).show();
                            itemdelete =(tv4.getText().toString());

                          //  stk.setBackgroundColor(Color.parseColor("#DDDDDD"));
                            // MyGloble.RName=(tv2.getText().toString());
                            // stk.removeAllViews();
                            // stk.removeView(tr1);
                          //view.setClickable(true);
                            view.setFocusableInTouchMode(true);
                            view.setBackgroundColor(Color.MAGENTA);
                           // view.setBackgroundResource(R.drawable.gradient_bg_hover);




                        }
                    });

                    stk.addView(tbrow);



                    cursor2.moveToNext();
                }
            }

        }

        GetTotal();

    }
    private void datat_Free() {

        int count = stk1.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = stk1.getChildAt(i);
            if (child instanceof TableRow) ((ViewGroup) child).removeAllViews();
        }

        TableRow tbrow0 = new TableRow(this);
        TextView tv0 = new TextView(this);
        tv0.setText("Product                				");
        tv0.setTextColor(Color.BLACK);
        tv0.setTypeface(null, Typeface.BOLD);
        tv0.setGravity(Gravity.LEFT);
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(this);
        tv1.setText("         		Free Qty");
        tv1.setTextColor(Color.BLACK);
        tv1.setTypeface(null, Typeface.BOLD);
        tv1.setGravity(Gravity.RIGHT);
        tbrow0.addView(tv1);
        stk1.addView(tbrow0);

        Cursor cursor2=db.GetC("select * from PDAInvoicedProduct,PDAProduct WHERE PDAProduct.ProductID=PDAInvoicedProduct.FreeIssueID AND FreeIssueQty>0 AND  InvoicedID='"+ MyGloble.InvoiceNo+"'   ");
        if( cursor2.getCount() > 0) {
            if (cursor2.moveToFirst())

            {
                while (!cursor2.isAfterLast()) {


                    TableRow tbrow = new TableRow(this);
                    TextView t1v = new TextView(this);
                    t1v.setText(cursor2.getString(cursor2.getColumnIndex("ProductName"))+ "-" + cursor2.getString(cursor2.getColumnIndex("Price")));
                    t1v.setTextColor(Color.BLACK);
                    t1v.setGravity(Gravity.LEFT);
                    tbrow.addView(t1v);
                    TextView t2v = new TextView(this);
                    t2v.setText(cursor2.getString(cursor2.getColumnIndex("FreeIssueQty")));
                    t2v.setTextColor(Color.BLACK);
                    t2v.setGravity(Gravity.RIGHT);
                    tbrow.addView(t2v);
                    stk1.addView(tbrow);

                    cursor2.moveToNext();
                }
            }
        }


    }
    public void DialogBoxOneButton(String msg,String head )
    {
        AlertDialog alertDialog = new AlertDialog.Builder(InvtypeActivity.this).create();

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
