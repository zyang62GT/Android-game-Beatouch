package com.gatech.beatouch.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.gatech.beatouch.Beatouch;
import com.gatech.beatouch.configuration.GlobalVariables;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		GlobalVariables.appVersionName = "debug build.";
		new LwjglApplication(new Beatouch(), "SS Train", 1280, 720);
	}
}
