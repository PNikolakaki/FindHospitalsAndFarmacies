package com.appcook.user.findhospitalsandpharmacies.Controllers;

import android.app.Activity;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.appcook.user.findhospitalsandpharmacies.Model.DistanceService;
import com.appcook.user.findhospitalsandpharmacies.Model.Service;
import com.appcook.user.findhospitalsandpharmacies.R;

import java.util.List;

public class ListOfServicesAdapter extends ArrayAdapter<DistanceService>{

    private Activity activity;
    private int resourceId;
    private List<DistanceService> lista;
    private ViewHolder holder;

    public ListOfServicesAdapter(Activity activity, int resourceId, List<DistanceService> lista) {
        super(activity, resourceId, lista);
        this.activity = activity;
        this.resourceId = resourceId;
        this.lista = lista;
    }

    public View getView(int position, View view, ViewGroup parent) {

        View itemView = view;
        if (itemView == null) {
            itemView = this.activity.getLayoutInflater().inflate(R.layout.item_list, parent, false);
            holder = new ListOfServicesAdapter.ViewHolder();
            holder.tvservice = (TextView) itemView.findViewById(R.id.tvservice);
            holder.tvdistance = (TextView) itemView.findViewById(R.id.tvdistance);
            itemView.setTag(holder);
        } else {
            holder = (ViewHolder) itemView.getTag();
        }

        String name = this.lista.get(position).getService().getName();
        String address = this.lista.get(position).getService().getAddress();
        String dist = this.lista.get(position).getDistance().toString();

        holder.tvservice.setText(Html.fromHtml("<font color='#55b5d2'>"+name+"</font><br/>" + address));
        holder.tvdistance.setText(Html.fromHtml(dist+"<small>Km</small>"));

        return itemView;
    }

    private class ViewHolder {
        private TextView tvservice;
        private TextView tvdistance;

    }
}
