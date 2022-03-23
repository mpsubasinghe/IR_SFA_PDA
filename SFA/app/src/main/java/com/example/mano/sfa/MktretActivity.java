package com.example.mano.sfa;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MktretActivity extends Activity {
    RadioGroup radioSexGroup;
    RadioButton radioSexButton;
    Button btnAdd;
    Button btnRem;
    Button btnBack;

    Spinner spinner;
    Spinner spinnerf;

    String pid;
    String pname;
    String itemSP;
    String fcate;
    String fdis;
    String fpid;
    String Rmethod;
    String itemdelete;
    String RetInvNo;

    EditText textPrice;
    EditText textQTY;
    EditText textDIS;
    EditText texfqty;

    TextView DisView;

    Integer PQTY;
    Integer fqty;
    Double Gross;
    Double Net;
    Double DisValue;
    Double SPrice;
    Double Discount_Write;

    String[] ItemF;
    String[] ItemP;
    String[] ItemN;
    String[] ItemFQ ;



    TableLayout stk;

    DatabaseHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mktret);
        setTitle("Add Mkt.Return Products");

        radioSexGroup = (RadioGroup) findViewById(R.id.radioGender);
        btnAdd = (Button) findViewById(R.id.button1);
        btnRem = (Button) findViewById(R.id.button2);
        btnBack = (Button) findViewById(R.id.button3);

        spinner = (Spinner) findViewById(R.id.spinner1);
        spinnerf = (Spinner) findViewById(R.id.spinner2);

        textPrice = (EditText) findViewById(R.id.editText1);
        textQTY = (EditText) findViewById(R.id.editText2);
        textDIS = (EditText) findViewById(R.id.editText3);
        texfqty = (EditText) findViewById(R.id.editText4);

        DisView = (TextView) findViewById(R.id.textView8);

        db = new DatabaseHandler(getApplicationContext());

        stk = (TableLayout) findViewById(R.id.table_main);

        Rmethod = "";
        fpid = "";

        textDIS.setEnabled(false);
        texfqty.setEnabled(false);

        loadSpinnerData();

        datat();

        btnAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                RetInvNo="";


                // TODO Auto-generated method stub
                // get selected radio button from radioGroup
                //  int selectedId = radioSexGroup.getCheckedRadioButtonId();

                // find the radiobutton by returned id
//	                radioSexButton = (RadioButton) findViewById(selectedId);
                //    Toast.makeText(getApplicationContext(),pname,Toast.LENGTH_SHORT).show();


//                // Create custom dialog object
//                final Dialog dialog = new Dialog(MktretActivity.this);
//                // Include dialog.xml file
//                dialog.setContentView(R.layout.dialog);
//                // Set dialog title
//                dialog.setTitle("Custom Dialog");
//
//                // set values for custom dialog components - text, image and button
//                TextView text = (TextView) dialog.findViewById(R.id.textDialog);
//                text.setText("Custom dialog Android example.");
//                ImageView image = (ImageView) dialog.findViewById(R.id.imageDialog);
//              //  image.setImageResource(R.drawable.image0);
//
//                dialog.show();
//
//                Button declineButton = (Button) dialog.findViewById(R.id.declineButton);
//                // if decline button is clicked, close the custom dialog
////                declineButton.setOnClickListener(new OnClickListener() {
////                    @Override
////                    public void onClick(View v) {
////                        // Close dialog
////                        dialog.dismiss();
////                    }
////                });




         //       dialogBox();



                // get prompts.xml view


                //Working Dialog box========================================================================================
//                LayoutInflater li = LayoutInflater.from(MktretActivity.this);
//                View promptsView = li.inflate(R.layout.custom, null);
//
//                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
//                        MktretActivity.this);
//
//                // set prompts.xml to alertdialog builder
//                alertDialogBuilder.setView(promptsView);
//
//                final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);
//                final Button   btn1 = (Button) promptsView.findViewById(R.id.button2);
//
//                // set dialog message
//                alertDialogBuilder.setCancelable(false).setPositiveButton("OK",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog,int id) {
//                                        // get user input and set it to result
//                                        // edit text
//                                      //  result.setText(userInput.getText());
//                                        Toast.makeText(getApplicationContext(),userInput.getText(),Toast.LENGTH_SHORT).show();
//                                        RetInvNo=String.valueOf(userInput.getText())  ;
//                                    }
//                                })
//                        .setNegativeButton("Cancel",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog,int id) {
//                                        dialog.cancel();
//
//                                    }
//                                });
//
//                // create alert dialog
//                AlertDialog alertDialog = alertDialogBuilder.create();
//
//                // show it
//                alertDialog.show();

//====================================================================================================================
                if (!(texfqty.getText().toString()).equals(""))
                {
                    if (Integer.valueOf(texfqty.getText().toString())< fqty) {
                        DialogBoxOneButton("Please Enter Correct Free Qty...????", "Market Return");
                        return;
                    }else
                    {
                        fqty = Integer.valueOf(texfqty.getText().toString());
                    }
                }






                if (textQTY.getText().toString().equals("")) {
                    DialogBoxOneButton("Please Enter Invoice Qty...????", "Market Return");
                    return;
                }

                if (fpid.equals("")) {
                    if (!texfqty.getText().toString().equals("")) {
                        // Toast.makeText(getApplicationContext(),"Please Enter the Invoice Qty...???",Toast.LENGTH_SHORT).show();
                        DialogBoxOneButton("You haven't free Product, Please Remove Free Qty...????", "Market Return");
                        return;
                    }

                }

                if (SPrice < Double.valueOf(textPrice.getText().toString())) {
                    DialogBoxOneButton("Sorry...!! You Can't Enter more than Item Price. ", "Market Return");
                    return;
                }

                if (Rmethod.equals("S")) {
                    if ((SPrice / 10) > Double.valueOf(textPrice.getText().toString())) {
                        DialogBoxOneButton("Sorry...!! You Can't edit Item Price, please enter correct Price..?", "Market Return");
                        return;
                    }

                }


                // Toast.makeText(getApplicationContext(),"jjjjjjjjjjjjjjjj",Toast.LENGTH_SHORT).show();

                Integer Same_p_qty = 0;
                Same_p_qty = Integer.valueOf(textQTY.getText().toString());

//                for (int i = 0; i < MyGloble.DataTableMKTR.length; i++) {
//                    if (!MyGloble.DataTableMKTR[i][0].equals("")) {
//                        if (MyGloble.DataTableMKTR[i][8].equals(pid) && MyGloble.DataTableMKTR[i][7].equals(Rmethod)) {
//                            Same_p_qty = Same_p_qty + Integer.valueOf(MyGloble.DataTableMKTR[i][2]);
//                            // Toast.makeText(getApplicationContext(),String.valueOf(Catefqty) ,Toast.LENGTH_LONG).show();
//                        }
//
//                    }
//                }


                if (Rmethod.equals("")) {
                    // Toast.makeText(getApplicationContext(), "Please Select Return Method...????",Toast.LENGTH_SHORT).show();
                    DialogBoxOneButton("Please Select Return Method...????", "Market Return");
                    return;
                }


                if (!textQTY.getText().toString().equals("")) {
//                    Discount_Write = 0.0;
//
//                  CheckDis(Same_p_qty);
                    // Toast.makeText(getApplicationContext(),pname,Toast.LENGTH_SHORT).show();
                    LoadTable(pname + " - " + String.format("%.2f", Double.parseDouble(textPrice.getText().toString())) + " - " + Rmethod, String.format("%.2f", Double.parseDouble(textPrice.getText().toString())), Same_p_qty.toString());

                    Rmethod = "";
                } else {

                    Toast.makeText(getApplicationContext(), "Please Enter the Invoice Quatity !!!", Toast.LENGTH_SHORT).show();

                }


                radioSexGroup.clearCheck();


            }
        });
        btnRem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stu
                DeleteItem(itemdelete);
                stk.removeAllViews();
                //stk.removeAllViewsInLayout();
                stk.invalidate();
                stk.refreshDrawableState();
                datat();
                itemdelete = "";


            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stu
//                startActivity(new Intent("com.example.mobile.INVTYPE"));
                Intent intent = new Intent(MktretActivity .this, InvtypeActivity .class);
                MktretActivity.this.startActivity(intent);


            }
        });
        textQTY.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if(!s.equals("") )
                { //do your work here }
                    radioSexGroup.clearCheck();
                    Rmethod="";
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
            public void onItemSelected(AdapterView<?> arg0, View view, int arg2, long arg3) {

                //  String selected_val=spinner.getSelectedItem().toString();
                //  spinnerf.setAdapter(null);
                //	 Toast.makeText(getApplicationContext(),"InvalSSSSSSSSSSSSid Quatity...???",Toast.LENGTH_SHORT).show();
                radioSexGroup.clearCheck();
                texfqty.setText("");
                spinnerf.setAdapter(null);
                fpid = "";

                pid = ItemP[arg2];
                pname = ItemN[arg2];

                DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                Cursor cursor = db.GetC("select * from PDAProduct where ProductID='" + pid.toString() + "' ");
                // Toast.makeText(getApplicationContext(), cursor.getString(2).toString(),Toast.LENGTH_LONG).show();
                while (cursor.moveToNext()) {
                    SPrice = Double.valueOf(cursor.getString(cursor.getColumnIndex("Price")));
                    textPrice.setText(cursor.getString(cursor.getColumnIndex("Price")));
                    //   text2.setText(cursor.getString(cursor.getColumnIndex("Quantity")));
                    // PQTY=Integer.valueOf(cursor.getString(cursor.getColumnIndex("Quantity")));
                    fcate = cursor.getString(cursor.getColumnIndex("FreeIssue"));
                    fdis = cursor.getString(cursor.getColumnIndex("Discount"));
                }

                textDIS.setText("");

                textQTY.requestFocus();
                textQTY.selectAll();


                DisValue = 0.0;
                fqty = 0;

                DisView.setText("(No Dis.)");

                textDIS.setEnabled(false);
                if (fdis.equals("No")) {
                    textDIS.setEnabled(false);
                } else {
                  //  textDIS.setEnabled(true);
                    if (fdis.equals("Rs")) {
                        DisView.setText("( " + fdis + " )");
                    } else {

                        Cursor cursor1 = db.GetC("select * from PDADiscountSpecial where CatID='" + fdis + "' ");
                        // Toast.makeText(getApplicationContext(), cursor.getString(2).toString(),Toast.LENGTH_LONG).show();
                        while (cursor1.moveToNext()) {
                            DisView.setText("( " + cursor1.getString(cursor1.getColumnIndex("DiscountMethod")) + " )");
                        }

                    }

                }


                texfqty.setEnabled(false);
                if (fcate.equals("No")) {
                    texfqty.setEnabled(false);
                } else {
                  //texfqty.setEnabled(true);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        spinnerf.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int arg2, long arg3) {

                String selected_val = spinner.getSelectedItem().toString();


                fpid = ItemF[arg2];
                fqty=Integer.valueOf(ItemFQ[arg2]);
                texfqty.setText(ItemFQ[arg2]);

                //fqty=Integer.valueOf(ItemF[arg2].substring(6, 10).toString().trim());


//	        		  Toast.makeText(getApplicationContext(), ItemF[arg2],
//	  	                    Toast.LENGTH_SHORT).show();

                if (fdis.equals("No")) {
                    textDIS.setEnabled(false);
                } else {
                  //textDIS.setEnabled(true);
                }

                if (fcate.equals("No")) {
                    texfqty.setEnabled(false);
                } else {
                //    texfqty.setEnabled(true);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });


        texfqty.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                //	Toast.makeText(getApplicationContext(),fpid,Toast.LENGTH_SHORT).show();
//					  if(fpid.equals(""))
//		                {
//						 //Toast.makeText(getApplicationContext(), "Please Select Return Method...????",Toast.LENGTH_SHORT).show();
//						 // texfqty.setText("");
//		               return;
//		                }



                if (textQTY.getText().toString().equals("")) {
                    // Toast.makeText(getApplicationContext(),"Please Enter the Invoice Qty...???",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (count > 0) { //do your work here }
                    if (Integer.valueOf(s.toString()) > Integer.valueOf(textQTY.getText().toString())) { //do your work here
                        texfqty.setText("");
                        Toast.makeText(getApplicationContext(), "Invalid Quatity...???", Toast.LENGTH_SHORT).show();

                    }
//                    else {
//                        fqty = Integer.valueOf(s.toString());
//                    }


                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
    }

    // Event handler for radio buttons
    public void onRadioButtonClicked(View view) {

        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        Discount_Write=0.0;
        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.S:
                if (checked)
                    // Toast.makeText(this, "You've selected: s", Toast.LENGTH_LONG).show();
                    // Toast.makeText(getApplicationContext(), "You've selected: s",Toast.LENGTH_SHORT).show();
                    Rmethod = "S";

                spinnerf.setAdapter(null);
                loadSpinnerDataF(fcate, pid);


//            textDIS.setEnabled(true);
           texfqty.setEnabled(true);

                // String[] separated = spinner.getSelectedItem().toString().split("-");
                //     separated[0]; // this will contain "Fruit"
                // separated[1]; // this will contain " they taste good"
                //  Toast.makeText(getApplicationContext(), "You've selected: s"+separated[0],Toast.LENGTH_SHORT).show();
//            String imgpath = "/mnt/sdcard/joke.png";
//            String result = imgpath.substring(imgpath.lastIndexOf("/") + 1);
//            System.out.println("Image name " + result);
//            Output :-Image name joke.png

                break;
            case R.id.E:
                if (checked)
                    spinnerf.setAdapter(null);
                textDIS.setText("");
                texfqty.setText("");
                textDIS.setEnabled(false);
                texfqty.setEnabled(false);

                //Toast.makeText(this, "You've selected: E", Toast.LENGTH_LONG).show();
                Rmethod = "E";
                //  Toast.makeText(getApplicationContext(), "You've selected: E",Toast.LENGTH_SHORT).show();
                break;
            case R.id.D:
                if (checked)
                    spinnerf.setAdapter(null);

                textDIS.setText("");
                texfqty.setText("");
                textDIS.setEnabled(false);
                texfqty.setEnabled(false);

                Rmethod = "D";
                //  Toast.makeText(this, "You've selected: D", Toast.LENGTH_LONG).show();
                break;
            case R.id.StExp:
                if (checked)
                    spinnerf.setAdapter(null);

                textDIS.setText("");
                texfqty.setText("");
                textDIS.setEnabled(false);
                texfqty.setEnabled(false);

                Rmethod = "T";
                //  Toast.makeText(this, "You've selected: D", Toast.LENGTH_LONG).show();
                break;
        }
    }


    // Disabled Back Button On 4n
    @Override
    public void onBackPressed() {
    }
    public void dialogBox() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Click on Image for tag");
        alertDialogBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });

        alertDialogBuilder.setNegativeButton("cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void DeleteItem(String a) {
//        for (int i = 0; i < MyGloble.DataTableMKTR.length; i++) {
//            if (MyGloble.DataTableMKTR[i][0] == a) {
//                MyGloble.DataTableMKTR[i][0] = "";
//                MyGloble.DataTableMKTR[i][1] = "";
//                MyGloble.DataTableMKTR[i][2] = "";
//                MyGloble.DataTableMKTR[i][3] = "";
//                MyGloble.DataTableMKTR[i][4] = "";
//                MyGloble.DataTableMKTR[i][5] = "";
//                MyGloble.DataTableMKTR[i][6] = "";
//                MyGloble.DataTableMKTR[i][7] = "";
//                MyGloble.DataTableMKTR[i][8] = "";
//                MyGloble.DataTableMKTR[i][9] = "";
//                MyGloble.DataTableMKTR[i][10] = "";
//
//
//            }
//        }

        Cursor cursor2=db.GetC("select PDAMarketReturn.ProductID,ReturnMethod,PDAMarketReturn.Qty,PDAMarketReturn.FIQty, PDAMarketReturn.FID from PDAMarketReturn,PDAProduct WHERE PDAProduct.ProductID=PDAMarketReturn.ProductID AND PDAProduct.ProductID='" +a+"' AND   InvID='"+ MyGloble.InvoiceNo+"'   ");
      //  Toast.makeText(getApplicationContext(),String.valueOf( cursor2.getCount()),Toast.LENGTH_LONG).show();
        if( cursor2.getCount() > 0) {

            if (cursor2.moveToFirst()) {
                while (!cursor2.isAfterLast()) {
                    if (cursor2.getString(cursor2.getColumnIndex("ReturnMethod")).equals("S"))
                    {
                        MyGloble.db.execSQL("Update PDAProduct SET Quantity=Quantity - '" + cursor2.getString(cursor2.getColumnIndex("Qty")) + "' WHERE ProductID = '"+cursor2.getString(cursor2.getColumnIndex("ProductID"))+"'" );
                        MyGloble.db.execSQL("Update PDAProduct SET Quantity=Quantity - '" + cursor2.getString(cursor2.getColumnIndex("FIQty")) + "' WHERE ProductID = '"+cursor2.getString(cursor2.getColumnIndex("FID"))+"' " );
                    }
                    MyGloble.db.execSQL("DELETE from PDAMarketReturn WHERE ReturnMethod='" +cursor2.getString(cursor2.getColumnIndex("ReturnMethod"))+"' and  ProductID = '"+cursor2.getString(cursor2.getColumnIndex("ProductID"))+"' AND   InvID='"+ MyGloble.InvoiceNo+"'" );
                    cursor2.moveToNext();
                }
            }
        }


    }

    public void CheckDis(Integer qty) {
        if (!textDIS.getText().toString().equals("")) {
            Discount_Write = Double.valueOf(textDIS.getText().toString());
            DisValue = Double.valueOf(textDIS.getText().toString());


            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
            Cursor cursor2 = db.GetC("select * from PDAProduct,PDADiscountSpecial WHERE PDAProduct.Discount=PDADiscountSpecial.CatID AND PDAProduct.ProductID='" + pid + "' ");
            if (cursor2.getCount() > 0) {
                Toast.makeText(getApplicationContext(), "You must give Discount As a Percentage(%) Value....!!!", Toast.LENGTH_LONG).show();
                if (cursor2.moveToFirst()) {

                    while (!cursor2.isAfterLast()) {
                        if (cursor2.getString(cursor2.getColumnIndex("DiscountMethod")).equals("%")) {
                            // DisValue=Double.valueOf(text3.getText().toString())*Double.valueOf(text2.getText().toString());
                            DisValue = ((Double.valueOf(textPrice.getText().toString()) / 100) * Double.valueOf(textDIS.getText().toString())) * Double.valueOf(qty);
                        }
                        cursor2.moveToNext();
                    }
                }

            } else {
                Toast.makeText(getApplicationContext(), "You must give Discount As a (Rs.) Value ....!!!", Toast.LENGTH_LONG).show();
            }


        }


    }

    public void LoadTable(String a1, String a2, String a3) {
        // pname/price/qty/dis/subtot/fqty/Fpid/disM/pid/fcate
//		if (PQTY < Integer.valueOf(a3))
//		{
//			Toast.makeText(getApplicationContext(),"No Enough Stock Quantity", Toast.LENGTH_LONG).show();
//			return;
//		}

//		if (PQTY < Integer.valueOf(a3))
//		{
//			Toast.makeText(getApplicationContext(),"No Enough Stock Quantity", Toast.LENGTH_LONG).show();
//			return;
//		}


//        for (int i = 0; i < MyGloble.DataTableMKTR.length; i++) {
//            if (!MyGloble.DataTableMKTR[i][0].equals("")) {
//                if (MyGloble.DataTableMKTR[i][8].equals(pid) && MyGloble.DataTableMKTR[i][7].equals(Rmethod)) {
//                    MyGloble.DataTableMKTR[i][0] = "";
//                    MyGloble.DataTableMKTR[i][1] = "";
//                    MyGloble.DataTableMKTR[i][2] = "";
//                    MyGloble.DataTableMKTR[i][3] = "";
//                    MyGloble.DataTableMKTR[i][4] = "";
//                    MyGloble.DataTableMKTR[i][5] = "";
//                    MyGloble.DataTableMKTR[i][6] = "";
//                    MyGloble.DataTableMKTR[i][7] = "";
//                    MyGloble.DataTableMKTR[i][8] = "";
//                    MyGloble.DataTableMKTR[i][9] = "";
//                    MyGloble.DataTableMKTR[i][10] = "";
//                }
//            }
//        }

        if (fqty == 0) {
            fpid = "-1";
        }

        Double subtot = 0.0;
        subtot = (Double.valueOf(a2) * Double.valueOf(a3)) - DisValue;

        // Toast.makeText(getApplicationContext(),"Discount_Write " + Discount_Write, Toast.LENGTH_LONG).show();

//        MyGloble.DtcountMKTR = MyGloble.DtcountMKTR + 1;
//
//        MyGloble.DataTableMKTR[MyGloble.DtcountMKTR][0] = a1; //pname
//        MyGloble.DataTableMKTR[MyGloble.DtcountMKTR][1] = a2;//price
//        MyGloble.DataTableMKTR[MyGloble.DtcountMKTR][2] = a3;//qty
//        MyGloble.DataTableMKTR[MyGloble.DtcountMKTR][3] = String.format("%.2f", DisValue);//dis
//        MyGloble.DataTableMKTR[MyGloble.DtcountMKTR][4] = String.format("%.2f", subtot);//subtot
//        MyGloble.DataTableMKTR[MyGloble.DtcountMKTR][5] = String.valueOf(fqty); //fqty
//        MyGloble.DataTableMKTR[MyGloble.DtcountMKTR][6] = fpid; //Fpid
//        MyGloble.DataTableMKTR[MyGloble.DtcountMKTR][7] = Rmethod; //Rmethod
//        MyGloble.DataTableMKTR[MyGloble.DtcountMKTR][8] = pid; //pid
//        MyGloble.DataTableMKTR[MyGloble.DtcountMKTR][9] = ""; //fcate
//        MyGloble.DataTableMKTR[MyGloble.DtcountMKTR][10] = String.valueOf(Discount_Write); //Write Discount into table
        if (Rmethod.equals("S"))
        {
            Cursor cursor6=db.GetC("select * from PDAMarketReturn WHERE ProductID = '"+pid+"' AND ReturnMethod='"+Rmethod+"' AND    InvID='"+ MyGloble.InvoiceNo+"'   ");
            if( cursor6.getCount() > 0) {
                if (cursor6.moveToFirst()) {
                    MyGloble.db.execSQL("Update PDAProduct SET Quantity=Quantity - '" + fqty + "' WHERE ProductID = '"+fpid+"' " );
                }
            }
        }


        Cursor cursor5=db.GetC("select * from PDAMarketReturn WHERE ProductID = '"+pid+"' AND ReturnMethod='"+Rmethod+"' AND    InvID='"+ MyGloble.InvoiceNo+"'   ");
        if( cursor5.getCount() > 0) {
            if (cursor5.moveToFirst()){

                MyGloble.db.execSQL("Update PDAMarketReturn SET Qty=Qty+'"+a3+"' , FID='"+fpid+"' , FIQty='"+fqty+"' , Discount='"+String.format("%.2f",Discount_Write)+"' , DisAmount='"+String.format("%.2f",DisValue)+"' , SubTot=SubTot+'"+String.format("%.2f",subtot)+"'  WHERE ProductID= '"+pid+"'  AND ReturnMethod='"+Rmethod+"'  AND   InvID='" + MyGloble.InvoiceNo + "'");
                if (Rmethod.equals("S"))
                {
                    MyGloble.db.execSQL("Update PDAProduct SET Quantity=Quantity + '" + a3 + "' WHERE ProductID = '"+pid+"'" );
                    MyGloble.db.execSQL("Update PDAProduct SET Quantity=Quantity + '" + fqty + "' WHERE ProductID = '"+fpid+"' " );
                }

                cursor5.moveToNext();
            }
        }else
        {
            MyGloble.db.execSQL("insert into PDAMarketReturn values ('"+MyGloble.InvoiceNo+"' ,'"+ pid +"','"+a3+"','"+Discount_Write+"','"+Rmethod+"','"+a2+"','"+fpid+"','"+fqty+"','"+String.format("%.2f",DisValue)+"','"+String.format("%.2f",subtot)+"','"+RetInvNo+"','N')" );
            if (Rmethod.equals("S"))
            {
                MyGloble.db.execSQL("Update PDAProduct SET Quantity=Quantity + '" + a3 + "' WHERE ProductID = '"+pid+"'" );
                MyGloble.db.execSQL("Update PDAProduct SET Quantity=Quantity + '" + fqty + "' WHERE ProductID = '"+fpid+"' " );
            }

        }


        MyGloble.TotalMKTR = 0.0;
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


        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd h:mm a");
        String date = df.format(Calendar.getInstance().getTime());

        Cursor cursor7=db.GetC("select InvoiceID from PDAInvoice WHERE   InvoiceID='"+ MyGloble.InvoiceNo+"'   ");
        if( cursor7.getCount() > 0) {
            MyGloble.db.execSQL("Update PDAInvoice SET MktReturn='" + MyGloble.TotalMKTR +"',IsMktReturn='Yes' WHERE InvoiceID='"+MyGloble.InvoiceNo+"' " );
        }else {
            MyGloble.db.execSQL("insert into PDAInvoice values ('"+MyGloble.InvoiceNo+"' ,'"+ MyGloble.RID +"','','','','Not-Printed','"+ MyGloble.TotalMKTR+"','"+date+"','No','Yes','0.00','0')" );
        }


        stk.removeAllViews();
        datat();


        //	text7.setText( String.format("%.2f",MyGloble.TotalMKTR) );


    }

    private void loadSpinnerData() {
        // database handler
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        Cursor C = db.GetC("select * from PDAProduct Order By ProductName");
        List<String> labels = new ArrayList<String>();
        //Toast.makeText(getApplicationContext(),String.valueOf( C.getCount()), Toast.LENGTH_LONG).show();

        ItemP = new String[C.getCount()];
        ItemN = new String[C.getCount()];

        Integer a = 0;
        while (C.moveToNext()) {


            ItemP[a] = C.getString(0);
            ItemN[a] = C.getString(6);
            labels.add(C.getString(6) + " - " + String.format("%.2f", C.getDouble(2)));
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
        spinner.setAdapter(dataAdapter);

    }


    private void loadSpinnerDataF(String fcate, String pid) {
        // database handler
//		    DatabaseHandler db = new DatabaseHandler(getApplicationContext());
//
//		    Cursor C = db.GetC("select * from PDAProduct WHERE ProductName LIKE '"+name.substring(0, 4)+"%'");
//			List<String> labels = new ArrayList<String>();
//			//Toast.makeText(getApplicationContext(),String.valueOf( C.getCount()), Toast.LENGTH_LONG).show();
//
//			ItemF = new String[C.getCount()];
//
//			Integer a=0;
//				while(C.moveToNext())
//				{
//				ItemF[a]=C.getString(0);
//				labels.add(C.getString(6) + " - " + String.format("%.2f",C.getDouble(2)));
//				a=a+1;
//				}

        if (textQTY.getText().toString().equals(""))
        {
            DialogBoxOneButton("Please Enter the return qty...????", "Market Return");
            return;
        }

        if (Double .valueOf(textQTY.getText().toString())<=0)
        {
            DialogBoxOneButton("Please Enter the correct qty...????", "Market Return");
            return;
        }


        texfqty.setText("");
        textDIS.setText("");

        DisValue= 0.0;
        Discount_Write=0.0;

        fqty=0;


        if (fcate.equals("No")) {

        } else if (fcate.equals("FI")) {
            FreeIssue(pid);
        } else {
            //Toast.makeText(getApplicationContext(),"khghjbbkb",Toast.LENGTH_LONG).show();
            FreeIssueSpecial(fcate);

        }

        if(fdis.equals("No")){

        }else if(fdis.equals("Rs")){
            //Toast.makeText(getApplicationContext(),fcate,Toast.LENGTH_LONG).show();
            Discount();
        }else{
            DiscountSpecial(fdis);
        }

    }
    public void Discount() {
        Discount_Write=0.0;
        Integer Catefqty=0;
        Catefqty=Integer.valueOf(textQTY.getText().toString());
        Cursor cursor1=db.GetC("select Qty from PDAMarketReturn WHERE  ProductID='"+pid+"' AND  InvID='"+ MyGloble.InvoiceNo+"'   ");
        if( cursor1.getCount() > 0) {
            if (cursor1.moveToFirst())
            {
                while (!cursor1.isAfterLast()) {

                    Catefqty=Catefqty+ Integer.valueOf(cursor1.getString(cursor1.getColumnIndex("Qty")));
                    cursor1.moveToNext();
                }
            }
        }

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
                        textDIS.setText(cursor.getString(2));

                        break;
                    }

                    cursor.moveToNext();
                }
            }
        }

    }

    public void DiscountSpecial(String Dcate) {
        Integer Catefqty=0;

        Catefqty=Integer.valueOf(textQTY.getText().toString());

        Integer sameProInvQty ;
        sameProInvQty=0;

        Discount_Write=0.0;

        Cursor cursor1=db.GetC("select Qty from PDAMarketReturn WHERE  ProductID='"+pid+"' AND  InvID='"+ MyGloble.InvoiceNo+"'   ");
        if( cursor1.getCount() > 0) {
            if (cursor1.moveToFirst())
            {
                while (!cursor1.isAfterLast()) {

                    Catefqty=Catefqty+ Integer.valueOf(cursor1.getString(cursor1.getColumnIndex("Qty")));
                    cursor1.moveToNext();
                }
            }
        }


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
                        // Toast.makeText(getApplicationContext(),cursor.getString(2),Toast.LENGTH_LONG).show();
                        if(cursor.getString(3).equals("Rs")){

                            DisValue= Double.valueOf(cursor.getString(2));
                            Discount_Write=Double.valueOf(cursor.getString(2));
                        }else if(cursor.getString(3).equals("%")){
                            //  Toast.makeText(getApplicationContext(),cursor.getString(2),Toast.LENGTH_LONG).show();
                            DisValue=((Double.valueOf(textPrice.getText().toString())/100)*Double.valueOf(cursor.getString(2)))* Double.valueOf(Catefqty);
                            Discount_Write= Double.valueOf(cursor.getString(2));
                        }

                        textDIS.setText(String.valueOf(Discount_Write));
                        break;
                    }

                    cursor.moveToNext();
                }
            }
        }

    }

    public void FreeIssue(String pid) {

        ItemF = new String[10];
        ItemFQ = new String[10];
        Integer a = 0;
        Integer Catefqty=0;
        Catefqty=Integer.valueOf(textQTY.getText().toString());

        Cursor cursor1=db.GetC("select Qty from PDAMarketReturn WHERE  ProductID='"+pid+"' AND  InvID='"+ MyGloble.InvoiceNo+"'   ");
        if( cursor1.getCount() > 0) {
            if (cursor1.moveToFirst())
            {
                while (!cursor1.isAfterLast()) {

                    Catefqty=Catefqty+ Integer.valueOf(cursor1.getString(cursor1.getColumnIndex("Qty")));
                    cursor1.moveToNext();
                }
            }
        }



        List<String> labels = new ArrayList<String>();
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

                                    texfqty.setText(ItemFQ[a]);
                                    fqty=Integer.valueOf(ItemFQ[a]);

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
    private void loadSpinnerFree(List<String> labels) {

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, labels);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerf.setAdapter(dataAdapter);
    }
    private void FreeIssueSpecial(String cate) {
        //Toast.makeText(getApplicationContext(),cate,Toast.LENGTH_LONG).show();

        Integer a = 0;
        List<String> labels = new ArrayList<String>();
        ItemF = new String[40];
        ItemFQ = new String[40];

        Integer Catefqty=0;
        Catefqty=Integer.valueOf(textQTY.getText().toString());

        Cursor cursor1=db.GetC("select Qty from PDAMarketReturn WHERE  ProductID='"+pid+"' AND  InvID='"+ MyGloble.InvoiceNo+"'   ");
        if( cursor1.getCount() > 0) {
            if (cursor1.moveToFirst())
            {
                while (!cursor1.isAfterLast()) {

                    Catefqty=Catefqty+ Integer.valueOf(cursor1.getString(cursor1.getColumnIndex("Qty")));
                    cursor1.moveToNext();
                }
            }
        }

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
                                                    texfqty.setText(ItemFQ[a]);
                                                    fqty=Integer.valueOf(ItemFQ[a]);


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

    private void datat() {

        TableRow tbrow0 = new TableRow(this);
        TextView tv0 = new TextView(this);
        tv0.setText("Product              ");
        tv0.setTextColor(Color.BLACK);
        tv0.setGravity(Gravity.LEFT);
        tv0.setTypeface(null, Typeface.BOLD);
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(this);
        tv1.setText("     Qty");
        tv1.setTextColor(Color.BLACK);
        tv1.setGravity(Gravity.RIGHT);
        tv1.setTypeface(null, Typeface.BOLD);
        tbrow0.addView(tv1);
        TextView tv2 = new TextView(this);
        tv2.setText("   F_Qty");
        tv2.setTextColor(Color.BLACK);
        tv2.setGravity(Gravity.RIGHT);
        tv2.setTypeface(null, Typeface.BOLD);
        tbrow0.addView(tv2);
        TextView tv3 = new TextView(this);
        tv3.setText("      Dis.");
        tv3.setTextColor(Color.BLACK);
        tv3.setGravity(Gravity.RIGHT);
        tv3.setTypeface(null, Typeface.BOLD);
        tbrow0.addView(tv3);

        TextView tv4 = new TextView(this);
        tv4.setText("     S_Total.");
        tv4.setTextColor(Color.BLACK);
        tv4.setGravity(Gravity.RIGHT);
        tv4.setTypeface(null, Typeface.BOLD);
        tbrow0.addView(tv4);

        TextView tv6 = new TextView(this);
        tv6.setText(" P_ID");
        tv6.setTextColor(Color.BLACK);
        tv6.setGravity(Gravity.RIGHT);
        tbrow0.addView(tv6);
        tv6.setTextSize(0);



        stk.addView(tbrow0);


    Cursor cursor2=db.GetC("select PDAProduct.Price,PDAMarketReturn.ReturnMethod,PDAMarketReturn.ProductID,ProductName,PDAMarketReturn.Qty,PDAMarketReturn.FIQty, PDAMarketReturn.DisAmount,PDAMarketReturn.SubTot from PDAMarketReturn,PDAProduct WHERE PDAProduct.ProductID=PDAMarketReturn.ProductID AND  InvID='"+ MyGloble.InvoiceNo+"'   ");
        if( cursor2.getCount() > 0) {
            if (cursor2.moveToFirst())
            {
                while (!cursor2.isAfterLast()) {

                    TableRow tbrow = new TableRow(this);
                    TextView t1v = new TextView(this);
                    t1v.setText(cursor2.getString(cursor2.getColumnIndex("ProductName"))+"-"+cursor2.getString(cursor2.getColumnIndex("Price"))+"-"+cursor2.getString(cursor2.getColumnIndex("ReturnMethod")));
                    t1v.setTextColor(Color.BLACK);
                    t1v.setGravity(Gravity.LEFT);
                    t1v.setTextSize(12);
                    tbrow.addView(t1v);
                    TextView t2v = new TextView(this);
                    t2v.setText(cursor2.getString(cursor2.getColumnIndex("Qty")));
                    t2v.setTextColor(Color.BLACK);
                    t2v.setGravity(Gravity.RIGHT);
                    t2v.setTextSize(12);
                    tbrow.addView(t2v);
                    TextView t3v = new TextView(this);
                    t3v.setText(cursor2.getString(cursor2.getColumnIndex("FIQty")));
                    t3v.setTextColor(Color.BLACK);
                    t3v.setGravity(Gravity.RIGHT);
                    t3v.setTextSize(12);
                    tbrow.addView(t3v);
                    TextView t4v = new TextView(this);
                    t4v.setText(cursor2.getString(cursor2.getColumnIndex("DisAmount")));
                    t4v.setTextColor(Color.BLACK);
                    t4v.setGravity(Gravity.RIGHT);
                    t4v.setTextSize(12);
                    tbrow.addView(t4v);
                    TextView t5v = new TextView(this);
                    t5v.setText(cursor2.getString(cursor2.getColumnIndex("SubTot")));
                    t5v.setTextColor(Color.BLACK);
                    t5v.setGravity(Gravity.RIGHT);
                    t5v.setTextSize(12);
                    t5v.setTypeface(null, Typeface.BOLD);
                    tbrow.addView(t5v);

                    TextView t6v = new TextView(this);
                    t6v.setText(cursor2.getString(cursor2.getColumnIndex("ProductID")));
                    t6v.setTextColor(Color.BLACK);
                    t6v.setGravity(Gravity.RIGHT);
                    tbrow.addView(t6v);
                    t6v.setTextSize(0);


                    tbrow.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {


                            TableRow tr1 = (TableRow) view;
                            TextView t6v = (TextView) tr1.getChildAt(5);
                            itemdelete = (t6v.getText().toString());
                            view.setBackgroundColor(Color.MAGENTA);


                        }
                    });
                    stk.addView(tbrow);


                    cursor2.moveToNext();
                }
            }
        }


//
//                    for (int i = 0; i < MyGloble.DataTableMKTR.length; i++) {
//            if (!MyGloble.DataTableMKTR[i][0].equals("")) {
//                TableRow tbrow = new TableRow(this);
//                TextView t1v = new TextView(this);
//                t1v.setText(MyGloble.DataTableMKTR[i][0]);
//                t1v.setTextColor(Color.BLACK);
//                t1v.setGravity(Gravity.LEFT);
//                t1v.setTextSize(12);
//                tbrow.addView(t1v);
//                TextView t2v = new TextView(this);
//                t2v.setText(MyGloble.DataTableMKTR[i][2]);
//                t2v.setTextColor(Color.BLACK);
//                t2v.setGravity(Gravity.RIGHT);
//                t2v.setTextSize(12);
//                tbrow.addView(t2v);
//                TextView t3v = new TextView(this);
//                t3v.setText(MyGloble.DataTableMKTR[i][5]);
//                t3v.setTextColor(Color.BLACK);
//                t3v.setGravity(Gravity.RIGHT);
//                t3v.setTextSize(12);
//                tbrow.addView(t3v);
//
//                TextView t4v = new TextView(this);
//                t4v.setText(MyGloble.DataTableMKTR[i][3]);
//                t4v.setTextColor(Color.BLACK);
//                t4v.setGravity(Gravity.RIGHT);
//                t4v.setTextSize(12);
//                tbrow.addView(t4v);
//
//                TextView t5v = new TextView(this);
//                t5v.setText(MyGloble.DataTableMKTR[i][4]);
//                t5v.setTextColor(Color.BLACK);
//                t5v.setGravity(Gravity.RIGHT);
//                t5v.setTextSize(12);
//                t5v.setTypeface(null, Typeface.BOLD);
//                tbrow.addView(t5v);
//
//                tbrow.setOnClickListener(new View.OnClickListener() {
//                    public void onClick(View view) {
//
//
//                        TableRow tr1 = (TableRow) view;
//                        TextView tv1 = (TextView) tr1.getChildAt(0);
//
//                        itemdelete = (tv1.getText().toString());
//
//                        view.setBackgroundColor(Color.MAGENTA);
//
//
//                    }
//                });
//                stk.addView(tbrow);
//            }
//
//        }
//



        MyGloble.TotalMKTR = 0.0;
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


//        MyGloble.TotalMKTR = 0.0;
//        for (int i = 0; i < MyGloble.DataTableMKTR.length; i++) {
//            if (!MyGloble.DataTableMKTR[i][0].equals("")) {
//                MyGloble.TotalMKTR = MyGloble.TotalMKTR + (Double.valueOf(MyGloble.DataTableMKTR[i][4]));
//            }
//        }

    }

     public void DialogBoxOneButton(String msg,String head )
    {
        AlertDialog alertDialog = new AlertDialog.Builder(MktretActivity.this).create();

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

