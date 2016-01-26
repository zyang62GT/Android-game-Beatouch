package com.gatech.beatouch.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.gatech.beatouch.World;
import com.gatech.beatouch.assets.Assets;
import com.gatech.beatouch.configuration.GlobalVariables;
import com.gatech.beatouch.controller.MainController;
import com.gatech.beatouch.entities.Results;
import com.gatech.beatouch.renderer.MainRenderer;


public class SongScreen implements Screen, InputProcessor {
    private World world;
    private MainRenderer renderer;
    private MainController controller;
    private int width;
    private int height;
    public boolean lgcombo;
    private TextButton buttonQuit = new TextButton("Play", Assets.menuSkin, "item1");




    @Override
    public void show() {
        world = new World();
        Results.clear();
        buttonQuit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //Same way we moved here from the Splash Screen
                //We set it to new Splash because we got no other screens
                //otherwise you put the screen there where you want to go
                ((Game) Gdx.app.getApplicationListener()).setScreen(new ResultsScreen());
            }
        });
        renderer = new MainRenderer(world);
        controller = new MainController(world);
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        float accelX1 = Gdx.input.getAccelerometerX();
        if( accelX1 > 5.0 & world.combo>10){
            GlobalVariables.comboMode=true;
//            System.out.println("11111");
        }else{
            GlobalVariables.comboMode=false;
//            System.out.println("22222");
        }
        controller.update(delta);
        renderer.render();
    }

    @Override
    public void resize(int width, int height) {
        // use height as base and force a 3:2 ratio
        int originalWidth = width;
        int newWidth = height * 3 / 2;

        int originalHeight = height;
        int newHeight = width * 2 / 3;

        // check which side should be shortened
        if (newWidth > width)
        {
            height = newHeight;
        }
        else
        {
            width = newWidth;
        }

        renderer.setSize(width, height, (originalWidth - width) / 2, (originalHeight - height) / 2);
        world.setSize(width, height, (originalWidth - width) /2, (originalHeight - height)/2);
        this.width = width;
        this.height = height;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public boolean keyDown(int keycode) {

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE) {
            // do nothing
            controller.back();
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
        controller.pressed(screenX, screenY, pointer, button, renderer.ppuX, renderer.ppuY, width, height);
        System.out.println("333333333");
        float accelX1 = Gdx.input.getAccelerometerX();
        boolean fastspeed = false;
        if( accelX1 > 5.0){
            fastspeed = true;
            GlobalVariables.noteSpeed=6;
            //System.out.println("11111");
        }else{
            fastspeed = false;
            GlobalVariables.noteSpeed=6;
            //System.out.println("22222");
        }
        if(world.combo>=2){
            System.out.println("11111");
        }else{
            System.out.println("22222");
        };
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        controller.released(screenX, screenY, pointer, button, renderer.ppuX, renderer.ppuY, width, height);
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        controller.dragged(screenX, screenY, pointer, renderer.ppuX, renderer.ppuY, width, height);
        return true;
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
