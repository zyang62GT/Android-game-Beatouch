package com.gatech.beatouch.loaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.gatech.beatouch.entities.SongMap;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class SongMapLoader extends AsynchronousAssetLoader<List, SongMapLoader.BeatmapParameter> {
    private List<SongMap> songMaps;

    public SongMapLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, BeatmapParameter parameter) {
        songMaps = new ArrayList<>();
        if (fileName.endsWith(".json")) {
            loadAsyncStandard(manager, fileName, file, parameter);
        }
    }

    private void loadAsyncStandard(AssetManager manager, String fileName, FileHandle file, BeatmapParameter parameter) {

        FileHandle handle = resolve(fileName);
        String jsonDefinition = handle.readString("UTF-8");
        SongMap info;
        try {
            info = new Gson().fromJson(jsonDefinition, SongMap.class);
            info.metadata.fileName = fileName;
            songMaps.add(info);
        } catch (Exception e) {
            // something went wrong.
            e.printStackTrace();
            Gdx.app.error("FILE_LOAD", "Failed to load beatmap from file: "+ fileName);
        }
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, BeatmapParameter parameter) {
        return null;
    }

    @Override
    public List<SongMap> loadSync(AssetManager manager, String fileName, FileHandle file, BeatmapParameter parameter) {
        return songMaps;
    }

    public class BeatmapParameter extends AssetLoaderParameters<List> {
    }
}
