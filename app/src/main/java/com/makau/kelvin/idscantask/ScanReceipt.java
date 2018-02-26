package com.makau.kelvin.idscantask;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.CallLog;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Line;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.android.gms.vision.text.internal.client.RecognitionOptions;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScanReceipt extends AppCompatActivity {
    Button scanbtn;
    ImageView imgToScan;
    private TextRecognizer textRecognizer;
    EditText detectedTV,date;
    TextView[] textView;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_receipt);
        //linearLayout = findViewById(R.id.vw);
        detectedTV = findViewById(R.id.rs_mercharnt);
        date = findViewById(R.id.rs_date);

        imgToScan = findViewById(R.id.imageToRead);
        textRecognizer = new TextRecognizer.Builder(this)

                .build();
        textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<TextBlock> detections) {

            }
        });
        final File file = new File(getIntent().getStringExtra("path")).getAbsoluteFile();
        try {
            scanPic(decodeBitmapUri(ScanReceipt.this, Uri.fromFile(file)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
      /*  try {
          //  Glide.with(ScanReceipt.this).load(decodeBitmapUri(ScanReceipt.this,Uri.fromFile(file))).fitCenter().into(imgToScan);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
*/
/*
        scanbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {
                    scanPic(decodeBitmapUri(ScanReceipt.this,Uri.fromFile(file)));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });*/

    }

    private Bitmap decodeBitmapUri(Context ctx, Uri uri) throws FileNotFoundException {
        int targetW = 600;
        int targetH = 600;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(ctx.getContentResolver().openInputStream(uri), null, options);
        int photoW = options.outWidth;
        int photoH = options.outHeight;

        int scalefactor = Math.min(photoW / targetW, photoH / targetH);
        options.inJustDecodeBounds = false;
        options.inSampleSize = scalefactor;


        return BitmapFactory.decodeStream(ctx.getContentResolver().openInputStream(uri), null, options);
    }

    private void scanPic(Bitmap bitmap) {
        String detected = " ";
        String items_str = " ";
        String price = " ";
        String total = " ";
        String footer = " ";

        String ITEM, QTY, AMOUNT;
        if (!textRecognizer.isOperational()) {
            new AlertDialog.Builder(this).setMessage("Recognizer could not be set up on your device :(").show();
            return;
        }
        Frame frame = new Frame.Builder()

                .setBitmap(bitmap).build();


        SparseArray<TextBlock> text = textRecognizer.zza(frame, new RecognitionOptions());

        Toast.makeText(this, "" + text.size(), Toast.LENGTH_SHORT).show();


        for (int i = 0; i < text.size(); i++) {

                    TextBlock textBlock = text.get(text.keyAt(i));

            if (textBlock != null && textBlock.getValue() != null) {
                try {
                    String priceRegex = "(\\d+[,.]\\d\\d)";

                            String l = textBlock.toString();
                            if(l.matches(priceRegex)) {
                                detected += l + "\n";
                            }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

            detectedTV.setText(detected);
           // items.setText(items_str);
            textRecognizer.release();

        }


    }
    @Override
    protected void onDestroy() {
        super.onDestroy();


    }

    public static boolean almostEqual(double a, double b, double eps) {
        return Math.abs(a - b) < (eps);
    }

    public static boolean pointAlmostEqual(Point a, Point b) {
        return almostEqual(a.y, b.y, 10);
    }

    public static boolean cornerPointAlmostEqual(Point[] rect1, Point[] rect2) {
        boolean almostEqual = true;
        for (int i = 0; i < rect1.length; i++) {
            if (!pointAlmostEqual(rect1[i], rect2[i])) {
                almostEqual = false;
            }
        }
        return almostEqual;
    }

}