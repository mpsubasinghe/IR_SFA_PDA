package com.example.mano.sfa;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static java.lang.Thread.sleep;

public class LoginformActivity extends AppCompatActivity {
    private ProgressDialog progress;
    int progreeval=0;
    Button btn1 ;
    Button btn2 ;
    Button sync ;
    EditText ps;
    EditText uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginform);
      //  getSupportActionBar().hide();
        setTitle("SFA Login");

        btn1= (Button)findViewById(R.id.button3);
        btn2= (Button)findViewById(R.id.button2);
        sync= (Button)findViewById(R.id.button4);
        uid=(EditText)findViewById(R.id.editText1);
        ps=(EditText)findViewById(R.id.editText2);




     //   download();

        btn1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub MyGloble.invedit

//                Intent intent = new Intent(LoginformActivity.this, MenucActivity.class);
//                LoginformActivity.this.startActivity(intent);

                CheckLogin();





              //  download();




            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub MyGloble.invedit

             //   Toast.makeText(getApplicationContext(),"ddddddddddddddd",Toast.LENGTH_LONG).show();
                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                homeIntent.addCategory( Intent.CATEGORY_HOME );
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);



            }
        });
        sync.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub MyGloble.invedit

//                Intent intent = new Intent(LoginformActivity.this,Synchoronization.class);
//                LoginformActivity.this.startActivity(intent);
                Intent intent = new Intent(LoginformActivity.this,SyncListActivity.class);
                LoginformActivity.this.startActivity(intent);



            }
        });
    }

    private void CheckLogin()
    {
        String userID="";
        String Password="";


        if(GetEvenStatus().equals("Close"))
        {
            DialogBoxOneButton( "Sorry You Can't Login To The System, Becasuse Already Done Evening Process In Device..!!!" ,"SFA Login");
            return;
        }

//					SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd h:mm a");
//	   				String date = df.format(Calendar.getInstance().getTime());
        //     MyGloble.db.execSQL("DELETE From PDARep2 " );
        //	MyGloble.db.execSQL("Insert Into PDARep2 Values('0000','Application_Login', '"+ date+"') " );


        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

//					Cursor cursor1=db.GetC("select * from PDARep2 Where Login='Evening' ");
//					//	Toast.makeText(getApplicationContext(),String.valueOf( cursor.getCount()),Toast.LENGTH_LONG).show();
//						if( cursor1.getCount() > 0)
//						{
//							DialogBoxOneButton( "Sorry You Can't Login To The System, Becasuse Already Done Evening Process In Device..!!!" ,"SFA Login");
//							return;
//						}


        Cursor cursor=db.GetC("select * from PDARep Where LoginName<>'' ");
        //	Toast.makeText(getApplicationContext(),String.valueOf( cursor.getCount()),Toast.LENGTH_LONG).show();
        if( cursor.getCount() > 0)
        {
            if (cursor.moveToFirst()){
                while(!cursor.isAfterLast()){

                    userID=  cursor.getString(cursor.getColumnIndex("LoginName"));
                    Password=  cursor.getString(cursor.getColumnIndex("Password"));
                    MyGloble.RepTp=cursor.getString(cursor.getColumnIndex("Password"));
                    cursor.moveToNext();
                }
            }
        }

        if ((uid.getText().toString().trim().equals(userID)) && (ps.getText().toString().trim().equals(Password)))
        {
            //startActivity(new Intent("com.example.calllayers.MENUC"));
            uid.setText("");
            ps.setText("");


            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd h:mm a");
            String date = df.format(Calendar.getInstance().getTime());

            MyGloble.db.execSQL("insert into PDARep (Login2,TimesStamp) values ('YES','"+date+"')");

            //startActivity(new Intent("com.example.mano.InvlistActivity"));
            //startActivity(new Intent("com.example.mobile.MENUC"));

//            Intent intent = new Intent(LoginformActivity.this, InvList.class);
//            LoginformActivity.this.startActivity(intent);

            Intent intent = new Intent(LoginformActivity.this, MenucActivity.class);
            LoginformActivity.this.startActivity(intent);



        }
        else
        {
            DialogBoxOneButton( "Please Check You Password & Try Again..?" ,"SFA Login");

        }



    }
    private void DialogBoxOneButton( String m,String t)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(LoginformActivity.this).create();

        // Setting Dialog Title
        alertDialog.setTitle(t);

        // Setting Dialog Message
        alertDialog.setMessage(m);

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
    public String GetEvenStatus()
    {

        File sdcard = Environment.getExternalStorageDirectory();
        File file = new File(sdcard,"/Android/Android.Mobile.System/Status.txt");

        //Read text from file
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                // text.append('\n');
            }
            br.close();
        }
        catch (IOException e) {
            //You'll need to add proper error handling here
        }
        String st=text.toString();
        //Toast.makeText(getApplicationContext(),st,Toast.LENGTH_LONG).show();
        return st;
    }
    public void download(){
        progress=new ProgressDialog(this);
        progress.setMessage("Downloading Music");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
       // progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.setMax(100);
        progress.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progreeval < 101) {
                    try {
                     sleep(10);
                        progreeval++;
                        progress.setProgress(progreeval);



                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
               // Toast.makeText(getApplicationContext(),String.valueOf( progreeval),Toast.LENGTH_LONG).show();
                if (progreeval==100)
                {
                   // CheckLogin();
                }
                progress.dismiss();

            }

        }).start();


   }

}
