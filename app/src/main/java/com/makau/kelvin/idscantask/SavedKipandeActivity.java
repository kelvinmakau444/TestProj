package com.makau.kelvin.idscantask;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.makau.kelvin.idscantask.adapters.SavedKipandeAdapter;
import com.makau.kelvin.idscantask.models.DBAccess;
import com.makau.kelvin.idscantask.models.Kipande;

import java.util.ArrayList;
import java.util.List;

public class SavedKipandeActivity extends AppCompatActivity {
RecyclerView scannedRV;
    SavedKipandeAdapter adapter;
    List<Kipande> kipandes;
    LinearLayoutManager linearLayoutManager;
    LinearLayout no_saved;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_kipande);
        scannedRV = (RecyclerView)findViewById(R.id.savedRV);
        no_saved =(LinearLayout)findViewById(R.id.no_saved);
        kipandes = new ArrayList<>();
        kipandes= new DBAccess(this).getSavedKipandes();

        if(kipandes.size()>0){
            if(no_saved.getVisibility() == View.VISIBLE){
                no_saved.setVisibility(View.GONE);
            }
            linearLayoutManager =new LinearLayoutManager(this);
            scannedRV.setLayoutManager(linearLayoutManager);
            adapter=new SavedKipandeAdapter(kipandes,this);
            scannedRV.setAdapter(adapter);
        }else{
            no_saved.setVisibility(View.VISIBLE);
        }





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
}
