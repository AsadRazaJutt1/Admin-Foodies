package com.example.adminfoodies;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.internal.GoogleApiAvailabilityCache;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.security.KeyStore;
import java.sql.Connection;

public class OrderTracker extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks
        , GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;

    private final static int PLAY_SERVICES_REQUEST = 1000;
    private final static int LOCATION_PRMISSION_REQUEST = 1001;

    private static int UPDATE_INTERVAT = 1000;
    private static int FATEST_INTERVAT = 5000;
    private static int DISPLACEMANT = 10;

    private Location mLastLocation;
    private GoogleApiClient mgoogleApiClient;
    private LocationRequest mLoactionResquest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_tracker);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
            requestRunTimePermissions();

        } else {
            if (checkPlayServices()) {

                buildGoogleApiClient();
                createLocationRequest();
            }
        }
        showLocation();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void createLocationRequest() {

        mLoactionResquest = new LocationRequest();
        mLoactionResquest.setInterval(UPDATE_INTERVAT);
        mLoactionResquest.setFastestInterval(FATEST_INTERVAT);
        mLoactionResquest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLoactionResquest.setSmallestDisplacement(DISPLACEMANT);

    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_REQUEST).show();
            } else {
                Toast.makeText(this, "This Device not Supported", Toast.LENGTH_SHORT).show();
                finish();
            }
            return false;
        }
        return true;
    }

    private void requestRunTimePermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION
                , Manifest.permission.ACCESS_FINE_LOCATION
        }, LOCATION_PRMISSION_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case LOCATION_PRMISSION_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (checkPlayServices()) {

                        buildGoogleApiClient();
                        createLocationRequest();
                        showLocation();

                    }
                }
                break;
        }
    }

    private void showLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            requestRunTimePermissions();

        } else {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mgoogleApiClient);
            if (mLastLocation != null) {
                double latitude = mLastLocation.getLatitude();
                double longitude = mLastLocation.getLongitude();
                LatLng currentLocation = new LatLng(latitude, longitude);
                mMap.addMarker(new MarkerOptions().position(currentLocation).title("You"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
                mMap.animateCamera(CameraUpdateFactory.zoomBy(15.10f));


            } else {
                Toast.makeText(this, "Something Wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mgoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mgoogleApiClient.connect();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onLocationChanged(Location location) {
        this.mLastLocation = location;
        showLocation();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        showLocation();
        locationUpdate();
    }

    private void locationUpdate() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            return;

        }
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(mgoogleApiClient, mLoactionResquest, (com.google.android.gms.location.LocationListener) this);
        }catch (Exception ex){
            Toast.makeText(this, ""+ex, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        mgoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mgoogleApiClient!=null){
            mgoogleApiClient.connect();
        }
    }
}
