package com.makau.kelvin.idscantask;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.makau.kelvin.idscantask.models.DBAccess;
import com.makau.kelvin.idscantask.models.Kipande;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import it.subito.masaccio.MasaccioImageView;

public class ResultsActivity extends AppCompatActivity {
Kipande kipande;
TextView serial_no,mrz_lines,country_code,dob,sex,fullnames,doi,id_no,country;
ImageView img;
    private String sex_str;
FloatingActionButton save_btn;
    private String id_noo;
    private MasaccioImageView moscai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        initVars();
        kipande = getIntent().getParcelableExtra("data");
        try {
            if (getIntent().getStringExtra("tag").equals("is_not_saved")) {
                save_btn.setVisibility(View.VISIBLE);
            } else if (getIntent().getStringExtra("tag").equals("is_saved")) {
                save_btn.setVisibility(View.GONE);


            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initVars() {
        moscai = (MasaccioImageView)findViewById(R.id.msaico);
        save_btn = (FloatingActionButton)findViewById(R.id.saveBtn);
        img=(ImageView)findViewById(R.id.img);
        serial_no=(TextView) findViewById(R.id.serial_no);
        mrz_lines=(TextView) findViewById(R.id.mrz);
        country_code=(TextView) findViewById(R.id.country_code);
        dob=(TextView) findViewById(R.id.dob);
        sex=(TextView) findViewById(R.id.sex);
        fullnames=(TextView) findViewById(R.id.fullnames);
        doi=(TextView) findViewById(R.id.doi);
        id_no=(TextView) findViewById(R.id.id_no);
        country=(TextView) findViewById(R.id.country);

        serial_no.setText(getIntent().getStringExtra("serial_no"));
        mrz_lines.setText(getIntent().getStringExtra("mrz_lines").replace("^","\n"));
        country_code.setText(getIntent().getStringExtra("country_code"));
        dob.setText(getIntent().getStringExtra("dob"));

        if((getIntent().getStringExtra("sex").equals("M"))){
             sex_str = "Male";
        }else if((getIntent().getStringExtra("sex").equals("F"))){
            sex_str = "Female";

        }else{
            sex_str =getIntent().getStringExtra("sex");
        }
        sex.setText(sex_str);
        fullnames.setText(getIntent().getStringExtra("fullnames"));
        doi.setText(getIntent().getStringExtra("doi"));

        if (getIntent().getStringExtra("tag").equals("is_not_saved")) {
             id_noo =  MyFormater(getIntent().getStringExtra("id_no"));

            save_btn.setVisibility(View.VISIBLE);

        } else if (getIntent().getStringExtra("tag").equals("is_saved")) {
             id_noo =  getIntent().getStringExtra("id_no");

            save_btn.setVisibility(View.GONE);



        }


        id_no.setText(id_noo);
        country.setText(getIntent().getStringExtra("country"));


       //Uri imageUri = Uri.parse();
        File file = new File(getIntent().getStringExtra("img_path"));
        try{
            InputStream stream=getContentResolver().openInputStream(Uri.fromFile(file));
            Bitmap selectedImg= BitmapFactory.decodeStream(stream);
            //img.setImageBitmap(selectedImg);
            moscai.setImageBitmap(selectedImg);
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }

    }

    private String MyFormater(String id_no) {

            String one = id_no.replace(String.valueOf(id_no.charAt(0)),"");
            String two = one.replace(String.valueOf(id_no.charAt(1)),"");
            String three =  two.replace(String.valueOf(id_no.charAt(id_no.length()-1)),"");

            return three.trim();

        
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu, menu);


        return super.onCreateOptionsMenu(menu);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public void saveToDB(View view) {
        String SERIAL = serial_no.getText().toString().trim();
        String MRZ = mrz_lines.getText().toString().trim();
        String COUNTRY_CODE = country_code.getText().toString().trim();
        String DOB = dob.getText().toString().trim();

        if((getIntent().getStringExtra("sex").equals("M"))){
            sex_str = "Male";
        }else if((getIntent().getStringExtra("sex").equals("F"))){
            sex_str = "Female";

        }else{
            sex_str =getIntent().getStringExtra("sex");
        }
        String SEX = sex_str;
        String FULLNAMES = fullnames.getText().toString().trim();
        String DOI = doi.getText().toString().trim();
        String ID_NO = MyFormater(getIntent().getStringExtra("id_no"));
        String COUNTRY = country.getText().toString().trim();
        String IMG_PATH = getIntent().getStringExtra("img_path").trim();

        Kipande kipande = new Kipande();
        kipande.setCountry(COUNTRY);
        kipande.setCountry_code(COUNTRY_CODE);
        kipande.setDob(DOB);
        kipande.setDoi(DOI);
        kipande.setFull_names(FULLNAMES);
        kipande.setId_no(ID_NO);
        kipande.setSex(SEX);
        kipande.setSerial_no(SERIAL);
        kipande.setImg_path(IMG_PATH);
        kipande.setMRZ_lines(MRZ);

       new DBAccess(ResultsActivity.this).sendToDb(kipande);


    }
}
