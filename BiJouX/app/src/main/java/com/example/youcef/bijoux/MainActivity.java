package com.example.youcef.bijoux;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

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

public class MainActivity extends Activity implements TaskListener{
    public  Result res;

    String descr;
    String prix;
    String photo;
    InputStream is=null;
    String result=null;
    String line=null;
    int code;
    int nbline = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText e_descr=(EditText) findViewById(R.id.editText1);
        final EditText e_prix=(EditText) findViewById(R.id.editText2);
      //  final EditText e_photo= (EditText) findViewById(R.id.editText3);
        Button insert=(Button) findViewById(R.id.button1);


        insert.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                prix= e_prix.getText().toString();
                descr = e_descr.getText().toString();
                photo = " ";//e_photo.getText().toString();




                insert();

            }
        });
    }

    public void insert()
    {
        DownloadTask dt;
        dt = new DownloadTask(this, this);
        dt.setDescr(descr);
        dt.setPrix(prix);
        dt.setPhoto(photo);
        dt.execute();


    }









    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onTaskStarted() {
        Toast.makeText(this, "commence",
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTaskFinished() {

        if(Result.nbline <= 0)
        {
            Toast.makeText(this, "insertion échouer, le bijou existe déjà dans la base de donnée ",
                    Toast.LENGTH_LONG).show();

        }
        else
        {
            Toast.makeText(this, "insertion reussite ",
                    Toast.LENGTH_SHORT).show();

        }
    }


    @Override
    public void onTaskSucceed() {
        Toast.makeText(this, "sucess",
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTaskFailed() {
        Toast.makeText(this, "failed",
                Toast.LENGTH_LONG).show();
    }
}