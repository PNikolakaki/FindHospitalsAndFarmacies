package com.appcook.user.findhospitalsandpharmacies.Controllers;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.appcook.user.findhospitalsandpharmacies.Model.DistanceService;
import com.appcook.user.findhospitalsandpharmacies.Model.Service;
import com.appcook.user.findhospitalsandpharmacies.Model.User;
import com.appcook.user.findhospitalsandpharmacies.R;
import com.appcook.user.findhospitalsandpharmacies.Services.UseJson;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeErrorDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeSuccessDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.text.DecimalFormat;
import java.util.Map;

import static com.appcook.user.findhospitalsandpharmacies.R.id.bgetlocation;
import static com.appcook.user.findhospitalsandpharmacies.R.id.light;

public class MainActivity extends AppCompatActivity implements Serializable,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{


    private Button bgetLocation;
    private Button bsearch;
    private Button binfo;
    private Spinner dropdownType;
    private Spinner dropdownRadius;
    private TextView tvCurrentLocation;
    private GoogleApiClient googleApiClient;
    private String url;
    String selectedType;
    int realRadius;
    public static CircularProgressView progressView;
    public static Dialog overlayDialog;
    com.appcook.user.findhospitalsandpharmacies.Model.Location usersLocation;
    com.appcook.user.findhospitalsandpharmacies.Model.Location serviceLocation;
    User user = new User();
    JSONArray jsonArray;
    private static DecimalFormat df2 = new DecimalFormat(".##");



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bsearch = (Button) findViewById(R.id.bsearch);
        bgetLocation = (Button) findViewById(bgetlocation);
        binfo = (Button) findViewById(R.id.binfo);
        dropdownType = (Spinner) findViewById(R.id.spinnerType);
        dropdownRadius = (Spinner) findViewById(R.id.spinnerRadius);
        tvCurrentLocation = (TextView) findViewById(R.id.tvCurrentLocation);
        usersLocation = new com.appcook.user.findhospitalsandpharmacies.Model.Location();
        serviceLocation = new com.appcook.user.findhospitalsandpharmacies.Model.Location();
        progressView = (CircularProgressView) findViewById(R.id.progress_view);
        overlayDialog = new Dialog(MainActivity.this , android.R.style.Theme_Panel);         //!!!
        overlayDialog.setCancelable(false);

        String[] items = new String[]{"Hospital", "Pharmacy"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdownType.setAdapter(adapter);

        String[] r = new String[]{"1", "5", "10", "50", "100"};
        ArrayAdapter<String> adapterR = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, r);
        dropdownRadius.setAdapter(adapterR);

        googleApiClient = new GoogleApiClient.Builder(MainActivity.this)
                .addConnectionCallbacks(MainActivity.this)
                .addOnConnectionFailedListener(MainActivity.this)
                .addApi(LocationServices.API)
                .build();


        binfo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                startActivity(intent);
            }
        });

        bgetLocation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getPermissionsToOpenLocation();
            }
        });
        bsearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int typePosition = dropdownType.getSelectedItemPosition();
                switch (typePosition) {
                    case 0:
                        selectedType = "hospital";
                        break;
                    case 1:
                        selectedType = "pharmacy";
                        break;
                }
                int radiusPosition = dropdownRadius.getSelectedItemPosition();
                switch (radiusPosition) {
                    case 0:
                        realRadius = 1000;
                        break;
                    case 1:
                        realRadius = 5000;
                        break;
                    case 2:
                        realRadius = 10000;
                        break;
                    case 3:
                        realRadius = 50000;
                        break;
                    case 4:
                        realRadius = 100000;
                        break;
                }
                if(isNetworkAvailable()) {
                    progressView.setVisibility(View.VISIBLE);
                    progressView.startAnimation();
                    overlayDialog.show();
                    getData();
                }else{
                    Toast.makeText(MainActivity.this, "Network connection problem", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void getData(){
        url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+usersLocation.getLatitude()+","+usersLocation.getLongtitude()+"&radius="+realRadius+"&type="+selectedType+"&opennow=true&key=AIzaSyAdv663nwVLXdI1qdtiH5Ucb6spFSYAiZY";
        new Thread(new Runnable() {
            public void run() {
                try {
                    List<DistanceService> distanceService = new ArrayList<>();
                    UseJson gtJson = new UseJson(url);
                    String response = gtJson.useJsonString("");
                    if (!response.equals("null")) {
                        JSONObject object = new JSONObject(response);
                        jsonArray= object.getJSONArray("results");
                        JSONObject cObject;
                        double lati;
                        double longi;
                        double dist;
                        for (int n = 0; n < jsonArray.length(); n++) {
                            cObject = jsonArray.getJSONObject(n);
                            Service service = new Service();
                            lati = cObject.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                            longi = cObject.getJSONObject("geometry").getJSONObject("location").getDouble("lng");
                            serviceLocation.setLatitude(lati);
                            serviceLocation.setLongtitude(longi);
                            service.setLocation(serviceLocation);
                            dist = Double.parseDouble(df2.format(meterDistanceBetweenPoints(usersLocation.getLatitude(),usersLocation.getLongtitude(), lati, longi)/1000).replace(",","."));
                            if(!cObject.get("name").toString().equals("")){
                                service.setName(cObject.get("name").toString());
                            }
                            if(!cObject.get("vicinity").toString().equals("")){
                                service.setAddress(cObject.get("vicinity").toString());
                            }
                            service.setType(selectedType);
                            distanceService.add(new DistanceService(dist, service));
                        }
                        sortDistanceService(distanceService);
                        user.setDistanceService(distanceService);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(!((jsonArray.length()) == 0)) {
                                progressView.setVisibility(View.GONE);
                                overlayDialog.cancel();
                                Intent intent = new Intent(MainActivity.this, ListOfServices.class);
                                intent.putExtra("user", user);
                                startActivity(intent);
                            }else{
                                Toast.makeText(MainActivity.this, "There is no service in this range. You can try again choosing different radius", Toast.LENGTH_LONG).show();
                                progressView.setVisibility(View.GONE);
                                overlayDialog.cancel();
                            }
                        }
                    });
                }
            }
        }).start();
    }

    public static void sortDistanceService(List<DistanceService> distanceService){
        Collections.sort(distanceService, new Comparator<DistanceService>() {
            public int compare(DistanceService distanceServiceOne, DistanceService distanceServiceTwo) {
                int lastDistanceCompare = distanceServiceOne.getDistance().compareTo(distanceServiceTwo.getDistance());
                if (lastDistanceCompare != 0) {
                    return lastDistanceCompare;
                } else {
                    return distanceServiceOne.getDistance().compareTo(distanceServiceTwo.getDistance());
                }
            }});
    }

    private double meterDistanceBetweenPoints(double lat_a, double lng_a, double lat_b, double lng_b) {
        double pk = (double) (180.f/Math.PI);

        double a1 = lat_a / pk;
        double a2 = lng_a / pk;
        double b1 = lat_b / pk;
        double b2 = lng_b / pk;

        double t1 = Math.cos(a1) * Math.cos(a2) * Math.cos(b1) * Math.cos(b2);
        double t2 = Math.cos(a1) * Math.sin(a2) * Math.cos(b1) * Math.sin(b2);
        double t3 = Math.sin(a1) * Math.sin(b1);
        double tt = Math.acos(t1 + t2 + t3);

        return 6366000 * tt;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (checkPlayServices()) {
            googleApiClient.connect();
        }else{
            tvCurrentLocation.setText("LOCATION ERROR CODE: CHECK_PLAY_SERVICES\nYour Google play services are not available");
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 6060);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        tvCurrentLocation.setText("LOCATION ERROR CODE: CONNECTION_SUSPENDED\nYour connection with Google play services has been suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        tvCurrentLocation.setText("LOCATION ERROR CODE: CONNECTION_FAILED\nYour connection with Google play services has been failed");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 6060: {
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    tvCurrentLocation.setText("LOCATION ERROR CODE: PERMISSION_GRANTED_FAILED\nYou have to check your's Application Managers Permission for this Application");
                }
            }
        }
    }

    private void getPermissionsToOpenLocation() {
        tvCurrentLocation.setText("");
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setNeedBle(true);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Location lastLocation = null;
                        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                        }
                        if (lastLocation != null) {

                            usersLocation.setLatitude(lastLocation.getLatitude());
                            usersLocation.setLongtitude(lastLocation.getLongitude());
                            user.setLocation(usersLocation);
                            new AwesomeSuccessDialog(MainActivity.this)
                                    .setTitle("Success!!!")
                                    .setMessage("Your Location is\nLatitude:  "+ usersLocation.getLatitude()+"\nLongitude: "+ usersLocation.getLongtitude())
                                    .setColoredCircle(R.color.dialogSuccessBackgroundColor)
                                    .setDialogIconAndColor(R.drawable.ic_success, R.color.white)
                                    .setCancelable(true)
                                    .setPositiveButtonText("ok")
                                    .setPositiveButtonbackgroundColor(R.color.dialogSuccessBackgroundColor)
                                    .setPositiveButtonTextColor(R.color.white)
                                    .setPositiveButtonClick(new Closure() {
                                        @Override
                                        public void exec() {
                                            //click
                                        }
                                    })
                                    .show();

                            tvCurrentLocation.setText("Latitude:  "+usersLocation.getLatitude()+"\nLongitude: "+usersLocation.getLongtitude());
                        }else{
                            new AwesomeErrorDialog(MainActivity.this)
                                    .setTitle("Something went wrong..")
                                    .setMessage("LOCATION ERROR CODE: PERMISSION_GRANTED_FAILED\nYou have to check your's Application Managers Permission for this Application")
                                    .setColoredCircle(R.color.dialogErrorBackgroundColor)
                                    .setDialogIconAndColor(R.drawable.ic_dialog_error, R.color.white)
                                    .setCancelable(true).setButtonText(getString(R.string.dialog_ok_button))
                                    .setButtonBackgroundColor(R.color.dialogErrorBackgroundColor)
                                    .setButtonText(getString(R.string.dialog_ok_button))
                                    .setErrorButtonClick(new Closure() {
                                        @Override
                                        public void exec() {
                                            // click
                                        }
                                    })
                                    .show();
                        }
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                        new AwesomeErrorDialog(MainActivity.this)
                                .setTitle("Something went wrong..")
                                .setMessage("LOCATION ERROR CODE: RESOLUTION_REQUIRED\nYou have to open your location in this device or check your's Application Managers Permission for this Application")
                                .setColoredCircle(R.color.dialogErrorBackgroundColor)
                                .setDialogIconAndColor(R.drawable.ic_dialog_error, R.color.white)
                                .setCancelable(true).setButtonText(getString(R.string.dialog_ok_button))
                                .setButtonBackgroundColor(R.color.dialogErrorBackgroundColor)
                                .setButtonText(getString(R.string.dialog_ok_button))
                                .setErrorButtonClick(new Closure() {
                                    @Override
                                    public void exec() {
                                        // click
                                    }
                                })
                                .show();
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        tvCurrentLocation.setText("LOCATION ERROR CODE: SETTINGS_CHANGE_UNAVAILABLE\nYou have to open your location in this device or check your's Application Managers Permission for this Application");
                        break;
                }
            }
        });
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            return false;
        }
        return true;
    }
}
