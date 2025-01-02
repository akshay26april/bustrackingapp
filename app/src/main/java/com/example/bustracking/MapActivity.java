package com.example.bustracking;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.Date;
import java.util.Locale;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private LatLng userLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Initialize the FusedLocationProviderClient to get the user's location
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Initialize the map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        } else {
            Toast.makeText(this, "Error: Map Fragment not found!", Toast.LENGTH_SHORT).show();
        }

        TextView timeTextView;
        final Handler handler = new Handler();
        final int delay = 1000;
        timeTextView = findViewById(R.id.timeTextView);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                String currentTime = sdf.format(new Date());
                timeTextView.setText(currentTime);

                handler.postDelayed(this, delay);
            }
        }, delay);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Set a default marker while fetching the location
        LatLng defaultLocation = new LatLng(0, 0);
        mMap.addMarker(new MarkerOptions()
                .position(defaultLocation)
                .title("Fetching Location..."));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 5));

        // Fetch and update the location from Firebase
        fetchLocationFromFirebase();

        // Get the user's location
        fetchUserLocation();
    }

    private void fetchLocationFromFirebase() {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("busLocation");

        // Fetch location updates from Firebase
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    Object latitudeObj = dataSnapshot.child("latitude").getValue();
                    Object longitudeObj = dataSnapshot.child("longitude").getValue();

                    if (latitudeObj instanceof String && longitudeObj instanceof String) {
                        try {
                            double latitude = Double.parseDouble((String) latitudeObj);
                            double longitude = Double.parseDouble((String) longitudeObj);
                            updateMap(latitude, longitude);
                        } catch (NumberFormatException e) {
                            Toast.makeText(MapActivity.this, "Error parsing GPS data!", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(MapActivity.this, "Error processing data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MapActivity.this, "Error fetching data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchUserLocation() {
        // Check if location permissions are granted
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request permissions if not granted
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.addMarker(new MarkerOptions()
                        .position(userLocation)
                        .title("Your Location"));

            } else {
                Toast.makeText(this, "Unable to fetch your location!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateMap(double busLatitude, double busLongitude) {
        LatLng busLocation = new LatLng(busLatitude, busLongitude);

        // Clear previous markers and add new ones
        mMap.clear();
        mMap.addMarker(new MarkerOptions()
                        .position(busLocation)
                        .title("Bus Location"))
                .setIcon(getBusIcon());

        // Add user's location to map if available
        TextView distanceTextView = null;
        if (userLocation != null) {
            mMap.addMarker(new MarkerOptions()
                    .position(userLocation)
                    .title("Your Location"));

            // Calculate distance between user and bus
            float[] results = new float[1];
            android.location.Location.distanceBetween(
                    userLocation.latitude, userLocation.longitude,
                    busLatitude, busLongitude,
                    results);

            // Distance is in meters; convert to kilometers and update TextView

            float distanceInKm = results[0] / 1000;
            distanceTextView = findViewById(R.id.distancecal);
            if (distanceTextView != null) {
                distanceTextView.setText(String.format(Locale.getDefault(), "%.2f KM", distanceInKm));
            } else {
                Log.e("MapActivity", "distanceTextView is null!");
            }
        } else {
            Log.w("MapActivity", "User location is null. Showing 0 KM.");
            // If userLocation is null, show 0 KM
            if (distanceTextView != null) {
                distanceTextView.setText("0 KM");
            } else {
                Log.e("MapActivity", "distanceTextView is null!");
            }
        }


        // Move camera to show both the user and bus locations
        if (userLocation != null) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(busLocation);
            builder.include(userLocation);
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100));
        } else {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(busLocation, 15));
        }
    }

    private BitmapDescriptor getBusIcon() {
        int height = 100; // Desired height in pixels
        int width = 150;  // Desired width in pixels
        BitmapDrawable bitmapDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.imagebus, null);
        Bitmap originalBitmap = bitmapDrawable.getBitmap();
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, width, height, false);
        return BitmapDescriptorFactory.fromBitmap(scaledBitmap);
    }
}
