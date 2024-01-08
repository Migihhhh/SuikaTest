package no.sandramoen.ggj2024oslo;

import no.sandramoen.ggj2024oslo.screens.gameplay.LevelScreen;
import no.sandramoen.ggj2024oslo.utils.BaseGame;

public class MyGdxGame extends BaseGame {

	@Override
	public void create() {
		super.create();
		// setActiveScreen(new SplashScreen());
		// setActiveScreen(new MenuScreen());
		setActiveScreen(new LevelScreen(BaseGame.testMap));
	}
}

