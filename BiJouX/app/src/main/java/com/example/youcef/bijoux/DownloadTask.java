package com.example.youcef.bijoux;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.BufferedReader;
import android.os.Environment;

/**
 * Created by YouCef on 05/05/15.
 */
public class DownloadTask extends AsyncTask<String, Void, String> {
    String descr;
    String prix;
    String photo;
    InputStream is=null;
    String result=null;
    String line=null;
    int nbline = 0;

    int code;
    protected final Context ct;
    private TaskListener listener;
    public DownloadTask (Context context, TaskListener listener)
    {
        ct = context;
        this.listener = listener;
    }

    public void setDescr (String  descr)
    {
        this.descr= descr;
    }

    public void setPrix (String prix)
    {
        this.prix = prix;
    }

    public void setPhoto (String photo)
    {
        this.photo = photo;
    }

    protected void onPreExecute(String s)
    {
        listener.onTaskStarted();
    }
    @Override
    protected String doInBackground(String... params)
    {

        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        nameValuePairs.add(new BasicNameValuePair("descr",descr));
        nameValuePairs.add(new BasicNameValuePair("photo",photo));
        nameValuePairs.add(new BasicNameValuePair("prix",prix));

        try
        {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://10.0.2.2/mysql_co.php"); // localhost
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();

            is = entity.getContent();
            Log.e("pass 1", "connection reussie ");

        }
        catch(Exception e)
        {
            Log.e("Fail 1", e.toString());

        }
        try
        {
            BufferedReader reader = new BufferedReader
                    (new InputStreamReader(is,"iso-8859-1"),8);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
            Log.e("pass 2", "connection reussie ");
        }
        catch(Exception e)
        {
            Log.e("Fail 2", e.toString());
        }

        try
        {
            JSONObject json_data = new JSONObject(result);

            String tmp;
            String[] ArrayMail = new String[500];
             int len = Integer.parseInt(json_data.getString("sel_result"));

            for (int i = 0; i < len; i++) {
                tmp = json_data.getString(String.valueOf(i));
                ArrayMail[i] = tmp;
                Log.e("Mail OutPut", tmp);
            }

            sendMailClient(ArrayMail,len);
            nbline=(json_data.getInt("ins_result"));
        }
        catch(Exception e)
        {
            Log.e("Fail 3", e.toString());
            Log.e("descr", descr);
            Log.e("prix", prix);
            Log.e("photo", photo);

        }

        return "";

    }

int sendMailClient(String[] mails, int len)
{

    String[] recipients = mails;
    Intent email = new Intent(Intent.ACTION_SEND);
    // prompts email clients only
    email.setType("message/rfc822");
    email.putExtra(Intent.EXTRA_EMAIL, recipients);
    email.putExtra(Intent.EXTRA_SUBJECT, "Un nouveau produit est disponible");
    email.putExtra(Intent.EXTRA_TEXT, "Bonjour, voila un merveuille bijoux du nom de " + descr + " à " + prix + " euros (très bon prix). " + photo);


    File pngDir = new File(

            Environment.getExternalStorageDirectory(),
            "/Users/YouCef/Desktop/BiJouX/app/src/main/res/drawable/");


    if (!pngDir.exists())
        pngDir.mkdirs();

    File pngFile = new File(pngDir, "bijoux.png");
    Uri pngUri = Uri.fromFile(pngFile);
    email.putExtra(android.content.Intent.EXTRA_STREAM, pngUri);
    email.setType("image/png");
    try {
        // the user can choose the email client
        //  ct.startActivity(Intent.createChooser(email, "Choose an email client from..."));
        ct.startActivity(Intent.createChooser(email, "Send mail..."));
       // finish();
        Log.e("SendMail fini..", "");
    } catch (android.content.ActivityNotFoundException e) {
        Log.e("SendMail échoué", e.toString());
    }


   return 0;
}

    //This Method is called when Network-Request finished
    protected void onPostExecute(String serverData) {

        Result.nbline = nbline;

        listener.onTaskFinished();

    }
}
