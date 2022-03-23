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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.DecimalFormat;

public class ReportCateActivity extends AppCompatActivity {

    TableLayout stk;
    DatabaseHandler db;
    Button btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_cate);
        stk = (TableLayout) findViewById(R.id.table_main);
        btnBack = (Button) findViewById(R.id.button1);
        db = new DatabaseHandler(getApplicationContext());
        datat() ;
        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub MyGloble.invedit
                //  startActivity(new Intent("com.example.mobile.INVPRINT"));
                Intent intent = new Intent(ReportCateActivity.this, MenucActivity.class);
                ReportCateActivity.this.startActivity(intent);

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
        tv0.setText("  Categeory  ");
        tv0.setTextColor(Color.BLACK);
        //  tv0.setTextSize(12);
        tv0.setTypeface(null, Typeface.BOLD);
        //   tv0.setBackgroundColor(Color.BLACK);
        tv0.setGravity(Gravity.LEFT);
        // tv0.setWidth(200);
        tbrow0.addView(tv0);

        TextView tv1 = new TextView(this);
        tv1.setText("  Day Sales");
        tv1.setTextColor(Color.BLACK);
        //tv1.setTextSize(12);
        tv1.setTypeface(null, Typeface.BOLD);   //tv1.setTypeface(null, Typeface.BOLD|Typeface.ITALIC);
        //tv1.setBackgroundColor(Color.BLACK);
        tv1.setGravity(Gravity.RIGHT);
        //tv1.setWidth(100);
        tbrow0.addView(tv1);


        TextView tv2 = new TextView(this);
        tv2.setText("  BF Sales");
        tv2.setTextColor(Color.BLACK);
        //tv1.setTextSize(12);
        tv2.setTypeface(null, Typeface.BOLD);   //tv1.setTypeface(null, Typeface.BOLD|Typeface.ITALIC);
        //tv1.setBackgroundColor(Color.BLACK);
        tv2.setGravity(Gravity.RIGHT);
        //tv1.setWidth(100);
        tbrow0.addView(tv2);

        TextView tv3 = new TextView(this);
        tv3.setText("  Total Sales");
        tv3.setTextColor(Color.BLACK);
        //tv1.setTextSize(12);
        tv3.setTypeface(null, Typeface.BOLD);   //tv1.setTypeface(null, Typeface.BOLD|Typeface.ITALIC);
        //tv1.setBackgroundColor(Color.BLACK);
        tv3.setGravity(Gravity.RIGHT);
        //tv1.setWidth(100);
        tbrow0.addView(tv3);

        //   tbrow0.setBackgroundColor(Color.BLACK);
        stk.addView(tbrow0);

        Double Gtot=0.0;
        Cursor cursor2=db.GetC("select ProCatNo, Sum(BFSales*Price) as total from PDATarget,PDAProduct WHERE PDAProduct.ProductID=PDATarget.ProductID  Group By ProCatNo  ");
        if( cursor2.getCount() > 0) {
            if (cursor2.moveToFirst()) {
                while (!cursor2.isAfterLast()) {
                    TableRow tbrow = new TableRow(this);
                    TextView t1v = new TextView(this);
                    t1v.setText("  "+cursor2.getString(cursor2.getColumnIndex("ProCatNo")));
                    t1v.setTextColor(Color.BLACK);
                    t1v.setGravity(Gravity.LEFT);
                    tbrow.addView(t1v);
                    Double daysale=0.0;
                    Cursor cursor3=db.GetC("select ProCatNo, Sum(PDAInvoicedProduct.Quantity*Price) as total from PDAInvoicedProduct,PDAProduct WHERE PDAProduct.ProductID=PDAInvoicedProduct.ProductID AND  ProCatNo='"+ cursor2.getString(cursor2.getColumnIndex("ProCatNo")) +"'  Group By ProCatNo  ");
                    if( cursor3.getCount() > 0) {
                        if (cursor3.moveToFirst()) {
                            daysale=Double .valueOf(cursor3.getString(cursor3.getColumnIndex("total")));
                        }
                    }
                    Double S_daysale=0.0;
                    Cursor cursor4=db.GetC("select ProCatNo, Sum(PDAMarketReturn.Qty*PDAMarketReturn.Price) as total from PDAMarketReturn,PDAProduct WHERE PDAProduct.ProductID=PDAMarketReturn.ProductID AND  ProCatNo='"+ cursor2.getString(cursor2.getColumnIndex("ProCatNo")) +"'  Group By ProCatNo  ");
                    if( cursor4.getCount() > 0) {
                        if (cursor4.moveToFirst()) {
                            S_daysale=Double .valueOf(cursor4.getString(cursor4.getColumnIndex("total")));
                        }
                    }
                    TextView t2v = new TextView(this);
                    DecimalFormat decim = new DecimalFormat("#,###.00");
                    t2v.setText("  "+ decim.format(daysale-S_daysale));
                    t2v.setTextColor(Color.BLACK);
                    t2v.setGravity(Gravity.RIGHT);
                    tbrow.addView(t2v);

                    TextView t3v = new TextView(this);
                    t3v.setText("  "+ decim.format(Double .valueOf(cursor2.getString(cursor2.getColumnIndex("total")))));
                    t3v.setTextColor(Color.BLACK);
                    t3v.setGravity(Gravity.RIGHT);
                    tbrow.addView(t3v);

                    TextView t4v = new TextView(this);
                    t4v.setText("  "+ decim.format(daysale+Double .valueOf(cursor2.getString(cursor2.getColumnIndex("total")))));
                    t4v.setTextColor(Color.BLACK);
                    t4v.setGravity(Gravity.RIGHT);
                    tbrow.addView(t4v);

                    Gtot=Gtot+((daysale-S_daysale)+Double .valueOf(cursor2.getString(cursor2.getColumnIndex("total"))));

                    tbrow.setBackgroundColor(Color.WHITE);
                    stk.addView(tbrow);
                    cursor2.moveToNext();
                }
            }
        }
        TableRow Footer = new TableRow(this);

        TextView t1v = new TextView(this);
        t1v.setText(" Total(Rs.)");
        t1v.setTextColor(Color.BLACK);
        t1v.setGravity(Gravity.LEFT);
        t1v.setTypeface(null, Typeface.BOLD);
        Footer.addView(t1v);

        TextView t3v = new TextView(this);
        t3v.setText("  ");
        t3v.setTextColor(Color.BLACK);
        t3v.setGravity(Gravity.RIGHT);
        Footer.addView(t3v);

        TextView t4v1 = new TextView(this);
        t4v1.setText("  ");
        t4v1.setTextColor(Color.BLACK);
        t4v1.setGravity(Gravity.RIGHT);
        Footer.addView(t4v1);

        TextView t4v = new TextView(this);
        DecimalFormat decim = new DecimalFormat("#,###.00");
        t4v.setText("  "+ decim.format(Gtot));
        t4v.setTextColor(Color.BLACK);
        t4v.setGravity(Gravity.RIGHT);
        t4v.setTypeface(null, Typeface.BOLD);
        Footer.addView(t4v);
        Footer.setBackgroundColor(Color.GREEN);
        stk.addView(Footer);

    }
}
