package com.gatech.beatouch.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.ExternalFileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;

import com.gatech.beatouch.loaders.SongMapLoader;

import com.gatech.beatouch.entities.BaseMetadata;
import com.gatech.beatouch.entities.SongMapGroup;
import com.gatech.beatouch.entities.Metadata;

import com.gatech.beatouch.entities.SongMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Assets {

    public static AssetManager internalManager = new AssetManager(new InternalFileHandleResolver());
    public static AssetManager externalManager = new AssetManager(new ExternalFileHandleResolver());

    static {
        externalManager.setLoader(List.class, new SongMapLoader(new ExternalFileHandleResolver()));
    }

    public static final String SONGMAP_LOCATION = "beatouch/beatmaps/";

    public static final String SONGFILES_LOCATION = "beatouch/soundfiles/";

    public static SongMap selectedSongMap;
    public static SongMapGroup selectedGroup;

    public static TextureAtlas atlas;

    public static Skin menuSkin;

    public static Sound greatTapTone;
    public static Sound perfectTapTone;

    public static Sound perfectSwipeSound;

    public static BitmapFont font;
    public static BitmapFont songFont;

    public static Texture mainMenuBackgroundTexture;

    public static Texture page1Texture;
    public static Texture page2Texture;
    public static Texture page3Texture;
    public static Texture page4Texture;
    public static Texture page5Texture;

    public static Texture resultsPageTexture;
    public static Texture holdBG;

    public static Array<SongMapGroup> songGroup;

    // In here we'll put everything that needs to be loaded in this format:
    // manager.load("file location in assets", fileType.class);
    //
    // libGDX AssetManager currently supports: Pixmap, Texture, BitmapFont,
    //     TextureAtlas, TiledAtlas, TiledMapRenderer, Music and Sound.
    public static void queueLoading() {
        internalManager.load("textures/textures.pack.atlas", TextureAtlas.class);
        internalManager.load("hitsounds/tap_great.mp3", Sound.class);
        internalManager.load("hitsounds/tap_perfect.mp3", Sound.class);
        internalManager.load("bigimages/main_menu_background.jpg", Texture.class);

        internalManager.load("bigimages/page1.png", Texture.class);
        internalManager.load("bigimages/page2.png", Texture.class);
        internalManager.load("bigimages/page3.png", Texture.class);
        internalManager.load("bigimages/page4.png", Texture.class);
        internalManager.load("bigimages/page5.png", Texture.class);

        internalManager.load("images/hold_background.png", Texture.class);

        internalManager.load("textures/textures.pack.atlas", TextureAtlas.class);
        internalManager.load("bigimages/main_menu_background.jpg", Texture.class);
        internalManager.load("bigimages/results.png", Texture.class);
        internalManager.load("images/hold_background.png", Texture.class);


        internalManager.load("fonts/newfont3.fnt", BitmapFont.class);
        internalManager.load("fonts/font.fnt", BitmapFont.class);
        reloadSongmaps();
    }

    public static void reloadSongmaps() {
        if (Gdx.files.absolute(Gdx.files.getExternalStoragePath() + SONGMAP_LOCATION).exists()) {
            for (String fileName : Gdx.files.absolute(Gdx.files.getExternalStoragePath() + SONGMAP_LOCATION).file().list()) {
                String fullPath = Gdx.files.getExternalStoragePath() + SONGMAP_LOCATION + fileName;
                if (Gdx.files.absolute(fullPath).isDirectory() || (!fileName.endsWith(".json")))
                    continue;

                externalManager.load(SONGMAP_LOCATION + fileName, List.class);
            }
        } else {
            (Gdx.files.absolute(Gdx.files.getExternalStoragePath() + SONGMAP_LOCATION)).mkdirs();
            (Gdx.files.absolute(Gdx.files.getExternalStoragePath() + SONGFILES_LOCATION)).mkdirs();
        }
    }

    //In here we'll create our skin, so we only have to create it once.
    public static void setMenuSkin() {
        if (menuSkin == null)
            menuSkin = new Skin(Gdx.files.internal("skins/menuSkin.json"), internalManager.get("textures/textures.pack.atlas", TextureAtlas.class));
    }

    public static void setTextures() {
        if (atlas == null)
            atlas = internalManager.get("textures/textures.pack.atlas");

        if (mainMenuBackgroundTexture == null)
            mainMenuBackgroundTexture = internalManager.get("bigimages/main_menu_background.jpg");

        if (holdBG == null)
            holdBG = internalManager.get("images/hold_background.png");


        if (resultsPageTexture == null)
            resultsPageTexture = internalManager.get("bigimages/results.png");

        if(page1Texture==null)
            page1Texture = internalManager.get("bigimages/page1.png");

        if(page2Texture==null)
            page2Texture = internalManager.get("bigimages/page2.png");

        if(page3Texture==null)
            page3Texture = internalManager.get("bigimages/page3.png");

        if(page4Texture==null)
            page4Texture = internalManager.get("bigimages/page4.png");

        if(page5Texture==null)
            page5Texture = internalManager.get("bigimages/page5.png");
    }

    public static void setFonts() {
        if (font == null) {
            font = internalManager.get("fonts/newfont3.fnt");
        }
        if (songFont == null) {
            songFont= internalManager.get("fonts/font.fnt");
        }

    }

    public static void setHitsounds() {
        if (greatTapTone == null)
            greatTapTone = internalManager.get("hitsounds/tap_great.mp3");
        if (perfectTapTone == null)
            perfectTapTone = internalManager.get("hitsounds/tap_perfect.mp3");

    }

    @SuppressWarnings("unchecked")
    public static void setSongs() {
        if (songGroup == null) {
            songGroup = new Array<>();
        } else {
            songGroup.clear();
        }

        Array<String> assets = externalManager.getAssetNames();
        Map<Long, SongMapGroup> groupMap = new HashMap<>();

        for (String string : assets) {
            List<SongMap> songMaps = externalManager.get(string, List.class);
            if (!songMaps.isEmpty()) {
                Metadata metadata = songMaps.get(0).metadata;
                Long liveId = metadata.id;
                if (groupMap.get(liveId) == null) {
                    SongMapGroup group = new SongMapGroup();
                    group.metadata = new BaseMetadata();
                    group.metadata.songFile = metadata.songFile;
                    group.metadata.songName = metadata.songName;
                    group.beatmaps = new Array<>();
                    groupMap.put(liveId, group);
                }

                SongMapGroup group = groupMap.get(liveId);
                for (SongMap songMap : songMaps) {
                    group.beatmaps.add(songMap);
                }
                group.beatmaps.sort();
            }
        }
        for (Long liveId : groupMap.keySet()) {
            songGroup.add(groupMap.get(liveId));
        }
        songGroup.sort();
    }

    public static boolean update() {

        return internalManager.update() && externalManager.update();

    }

    public static float getProgress() {
        return (internalManager.getProgress() + externalManager.getProgress()) / 2;
    }
}
