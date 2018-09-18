package com.multics.meatandmeet;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;
import com.multics.meatandmeet.adapters.CustomGridViewAdapter;
import com.multics.meatandmeet.models.Seat;

import java.util.ArrayList;
import java.util.List;

public class WelcomeActivity extends AppCompatActivity {

    int count = 0;
    List<String> noKursiDipilih = new ArrayList<String>();
    List<String> no_set_available = new ArrayList<String>();
    private Button seatset,getseat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        seatset = findViewById(R.id.seatset);
        seatset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSeatArrangements();
            }
        });


    }

    private void showSeatArrangements() {

        GridView gridView;
        final ArrayList<Seat> gridArray = new ArrayList<Seat>();
        final CustomGridViewAdapter customGridAdapter;
        final Bitmap seatIcon;
        final Bitmap seatSelect;
        final Bitmap seatBooked;

        final Dialog d = new Dialog(WelcomeActivity.this);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.show_seat_dialog);

        seatIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.seat_layout_screen_nor_avl);
        seatSelect = BitmapFactory.decodeResource(this.getResources(), R.drawable.seat_layout_screen_nor_std);
        seatBooked = BitmapFactory.decodeResource(this.getResources(), R.drawable.seat_layout_screen_nor_bkd);

        no_set_available.clear();
        noKursiDipilih.clear();
        count = 0;

        for(int i=0; i<no_set_available.size(); i++) {
            if (no_set_available.get(i).contains(",")) {
                String[] kursiSplit = no_set_available.get(i).split(",");
                for (int a = 0; a < kursiSplit.length; a++) {
                    no_set_available.add(kursiSplit[a]);
                }
                no_set_available.remove(i);
            }
            Log.d("no Chairs selected:", no_set_available.get(i));
        }

        for (int i = 1; i <= 6; ++i)
        {
            if(no_set_available.contains(String.valueOf(i))) {
                gridArray.add(new Seat(seatBooked, "" + i));
            } else {
                gridArray.add(new Seat(seatIcon, "" + i));
            }
        }

        gridView = (GridView) d.findViewById(R.id.gridView1);
        customGridAdapter = new CustomGridViewAdapter(this, R.layout.seatrow_grid, gridArray);
        gridView.setAdapter(customGridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Seat item = gridArray.get(i);
                Bitmap seatcompare = item.getImage();
                if (seatcompare == seatIcon) {
                    if(count<=6-1) {
                        gridArray.remove(i);
                        gridArray.add(i, new Seat(seatSelect, ""+(i+1)));
                        customGridAdapter.notifyDataSetChanged();
                        noKursiDipilih.add((i+1)+"");
                        count++;
                    } else {
                        Toast.makeText(getApplicationContext(), "Sorry, Seat Selection Limit matches the number of passengers", Toast.LENGTH_LONG).show();
                    }
                } else {
                    if(seatcompare == seatBooked) {
                        Toast.makeText(getApplicationContext(), "Sorry, the seat has been booked", Toast.LENGTH_LONG).show();
                    } else {
                        gridArray.remove(i);
                        int ia = i + 1;
                        gridArray.add(i, new Seat(seatIcon, "" + ia));
                        customGridAdapter.notifyDataSetChanged();
                        noKursiDipilih.remove(ia + "");
                        count--;
                    }
                }
            }
        });


        d.show();
    }

}
