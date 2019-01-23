package com.rrdl.gradientpolylinesexample;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.rrdl.gradientpoly.GradientPoly;

import java.util.Random;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Random r = new Random();
    Button generate;
    LatLng from,to;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideSystemUI();
        setContentView(R.layout.activity_maps);
        generate=findViewById(R.id.generate);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMaxZoomPreference(18f);
        // Add a marker in Sydney and move the camera


        final GradientPoly gradientPoly = new GradientPoly(mMap, MainActivity.this);

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double randomValuex = 36.046851 + ((36.203712 - 36.046851) * r.nextDouble());
                double randomValuex2 = 36.046851 + ((36.203712 - 36.046851) * r.nextDouble());
                double randomValuey = 8.269289 + ((10.486982 - 8.269289) * r.nextDouble());
                double randomValuey2 = 8.269289 + ((10.486982 - 8.269289) * r.nextDouble());
                from = new LatLng(randomValuex, randomValuey);
                to = new LatLng(randomValuex2, randomValuey2);

                //Setting up our awesome gradient 🌈🌈

                gradientPoly.setApiKey("API KEY GOES HERE")
                        .setStartPoint(from).setEndPoint(to)
                        .setStartColor(Color.parseColor("#1eb5ab"))
                        .setWidth(11).setEndColor(Color.parseColor("#ff0098"))
                        .DrawPolyline();
            }
        });



    }
    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

}
