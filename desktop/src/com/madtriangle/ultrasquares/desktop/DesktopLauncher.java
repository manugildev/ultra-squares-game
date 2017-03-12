package com.madtriangle.ultrasquares.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import paddle.PaddleGame;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Squary";
        config.width = (int) (1080 / 3);
        config.height = (int) (1720 / 3);
        new LwjglApplication(new PaddleGame(new ActionResolverDesktop()), config);
    }
}
