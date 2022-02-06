package edu.neu.numad21fa_nikhilmollay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button aboutMebtn = findViewById(R.id.aboutMeButton);

        aboutMebtn.setOnClickListener(x -> Toast.makeText(getApplicationContext(),
                "Name : Nikhil Mollay \n Email: mollay.n@northeastern.edu",
                Toast.LENGTH_LONG).show());

        Button clickyBtn = findViewById(R.id.clickyButton);
        clickyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ClickActivity.class);
                startActivity(intent);
            }
        });
    }
}