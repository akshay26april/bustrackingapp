package com.example.bustracking;

import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class Vita_Bhalavni extends AppCompatActivity implements OnMapReadyCallback {

    // Define an array of LatLng objects to hold multiple locations
    private LatLng[] locations = new LatLng[]{
            // AITRC
            new LatLng(17.222968, 74.531285), // Bus Stop in AITRC (existing)
            // Nevri Naka - Vita
            new LatLng(17.272612, 74.532554),
            // Vivekanand Nagar
            new LatLng(17.274100, 74.525701),
            // Dhavleshwar
            new LatLng(17.248252, 74.487707),
            // Kalambi
            new LatLng(17.228871, 74.481783),
            // Panchling Nagar
            new LatLng(17.222979, 74.461905),
            // Bhalawani
             new LatLng(17.219580, 74.447821),

    };

    private static final int[] markerColors = {
            Color.RED,
            Color.BLUE,
            Color.GREEN,
            Color.YELLOW,
            Color.CYAN,
            Color.MAGENTA,
            Color.BLACK,
            Color.GRAY,
            Color.LTGRAY,
            Color.DKGRAY,
            Color.WHITE
    };

    private static final String[] markerTitles = {
            "AITRC Bus Stop",
            "Vita (Nivri Naka)",
            "Vivekanandnagar",
            "Dhavleshwar",
            "Kalambi",
            "Panchlingnagar",
            "Bhalawani",
    };

    private TextView timeTextView;
    final Handler handler = new Handler();
    final int delay = 1000; // Update time every 1 second

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vita_bhalavni);

        timeTextView = findViewById(R.id.timeTextView);

        // Real timing clock

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                String currentTime = sdf.format(new Date());
                timeTextView.setText(currentTime);

                handler.postDelayed(this, delay);
            }
        }, delay);

        // Handle system window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return WindowInsetsCompat.CONSUMED; // Consume the insets
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        // Add markers for each location in the array
        for (LatLng location : locations) {
            googleMap.addMarker(new MarkerOptions()
                    .position(location)
                    .title("AITRC Vita")); // Replace with custom titles if needed

        }

        // Move camera to the first location (adjust as needed)
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(locations[0]));
        googleMap.setMinZoomPreference(10f); // Set minimum zoom level
        googleMap.setMaxZoomPreference(18f); // Set maximum zoom level

        // Comment out the following code to remove the polyline
        // googleMap.addPolyline(new PolylineOptions()
        //         .addAll(Arrays.asList(locations)) // Add all locations from the array
        //         .width(5f)
        //         .color(Color.BLUE));
    }
}