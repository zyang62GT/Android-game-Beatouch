package com.gatech.beatouch.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.gatech.beatouch.assets.Assets;
import com.gatech.beatouch.configuration.GlobalVariables;
import com.gatech.beatouch.entities.SongMap;
import com.gatech.beatouch.entities.SongMapGroup;

@SuppressWarnings("unchecked")
public class SongSelectScreen implements Screen, InputProcessor {

    private Stage stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
    private List<SongMapGroup> availableSongList = new List<>(Assets.menuSkin, "diff_list");
    private ScrollPane SongListPanel = new ScrollPane(null, Assets.menuSkin);
    private List<SongMap> songList = new List<>(Assets.menuSkin, "diff_list");
    private Table table = new Table();
    private TextButton nextButton = new TextButton("Next", Assets.menuSkin, "item1");
    private TextButton backButton = new TextButton("Back", Assets.menuSkin, "item1");
    private Image backgroundImage = new Image(Assets.mainMenuBackgroundTexture);

    @Override
    public void show() {
        float scaleFactor = stage.getHeight() / GlobalVariables.BASE_HEIGHT;
        backgroundImage.setSize(stage.getWidth(), stage.getHeight());
        stage.addActor(backgroundImage);

        Assets.songGroup.sort();
        availableSongList.setItems(Assets.songGroup);

        if (Assets.selectedGroup != null) {
            availableSongList.setSelected(Assets.selectedGroup);
            songList.setItems(Assets.selectedGroup.beatmaps);
        } else {
            if (availableSongList.getItems().size != 0)
            {
                Assets.selectedGroup = availableSongList.getItems().get(0);
                songList.setItems(Assets.selectedGroup.beatmaps);
            }

        }

        availableSongList.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SongMapGroup previousGroup = Assets.selectedGroup;
                SongMapGroup newSelected = (SongMapGroup) ((List) actor).getSelected();
                if (previousGroup == newSelected) {
                    // if the same group was selected we ignore it
                    return;
                }

                Assets.selectedGroup = newSelected;
                songList.setItems(newSelected.beatmaps);
            }
        });

        if (Assets.selectedSongMap != null) {
            songList.setSelected(Assets.selectedSongMap);
        } else {
            songList.setSelected(songList.getItems().size == 0 ? null : songList.getItems().first());
        }
        nextButton.getLabel().setFontScale(scaleFactor);
        backButton.getLabel().setFontScale(scaleFactor);

        SongListPanel.setWidget(availableSongList);
        SongListPanel.setWidth(stage.getWidth());

        table.add(SongListPanel).colspan(3).size(stage.getWidth() * 0.87f, stage.getHeight() * 0.49f).padBottom(stage.getHeight() * 0.01f).row();
        //table.add(diffListPane).colspan(3).size(stage.getWidth() * 0.87f, stage.getHeight() * 0.23f).padBottom(stage.getHeight() * 0.01f).padTop(stage.getHeight() * 0.01f).row();
        table.setWidth(stage.getWidth());
        table.setHeight(stage.getHeight());

        backButton.addListener((new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Assets.selectedGroup = availableSongList.getSelected();
                Assets.selectedSongMap = songList.getSelected();
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen());
            }
        }));
        nextButton.addListener((new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (songList.getSelected() == null) {
                    return;
                }
                Assets.selectedSongMap = songList.getSelected();
                 ((Game) Gdx.app.getApplicationListener()).setScreen(new SongScreen());
            }
        }));
        table.add(backButton).size(stage.getWidth() * 0.87f / 2, stage.getHeight() * 0.12f);
        table.add(nextButton).size(stage.getWidth() * 0.87f / 2, stage.getHeight() * 0.12f);
        stage.addActor(table);

        InputMultiplexer impx = new InputMultiplexer();
        impx.addProcessor(this);
        impx.addProcessor(stage);

        Gdx.input.setInputProcessor(impx);
        Gdx.input.setCatchBackKey(true);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        SongListPanel.act(delta);
        stage.act();
        stage.draw();
    }


    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE) {
            Assets.selectedSongMap = songList.getSelected();
            Assets.selectedGroup = availableSongList.getSelected();
            ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen());
            // do nothing
            return true;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

}
