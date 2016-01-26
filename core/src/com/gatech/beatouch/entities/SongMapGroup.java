package com.gatech.beatouch.entities;

import com.badlogic.gdx.utils.Array;
import com.gatech.beatouch.configuration.GlobalVariables;
import com.gatech.beatouch.utilities.SongUtilities;

public class SongMapGroup implements Comparable<SongMapGroup>{
    public BaseMetadata metadata;
    public Array<SongMap> beatmaps;

    public String toString()
    {
        return metadata.songName.replaceAll("\\\\n", " ");
    }

    @Override
    public int compareTo(SongMapGroup o) {
        if (GlobalVariables.sortOrder == SongUtilities.SORTING_MODE_ASCENDING) {
            if (GlobalVariables.sortMode == SongUtilities.SORTING_MODE_FILE_NAME)
                return metadata.songFile.compareTo(o.metadata.songFile);
            if (GlobalVariables.sortMode == SongUtilities.SORTING_MODE_SONG_NAME)
                return metadata.songName.compareTo(o.metadata.songName);
            if (GlobalVariables.sortMode == SongUtilities.SORTING_MODE_SONG_ID)
                return metadata.id.intValue() - o.metadata.id.intValue();
        }
        else if (GlobalVariables.sortOrder == SongUtilities.SORTING_MODE_DESCENDING){
            if (GlobalVariables.sortMode == SongUtilities.SORTING_MODE_FILE_NAME)
                return -metadata.songFile.compareTo(o.metadata.songFile);
            if (GlobalVariables.sortMode == SongUtilities.SORTING_MODE_SONG_NAME)
                return -metadata.songName.compareTo(o.metadata.songName);
            if (GlobalVariables.sortMode == SongUtilities.SORTING_MODE_SONG_ID)
                return -metadata.id.intValue() - o.metadata.id.intValue();
            return -Double.compare(metadata.duration, o.metadata.duration);
        }
        return Double.compare(metadata.duration, o.metadata.duration);
    }
}
