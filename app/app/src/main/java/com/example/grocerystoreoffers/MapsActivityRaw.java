// Copyright 2020 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.example.grocerystoreoffers;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * A styled map using JSON styles from a raw resource.
 */
public class MapsActivityRaw extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = MapsActivityRaw.class.getSimpleName();
    private final LatLng WILLYS = new LatLng(59.387, 13.4809);
    private final LatLng ICA_MAXI_BERGVIK = new LatLng(59.376501, 13.430137);
    private final LatLng COOP = new LatLng(59.40284, 13.57188);
    private final LatLng LIDL_RATTGATAN = new LatLng(59.38751, 13.47539);
    private final LatLng LIDL_OSTRA = new LatLng(59.39928, 13.53470);

    private Marker markerWILLYS;
    private Marker markerICA;
    private Marker markerCOOP;
    private Marker markerLIDLRATTGATAN;
    private Marker markerLIDLOSTRA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_maps_raw);

        // Get the SupportMapFragment and register for the callback
        // when the map is ready for use.
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map when it's available.
     * The API invokes this callback when the map is ready for use.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
        /*
        if(checkPermission())
            //googleMap.setMyLocationEnabled(true);
        else askPermission();


        googleMap.setOnMyLocationButtonClickListener(this);
        googleMap.setOnMyLocationClickListener(this);

         */
        // Position the map's camera near Karlstad, Sweden.
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(59.4022, 13.5115)));
        // Längdgrad = longitude
        // Breddgrad = latitude
        markerWILLYS = googleMap.addMarker(new MarkerOptions()
                .position(WILLYS)
                .title("Willys"));
        markerWILLYS.setTag(0);

        markerICA = googleMap.addMarker(new MarkerOptions()
                .position(ICA_MAXI_BERGVIK)
                .title("Ica Maxi Bergvik"));
        markerICA.setTag(0);

        markerCOOP = googleMap.addMarker(new MarkerOptions()
                .position(COOP)
                .title("Coop Kronoparken"));
        markerCOOP.setTag(0);

        markerLIDLRATTGATAN = googleMap.addMarker(new MarkerOptions()
                .position(LIDL_RATTGATAN)
                .title("LIDL Rattgatan"));
        markerLIDLRATTGATAN.setTag(0);

        markerLIDLOSTRA = googleMap.addMarker(new MarkerOptions()
                .position(LIDL_OSTRA)
                .title("LIDL Östra Infarten"));
        markerLIDLOSTRA.setTag(0);


    }

}