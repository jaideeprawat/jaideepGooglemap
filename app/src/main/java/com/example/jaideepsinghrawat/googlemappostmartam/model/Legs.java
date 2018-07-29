package com.example.jaideepsinghrawat.googlemappostmartam.model;

import com.example.jaideepsinghrawat.googlemappostmartam.DirectionConverter;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;

public class Legs {
    private Duration duration;

    private Distance distance;

    private End_location end_location;

    private String start_address;

    private String end_address;

    private Start_location start_location;

    private String[] traffic_speed_entry;

    private String[] via_waypoint;

    private Steps[] steps;

    public Duration getDuration ()
    {
        return duration;
    }

    public void setDuration (Duration duration)
    {
        this.duration = duration;
    }

    public Distance getDistance ()
    {
        return distance;
    }

    public void setDistance (Distance distance)
    {
        this.distance = distance;
    }

    public End_location getEnd_location ()
    {
        return end_location;
    }

    public void setEnd_location (End_location end_location)
    {
        this.end_location = end_location;
    }

    public String getStart_address ()
    {
        return start_address;
    }

    public void setStart_address (String start_address)
    {
        this.start_address = start_address;
    }

    public String getEnd_address ()
    {
        return end_address;
    }

    public void setEnd_address (String end_address)
    {
        this.end_address = end_address;
    }

    public Start_location getStart_location ()
    {
        return start_location;
    }

    public void setStart_location (Start_location start_location)
    {
        this.start_location = start_location;
    }

    public String[] getTraffic_speed_entry ()
    {
        return traffic_speed_entry;
    }

    public void setTraffic_speed_entry (String[] traffic_speed_entry)
    {
        this.traffic_speed_entry = traffic_speed_entry;
    }

    public String[] getVia_waypoint ()
    {
        return via_waypoint;
    }

    public void setVia_waypoint (String[] via_waypoint)
    {
        this.via_waypoint = via_waypoint;
    }

    public Steps[] getSteps ()
    {
        return steps;
    }

    public void setSteps (Steps[] steps)
    {
        this.steps = steps;
    }
    public ArrayList<LatLng> getDirectionPoint() {
        return DirectionConverter.getDirectionPoint(Arrays.asList(steps));
    }
    @Override
    public String toString()
    {
        return "ClassPojo [duration = "+duration+", distance = "+distance+", end_location = "+end_location+", start_address = "+start_address+", end_address = "+end_address+", start_location = "+start_location+", traffic_speed_entry = "+traffic_speed_entry+", via_waypoint = "+via_waypoint+", steps = "+steps+"]";
    }
}
