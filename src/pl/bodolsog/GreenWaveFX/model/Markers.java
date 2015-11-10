package pl.bodolsog.GreenWaveFX.model;


import java.util.HashMap;
import java.util.Map;

public class Markers {
    private double lat;
    private double lng;

    public void setLatLng(double lat, double lng){
        this.lat = lat;
        this.lng = lng;
    }

    public String getLatLng(){
        return "lat: "+String.valueOf(lat)+", lng: "+String.valueOf(lng);
    }
}
