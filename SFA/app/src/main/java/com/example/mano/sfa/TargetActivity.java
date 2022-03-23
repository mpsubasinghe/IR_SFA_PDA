package com.example.mano.sfa;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class TargetActivity extends AppCompatActivity {
    TableLayout stk;

    Button btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target);
        stk = (TableLayout) findViewById(R.id.table_main);

        btnBack=(Button)findViewById(R.id.button1);


        datat();

        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub MyGloble.invedit


                Intent intent = new Intent(TargetActivity .this, MenucActivity .class);
                TargetActivity.this.startActivity(intent);




            }
        });

    }


    // Disabled Back Button On 4n
    @Override
    public void onBackPressed() {
    }


    private void datat() {

        stk.setColumnStretchable(0, true);
        stk.setColumnStretchable(2, true);

        TableRow tbrow0 = new TableRow(this);
        TextView tv0 = new TextView(this);
        tv0.setText("Product");
        tv0.setTextColor(Color.BLACK);
        tv0.setGravity(Gravity.LEFT);
        tv0.setBackgroundColor(Color.GREEN);
        tv0.setWidth(300);
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(this);
        tv1.setText("  DS");
        tv1.setTextColor(Color.BLACK);
        tv1.setGravity(Gravity.RIGHT);
        tv1.setBackgroundColor(Color.GREEN);
        tv1.setWidth(100);
        tbrow0.addView(tv1);
        TextView tv2 = new TextView(this);
        tv2.setText(" DT");
        tv2.setTextColor(Color.BLACK);
        tv2.setGravity(Gravity.RIGHT);
        tv2.setBackgroundColor(Color.GREEN);
        tv2.setWidth(100);
        tbrow0.addView(tv2);
        TextView tv3 = new TextView(this);
        tv3.setText("PPC");
        tv3.setTextColor(Color.BLACK);
        tv3.setGravity(Gravity.RIGHT);
        tv3.setBackgroundColor(Color.GREEN);
        tv3.setWidth(100);
        tbrow0.addView(tv3);
        TextView tv4 = new TextView(this);
        tv4.setText("BF");
        tv4.setTextColor(Color.BLACK);
        tv4.setGravity(Gravity.RIGHT);
        tv4.setBackgroundColor(Color.GREEN);
        tv4.setWidth(100);
        tbrow0.addView(tv4);
        TextView tv5 = new TextView(this);
        tv5.setText("MT");
        tv5.setTextColor(Color.BLACK);
        tv5.setGravity(Gravity.RIGHT);
        tv5.setBackgroundColor(Color.GREEN);
        tv5.setWidth(100);
        tbrow0.addView(tv5);
        stk.addView(tbrow0);

        String pname="";
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
      //  Cursor cursor2=db.GetC("select * from PDATarget");
        Cursor cursor2=db.GetC("select PDATarget.ProductID,DailyTarget,PPC,BFSales,MonthlyTarget from PDATarget,PDAProduct WHERE PDAProduct.ProductID=PDATarget.ProductID Order BY ProCatNo ");
        if( cursor2.getCount() > 0)
        {

            if (cursor2.moveToFirst())

            {
                while(!cursor2.isAfterLast())
                {
                    //Toast.makeText(getApplicationContext(), cursor2.getString(cursor2.getColumnIndex("ProductID")),Toast.LENGTH_LONG).show();
                    Cursor cursor22=db.GetC("select * from PDAProduct WHERE ProductID='"+ cursor2.getString(cursor2.getColumnIndex("ProductID"))+"'  Order BY ProCatNo");
                    if( cursor22.getCount() > 0)
                    {
                        if (cursor22.moveToFirst())

                        {
                            while(!cursor22.isAfterLast())
                            {
                                pname=cursor22.getString(cursor22.getColumnIndex("ProductName"))+ " - "+cursor22.getString(cursor22.getColumnIndex("Price"));
                                cursor22.moveToNext();
                            }

                        }
                        cursor22.close();
                    }

                    Integer ds=0;
                    Cursor cursor23=db.GetC("select SUM(Quantity) AS QTY ,PDAInvoicedProduct.ProductID from PDAInvoicedProduct,PDAInvoice WHERE PDAInvoice.InvoiceID=PDAInvoicedProduct.InvoicedID AND Cancel='No' AND PDAInvoicedProduct.ProductID='"+ cursor2.getString(cursor2.getColumnIndex("ProductID"))+"' GROUP BY PDAInvoicedProduct.ProductID");
                    if( cursor23.getCount() > 0)
                    {
                        if (cursor23.moveToFirst())

                        {
                            while(!cursor23.isAfterLast())
                            {
                                ds=Integer.valueOf(cursor23.getString(cursor23.getColumnIndex("QTY")));
                                cursor23.moveToNext();
                            }

                        }
                        cursor23.close();

                    }

                    TableRow tbrow = new TableRow(this);
                    TextView t1v = new TextView(this);
                    t1v.setText(pname);
                    t1v.setTextColor(Color.BLACK);
                    t1v.setGravity(Gravity.LEFT);
                    t1v.setBackgroundColor(Color.WHITE);
                    tbrow.addView(t1v);
                    TextView t2v = new TextView(this);
                    t2v.setText(String.valueOf(ds));
                    t2v.setTextColor(Color.BLACK);
                    t2v.setGravity(Gravity.RIGHT);
                    t2v.setBackgroundColor(Color.CYAN);
                    tbrow.addView(t2v);
                    TextView t3v = new TextView(this);
                    t3v.setText(cursor2.getString(cursor2.getColumnIndex("DailyTarget")));
                    t3v.setTextColor(Color.BLACK);
                    t3v.setGravity(Gravity.RIGHT);
                    t3v.setBackgroundColor(Color.YELLOW );
                    tbrow.addView(t3v);

                    TextView t4v = new TextView(this);
                    t4v.setText(cursor2.getString(cursor2.getColumnIndex("PPC")));
                    t4v.setTextColor(Color.BLACK);
                    t4v.setGravity(Gravity.RIGHT);
                    t4v.setBackgroundColor(Color.MAGENTA);
                    tbrow.addView(t4v);

                    TextView t5v = new TextView(this);
                    t5v.setText(String.valueOf(Integer.valueOf(cursor2.getString(cursor2.getColumnIndex("BFSales")))+ds));
                    t5v.setTextColor(Color.BLACK);
                    t5v.setGravity(Gravity.RIGHT);
                    t5v.setBackgroundColor(Color.LTGRAY);
                    tbrow.addView(t5v);

                    TextView t6v = new TextView(this);
                    t6v.setText(cursor2.getString(cursor2.getColumnIndex("MonthlyTarget")));
                    t6v.setTextColor(Color.BLACK);
                    t6v.setGravity(Gravity.RIGHT);
                    t6v.setBackgroundColor(Color.YELLOW );
                    tbrow.addView(t6v);

                    stk.addView(tbrow);


                    //Toast.makeText(getApplicationContext(), "Sorry..!!! Already You have Cancel this Invoice!!!!",Toast.LENGTH_LONG).show();
                    cursor2.moveToNext();
                }
            }

            cursor2.close();
        }




    }





}

