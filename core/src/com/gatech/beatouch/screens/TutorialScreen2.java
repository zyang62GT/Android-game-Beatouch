package com.gatech.beatouch.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.gatech.beatouch.assets.Assets;
import com.gatech.beatouch.configuration.GlobalVariables;
import com.gatech.beatouch.controller.SimpleDirectionGestureDetector;

/**
 * Created by zyang_000 on 2015/11/9.
 */
public class TutorialScreen2 implements Screen {
    Stage stage = new Stage();
    private Texture texture = Assets.page2Texture;
    private Image splashImage = new Image(texture);
    private Table table = new Table();

    public void show() {
        float fontScale = stage.getHeight() / GlobalVariables.BASE_HEIGHT;

        splashImage.setHeight(stage.getHeight());
        splashImage.setWidth(stage.getWidth());
        stage.addActor(splashImage);
        table.setFillParent(true);

        Gdx.input.setInputProcessor(stage);

    }
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.input.setInputProcessor(new SimpleDirectionGestureDetector(new SimpleDirectionGestureDetector.DirectionListener() {



            @Override
            public void onRight() {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new TutorialScreen3());

            }

            @Override
            public void onLeft() {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new TutorialScreen1());

            }


        }));
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
}
