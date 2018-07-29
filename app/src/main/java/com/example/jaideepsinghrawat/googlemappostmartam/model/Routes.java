package com.example.jaideepsinghrawat.googlemappostmartam.model;

public class Routes {
    private String summary;

    private Bounds bounds;

    private String copyrights;

    private String[] waypoint_order;

    private Legs[] legs;

    private String[] warnings;

    private Overview_polyline overview_polyline;

    public String getSummary ()
    {
        return summary;
    }

    public void setSummary (String summary)
    {
        this.summary = summary;
    }

    public Bounds getBounds ()
    {
        return bounds;
    }

    public void setBounds (Bounds bounds)
    {
        this.bounds = bounds;
    }

    public String getCopyrights ()
    {
        return copyrights;
    }

    public void setCopyrights (String copyrights)
    {
        this.copyrights = copyrights;
    }

    public String[] getWaypoint_order ()
    {
        return waypoint_order;
    }

    public void setWaypoint_order (String[] waypoint_order)
    {
        this.waypoint_order = waypoint_order;
    }

    public Legs[] getLegs ()
    {
        return legs;
    }

    public void setLegs (Legs[] legs)
    {
        this.legs = legs;
    }

    public String[] getWarnings ()
    {
        return warnings;
    }

    public void setWarnings (String[] warnings)
    {
        this.warnings = warnings;
    }

    public Overview_polyline getOverview_polyline ()
    {
        return overview_polyline;
    }

    public void setOverview_polyline (Overview_polyline overview_polyline)
    {
        this.overview_polyline = overview_polyline;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [summary = "+summary+", bounds = "+bounds+", copyrights = "+copyrights+", waypoint_order = "+waypoint_order+", legs = "+legs+", warnings = "+warnings+", overview_polyline = "+overview_polyline+"]";
    }
}
