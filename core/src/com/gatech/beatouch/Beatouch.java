package com.gatech.beatouch;

import com.badlogic.gdx.Game;
import com.gatech.beatouch.screens.SplashScreen;

public class Beatouch extends Game {

    @Override
    public void create() {
        setScreen(new SplashScreen());
    }

}
