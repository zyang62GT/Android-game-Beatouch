package com.gatech.beatouch.android;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.gatech.beatouch.Beatouch;
import com.gatech.beatouch.configuration.GlobalVariables;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useImmersiveMode = true;
		try {
			PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            GlobalVariables.appVersionName = pInfo.versionName;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
        initialize(new Beatouch(), config);
	}
}
