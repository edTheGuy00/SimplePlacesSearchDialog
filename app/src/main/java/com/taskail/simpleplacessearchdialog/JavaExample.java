package com.taskail.simpleplacessearchdialog;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.location.places.Place;
import com.taskail.googleplacessearchdialog.SimplePlacesSearchDialogBuilder;
import com.taskail.googleplacessearchdialog.SimplePlacesSearchDialog;

import org.jetbrains.annotations.NotNull;

/**
 * Created by ed on 1/22/18.
 */

public class JavaExample extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        Button searchBtn = findViewById(R.id.locationBtn);

        final SimplePlacesSearchDialog searchDialog = new SimplePlacesSearchDialogBuilder(this)
                .setLocationListener(new SimplePlacesSearchDialog.PlaceSelectedCallback() {
            @Override
            public void onPlaceSelected(@NotNull Place place) {

            }
        }).build();

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchDialog.show();
            }
        });

    }
}
