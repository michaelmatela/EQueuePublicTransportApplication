package com.example.phmima.equeue.MapModules;


import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import com.example.phmima.equeue.MapModules.Route;


public interface DirectionFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> route);
}