package com.example.mano.sfa;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MANO on 23/02/2017.
 */

public class MyGloble  extends Application {

    public static String myVs;
    public static List<String> list = new ArrayList<String>();

    public static String[][] DataTable1 = new String[50][11] ;

    public static String[][] DataTableMKTR = new String[50][11] ;

    public static String[][] DataTableREDEM = new String[50][10] ;

//    public static String[][] DiscountSpecial = new String[50][6] ;

    public static Integer Dtcount=0;
    public static Integer DtcountMKTR=0;
    public static Integer DtcountREDEM=0;


    public static String myPath ;
    public static byte[] Ekey;
    public static SQLiteDatabase db  ;


    public static Double Total=0.0;
    public static Double TotalMKTR=0.0;


    public static String RET_Name;
    public static String RID;
    public static String Repid;
    public static String Repname;
    public static String RepTp;
    public static String RName;
    public static String Radd;
    public static String [] invtype =new String[6];
    public static String Status;
    public static String invedit;
    public static String BillCopy;
    public static String Vat;

    public static String TCBill="";
    public static String CASH="";
    public static String CHEQUE="";

    public static String PRINTERTYPE;

    public static String InvoiceNo;
    public static String Display_InvoiceNo;
    //	public static Integer invno;
    //public static Integer invnoView;
    public static Integer Chqdate=0;

//    public Double GetC(String sql) {
//        //SQLiteDatabase db = this.getWritableDatabase();
//        Cursor res = MyGloble.db.rawQuery(sql,null);
//        return res;
//    }


}