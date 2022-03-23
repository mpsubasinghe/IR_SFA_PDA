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

public class RedemviewActivity extends AppCompatActivity {
    Integer Itempossition;

    DatabaseHandler db ;
    Button btnBack;
    Button btnRem;


    TextView textname;
    TextView textAdd;


    String itemdelete;

    TableLayout stk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redemview);
        setTitle("Redem. Products View");

        stk = (TableLayout) findViewById(R.id.table_main);

        //	gridview=(GridView)findViewById(R.id.gridView1);

        btnBack=(Button)findViewById(R.id.button1);
        btnRem=(Button)findViewById(R.id.button2);

        btnBack.setFocusable(true);
        btnBack.setFocusableInTouchMode(true);///add this line
        btnBack.requestFocus();


         db = new DatabaseHandler(getApplicationContext());

        textAdd=(TextView)findViewById(R.id.textView3);
        textname=(TextView)findViewById(R.id.textView2);


        datat() ;

        textAdd.setText("Address : " + MyGloble.Radd.toString());
        textname.setText("Retailer: " + MyGloble.RName.toString());


        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub MyGloble.invedit
                Intent intent = new Intent(RedemviewActivity .this, InvtypeActivity .class);
                RedemviewActivity.this.startActivity(intent);

            }
        });


        btnRem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub MyGloble.invedit
                DeleteItem(itemdelete);
                stk.removeAllViews();
                //stk.removeAllViewsInLayout();
                stk.invalidate();
                stk.refreshDrawableState();
                datat();
                itemdelete="";

            }
        });

    }

    // Disabled Back Button On 4n
    @Override
    public void onBackPressed() {
    }

    public void DeleteItem(String a)
    {
//        for (int q=0;q<MyGloble.DataTableREDEM.length;q++)
//        {
//            if (MyGloble.DataTableREDEM[q][8] == a)
//            {
//                MyGloble.DataTableREDEM[q][0]="";
//                MyGloble.DataTableREDEM[q][1]="";
//                MyGloble.DataTableREDEM[q][2]="";
//                MyGloble.DataTableREDEM[q][3]="";
//                MyGloble.DataTableREDEM[q][4]="";
//                MyGloble.DataTableREDEM[q][5]="";
//                MyGloble.DataTableREDEM[q][6]="";
//                MyGloble.DataTableREDEM[q][7]="";
//                MyGloble.DataTableREDEM[q][8]="";
//                MyGloble.DataTableREDEM[q][9]="";
//            }
//        }
        Cursor cursor5=db.GetC("select * from PDAInvRedemption WHERE RedProID = '"+a+"' AND   InvID='"+ MyGloble.InvoiceNo+"'  ");
        if( cursor5.getCount() > 0) {
            if (cursor5.moveToFirst()){
                while (!cursor5.isAfterLast()) {
                    MyGloble.db.execSQL("Update PDAProduct SET Quantity=Quantity + '" + cursor5.getString(cursor5.getColumnIndex("Qty")) + "' WHERE ProductID = '"+cursor5.getString(cursor5.getColumnIndex("ProID"))+"'" );
                    cursor5.moveToNext();
                }
            }
        }
        MyGloble.db.execSQL("DELETE from PDAInvRedemption WHERE RedProID= '"+a+"' AND   InvID='"+ MyGloble.InvoiceNo+"'" );

    }

    private void datat() {

        TableRow tbrow0 = new TableRow(this);
        TextView tv0 = new TextView(this);
        tv0.setText("Product                             ");
        tv0.setTextColor(Color.BLACK);
        tv0.setTypeface(null, Typeface.BOLD);
        tv0.setGravity(Gravity.LEFT);
        tbrow0.addView(tv0);

        TextView tv1 = new TextView(this);
        tv1.setText("         Free Qty");
        tv1.setTextColor(Color.BLACK);
        tv1.setTypeface(null, Typeface.BOLD);
        tv1.setGravity(Gravity.RIGHT);
        tbrow0.addView(tv1);

        TextView tv2 = new TextView(this);
        tv2.setText("     Rem.Qty");
        tv2.setTextColor(Color.BLACK);
        tv2.setTypeface(null, Typeface.BOLD);
        tv2.setGravity(Gravity.RIGHT);
        tbrow0.addView(tv2);

        TextView tv3 = new TextView(this);
        tv3.setText("P_ID");
        tv3.setTextColor(Color.BLACK);
        tv3.setGravity(Gravity.RIGHT);
        tv3.setTypeface(null, Typeface.BOLD);
        tbrow0.addView(tv3);
        tv3.setTextSize(0);

        stk.addView(tbrow0);


//        for (int i=0;i<MyGloble.DataTableREDEM.length;i++) {
//            if (!MyGloble.DataTableREDEM[i][0].equals(""))
//            {
        Cursor cursor2=db.GetC("select * from PDAInvRedemption WHERE InvID='"+ MyGloble.InvoiceNo+"'   ");
        if( cursor2.getCount() > 0) {
            if (cursor2.moveToFirst())
            {
                while (!cursor2.isAfterLast()) {
                    String fname= "";
                    DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                    Cursor cursor3=db.GetC("select * from PDAProduct WHERE ProductID='"+cursor2.getString(cursor2.getColumnIndex("ProID"))+"' ");
                    if( cursor3.getCount() > 0)
                    {
                        if (cursor3.moveToFirst()){
                            while(!cursor3.isAfterLast()){
                                fname=cursor3.getString(6)+ "-" + cursor2.getString(2);
                                cursor3.moveToNext();
                            }
                        }

                    }

                    TableRow tbrow = new TableRow(this);
                    TextView t1v = new TextView(this);
                    t1v.setText(fname);
                    t1v.setTextColor(Color.BLACK);
                    t1v.setGravity(Gravity.LEFT);
                    tbrow.addView(t1v);
                    TextView t2v = new TextView(this);
                    t2v.setText(cursor2.getString(cursor2.getColumnIndex("Qty")));
                    t2v.setTextColor(Color.BLACK);
                    t2v.setGravity(Gravity.RIGHT);
                    tbrow.addView(t2v);
                    TextView t3v = new TextView(this);
                    t3v.setText(cursor2.getString(cursor2.getColumnIndex("RedProQty")));
                    t3v.setTextColor(Color.BLACK);
                    t3v.setGravity(Gravity.RIGHT);
                    tbrow.addView(t3v);
                    TextView t4v = new TextView(this);
                    t4v.setText(cursor2.getString(cursor2.getColumnIndex("RedProID")));
                    t4v.setTextColor(Color.BLACK);
                    t4v.setGravity(Gravity.RIGHT);
                    tbrow.addView(t4v);
                    t4v.setTextSize(0);

                    tbrow.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {


                            TableRow tr1=(TableRow)view;
                            TextView tv3= (TextView)tr1.getChildAt(3);

                            itemdelete =(tv3.getText().toString());

                            view.setBackgroundColor(Color.MAGENTA);


                        }
                    });


                    stk.addView(tbrow);
                    cursor2.moveToNext();
                }
            }
        }





//            }
//
//        }
    }

}
