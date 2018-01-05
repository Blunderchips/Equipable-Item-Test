package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.BaseEngine;

/**
 * The sole purpose of this class is to hold the main method. Any other
 * operation should be placed in a separate class. <strong>You can not
 * instantiate this class.</strong>
 *
 * @author siD
 */
public final class DesktopLauncher {

    @Deprecated
    private DesktopLauncher() {
    }

    /**
     * @param args Arguments from the command line
     */
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public static void main(String[] args) {
        LwjglApplicationConfiguration cfg
                = new LwjglApplicationConfiguration();

        cfg.width = 800;
        cfg.height = 450;

        cfg.samples = 16;

        new LwjglApplication(new BaseEngine(), cfg);
    }
}
