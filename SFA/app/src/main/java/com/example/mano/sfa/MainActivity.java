package com.example.mano.sfa;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.File;

import static com.example.mano.sfa.MyGloble.myPath;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MyGloble.PRINTERTYPE="WOOSIM";

      //  myPath = "/storage/0D0C-3C06/Android/data/Database/SFAD.sqlite";

     //   myPath = "/storage/1106-4307/LOST.DIR/Database/SFAD.sqlite";

        Toast.makeText(getApplicationContext(),String.valueOf( "WWWWWggWWfygyghgyyWWhghWhjunhcdfWWWWWWWWWWWW"),Toast.LENGTH_LONG).show();


       // MyGloble. myPath = "/data/data/com.android.music/Database/SFAD.sqlite";
     MyGloble.myPath = Environment.getExternalStorageDirectory()+"/Android/Android.Mobile.System/SFAD.sqlite";

        //MyGloble.myPath =Environment.getExternalStorageDirectory()+ "/SFAD.sqlite";

        File file = new File(myPath);
        if (file.exists() && !file.isDirectory()) {
          // file.setWritable(true);
        //    Toast.makeText(getApplicationContext(),String.valueOf( "DDDDDDDDDDDDDD"),Toast.LENGTH_LONG).show();
        }
//
//if (checkDataBase())
//        {
//            Toast.makeText(getApplicationContext(),String.valueOf( "WWWWWWWWWWWWWWWWWWWWWW"),Toast.LENGTH_LONG).show();
//        }

      MyGloble.db  = SQLiteDatabase.openDatabase(myPath , null, SQLiteDatabase.OPEN_READWRITE);
////
        Intent intent = new Intent(MainActivity.this, LoginformActivity.class);
        MainActivity.this.startActivity(intent);

//        Intent intent = new Intent(MainActivity.this,InvList.class);
//        MainActivity.this.startActivity(intent);
//
//        Intent intent = new Intent(MainActivity.this,MenucActivity.class);
//        MainActivity.this.startActivity(intent);

//        Intent intent = new Intent(MainActivity.this,specialdis.class);
//        MainActivity.this.startActivity(intent);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action  ", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {

            String myPath ="/storage/0D0C-3C06/Android/data/Database/SFAD.sqlite";

            File file = new File(myPath);
            if (file.exists() && !file.isDirectory())
              checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);

            Toast.makeText(getApplicationContext(),String.valueOf( "WWWWWWWWWWWWWWWWWWWWWW"),Toast.LENGTH_LONG).show();
        } catch (SQLiteException e) {
            Toast.makeText(getApplicationContext(),String.valueOf( e.toString()),Toast.LENGTH_LONG).show();
        }

        if (checkDB != null) {
            checkDB.close();
        }

        return checkDB != null ? true : false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
