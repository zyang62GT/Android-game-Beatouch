package com.gatech.beatouch.entities;

import java.util.List;

public class SongMap implements Comparable<SongMap>{
    public Metadata metadata;
    public List<Note> notes;

    @Override
    public int compareTo(SongMap o) {
        if (metadata == null)
            return 1;
        if (o.metadata == null)
            return -1;
        return metadata.compareTo(o.metadata);
    }

}
