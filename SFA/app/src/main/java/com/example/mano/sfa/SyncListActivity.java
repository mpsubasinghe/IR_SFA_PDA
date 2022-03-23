package com.example.mano.sfa;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SyncListActivity extends ListActivity {

    ArrayList<HashMap<String, String>> productList;
    public static final String REST_SERVICE_URL = "http://ebcreasy-001-site42.btempurl.com/api/";
    public static final String PRODUCT = REST_SERVICE_URL + "sync";

    String REPID="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_list);

        initializeComponents();

        //   RepID=GetString();

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        Cursor cursor2=db.GetC("select * from PDARep  Where LoginName<>''   ");
        if( cursor2.getCount() > 0)
        {
            if (cursor2.moveToFirst()){
                while(!cursor2.isAfterLast()){

                    REPID=cursor2.getString(0);
//                    STKID=cursor2.getString(14);
//                    LastInvNo=Integer.valueOf(cursor2.getString(15).substring(2) );
                    cursor2.moveToNext();
                }
            }

        }

        productList = new ArrayList<HashMap<String, String>>();
        new SyncListActivity.LoadAllProducts().execute();
    }

    private void initializeComponents() {
        ListView listView = getListView();
        listView.setOnItemClickListener(listViewItemClickListener);
    }

    private AdapterView.OnItemClickListener listViewItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String repid = (((TextView) view.findViewById(R.id.textViewId)).getText().toString());
            String DBName = (((TextView) view.findViewById(R.id.textViewName)).getText().toString());
            Toast.makeText(getBaseContext(), (repid), Toast.LENGTH_LONG).show();

            Intent intent = new Intent(getApplicationContext(), Synchoronization.class);
            intent.putExtra("RepID", repid);
            intent.putExtra("DBName", DBName);
            startActivityForResult(intent,1);
        }

    };


    class LoadAllProducts extends AsyncTask<String, String, String> {
        private ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            JSONHttpClient jsonHttpClient = new JSONHttpClient();
            WebSync[] products = jsonHttpClient.Get(PRODUCT, nameValuePairs, WebSync[].class);
            if (products.length > 0) {
                String cate="";
                for (WebSync product : products) {
                    if(String.valueOf(product.getRepID()).equals(REPID))
                    {
                        cate=product.getDID();
                    }
                }
                for (WebSync product : products) {
                    if(cate.equals(product.getDID()))
                    {
                        HashMap<String, String> mapProduct = new HashMap<String, String>();
                        mapProduct.put("RepID", String.valueOf(product.getRepID()));
                        mapProduct.put("Distributor", product.getDistributor());
                        productList.add(mapProduct);
                    }
                }

            } else {
//                Intent intent = new Intent(getApplicationContext(), NewProductActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();    //To change body of overridden methods use File | Settings | File Templates.
            progressDialog = new ProgressDialog(SyncListActivity.this);
            progressDialog.setMessage("Loading Data... Please wait...");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            if(progressDialog != null)
                progressDialog.dismiss();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ListAdapter adapter = new SimpleAdapter(SyncListActivity.this, productList, R.layout.list_item_user, new String[]{"RepID", "Distributor"}, new int[]{R.id.textViewId, R.id.textViewName});
                    setListAdapter(adapter);
                }
            });
        }
    }


}

