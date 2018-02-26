package com.makau.kelvin.idscantask;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.makau.kelvin.idscantask.BluePrints.BluePrint;
import com.regula.sdk.CaptureActivity;
import com.regula.sdk.DocumentReader;
import com.regula.sdk.enums.eVisualFieldType;
import com.regula.sdk.results.TextField;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;


public class MainActivity extends AppCompatActivity {
    Uri uri;
    private BluePrint bluePrint;
    String [] perms = {"android.permission.WRITE_EXTERNAL_STORAGE","android.permission.READ_EXTERNAL_STORAGE","android.permission.CAMERA"};
    int PERM_REQ_CODE = 200;
    private static final int REQ_CODE =100 ;
    private File file;
    TextView tv_view, tv_view_two;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_main);
       // setContentView(R.layout.main_view);
        checkPerm();

        tv_view=(TextView)findViewById(R.id.tv_view);
        tv_view_two=(TextView)findViewById(R.id.tv_view_two);
        YoYo.with(Techniques.FadeIn).playOn(tv_view);
        YoYo.with(Techniques.FadeIn).playOn(tv_view_two);
    }

    private void checkPerm() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ){
               // doStuff();
                return;

            }else{
                ActivityCompat.requestPermissions(this,perms,PERM_REQ_CODE);
            }



        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults) {
        boolean writeStoragePerm = grantResults[0] == PackageManager.PERMISSION_GRANTED;
        boolean readStoragePerm = grantResults[1] == PackageManager.PERMISSION_GRANTED;
        boolean camPerm = grantResults[2] == PackageManager.PERMISSION_GRANTED;

        if(requestCode == PERM_REQ_CODE && grantResults.length >1){
            if(writeStoragePerm && readStoragePerm && camPerm){

                return;
            }else{
                Toast.makeText(this, "Denied Permission.. Exiting..", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
                finish();
                System.exit(0);
            }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    public void goToScanActivity(View view) {
        // Intent for ScanCard Activity
       // startActivity(new Intent(MainActivity.this, ScanActivity.class));
        Intent intent = new  Intent(MainActivity.this, CaptureActivity.class);
        MainActivity.this.startActivityForResult(intent, REQ_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1000:
                if (resultCode == RESULT_OK) {
                    bluePrint.addToGallery();
                    doCrop();


                }
                break;
            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);

                    Intent intee = new Intent(this, ScanReceipt.class);
                    intee.putExtra("path", result.getUri().getPath());

                    startActivity(intee);

                }
                break;

            case REQ_CODE:
                if (resultCode == RESULT_OK) {

                    TextField fullnames_tf = DocumentReader.Instance().getTextFieldByType(eVisualFieldType.ft_Surname_And_Given_Names);
                    TextField idno_tf = DocumentReader.Instance().getTextFieldByType(eVisualFieldType.ft_Identity_Card_Number);
                    TextField serial_no_tf = DocumentReader.Instance().getTextFieldByType(eVisualFieldType.ft_Document_Number);
                    TextField mrzlines_tf = DocumentReader.Instance().getTextFieldByType(eVisualFieldType.ft_MRZ_Strings_With_Correct_CheckSums);
                    TextField countrycode_tf = DocumentReader.Instance().getTextFieldByType(eVisualFieldType.ft_Issuing_State_Code);
                    TextField dob_tf = DocumentReader.Instance().getTextFieldByType(eVisualFieldType.ft_Date_of_Birth);
                    TextField sex_tf = DocumentReader.Instance().getTextFieldByType(eVisualFieldType.ft_Sex);
                    TextField doi_tf = DocumentReader.Instance().getTextFieldByType(eVisualFieldType.ft_Date_of_Issue);
                    TextField country_tf = DocumentReader.Instance().getTextFieldByType(eVisualFieldType.ft_Issuing_State_Name);

                    Bitmap bmp = DocumentReader.Instance().getSourceImage();

                    /////////////////////////////////////////
                    ////////////////////////////////////////////
                    BluePrint bluePrint = new BluePrint(MainActivity.this);
                    bluePrint.createSaveMyImage("e-scanner", fullnames_tf.bufText + "_" + idno_tf.bufText, "png", bmp);
                    ////////
                    String dob = dob_tf.bufText;
                    String finalle_dob = formatDate(dob);

                    String doi = doi_tf.bufText;
                    String finalle_doi = formatDate(doi);
                    Intent results_intent = new Intent(MainActivity.this, ResultsActivity.class);

                    results_intent.putExtra("serial_no", serial_no_tf.bufText);
                    results_intent.putExtra("mrz_lines", mrzlines_tf.bufText);
                    results_intent.putExtra("country_code", countrycode_tf.bufText);
                    //results_intent.putExtra("dob",dob_tf.bufText);
                    results_intent.putExtra("dob", finalle_dob);
                    results_intent.putExtra("sex", sex_tf.bufText);
                    results_intent.putExtra("fullnames", fullnames_tf.bufText);
                    results_intent.putExtra("doi", finalle_doi);
                    results_intent.putExtra("id_no", idno_tf.bufText);
                    results_intent.putExtra("country", country_tf.bufText);
                    results_intent.putExtra("img_path", bluePrint.getMyImagePath());
                    results_intent.putExtra("tag", "is_not_saved");


                    startActivity(results_intent);

                }

        }
    }

    private String formatDate(String dob) {

        String one = dob.substring(0,2);
        String two = dob.substring(2,4);
        String three = dob.substring(4,6);

        return one+"."+two+"."+three;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        startActivity(new Intent(Intent.ACTION_MAIN).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addCategory(Intent.CATEGORY_HOME));
        finish();
        System.exit(0);

    }

    public void goToSaved(View view) {
      startActivity(new Intent(MainActivity.this,SavedKipandeActivity.class));
    }

    public void goToScanReceiptActivity(View view) {
       startActivity(new Intent(MainActivity.this,ScanTypeChooserActivity.class));
       //doCrop();
    }
    public void startCam() {

        bluePrint = new BluePrint(MainActivity.this);
        File out = bluePrint.createSaveMyImage("e-scanner","jpg");

        uri  = Uri.fromFile(out);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
        startActivityForResult(intent,1000);

    }



    public void startCrop() {
        doCrop();
    }
    public void doCrop(){
        CropImage.activity(uri)
                .start(this);
    }
}