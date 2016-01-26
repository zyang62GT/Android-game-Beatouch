package com.gatech.beatouch.objects;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.gatech.beatouch.assets.Assets;
import com.gatech.beatouch.configuration.GlobalVariables;
import com.gatech.beatouch.entities.Note;
import com.gatech.beatouch.entities.Results;
import com.gatech.beatouch.utilities.Accuracy;
import com.gatech.beatouch.utilities.SongUtilities;

public class Circle implements Comparable<Circle> {

    public Note note;

    public Vector2 origin = new Vector2();
    public Vector2 position = new Vector2();
    Vector2 velocity = new Vector2();

    public boolean hold;
    public Long destination;
    Double speed;
    public Circle nextNote;
    public Circle previousNote;
    public Circle nextSyncNote;

    float spawnTime;
    float despawnTime;
    float startWaitTime;
    float endWaitTime;

    float size;

    public float hitTime;
    float previousTime;
    long previousSystemTime;

    public boolean visible;
    public boolean holding;
    public boolean waiting;
    public boolean soundPlayed;
    public boolean miss;

    public float alpha = 1f;

    public Accuracy accuracy;
    public boolean processed;

    public Circle(float x, float y, Note note, Double noteSpeed, float delay) {

        float timing = (float) (delay + note.timing * 1f + GlobalVariables.offset * 1f / 1000f);

        this.origin.x = x;
        this.origin.y = y;
        this.position.x = x;
        this.position.y = y;
        this.note = note;
        this.hold = (note.type & SongUtilities.NOTE_TYPE_HOLD) != 0;
        // position goes 1-5
        this.destination = note.endPos;
        this.speed = noteSpeed;
        this.spawnTime = (float) (timing - speed);
        this.startWaitTime = (float) (timing - (hold || !note.status.equals(SongUtilities.NOTE_NO_SWIPE) ? 2f : 1f) * SongUtilities.overallDiffBad[GlobalVariables.overallDifficulty] / 1000f);
        this.endWaitTime = (float) (timing + (hold || !note.status.equals(SongUtilities.NOTE_NO_SWIPE) ? 2f : 1f) * SongUtilities.overallDiffBad[GlobalVariables.overallDifficulty] / 1000f);
        this.despawnTime = timing * 1.0f;
        this.size = 1f;
        this.previousSystemTime = 0L;

        hitTime = -9f;
        previousTime = 0f;

        initializeVelocity();
        initializeStates();
    }

    private void initializeStates() {
        visible = false;
        holding = false;
        soundPlayed = false;
        miss = false;
    }

    public void setPreviousNote(Circle previousNote) {
        this.previousNote = previousNote;
        if (previousNote != null) {
            if (previousNote.hold && previousNote.previousNote == null) {
                this.startWaitTime = (float) (despawnTime - 2f * SongUtilities.overallDiffBad[GlobalVariables.overallDifficulty] / 1000f);
                this.endWaitTime = (float) (despawnTime + 2f * SongUtilities.overallDiffBad[GlobalVariables.overallDifficulty] / 1000f);

            }
        }

    }

    public void setNextNote(Circle nextNote) {
        this.nextNote = nextNote;
    }

    private void initializeVelocity() {
        velocity.x = 0;
        velocity.y = (float) (-249 / speed);
        //velocity.y /=2;
    }

    public void update(float time) {

        if (miss || (accuracy != null && !holding)) {
            if (visible) {
                visible = false;
            }
            return;
        }

        if (spawnTime <= time && despawnTime > time && !visible) {
            visible = true;
        }

        if (spawnTime >= time && visible)
            visible = false;

        if (visible && despawnTime <= time) {
            if (GlobalVariables.playHintSounds && !soundPlayed) {
                // hint sounds play at 50% of the volume
                if (note.status.equals(SongUtilities.NOTE_NO_SWIPE)) {
                    Assets.perfectTapTone.play(GlobalVariables.feedbackVolume / 200f);
                } else {
                    Assets.perfectSwipeSound.play(GlobalVariables.feedbackVolume / 200f);
                }
                soundPlayed = true;
            }

            if (holding) {
                alpha = 1f;
            } else {
                alpha = MathUtils.clamp((endWaitTime - time) / (endWaitTime - despawnTime), 0f, 1f);
                if (alpha == 0f)
                    visible = false;
            }
        }

        if (visible) {
            // TODO: implement parabolic movement of the notes towards the player and use the origin spot instead of spawning from the same lane (more SS-like)
            float scl = time - spawnTime;
            if (holding) {
                position.set(origin.cpy().x, origin.cpy().y - 249);
            } else
                position.set(origin.cpy().add(velocity.cpy().scl(scl)));
        }
        if (startWaitTime <= time && endWaitTime > time && !waiting && accuracy == null) {
            waiting = true;
        }

        processMiss(time);
        previousTime = time;
        previousSystemTime = System.currentTimeMillis();
    }


    private void processMiss(float time) {
        // miss if we miss the first note
        if (nextNote != null && hold && !holding && endWaitTime <= time && accuracy == null && !miss) {
            waiting = false;
            miss = true;
            accuracy = Accuracy.MISS;
            nextNote.miss = true;
            nextNote.accuracy = Accuracy.MISS;
            nextNote.processed = true;
            nextNote.waiting = false;
        } else if (nextNote == null && endWaitTime <= time && !miss && accuracy == null) {
            waiting = false;
            miss = true;
            accuracy = Accuracy.MISS;
        } else if (nextNote != null && !hold && endWaitTime <= time && accuracy == null && !miss) {
            waiting = false;
            miss = true;
            accuracy = Accuracy.MISS;
        }
        if (hold && !miss) {
            if (nextNote != null && nextNote.endWaitTime <= time && nextNote.accuracy == null) {
                miss = true;
                holding = false;
                waiting = false;
                accuracy = Accuracy.MISS;
            }
        }
    }

    public Accuracy hit() {
        if (previousNote != null && previousNote.hold)
            return Accuracy.NONE;

        float delta = (System.currentTimeMillis() - previousSystemTime) / 1000f;
        float hit = previousTime + delta - despawnTime - GlobalVariables.inputOffset / 1000f;

        Accuracy accuracy = hold ? Results.getAccuracyForSwipesAndHolds(hit) : Results.getAccuracyFor(hit);
        if (despawnTime > previousTime && accuracy == Accuracy.MISS) {
            return Accuracy.NONE;
        }
        hitTime = hit;
        waiting = false;
        if (hold) {
            hitTime *= Results.SWIPE_HOLD_MULTIPLIER;
            holding = true;
        } else {
            visible = false;
        }
        this.accuracy = accuracy;
        return accuracy;
    }

    public Accuracy release() {
        if (previousNote != null && previousNote.hold && !note.status.equals(SongUtilities.NOTE_NO_SWIPE)) {
            accuracy = Accuracy.MISS;
            miss = true;
            visible = false;
            previousNote.release();
            waiting = false;
            // only type 2 can gain from a release.
            // type 1 with status calls release on swipe
            return accuracy;
        }
        // RELEASE DOESN'T COUNT FOR HOLD START
        if (holding) {
            holding = false;
            visible = false;
        }
        if (nextNote != null)
            return Accuracy.NONE;

        float delta = (System.currentTimeMillis() - previousSystemTime) / 1000f;
        float hit = previousTime + delta - despawnTime - GlobalVariables.inputOffset / 1000f;
        accuracy = Results.getAccuracyForSwipesAndHolds(hit);
        previousNote.release();
        waiting = false;
        if (accuracy == Accuracy.MISS) {
            waiting = false;
            visible = false;
            miss = true;
            processed = true;
        } else {
            hitTime = hit;
            hitTime *= Results.SWIPE_HOLD_MULTIPLIER;
        }
        return accuracy;
    }

    public Accuracy swipeLeft() {
        // some songs have notes with type 2 and status != 0
        if (note.status.equals(SongUtilities.NOTE_NO_SWIPE) || note.status.equals(SongUtilities.NOTE_SWIPE_RIGHT)) {
            return Accuracy.NONE;
        }
        if (previousNote != null && previousNote.hold) {
            previousNote.release();
        }

        if (previousNote != null && previousNote.previousNote != null) {
            if (previousNote.previousNote.note.status.equals(note.status) && !previousNote.isDone()) {
                return Accuracy.NONE;
            }
        }
        float delta = (System.currentTimeMillis() - previousSystemTime) / 1000f;
        float hit = previousTime + delta - despawnTime - GlobalVariables.inputOffset / 1000f;
        Accuracy accuracy = Results.getAccuracyForSwipesAndHolds(hit);
        // If the note was tapped too early, we ignore the tap
        if (despawnTime > previousTime && accuracy == Accuracy.MISS) {
            return Accuracy.NONE;
        }
        hitTime = hit;
        hitTime *= Results.SWIPE_HOLD_MULTIPLIER;
        waiting = false;
        this.accuracy = accuracy;
        visible = false;
        return accuracy;
    }

    public Accuracy swipeRight() {
        if (note.status.equals(SongUtilities.NOTE_NO_SWIPE) || note.status.equals(SongUtilities.NOTE_SWIPE_LEFT)) {
            return Accuracy.NONE;
        }
        if (previousNote != null && previousNote.hold) {
            previousNote.release();
        }

        if (previousNote != null && previousNote.previousNote != null) {
            if (previousNote.previousNote.note.status.equals(note.status) && !previousNote.isDone()) {
                return Accuracy.NONE;
            }
        }

        float delta = (System.currentTimeMillis() - previousSystemTime) / 1000f;
        float hit = previousTime + delta - despawnTime - GlobalVariables.inputOffset / 1000f;
        Accuracy accuracy = Results.getAccuracyForSwipesAndHolds(hit);
        if (despawnTime > previousTime && accuracy == Accuracy.MISS) {
            return Accuracy.NONE;
        }
        hitTime = hit;
        hitTime *= Results.SWIPE_HOLD_MULTIPLIER;
        waiting = false;
        this.accuracy = accuracy;
        visible = false;
        return accuracy;
    }

    public boolean isDone() {
        return miss || (accuracy != null && !holding);
    }

    @Override
    public int compareTo(Circle o) {
        if (o == null)
            return 1;
        // if the notes have the same timing, sort them by destination
        if (note.timing.equals(o.note.timing)) {
            return Long.compare(note.endPos, o.note.endPos);
        }

        return Double.compare(note.timing, o.note.timing);
    }
}

