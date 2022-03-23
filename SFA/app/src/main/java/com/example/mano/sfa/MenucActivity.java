package com.example.mano.sfa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MenucActivity extends AppCompatActivity {

    GridView androidGridView;

    String[] gridViewString = {
            "Invoice", "Stocks", "Retailer", "Setting", "Sync", "Target","Report","Exit",} ;

    int[] gridViewImageId = {
            R.drawable.invoice, R.drawable.stock, R.drawable.retailer, R.drawable.setting, R.drawable.sync, R.drawable.target, R.drawable.report5,R.drawable.exit,
    };

    static String KEY_ANIM = "TARGET_ANIM";
    static String Target_Translate = "Translate";
    static String Target_Alpha = "Alpha";
    static String Target_Scale = "Scale";
    static String Target_Rotate = "Rotate";
    String target_op = Target_Translate;

    static String ITEM = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menuc);


        final Animation animTranslate = AnimationUtils.loadAnimation(this, R.anim.anim_translate);
        final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);
        final Animation animScale = AnimationUtils.loadAnimation(this, R.anim.anim_scale);
        final Animation animRotate = AnimationUtils.loadAnimation(this, R.anim.anim_rotate);

        CustomGridViewActivity adapterViewAndroid = new CustomGridViewActivity(MenucActivity.this, gridViewString, gridViewImageId);
        androidGridView=(GridView)findViewById(R.id.grid_view_image_text);
        androidGridView.setAdapter(adapterViewAndroid);

        androidGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int i, long id) {
              //  Toast.makeText(MenucActivity.this, "GridView Item: " + gridViewString[+i], Toast.LENGTH_LONG).show();

                if (gridViewString[+i].toString().equals("Invoice"))
                {
                    ITEM = "Invoice";
                    target_op = Target_Rotate;
                    view.startAnimation(animRotate);

                }
                if (gridViewString[+i].toString().equals("Stocks"))
                {
                    Toast.makeText(MenucActivity.this, "Please Wait..." , Toast.LENGTH_LONG).show();
                    ITEM = "Stocks";
                    target_op = Target_Rotate;
                    view.startAnimation(animRotate);

                }
                if (gridViewString[+i].toString().equals("Exit"))
                {
                    ITEM = "Exit";
                    target_op = Target_Translate;
                    view.startAnimation(animTranslate);

                }
                if (gridViewString[+i].toString().equals("Setting"))
                {
//                    ITEM = "Setting";
//                    target_op = Target_Translate;
//                    view.startAnimation(animTranslate);

                }
                if (gridViewString[+i].toString().equals("Retailer"))
                {
                    ITEM = "Retailer";
                    target_op = Target_Rotate;
                    view.startAnimation(animRotate);
                }
                if (gridViewString[+i].toString().equals("Sync"))
                {
                    ITEM = "Sync";
                    target_op = Target_Scale;
                    view.startAnimation(animScale);
                }
                if (gridViewString[+i].toString().equals("Target"))
                {
                    Toast.makeText(MenucActivity.this, "Please Wait..." , Toast.LENGTH_LONG).show();
                    ITEM = "Target";
                    target_op = Target_Rotate;
                    view.startAnimation(animRotate);
                }
                if (gridViewString[+i].toString().equals("Report"))
                {
                    Toast.makeText(MenucActivity.this, "Please Wait..." , Toast.LENGTH_LONG).show();
                    ITEM = "Report";
                    target_op = Target_Rotate;
                    view.startAnimation(animRotate);
                }
            }

        });

        animTranslate.setAnimationListener(animationListener);
        animAlpha.setAnimationListener(animationListener);
        animScale.setAnimationListener(animationListener);
        animRotate.setAnimationListener(animationListener);


    }
    AnimationListener animationListener = new AnimationListener(){

        @Override
        public void onAnimationStart(Animation animation) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onAnimationEnd(Animation animation) {
//                    Intent intent = new Intent(
//                            MenucActivity.this,
//                            InvList.class);
//            intent.putExtra(KEY_ANIM, target_op);
//            startActivity(intent);
            if ( ITEM .equals("Invoice") )
            {
                Intent intent = new Intent(MenucActivity.this,InvList.class);
                MenucActivity.this.startActivity(intent);
            }
            if ( ITEM .equals("Retailer") )
            {
                Intent intent = new Intent(MenucActivity.this,RetailerViewActivity.class);
                MenucActivity.this.startActivity(intent);
            }
            if ( ITEM .equals("Exit") )
            {
                SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd h:mm a");
                String date = df.format(Calendar.getInstance().getTime());

                MyGloble.db.execSQL("DELETE From PDARep2 ");
                MyGloble.db.execSQL("Insert Into PDARep2 Values('"+ MyGloble.Repid+"','Application_Exit', '"+ date+"') " );

//                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
//                homeIntent.addCategory( Intent.CATEGORY_HOME );
//                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(homeIntent);

                Intent intent = new Intent(MenucActivity.this, LoginformActivity.class);
                MenucActivity.this.startActivity(intent);

            }
            if ( ITEM .equals("Stocks") )
            {
                Intent intent = new Intent(MenucActivity.this,StocksActivity.class);
                MenucActivity.this.startActivity(intent);
            }

            if ( ITEM .equals("Target") )
            {
                Intent intent = new Intent(MenucActivity.this,TargetActivity.class);
                MenucActivity.this.startActivity(intent);
            }
            if ( ITEM .equals("Report") )
            {
                Intent intent = new Intent(MenucActivity.this,ReportCateActivity.class);
                MenucActivity.this.startActivity(intent);
            }
            if ( ITEM .equals("Sync") )
            {
//                Intent intent = new Intent(MenucActivity.this,Synchoronization.class);
//                MenucActivity.this.startActivity(intent);
//
//                Intent intent = new Intent(MenucActivity.this,SyncListActivity.class);
//                MenucActivity.this.startActivity(intent);
            }




        }

        @Override
        public void onAnimationRepeat(Animation animation) {
            // TODO Auto-generated method stub

        }};
}
