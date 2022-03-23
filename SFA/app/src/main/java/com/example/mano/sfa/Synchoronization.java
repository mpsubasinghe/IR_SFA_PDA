package com.example.mano.sfa;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import static com.example.mano.sfa.MyGloble.myPath;


public class Synchoronization extends  ListActivity {
 //   AppCompatActivity,
    private ProgressDialog pDialog;
    Button btnShowProgress;
    Button btnUpload;
    // Progress dialog type (0 - for Horizontal progress bar)
    public static final int progress_bar_type = 0;
    private static final int BUFFER = 100000;
    private static String RepID = "";


    private ProgressBar progressBar;
    private String filePath = null;
    private VideoView vidPreview;
    long totalSize = 0;
    private TextView textRepid;
    private TextView textDBname;
    private TextView txtPercentage;


    ArrayList<HashMap<String, String>> productList;
    public static final String REST_SERVICE_URL = "http://ebcreasy-001-site42.btempurl.com/api/";
    public static final String PRODUCT = REST_SERVICE_URL + "sync";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synchoronization);

        initializeComponents();

        Intent intent = getIntent();
        RepID = intent.getStringExtra("RepID");
      String  DBName = intent.getStringExtra("DBName");


        textRepid=(TextView)findViewById(R.id.textView12);
        textDBname=(TextView)findViewById(R.id.textView13);
        btnShowProgress = (Button) findViewById(R.id.button);
        btnUpload  = (Button) findViewById(R.id.button8);
        txtPercentage = (TextView) findViewById(R.id.textView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
     //   RepID=GetString();

        textRepid .setText("RepID : "+RepID);
        textDBname .setText("Distributor : "+DBName);

        productList = new ArrayList<HashMap<String, String>>();
        //============================== new LoadAllProducts().execute();




        btnShowProgress.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // starting new Async Task

                new DownloadFileFromURL().execute("http://ebcreasy-001-site15.htempurl.com/Database/uploads/M_"+RepID+".zip");
              //  new DownloadFileFromURL().execute("http://ebcreasy-001-site15.htempurl.com/Database/uploads/U_SFAD_1234.zip");

//				try {
//					//unzipFAST(Environment.getExternalStorageDirectory()+"/DCIM/M_SFAD"+"_"+RepID+".zip",Environment.getExternalStorageDirectory()+"/DCIM/");
//                    MyGloble.db.close();
//                    unzipFAST(Environment.getExternalStorageDirectory()+"/Android/data/Android.Mobile.System/data/M_"+RepID+".zip",Environment.getExternalStorageDirectory()+"/Android/data/Android.Mobile.System/data/");
//                    MyGloble.db  = SQLiteDatabase.openDatabase(myPath , null, SQLiteDatabase.OPEN_READWRITE);
//
//                    Toast.makeText(getBaseContext(), "Morning Process Successfuly Comleted..!!", Toast.LENGTH_LONG).show();
//
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // starting new Async Task
              //  RepID=GetString();

                filePath = Environment.getExternalStorageDirectory()+"/Android/Android.Mobile.System/E"+"_"+RepID+".zip";
                String[] s = new String[1];
                //String inputPath=Environment.getExternalStorageDirectory()+"/DCIM/";
                String inputPath=	Environment.getExternalStorageDirectory()+"/Android/Android.Mobile.System/";
                // Type the path of the files in here
                s[0] = inputPath + "SFAD.sqlite";
                zip(s, filePath);


                filePath =  Environment.getExternalStorageDirectory()+"/Android/Android.Mobile.System/E_"+RepID+".zip";
                new UploadFileToServer().execute();
                //  new DownloadFileFromURL().execute("http://ebcreasy-001-site15.htempurl.com/Database/uploads/U_SFAD_1234.zip");

//				try {
//					//unzipFAST(Environment.getExternalStorageDirectory()+"/DCIM/M_SFAD"+"_"+RepID+".zip",Environment.getExternalStorageDirectory()+"/DCIM/");
//                    MyGloble.db.close();
//                    unzipFAST(Environment.getExternalStorageDirectory()+"/Android/data/Android.Mobile.System/data/M_"+RepID+".zip",Environment.getExternalStorageDirectory()+"/Android/data/Android.Mobile.System/data/");
//                    MyGloble.db  = SQLiteDatabase.openDatabase(myPath , null, SQLiteDatabase.OPEN_READWRITE);
//
//                    Toast.makeText(getBaseContext(), "Morning Process Successfuly Comleted..!!", Toast.LENGTH_LONG).show();
//
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
            }
        });
    }
    private void initializeComponents() {
        ListView listView = getListView();
        listView.setOnItemClickListener(listViewItemClickListener);
    }
    private AdapterView.OnItemClickListener listViewItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            int productId = Integer.parseInt(((TextView) view.findViewById(R.id.textViewId)).getText().toString());

            Toast.makeText(getBaseContext(),String.valueOf(productId) , Toast.LENGTH_LONG).show();
//            Intent intent = new Intent(getApplicationContext(), EditProductActivity.class);
//            intent.putExtra(Product.PRODUCT_ID, productId);
//            startActivityForResult(intent, RequestCode.PRODUCT_DETAILS);
        }
    };




    public  void unzipFAST(String zipFile, String location) throws IOException {
        int size;
        byte[] buffer = new byte[BUFFER];

        try {
            if ( !location.endsWith("/") ) {
                location += "/";
            }
            File f = new File(location);
            if(!f.isDirectory()) {
                f.mkdirs();
            }
            ZipInputStream zin = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFile), BUFFER));
            try {
                ZipEntry ze = null;
                while ((ze = zin.getNextEntry()) != null) {
                    String path = location + ze.getName();
                    File unzipFile = new File(path);

                    if (ze.isDirectory()) {
                        if(!unzipFile.isDirectory()) {
                            unzipFile.mkdirs();
                        }
                    } else {
                        // check for and create parent directories if they don't exist
                        File parentDir = unzipFile.getParentFile();
                        if ( null != parentDir ) {
                            if ( !parentDir.isDirectory() ) {
                                parentDir.mkdirs();
                            }
                        }

                        // unzip the file
                        FileOutputStream out = new FileOutputStream(unzipFile, false);
                        BufferedOutputStream fout = new BufferedOutputStream(out, BUFFER);
                        try {
                            while ( (size = zin.read(buffer, 0, BUFFER)) != -1 ) {
                                fout.write(buffer, 0, size);
                            }

                            zin.closeEntry();
                        }
                        finally {
                            fout.flush();
                            fout.close();
                        }
                    }
                }
            }
            finally {
                zin.close();
            }

        }
        catch (Exception e) {
            System.out.println(e);
        }

    }
    public  String GetString()
    {//*Don't* hardcode "/sdcard"

        String transferState="";

        File sdcard = Environment.getExternalStorageDirectory();

        //Get the text file
        //File file = new File(sdcard,"/data/data/com.example.mobile/database/RepInfo.txt");
        File file = new File(sdcard,"/Android/Android.Mobile.System/RepInfo.txt");
        //Read text from file
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            int ac=0;
            while ((line = br.readLine()) != null) {
                // text.append(line);
                //   text.append('\n');
                ac=ac+1;
//                if( ac==2)
//                {
//                    transferState=line;
//                }
		    	if( ac==1)
		    	{
                transferState=line;
		    	}
//		    	if( ac==3)
//		    	{
//		    		 ExportTp=line;
//		    		 ImportTp=line;
//		    	}
                //	Toast.makeText(getBaseContext(), line, Toast.LENGTH_LONG).show();
            }
            br.close();
        }
        catch (IOException e) {
            //You'll need to add proper error handling here
            Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
            Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
            Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
            Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
        return transferState ;

    }

    /**
     * Showing Dialog
     * */
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type:
                pDialog = new ProgressDialog(this);
                pDialog.setMessage("Morning Synchonization. Please wait...");
                pDialog.setIndeterminate(false);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setCancelable(true);
                pDialog.show();
                return pDialog;
            default:
                return null;
        }
    }

    /**
     * Background Async Task to download file
     * */
    class DownloadFileFromURL extends AsyncTask<String, Integer, String> {

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         * */
        @Override
        protected void onPreExecute() {
//            super.onPreExecute();
//            showDialog(progress_bar_type);

            progressBar.setProgress(0);
            super.onPreExecute();
        }

        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
	            URL url = new URL(f_url[0]);
	            URLConnection conection = url.openConnection();
	            conection.connect();
	            // getting file length
	            int lenghtOfFile = conection.getContentLength();

	            // input stream to read file - with 8k buffer
	            InputStream input = new BufferedInputStream(url.openStream(), 8192);

	            // Output stream to write file
	          //  OutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory()+"/DCIM/M_SFAD"+"_"+RepID+".zip");
	           OutputStream output = new FileOutputStream( Environment.getExternalStorageDirectory()+"/Android/Android.Mobile.System/M_"+RepID+".zip");
	          // OutputStream output = new FileOutputStream( Environment.getExternalStorageDirectory()+"/Android/data/Android.Mobile.System/data/Mobile.zip");

	            byte data[] = new byte[1024];

	            long total = 0;

	            while ((count = input.read(data)) != -1) {
	                total += count;
	                // publishing the progress....
	                // After this onProgressUpdate will be called
	                publishProgress((int)((total*100)/lenghtOfFile));

	                // writing data to file
	                output.write(data, 0, count);
	            }

	            // flushing output
	            output.flush();

	            // closing streams
	            output.close();
	            input.close();



                try {
                    //unzipFAST(Environment.getExternalStorageDirectory()+"/DCIM/M_SFAD"+"_"+RepID+".zip",Environment.getExternalStorageDirectory()+"/DCIM/");
                    MyGloble.db.close();
                    unzipFAST(Environment.getExternalStorageDirectory()+"/Android/Android.Mobile.System/M_"+RepID+".zip",Environment.getExternalStorageDirectory()+"/Android/Android.Mobile.System/");
                    MyGloble.db  = SQLiteDatabase.openDatabase(myPath , null, SQLiteDatabase.OPEN_READWRITE);



                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

//	            try {
//                unzipFAST(Environment.getExternalStorageDirectory()+"/DCIM/M_SFAD"+"_"+RepID+".zip",Environment.getExternalStorageDirectory()+"/DCIM/");
//					unzipFAST(Environment.getExternalStorageDirectory()+"/Android/data/Android.Mobile.System/data/Mobile.zip",Environment.getExternalStorageDirectory()+"/Android/data/Android.Mobile.System/data/");
//					Toast.makeText(getApplicationContext(),"Download Process is Successfully completed...!!!", Toast.LENGTH_LONG).show();
//
////					Intent intent = new Intent(Intent.ACTION_VIEW);
////			        intent.setDataAndType(Uri.fromFile(new    File(Environment.getExternalStorageDirectory()+"/Android/data/Android.Mobile.System/data/Mobile.apk")), "application/vnd.android.package-archive");
////			        startActivity(intent);
//
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory()+"/Android/data/Android.Mobile.System/data/Mobile.apk")),
//                        "application/vnd.android.package-archive");
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//
//
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }


            } catch (Exception e) {
                Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
                Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        /**
         * Updating progress bar
         * */
        protected void onProgressUpdate(Integer... progress) {
            // setting progress percentage
//            pDialog.setProgress(Integer.parseInt(progress[0]));

            // Making progress bar visible
            progressBar.setVisibility(View.VISIBLE);

            // updating progress bar value
            progressBar.setProgress(progress[0]);

            // updating percentage value
            txtPercentage.setText(String.valueOf(progress[0]) + "%");


        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String result) {
            // dismiss the dialog after the file was downloaded
         //   dismissDialog(progress_bar_type);
       //    Toast.makeText(getBaseContext(), "Morning Process is  Successfuly Completed..!!", Toast.LENGTH_LONG).show();
            Toast.makeText(getBaseContext(), "Successfully completed...!!", Toast.LENGTH_LONG).show();
            // Displaying downloaded image into image view
            // Reading image path from sdcard
//            String imagePath = Environment.getExternalStorageDirectory().toString() + "/downloadedfile.jpg";
            // setting downloaded into image view
           // my_image.setImageDrawable(Drawable.createFromPath(imagePath));

         //  showAlert(result);

            Intent intent = new Intent(Synchoronization.this,LoginformActivity.class);
            Synchoronization.this.startActivity(intent);

            super.onPostExecute(result);


        }

    }

//    private class FileUploadTask extends AsyncTask<Object, Integer, Void> {
//
//        private ProgressDialog dialog;
//
//        @Override
//        protected void onPreExecute() {
//            dialog = new ProgressDialog(Synchoronization.this);
//            dialog.setMessage("Uploading...");
//            dialog.setIndeterminate(false);
//            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//            dialog.setProgress(0);
//            dialog.show();
//        }
//
//        @Override
//        protected Void doInBackground(Object... arg0) {
//            try {
//                File file = new File( Environment.getExternalStorageDirectory()+"/Android/Android.Mobile.System/SFAD.zip");
//                FileInputStream fileInputStream = new FileInputStream(file);
//                byte[] bytes = new byte[(int) file.length()];
//                fileInputStream.read(bytes);
//                fileInputStream.close();
//
//                URL url = new URL("http://ebcreasy-001-site15.htempurl.com/Database/uploads/M_22222.zip");
//                HttpURLConnection connection =
//                        (HttpURLConnection) url.openConnection();
//                connection.setRequestMethod("POST");
//                OutputStream outputStream = connection.getOutputStream();
//
//
//
//                int bufferLength = 1024;
//                for (int i = 0; i < bytes.length; i += bufferLength) {
//                    int progress = (int)((i / (float) bytes.length) * 100);
//                    publishProgress(progress);
//                    if (bytes.length - i >= bufferLength) {
//                        outputStream.write(bytes, i, bufferLength);
//                    } else {
//                        outputStream.write(bytes, i, bytes.length - i);
//                    }
//                }
//                publishProgress(100);
//
////                outputStream.close();
////                outputStream.flush();
//
//                InputStream inputStream = connection.getInputStream();
//                // read the response
//                inputStream.close();
//
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//             //   Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
//            } catch (IOException e) {
//                e.printStackTrace();
//             //   Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onProgressUpdate(Integer... progress) {
//            dialog.setProgress(progress[0]);
//        }
//
//        @Override
//        protected void onPostExecute(Void result) {
//            try {
//                dialog.dismiss();
//            } catch(Exception e) {
//            }
//
//        }
//
//    }
    /**
     * Uploading the file to server
     * */
    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            progressBar.setProgress(0);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Making progress bar visible
            progressBar.setVisibility(View.VISIBLE);

            // updating progress bar value
            progressBar.setProgress(progress[0]);

            // updating percentage value
            txtPercentage.setText(String.valueOf(progress[0]) + "%");
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://ebcreasy-001-site15.htempurl.com/Database/fileUpload.php");

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                File sourceFile = new File(filePath);

                // Adding file data to http body
                entity.addPart("image", new FileBody(sourceFile));

                // Extra parameters if you want to pass to server
                entity.addPart("website",
                        new StringBody("www.androidhive.info"));
                entity.addPart("email", new StringBody("abc@gmail.com"));

                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
//            Log.e(TAG, "Response from server: " + result);

            // showing the server response in an alert dialog

            Toast.makeText(getBaseContext(), "Successfully completed...!!", Toast.LENGTH_LONG).show();
        //   showAlert(result);


            Intent intent = new Intent(Synchoronization.this,LoginformActivity.class);
            Synchoronization.this.startActivity(intent);


            super.onPostExecute(result);
        }

    }

    /**
     * Method to show alert dialog
     * */
    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message).setTitle("Response from Servers")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // do nothing
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void zip(String[] _files, String zipFileName) {
        try {
            BufferedInputStream origin = null;
            FileOutputStream dest = new FileOutputStream(zipFileName);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
                    dest));
            byte data[] = new byte[BUFFER];

            for (int i = 0; i < _files.length; i++) {
                Log.v("Compress", "Adding: " + _files[i]);
                FileInputStream fi = new FileInputStream(_files[i]);
                origin = new BufferedInputStream(fi, BUFFER);

                ZipEntry entry = new ZipEntry(_files[i].substring(_files[i].lastIndexOf("/") + 1));
                out.putNextEntry(entry);
                int count;

                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }

            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class LoadAllProducts extends AsyncTask<String, String, String> {
        private ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            JSONHttpClient jsonHttpClient = new JSONHttpClient();
            WebSync[] products = jsonHttpClient.Get(PRODUCT, nameValuePairs, WebSync[].class);
            if (products.length > 0) {

                for (WebSync product : products) {
                    HashMap<String, String> mapProduct = new HashMap<String, String>();
                    mapProduct.put("RepID", String.valueOf(product.getRepID()));
                    mapProduct.put("Distributor", product.getDistributor());
                    productList.add(mapProduct);
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
            progressDialog = new ProgressDialog(Synchoronization.this);
            progressDialog.setMessage("Loading products. Please wait...");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            if(progressDialog != null)
                progressDialog.dismiss();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ListAdapter adapter = new SimpleAdapter(Synchoronization.this, productList, R.layout.list_item_user, new String[]{"RepID", "Distributor"}, new int[]{R.id.textViewId, R.id.textViewName});
                    setListAdapter(adapter);
                }
            });
        }
    }
}
