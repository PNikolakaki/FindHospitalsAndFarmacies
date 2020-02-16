package com.appcook.user.findhospitalsandpharmacies.Controllers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.appcook.user.findhospitalsandpharmacies.R;

public class InfoActivity extends AppCompatActivity {

    private TextView tvgoogleLocation;
    private TextView tvgoogleData;
    private TextView tvourInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        tvgoogleLocation = (TextView) findViewById(R.id.tvgoogleLocation);
        tvgoogleData = (TextView) findViewById(R.id.tvgoogleData);
        tvourInfo = (TextView) findViewById(R.id.tvourinfo);

        tvgoogleLocation.setText(Html.fromHtml("<b><font color='#55b5d2'>About Location</font></b><br/><p>The user's location is determined " +
                "by Google's FusedLocationProviderApi.<br/> If the user doesn't want to give his location the application uses " +
                "a default location(Egnatias 144).</p>"));
        tvgoogleData.setText(Html.fromHtml("<b><font color='#55b5d2'>About Data</font></b><br/><p>The data for hospitals and pharmacies " +
                "are provided by the real Google Places API Web Service. The application presents only those services that are open" +
                " the current time. Services that do not specify opening hours in the Google Places database are not presented.</p>"));
        tvourInfo.setText(Html.fromHtml("<b><font color='#55b5d2'>About Us</font></b><br/><p>Application Name: Find Hospitals and Pharmacies\nDeveloped by:\nEuaggelia Papageorgiou and \nNikolakaki Pinelopi</p>"));

    }
}
