package com.example.srmveccse;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.nfc.Tag;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final int[] red0 = { Color.rgb(51, 204, 51), Color.rgb(255, 0, 0)};
    private static final int[] red1 = { Color.rgb(255, 204, 0), Color.rgb(255, 0, 0)};
    private static final int[] red2 = { Color.rgb(204, 0, 0), Color.rgb(255, 0, 0)};
    private static final float[] sp = { 0.2f, 1f };
    private Gradient gradient;

    PlacesClient placesClient;
    TextToSpeech t1;
    String voice = "getting data from cloud";
    private GoogleMap mMap;
    private ImageView img1,img2,img3,img4;
    private HeatmapTileProvider mProvider;
    private TileOverlay mOverlay;
    private HashMap<String, DataSet> mLists = new HashMap<>();
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("spot");


    public MapsActivity() {
        // Required empty public constructor
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        img1=findViewById(R.id.imageView2);
        img2=findViewById(R.id.imageView3);
        img3=findViewById(R.id.imageView4);
        img4=findViewById(R.id.imageView5);

        if(!Places.isInitialized()){
            Places.initialize(getApplicationContext(), String.valueOf(R.string.key));
        }
        placesClient=Places.createClient(this);
        final AutocompleteSupportFragment autocompleteSupportFragment=(AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.places_autocomplete_fragment);
        assert autocompleteSupportFragment != null;
        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID,Place.Field.LAT_LNG,Place.Field.NAME));
        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                final LatLng latLng=place.getLatLng();
                Log.i("PlaceApi","onPlaceSelected :"+latLng.latitude+"\n"+latLng.longitude);
            }

            @Override
            public void onError(@NonNull Status status) {
                 Toast.makeText(getApplicationContext(),"Please pay bill to console.cloud.google.com ",Toast.LENGTH_LONG).show();
                int speech = t1.speak("Please pay bill to console.cloud.google.com", TextToSpeech.QUEUE_FLUSH, null);
            }
        });
        t1 = new TextToSpeech(getApplicationContext(),
                new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if (status != TextToSpeech.SUCCESS) {
                            int lang = t1.setLanguage(Locale.ENGLISH);
                        }
                        int speech = t1.speak(voice, TextToSpeech.QUEUE_FLUSH, null);
                    }
                });

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int speech = t1.speak("emergency contact selected", TextToSpeech.QUEUE_FLUSH, null);
                startActivity(new Intent(MapsActivity.this, Button_sos.class));
            }
        });
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int speech = t1.speak("reloading       ", TextToSpeech.QUEUE_FLUSH, null);
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int speech = t1.speak("opening camera please wait", TextToSpeech.QUEUE_FLUSH, null);
                startActivity(new Intent(MapsActivity.this, Button_cam.class));
            }
        });
        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int speech = t1.speak("fetching your current location please wait ", TextToSpeech.QUEUE_FLUSH, null);
                startActivity(new Intent(MapsActivity.this, Get_location.class));
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;

        Intent mIntent = getIntent();
        double mylat = mIntent.getDoubleExtra("lat", 0.00);
        double mylon = mIntent.getDoubleExtra("lon", 0.00);
        LatLng mylocation =new LatLng(mylat,mylon);
        googleMap.addMarker(new MarkerOptions()
                .position(mylocation)
                .title("I'm Here" )
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.maps_mylocation)));
        for (int i = 0; i < 15; i++) {
            ref.child(String.valueOf(i))
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final String place = dataSnapshot.child("place").getValue(String.class);
                            final String des = dataSnapshot.child("description").getValue(String.class);
                            final String dam = dataSnapshot.child("damage").getValue(String.class);
                            if (place != null && des != null && dam != null) {
                                findcolor(dam);
                                if(place.equals("VEC New block")){
                                    googleMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(12.826401, 80.042637))
                                            .title(place + " : " + des)
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.maps_dan60))
                                            .snippet("Damage :" + dam + "/17"));
                                    mLists.put(getString(R.string.s1), new DataSet(plotmarker(12.826401, 80.042637),
                                            getString(R.string.s1url)));
                                    mProvider = new HeatmapTileProvider.Builder().data(
                                            mLists.get(getString(R.string.s1)).getData()).build();
                                    mProvider.setRadius(120);
                                    mProvider.setGradient(gradient);
                                    mOverlay = googleMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
                                }
                                else if(place.equals("VEC First-year block")){
                                    googleMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(12.825797, 80.042254))
                                            .title(place + " : " + des)
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.maps_dan60))
                                            .snippet("Damage :" + dam + "/17"));
                                    mLists.put(getString(R.string.s1), new DataSet(plotmarker(12.825797, 80.042254),
                                            getString(R.string.s1url)));
                                    mProvider = new HeatmapTileProvider.Builder().data(
                                            mLists.get(getString(R.string.s1)).getData()).build();
                                    mProvider.setRadius(120);
                                    mProvider.setGradient(gradient);
                                    mOverlay = googleMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));

                                }
                                else if(place.equals("VEC Admin block")){
                                    googleMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(12.825857, 80.042831))
                                            .title(place + " : " + des)
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.maps_dan60))
                                            .snippet("Damage :" + dam + "/17"));
                                    mLists.put(getString(R.string.s1), new DataSet(plotmarker(12.825857, 80.042831),
                                            getString(R.string.s1url)));
                                    mProvider = new HeatmapTileProvider.Builder().data(
                                            mLists.get(getString(R.string.s1)).getData()).build();
                                    mProvider.setRadius(120);
                                    mProvider.setGradient(gradient);
                                    mOverlay = googleMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));


                                }
                                else if(place.equals("VEC Student parking")){
                                    googleMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(12.826184, 80.042011))
                                            .title(place + " : " + des)
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.maps_dan60))
                                            .snippet("Damage :" + dam + "/17"));
                                    mLists.put(getString(R.string.s1), new DataSet(plotmarker(12.826184, 80.042011),
                                            getString(R.string.s1url)));
                                    mProvider = new HeatmapTileProvider.Builder().data(
                                            mLists.get(getString(R.string.s1)).getData()).build();
                                    mProvider.setRadius(120);
                                    mProvider.setGradient(gradient);
                                    mOverlay = googleMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));


                                }
                                else if(place.equals("VEC Canteen")){
                                    googleMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(12.825535, 80.041485))
                                            .title(place + " : " + des)
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.maps_dan60))
                                            .snippet("Damage :" + dam + "/17"));
                                    mLists.put(getString(R.string.s1), new DataSet(plotmarker(12.825535, 80.041485),
                                            getString(R.string.s1url)));
                                    mProvider = new HeatmapTileProvider.Builder().data(
                                            mLists.get(getString(R.string.s1)).getData()).build();
                                    mProvider.setRadius(120);
                                    mProvider.setGradient(gradient);
                                    mOverlay = googleMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));


                                }
                                else if(place.equals("UB Bulding")){
                                    googleMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(12.823626, 80.042479))
                                            .title(place + " : " + des)
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.maps_dan60))
                                            .snippet("Damage :" + dam + "/17"));
                                    mLists.put(getString(R.string.s1), new DataSet(plotmarker(12.823626, 80.042479),
                                            getString(R.string.s1url)));
                                    mProvider = new HeatmapTileProvider.Builder().data(
                                            mLists.get(getString(R.string.s1)).getData()).build();
                                    mProvider.setRadius(120);
                                    mProvider.setGradient(gradient);
                                    mOverlay = googleMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));

                                }
                                else if(place.equals("SRM Tech-Park")){
                                    googleMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(12.824849, 80.045150))
                                            .title(place + " : " + des)
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.maps_dan60))
                                            .snippet("Damage :" + dam + "/17"));
                                    mLists.put(getString(R.string.s1), new DataSet(plotmarker(12.824849, 80.045150),
                                            getString(R.string.s1url)));
                                    mProvider = new HeatmapTileProvider.Builder().data(
                                            mLists.get(getString(R.string.s1)).getData()).build();
                                    mProvider.setRadius(120);
                                    mProvider.setGradient(gradient);
                                    mOverlay = googleMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));

                                }
                                else if(place.equals("Java-Green Food Court")){
                                    //googleMap.addMarker(new MarkerOptions()
                                    MarkerOptions marker = new MarkerOptions();
                                    marker.position(new LatLng(12.823359, 80.044494))
                                            .title(place + " : " + des)
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.maps_dan60))
                                            .snippet("Damage :" + dam + "/17");
                                    googleMap.addMarker(marker);
                                    mLists.put(getString(R.string.s1), new DataSet(plotmarker(12.823359, 80.044494),
                                            getString(R.string.s1url)));
                                    mProvider = new HeatmapTileProvider.Builder().data(
                                            mLists.get(getString(R.string.s1)).getData()).build();
                                    mProvider.setRadius(120);
                                    mProvider.setGradient(gradient);
                                    mOverlay = googleMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
                                }
                                else {
                                    Toast.makeText(getApplicationContext(),"no place",Toast.LENGTH_LONG).show();
                                }

                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(12.823154, 80.045785), 16));
                                googleMap.setMinZoomPreference(10.0f);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
        }

    }



    private Gradient findcolor(String dam) {
        int n = Integer.parseInt(dam);
        if(n <6) {
            gradient=new Gradient(red0,sp);
            return gradient;
        }else if(n < 13 ){
            gradient=new Gradient(red1,sp);
            return gradient;
        }else{
            gradient=new Gradient(red2,sp);
            return gradient;
        }
    }


    private ArrayList<LatLng> plotmarker(double lat,double lng)  {
        ArrayList<LatLng> list = new ArrayList<>();
        list.add(new LatLng(lat,lng));
        return list;
    }


    private class DataSet {
        private ArrayList<LatLng> mDataset;
        private String mUrl;

        public DataSet(ArrayList<LatLng> dataSet, String url) {
            this.mDataset = dataSet;
            this.mUrl = url;
        }

        public ArrayList<LatLng> getData() {

            return mDataset;
        }

        public String getUrl() {
            return mUrl;
        }
    }

}
