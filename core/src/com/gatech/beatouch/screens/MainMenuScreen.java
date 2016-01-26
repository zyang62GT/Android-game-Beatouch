package com.gatech.beatouch.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.gatech.beatouch.assets.Assets;
import com.gatech.beatouch.configuration.GlobalVariables;

public class MainMenuScreen implements Screen, InputProcessor {
    private Stage stage = new Stage();
    private Image mainMenuBackgroundImage = new Image(Assets.mainMenuBackgroundTexture);
    private Table table = new Table();

    private TextButton buttonPlay = new TextButton("Play", Assets.menuSkin, "item1");
    private TextButton buttonTutorial = new TextButton("Tutorials", Assets.menuSkin, "item1");
    private TextButton buttonQuit = new TextButton("Exit Game", Assets.menuSkin, "item1");

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {

        mainMenuBackgroundImage.setSize(stage.getWidth(), stage.getHeight());
        stage.addActor(mainMenuBackgroundImage);
        buttonPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //Same way we moved here from the Splash Screen
                //We set it to new Splash because we got no other screens
                //otherwise you put the screen there where you want to go
                ((Game) Gdx.app.getApplicationListener()).setScreen(new SongSelectScreen());
            }
        });
        buttonTutorial.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new TutorialScreen1());
            }
        });
        buttonQuit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.exit(0);

            }
        });

        //The elements are displayed in the order you add them.
        //The first appear on top, the last at the bottom.
        // title font scale = 1 for a 720 height
        float fontScale = stage.getHeight() / GlobalVariables.BASE_HEIGHT;



        Label versionLabel = new Label("VERSION: " + GlobalVariables.appVersionName, Assets.menuSkin, "version_label");
        versionLabel.setFontScale(fontScale / 2f);
        versionLabel.setX(stage.getWidth() * 0.075f);
        versionLabel.setY(stage.getHeight() * 0.9f);




        buttonPlay.getLabel().setFontScale(fontScale);
        buttonTutorial.getLabel().setFontScale(fontScale);
        buttonQuit.getLabel().setFontScale(fontScale);
        table.add(buttonPlay).size(stage.getWidth() * 0.2f, stage.getHeight() * 0.10f).padBottom(stage.getHeight() * 0.04f).padTop(50).row();
        table.add(buttonTutorial).size(stage.getWidth() * 0.2f, stage.getHeight() * 0.10f).padBottom(stage.getHeight() * 0.04f).row();
        table.add(buttonQuit).size(stage.getWidth() * 0.2f, stage.getHeight() * 0.10f).padBottom(stage.getHeight() * 0.04f).row();

        table.setFillParent(true);
        stage.addActor(table);

        InputMultiplexer impx = new InputMultiplexer();
        impx.addProcessor(this);
        impx.addProcessor(stage);

        Gdx.input.setInputProcessor(impx);
        Gdx.input.setCatchBackKey(true);
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
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