package no.sandramoen.ggj2024oslo.lwjgl3;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import no.sandramoen.ggj2024oslo.MyGdxGame;

import java.util.Locale;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
class Lwjgl3Launcher {
    public static void main(String[] arg) {
        if (StartupHelper.startNewJvmIfRequired(true)) return; // This handles macOS support and helps on Windows.
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setForegroundFPS(60);
        config.setTitle("Global Game Jam 2024");
        config.setResizable(false);
        config.useVsync(true);
        config.setWindowIcon("images/excluded/icon_16x16.png", "images/excluded/icon_32x32.png", "images/excluded/icon_64x64.png");

        boolean isFullscreen = false;
        if (isFullscreen)
            config.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode());
        else
            setWindowedMode(.6f, config);

        new Lwjgl3Application(new MyGdxGame(), config);
    }

    private static String getCountryCode() {
        String countryCode = Locale.getDefault().getCountry().toLowerCase(Locale.ROOT);
        System.out.println("[DesktopLauncher] Locale => Country code: " + countryCode);
        return countryCode;
    }

    private static void setWindowedMode(float percentOfScreenSize, Lwjgl3ApplicationConfiguration config) {
        Graphics.DisplayMode dimension = Lwjgl3ApplicationConfiguration.getDisplayMode();
        int width = (int) (dimension.width * percentOfScreenSize);

        float aspectRatio = 16 / 9f;
        int height = (int) (width / aspectRatio);

        System.out.println("[DesktopLauncher] Window dimensions => width: " + width + ", height: " + height);
        config.setWindowedMode(width, height);
    }
}
