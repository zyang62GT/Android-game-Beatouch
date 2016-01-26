package com.gatech.beatouch.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gatech.beatouch.assets.Assets;
import com.gatech.beatouch.configuration.GlobalVariables;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.gatech.beatouch.objects.LoadingBar;


public class SplashScreen implements Screen {
    private Texture texture = new Texture(Gdx.files.internal("bigimages/splash.png"));
    private Image splashImage = new Image(texture);
    private Stage stage = new Stage();
    TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("textures/textures.pack.atlas"));
    private Skin skin = new Skin(Gdx.files.internal("skins/splashSkin.json"), atlas);
//    private ProgressBar loadingProgress = new ProgressBar(0.0f, 100f, 0.1f, false, skin);

    private Image logo;
    private Image loadingFrame;
    private Image loadingBarHidden;
    private Image loadingBg;
    private Actor loadingBar;

    private float startX, endX;
    private float percent;

    public boolean animationDone = false;
    int phase = 0;

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();

        if (Assets.update()) { // check if all files are loaded

            if (animationDone) { // when the animation is finished, go to MainMenu()
                // load the assets to into the Assets class
                Assets.setMenuSkin();
                Assets.setHitsounds();
                Assets.setTextures();
                Assets.setFonts();
                if (phase == 1) {
                    Assets.setSongs();
                    ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen());
                }
                if (phase == 0) {
                    phase++;
                    Assets.reloadSongmaps();
                }
            }
        }
        // Interpolate the percentage to make it more smooth
        percent = Interpolation.linear.apply(percent, Assets.getProgress(), 0.1f);

        // Update positions (and size) to match the percentage
        loadingBarHidden.setX(startX + endX * percent);
        loadingBg.setX(loadingBarHidden.getX() + 30);
        loadingBg.setWidth(450 - 450 * percent);
        loadingBg.invalidate();

        // Show the loading screen
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // Scale the viewport to fit the screen
//        Vector2 scaledView = Scaling.fit.apply(800, 480, width, height);
//        stage.getViewport().update((int)scaledView.x, (int)scaledView.y, true);


        // Place the logo in the middle of the screen and 100 px up
        logo.setX((width - logo.getWidth()) / 2);
        logo.setY((height - logo.getHeight()) / 2 + 100);

        // Place the loading frame in the middle of the screen
        loadingFrame.setX((stage.getWidth() - loadingFrame.getWidth()) / 2);
        loadingFrame.setY((stage.getHeight() - loadingFrame.getHeight()) / 2);

        // Place the loading bar at the same spot as the frame, adjusted a few
        // px
        loadingBar.setX(loadingFrame.getX() + 15);
        loadingBar.setY(loadingFrame.getY() + 5);

        // Place the image that will hide the bar on top of the bar, adjusted a
        // few px
        loadingBarHidden.setX(loadingBar.getX() + 35);
        loadingBarHidden.setY(loadingBar.getY() - 3);
        // The start position and how far to move the hidden loading bar
        startX = loadingBarHidden.getX();
        endX = 440;

        // The rest of the hidden bar
        loadingBg.setSize(450, 50);
        loadingBg.setX(loadingBarHidden.getX() + 30);
        loadingBg.setY(loadingBarHidden.getY() + 3);
    }

    @Override
    public void show() {
        float sourceHeight = texture.getHeight();
        float targetHeight = stage.getHeight();
        float scale = targetHeight / sourceHeight;

        splashImage.setScale(scale);
        splashImage.setX(stage.getWidth() / 2 - (scale * splashImage.getWidth()) / 2);

//        loadingProgress.setSize(scale * splashImage.getWidth() * 0.7f, stage.getHeight() * 0.07f);
//        loadingProgress.setX(splashImage.getX() + scale * splashImage.getWidth() * 0.15f);
//        loadingProgress.setY(stage.getHeight() * 0.2f);
//        loadingProgress.setAnimateDuration(0.01f);

        stage.addActor(splashImage);
//        stage.addActor(loadingProgress);


        logo = new Image(atlas.findRegion("libgdx-logo"));
        loadingFrame = new Image(atlas.findRegion("loading-frame"));
        loadingBarHidden = new Image(atlas.findRegion("loading-bar-hidden"));
        loadingBg = new Image(atlas.findRegion("loading-frame-bg"));

        Animation anim = new Animation(0.05f, atlas.findRegions("loading-bar-anim"));
        anim.setPlayMode(PlayMode.LOOP_REVERSED);
        loadingBar = new LoadingBar(anim);

        stage.addActor(loadingBar);
        stage.addActor(loadingBg);
        stage.addActor(loadingBarHidden);
        stage.addActor(loadingFrame);
        stage.addActor(logo);


        //splashImage.setX(stage.getWidth() / 2 - width / 2);
        splashImage.addAction(Actions.sequence(Actions.alpha(0)
                , Actions.fadeIn(0.75f), Actions.delay(1.5f), Actions.run(new Runnable() {
            @Override
            public void run() {
                animationDone = true;
            }
        })));
        // Queue loading will load 2 sets of assets:
        // internal assets: images and hit sounds
        // external assets: beatmaps
        // while processing external assets, it may also install .osz beatmaps
        // or convert .osu files, which is why after the first pass is completed,
        // we update the set of loaded maps to include the recently extracted maps
        // if nothing was installed or changed, the second phase won't last long.
        Assets.queueLoading();
        GlobalVariables.loadConfiguration();
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
        GlobalVariables.storeConfiguration();
        texture.dispose();
        stage.dispose();
    }
}

