package com.gatech.beatouch.entities;


import com.gatech.beatouch.configuration.GlobalVariables;
import com.gatech.beatouch.utilities.Accuracy;
import com.gatech.beatouch.utilities.SongUtilities;

public class Results {
    public static Integer combo;
    public static float accuracy;
    public static int miss;
    public static int greats;
    public static int perfects;
    public static int bonus;
    public static int total;
    public static float normalizedAccuracy;

    public static void clear() {
        combo = 0;
        accuracy = 0;
        miss = 0;
        greats = 0;
        perfects = 0;
        bonus = 0;
        total = 0;
        normalizedAccuracy = 0;
    }


    public static float getAccuracyMultiplierForAccuracy(Accuracy accuracy) {
        if (accuracy == Accuracy.PERFECT) {
            return 1.0f;
        }
        if (accuracy == Accuracy.GREAT) {
            return 0.5f;
        }
        //Zhou
        return 0f;
    }

    public static Accuracy getAccuracyFor(float timing) {
        // Perfect
        if (Math.abs(timing) < SongUtilities.overallDiffPerfect[GlobalVariables.overallDifficulty] / 1000) {
            return Accuracy.PERFECT;
        }
        if (Math.abs(timing) < SongUtilities.overallDiffGreat[GlobalVariables.overallDifficulty]/ 1000) {
            return Accuracy.GREAT;
        }
        return Accuracy.MISS;
    }

    // holds and swipes have bigger windows
    public static Accuracy getAccuracyForSwipesAndHolds(float timing) {
        return getAccuracyFor(timing * SWIPE_HOLD_MULTIPLIER);
    }

    public final static float SWIPE_HOLD_MULTIPLIER = 0.5f;
}
