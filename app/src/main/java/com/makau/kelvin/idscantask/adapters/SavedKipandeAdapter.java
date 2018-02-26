package com.makau.kelvin.idscantask.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.makau.kelvin.idscantask.MainActivity;
import com.makau.kelvin.idscantask.R;
import com.makau.kelvin.idscantask.ResultsActivity;
import com.makau.kelvin.idscantask.models.DBAccess;
import com.makau.kelvin.idscantask.models.Kipande;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by KaKaNg on 12/29/2017.
 */

public class SavedKipandeAdapter extends RecyclerView.Adapter<SavedKipandeAdapter.myHolder> {
    List<Kipande> kipandes;
    Context ctx;
    LayoutInflater inflater;
    Kipande kipande;



    public SavedKipandeAdapter(List<Kipande> kipandes, Context ctx) {
        this.kipandes = kipandes;
        this.ctx = ctx;
        inflater = LayoutInflater.from(ctx);
    }

    public class myHolder extends RecyclerView.ViewHolder {
        TextView fullnames, id_no;
        ImageView scaned_img;
        public myHolder(final View itemView) {
            super(itemView);
            fullnames=itemView.findViewById(R.id.fullnames);
            id_no =itemView.findViewById(R.id.id_no);
            scaned_img = itemView.findViewById(R.id.img);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    kipande = kipandes.get(getAdapterPosition());

                    Intent results_intent = new Intent(ctx,ResultsActivity.class);
                  /*  results_intent.putExtra("serial_no",kipande.getSerial_no());
                    results_intent.putExtra("mrz_lines",kipande.getMRZ_lines());
                    results_intent.putExtra("country_code",kipande.getCountry_code());
                    results_intent.putExtra("dob",kipande.getDob());
                    results_intent.putExtra("sex",kipande.getSex());
                    results_intent.putExtra("fullnames",kipande.getFull_names());
                    results_intent.putExtra("doi",kipande.getDoi());
                    results_intent.putExtra("id_no",kipande.getId_no());
                    results_intent.putExtra("country",kipande.getCountry());
                    results_intent.putExtra("img_path",kipande.getImg_path());*/
                    results_intent.putExtra("tag","is_saved");
                    results_intent.putExtra("data",kipande);

                    ctx.startActivity(results_intent);


                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(ctx,view);
                    popupMenu.inflate(R.menu.menu);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            int id = item.getItemId();
                            if(id==R.id.menu_del){
                                new DBAccess(ctx).deleteFromFavDB(kipande);
                                kipandes.remove(kipande);
                                refresh();
                            }


                            return false;
                        }
                    });
                    popupMenu.show();






                    return true;
                }
            });
        }

        private void refresh() {
            notifyDataSetChanged();
        }
    }

    @Override
    public SavedKipandeAdapter.myHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.saved_scan_item,parent,false);
        return new myHolder(v);
    }

    @Override
    public void onBindViewHolder(SavedKipandeAdapter.myHolder holder, int position) {
    kipande = kipandes.get(position);
        holder.fullnames.setText(kipande.getFull_names());
        holder.id_no.setText(kipande.getId_no());

        File file = new File(kipande.getImg_path());
        try{
            InputStream stream=ctx.getContentResolver().openInputStream(Uri.fromFile(file));
            Bitmap selectedImg= BitmapFactory.decodeStream(stream);
            holder.scaned_img.setImageBitmap(selectedImg);
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return kipandes.size();
    }
}
