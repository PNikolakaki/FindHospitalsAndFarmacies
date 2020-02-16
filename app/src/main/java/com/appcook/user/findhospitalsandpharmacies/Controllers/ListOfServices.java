package com.appcook.user.findhospitalsandpharmacies.Controllers;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.appcook.user.findhospitalsandpharmacies.Model.User;
import com.appcook.user.findhospitalsandpharmacies.R;

import java.io.Serializable;

public class ListOfServices extends AppCompatActivity implements Serializable{

    public User user;
    private ListView list;
    private ListOfServicesAdapter adapter;

    private void initialization() {
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        adapter = new ListOfServicesAdapter(ListOfServices.this, R.layout.item_list, user.getDistanceService());
        list = (ListView) findViewById(R.id.LVServices);
        list.setAdapter(adapter);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_services);

        initialization();


    }
}
