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

public class RetailerViewActivity extends AppCompatActivity {
    TableLayout stk;
    String item;
    Button btn1;
    Button btn2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_view);
        setTitle("Retaler List");


        btn1= (Button)findViewById(R.id.button1);
        btn2= (Button)findViewById(R.id.button2);
        btn2.setFocusable(true);
        btn2.setFocusableInTouchMode(true);///add this line
        btn2.requestFocus();
        stk = (TableLayout) findViewById(R.id.table_main);

//		   if(MyGloble.db.isOpen())
//		    {
//		    	Toast.makeText(getApplicationContext(),String.valueOf( "Open"),Toast.LENGTH_LONG).show();
//		    }else
//		    {
//
//		    	  Toast.makeText(getApplicationContext(),String.valueOf( "close"),Toast.LENGTH_LONG).show();
//		    	MyGloble.myPath =Environment.getExternalStorageDirectory()+"/Android/data/Android.Mobile.System/data/SFAD.sqlite";
//		    	MyGloble. db  = SQLiteDatabase.openDatabase(MyGloble.myPath , null, SQLiteDatabase.OPEN_READWRITE);
//		    }
//


        loadretailer();








        btn1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Intent intent = new Intent(RetailerViewActivity.this, MenucActivity.class);
                RetailerViewActivity.this.startActivity(intent);

            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Intent intent = new Intent(RetailerViewActivity.this, RetailerAddActivity.class);
                RetailerViewActivity.this.startActivity(intent);

            }
        });
    }




//	@Override
//	public void onAttachedToWindow() {
//	    this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);
//	    super.onAttachedToWindow();
//	}
//

    // Disabled Back Button On 4n
    @Override
    public void onBackPressed() {
    }



//
//		@Override
//		protected void onStop() {
//		    super.onStop();
//		    Toast.makeText(getApplicationContext(),String.valueOf( "AAAAAAAAAAAAAA"),Toast.LENGTH_LONG).show();
//		    MyGloble.db.close();
//
//		    finish();
//			MyGloble.myPath =Environment.getExternalStorageDirectory()+"/Android/data/Android.Mobile.System/data/SFAD.sqlite";
//	    	MyGloble. db  = SQLiteDatabase.openDatabase(MyGloble.myPath , null, SQLiteDatabase.OPEN_READWRITE);
//		}
//



    public void loadretailer()
    {
        TableRow tbrow0 = new TableRow(this);
        TextView tv0 = new TextView(this);
        tv0.setText(" ");
        tv0.setTextColor(Color.WHITE);
        tv0.setGravity(Gravity.LEFT);
        //  tv0.setTextSize(0);
        tv0.setTypeface(null, Typeface.BOLD);
        //      tv0.setWidth(0);
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(this);
        tv1.setText("Retailer Name                        ");
        tv1.setTextColor(Color.WHITE);
        tv1.setGravity(Gravity.LEFT);
        tv1.setTypeface(null, Typeface.BOLD);
        //      tv1.setWidth(200);
        tbrow0.addView(tv1);
        TextView tv2 = new TextView(this);
        tv2.setText("Address");
        tv2.setTextColor(Color.WHITE);
        tv2.setGravity(Gravity.LEFT);
        tv2.setTypeface(null, Typeface.BOLD);
        //   tv2.setWidth(270);
        tbrow0.addView(tv2);

        tbrow0.setBackgroundColor(Color.BLACK);
        stk.addView(tbrow0);


        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        //Toast.makeText(getApplicationContext(),String.valueOf( "AAAAAAAAAAAAAA"),Toast.LENGTH_LONG).show();
        Cursor cursor=db.GetC("select * from PDARetailer ORDER BY Ret_Name ASC ");
        //Toast.makeText(getApplicationContext(),String.valueOf( cursor.getCount()),Toast.LENGTH_LONG).show();
        if( cursor.getCount() > 0)
        {
            if (cursor.moveToFirst()){
                while(!cursor.isAfterLast()){

                    // labels.add(cursor.getString(cursor.getColumnIndex("RName")));

                    TableRow tbrow = new TableRow(this);
                    TextView t1v = new TextView(this);
                    t1v.setText(cursor.getString(6));
                    //  t1v.setTextColor(Color.WHITE);
                    t1v.setGravity(Gravity.LEFT);
                    t1v.setTextSize(0);
                    tbrow.addView(t1v);
                    TextView t2v = new TextView(this);
                    t2v.setText(cursor.getString(0));
                    //  t2v.setTextColor(Color.WHITE);
                    t2v.setGravity(Gravity.LEFT);
                    tbrow.addView(t2v);
                    TextView t3v = new TextView(this);
                    // t3v.setText(cursor.getString(cursor.getColumnIndex("Ret_Address")));
                    if(cursor.getString(1).length()>20)
                    {
                        t3v.setText(cursor.getString(1).substring(0, 20));
                    }else{
                        t3v.setText(cursor.getString(1).substring(0, cursor.getString(1).length()));
                    }
                    //   t3v.setTextColor(Color.WHITE);
                    t3v.setGravity(Gravity.LEFT);
                    tbrow.addView(t3v);





                    stk.addView(tbrow);

                    cursor.moveToNext();
                }
            }

        }

    }
}