package com.example.grocerystoreoffers;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.content.Context;


import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class NearbyStoresFragment extends Fragment {

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Initialize view
        View view=inflater.inflate(R.layout.fragment_nearby_stores, container, false);

        // Initialize map fragment
        SupportMapFragment supportMapFragment=(SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map);

        // Async map
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                // When map is loaded
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(59.4022, 13.5115),11));
                markerWILLYS = googleMap.addMarker(new MarkerOptions()
                        .position(WILLYS)
                        .title("Willys")
                        .icon(BitmapFromVector(getActivity().getApplicationContext(), R.drawable.sponsor_logo_willys_2)));
                markerWILLYS.setTag(0);

                markerICA = googleMap.addMarker(new MarkerOptions()
                        .position(ICA_MAXI_BERGVIK)
                        .title("Ica Maxi Bergvik")
                        .icon(BitmapFromVector(getActivity().getApplicationContext(), R.drawable.ica_logotyp)));
                markerICA.setTag(0);

                markerCOOP = googleMap.addMarker(new MarkerOptions()
                        .position(COOP)
                        .title("Coop Kronoparken")
                        .icon(BitmapFromVector(getActivity().getApplicationContext(), R.drawable.coop_logotyp_600x174_1_2)));
                markerCOOP.setTag(0);

                markerLIDLRATTGATAN = googleMap.addMarker(new MarkerOptions()
                        .position(LIDL_RATTGATAN)
                        .title("LIDL Rattgatan")
                        .icon(BitmapFromVector(getActivity().getApplicationContext(), R.drawable.lidl_logo_2)));
                markerLIDLRATTGATAN.setTag(0);

                markerLIDLOSTRA = googleMap.addMarker(new MarkerOptions()
                        .position(LIDL_OSTRA)
                        .title("LIDL Östra Infarten")
                        .icon(BitmapFromVector(getActivity().getApplicationContext(), R.drawable.lidl_logo_2)));
                markerLIDLOSTRA.setTag(0);
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        /*
                        // When clicked on map
                        // Initialize marker options
                        MarkerOptions markerOptions=new MarkerOptions();
                        // Set position of marker
                        markerOptions.position(latLng);
                        // Set title of marker
                        markerOptions.title(latLng.latitude+" : "+latLng.longitude);
                        // Remove all marker
                        googleMap.clear();
                        // Animating to zoom the marker
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                        // Add marker on map
                        googleMap.addMarker(markerOptions);

                         */
                    }
                });

            }
            private BitmapDescriptor BitmapFromVector(Context context, int vectorResId) {
                // below line is use to generate a drawable.
                Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);

                // below line is use to set bounds to our vector drawable.
                vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());

                // below line is use to create a bitmap for our
                // drawable which we have added.
                Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

                // below line is use to add bitmap in our canvas.
                Canvas canvas = new Canvas(bitmap);

                // below line is use to draw our
                // vector drawable in canvas.
                vectorDrawable.draw(canvas);

                // after generating our bitmap we are returning our bitmap.
                return BitmapDescriptorFactory.fromBitmap(bitmap);
            }
        });
        // Return view
        return view;
    }
}