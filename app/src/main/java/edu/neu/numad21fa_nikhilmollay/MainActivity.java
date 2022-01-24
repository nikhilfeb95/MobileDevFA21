package edu.neu.numad21fa_nikhilmollay;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = findViewById(R.id.button);

        btn.setOnClickListener(x -> Toast.makeText(getApplicationContext(),
                "Name : Nikhil Nollay \n Email: mollay.n@northeastern.edu",
                Toast.LENGTH_LONG).show());
    }
}