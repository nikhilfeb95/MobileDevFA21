package edu.neu.numad21fa_nikhilmollay;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);

        TextView textView = findViewById(R.id.infoText);
        textView.setText("Name : Nikhil Mollay \n Email: mollay.n@northeastern.edu");
    }
}
