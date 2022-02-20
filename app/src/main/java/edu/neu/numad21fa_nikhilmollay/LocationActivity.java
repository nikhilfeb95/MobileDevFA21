package edu.neu.numad21fa_nikhilmollay;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;


/**
 * An activity which obtains the location from the location manager.
 */
public class LocationActivity extends AppCompatActivity {
    private static int REQUEST_ID = 1;
    FusedLocationProviderClient fusedLocationProviderClient;
    TextView lattitudeTextView;
    TextView longitudeTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_style);

        lattitudeTextView = findViewById(R.id.lattitudeTextView);
        longitudeTextView = findViewById(R.id.longitude_text_view);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        //Request location permission on creation
        requestPermissions();
        getUserLocation();
    }

    @SuppressLint("MissingPermission")
    private void getUserLocation() {
        if (checkPermissions()) {
            LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            //switch on location services
            if (locManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if(location != null){
                            lattitudeTextView.setText(String.valueOf(location.getLatitude()));
                            longitudeTextView.setText(String.valueOf(location.getLongitude()));
                        }
                    }
                });
            }
            else {
                Toast.makeText(this,"Please switch on location services!!", Toast.LENGTH_LONG);
            }
        }
        else {
            requestPermissions();
        }
    }

    private void requestPermissions(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_ID);
    }

    //A method to check if the permission has been granted by the user.
    private boolean checkPermissions(){
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }
}
