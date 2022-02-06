package edu.neu.numad21fa_nikhilmollay;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ClickActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buttons_activity);
    }

    public void onClick(View view){
        String toPrint = "Pressed : ";
        switch (view.getId()){
            case R.id.button_A:
                toPrint+="A";
                break;
            case R.id.button_B:
                toPrint+="B";
                break;
            case R.id.button_C:
                toPrint+="C";
                break;
            case R.id.button_D:
                toPrint+="D";
                break;
            case R.id.button_E:
                toPrint+="E";
                break;
            case R.id.button_F:
                toPrint+="F";
                break;
            default:
                break;
        }
        TextView textView = findViewById(R.id.keyPressedTextView);
        textView.setText(toPrint);
    }

}
