package com.makau.kelvin.idscantask;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.regula.sdk.DocumentReader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Splash extends AppCompatActivity {
    private byte[] licence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_splash);
        startActivity(new Intent(Splash.this,MainActivity.class));
        finish();
       // doStuff();

    }

    private void doStuff() {
        //InitialiseSdkNow();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               // DocumentReader.Instance().Init(Splash.this,licence);

            }
        },1000);
    }

    private void InitialiseSdkNow() {
        try{
            InputStream in  = this.getResources().openRawResource(R.raw.regula);

            ByteArrayOutputStream byteout = new ByteArrayOutputStream();
            int i;
            try {
                i = in.read();
                while (i != -1) {
                    byteout.write(i);
                    i = in.read();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
            licence = byteout.toByteArray();

            in.close();
            byteout.close();

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
       // super.onBackPressed();
        startActivity(new Intent(Intent.ACTION_MAIN).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addCategory(Intent.CATEGORY_HOME));
        finish();
        System.exit(0);
    }
}
