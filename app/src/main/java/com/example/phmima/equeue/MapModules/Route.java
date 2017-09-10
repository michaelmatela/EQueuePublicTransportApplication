package com.example.phmima.equeue.MapModules;


import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by phmima on 9/10/2017.
 */

public class Route {
    public Distance distance;
    public Duration duration;
    public String endAddress;
    public LatLng endLocation;
    public String startAddress;
    public LatLng startLocation;

    public List<LatLng> points;
}