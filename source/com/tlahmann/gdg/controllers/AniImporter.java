package com.tlahmann.gdg.controllers;

import de.looksgood.ani.AniConstants;
import de.looksgood.ani.easing.Easing;
import processing.core.PApplet;
import processing.data.JSONArray;
import processing.data.JSONObject;

import java.util.ArrayList;

public abstract class AniImporter {
    public static ArrayList<CustomAnimation> importAnimation(PApplet parent, String filePath, String type) {
        JSONObject file = parent.loadJSONObject(filePath);
        JSONArray backgroundAnimations = file.getJSONArray(type);

        ArrayList<CustomAnimation> anis = new ArrayList<>();
        for (int i = 0; i < backgroundAnimations.size(); i++) {
            JSONObject o = backgroundAnimations.getJSONObject(i);
            float start = o.getFloat("start");
            float duration = o.getFloat("duration");
            float value = o.getFloat("value");
            String easingString = o.getString("easing");
            Easing easing = determineEasing(easingString);

            anis.add(new CustomAnimation(start, duration, type, value, easing));
        }

        return anis;
    }

    private static Easing determineEasing(String ease) {
        Easing e;
        switch (ease) {
            case "sine_in":
                e = AniConstants.SINE_IN;
                break;
            case "sine_out":
                e = AniConstants.SINE_OUT;
                break;
            case "cubic_out":
                e = AniConstants.CUBIC_OUT;
                break;
            case "bounce_out":
                e = AniConstants.BOUNCE_OUT;
                break;
            case "bounce_in_out":
                e = AniConstants.BOUNCE_IN_OUT;
                break;
            case "elastic_out":
                e = AniConstants.ELASTIC_OUT;
                break;
            case "elastic_in_out":
                e = AniConstants.ELASTIC_IN_OUT;
                break;
            default:
                e = AniConstants.LINEAR;
                break;
        }
        return e;
    }
}
