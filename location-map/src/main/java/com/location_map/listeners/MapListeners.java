package com.location_map.listeners;

import android.location.Location;


public interface MapListeners {

    void prepareMapClicks();
    void gotLocationChanged(Location newLocation);
    void findLocation();

}
