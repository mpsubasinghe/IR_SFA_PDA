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

public class PrevinvoiceActivity extends AppCompatActivity {
    String item ;
    TableLayout stk;
    Button btn1;
    Button btn2;
    TextView name;
    TextView billtype;
    TextView mktretval;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previnvoice);


        setTitle("Previous Invoice");


        btn1= (Button)findViewById(R.id.button1);
        btn2= (Button)findViewById(R.id.button2);
        btn2.setFocusable(true);
        btn2.setFocusableInTouchMode(true);///add this line
        btn2.requestFocus();
        name=( TextView)findViewById(R.id.textView3);
        billtype=( TextView)findViewById(R.id.textView5);
        mktretval=( TextView)findViewById(R.id.textView7);

        stk = (TableLayout) findViewById(R.id.table_main);

        name.setText(MyGloble.RName);

        loadretailer();

        btn1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Intent intent = new Intent(PrevinvoiceActivity .this, RetailerActivity .class);
                PrevinvoiceActivity.this.startActivity(intent);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Intent intent = new Intent(PrevinvoiceActivity .this, InvtypeActivity .class);
                PrevinvoiceActivity.this.startActivity(intent);
            }
        });

    }


    // Disabled Back Button On 4n
    @Override
    public void onBackPressed() {
    }


    public void loadretailer()
    {
        TableRow tbrow0 = new TableRow(this);
        TextView tv0 = new TextView(this);
        tv0.setText("Product Name                                            ");
        tv0.setTextColor(Color.WHITE);
        tv0.setGravity(Gravity.LEFT);
        tv0.setTypeface(null, Typeface.BOLD);
        //    tv0.setWidth(300);


        tbrow0.addView(tv0);
        TextView tv1 = new TextView(this);
        tv1.setText("Price               ");
        tv1.setTextColor(Color.WHITE);
        tv1.setGravity(Gravity.LEFT);
        tv1.setTypeface(null, Typeface.BOLD);
        //   tv1.setWidth(150);
        tbrow0.addView(tv1);
        TextView tv2 = new TextView(this);
        tv2.setText("Qty");
        tv2.setTextColor(Color.WHITE);
        tv2.setGravity(Gravity.LEFT);
        tv2.setTypeface(null, Typeface.BOLD);
        //   tv2.setWidth(65);

        tbrow0.addView(tv2);

        tbrow0.setBackgroundColor(Color.BLACK);
        stk.addView(tbrow0);

        //  Toast.makeText(getApplicationContext(),MyGloble.RID,Toast.LENGTH_LONG).show();

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        Cursor cursor=db.GetC("select ProductID,PDAPreviousInvoice.Price,PDAPreviousInvoice.Quantity from PDAPreviousInvoice WHERE PDAPreviousInvoice.RetID ="+MyGloble.RID+"  ");
        //Toast.makeText(getApplicationContext(),String.valueOf( cursor.getCount()),Toast.LENGTH_LONG).show();
        if( cursor.getCount() > 0)
        {
            if (cursor.moveToFirst()){
                while(!cursor.isAfterLast()){

                    // labels.add(cursor.getString(cursor.getColumnIndex("RName")));

                    TableRow tbrow = new TableRow(this);
                    TextView t1v = new TextView(this);
                    t1v.setText(cursor.getString(0));
                    //   t1v.setTextColor(Color.WHITE);
                    t1v.setGravity(Gravity.LEFT);
                    tbrow.addView(t1v);
                    TextView t2v = new TextView(this);
                    t2v.setText(cursor.getString(1));
                    // t2v.setTextColor(Color.WHITE);
                    t2v.setGravity(Gravity.LEFT);
                    tbrow.addView(t2v);
                    TextView t3v = new TextView(this);
                    t3v.setText(cursor.getString(2));

                    //  t3v.setTextColor(Color.WHITE);
                    t3v.setGravity(Gravity.LEFT);
                    tbrow.addView(t3v);





                    stk.addView(tbrow);

                    cursor.moveToNext();
                }
            }

        }

        String mktret="";
        String type="";

        Cursor cursor1=db.GetC("select * from PDAPreRetDetail WHERE RetIDIndex ="+MyGloble.RID+"  ");
        //Toast.makeText(getApplicationContext(),String.valueOf( cursor1.getCount()),Toast.LENGTH_LONG).show();
        if( cursor1.getCount() > 0)
        {
            if (cursor1.moveToFirst()){
                while(!cursor1.isAfterLast()){
                    if(cursor1.getString(cursor1.getColumnIndex("TCBill")).equals("1"))
                    {
                        type=type+"TC Bill/ ";
                    }
                    if(cursor1.getString(cursor1.getColumnIndex("Cash")).equals("1"))
                    {
                        type=type+"Cash/ ";
                    }
                    if(cursor1.getString(cursor1.getColumnIndex("Cheque")).equals("1"))
                    {
                        type=type+"Cheque ";
                    }

                    mktret= cursor1.getString(cursor1.getColumnIndex("MktRtn"));
                    cursor1.moveToNext();
                }
            }
        }


        billtype.setText(": " +type);
        mktretval.setText(": " + mktret);


    }


}

