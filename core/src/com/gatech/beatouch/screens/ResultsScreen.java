package com.gatech.beatouch.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.gatech.beatouch.assets.Assets;
import com.gatech.beatouch.configuration.GlobalVariables;
import com.gatech.beatouch.entities.Results;

public class ResultsScreen implements Screen {

    Stage stage = new Stage();
    private Texture texture = Assets.resultsPageTexture;
    private Image splashImage = new Image(texture);
    private Table table = new Table();
    private Label difficulty = new Label("Difficulty:", Assets.menuSkin, "song_style_result_values");
    private Label difficultyLabel;
    private Label normalizedAccuracyLabel = new Label("% Accuracy:", Assets.menuSkin, "song_style_result_values");
    private Label normalizedAccuracyResultLabel;
    private Label greatLabel = new Label("Great", Assets.menuSkin, "song_style_result_values");
    private Label greatResultLabel;
    private Label perfectLabel = new Label("Perfect:", Assets.menuSkin, "song_style_result_values");
    private Label perfectResultLabel;
    private Label missLabel = new Label("Miss:", Assets.menuSkin, "song_style_result_values");
    private Label missResultLabel;
    private Label comboLabel = new Label("Largest Combo:", Assets.menuSkin, "song_style_result_values");
    private Label comboResultLabel;
    private Label bonusLabel = new Label("Bonus:", Assets.menuSkin, "song_style_result_values");
    private Label bonusResultLabel;
    private Label totalLabel = new Label("Total Score:", Assets.menuSkin, "song_style_result_values");
    private Label totalResultLabel;

    @Override
    public void show() {
        // title font scale = 1 for a 720 height
        float fontScale = stage.getHeight() / GlobalVariables.BASE_HEIGHT;

        splashImage.setHeight(stage.getHeight());
        splashImage.setWidth(stage.getWidth());
        stage.addActor(splashImage);


        table.setFillParent(true);

        normalizedAccuracyResultLabel = new Label(String.format("%.2f", Results.normalizedAccuracy * 100f) + "%", Assets.menuSkin, "song_style_result_values");
        missResultLabel = new Label(Integer.toString(Results.miss), Assets.menuSkin, "song_style_result_values");
        greatResultLabel = new Label(Integer.toString(Results.greats), Assets.menuSkin, "song_style_result_values");
        perfectResultLabel = new Label(Integer.toString(Results.perfects), Assets.menuSkin, "song_style_result_values");
        bonusResultLabel = new Label(Integer.toString(Results.bonus), Assets.menuSkin, "song_style_result_values");
        totalResultLabel = new Label(Integer.toString(Results.total), Assets.menuSkin, "song_style_result_values");
        comboResultLabel = new Label(Integer.toString(Results.combo) + (Results.combo == Assets.selectedSongMap.notes.size() ? " (FC)" : ""), Assets.menuSkin, "song_style_result_values");

        normalizedAccuracyResultLabel.setFontScale(fontScale);
        missResultLabel.setFontScale(fontScale);
        greatResultLabel.setFontScale(fontScale);
        perfectResultLabel.setFontScale(fontScale);
        bonusResultLabel.setFontScale(fontScale);
        comboResultLabel.setFontScale(fontScale);
        totalResultLabel.setFontScale(fontScale);

        normalizedAccuracyLabel.setFontScale(fontScale);
        perfectLabel.setFontScale(fontScale);
        greatLabel.setFontScale(fontScale);
        bonusLabel.setFontScale(fontScale);
        comboLabel.setFontScale(fontScale);
        totalLabel.setFontScale(fontScale);
        missLabel.setFontScale(fontScale);



        table.add(comboLabel).padTop(430).padLeft(550);
        table.add().width(stage.getWidth() * 0.2f).padBottom(20);
        table.add(comboResultLabel).fillX().padTop(430).row();


        table.add(normalizedAccuracyLabel).fillX().padTop(20).padLeft(550);
        table.add().width(stage.getWidth() * 0.2f);
        table.add(normalizedAccuracyResultLabel).fillX().padTop(10).row();

        table.add(bonusLabel).fillX().padTop(10).padLeft(550);
        table.add().width(stage.getWidth() * 0.2f);
        table.add(bonusResultLabel).fillX().padTop(10).row();


        table.add(perfectLabel).fillX().padTop(10).padLeft(550);
        table.add().width(stage.getWidth() * 0.2f);
        table.add(perfectResultLabel).fillX().padTop(10).row();

        table.add(greatLabel).fillX().padTop(10).padLeft(550);
        table.add().width(stage.getWidth() * 0.2f);
        table.add(greatResultLabel).fillX().padTop(10).row();

        table.add(missLabel).fillX().padTop(10).padLeft(550);
        table.add().width(stage.getWidth() * 0.2f);
        table.add(missResultLabel).fillX().padTop(10).row();

        table.add(totalLabel).fillX().padTop(10).padLeft(550);
        table.add().width(stage.getWidth() * 0.2f).padBottom(20);
        table.add(totalResultLabel).fillX().padTop(10).padBottom(10).row();

        TextButton retryButton = new TextButton("Retry", Assets.menuSkin, "item1");
        TextButton continueButton = new TextButton("Continue", Assets.menuSkin, "item1");

        retryButton.getLabel().setFontScale(fontScale);
        continueButton.getLabel().setFontScale(fontScale);

        retryButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new SongScreen());
            }
        });
        continueButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new SongSelectScreen());
            }
        });

        table.add(retryButton).width(stage.getWidth() * 0.3f).height(stage.getHeight() * 0.1f);
        table.add().fillX();
        table.add(continueButton).width(stage.getWidth() * 0.3f).height(stage.getHeight() * 0.1f).row();
        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);
    }

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
