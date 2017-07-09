package com.tlahmann.gdg.controllers;

import com.tlahmann.gdg.Dance;
import de.looksgood.ani.easing.Easing;

/**
 * A class to hold all needed information for the variation of parameters in our animation
 */
public class CustomAnimation implements Comparable<CustomAnimation> {
    public float start;
    public float duration;
    public float value;
    public String params;
    public Easing mode;

    /**
     * Constructor of the CustomAnimation
     *
     * @param start    start cue of the animation in seconds
     * @param duration the duration of the animation in seconds
     * @param params   what parameter should be changed
     * @param value    what the finish value of the animation should be
     * @param mode     the animation mode as Easing
     */
    CustomAnimation(float start, float duration, String params, float value, Easing mode) {
        this.start = start;
        this.duration = duration;
        this.params = params;
        this.value = value;
        this.mode = mode;
    }

    @Override
    public int compareTo(CustomAnimation o) {
        if (this.start < o.start) {
            return -1;
        } else if (this.start > o.start) {
            return 1;
        }
        return 0;
    }
}
