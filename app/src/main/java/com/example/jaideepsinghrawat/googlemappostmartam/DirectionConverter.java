/*

Copyright 2015 Akexorcist

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

*/

package com.example.jaideepsinghrawat.googlemappostmartam;

import android.content.Context;
import android.util.DisplayMetrics;

import com.example.jaideepsinghrawat.googlemappostmartam.model.Steps;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Akexorcist on 11/29/15 AD.
 */
public class DirectionConverter {
    public static ArrayList<LatLng> getDirectionPoint(List<Steps> stepList) {
        ArrayList<LatLng> directionPointList = new ArrayList<>();
        if (stepList != null && stepList.size() > 0) {
            for (Steps step : stepList) {
                convertStepToPosition(step, directionPointList);
            }
        }
        return directionPointList;
    }

    private static void convertStepToPosition(Steps step, ArrayList<LatLng> directionPointList) {
        // Get start location
        directionPointList.add(step.getStart_location().getCoordination());

        // Get encoded points location
        if (step.getPolyline() != null) {
            List<LatLng> decodedPointList = step.getPolyline().getPointList();
            if (decodedPointList != null && decodedPointList.size() > 0) {
                for (LatLng position : decodedPointList) {
                    directionPointList.add(position);
                }
            }
        }

        // Get end location
        directionPointList.add(step.getEnd_location().getCoordination());
    }

    public static ArrayList<LatLng> getSectionPoint(List<Steps> stepList) {
        ArrayList<LatLng> directionPointList = new ArrayList<>();
        if (stepList != null && stepList.size() > 0) {
            // Get start location only first position
            directionPointList.add(stepList.get(0).getStart_location().getCoordination());
            for (Steps step : stepList) {
                // Get end location
                directionPointList.add(step.getEnd_location().getCoordination());
            }
        }
        return directionPointList;
    }

    public static PolylineOptions createPolyline(Context context, ArrayList<LatLng> locationList, int width, int color) {
        PolylineOptions rectLine = new PolylineOptions().width(dpToPx(context, width)).color(color).geodesic(true);
        for (LatLng location : locationList) {
            rectLine.add(location);
        }
        return rectLine;
    }

    public static ArrayList<PolylineOptions> createTransitPolyline(Context context, List<Steps> stepList, int transitWidth, int transitColor, int walkingWidth, int walkingColor) {
        ArrayList<PolylineOptions> polylineOptionsList = new ArrayList<>();
        if (stepList != null && stepList.size() > 0) {
            for (Steps step : stepList) {
                ArrayList<LatLng> directionPointList = new ArrayList<>();
                convertStepToPosition(step, directionPointList);
                if (stepList != null && stepList.size() > 0) {
                    polylineOptionsList.add(createPolyline(context, directionPointList, walkingWidth, walkingColor));
                } else {
                    polylineOptionsList.add(createPolyline(context, directionPointList, transitWidth, transitColor));
                }
            }
        }
        return polylineOptionsList;
    }

    private static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}
