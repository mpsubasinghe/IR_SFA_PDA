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

import java.util.ArrayList;
import java.util.List;

public class StocksActivity extends AppCompatActivity {
    TableLayout stk;
    Button btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stocks);
        setTitle("Product List");


        stk = (TableLayout) findViewById(R.id.table_main);


        btnBack=(Button)findViewById(R.id.button1);
        init();

        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub MyGloble.invedit


                Intent intent = new Intent(StocksActivity .this, MenucActivity .class);
                StocksActivity.this.startActivity(intent);





            }
        });
    }

    // Disabled Back Button On 4n
    @Override
    public void onBackPressed() {
    }


    public void init() {

        TableRow tbrow0 = new TableRow(this);
        TextView tv0 = new TextView(this);
        tv0.setText("Product                             ");
        tv0.setTextColor(Color.WHITE);
        tv0.setGravity(Gravity.LEFT);
        tv0.setTypeface(null, Typeface.BOLD);
        //  tv0.setWidth(250);
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(this);
        tv1.setText("            Price");
        tv1.setTextColor(Color.WHITE);
        tv1.setGravity(Gravity.RIGHT);
        tv1.setTypeface(null, Typeface.BOLD);
        //    tv1.setWidth(150);
        tbrow0.addView(tv1);
        TextView tv2 = new TextView(this);
        tv2.setText("          Qty");
        tv2.setTextColor(Color.WHITE);
        tv2.setGravity(Gravity.RIGHT);
        tv2.setTypeface(null, Typeface.BOLD);
        //    tv2.setWidth(115);
        tbrow0.addView(tv2);


        tbrow0.setBackgroundColor(Color.BLACK);
        stk.addView(tbrow0);

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        Cursor C = db.GetC("select ProductName,Price,Quantity from PDAProduct Order By ProductName");
        List<String> labels = new ArrayList<String>();

        while(C.moveToNext())
        {
            TableRow tbrow = new TableRow(this);
            TextView t1v = new TextView(this);
            t1v.setText(C.getString(0));
            //   t1v.setTextColor(Color.WHITE);
            t1v.setGravity(Gravity.LEFT);
            tbrow.addView(t1v);
            TextView t2v = new TextView(this);
            t2v.setText( String.format("%.2f",Double.valueOf( C.getString(1)))  );
            // t2v.setTextColor(Color.WHITE);
            t2v.setGravity(Gravity.RIGHT);
            tbrow.addView(t2v);
            TextView t3v = new TextView(this);
            t3v.setText(C.getString(2));
            //  t3v.setTextColor(Color.WHITE);
            t3v.setGravity(Gravity.RIGHT);
            tbrow.addView(t3v);

            stk.addView(tbrow);
        }


    }

}
