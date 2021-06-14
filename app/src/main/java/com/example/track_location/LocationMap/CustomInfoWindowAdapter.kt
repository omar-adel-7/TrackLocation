package com.example.track_location.LocationMap

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.example.track_location.R
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import com.google.android.gms.maps.model.Marker

class CustomInfoWindowAdapter(var mInflater: LayoutInflater) : InfoWindowAdapter {
    // This defines the contents within the info window based on the marker
    override fun getInfoContents(marker: Marker): View {
        // Getting view from the layout file
        val v = mInflater.inflate(R.layout.custom_info_window, null)
        // Populate fields
        val title = v.findViewById<View>(R.id.tv_info_window_title) as TextView
        title.text = marker.title
        val description = v.findViewById<View>(R.id.tv_info_window_description) as TextView
        //description.setText(marker.getSnippet());
        // Return info window contents
        return v
    }

    // This changes the frame of the info window; returning null uses the default frame.
    // This is just the border and arrow surrounding the contents specified above
    override fun getInfoWindow(marker: Marker): View? {
        return null
    }
}