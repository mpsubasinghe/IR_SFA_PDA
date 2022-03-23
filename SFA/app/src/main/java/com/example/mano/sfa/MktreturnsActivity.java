package com.example.mano.sfa;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MktreturnsActivity extends AppCompatActivity {

    EditText textTot;
    Integer Itempossition;

    DatabaseHandler db;
    Button btnBack;


    TextView textname;
    TextView textAdd;


    String itemdelete;

    TableLayout stk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mktreturns);
        setTitle("Mkt.Return Products");

        stk = (TableLayout) findViewById(R.id.table_main);

        //	gridview=(GridView)findViewById(R.id.gridView1);
        textTot = (EditText) findViewById(R.id.editText1);
        btnBack = (Button) findViewById(R.id.button1);
        btnBack.setFocusable(true);
        btnBack.setFocusableInTouchMode(true);///add this line
        btnBack.requestFocus();
        db = new DatabaseHandler(getApplicationContext());

        textAdd = (TextView) findViewById(R.id.textView3);
        textname = (TextView) findViewById(R.id.textView2);


        datat();

        textAdd.setText("Address : " + MyGloble.Radd.toString());
        textname.setText("Retailer: " + MyGloble.RName.toString());
        textTot.setText(String.valueOf(MyGloble.TotalMKTR));

        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub MyGloble.invedit
              //  startActivity(new Intent("com.example.mobile.INVPRINT"));
                Intent intent = new Intent(MktreturnsActivity.this, InvprintActivity.class);
                MktreturnsActivity.this.startActivity(intent);

            }
        });

    }

    // Disabled Back Button On 4n
    @Override
    public void onBackPressed() {
    }


    private void datat() {

        TableRow tbrow0 = new TableRow(this);
        TextView tv0 = new TextView(this);
        tv0.setText("Product                             ");
        tv0.setTextColor(Color.BLACK);
        //  tv0.setTextSize(12);
        tv0.setTypeface(null, Typeface.BOLD);
        //   tv0.setBackgroundColor(Color.BLACK);
        tv0.setGravity(Gravity.LEFT);
        // tv0.setWidth(200);
        tbrow0.addView(tv0);




        TextView tv1 = new TextView(this);
        tv1.setText(" Qty");
        tv1.setTextColor(Color.BLACK);
        //tv1.setTextSize(12);
        tv1.setTypeface(null, Typeface.BOLD);   // tv1.setTypeface(null, Typeface.BOLD|Typeface.ITALIC);
        // tv1.setBackgroundColor(Color.BLACK);
        tv1.setGravity(Gravity.RIGHT);
        //   tv1.setWidth(100);
        tbrow0.addView(tv1);

        TextView tv2 = new TextView(this);
        tv2.setText("     F_Qty");
        tv2.setTextColor(Color.BLACK);
        // tv2.setTextSize(12);
        tv2.setTypeface(null, Typeface.BOLD);
        //  tv2.setBackgroundColor(Color.BLACK);
        tv2.setGravity(Gravity.RIGHT);
        // tv2.setWidth(100);
        tbrow0.addView(tv2);

        TextView tv3 = new TextView(this);
        tv3.setText("    Dis.");
        tv3.setTextColor(Color.BLACK);
        // tv3.setTextSize(12);
        tv3.setTypeface(null, Typeface.BOLD);
        //  tv3.setBackgroundColor(Color.BLACK);
        tv3.setGravity(Gravity.RIGHT);
        // tv1.setWidth(115);
        tbrow0.addView(tv3);

        //   tbrow0.setBackgroundColor(Color.BLACK);
        stk.addView(tbrow0);


        Cursor cursor2=db.GetC("select PDAMarketReturn.ProductID,ProductName,PDAMarketReturn.Qty,PDAMarketReturn.FIQty, PDAMarketReturn.DisAmount,PDAMarketReturn.SubTot from PDAMarketReturn,PDAProduct WHERE PDAProduct.ProductID=PDAMarketReturn.ProductID AND  InvID='"+ MyGloble.InvoiceNo+"'   ");
        if( cursor2.getCount() > 0) {
            if (cursor2.moveToFirst()) {
                while (!cursor2.isAfterLast()) {
                    TableRow tbrow = new TableRow(this);
                    TextView t1v = new TextView(this);
                    t1v.setText(cursor2.getString(cursor2.getColumnIndex("ProductName")));
                    t1v.setTextColor(Color.BLACK);
                    t1v.setGravity(Gravity.LEFT);
                    tbrow.addView(t1v);
                    TextView t2v = new TextView(this);
                    t2v.setText(cursor2.getString(cursor2.getColumnIndex("Qty")));
                    t2v.setTextColor(Color.BLACK);
                    t2v.setGravity(Gravity.RIGHT);
                    tbrow.addView(t2v);
                    TextView t3v = new TextView(this);
                    t3v.setText(cursor2.getString(cursor2.getColumnIndex("FIQty")));
                    t3v.setTextColor(Color.BLACK);
                    t3v.setGravity(Gravity.RIGHT);
                    tbrow.addView(t3v);

                    TextView t4v = new TextView(this);
                    t4v.setText(cursor2.getString(cursor2.getColumnIndex("DisAmount")));
                    t4v.setTextColor(Color.BLACK);
                    t4v.setGravity(Gravity.RIGHT);
                    tbrow.addView(t4v);

                    tbrow.setBackgroundColor(Color.WHITE);
                    stk.addView(tbrow);
                    cursor2.moveToNext();
                }
            }
        }




    }
}

