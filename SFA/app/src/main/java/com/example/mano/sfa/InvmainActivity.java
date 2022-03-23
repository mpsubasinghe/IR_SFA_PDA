package com.example.mano.sfa;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class InvmainActivity extends AppCompatActivity {

    DatabaseHandler db;

    Spinner spinner;
    Spinner spinnerf;

    EditText textPrice;
    EditText textQty;
  //  EditText text3;
    EditText texdis;
    EditText gtot;
    EditText net;
    EditText text7;
    EditText textFreeQty;
    //GridView grid;
    CheckBox ckbox;

    String pid ;
    String pname ;

    String itemSP ;
    String fcate ;
    String fdis ;
    String fpid ;
    String mix ;

    String[] ItemF ;
    String[] ItemFQ ;
    String[] ItemP ;
    String[] ItemPName ;

    TableLayout stk;

    Button btnRedum;
    Button btnDisFree;
    Button btnBck;
    Button btnAdd;
    Button btnsp;


    ScrollView outerScrollView;
    ScrollView innerScrollView;

    Integer PQTY ;
    Integer IQTY=0 ;
   // Integer Total_IQTY=0 ;
    Integer fqty ;
    Integer Pre_fqty=0 ;
    Double DisPer=0.0 ;

    Boolean CheckRedem=false;
    Boolean CheckRedemCondition=false;

    Double Gross;
    Double Net;
    Double DisValue;
    Double Discount_Write;


    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invmain);


        db = new DatabaseHandler(getApplicationContext());

        setTitle("Add Products");

        spinner = (Spinner)findViewById(R.id.spinner1);
        spinnerf = (Spinner)findViewById(R.id.spinner3);
        ckbox=(CheckBox) findViewById(R.id.checkBox);

        textPrice=(EditText)findViewById(R.id.editText1);
        textQty = (EditText)findViewById(R.id.editText2);
     //   text3 = (EditText)findViewById(R.id.editText3);
        texdis = (EditText)findViewById(R.id.editText4);
        gtot = (EditText)findViewById(R.id.editText5);
        net = (EditText)findViewById(R.id.editText6);
        text7 = (EditText)findViewById(R.id.editText7);
        textFreeQty  = (EditText)findViewById(R.id.editText8);

        btnRedum =(Button)findViewById(R.id .button1);
        btnDisFree =(Button)findViewById(R.id .button2);
        //	grid = (GridView) findViewById(R.id.gridView1);

        stk = (TableLayout) findViewById(R.id.table_main);

        btnBck= (Button)findViewById(R.id.button3);
        btnAdd= (Button)findViewById(R.id.button4);


        innerScrollView= (ScrollView) findViewById(R.id.scrollView1);
       // outerScrollView= (ScrollView) findViewById(R.id.scrollView3);

        text7.setText( String.format("%.2f",MyGloble.Total));


        fqty=0 ;
        Gross=0.0;
        Net=0.0;

      //  textPrice.setKeyListener(null);
//        texdis.setKeyListener(null);
        gtot.setKeyListener(null);
        net.setKeyListener(null);
        text7.setKeyListener(null);

        // Spinner click listener
        //	spinner.setOnItemSelectedListener(this);
        //grid.setOnItemSelectedListener(this);
        loadSpinnerData();
        loadFreeSpinnerDataAllProduct();;
        datat();

        ckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Log.i(TAG, String.format("checkbox onClick, isSelected: %s, identityHashCode: %s", checkBox.isSelected(), System.identityHashCode(checkBox)));

                if (ckbox.isChecked()) {
                    Cursor cursor=db.GetC("select * from PDAProduct where ProductID='"+pid.toString()+"' ");
                    // Toast.makeText(getApplicationContext(), cursor.getString(2).toString(),Toast.LENGTH_LONG).show();
                    while(cursor.moveToNext())
                    {
                        if(!cursor.getString(cursor.getColumnIndex("DDisPcnt")).equals("0"))
                        {
                            textPrice.setText(cursor.getString(cursor.getColumnIndex("DDisPcnt")));
                            IQTY=Integer.valueOf(textQty.getText().toString());
                            CheckFree(IQTY);
                        }else
                        {
                            DialogBoxOneButton("This product hasn't Distributor Discount Price..?","Distributor Discount");
                            ckbox.setChecked(false);
                        }

                    }
            } else {
                    Cursor cursor=db.GetC("select * from PDAProduct where ProductID='"+pid.toString()+"' ");
                    // Toast.makeText(getApplicationContext(), cursor.getString(2).toString(),Toast.LENGTH_LONG).show();
                    while(cursor.moveToNext())
                    {
                        textPrice.setText(cursor.getString(cursor.getColumnIndex("Price")));
                        IQTY=Integer.valueOf(textQty.getText().toString());
                        CheckFree(IQTY);
                    }
            }


        }
        });







        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?>arg0, View view, int arg2, long arg3) {

                String selected_val=spinner.getSelectedItem().toString();
//                spinnerf.setAdapter(null);


                pid=ItemP[arg2];
                pname=ItemPName[arg2];

                PQTY=0;
                fdis="";
                fcate="";

                DatabaseHandler db = new DatabaseHandler(getApplicationContext());

                Cursor cursor=db.GetC("select * from PDAProduct where ProductID='"+pid.toString()+"' ");
                // Toast.makeText(getApplicationContext(), cursor.getString(2).toString(),Toast.LENGTH_LONG).show();
                while(cursor.moveToNext())
                {
                    textPrice.setText(cursor.getString(cursor.getColumnIndex("Price")));
//	        			   textQty.setText(cursor.getString(cursor.getColumnIndex("Quantity")));
                    PQTY=Integer.valueOf(cursor.getString(cursor.getColumnIndex("Quantity")));
                    fcate=cursor.getString(cursor.getColumnIndex("FreeIssue"));
                    fdis=cursor.getString(cursor.getColumnIndex("Discount"));
                    mix=cursor.getString(cursor.getColumnIndex("Mix"));

                }

//	        		   for (int i=0;i<MyGloble.DataTable1.length;i++)
//	        		   {
//	        				  if (!MyGloble.DataTable1[i][0].equals(""))
//	        				  {
//	        					  if (MyGloble.DataTable1[i][8].equals(pid) )
//	        					  {
//	        						  PQTY=PQTY-Integer.valueOf(MyGloble.DataTable1[i][2]);
//	        					  }
//	        					  if (MyGloble.DataTable1[i][6].equals(fpid) )
//	        					  {
//	        						  PQTY=PQTY-Integer.valueOf(MyGloble.DataTable1[i][5]);
//	        					  }
//	        				  }
//	        			}

                textQty.setText(String.valueOf(PQTY));


             //
                //   text3.setText("");
                gtot.setText("");
                net.setText("");
                textQty.requestFocus();
                textQty.selectAll();


                CheckRedem=false;

                DisValue=0.0;
                fqty=0;

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        spinnerf.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?>arg0, View view, int arg2, long arg3) {

                //   String selected_val=spinner.getSelectedItem().toString();
                fpid=ItemF[arg2].trim();

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });



        btnRedum.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

//                if(textQty.getText().toString().equals(""))
//                {
//                    //  Toast.makeText(getApplicationContext(), "Please Enter QTY Value...?", Toast.LENGTH_LONG).show();
//                    DialogBoxOneButton("Please Enter Redemtion QTY Value...?","Value");
//                }else
//                {
//                    CheckRedem=true;
//                    Redemtion() ;
//                }

            }
        });

        textQty.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                Gross=0.0;
//                if(!s.equals("") )
//                { //do your work here }
//
//
//                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s) {

            }
        });

        btnDisFree.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(textQty.getText().toString().equals(""))
                {
                    //  Toast.makeText(getApplicationContext(), "Please Enter QTY Value...?", Toast.LENGTH_LONG).show();
                    DialogBoxOneButton("Please Enter QTY Value...?","Value");
                }else
                {
                  //  Total_IQTY=0;
                    IQTY=Integer.valueOf(textQty.getText().toString());
                    CheckFree(IQTY);

                }

            }
        });

        btnBck.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Intent intent = new Intent(InvmainActivity .this, InvtypeActivity .class);
                InvmainActivity.this.startActivity(intent);


                //	GridView gv = (GridView) findViewById(R.id.gridView1);
//					grid.setBackgroundColor(Color.WHITE);
//					grid.setVerticalSpacing(2);
//					grid.setHorizontalSpacing(2);
//
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (textQty.getText().toString().equals("") )
                {
                    //	Toast.makeText(getApplicationContext(),"Please Enter the Qty..??", Toast.LENGTH_LONG).show();
                    DialogBoxOneButton("Please Enter the QTY Value...?","Value");
                    return;
                }
                if ( Integer.parseInt(textQty.getText().toString())<= 0 )
                {
                    //	Toast.makeText(getApplicationContext(),"Please Enter the Qty..??", Toast.LENGTH_LONG).show();
                    DialogBoxOneButton("Not Allow For Zero Or Minus QTY Value...?","Value");
                    return;
                }


                if (Gross <= 0.0 && !CheckRedem)
                {
                    //Toast.makeText(getApplicationContext(), "Please Check Free Issue and Discount OR Redumption Before Add Item...!!!!", Toast.LENGTH_LONG).show();
                    DialogBoxOneButton("Please Check Free Issue and Discount OR Redumption Before Add Item...!!!!","Check Value");
                    return;

                }
                if(!textFreeQty .getText().toString().equals("") && Integer .parseInt(textFreeQty .getText().toString())>0)
                {

                    fqty=Integer .parseInt(textFreeQty .getText().toString());
                }else
                {
                    fpid="";
                    fqty=0;
                }

                LoadTable(pname+" - " +String.format("%.2f",Double.parseDouble(textPrice.getText().toString())),String.format("%.2f",Double.parseDouble(textPrice.getText().toString())), textQty.getText().toString());


                ckbox.setChecked(false);

                fqty=0 ;
                Gross=0.0;
                Net=0.0;

             //   text3.setText("");
                gtot.setText("");
                net.setText("");
                texdis.setText("");
                textQty.setText("");

//                spinnerf.setAdapter(null);

            }
        });



    }

    // Disabled Back Button On 4n
    @Override
    public void onBackPressed() {
    }
    public void CheckFree(Integer qty) {
        // Pname/price/qty/freeqty/dis
        //Toast.makeText(getApplicationContext(),fcate,Toast.LENGTH_LONG).show();

//        spinnerf.setAdapter(null);
//        fpid="";

        fqty=0;
        Pre_fqty=0;
        Gross = 0.0;
        Net =0.0;
        DisValue=0.0;
        CheckRedem =false;
        Discount_Write=0.0;

//        Integer	Same_p_qty=0;
//        for (int i=0;i<MyGloble.DataTable1.length;i++)
//        {
//            if (!MyGloble.DataTable1[i][0].equals(""))
//            {
//                if (MyGloble.DataTable1[i][8].equals(pid))
//                {
//                    Same_p_qty=Same_p_qty+ Integer.valueOf(MyGloble.DataTable1[i][2]);
//                    // Toast.makeText(getApplicationContext(),String.valueOf(Catefqty) ,Toast.LENGTH_LONG).show();
//                }
//
//            }
//        }
//        if(Same_p_qty==0)
//        {
//            Total_IQTY=Integer.valueOf(textQty.getText().toString());
//        }else
//        {
//            Total_IQTY=Same_p_qty + Integer.valueOf(textQty.getText().toString());
//        }


//        Total_IQTY=0;
//        Total_IQTY=  Integer.valueOf(textQty.getText().toString());
//        Cursor cursor1=db.GetC("select Quantity from PDAInvoicedProduct WHERE  ProductID='"+pid+"' AND  InvoicedID='"+ MyGloble.InvoiceNo+"'   ");
//        if( cursor1.getCount() > 0) {
//            if (cursor1.moveToFirst())
//            {
//                while (!cursor1.isAfterLast()) {
//
//                    Total_IQTY=Total_IQTY+ Integer.valueOf(cursor1.getString(cursor1.getColumnIndex("Quantity")));
//                    cursor1.moveToNext();
//                }
//            }
//        }



//        if(fcate.equals("No")){
//
//        }else if(fcate.equals("FI")){
//            FreeIssue(qty);
//        }else{
//            FreeIssueSpecial(qty,fcate);
//        }
//
//        if(fdis.equals("No")){
//
//        }else if(fdis.equals("Rs")){
//            //Toast.makeText(getApplicationContext(),fcate,Toast.LENGTH_LONG).show();
//            Discount(qty);
//        }else{
//            DiscountSpecial(qty,fdis);
//        }

        loadvalue() ;

    }
    public void loadvalue() {
        if(!texdis.getText().toString().equals("") )
        {
            if( Double.parseDouble(texdis .getText().toString())>0)
            {
                DisValue = Double.parseDouble(texdis.getText().toString())* Double.parseDouble(textQty .getText().toString());
            }

        }


        Gross = Double.parseDouble(textPrice.getText().toString()) * Double.parseDouble(IQTY.toString());
        Net = Double.parseDouble(textPrice.getText().toString())  * Double.parseDouble(IQTY.toString()) - DisValue;

        texdis.setText(String.format("%.2f",DisValue));
      //  text3.setText(String.valueOf(fqty));
        gtot.setText(String.format("%.2f",Gross));
        net.setText(String.format("%.2f",Net));

    }
    public void Redemtion()
    {
        spinnerf.setAdapter(null);
        fpid="";
        fqty=0;
        CheckRedemCondition=false;

        Integer	Same_p_qty=0;
//        for (int i=0;i<MyGloble.DataTableREDEM.length;i++)
//        {
//            if (!MyGloble.DataTableREDEM[i][0].equals(""))
//            {
//                if (MyGloble.DataTableREDEM[i][8].equals(pid))
//                {
//                    Same_p_qty=Same_p_qty+ Integer.valueOf(MyGloble.DataTableREDEM[i][2]);
//                    // Toast.makeText(getApplicationContext(),String.valueOf(Catefqty) ,Toast.LENGTH_LONG).show();
//                }
//
//            }
//        }
//        if(Same_p_qty==0)
//        {
//            Total_IQTY=Integer.valueOf(textQty.getText().toString());
//        }else
//        {
//            Total_IQTY=Same_p_qty + Integer.valueOf(textQty.getText().toString());
//        }

        Cursor cursor5=db.GetC("select * from PDAInvRedemption WHERE RedProID = '"+pid+"' AND   InvID='"+ MyGloble.InvoiceNo+"'   ");
        if( cursor5.getCount() > 0) {
            if (cursor5.moveToFirst()) {
                while (!cursor5.isAfterLast()) {
                    Same_p_qty=Same_p_qty+ Integer.valueOf(cursor5.getString(cursor5.getColumnIndex("RedProQty")));
                    cursor5.moveToNext();
                }
            }
        }


        if(textQty.getText().toString().equals(""))
        {
            //  Toast.makeText(getApplicationContext(), "Please Enter QTY Value...?", Toast.LENGTH_LONG).show();
            DialogBoxOneButton("Please Enter QTY Value...?","Value");
            return;
        }


        if(MyGloble.Total==0)
        {
            //Toast.makeText(getApplicationContext(),"Please Add Item To Invoice Before Adding Redemption...!!!",Toast.LENGTH_LONG).show();
            DialogBoxOneButton("Please Add Item To Invoice Before Adding Redemption...!!!","Value");
            return;
        }

        IQTY= Same_p_qty + Integer.valueOf(textQty.getText().toString());
        ItemFQ = new String[10];
        ItemF = new String[10];

        Integer a=0;
        Integer Catefqty=0;
        List<String> labels = new ArrayList<String>();
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        Cursor cursor1=db.GetC("select * from PDARedemption WHERE ProductID='"+pid+"' ");
        //	Toast.makeText(getApplicationContext(),String.valueOf( cursor.getCount()),Toast.LENGTH_LONG).show();
        if( cursor1.getCount() > 0)
        {
            if (cursor1.moveToFirst()){
                while(!cursor1.isAfterLast()){

                    if( IQTY >= Integer.parseInt(cursor1.getString(cursor1.getColumnIndex("ConQty"))))
                    {

                        //Toast.makeText(getApplicationContext(),"truuuuuuuu", Toast.LENGTH_LONG).show();
                        CheckRedemCondition=true;
                        Cursor c=db.GetC("select * from PDAProduct where   ProductID='"+cursor1.getString(2)+"' ");
                        if( c.getCount() > 0)
                        {	//Toast.makeText(getApplicationContext(),String.valueOf( c.getCount()),Toast.LENGTH_LONG).show();
                            if (c.moveToFirst()){
                                Integer q=0;
                                if(cursor1.getString(4).equals("Yes"))
                                {
                                    q=(IQTY/cursor1.getInt(1))*cursor1.getInt(3);
                                    labels.add(c.getString(6) + " - " +c.getString(2) + " - " + String.valueOf(q));
                                    //	ItemF[a]=padRight(c.getString(0),5).substring(0, 5) +padLeft(String.valueOf(q),5).substring(0, 5) ;
                                    ItemF[a]=c.getString(0) ;
                                    ItemFQ[a]=String.valueOf(q);

                                }
                                else
                                {
                                    q=cursor1.getInt(3);
                                    labels.add(c.getString(6) + " - " + c.getString(2) + " - " + String.valueOf(q));
                                    //ItemF[a]=padRight(c.getString(0),5).substring(0, 5) +padLeft(String.valueOf(q),5).substring(0, 5) ;
                                    ItemF[a]=c.getString(0) ;
                                    ItemFQ[a]=String.valueOf(q);
                                }

                                a=a+1;
                                c.moveToNext();
                            }
                        }

                        c.close();

                        loadSpinnerFree(labels);
                        break;
                    }

                    cursor1.moveToNext();
                }
            }

        }
        else
        {
            Toast.makeText(getApplicationContext(),"Sorry...!!! No Redemption For This Item.",Toast.LENGTH_LONG).show();

        }

    }

    public void FreeIssue(Integer qty) {
        Integer f=0;
        ItemFQ = new String[10];
        ItemF = new String[10];
        Integer a=0;
        Integer Catefqty=0;

        //	Total_IQTY=qty;
        Catefqty=qty;

        //	Toast.makeText(getApplicationContext(),"stsrt= Pre_fqty-"+ Pre_fqty +"-"+"",Toast.LENGTH_LONG).show();
        List<String> labels = new ArrayList<String>();
        //Toast.makeText(getApplicationContext(),"mix="+mix,Toast.LENGTH_LONG).show();
        if(!mix.equals("NO"))
        {
            Cursor cursor2=db.GetC("select Quantity from PDAInvoicedProduct WHERE  ProductID='"+mix+"' AND  InvoicedID='"+ MyGloble.InvoiceNo+"'   ");
            if( cursor2.getCount() > 0) {
                if (cursor2.moveToFirst())
                {
                    while (!cursor2.isAfterLast()) {

                        Catefqty=Catefqty+ Integer.valueOf(cursor2.getString(cursor2.getColumnIndex("Quantity")));
                        cursor2.moveToNext();
                    }
                }
            }
            Cursor cursor1=db.GetC("select Quantity from PDAInvoicedProduct WHERE  ProductID='"+pid+"' AND  InvoicedID='"+ MyGloble.InvoiceNo+"'   ");
            if( cursor1.getCount() > 0) {
                if (cursor1.moveToFirst())
                {
                    while (!cursor1.isAfterLast()) {

                        Catefqty=Catefqty+ Integer.valueOf(cursor1.getString(cursor1.getColumnIndex("Quantity")));
                        cursor1.moveToNext();
                    }
                }
            }

//            for (int i=0;i<MyGloble.DataTable1.length;i++)
//            {
//                if (!MyGloble.DataTable1[i][0].equals(""))
//                {
//                    if (MyGloble.DataTable1[i][8].equals(mix))
//                    {
//                        Catefqty=Catefqty+ Integer.valueOf(MyGloble.DataTable1[i][2]);
//                        Pre_fqty=Integer.valueOf(MyGloble.DataTable1[i][5]);
//                    }
//                    if(MyGloble.DataTable1[i][8].equals(pid))
//                    {
//                        //Toast.makeText(getApplicationContext(),"fcate-----"+ cate +"-"+MyGloble.DataTable1[i][2],Toast.LENGTH_LONG).show();
//                        //  Total_IQTY=qty+ Integer.valueOf(MyGloble.DataTable1[i][2]);
//                        Catefqty=Catefqty+ (Integer.valueOf(MyGloble.DataTable1[i][2]));
//                        Pre_fqty=Integer.valueOf(MyGloble.DataTable1[i][5]);
//                    }
//                }
//
//            }
        }
        else
        {
//            //this is for mix==NO product....
//            String [] mixproduct =new String[2];
//            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
//            Cursor cursor44=db.GetC("select * from PDAFreeIssue where   ProductID='"+pid+"' ORDER BY ConQty DESC ");
//            //	Toast.makeText(getApplicationContext(),String.valueOf( cursor.getCount()),Toast.LENGTH_LONG).show();
//            if( cursor44.getCount() > 0)
//            {
//                if (cursor44.moveToFirst())
//                {
//                    while(!cursor44.isAfterLast())
//                    {
//
//                        for (int q=0;q<2;q++)
//                        {
//                            mixproduct[q]= cursor44.getString(q*2+3);
//                        }
//
//                        cursor44.moveToNext();
//                        break;
//                    }
//                }
//            }
//
//            for (int i=0;i<MyGloble.DataTable1.length;i++)
//            {
//                if (!MyGloble.DataTable1[i][0].equals(""))
//                {
//					  if (MyGloble.DataTable1[i][8].equals(mixproduct[0]) && !MyGloble.DataTable1[i][8].equals(pid))
//						  {
//							  Catefqty=Catefqty+ Integer.valueOf(MyGloble.DataTable1[i][2]);
//							//  Pre_fqty=Integer.valueOf(MyGloble.DataTable1[i][5]);
//							 Toast.makeText(getApplicationContext(),"free 1 = "+String.valueOf( Catefqty),Toast.LENGTH_LONG).show();
//						  }
//					  if (MyGloble.DataTable1[i][8].equals(mixproduct[1]) && !MyGloble.DataTable1[i][8].equals(pid))
//					  {
//						  Catefqty=Catefqty+ Integer.valueOf(MyGloble.DataTable1[i][2]);
//						//  Pre_fqty=Integer.valueOf(MyGloble.DataTable1[i][5]);
//						 Toast.makeText(getApplicationContext(),"free 2 = "+String.valueOf( Catefqty),Toast.LENGTH_LONG).show();
//					  }
//
//                    if(MyGloble.DataTable1[i][8].equals(pid))
//                    {
//                        //Toast.makeText(getApplicationContext(),"fcate-----"+ cate +"-"+MyGloble.DataTable1[i][2],Toast.LENGTH_LONG).show();
//                        Total_IQTY=qty+ Integer.valueOf(MyGloble.DataTable1[i][2]);
//                        Catefqty=Catefqty+ (Integer.valueOf(MyGloble.DataTable1[i][2]));
//                        Pre_fqty=Integer.valueOf(MyGloble.DataTable1[i][5]);
//                        //  Toast.makeText(getApplicationContext(),"same product = Pre_fqty-"+ Pre_fqty +"-"+MyGloble.DataTable1[i][1],Toast.LENGTH_LONG).show();
//
//                        //  Toast.makeText(getApplicationContext(),"same product = "+String.valueOf( Catefqty),Toast.LENGTH_LONG).show();
//                    }
//
//                }
//
//            }


            Cursor cursor1=db.GetC("select Quantity from PDAInvoicedProduct WHERE  ProductID='"+pid+"' AND  InvoicedID='"+ MyGloble.InvoiceNo+"'   ");
            if( cursor1.getCount() > 0) {
                if (cursor1.moveToFirst())
                {
                    while (!cursor1.isAfterLast()) {

                        Catefqty=Catefqty+ Integer.valueOf(cursor1.getString(cursor1.getColumnIndex("Quantity")));
                        cursor1.moveToNext();
                    }
                }
            }

        }

        // Toast.makeText(getApplicationContext(),"Without same product = Pre_fqty-"+ Pre_fqty +"-"+"",Toast.LENGTH_LONG).show();


        //Toast.makeText(getApplicationContext(),pid,Toast.LENGTH_LONG).show();
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        Cursor cursor=db.GetC("select * from PDAFreeIssue where   ProductID='"+pid+"' ORDER BY ConQty DESC ");
        //	Toast.makeText(getApplicationContext(),String.valueOf( cursor.getCount()),Toast.LENGTH_LONG).show();
        if( cursor.getCount() > 0)
        {
            if (cursor.moveToFirst()){
                while(!cursor.isAfterLast()){
                    //String data = cursor.getString(cursor.getColumnIndex("data"));
                    if( Catefqty >= Integer.parseInt(cursor.getString(cursor.getColumnIndex("ConQty"))))
                    {
                        for (int i=0;i<2;i++)
                        {          //Toast.makeText(getApplicationContext(),cursor.getString(i+3),Toast.LENGTH_LONG).show();
                            Cursor c=db.GetC("select * from PDAProduct where   ProductID='"+cursor.getString(i*2+3)+"'  ");
                            if( c.getCount() > 0)
                            {	//Toast.makeText(getApplicationContext(),String.valueOf( c.getCount()),Toast.LENGTH_LONG).show();
                                if (c.moveToFirst()){
                                    //Toast.makeText(getApplicationContext(),c.getString(6),Toast.LENGTH_LONG).show();

                                    ItemF[a]=c.getString(0) ;
                                    if (cursor.getString(cursor.getColumnIndex("Repeat")).equals("NO"))
                                    {
                                        ItemFQ[a]=cursor.getString(i*2+4);
                                        labels.add(c.getString(6) + " - " +c.getString(2) + " - " +cursor.getString(i*2+4)+ " - ( "+ c.getString(3) +" )");
                                    }else
                                    {
                                        ItemFQ[a]=String.valueOf(Catefqty/Integer.parseInt(cursor.getString(cursor.getColumnIndex("ConQty")))*Integer.parseInt(cursor.getString(i*2+4)));
                                        labels.add(c.getString(6) + " - " +c.getString(2) + " - " + ItemFQ[a]);
                                    }

                                    a=a+1;
                                    c.moveToNext();
                                }
                            }

                            c.close();
                        }

                        loadSpinnerFree(labels);
                        break;
                    }
                    //	fqty=  Integer.parseInt(a3)/Integer.parseInt(cursor.getString(cursor.getColumnIndex("Qty"))) * Integer.parseInt(cursor.getString(cursor.getColumnIndex("FQty")));
                    // do what ever you want here
                    cursor.moveToNext();
                }
            }
        }
    }
    public void FreeIssueSpecial(Integer qty,String cate) {
        Integer f=0;
        ItemFQ = new String[40];
        ItemF = new String[40];
        Integer a=0;
        Integer Catefqty=0;

        //set Current Qty Display---> LoadTable(...)
        //Total_IQTY=qty;
        Catefqty=qty;

        List<String> labels = new ArrayList<String>();

        Cursor cursor1=db.GetC("select PDAInvoicedProduct.Quantity from PDAInvoicedProduct,PDAProduct WHERE PDAProduct.ProductID=PDAInvoicedProduct.ProductID AND FreeIssue='"+cate+"' AND  InvoicedID='"+ MyGloble.InvoiceNo+"'   ");
        if( cursor1.getCount() > 0) {
            if (cursor1.moveToFirst())
            {
                while (!cursor1.isAfterLast()) {

                    Catefqty=Catefqty+ Integer.valueOf(cursor1.getString(cursor1.getColumnIndex("Quantity")));
                    cursor1.moveToNext();
                }
            }
        }


//        for (int i=0;i<MyGloble.DataTable1.length;i++)
//        {
//            if (!MyGloble.DataTable1[i][0].equals(""))
//            {
//                if (MyGloble.DataTable1[i][9].equals(cate))
//                {
//                    //Toast.makeText(getApplicationContext(),"fcate="+ MyGloble.DataTable1[i][9] +"="+ cate,Toast.LENGTH_LONG).show();
//                    //  MyGloble.DataTable1[i][5]=null;
//                    // Toast.makeText(getApplicationContext(),"pid="+ MyGloble.DataTable1[i][8] +"="+ pid,Toast.LENGTH_LONG).show();
//
//                    // Plus++ Qty From Sam Categeroy
//                    Catefqty=Catefqty+ Integer.valueOf(MyGloble.DataTable1[i][2]);
//                    //  Pre_fqty=Pre_fqty+Integer.valueOf(MyGloble.DataTable1[i][5]);
//                    // Toast.makeText(getApplicationContext(),"Pre_fqty-----"+ Pre_fqty +"-"+MyGloble.DataTable1[i][9],Toast.LENGTH_LONG).show();
//                    if(MyGloble.DataTable1[i][8].equals(pid))
//                    {
//                        // Toast.makeText(getApplicationContext(),"fcate-----"+ cate +"-"+MyGloble.DataTable1[i][2],Toast.LENGTH_LONG).show();
//                        //  Catefqty=Catefqty+ Integer.valueOf(MyGloble.DataTable1[i][2]);
//
//                        //Get Qty (Current enter qty + already entered qty) For display ---> LoadTable(.... ,... ,..)
//                        //  Total_IQTY=qty+ Integer.valueOf(MyGloble.DataTable1[i][2]);
//                    }
//
//
//
//
//                }
//
//            }
//
//        }
        // Toast.makeText(getApplicationContext(),"total fqty-" + String.valueOf(Total_IQTY),Toast.LENGTH_LONG).show();

        //Get sum of (all entered Catagery qty + enter Current qty)
        // Catefqty=Catefqty +IQTY;


        //	Toast.makeText(getApplicationContext(),"total fqty-" + String.valueOf(Catefqty),Toast.LENGTH_LONG).show();
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        Cursor cursor=db.GetC("select * from PDAFreeIssueSpecial where CatID='"+cate+"' ORDER BY ConQty DESC ");
        //	Toast.makeText(getApplicationContext(),String.valueOf( cursor.getCount()),Toast.LENGTH_LONG).show();
        if( cursor.getCount() > 0)
        {
            if (cursor.moveToFirst()){
                while(!cursor.isAfterLast()){
                    //  String data = cursor.getString(cursor.getColumnIndex("data"));
                    if( Catefqty >= Integer.parseInt(cursor.getString(cursor.getColumnIndex("ConQty"))))
                    {
                        Cursor CFreeSame=db.GetC("select * from PDAFreeIssueSpecialCatogery where FreeSame='0' AND ProductID='"+pid+"' ");
                        if( CFreeSame.getCount() > 0)
                        {
                            for (int i=0;i<37;i++)
                            {       // Toast.makeText(getApplicationContext(),cursor.getString(i+2),Toast.LENGTH_LONG).show();

                                if (!cursor.isNull(i*2+2) && !cursor.isNull(i*2+3))
                                {
                                    if (!cursor.getString(i*2+2).equals("") && !cursor.getString(i*2+3).equals(""))
                                    {
                                        //  Cursor c=db.GetC("select * from PDAProduct,PDAFreeIssueSpecialCatogery where PDAFreeIssueSpecialCatogery.ProductID= PDAProduct.ProductID AND FreeSame='0' AND   PDAProduct.ProductID='"+cursor.getString(i*2+2)+"' ");
                                        Cursor c=db.GetC("select * from PDAProduct where   ProductID='"+cursor.getString(i*2+2)+"' ");
                                        if( c.getCount() > 0)
                                        {	//Toast.makeText(getApplicationContext(),String.valueOf( c.getCount()),Toast.LENGTH_LONG).show();
                                            if (c.moveToFirst())
                                            {
                                                //Toast.makeText(getApplicationContext(),c.getString(6),Toast.LENGTH_LONG).show();
                                                Cursor CFreeSameADD=db.GetC("select * from PDAFreeIssueSpecialCatogery where FreeSame='0' AND ProductID='"+c.getString(0)+"' ");
                                                if( CFreeSameADD.getCount() > 0)
                                                {
                                                    labels.add(c.getString(6) + " - " +c.getString(2) + " - " +cursor.getString(i*2+3) + " - ( "+ c.getString(3) +" )");

                                                    ItemF[a]=c.getString(0) ;
                                                    ItemFQ[a]=cursor.getString(i*2+3) ;
                                                    a=a+1;
                                                    c.moveToNext();
                                                }


                                            }
                                        }

                                        c.close();

                                    }
                                }


                            }
                        }else
                        {
                            Integer FreeSameItemc=0;
                            Integer CateItemc=0;
                            for (int i=0;i<MyGloble.DataTable1.length;i++)
                            {
                                if (!MyGloble.DataTable1[i][0].equals(""))
                                {
                                    if (MyGloble.DataTable1[i][9].equals(cate))
                                    {
                                        CateItemc=CateItemc+1;
                                        Cursor CFreeSameADD=db.GetC("select * from PDAFreeIssueSpecialCatogery where FreeSame<>'0' AND ProductID='"+MyGloble.DataTable1[i][8]+"' ");
                                        if( CFreeSameADD.getCount() > 0)
                                        {
                                            FreeSameItemc=FreeSameItemc+1;
                                        }
                                    }
                                }
                            }


                            if(CateItemc==FreeSameItemc) //== only Freesame items invoiced
                            {
                                if(FreeSameItemc==0 ) //===  when only one freesame item
                                {
                                    Integer frsmqty=0;
                                    for (int i=0;i<37;i++)
                                    {       // Toast.makeText(getApplicationContext(),cursor.getString(i+2),Toast.LENGTH_LONG).show();
                                        if (!cursor.isNull(i*2+3))
                                        {
                                            if (!cursor.getString(i*2+3).equals("") )
                                            {
                                                frsmqty=Integer.valueOf( cursor.getString(i*2+3));
                                                break;
                                            }
                                        }
                                    }
                                    labels.add(pname + " - " + textPrice.getText().toString()  + " - " + frsmqty.toString());
                                    ItemF[a]=pid ;
                                    ItemFQ[a]=frsmqty.toString() ;

                                }else
                                {
                                    Cursor CFreesame=db.GetC("select Price,ProductName,PDAFreeIssueSpecialCatogery.ProductID from PDAFreeIssueSpecialCatogery,PDAProduct where PDAFreeIssueSpecialCatogery.ProductID=PDAProduct.ProductID AND  FreeSame<>'0' AND CatID='"+ cate +"'   ORDER BY Price   ");
                                    if( CFreesame.getCount() > 0) {
                                        if (CFreesame.moveToFirst())
                                        {
                                            for (int i=0;i<37;i++) {       // Toast.makeText(getApplicationContext(),cursor.getString(i+2),Toast.LENGTH_LONG).show();

                                                if (!cursor.isNull(i * 2 + 2)) {
                                                    if (cursor.getString(i * 2 + 2).equals(CFreesame.getString(2))) {

                                                        labels.add(CFreesame.getString(1) + " - " + CFreesame.getString(0) + " - " +cursor.getString(i * 2 + 3));
                                                        ItemF[a]=CFreesame.getString(2) ;
                                                        ItemFQ[a]=cursor.getString(i * 2 + 3) ;

                                                    }
                                                }
                                            }

                                        }
                                    }
                                }

//                                labels.add(pname + " - " + textPrice.getText().toString()  + " - " +cursor.getString(3));
//                                ItemF[a]=pid ;
//                                ItemFQ[a]=cursor.getString(3) ;
                            }
                            else
                            {

                                for (int i=0;i<37;i++)
                                {       // Toast.makeText(getApplicationContext(),cursor.getString(i+2),Toast.LENGTH_LONG).show();

                                    if (!cursor.isNull(i*2+2))
                                    {
                                        if (!cursor.getString(i*2+2).equals("") )
                                        {
                                            //  Cursor c=db.GetC("select * from PDAProduct,PDAFreeIssueSpecialCatogery where PDAFreeIssueSpecialCatogery.ProductID= PDAProduct.ProductID AND FreeSame='0' AND   PDAProduct.ProductID='"+cursor.getString(i*2+2)+"' ");
                                            Cursor c=db.GetC("select * from PDAProduct where   ProductID='"+cursor.getString(i*2+2)+"' ");
                                            if( c.getCount() > 0)
                                            {	//Toast.makeText(getApplicationContext(),String.valueOf( c.getCount()),Toast.LENGTH_LONG).show();
                                                if (c.moveToFirst())
                                                {
                                                    //Toast.makeText(getApplicationContext(),c.getString(6),Toast.LENGTH_LONG).show();
                                                    Cursor CFreeSameADD=db.GetC("select * from PDAFreeIssueSpecialCatogery where FreeSame='0' AND ProductID='"+c.getString(0)+"' ");
                                                    if( CFreeSameADD.getCount() > 0)
                                                    {
                                                        labels.add(c.getString(6) + " - " +c.getString(2) + " - " +cursor.getString(i*2+3) + " - ( "+ c.getString(3) +" )");

                                                        ItemF[a]=c.getString(0) ;
                                                        ItemFQ[a]=cursor.getString(i*2+3) ;
                                                        a=a+1;
                                                        c.moveToNext();
                                                    }


                                                }
                                            }

                                            c.close();

                                        }
                                    }


                                }


                            }




                        }



                        loadSpinnerFree(labels);
                        break;
                    }
                    //fqty=  Integer.parseInt(a3)/Integer.parseInt(cursor.getString(cursor.getColumnIndex("Qty"))) * Integer.parseInt(cursor.getString(cursor.getColumnIndex("FQty")));
                    // do what ever you want here
                    cursor.moveToNext();
                }
            }
        }











    }
    public void Discount(Integer qty) {

        Integer Catefqty=0;
        Catefqty=qty;

        Cursor cursor1=db.GetC("select Quantity from PDAInvoicedProduct WHERE  ProductID='"+pid+"' AND  InvoicedID='"+ MyGloble.InvoiceNo+"'   ");
        if( cursor1.getCount() > 0) {
            if (cursor1.moveToFirst())
            {
                while (!cursor1.isAfterLast()) {

                    Catefqty=Catefqty+ Integer.valueOf(cursor1.getString(cursor1.getColumnIndex("Quantity")));
                    cursor1.moveToNext();
                }
            }
        }


//        for (int i=0;i<MyGloble.DataTable1.length;i++)
//        {
//            if (!MyGloble.DataTable1[i][0].equals(""))
//            {
//                if (MyGloble.DataTable1[i][8].equals(pid))
//                {
//                    Catefqty=Catefqty+ Integer.valueOf(MyGloble.DataTable1[i][2]);
//                    // Toast.makeText(getApplicationContext(),String.valueOf(Catefqty) ,Toast.LENGTH_LONG).show();
//                }
//
//            }
//        }

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        Cursor cursor=db.GetC("select * from PDADiscount where ProductID='"+pid+"' ORDER BY ConQty DESC ");
        //Toast.makeText(getApplicationContext(),String.valueOf( cursor.getCount()),Toast.LENGTH_LONG).show();
        if( cursor.getCount() > 0)
        {
            if (cursor.moveToFirst()){
                while(!cursor.isAfterLast()){
                    //  String data = cursor.getString(cursor.getColumnIndex("data"));
                    if( (Catefqty) >= Integer.parseInt(cursor.getString(cursor.getColumnIndex("ConQty"))))
                    {
                        DisValue= Double.valueOf(cursor.getString(2));
                        Discount_Write= Double.valueOf(cursor.getString(2));
                        break;
                    }

                    cursor.moveToNext();
                }
            }
        }

    }
    public void DiscountSpecial(Integer qty,String Dcate) {
        Integer Catefqty=0;

        Catefqty=qty;
        //Total_IQTY=qty;

        Integer sameProInvQty ;
        sameProInvQty=0;

        Cursor cursor1=db.GetC("select PDAInvoicedProduct.Quantity,PDAInvoicedProduct.ProductID from PDAInvoicedProduct,PDAProduct WHERE PDAProduct.ProductID=PDAInvoicedProduct.ProductID AND PDAProduct.Discount='"+Dcate+"' AND  InvoicedID='"+ MyGloble.InvoiceNo+"'   ");
        if( cursor1.getCount() > 0) {
            if (cursor1.moveToFirst())
            {
                while (!cursor1.isAfterLast()) {

                    Catefqty=Catefqty+ Integer.valueOf(cursor1.getString(cursor1.getColumnIndex("Quantity")));
                    if (cursor1.getString(cursor1.getColumnIndex("ProductID")).equals(pid)){
                        sameProInvQty=Integer.valueOf(cursor1.getString(cursor1.getColumnIndex("Quantity")));
                    }
                    cursor1.moveToNext();
                }
            }
        }

//        for (int i=0;i<MyGloble.DataTable1.length;i++)
//        {
//            if (!MyGloble.DataTable1[i][0].equals(""))
//            {
//                if (MyGloble.DataTable1[i][7].equals(Dcate))
//                {
//                    //Toast.makeText(getApplicationContext(),"fcate="+ MyGloble.DataTable1[i][9] +"="+ cate,Toast.LENGTH_LONG).show();
//
//                    //  MyGloble.DataTable1[i][3]=null;
//                    // Toast.makeText(getApplicationContext(),"pid="+ MyGloble.DataTable1[i][8] +"="+ pid,Toast.LENGTH_LONG).show();
//                    Catefqty=Catefqty+ Integer.valueOf(MyGloble.DataTable1[i][2]);
//
//                    if(MyGloble.DataTable1[i][8].equals(pid))
//                    {
//                        //  Total_IQTY=qty+ Integer.valueOf(MyGloble.DataTable1[i][2]);
//                        // Toast.makeText(getApplicationContext(),"fcate-----"+ cate +"-"+MyGloble.DataTable1[i][2],Toast.LENGTH_LONG).show();
//                        // Catefqty=Catefqty+ Integer.valueOf(MyGloble.DataTable1[i][2]);
//                    }
//                }
//
//            }
//
//        }
        // Catefqty=Catefqty + IQTY;

        //  Toast.makeText(getApplicationContext(),String.valueOf(Catefqty),Toast.LENGTH_LONG).show();

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        Cursor cursor=db.GetC("select * from PDADiscountSpecial where CatID='"+Dcate+"' ORDER BY ConQty DESC ");
        //Toast.makeText(getApplicationContext(),String.valueOf( cursor.getCount()),Toast.LENGTH_LONG).show();

        if( cursor.getCount() > 0)
        {
            if (cursor.moveToFirst()){
                while(!cursor.isAfterLast()){
                    //  String data = cursor.getString(cursor.getColumnIndex("data"));
                    DisPer=0.0;
                    if( Catefqty >= Integer.parseInt(cursor.getString(cursor.getColumnIndex("ConQty"))))
                    {
                        // Toast.makeText(getApplicationContext(),cursor.getString(2),Toast.LENGTH_LONG).show();
                        if(cursor.getString(3).equals("Rs")){
                            DisValue= Double.valueOf(cursor.getString(2));
                            DisPer=0.0;
                            Discount_Write=Double.valueOf(cursor.getString(2));
                        }else if(cursor.getString(3).equals("%")){
                            //  Toast.makeText(getApplicationContext(),cursor.getString(2),Toast.LENGTH_LONG).show();
                            DisPer=Double.valueOf(cursor.getString(2));
                            DisValue=((Double.valueOf(textPrice.getText().toString())/100)*Double.valueOf(cursor.getString(2)))* Double.valueOf(sameProInvQty+qty);
                            Discount_Write= Double.valueOf(cursor.getString(2));
                        }
                        break;
                    }

                    cursor.moveToNext();
                }
            }
        }

    }
    public static String padLeft(String s, int n) {
        return String.format("%1$" + n + "s", s);
    }
    public static String padRight(String s, int n) {
        return String.format("%1$-" + n + "s", s);
    }
    private void loadSpinnerFree(List<String> labels) {

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, labels);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerf.setAdapter(dataAdapter);
    }
    public Integer GetStock(String StockProduct){
        Integer invQty=0;
        for (int i=0;i<MyGloble.DataTable1.length;i++)
        {
            if (!MyGloble.DataTable1[i][0].equals(""))
            {
                if (MyGloble.DataTable1[i][8] != null )
                {
                    if (MyGloble.DataTable1[i][8].equals(StockProduct))
                    {
                        invQty=invQty+Integer.valueOf(MyGloble.DataTable1[i][2]) ;
                    }
                }
                if(MyGloble.DataTable1[i][6] != null)
                {
                    if(MyGloble.DataTable1[i][6].equals(StockProduct) )
                    {
                        invQty=invQty + Integer.valueOf(MyGloble.DataTable1[i][5]);
                    }
                }


            }
        }

        for (int i=0;i<MyGloble.DataTableREDEM.length;i++)
        {
            if (!MyGloble.DataTableREDEM[i][0].equals(""))
            {
                if (MyGloble.DataTableREDEM[i][6] != null && MyGloble.DataTableREDEM[i][8] != null)
                {
//		     		 if (MyGloble.DataTableREDEM[i][8].equals(pid))
//			     	 {
//		     			invQty=invQty+Integer .valueOf(MyGloble.DataTableREDEM[i][2]) ;
//			     	 }
                    if (MyGloble.DataTableREDEM[i][6].equals(pid))
                    {
                        invQty=invQty+Integer .valueOf(MyGloble.DataTableREDEM[i][2]) ;
                    }
                }
            }
        }

        return invQty;

    }
    public void LoadTable(String a1,String a2,String a3) {
        // pname/price/qty/dis/subtot/fqty/Fpid/disM/pid/fcate

        //Toast.makeText(getApplicationContext(),a1+"--"+a2+"--"+String.valueOf(fqty),Toast.LENGTH_LONG).show();
        Integer EnterTextBoxQty=0;
        if(CheckRedem == true)
        {
            EnterTextBoxQty=0;
        }
        else
        {
            EnterTextBoxQty=Integer.valueOf(textQty.getText().toString());
        }

        Pre_fqty=0;
//        for (int i=0;i<MyGloble.DataTable1.length;i++)
//        {
//            if (!MyGloble.DataTable1[i][0].equals(""))
//            {
//                if (MyGloble.DataTable1[i][9].equals(fcate))
//                {
//                    if (MyGloble.DataTable1[i][6].equals(fpid))
//                    {
//                        Pre_fqty=Pre_fqty+Integer.valueOf(MyGloble.DataTable1[i][5]);
//                    }
//                }
//            }
//        }
//
//        if(fpid.equals(""))
//        {
//            if (PQTY < EnterTextBoxQty+GetStock(pid) )
//            {
//                //Toast.makeText(getApplicationContext(),"No Enough Stock Quantity for Sale Qty..??", Toast.LENGTH_LONG).show();
//                DialogBoxOneButton("No Enough Stock Quantity for Sale Qty..??(Not Free Qty)","Stock");
//                return;
//            }
//        }
//        else if (pid.equals(fpid))
//        {
//            Integer qty=0;
//            qty= EnterTextBoxQty+ fqty-Pre_fqty +GetStock(pid);
//            //	DialogBoxOneButton(EnterTextBoxQty.toString()+"====  fqty="+fqty.toString()+" Pre_fqty-"+Pre_fqty.toString()+"-"+  String.valueOf((GetStock(fpid)))  ,"2");
//
//            if (PQTY < qty )
//            {
//                //Toast.makeText(getApplicationContext(),"No Enough Stock Quantity for Sale & Free-Issue Qty..??", Toast.LENGTH_LONG).show();
//                DialogBoxOneButton(" No Enough Stock Quantity for Sale & Free-Issue Qty..?? (Same Product For Inv & Free)","Stock");
//                return;
//            }
//        }
//        else
//        {
//
//            Integer checkfqty=0;
//            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
//            Cursor cursor=db.GetC("select * from PDAProduct where ProductID='"+fpid+"' ");
//            while(cursor.moveToNext())
//            {
//                checkfqty=Integer.valueOf(cursor.getString(cursor.getColumnIndex("Quantity")));
//            }
//
//            //	DialogBoxOneButton(checkfqty.toString()+"====  fqty="+fqty.toString()+" Pre_fqty-"+Pre_fqty.toString()+"-"+  String.valueOf((GetStock(fpid)))  ,"1");
//
//            if (checkfqty < fqty-Pre_fqty+GetStock(fpid))
//            {
//                //Toast.makeText(getApplicationContext(),"No Enough Stock Quantity for Free-Issue..??", Toast.LENGTH_LONG).show();
//                DialogBoxOneButton(" No Enough Stock Quantity for Free-Issue..?? (Not Same Product For Inv & Free)","Stock");
//                return;
//            }
//
//            if (PQTY < EnterTextBoxQty +GetStock(pid))
//            {
//                //Toast.makeText(getApplicationContext(),"No Enough Stock Quantity for Free-Issue..??", Toast.LENGTH_LONG).show();
//                DialogBoxOneButton("No Enough Stock Quantity for Sale..??(Not Same Product For Inv & Free)","Stock");
//                return;
//            }
//
//
//
//        }


        Integer checkfqty=0;

        Cursor cursor=db.GetC("select * from PDAProduct where ProductID='"+fpid+"' ");
        while(cursor.moveToNext())
        {
            checkfqty=Integer.valueOf(cursor.getString(cursor.getColumnIndex("Quantity")));
        }
        if (checkfqty < fqty)
        {
            //Toast.makeText(getApplicationContext(),"No Enough Stock Quantity for Free-Issue..??", Toast.LENGTH_LONG).show();
            DialogBoxOneButton(" No Enough Stock Quantity for Free-Issue..?? (Not Same Product For Inv & Free)","Stock");
            return;
        }
        if (pid.equals(fpid))
        {
            Integer Catefqty=0;
            Cursor cursor1=db.GetC("select PDAInvoicedProduct.FreeIssueQty from PDAInvoicedProduct WHERE  PDAInvoicedProduct.ProductID='"+fpid+"'  AND FreeIssueID='"+fpid+"' AND  InvoicedID='"+ MyGloble.InvoiceNo+"'   ");
            if( cursor1.getCount() > 0) {
                if (cursor1.moveToFirst())
                {
                    while (!cursor1.isAfterLast()) {

                        Catefqty=Catefqty+ Integer.valueOf(cursor1.getString(cursor1.getColumnIndex("FreeIssueQty")));
                        cursor1.moveToNext();
                    }
                }
            }



            if (checkfqty < (fqty-Catefqty)+EnterTextBoxQty)
            {
                DialogBoxOneButton(" No Enough Stock Quantity for Sale & Free-Issue Qty..?? (Same Product For Inv & Free)","Stock");
                return;
            }

        }


        Integer checkinvqty=0;
        Cursor cursor12=db.GetC("select * from PDAProduct where ProductID='"+pid+"' ");
        while(cursor12.moveToNext()) {
            checkinvqty=Integer.valueOf(cursor12.getString(cursor12.getColumnIndex("Quantity")));
        }
        if (checkinvqty < EnterTextBoxQty)
        {
            //Toast.makeText(getApplicationContext(),"No Enough Stock Quantity for Free-Issue..??", Toast.LENGTH_LONG).show();
            DialogBoxOneButton("No Enough Stock Quantity for Sale..??(Not Same Product For Inv & Free)","Stock");
            return;
        }



        //======================================================================================================================

        if(CheckRedem == true)
        {
//			if (PQTY < fqty )
//			{
//				//Toast.makeText(getApplicationContext(),"No Enough Stock Quantity For Redumption..?", Toast.LENGTH_LONG).show();
//				DialogBoxOneButton("No Enough Stock Quantity For Redumption..?","Stock");
//				return;
//			}



            //	Boolean CheckInVQty=false;
//			  for (int i=0;i<MyGloble.DataTable1.length;i++)
//			   {
//				  if (!MyGloble.DataTable1[i][0].equals(""))
//				  {
//

//					  if (MyGloble.DataTable1[i][8].equals(pid))
//					  {
//
//            for (int q=0;q<MyGloble.DataTableREDEM.length;q++)
//            {
//                if (MyGloble.DataTableREDEM[q][8].equals(pid))
//                {
//                    MyGloble.DataTableREDEM[q][0]="";
//                    MyGloble.DataTableREDEM[q][1]="";
//                    MyGloble.DataTableREDEM[q][2]="";
//                    MyGloble.DataTableREDEM[q][3]="";
//                    MyGloble.DataTableREDEM[q][4]="";
//                    MyGloble.DataTableREDEM[q][5]="";
//                    MyGloble.DataTableREDEM[q][6]="";
//                    MyGloble.DataTableREDEM[q][7]="";
//                    MyGloble.DataTableREDEM[q][8]="";
//                    MyGloble.DataTableREDEM[q][9]="";
//                }
//            }

            if(CheckRedemCondition)
            {
                //  Toast.makeText(getApplicationContext(),fpid, Toast.LENGTH_LONG).show();
//                MyGloble.DtcountREDEM=MyGloble.DtcountREDEM + 1;
//                MyGloble.DataTableREDEM[MyGloble.DtcountREDEM][0] = a1; //pname
//                MyGloble.DataTableREDEM[MyGloble.DtcountREDEM][1] = a2;//price
//                MyGloble.DataTableREDEM[MyGloble.DtcountREDEM][2] = String.valueOf(fqty);//qty
//                MyGloble.DataTableREDEM[MyGloble.DtcountREDEM][3] = String.format("%.2f",0.00);//dis
//                MyGloble.DataTableREDEM[MyGloble.DtcountREDEM][4] = String.format("%.2f",0.00);//subtot
//                MyGloble.DataTableREDEM[MyGloble.DtcountREDEM][5] = a3; //fqty
//                MyGloble.DataTableREDEM[MyGloble.DtcountREDEM][6] = fpid; //Fpid
//                MyGloble.DataTableREDEM[MyGloble.DtcountREDEM][7] = ""; //disM
//                MyGloble.DataTableREDEM[MyGloble.DtcountREDEM][8] = pid; //pid
//                MyGloble.DataTableREDEM[MyGloble.DtcountREDEM][9] = ""; //fcate
                Cursor cursor5=db.GetC("select * from PDAInvRedemption WHERE RedProID = '"+pid+"' AND   InvID='"+ MyGloble.InvoiceNo+"'   ");
                if( cursor5.getCount() > 0) {
                    if (cursor5.moveToFirst()){
                        MyGloble.db.execSQL("Update  PDAInvRedemption SET RedProQty=RedProQty+'"+a3+"', ProID='" + fpid + "', Qty='" + fqty + "'  WHERE RedProID = '" + pid + "' AND   InvID='" + MyGloble.InvoiceNo + "'");
                        MyGloble.db.execSQL("Update PDAProduct SET Quantity=Quantity - '" + (fqty-(Double.valueOf( cursor5.getString(cursor5.getColumnIndex("Qty")))) + "' WHERE ProductID = '"+fpid+"' " ));
                        cursor5.moveToNext();
                    }
                }else
                {
                    MyGloble.db.execSQL("insert into PDAInvRedemption values ('"+MyGloble.InvoiceNo+"' ,'"+ fpid +"','"+fqty+"','"+pid+"','"+a3+"')" );
                    MyGloble.db.execSQL("Update PDAProduct SET Quantity=Quantity - '" + fqty + "' WHERE ProductID = '"+fpid+"'" );
                }
            }else
            {
                DialogBoxOneButton("No Enough Qty For Redemption Free-Qty..???","Add Qty");
            }

            CheckRedemCondition=false;
            CheckRedem =false;
            return;
        }


        // Toast.makeText(getApplicationContext(),fpid+"  ==  " +fqty,Toast.LENGTH_LONG).show();

        //=================================================================================================
        // clear Discount values


        Cursor cursor1=db.GetC("select PDAInvoicedProduct.Quantity,PDAProduct.Price,PDAInvoicedProduct.ProductID from PDAInvoicedProduct,PDAProduct WHERE PDAProduct.ProductID=PDAInvoicedProduct.ProductID AND PDAProduct.Discount NOT IN('No','Rs') AND PDAProduct.Discount='"+fdis+"' AND   InvoicedID='"+ MyGloble.InvoiceNo+"'   ");
        if( cursor1.getCount() > 0) {
            if (cursor1.moveToFirst())
            {
                while (!cursor1.isAfterLast()) {
                    if(DisPer>0)
                    {
                        Double temdis;
                        Double temsub;
                         temdis=0.0;
                         temsub=0.0;
//                        Toast.makeText(getApplicationContext(),"Price="+cursor1.getString(cursor1.getColumnIndex("Price")) +"  Quantity="+cursor1.getString(cursor1.getColumnIndex("Quantity")),Toast.LENGTH_LONG).show();
//                        Toast.makeText(getApplicationContext(),"Price="+cursor1.getString(cursor1.getColumnIndex("Price")) +"  Quantity="+cursor1.getString(cursor1.getColumnIndex("Quantity")),Toast.LENGTH_LONG).show();
                        temdis=(((Double.valueOf( cursor1.getString(cursor1.getColumnIndex("Price")))/100)*DisPer) * Double.valueOf(cursor1.getString(cursor1.getColumnIndex("Quantity"))));
                        temsub=((Double .valueOf(cursor1.getString(cursor1.getColumnIndex("Price")))*Double.valueOf(cursor1.getString(cursor1.getColumnIndex("Quantity"))))-temdis);
//                        Toast.makeText(getApplicationContext(),"Price="+cursor1.getString(cursor1.getColumnIndex("Price")) +"  Quantity="+cursor1.getString(cursor1.getColumnIndex("Quantity")),Toast.LENGTH_LONG).show();
//                        Toast.makeText(getApplicationContext(),"Price="+cursor1.getString(cursor1.getColumnIndex("Price")) +"  Quantity="+cursor1.getString(cursor1.getColumnIndex("Quantity")),Toast.LENGTH_LONG).show();
//                        Toast.makeText(getApplicationContext(),"Price="+cursor1.getString(cursor1.getColumnIndex("Price")) +"  Quantity="+cursor1.getString(cursor1.getColumnIndex("Quantity")),Toast.LENGTH_LONG).show();
// MyGloble.DataTable1[i][3]=String.format("%.2f",temdis);
//                        MyGloble.DataTable1[i][4] = String.format("%.2f",temsub);//subtot
//                        MyGloble.DataTable1[i][10] = String .valueOf(Discount_Write) ;
                        MyGloble.db.execSQL("Update PDAInvoicedProduct SET Discount='"+Discount_Write+"' , DisAmount='" + String.format("%.2f",temdis) + "',SubTot='" + String.format("%.2f",temsub) + "' WHERE ProductID = '"+cursor1.getString(cursor1.getColumnIndex("ProductID"))+"' AND  InvoicedID='"+ MyGloble.InvoiceNo+"'  " );

                    }
                    else
                    {
                        // Toast.makeText(getApplicationContext(),"dis="+ MyGloble.DataTable1[i][3] +"  sub="+ MyGloble.DataTable1[i][4],Toast.LENGTH_LONG).show();
//					 				  //

//                        MyGloble.DataTable1[i][3]="0.00";
//                        MyGloble.DataTable1[i][4] = String.format("%.2f",(Double .valueOf(cursor1.getColumnIndex("PDAInvoicedProduct.Quantity"))*Double .valueOf(cursor1.getString(cursor1.getColumnIndex("Price")))));//subtot
//                        MyGloble.DataTable1[i][10] ="0.00";
                    MyGloble.db.execSQL("Update PDAInvoicedProduct SET Discount='0.00' , DisAmount='0.00',SubTot='" + String.format("%.2f",(Double .valueOf(cursor1.getString(cursor1.getColumnIndex("Quantity")))*Double .valueOf(cursor1.getString(cursor1.getColumnIndex("Price"))))) + "' WHERE ProductID = '"+cursor1.getString(cursor1.getColumnIndex("ProductID"))+"' AND  InvoicedID='"+ MyGloble.InvoiceNo+"'  " );
                        //	 Toast.makeText(getApplicationContext(),"dis="+ MyGloble.DataTable1[i][3] +"  sub="+ MyGloble.DataTable1[i][4],Toast.LENGTH_LONG).show();
                    }


                    cursor1.moveToNext();
                }
            }
        }





//        for (int i=0;i<MyGloble.DataTable1.length;i++)
//        {
//            if (!MyGloble.DataTable1[i][0].equals(""))
//            {
//                if (!MyGloble.DataTable1[i][7].equals("No") )
//                {
//                    if (MyGloble.DataTable1[i][7].equals(fdis) && !fdis.equals("Rs")) // Get item withut normal discount
//                    {
//                        //  Toast.makeText(getApplicationContext(),"fcate="+ MyGloble.DataTable1[i][7] +"="+ DisPer.toString(),Toast.LENGTH_LONG).show();
//                        if(DisPer>0)
//                        {
//                            Double temdis=0.0;
//                            Double temsub=0.0;
//
//                            temdis=(((Double.valueOf( MyGloble.DataTable1[i][1] )/100)*DisPer) * Double.valueOf( MyGloble.DataTable1[i][2] ));
//                            temsub=((Double .valueOf(MyGloble.DataTable1[i][1])*Double .valueOf(MyGloble.DataTable1[i][2]))-((Double.valueOf( MyGloble.DataTable1[i][1] )/100)*DisPer) * Double.valueOf( MyGloble.DataTable1[i][2] ))	;
//                            MyGloble.DataTable1[i][3]=String.format("%.2f",temdis);
//                            MyGloble.DataTable1[i][4] = String.format("%.2f",temsub);//subtot
//                            MyGloble.DataTable1[i][10] = String .valueOf(Discount_Write) ;
//                        }
//                        else
//                        {
//                            // Toast.makeText(getApplicationContext(),"dis="+ MyGloble.DataTable1[i][3] +"  sub="+ MyGloble.DataTable1[i][4],Toast.LENGTH_LONG).show();
////					 				  //
//
//                            MyGloble.DataTable1[i][3]="0.00";
//                            MyGloble.DataTable1[i][4] = String.format("%.2f",(Double .valueOf(MyGloble.DataTable1[i][2])*Double .valueOf(MyGloble.DataTable1[i][1])));//subtot
//                            MyGloble.DataTable1[i][10] ="0.00";
//
//                            //	 Toast.makeText(getApplicationContext(),"dis="+ MyGloble.DataTable1[i][3] +"  sub="+ MyGloble.DataTable1[i][4],Toast.LENGTH_LONG).show();
//                        }
//
//
//
//                    }
//
//
//                }
//            }
//
//        }


        DisPer=0.0;
        //===================================================================================================
        //clear free qty
        if (!fcate.equals("No") && !fcate.equals("FI"))
        {
            Cursor cursor2=db.GetC("select * from PDAInvoicedProduct,PDAProduct WHERE PDAProduct.ProductID=PDAInvoicedProduct.ProductID AND FreeIssue NOT IN('No','FI') AND FreeIssue='"+fcate+"' AND   InvoicedID='"+ MyGloble.InvoiceNo+"'   ");
            if( cursor2.getCount() > 0) {
                if (cursor2.moveToFirst()) {
                    while (!cursor2.isAfterLast()) {
                        MyGloble.db.execSQL("Update PDAInvoicedProduct SET FreeIssueID='' , FreeIssueQty='0' WHERE ProductID = '"+cursor2.getString(cursor2.getColumnIndex("PDAInvoicedProduct.ProductID"))+"' AND  InvoicedID='"+ MyGloble.InvoiceNo+"'  " );
                        if(!cursor2.getString(cursor2.getColumnIndex("FreeIssueID")).equals(""))
                        {
                            MyGloble.db.execSQL("Update PDAProduct SET Quantity=Quantity + '" + cursor2.getString(cursor2.getColumnIndex("FreeIssueQty")) + "' WHERE ProductID = '"+cursor2.getString(cursor2.getColumnIndex("FreeIssueID"))+"' " );
                        }
                        cursor2.moveToNext();
                    }
                }
            }

        } else if (mix.equals("NO") && fcate.equals("FI"))
        {

            Cursor cursor3=db.GetC("select * from PDAInvoicedProduct,PDAProduct WHERE PDAProduct.ProductID=PDAInvoicedProduct.ProductID AND FreeIssue IN('FI') AND PDAInvoicedProduct.ProductID='"+pid+"' AND   InvoicedID='"+ MyGloble.InvoiceNo+"'   ");
            if( cursor3.getCount() > 0) {
                if (cursor3.moveToFirst()) {
                    while (!cursor3.isAfterLast()) {
                        MyGloble.db.execSQL("Update PDAInvoicedProduct SET FreeIssueID='' , FreeIssueQty='0' WHERE ProductID = '"+cursor3.getString(cursor3.getColumnIndex("PDAInvoicedProduct.ProductID"))+"' AND  InvoicedID='"+ MyGloble.InvoiceNo+"'  " );
                        if(!cursor3.getString(cursor3.getColumnIndex("FreeIssueID")).equals(""))
                        {
                            MyGloble.db.execSQL("Update PDAProduct SET Quantity=Quantity + '" + cursor3.getString(cursor3.getColumnIndex("FreeIssueQty")) + "' WHERE ProductID = '"+cursor3.getString(cursor3.getColumnIndex("FreeIssueID"))+"' " );
                        }
                        cursor3.moveToNext();
                    }
                }
            }

        }else if (!mix.equals("NO") && fcate.equals("FI"))
        {
            Cursor cursor3=db.GetC("select * from PDAInvoicedProduct,PDAProduct WHERE PDAProduct.ProductID=PDAInvoicedProduct.ProductID AND FreeIssue IN('FI') AND PDAInvoicedProduct.ProductID='"+mix+"' AND   InvoicedID='"+ MyGloble.InvoiceNo+"'   ");
            if( cursor3.getCount() > 0) {
                if (cursor3.moveToFirst()) {
                    while (!cursor3.isAfterLast()) {
                        MyGloble.db.execSQL("Update PDAInvoicedProduct SET FreeIssueID='' , FreeIssueQty='0' WHERE ProductID = '"+cursor3.getString(cursor3.getColumnIndex("PDAInvoicedProduct.ProductID"))+"' AND  InvoicedID='"+ MyGloble.InvoiceNo+"'  " );
                        if(!cursor3.getString(cursor3.getColumnIndex("FreeIssueID")).equals(""))
                        {
                            MyGloble.db.execSQL("Update PDAProduct SET Quantity=Quantity + '" + cursor3.getString(cursor3.getColumnIndex("FreeIssueQty")) + "' WHERE ProductID = '"+cursor3.getString(cursor3.getColumnIndex("FreeIssueID"))+"' " );
                        }
                        cursor3.moveToNext();
                    }
                }
            }
        }







//        for (int i=0;i<MyGloble.DataTable1.length;i++)
//        {
//            if (!MyGloble.DataTable1[i][0].equals(""))
//            {
//                if (!MyGloble.DataTable1[i][9].equals("No") )
//                {
//                    if (MyGloble.DataTable1[i][9].equals(fcate) && !fcate.equals("FI"))
//                    {
//                        // Toast.makeText(getApplicationContext(),MyGloble.DataTable1[i][0] +"  fcate="+ MyGloble.DataTable1[i][9] +"="+ fcate,Toast.LENGTH_LONG).show();
//                        MyGloble.DataTable1[i][5]="0";
//                        MyGloble.DataTable1[i][6]="";
//                    }
//                    if (!mix.equals("NO") && fcate.equals("FI"))
//                    {
//                        if(mix.equals(MyGloble.DataTable1[i][8]))
//                        {
//                            MyGloble.DataTable1[i][5]="0";
//                            MyGloble.DataTable1[i][6]="";
//                        }
//
//                    }
//                }
//            }
//        }

        //=================================================================================================
        //Add value to table

//        for (int i=0;i<MyGloble.DataTable1.length;i++) {
//            if (!MyGloble.DataTable1[i][0].equals(""))
//            {
//                if (MyGloble.DataTable1[i][8].equals(pid) && MyGloble.DataTable1[i][1].equals(a2) )
//                {
//                    MyGloble.DataTable1[i][0]="";
//                    MyGloble.DataTable1[i][1]="";
//                    MyGloble.DataTable1[i][2]="";
//                    MyGloble.DataTable1[i][3]="";
//                    MyGloble.DataTable1[i][4]="";
//                    MyGloble.DataTable1[i][5]="0";
//                    MyGloble.DataTable1[i][6]="";
//                    MyGloble.DataTable1[i][7]="";
//                    MyGloble.DataTable1[i][8]="";
//                    MyGloble.DataTable1[i][9]="";
//                    MyGloble.DataTable1[i][10]="";
//                }
//            }
//        }

       Double subtot=0.0;



        //  Toast.makeText(getApplicationContext(),fqty, Toast.LENGTH_LONG).show();

//        MyGloble.Dtcount=MyGloble.Dtcount+1;
//
//        MyGloble.DataTable1[MyGloble.Dtcount][0] = a1; //pname
//        MyGloble.DataTable1[MyGloble.Dtcount][1] = a2;//price
//        MyGloble.DataTable1[MyGloble.Dtcount][2] = a3;//qty
//        MyGloble.DataTable1[MyGloble.Dtcount][3] = String.format("%.2f",DisValue);//dis
//        MyGloble.DataTable1[MyGloble.Dtcount][4] = String.format("%.2f",subtot);//subtot
//        MyGloble.DataTable1[MyGloble.Dtcount][5] = String.valueOf(fqty); //fqty
//        MyGloble.DataTable1[MyGloble.Dtcount][6] = fpid; //Fpid
//        MyGloble.DataTable1[MyGloble.Dtcount][7] = fdis; //disM
//        MyGloble.DataTable1[MyGloble.Dtcount][8] = pid; //pid
//        MyGloble.DataTable1[MyGloble.Dtcount][9] = fcate; //fcate
//        MyGloble.DataTable1[MyGloble.Dtcount][10] = String.valueOf(Discount_Write); //Write Discount into the table

        //	Toast.makeText(getApplicationContext(),MyGloble.DataTable1[MyGloble.Dtcount][6], Toast.LENGTH_LONG).show();

        Cursor cursor5=db.GetC("select * from PDAInvoicedProduct WHERE ProductID = '"+pid+"' AND   InvoicedID='"+ MyGloble.InvoiceNo+"'   ");
        if( cursor5.getCount() > 0) {
            if (cursor5.moveToFirst()){
                subtot=(Double.valueOf(a2) *(Double.valueOf(a3)+Double.valueOf(cursor5.getString(cursor5.getColumnIndex("Quantity")))))- DisValue;
                MyGloble.db.execSQL("Update  PDAInvoicedProduct SET Quantity=Quantity+'"+a3+"', Discount=Discount+'"+DisValue+"' ,  FreeIssueID='"+fpid+"' , FreeIssueQty='"+fqty+"' , DisAmount=DisAmount+'"+String.format("%.2f",DisValue)+"' , SubTot='"+String.format("%.2f",subtot)+"'  WHERE ProductID = '"+pid+"' AND   InvoicedID='" + MyGloble.InvoiceNo + "'");
                MyGloble.db.execSQL("Update PDAProduct SET Quantity=Quantity - '" + a3 + "' WHERE ProductID = '"+pid+"'" );
                MyGloble.db.execSQL("Update PDAProduct SET Quantity=Quantity - '" + fqty + "' WHERE ProductID = '"+fpid+"' " );
                cursor5.moveToNext();
            }
        }else
        {
            String DBdis="";
            Cursor cursor33=db.GetC("select * from PDAProduct where ProductID='"+pid+"' ");
            while(cursor33.moveToNext()) {
                if(!cursor33.getString(cursor33.getColumnIndex("DDisPcnt")).equals("0"))
                {
                    DBdis="YES";
                }
            }

            subtot=(Double.valueOf(a2) * Double.valueOf(a3))- DisValue;
            MyGloble.db.execSQL("insert into PDAInvoicedProduct values ('"+MyGloble.InvoiceNo+"' ,'"+ pid +"','"+a3+"','"+String.format("%.2f",DisValue)+"','"+fpid+"','"+fqty+"','"+fdis+"','"+String.format("%.2f",DisValue)+"','"+String.format("%.2f",subtot)+"','" + DBdis + "','NO')" );
            MyGloble.db.execSQL("Update PDAProduct SET Quantity=Quantity - '" + a3 + "' WHERE ProductID = '"+pid+"'" );
            MyGloble.db.execSQL("Update PDAProduct SET Quantity=Quantity - '" + fqty + "' WHERE ProductID = '"+fpid+"' " );
        }








        //=============================================================================================================
        // Get Total for  invoice
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



//        for (int i=0;i<MyGloble.DataTable1.length;i++) {
//            if (!MyGloble.DataTable1[i][0].equals(""))
//            {
//                MyGloble.Total=MyGloble.Total+ (Double.valueOf(MyGloble.DataTable1[i][4]) ) ;
//            }
//        }
        //  textQty.setText(String.valueOf(PQTY-Integer.valueOf(textQty.getText().toString())));
        text7.setText( String.format("%.2f",MyGloble.Total));


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

        stk.removeAllViews();
        datat();
        loadSpinnerData();

        loadFreeSpinnerDataAllProduct();
    }
    private String[] append(String[] letters, String string) {
        // TODO Auto-generated method stub
        return null;
    }
    private void loadSpinnerData() {
        // database handler
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        Cursor C = db.GetC("select * from PDAProduct WHERE Quantity>0 Order by ProductName");
        List<String> labels = new ArrayList<String>();
        //Toast.makeText(getApplicationContext(),String.valueOf( C.getCount()), Toast.LENGTH_LONG).show();

        ItemP = new String[C.getCount()];
        ItemPName = new String[C.getCount()];

        Integer a=0;
        while(C.moveToNext())
        {


            ItemP[a]=C.getString(0);
            ItemPName[a]=C.getString(6);

            labels.add(C.getString(6) + " - " + String.format("%.2f",C.getDouble(2))+ " - " +C.getString(3));
            a=a+1;

            //Toast.makeText(getApplicationContext(), C.getString(C.getColumnIndex("Ename")), Toast.LENGTH_LONG).show();
        }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, labels);

        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }
    private void datat() {

        TableRow tbrow0 = new TableRow(this);
        TextView tv0 = new TextView(this);
        tv0.setText("Product                ");
        tv0.setTextColor(Color.BLACK);
        tv0.setGravity(Gravity.LEFT);
        tv0.setTypeface(null, Typeface.BOLD);
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(this);
        tv1.setText("          Qty");
        tv1.setTextColor(Color.BLACK);
        tv1.setGravity(Gravity.RIGHT);
        tv1.setTypeface(null, Typeface.BOLD);
        tbrow0.addView(tv1);
        TextView tv2 = new TextView(this);
        tv2.setText("     F_Qty");
        tv2.setTextColor(Color.BLACK);
        tv2.setGravity(Gravity.RIGHT);
        tv2.setTypeface(null, Typeface.BOLD);
        tbrow0.addView(tv2);
        TextView tv3 = new TextView(this);
        tv3.setText("        Dis.");
        tv3.setTextColor(Color.BLACK);
        tv3.setGravity(Gravity.RIGHT);
        tv3.setTypeface(null, Typeface.BOLD);
        tbrow0.addView(tv3);
        stk.addView(tbrow0);


        Cursor cursor2=db.GetC("select ProductName,PDAInvoicedProduct.Quantity,FreeIssueQty,DisAmount,Price,DDisPcnt from PDAInvoicedProduct,PDAProduct WHERE PDAProduct.ProductID=PDAInvoicedProduct.ProductID AND  InvoicedID='"+ MyGloble.InvoiceNo+"'   ");
        if( cursor2.getCount() > 0) {
            if (cursor2.moveToFirst())

            {
                while (!cursor2.isAfterLast()) {


                    TableRow tbrow = new TableRow(this);
                    TextView t1v = new TextView(this);
                    t1v.setText(cursor2.getString(cursor2.getColumnIndex("ProductName")) +"-" +cursor2.getString(cursor2.getColumnIndex("Price")));
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

                    stk.addView(tbrow);
                    cursor2.moveToNext();
                }
            }

        }
//        for (int i=0;i<MyGloble.DataTable1.length;i++) {
//            if (!MyGloble.DataTable1[i][0].equals(""))
//            {
//
//            }
//
//        }


    }
    public void DialogBoxOneButton(String msg,String head )
    {
        AlertDialog alertDialog = new AlertDialog.Builder(InvmainActivity.this).create();

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
    private void loadFreeSpinnerDataAllProduct() {
        // database handler
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        Cursor C = db.GetC("select * from PDAProduct WHERE Quantity>0 Order by ProductName");
        List<String> labels = new ArrayList<String>();
        //Toast.makeText(getApplicationContext(),String.valueOf( C.getCount()), Toast.LENGTH_LONG).show();

        ItemF = new String[C.getCount()];


        Integer a=0;
        while(C.moveToNext())
        {


            ItemF[a]=C.getString(0);


            labels.add(C.getString(6) + " - " + String.format("%.2f",C.getDouble(2))+ " - " +C.getString(3));
            a=a+1;

            //Toast.makeText(getApplicationContext(), C.getString(C.getColumnIndex("Ename")), Toast.LENGTH_LONG).show();
        }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, labels);

        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //attaching data adapter to spinner
        spinnerf.setAdapter(dataAdapter);
    }
}

