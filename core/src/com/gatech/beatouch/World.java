package com.gatech.beatouch;

import com.badlogic.gdx.utils.Array;
import com.gatech.beatouch.assets.Assets;
import com.gatech.beatouch.configuration.GlobalVariables;
import com.gatech.beatouch.entities.Note;
import com.gatech.beatouch.objects.AccuracyMarker;
import com.gatech.beatouch.objects.Circle;
import com.gatech.beatouch.objects.TapRegion;
import com.gatech.beatouch.objects.AccuracyPopup;
import com.gatech.beatouch.utilities.SongUtilities;

public class World {
    int width;
    int height;

    public int combo;
    //Zhou
    public float length;
    public boolean started;
    public boolean paused;
    public int offsetX;
    public int offsetY;

    private Array<AccuracyMarker> accuracyMarkers;
    private Array<AccuracyPopup> accuracyPopups;

    Array<TapRegion> tapZones = new Array<>();
    Array<Circle> circles = new Array<>();

    public float delay;

    public World() {
        createWorld();
    }

    private void createWorld() {
        float x = 0f;
        float y = 0f;

        float h = 400;
        float w = 600;

        float radius = h * 0.065f;

        Double noteSpeed = SongUtilities.getSpeedFromConfig(GlobalVariables.noteSpeed) / 1000.0;

        delay = Assets.selectedSongMap.metadata.leadIn != null ? Assets.selectedSongMap.metadata.leadIn : 0f;

        if (delay < noteSpeed) {
            delay += noteSpeed;
        }

        for (Note notesInfo : Assets.selectedSongMap.notes) {

            // we create a copy which is modified based on the live options - speed / a-b repeat
            Note copy = copy(notesInfo);

            if (GlobalVariables.playbackRate != null) {
                copy.timing = copy.timing / GlobalVariables.playbackRate;

            }

            x = (copy.endPos - 3) * radius * 4;
            Circle mark = new Circle(x, 0, copy, noteSpeed, delay);
            circles.add(mark);
        }
//        System.out.println("Loaded: " + circles.size + " notes");

        linkCircles(circles);
        linkSyncCircles(circles);

        circles.sort();

        int zoneId = 1;
        tapZones = new Array<>();

        for (int i = 0; i < 11; i++) {
            if (i % 2 == 0)
                continue;

            x = (i * 2 - 10) * radius;

            TapRegion zone = new TapRegion(x, -249.0f, zoneId++);
            tapZones.add(zone);
        }
        tapZones.sort();
        this.accuracyMarkers = new Array<>();
        this.accuracyPopups = new Array<>();
        paused = false;
    }

    private Note copy(Note notesInfo) {
        Note copy = new Note();
        copy.status = notesInfo.status;
        copy.timing = notesInfo.timing;
        copy.id = notesInfo.id;
        copy.nextNoteId = notesInfo.nextNoteId;
        copy.prevNoteId = notesInfo.prevNoteId;
        copy.endPos = notesInfo.endPos;
        copy.startPos = notesInfo.startPos;
        copy.groupId = notesInfo.groupId;
        copy.sync = notesInfo.sync;
        copy.type = notesInfo.type;
        return copy;
    }

    private void linkCircles(Array<Circle> circles) {
        for (int i = 0; i < circles.size; i++) {
            Circle current = circles.get(i);
            if (current.note.nextNoteId == 0)
                continue;

            Circle next = findNext(circles, current.note.nextNoteId);
        }
    }

    private Circle findNext(Array<Circle> circles, Long nextNoteId) {
        for (Circle circle : circles) {
            if (circle.note.id.equals(nextNoteId))
                return circle;
        }
        return null;
    }

    private void linkSyncCircles(Array<Circle> circles) {
        for (int i = 0; i < circles.size; i++) {
            Circle mark = circles.get(i);
            if (mark.note.sync != 0) {
                Circle next = findNextSync(circles, mark.note.timing, mark.note.id);
                if (next != null) {
                    mark.nextSyncNote = next;
                }
            }
        }

    }

    private Circle findNextSync(Array<Circle> circles, Double timing, Long id) {
        for (int i = 0; i < circles.size; i++) {
            Circle circle = circles.get(i);
            if (circle.note.timing.equals(timing) && circle.note.id > id) {
                return circle;
            }
        }
        return null;
    }

    public Array<TapRegion> getTapZones() {
        return tapZones;
    }

    public Array<Circle> getCircles() {
        return circles;
    }

    public Array<AccuracyMarker> getAccuracyMarkers() {
        return accuracyMarkers;
    }

    public Array<AccuracyPopup> getAccuracyPopups() {
        return accuracyPopups;
    }

    public void setSize(int width, int height, int offsetX, int offsetY) {
        this.width = width;
        this.height = height;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }
}
