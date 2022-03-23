package com.example.mano.sfa;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Set;
import java.util.UUID;

public class PrintdataActivity extends AppCompatActivity {
    String a="";
    DatabaseHandler db;
    String DNME="";

    Double TOTAL_VatValue=0.0;
    Double TOTAL_VatSubtotal=0.0;


    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;

    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;

    byte[] readBuffer;
    int readBufferPosition;
    int counter;
    volatile boolean stopWorker;


    BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printdata);

        db = new DatabaseHandler(getApplicationContext());

        try {

            // we are goin to have three buttons for specific functions
            Button openButton = (Button) findViewById(R.id.button1);

            //	Toast.makeText(getApplicationContext(), "print pppppppppppppppppp",Toast.LENGTH_SHORT).show();
            //	Prittest();
            if(MyGloble.PRINTERTYPE.equals("WOOSIM"))
            {
                //Toast.makeText(getApplicationContext(), "print pppppppppppppppppp",Toast.LENGTH_SHORT).show();
                InvoicePrint();


            }else
            {

                //	Toast.makeText(getApplicationContext(), "Small print",Toast.LENGTH_SHORT).show();
                S_InvoicePrint();
            }



            // open bluetooth connection
            openButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(PrintdataActivity.this, InvprintActivity.class);
                    PrintdataActivity.this.startActivity(intent);
                }
            });



        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
    }


    public void Prittest() throws IOException
    {
        MyGloble.PRINTERTYPE="XXXXJ121601606" ;
        findBT();
        openBT();

        String a="";
        a=a+padLeft("DARLRY BUTLER & CO.LTD              ",48);
        a=a+padLeft("DARLRY BUTLER & CO.LTD              ",48);

        a=a+padLeft("DARLRY BUTLER & CO.LTD              ",48);
        a=a+padLeft("P.O.Box:1487 phne:24787880,2424311-4        ",48);
        a=a+padLeft("DARLRY BUTLER & CO.LTD              ",48);
        a=a+padLeft("No:98,Sri Sangsraja Mawatha,Colombo10       ",48);


        a=a+(a);
        closeBT();
    }
    public void InvoicePrint() throws IOException
    {
        a="";
        TOTAL_VatValue=0.0;
        TOTAL_VatSubtotal=0.0;



//		 Toast.makeText(getApplicationContext(),  MyGloble .Vat,Toast.LENGTH_SHORT).show();

        Header();
        ItemHeader();

//
//        for (int i=0;i<MyGloble.DataTable1.length;i++) {
//            if (!MyGloble.DataTable1[i][0].equals(""))
//            {

        Cursor cursor2=db.GetC("select PDAProduct.Price,PDAInvoicedProduct.ProductID,ProductName,PDAInvoicedProduct.Quantity,FreeIssueQty,DisAmount,SubTot from PDAInvoicedProduct,PDAProduct WHERE PDAProduct.ProductID=PDAInvoicedProduct.ProductID AND  InvoicedID='"+ MyGloble.InvoiceNo+"'   ");
        if( cursor2.getCount() > 0) {
            if (cursor2.moveToFirst())

            {
                while (!cursor2.isAfterLast()) {

                    if(MyGloble .Vat.equals("Yes"))
                    {
                        String V_pname="";
                        String vatRate="";
                        Double nonVatPrice=0.0;
                        Double VatSubtotal=0.0;
                        Double VatValue=0.0;
                        Double VatDisValue=0.0;
                        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                        Cursor cursor22=db.GetC("select NONVATPrice,VATRate,ProductName from PDAProduct WHERE ProductID='"+ cursor2.getString(cursor2.getColumnIndex("ProductID"))+"' ");
                        if( cursor22.getCount() > 0)
                        {
                            if (cursor22.moveToFirst()){
                                while(!cursor22.isAfterLast()){
                                    vatRate=cursor22.getString(1);
                                    nonVatPrice=Double.valueOf(cursor22.getString(0));
                                    V_pname=cursor22.getString(2);
                                    cursor22.moveToNext();
                                }
                            }
                        }

                        VatSubtotal=nonVatPrice *Double.valueOf(cursor2.getString(cursor2.getColumnIndex("Quantity")));
                        VatDisValue=(Double.valueOf(cursor2.getString(cursor2.getColumnIndex("DisAmount"))))*100/(Double.valueOf(vatRate)+100);
                        VatSubtotal=VatSubtotal-VatDisValue;

                        VatValue=VatSubtotal*Double.valueOf(vatRate)/100;

                        TOTAL_VatValue=TOTAL_VatValue+VatValue;
                        TOTAL_VatSubtotal=TOTAL_VatSubtotal+VatSubtotal;
                        InvoiceRow(V_pname+" - "+ String.format("%.2f",nonVatPrice ),cursor2.getString(cursor2.getColumnIndex("Quantity")),String.format( "%.2f",VatDisValue),String.format( "%.2f",VatSubtotal));

                    }else
                    {

                        InvoiceRow(cursor2.getString(cursor2.getColumnIndex("ProductName"))+"-"+cursor2.getString(cursor2.getColumnIndex("Price")),cursor2.getString(cursor2.getColumnIndex("Quantity")),String.format( "%.2f", Double.valueOf(cursor2.getString(cursor2.getColumnIndex("DisAmount")))),String.format( "%.2f", Double.valueOf(cursor2.getString(cursor2.getColumnIndex("SubTot")))));
                    }

                    cursor2.moveToNext();

                }
            }
        }


//            }
//        }

//		   Toast.makeText(getApplicationContext(),  "invoice...!",Toast.LENGTH_SHORT).show();
        String Dhead="NO";
//        for (int i=0;i<MyGloble.DataTable1.length;i++) {
//            if (!MyGloble.DataTable1[i][5].equals(""))
//            {
//                //Toast.makeText(getApplicationContext(),MyGloble.DataTable1[i][5],Toast.LENGTH_SHORT).show();
//                if (!MyGloble.DataTable1[i][5].equals("0") )
//                {
        Cursor cursor3=db.GetC("select * from PDAInvoicedProduct,PDAProduct WHERE PDAProduct.ProductID=PDAInvoicedProduct.ProductID AND FreeIssueQty>0 AND  InvoicedID='"+ MyGloble.InvoiceNo+"'   ");
        if( cursor3.getCount() > 0) {
            if (cursor3.moveToFirst())

            {
                while (!cursor3.isAfterLast()) {

                    if( Dhead.equals("NO"))
                    {
                        DisHeader();
                        Dhead="YES";
                    }
                    String  fname="";
                    DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                    Cursor cursor4=db.GetC("select * from PDAProduct WHERE ProductID='"+ cursor3.getString(cursor3.getColumnIndex("FreeIssueID"))+"' ");
                    if( cursor4.getCount() > 0)
                    {
                        if (cursor4.moveToFirst()){
                            while(!cursor4.isAfterLast()){
                                fname=cursor4.getString(6)+ "-" + cursor4.getString(2);
                                cursor4.moveToNext();
                            }
                        }

                    }

                    DicountRow(fname,cursor3.getString(cursor3.getColumnIndex("FreeIssueQty")));
                    cursor3.moveToNext();


                }
            }
        }


//                }
//
//            }
//        }
//		   Toast.makeText(getApplicationContext(),  "Free()...!",Toast.LENGTH_SHORT).show();
        String RedemHead="NO";
//        for (int i=0;i<MyGloble.DataTableREDEM.length;i++) {
//            if (!MyGloble.DataTableREDEM[i][5].equals(""))
//            {
//                //Toast.makeText(getApplicationContext(),MyGloble.DataTable1[i][5],Toast.LENGTH_SHORT).show();
//                if (!MyGloble.DataTableREDEM[i][5].equals("0") )
//                {
        Cursor cursor5=db.GetC("select * from PDAInvRedemption WHERE InvID='"+ MyGloble.InvoiceNo+"'   ");
        if( cursor5.getCount() > 0) {
            if (cursor5.moveToFirst()) {
                while (!cursor5.isAfterLast()) {

                    if( RedemHead.equals("NO"))
                    {
                        RedumHeader();
                        RedemHead="YES";
                    }
                    String  fname="";
                    DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                    Cursor cursor6=db.GetC("select * from PDAProduct WHERE ProductID='"+ cursor5.getString(cursor5.getColumnIndex("ProID"))+"' ");
                    if( cursor6.getCount() > 0)
                    {
                        if (cursor6.moveToFirst()){
                            while(!cursor6.isAfterLast()){
                                fname=cursor6.getString(6)+ "-" + cursor6.getString(2);
                                cursor6.moveToNext();
                            }
                        }
                    }
                    String  Rfname="";
                    Cursor cursor7=db.GetC("select * from PDAProduct WHERE ProductID='"+ cursor5.getString(cursor5.getColumnIndex("RedProID"))+"' ");
                    if( cursor7.getCount() > 0)
                    {
                        if (cursor7.moveToFirst()){
                            while(!cursor7.isAfterLast()){
                                Rfname=cursor7.getString(6);
                                cursor7.moveToNext();
                            }
                        }
                    }

                    DicountRow(fname,cursor5.getString(cursor5.getColumnIndex("Qty")) +" (" + Rfname + "-" + cursor5.getString(cursor5.getColumnIndex("RedProQty")) +")" );

                    cursor5.moveToNext();
                }
            }
        }


//                }
//
//            }
//        }

//		   Toast.makeText(getApplicationContext(),  "MKT return()...!",Toast.LENGTH_SHORT).show();

        String Rhead="NO";
//        for (int i=0;i<MyGloble.DataTableMKTR.length;i++) {
//            if ( !MyGloble.DataTableMKTR[i][0].equals(""))
//            {
        Cursor cursor10=db.GetC("select PDAProduct.Price, PDAMarketReturn.ProductID,ProductName,PDAMarketReturn.Qty,PDAMarketReturn.FIQty, PDAMarketReturn.ReturnMethod,PDAMarketReturn.Discount,PDAMarketReturn.SubTot from PDAMarketReturn,PDAProduct WHERE PDAProduct.ProductID=PDAMarketReturn.ProductID AND    InvID='"+ MyGloble.InvoiceNo+"'   ");
        if( cursor10.getCount() > 0) {
            if (cursor10.moveToFirst()) {
                while (!cursor10.isAfterLast()) {
                    if( Rhead.equals("NO"))
                    {
                        RetHeader();
                        ItemHeader();
                        Rhead="YES";
                    }

                    InvoiceRow(cursor10.getString(cursor10.getColumnIndex("ProductName"))+"-"+cursor10.getString(cursor10.getColumnIndex("Price"))+" - "+cursor10.getString(cursor10.getColumnIndex("ReturnMethod")),cursor10.getString(cursor10.getColumnIndex("Qty")),String.format( "%.2f", Double.valueOf(cursor10.getString(cursor10.getColumnIndex("Discount")))),String.format( "%.2f", Double.valueOf(cursor10.getString(cursor10.getColumnIndex("SubTot")))));

                    cursor10.moveToNext();
                }
            }
        }



//            }
//        }

//		   Toast.makeText(getApplicationContext(),  "MKT return Free()...!",Toast.LENGTH_SHORT).show();

        String R_Dhead="NO";
//        for (int i=0;i<MyGloble.DataTableMKTR.length;i++) {
//            if (!MyGloble.DataTableMKTR[i][5].equals(""))
//            {
//                //Toast.makeText(getApplicationContext(),MyGloble.DataTable1[i][5],Toast.LENGTH_SHORT).show();
//                if (!MyGloble.DataTableMKTR[i][5].equals("0") )
//                {

        Cursor cursor8=db.GetC("select PDAMarketReturn.ProductID,ProductName,PDAMarketReturn.Qty,PDAMarketReturn.FIQty,PDAMarketReturn.FID, PDAMarketReturn.ReturnMethod,PDAMarketReturn.SubTot from PDAMarketReturn,PDAProduct WHERE PDAProduct.ProductID=PDAMarketReturn.ProductID AND PDAMarketReturn.FIQty>0 AND   InvID='"+ MyGloble.InvoiceNo+"'   ");
        if( cursor8.getCount() > 0) {
            if (cursor8.moveToFirst()) {
                while (!cursor8.isAfterLast()) {

                    if( R_Dhead.equals("NO"))
                    {
                        RetDisHeader();
                        R_Dhead="YES";
                    }

                    String  fname="";
                    DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                    Cursor cursor9=db.GetC("select * from PDAProduct WHERE ProductID='"+ cursor8.getString(cursor8.getColumnIndex("FID"))+"' ");
                    if( cursor9.getCount() > 0)
                    {
                        if (cursor9.moveToFirst()){
                            while(!cursor9.isAfterLast()){
                                fname=cursor9.getString(6)+ "-" + cursor9.getString(2);
                                cursor9.moveToNext();
                            }
                        }

                    }

                    DicountRow(fname+" - "+cursor8.getString(cursor8.getColumnIndex("ReturnMethod")),cursor8.getString(cursor8.getColumnIndex("FIQty")));

                    cursor8.moveToNext();
                }
            }
        }



//                }
//
//            }
//
//
//        }

        MyGloble.Total=0.0;
//        for (int i=0;i<MyGloble.DataTable1.length;i++) {
//            if (!MyGloble.DataTable1[i][0].equals(""))
//            {
//
//                MyGloble.Total=MyGloble.Total + (Double.valueOf(MyGloble.DataTable1[i][4]));
//            }
//        }
        Cursor cursor11=db.GetC("select SubTot from PDAInvoicedProduct WHERE   InvoicedID='"+ MyGloble.InvoiceNo+"' ");
        if( cursor11.getCount() > 0) {
            if (cursor11.moveToFirst())
            {
                while (!cursor11.isAfterLast()) {
                    MyGloble.Total=MyGloble.Total+ Double.valueOf(cursor11.getString(cursor11.getColumnIndex("SubTot")));
                    cursor11.moveToNext();
                }
            }
        }


        MyGloble.TotalMKTR=0.0;
//        for (int i=0;i<MyGloble.DataTableMKTR.length;i++) {
//            if (!MyGloble.DataTableMKTR[i][0].equals(""))
//            {
//                MyGloble.TotalMKTR=MyGloble.TotalMKTR+ (Double.valueOf(MyGloble.DataTableMKTR[i][4]) ) ;
//            }
//        }
        Cursor cursor4=db.GetC("select SubTot from PDAMarketReturn WHERE   InvID='"+ MyGloble.InvoiceNo+"' ");
        if( cursor4.getCount() > 0) {
            if (cursor4.moveToFirst())
            {
                while (!cursor4.isAfterLast()) {

                    MyGloble.TotalMKTR = MyGloble.TotalMKTR + Double.valueOf(cursor4.getString(cursor4.getColumnIndex("SubTot")));
                    cursor4.moveToNext();
                }
            }
        }



        if(MyGloble.Vat.equals("Yes"))
        {
            InvEnd_Vat(String.format( "%.2f",TOTAL_VatSubtotal) ,String.format( "%.2f",MyGloble .TotalMKTR) ,  String.format("%.2f",TOTAL_VatSubtotal + TOTAL_VatValue - MyGloble .TotalMKTR),String.format("%.2f",TOTAL_VatValue));

        }else
        {
            InvEnd(String.format( "%.2f",MyGloble .Total) ,String.format( "%.2f",MyGloble .TotalMKTR) ,  String.format("%.2f",MyGloble .Total-MyGloble .TotalMKTR));

        }

        //  closeBT();


//				Toast.makeText(getApplicationContext(),  "Start()...!",Toast.LENGTH_SHORT).show();

        findBT();
//		   Toast.makeText(getApplicationContext(),  "findBT()...!",Toast.LENGTH_SHORT).show();

        openBT();
//			Toast.makeText(getApplicationContext(),  "openBT()...!",Toast.LENGTH_SHORT).show();

        sendData(a);
//			Toast.makeText(getApplicationContext(),  "sendData()...!",Toast.LENGTH_SHORT).show();

        closeBT();
//		   MyGloble.invedit="NO";
//			  startActivity(new Intent("com.example.mobile.INVPRINT"));






//			  DatabaseHandler db = new DatabaseHandler(getApplicationContext());
//			  db.ClearDatatable();
//			  db.ClearValues();


//			  Toast.makeText(getApplicationContext(),DNME,Toast.LENGTH_LONG).show();
//			  Toast.makeText(getApplicationContext(),  "Invoice Printing is Successfully Completed...!",Toast.LENGTH_SHORT).show();
//			  startActivity(new Intent("com.example.mobile.INVLIST"));
//			  if(mBluetoothAdapter == null) {
//
//
//				  startActivity(new Intent("com.example.mobile.INVLIST"));
//			  }


    }
    public void Header() throws IOException {
//		a=a+("Client\n");
//		a=a+("\n");
//		a=a+("\n");
//		a=a+("                             D A R L R Y   B U T L E R   &   C O . L T D\n");
//		a=a+("                             ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n");
//		a=a+("                                 No:98,Sri Sangsraja Mawatha,Colombo10\n");
//		a=a+("                                 P.O.Box:1487 phone:24787880,2424311-4\n");
//		a=a+("                                      Vat Reg.No-2040046337000\n");
//		a=a+("Client\n");
//		a=a+("\n");
//		a=a+("\n");
//		a=a+("                             D A R L R Y   B U T L E R   &   C O . L T D\n");
//		a=a+("                             ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n");
//		a=a+("                                 No:98,Sri Sangsraja Mawatha,Colombo10\n");
//		a=a+("                                 P.O.Box:1487 phone:24787880,2424311-4\n");
//		a=a+("                                      Vat Reg.No-2040046337000\n");
//		a=a+("Client\n");
//		a=a+("\n");
//		a=a+("\n");
//		a=a+("                             D A R L R Y   B U T L E R   &   C O . L T D\n");
//		a=a+("                             ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n");
//		a=a+("                                 No:98,Sri Sangsraja Mawatha,Colombo10\n");
//		a=a+("                                 P.O.Box:1487 phone:24787880,2424311-4\n");
//		a=a+("                                      Vat Reg.No-2040046337000\n");
//		a=a+("Client\n");
//		a=a+("\n");
//		a=a+("\n");
//		a=a+("                             D A R L R Y   B U T L E R   &   C O . L T D\n");
//		a=a+("                             ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n");
//		a=a+("                                 No:98,Sri Sangsraja Mawatha,Colombo10\n");
//		a=a+("                                 P.O.Box:1487 phone:24787880,2424311-4\n");
//		a=a+("                                      Vat Reg.No-2040046337000\n");
//		a=a+("Client\n");
//		a=a+("\n");
//		a=a+("\n");
//		a=a+("                             D A R L R Y   B U T L E R   &   C O . L T D\n");
//		a=a+("                             ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n");
//		a=a+("                                 No:98,Sri Sangsraja Mawatha,Colombo10\n");
//		a=a+("                                 P.O.Box:1487 phone:24787880,2424311-4\n");
//		a=a+("                                      Vat Reg.No-2040046337000\n");
//
//
//		a=a+("Client\n");
//		a=a+("\n");
//		a=a+("\n");
//		a=a+("                             D A R L R Y   B U T L E R   &   C O . L T D\n");
//		a=a+("                             ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n");
//		a=a+("                                 No:98,Sri Sangsraja Mawatha,Colombo10\n");
//		a=a+("                                 P.O.Box:1487 phone:24787880,2424311-4\n");
//		a=a+("                                      Vat Reg.No-2040046337000\n");
//		a=a+("Client\n");
//		a=a+("\n");
//		a=a+("\n");
//		a=a+("                             D A R L R Y   B U T L E R   &   C O . L T D\n");
//		a=a+("                             ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n");
//		a=a+("                                 No:98,Sri Sangsraja Mawatha,Colombo10\n");
//		a=a+("                                 P.O.Box:1487 phone:24787880,2424311-4\n");
//		a=a+("                                      Vat Reg.No-2040046337000\n");
//		a=a+("Client\n");
//		a=a+("\n");
//		a=a+("\n");
//		a=a+("                             D A R L R Y   B U T L E R   &   C O . L T D\n");
//		a=a+("                             ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n");
//		a=a+("                                 No:98,Sri Sangsraja Mawatha,Colombo10\n");
//		a=a+("                                 P.O.Box:1487 phone:24787880,2424311-4\n");
//		a=a+("                                      Vat Reg.No-2040046337000\n");
//		a=a+("Client\n");
//		a=a+("\n");
//		a=a+("\n");
//		a=a+("                             D A R L R Y   B U T L E R   &   C O . L T D\n");
//		a=a+("                             ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n");
//		a=a+("                                 No:98,Sri Sangsraja Mawatha,Colombo10\n");
//		a=a+("                                 P.O.Box:1487 phone:24787880,2424311-4\n");
//		a=a+("                                      Vat Reg.No-2040046337000\n");
//		a=a+("Client\n");
//		a=a+("\n");
//		a=a+("\n");
//		a=a+("                             D A R L R Y   B U T L E R   &   C O . L T D\n");
//		a=a+("                             ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n");
//		a=a+("                                 No:98,Sri Sangsraja Mawatha,Colombo10\n");
//		a=a+("                                 P.O.Box:1487 phone:24787880,2424311-4\n");
//		a=a+("########################################################################\n");

        //==================================================================
        a=a+("\n");
        a=a+("\n");
        a=a+(padRight(MyGloble.BillCopy,48) + "\n");
        MyGloble.BillCopy="";

        String stname="";
        String stadd="";
        String sttp="";
        String vatRNo="";

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        Cursor cursor2=db.GetC("select * from PDARep   Where LoginName<>''");
        if( cursor2.getCount() > 0)
        {
            if (cursor2.moveToFirst()){
                while(!cursor2.isAfterLast()){
                    MyGloble.Repname=cursor2.getString(1);
                    stname=cursor2.getString(9);
                    stadd=cursor2.getString(12);
                    sttp=cursor2.getString(13);
                    if (!cursor2.isNull(8))
                    {
                        vatRNo=cursor2.getString(8);
                    }

                    cursor2.moveToNext();
                }
            }

        }
        if(MyGloble.Vat.equals("Yes"))
        {
            a=a+("<== TAX INVOICE ==>".replace("", " ") +"\n");

        }


        SimpleDateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
        String date = df.format(Calendar.getInstance().getTime());
        a=a+("Date : "+date +"\n");
        a=a+("\n");


        stname = stname.replace("", " ");
        a=a+(stname.toUpperCase() +"\n");
        String star="";
        for(int q=0;q<stname.length();q++)
        {
            star=star+"^";
        }
        a=a+(star+"\n");

        a=a+(stadd+"\n");
        a=a+(sttp+"\n");
        if(!vatRNo.equals(""))
        {
            a=a+("VAT Reg.No  : "+vatRNo +"\n");
        }
        a=a+("(Authorized Distributor for CW MACKIE PLC)\n");
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

        String retVATNo="";
        Cursor cursor22=db.GetC("select * from PDARetailer Where ID='"+MyGloble.RID+"'");
        if( cursor22.getCount() > 0)
        {
            if (cursor22.moveToFirst()){
                while(!cursor22.isAfterLast()){
                    retVATNo=cursor22.getString(3);
                    cursor22.moveToNext();
                }
            }
        }

        a=a+("\n");
        a=a+(padRight("Inv.No : "+MyGloble.Display_InvoiceNo,45)+padRight("Bill Type  : "+billtype ,45) +"\n");
        a=a+("\n");
        a=a+(padRight("Retailer :"+MyGloble.RName,45)+padRight("Sales Rep : "+MyGloble.Repname,45) +"\n");
        a=a+("Address :"+MyGloble.Radd +"\n");
        a=a+("VAT No : "+retVATNo +"\n");
        a=a+(padRight("..................................................................................................",90).substring(0,90)+"\n");
        a=a+("                                    <<__Invoiced Products__>>\n");



    }
    public void ItemHeader() throws IOException {
        //a=a+("\n");
        a=a+(padRight("Product",50)+padLeft("Qty",10)+padLeft("Dis.",15) +padLeft("Total",15)+"\n");
        //a=a+(padRight("-------",50)+padLeft("---",10)+padLeft("----",15) +padLeft("-----",15)+"\n");
        //a=a+("\n");
    }
    public void DisHeader() throws IOException {
        a=a+(padRight(".................................................................................................",90).substring(0,90)+"\n");
        //	a=a+("\n");
        a=a+("                                   <<__Quantity Discount__>>\n");
        //	a=a+("\n");
    }
    public void RedumHeader() throws IOException {
        a=a+(padRight(".................................................................................................",90).substring(0,90)+"\n");
        //a=a+("\n");
        a=a+("                                      <<__Redemption__>>\n");
        //	a=a+("\n");
    }
    public void RetDisHeader() throws IOException {
        a=a+(padRight("...............................................................................................",90).substring(0,90)+"\n");
        //a=a+("\n");
        a=a+("                                <<__Return Quantity Discount__>>\n");
        //a=a+("\n");
    }
    public void RetHeader() throws IOException {
        a=a+(padRight("..............................................................................................",90).substring(0,90)+"\n");
        //a=a+("\n");
        a=a+("                                 <<__Market Return Products__>>\n");
        //a=a+("\n");
    }
    public void DicountRow(String item, String Qty) throws IOException
    {
        a=a+(padRight(item,58)+padRight(Qty,32)+"\n");
    }
    public void InvEnd(String tot, String ret, String net) throws IOException {
        a=a+(padRight("===============================================================================================",90).substring(0,90)+"\n");
        //a=a+("\n");
        a=a+(padRight("Total value : ",45)+padLeft(tot,45) +"\n");
        a=a+(padRight("Market Return Value : ",45)+padLeft(ret,45) +"\n");
        a=a+(padRight("Net Value : ",45)+padLeft(net,45) +"\n");
        a=a+(padRight("Received goods in good condition.:_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _  _ _ _" ,90).substring(0,90)+"\n");
        a=a+(padRight("=================================================================================================",90).substring(0,90)+"\n");
        a=a+("\n");
        a=a+("\n");
    }
    public void InvEnd_Vat(String tot, String ret, String net,String vat) throws IOException {
        a=a+(padRight("===============================================================================================",90).substring(0,90)+"\n");
//    	a=a+("\n");
        a=a+(padRight("Total value : ",45)+padLeft(tot,45) +"\n");
        a=a+(padRight("Vat value : ",45)+padLeft(vat,45) +"\n");
        a=a+(padRight("Market Return Value : ",45)+padLeft(ret,45) +"\n");
        a=a+(padRight("Net Value : ",45)+padLeft(net,45) +"\n");
        a=a+("Received good is good condition.:_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _   \n");
        a=a+(padRight("===============================================================================================",90).substring(0,90)+"\n");
        a=a+("\n");
        a=a+("\n");
    }
    public void InvoiceRow(String item, String Qty,String Dis,String tot) throws IOException {
        String row="";
        String ITEM="";
        String QTY="";
        String DIS="";
        String TOTAL="";


        QTY = padLeft(Qty, 10) ;
        DIS = padLeft(Dis, 15) ;
        TOTAL = padLeft(tot, 15);
        ITEM = padRight(item, 50) ;


        //	ITEM = padRight(item, 40) + "*";
//		  for (int i = 0; i < (70 - item.length()); i++)
//		    {
//			  item=item+"";
//		    }

//		StringBuilder sb = new StringBuilder();
//	    sb.append(item);
//	    for (int i = 0; i < (70 - item.length()); i++)
//	    {
//	        sb.insert(0, " ");
//	    }
//	    ITEM=sb.toString();

        // ITEM=item;

        row=ITEM+QTY+DIS+TOTAL;

        //	Toast.makeText(getApplicationContext(),  row,Toast.LENGTH_SHORT).show();
        //a=a+(ITEM);
        a=a+(row+"\n");


    }
    public static String padLeft(String s, int n) {
        return String.format("%1$" + n + "s", s);
    }
    public static String padRight(String s, int n) {
        return String.format("%1$-" + n + "s", s);
    }
    void findBT() {

        try {
//			if (mBluetoothAdapter != null)
//				mBluetoothAdapter.cancelDiscovery();

            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if (mBluetoothAdapter == null) {
                Toast.makeText(getApplicationContext(),"mBluetoothAdapter Null",Toast.LENGTH_LONG).show();
            }

            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBluetooth = new Intent(
                        BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetooth, 0);
            }



            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter
                    .getBondedDevices();
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {

                    // Toast.makeText(getApplicationContext(), device.getName(),Toast.LENGTH_LONG).show();
                    // MP300 is the name of the bluetooth printer device
                    if (device.getName().equals(MyGloble.PRINTERTYPE)) {
                        mmDevice = device;
                        DNME=device.getName();
                        break;
                    }
                }
            }



            //	myLabel.setText("Bluetooth Device Found");
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    void openBT() throws IOException {
        try {
            // Standard SerialPortService ID
            //--------	UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

            // UUID SPP_UUID = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");
            //UUID uuid = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

            //	UUID uuid = mmDevice.getUuids()[0].getUuid();

            //--------mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);

            mmSocket = mmDevice.createInsecureRfcommSocketToServiceRecord(uuid);
            //==============================================================================
//			try{
//				IntentFilter intentFilter = new IntentFilter();
//				intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
//				mmSocket.connect();
//		    }
//		    catch(IOException e){
//		        try {
////		            Log.i(TAG,"Trying fallback...");
//		        	Toast.makeText(getApplicationContext(),"Trying fallback...",Toast.LENGTH_LONG).show();
//		            mmSocket =(BluetoothSocket) mmDevice.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(mmDevice,1);
//		            mmSocket.connect();
////		            Log.i(TAG,"Connected");
//		          } catch (Exception e2) {
////		            Log.e(TAG, "Couldn't establish Bluetooth connection!");
//		        	  Toast.makeText(getApplicationContext(),"Couldn't establish Bluetooth connection!",Toast.LENGTH_LONG).show();
//		            try {
//		              mmSocket.close();
//		            } catch (IOException e3) {
////		              Log.e(TAG, "unable to close() " + mSocketType + " socket during connection failure", e3);
//		            }
////		            connectionFailed();
//		            return;
//		          }
//
//
//		    }
            //==================================================================

            if (!connectToPrinter())
            {
                Toast.makeText(getApplicationContext(),"Please Restart Your Blutooth Printer / Application..???",Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(),"Please Restart Your Blutooth Printer / Application..???",Toast.LENGTH_LONG).show();
                closeBT() ;
            }else
            {
                //Toast.makeText(getApplicationContext(),"AAASSSSSSSSSS..???",Toast.LENGTH_LONG).show();
                DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                Cursor cursor2=db.GetC("select * from PDAInvoice WHERE InvoiceID='"+ MyGloble.InvoiceNo+"' AND Cancel <> 'Yes' ");
                if( cursor2.getCount() > 0)
                {
                    // Toast.makeText(getApplicationContext(), "AAAAAAAAAAAAAAAAAAAA",Toast.LENGTH_LONG).show();
                    if (cursor2.moveToFirst()){
                        while(!cursor2.isAfterLast()){

//                            if(cursor2.getString(cursor2.getColumnIndex("Printed")).equals(""))
//                            { //Toast.makeText(getApplicationContext(), "1111111111111111",Toast.LENGTH_LONG).show();
//                                //Toast.makeText(getApplicationContext(), "empty",Toast.LENGTH_LONG).show();
//                                update("Printed-1", MyGloble.InvoiceNo);
//                            }else if(cursor2.getString(cursor2.getColumnIndex("Printed")).equals("Printed-1"))
//                            { //Toast.makeText(getApplicationContext(), "2222222222222222",Toast.LENGTH_LONG).show();
//                                //Toast.makeText(getApplicationContext(), "empty",Toast.LENGTH_LONG).show();
//                                update("Printed-2", MyGloble.InvoiceNo);
//                            }
//                            else if(cursor2.getString(cursor2.getColumnIndex("Printed")).equals("Not-Printed"))
//                            { //Toast.makeText(getApplicationContext(), "2222222222222222",Toast.LENGTH_LONG).show();
//                                //Toast.makeText(getApplicationContext(), "empty",Toast.LENGTH_LONG).show();
//                                update("Printed-1", MyGloble.InvoiceNo);
//                            }
                           cursor2.moveToNext();
                        }
                    }
                }

            }





            mmOutputStream = mmSocket.getOutputStream();
            mmInputStream = mmSocket.getInputStream();

            beginListenForData();

            //myLabel.setText("Bluetooth Opened");
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public boolean connectToPrinter() throws IOException
    {

        mBluetoothAdapter.getDefaultAdapter().cancelDiscovery();

        //   BluetoothDevice device = getPrinterByName(printerName);
        //Toast.makeText(getApplicationContext(),"connectToPrinter()",Toast.LENGTH_LONG).show();

        if (mmSocket != null)
        {
            mmSocket.close();
        }


        try {

            Method m=mmDevice.getClass().getMethod("createRfcommSocket", new Class[]{int.class});
            mmSocket=(BluetoothSocket) m.invoke(mmDevice, 1);

        } catch (Exception e) {

            e.printStackTrace();
        }


        if (mmSocket == null)
            return false;

        try
        {
            mmSocket.connect();
        }catch(IOException e){

            return false;
        }

        // mmSocket.connect();
        return true;
    }

    /*
     * After opening a connection to bluetooth printer device,
     * we have to listen and check if a data were sent to be printed.
     */
    void beginListenForData() {
        try {
            final Handler handler = new Handler();

            // This is the ASCII code for a newline character
            final byte delimiter = 10;

            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];

            workerThread = new Thread(new Runnable() {
                public void run() {
                    while (!Thread.currentThread().isInterrupted()
                            && !stopWorker) {

                        try {

                            int bytesAvailable = mmInputStream.available();
                            if (bytesAvailable > 0) {
                                byte[] packetBytes = new byte[bytesAvailable];
                                mmInputStream.read(packetBytes);
                                for (int i = 0; i < bytesAvailable; i++) {
                                    byte b = packetBytes[i];
                                    if (b == delimiter) {
                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(readBuffer, 0,
                                                encodedBytes, 0,
                                                encodedBytes.length);
                                        final String data = new String(
                                                encodedBytes, "US-ASCII");
                                        readBufferPosition = 0;

                                        handler.post(new Runnable() {
                                            public void run() {
                                                //	myLabel.setText(data);
                                                Toast.makeText(getApplicationContext(), data,Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    } else {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }

                        } catch (IOException ex) {
                            stopWorker = true;
                        }

                    }
                }
            });

            workerThread.start();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * This will send data to be printed by the bluetooth printer
     */
    void sendData(final String msg) throws IOException {
        try {

            // the text typed by the user
            //String msg = myTextbox.getText().toString();
            //	String msg ="                                      DARLRY BUTLER AND CO.LTD";
            //	msg += "\n";
            //Toast.makeText(getApplicationContext(), "print pppppppppppppppppp",Toast.LENGTH_SHORT).show();

//			byte FONT_TYPE;
//
//            byte[] printformat = { 0x1B, 0*21 };
//            mmOutputStream.write(printformat);
//
//			Thread t = new Thread() {
//                public void run() {
//                    try {
            //mmOutputStream.write(msg.getBytes());
            //=====================================================
            //byte[] arrayOfByte1 = { 27, 33, 0 };
            // byte[] format = { 27, 33, 0 };

            //	 format[2] = ((byte)(0x8 | arrayOfByte1[2]));

            // mmOutputStream.write(format);
            mmOutputStream.write(msg.getBytes(),0,msg.getBytes().length);

            Thread.sleep(2000);
//                    } catch (Exception e) {
//                        Log.e("Main", "Exe ", e);
//                    }
//                }
//            };
//            t.start();
//
//			  DataOutputStream dOut = null;
//			    dOut = new DataOutputStream(mmSocket.getOutputStream());
//			    // Send message
//			    dOut.writeBytes(msg);
//			    dOut.flush();
//


            //	Toast.makeText(getApplicationContext(), "print sssssssssssssssssssssssssssssssssssssssss",Toast.LENGTH_SHORT).show();

            // tell the user data were sent
            //	myLabel.setText("Data Sent");

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * Close the connection to bluetooth printer.
     */
    void closeBT() throws IOException {
        try {
            stopWorker = true;
            mmOutputStream.close();
            mmInputStream.close();
            mmSocket.close();
            mBluetoothAdapter=null;
            //myLabel.setText("Bluetooth Closed");

            MyGloble.invedit="NO";
            Intent intent = new Intent(PrintdataActivity.this, InvprintActivity.class);
            PrintdataActivity.this.startActivity(intent);


        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void update(String a ,String b) {

        MyGloble.db.execSQL("Update PDAInvoice SET Printed='" + a + "' WHERE InvoiceID = '"+b +"'" );

//		  DatabaseHandler db = new DatabaseHandler(getApplicationContext());
//		  db.ClearDatatable();
//		  db.ClearValues();

        //	 Toast.makeText(getApplicationContext(), "You Have Printed Copy Bill Of Invoice ",Toast.LENGTH_LONG).show();

    }


    public void InitPrinter()
    {

        try
        {
            if(!bluetoothAdapter.isEnabled())
            {
                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetooth, 0);
            }

            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

            if(pairedDevices.size() > 0)
            {
                for(BluetoothDevice device : pairedDevices)
                {
                    if(device.getName().equals(MyGloble.PRINTERTYPE)) //Note, you will need to change this to match the name of your device
                    {
                        mmDevice = device;
                        break;
                    }
                }

                UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //Standard SerialPortService ID
                //Method m = mmDevice.getClass().getMethod("createRfcommSocket", new Class[] { int.class });
                //mmSocket = (BluetoothSocket) m.invoke(mmDevice, uuid);
                mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
                bluetoothAdapter.cancelDiscovery();
                if(mmDevice.getBondState()==2)
                {
                    mmSocket.connect();
                    mmOutputStream = mmSocket.getOutputStream();
                }
                else
                {
//	                value+="Device not connected";
//	                txtLogin.setText(value);
                }
            }
            else
            {
//	            value+="No Devices found";
//	            txtLogin.setText(value);
                return;
            }
        }
        catch(Exception ex)
        {
//	        value+=ex.toString()+ "\n" +" InitPrinter \n";
//	        txtLogin.setText(value);
        }
    }

    public void IntentPrint(String txtvalue)
    {
        byte[] buffer = txtvalue.getBytes();
        byte[] PrintHeader = { (byte) 0xAA, 0x55,2,0 };
        PrintHeader[3]=(byte) buffer.length;
        InitPrinter();
        if(PrintHeader.length<128)
        {
//	        value+="\nValue is more than 128 size\n";
//	        txtLogin.setText(value);

            try
            {
                for(int i=0;i<=PrintHeader.length-1;i++)
                {
                    mmOutputStream.write(PrintHeader[i]);
                }
                for(int i=0;i<=buffer.length-1;i++)
                {
                    mmOutputStream.write(buffer[i]);
                }
                mmOutputStream.close();
                mmSocket.close();
            }
            catch(Exception ex)
            {
//	            value+=ex.toString()+ "\n" +"Excep IntentPrint \n";
//	            txtLogin.setText(value);
            }
        }
    }




    //===============================================================================================================================================================================

    //=============================================================================================================================================================================









    public void S_InvoicePrint() throws IOException
    {
        a="";
        findBT();
        openBT();

        S_Header();
        S_ItemHeader();

        for (int i=0;i<MyGloble.DataTable1.length;i++) {
            if ( !MyGloble.DataTable1[i][0].equals(""))
            {

                if(MyGloble .Vat.equals("Yes"))
                {
                    String V_pname="";
                    String vatRate="";
                    Double nonVatPrice=0.0;
                    Double VatSubtotal=0.0;
                    Double VatValue=0.0;
                    DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                    Cursor cursor22=db.GetC("select NONVATPrice,VATRate,ProductName from PDAProduct WHERE ProductID='"+ MyGloble.DataTable1[i][8]+"' ");
                    if( cursor22.getCount() > 0)
                    {
                        if (cursor22.moveToFirst()){
                            while(!cursor22.isAfterLast()){
                                vatRate=cursor22.getString(1);
                                nonVatPrice=Double.valueOf(cursor22.getString(0));
                                V_pname=cursor22.getString(2);
                                cursor22.moveToNext();
                            }
                        }
                    }

                    VatSubtotal=nonVatPrice *Double.valueOf(MyGloble.DataTable1[i][2]);
                    VatValue=VatSubtotal*Double.valueOf(vatRate)/100;
                    VatSubtotal=VatSubtotal-(Double.valueOf(MyGloble.DataTable1[i][3]));

                    TOTAL_VatValue=TOTAL_VatValue+VatValue;
                    VatSubtotal=VatSubtotal-(Double.valueOf(MyGloble.DataTable1[i][3])*(100-Double.valueOf(vatRate))/100);
                    TOTAL_VatSubtotal=TOTAL_VatSubtotal+VatSubtotal;
                    S_InvoiceRow(V_pname+" - "+ String.format("%.2f",nonVatPrice ),MyGloble.DataTable1[i][2],MyGloble.DataTable1[i][3],String.format( "%.2f",VatSubtotal));

                }else
                {
                    S_InvoiceRow(MyGloble.DataTable1[i][0],MyGloble.DataTable1[i][2],MyGloble.DataTable1[i][3],MyGloble.DataTable1[i][4]);
                }


                //S_InvoiceRow(MyGloble.DataTable1[i][0],MyGloble.DataTable1[i][2],MyGloble.DataTable1[i][3],MyGloble.DataTable1[i][4]);
            }
        }

        String Dhead="NO";
        for (int i=0;i<MyGloble.DataTable1.length;i++) {
            if (!MyGloble.DataTable1[i][5].equals(""))
            {
                //Toast.makeText(getApplicationContext(),MyGloble.DataTable1[i][5],Toast.LENGTH_SHORT).show();
                if (!MyGloble.DataTable1[i][5].equals("0") )
                {

                    if( Dhead.equals("NO"))
                    {
                        S_DisHeader();
                        Dhead="YES";
                    }
                    String  fname="";
                    DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                    Cursor cursor2=db.GetC("select * from PDAProduct WHERE ProductID='"+ MyGloble.DataTable1[i][6]+"' ");
                    if( cursor2.getCount() > 0)
                    {
                        if (cursor2.moveToFirst()){
                            while(!cursor2.isAfterLast()){
                                fname=cursor2.getString(6)+ "-" + cursor2.getString(2);
                                cursor2.moveToNext();
                            }
                        }

                    }

                    S_DicountRow(fname,MyGloble.DataTable1[i][5]);
                }

            }
        }

        String RedemHead="NO";
        for (int i=0;i<MyGloble.DataTableREDEM.length;i++) {
            if (!MyGloble.DataTableREDEM[i][5].equals(""))
            {
                //Toast.makeText(getApplicationContext(),MyGloble.DataTable1[i][5],Toast.LENGTH_SHORT).show();
                if (!MyGloble.DataTableREDEM[i][5].equals("0") )
                {

                    if( RedemHead.equals("NO"))
                    {
                        S_RedumHeader();
                        RedemHead="YES";
                    }
                    String  fname="";
                    DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                    Cursor cursor2=db.GetC("select * from PDAProduct WHERE ProductID='"+ MyGloble.DataTableREDEM[i][6]+"' ");
                    if( cursor2.getCount() > 0)
                    {
                        if (cursor2.moveToFirst()){
                            while(!cursor2.isAfterLast()){
                                fname=cursor2.getString(6)+ "-" + cursor2.getString(2);
                                cursor2.moveToNext();
                            }
                        }

                    }

                    S_DicountRow(fname,MyGloble.DataTableREDEM[i][5] +"(" + fname + "-" + MyGloble.DataTableREDEM[i][2] +")" );
                }

            }
        }

        //========================================================================================='

        String Rhead="NO";
        for (int i=0;i<MyGloble.DataTableMKTR.length;i++) {
            if (!MyGloble.DataTableMKTR[i][0].equals(""))
            {
                if( Rhead.equals("NO"))
                {
                    S_RetHeader();
                    S_ItemHeader();
                    Rhead="YES";
                }

                S_InvoiceRow(MyGloble.DataTableMKTR[i][0] +"-"+MyGloble.DataTableMKTR[i][7],MyGloble.DataTableMKTR[i][2],MyGloble.DataTableMKTR[i][3],MyGloble.DataTableMKTR[i][4]);
            }
        }

        String R_Dhead="NO";
        for (int i=0;i<MyGloble.DataTableMKTR.length;i++) {
            if (!MyGloble.DataTableMKTR[i][5].equals(""))
            {
                //Toast.makeText(getApplicationContext(),MyGloble.DataTable1[i][5],Toast.LENGTH_SHORT).show();
                if (!MyGloble.DataTableMKTR[i][5].equals("0") )
                {
                    if( R_Dhead.equals("NO"))
                    {
                        S_RetDisHeader();
                        R_Dhead="YES";
                    }

                    String  fname="";
                    DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                    Cursor cursor2=db.GetC("select * from PDAProduct WHERE ProductID='"+ MyGloble.DataTableMKTR[i][6]+"' ");
                    if( cursor2.getCount() > 0)
                    {
                        if (cursor2.moveToFirst()){
                            while(!cursor2.isAfterLast()){
                                fname=cursor2.getString(6)+ "-" + cursor2.getString(2);
                                cursor2.moveToNext();
                            }
                        }

                    }

                    S_DicountRow(fname,MyGloble.DataTableMKTR[i][5]);
                }

            }


        }
        //==============================================================================================
//		   Double net= MyGloble .Total - MyGloble.TotalMKTR;
//		   S_InvEnd(String.format( "%.2f",MyGloble .Total) ,String.format( "%.2f",MyGloble .TotalMKTR) ,  String.format("%.2f",net));
//
//


        if(MyGloble.Vat.equals("Yes"))
        {
            S_InvEnd_Vat(String.format( "%.2f",TOTAL_VatSubtotal) ,String.format( "%.2f",MyGloble .TotalMKTR) ,  String.format("%.2f",TOTAL_VatSubtotal + TOTAL_VatValue - MyGloble .TotalMKTR),String.format("%.2f",TOTAL_VatValue));

        }else
        {
            S_InvEnd(String.format( "%.2f",MyGloble .Total) ,String.format( "%.2f",MyGloble .TotalMKTR) ,  String.format("%.2f",MyGloble .Total-MyGloble .TotalMKTR));

        }









        sendData(a);

        closeBT();

//			  DatabaseHandler db = new DatabaseHandler(getApplicationContext());
//			  db.ClearDatatable();
//			  db.ClearValues();
//
        MyGloble.invedit="NO";
        Toast.makeText(getApplicationContext(),  "Invoice Printing is Successfully Completed...!",Toast.LENGTH_SHORT).show();
        startActivity(new Intent("com.example.mobile.INVPRINT"));

    }
    public void S_Header() throws IOException {

        a=a+(padRight(MyGloble.BillCopy,48) + "\n");
        MyGloble.BillCopy="";

//		a=a+("\n");
//		a=a+("\n");
//		a=a+(padLeft("DARLRY BUTLER & CO.LTD              ",48));
//		a=a+(padLeft("^^^^^^^^^^^^^^^^^^^^^^              ",48));
//		a=a+(padLeft("No:98,Sri Sangsraja Mawatha,Colombo10       ",48));
//		a=a+(padLeft("P.O.Box:1487 phne:24787880,2424311-4        ",48));
//		a=a+(padLeft("Vat Reg.No-2040046337000               ",48));

        String stname="";
        String stadd="";
        String sttp="";

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        Cursor cursor2=db.GetC("select * from PDARep   Where LoginName<>'' ");
        if( cursor2.getCount() > 0)
        {
            if (cursor2.moveToFirst()){
                while(!cursor2.isAfterLast()){
                    MyGloble.Repname=cursor2.getString(1);
                    stname=cursor2.getString(9);
                    stadd=cursor2.getString(12);
                    sttp=cursor2.getString(13);
                    cursor2.moveToNext();
                }
            }

        }

        stname = stname.replace("", " ");
        a=a+(padRight(stname.toUpperCase(),48));
        String star="";
        for(int q=0;q<stname.length();q++)
        {
            star=star+"^";
        }
        a=a+(padRight(star,48));
        a=a+(padRight(stadd,48));
        a=a+(padRight(sttp,48));
        a=a+(padRight("(Authorized Distributor for Darley Butler & Co.Ltd)",48).substring(0, 48));



        a=a+(padRight("Inv.No : "+MyGloble.Display_InvoiceNo ,48));

        SimpleDateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
        String date = df.format(Calendar.getInstance().getTime());
        a=a+(padRight("Date : "+date ,48));


        //		a=a+(padRight("Stockist: "+stname,24).substring(0, 24)+padRight("Address: "+stadd,24).substring(0, 24) );

        a=a+(padRight("Retailer: "+MyGloble.RName,48).substring(0, 48) );
        a=a+(padRight("Sales Rep: "+MyGloble.Repname,48).substring(0, 48));
        a=a+(padRight("Address : "+MyGloble.Radd ,48).substring(0, 48));

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
        a=a+(padRight("Bill Type  : "+billtype ,48).substring(0, 48));
        a=a+(padRight(".................................................." ,48).substring(0, 48));


    }

    public void S_ItemHeader() throws IOException {
        a=a+(padRight("Product",24)+padLeft("Qty",4)+padLeft("Dis.",9) +padLeft("Total",11));
        //a=a+(padRight("-------",50)+padLeft("---",10)+padLeft("----",15) +padLeft("-----",15)+"\n");
        //a=a+("\n");
    }
    public void S_DisHeader() throws IOException {
        a=a+(padRight("................................................." ,48).substring(0, 48));
        //a=a+("\n");
        a=a+(padLeft("<<__Discount Quantity__>>          ",48));
        a=a+("\n");
    }

    public void S_RedumHeader() throws IOException {
        a=a+(padRight("................................................." ,48).substring(0, 48));
        //a=a+("\n");
        a=a+(padLeft("<<__Redemption__>>             ",48));
        a=a+("\n");
    }

    public void S_RetHeader() throws IOException {

        a=a+(padRight("................................................." ,48).substring(0, 48));
        a=a+(padLeft("<<------Market Returns------>>         ",48));
        a=a+("\n");
    }
    public void S_RetDisHeader() throws IOException {
        a=a+(padRight("................................................." ,48).substring(0, 48));

        a=a+(padLeft("<<__Return Discount Quantity__>>       ",48));
        a=a+("\n");
    }
    public void S_DicountRow(String item, String Qty) throws IOException
    {
        a=a+((padRight(item,26)+padRight(Qty,22)).substring(0, 48));
    }
    public void S_InvEnd(String tot, String ret, String net) throws IOException {
        a=a+(padRight("==================================================" ,48).substring(0, 48));
        a=a+(padRight("Total value : ",24)+padLeft(tot,24) );
        a=a+(padRight("Market Return Value : ",24)+padLeft(ret,24) );
        a=a+(padRight("Net Value : ",24)+padLeft(net,24) );
        a=a+("Received goods in good condition.:______________ ");
        a=a+(padRight("==================================================" ,48).substring(0, 48));
        a=a+("\n");
        a=a+("\n");
        a=a+("\n");
        a=a+("\n");
        a=a+("\n") ;
    }
    public void S_InvoiceRow(String item, String Qty,String Dis,String tot) throws IOException {
        String row="";
        String ITEM="";
        String QTY="";
        String DIS="";
        String TOTAL="";


        QTY = padLeft(Qty,4).substring(0, 4) ;
        DIS = padLeft(Dis, 9).substring(0, 9) ;
        TOTAL = padLeft(tot,11).substring(0, 11);
        ITEM = padRight(item , 24).substring(0, 24) ;


        row=ITEM+QTY+DIS+TOTAL;

        a=a+(row);

    }
    public void S_InvEnd_Vat(String tot, String ret, String net,String vat) throws IOException {

        a=a+(padRight("==================================================" ,48).substring(0, 48));
        a=a+(padRight("Total value : ",24)+padLeft(tot,24) );
        a=a+(padRight("Vat value : ",24)+padLeft(vat,24) );
        a=a+(padRight("Market Return Value : ",24)+padLeft(ret,24) );
        a=a+(padRight("Net Value : ",24)+padLeft(net,24) );
        a=a+("Received good is good condition.:______________ ");
        a=a+(padRight("==================================================" ,48).substring(0, 48));
        a=a+("\n");
        a=a+("\n");
        a=a+("\n") ;
        a=a+("\n");
        a=a+("\n") ;
    }

}
