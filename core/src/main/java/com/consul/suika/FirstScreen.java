package com.consul.suika;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;



/** First screen of the application. Displayed after the application is created. */
public class FirstScreen implements Screen {

    private Texture mainMenuPlayTexture;private TextureRegion mainMenuPlayTextureRegion;private TextureRegionDrawable mainMenuPlayTextureRegionDrawable;private ImageButton mainMenuPlayButton;
    private Texture mainMenuLeaderboardTexture;private TextureRegion mainMenuLeaderboardTextureRegion;private TextureRegionDrawable mainMenuLeaderboardTextureRegionDrawable;private ImageButton mainMenuLeaderboardButton;
    private Texture mainMenuExitTexture;private TextureRegion mainMenuExitTextureRegion;private TextureRegionDrawable mainMenuExitTextureRegionDrawable;private ImageButton mainMenuExitButton;

    private Game game;

    private SpriteBatch menuBatch;
    private SpriteBatch gamemodeBatch;


    private Stage mainMenuStage;
    private Texture mainMenuBG;
    private Texture gamemodeBG;
    FitViewport viewport;

    public float PPM = 100f; // Pixels per meter

    private final int WORLD_WIDTH = 110; // in pixels
    private final int WORLD_HEIGHT = 192; // in pixels

    private boolean inMainMenu= true;
    private boolean inGameMode= false;

    private Stage gamemodeStage;
    private Texture gameModeBackTexture;private TextureRegion gameModeBackTextureRegion;private TextureRegionDrawable gameModeBackTextureRegionDrawable; private ImageButton gameModeBackButton;
    private Texture game1buttonTexture;private TextureRegion game1buttonTextureRegion;private TextureRegionDrawable game1buttonTextureRegionDrawable;private ImageButton game1Button;
    private Texture mainMenucreditsTexture;private TextureRegion mainMenucreditsTextureRegion;private TextureRegionDrawable mainMenucreditsTextureRegionDrawable; private ImageButton mainMenuCreditsButton;
    private Texture mainMenuoptionsTexture;private TextureRegion mainMenuoptionsTextureRegion;private TextureRegionDrawable mainMenuoptionsTextureRegionDrawable;private ImageButton mainMenuOptionsButton;

    private Texture game2buttonTexture;private TextureRegion game2buttonTextureRegion;private TextureRegionDrawable game2buttonTextureRegionDrawable;private ImageButton game2Button;
    private Texture game3buttonTexture;private TextureRegion game3buttonTextureRegion;private TextureRegionDrawable game3buttonTextureRegionDrawable; private ImageButton game3Button;
    private Texture StoryModeButtonTexture;private TextureRegion StoryModeButtonTextureRegion;private TextureRegionDrawable StoryModeButtonTextureRegionDrawable;private ImageButton StoryModeButton;

    private boolean isMusicPlayingmm = true;
    private Music mainMenuMusic;
    private Sound gameSelectSound,mainMenuButtonSound;


    public FirstScreen(Game game) {
        this.game = game;

    }


    @Override
    public void show() {

        viewport = new FitViewport(WORLD_WIDTH / PPM, WORLD_HEIGHT / PPM);

        // MAIN MENU BG
        menuBatch = new SpriteBatch();
        mainMenuBG = new Texture("mainMenuBG.png");

        gamemodeBatch = new SpriteBatch();
        gamemodeBG = new Texture("gamemodeBG.png");

        mainMenuStage = new Stage(viewport);

        // for button positioning
        float viewportWidth = viewport.getWorldWidth();
        float viewportHeight = viewport.getWorldHeight();

        //Main menu buttons
        mainMenuPlayTexture = new Texture("mainMenuPlay.png");
        mainMenuPlayTextureRegion = new TextureRegion(mainMenuPlayTexture);
        mainMenuPlayTextureRegionDrawable = new TextureRegionDrawable(mainMenuPlayTextureRegion);

        ImageButton.ImageButtonStyle playButtonStyle = new ImageButton.ImageButtonStyle();
        playButtonStyle.up = mainMenuPlayTextureRegionDrawable;
        mainMenuPlayButton = new ImageButton(playButtonStyle);

        mainMenuPlayButton.getImage().setScale(0.7f); // This will reduce the size
        mainMenuPlayButton.setSize(0.78f, 0.3f);
        mainMenuPlayButton.setPosition(0.15f * viewportWidth, 0.55f * viewportHeight);
        mainMenuStage.addActor(mainMenuPlayButton);

        Gdx.input.setInputProcessor(mainMenuStage);

        //create new stage for gamemodes
        gamemodeStage = new Stage(viewport);

        gameModeBackTexture = new Texture("mainMenuBack.png");
        gameModeBackTextureRegion = new TextureRegion(gameModeBackTexture);
        gameModeBackTextureRegionDrawable = new TextureRegionDrawable(gameModeBackTextureRegion);

        ImageButton.ImageButtonStyle backButtonStyle = new ImageButton.ImageButtonStyle();
        backButtonStyle.up = gameModeBackTextureRegionDrawable;
        gameModeBackButton = new ImageButton(backButtonStyle);

        gameModeBackButton.getImage().setScale(0.6f); // This will reduce the size
        gameModeBackButton.setSize(0.5f, 0.3f);
        gameModeBackButton.setPosition(0.1f * viewportWidth, 0f * viewportHeight);
        gamemodeStage.addActor(gameModeBackButton);

        game1buttonTexture = new Texture("game1button.png");
        game1buttonTextureRegion = new TextureRegion(game1buttonTexture);
        game1buttonTextureRegionDrawable = new TextureRegionDrawable(game1buttonTextureRegion);

        ImageButton.ImageButtonStyle game1ButtonStyle = new ImageButton.ImageButtonStyle();
        game1ButtonStyle.up = game1buttonTextureRegionDrawable;
        game1Button = new ImageButton(game1ButtonStyle);

        game1Button.getImage().setScale(1f); // This will reduce the size
        game1Button.setSize(0.46f, 0.38f);
        game1Button.setPosition(0.53f * viewportWidth, 0.5f * viewportHeight);
        gamemodeStage.addActor(game1Button);

        game2buttonTexture = new Texture("game2Button.png");
        game2buttonTextureRegion = new TextureRegion(game2buttonTexture);
        game2buttonTextureRegionDrawable = new TextureRegionDrawable(game2buttonTextureRegion);

        ImageButton.ImageButtonStyle game2ButtonStyle = new ImageButton.ImageButtonStyle();
        game2ButtonStyle.up = game2buttonTextureRegionDrawable;
        game2Button = new ImageButton(game2ButtonStyle);

        game2Button.getImage().setScale(1f); // This will reduce the size
        game2Button.setSize(0.46f, 0.38f);
        game2Button.setPosition(0.05f * viewportWidth, 0.25f * viewportHeight);
        gamemodeStage.addActor(game2Button);

        game3buttonTexture = new Texture("game3Button.png");
        game3buttonTextureRegion = new TextureRegion(game3buttonTexture);
        game3buttonTextureRegionDrawable = new TextureRegionDrawable(game3buttonTextureRegion);

        ImageButton.ImageButtonStyle game3ButtonStyle = new ImageButton.ImageButtonStyle();
        game3ButtonStyle.up = game3buttonTextureRegionDrawable;
        game3Button = new ImageButton(game3ButtonStyle);

        game3Button.getImage().setScale(1f); // This will reduce the size
        game3Button.setSize(0.46f, 0.38f);
        game3Button.setPosition(0.53f * viewportWidth, 0.25f * viewportHeight);
        gamemodeStage.addActor(game3Button);

        StoryModeButtonTexture = new Texture("StoryModeButton.png");
        StoryModeButtonTextureRegion = new TextureRegion(StoryModeButtonTexture);
        StoryModeButtonTextureRegionDrawable = new TextureRegionDrawable(StoryModeButtonTextureRegion);

        ImageButton.ImageButtonStyle storyModeButtonStyle = new ImageButton.ImageButtonStyle();
        storyModeButtonStyle.up = StoryModeButtonTextureRegionDrawable;
        StoryModeButton = new ImageButton(storyModeButtonStyle);

        StoryModeButton.getImage().setScale(1f); // This will reduce the size
        StoryModeButton.setSize(0.46f, 0.38f);
        StoryModeButton.setPosition(0.05f * viewportWidth, 0.5f * viewportHeight);
        gamemodeStage.addActor(StoryModeButton);

        mainMenuPlayButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("mainMenuPlayButton", "Play Button clicked");
                mainMenuButtonSound.play();
                inMainMenu=false;
                inGameMode=true;
                Gdx.input.setInputProcessor(gamemodeStage);
            }
        });

        gameModeBackButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("mainMenuPlayButton", "Play Button clicked");
                mainMenuButtonSound.play();
                inGameMode=false;
                inMainMenu=true;
                Gdx.input.setInputProcessor(mainMenuStage);
            }
        });

        game1Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("mainMenuPlayButton", "Play Button clicked");
                gameSelectSound.play();
                inGameMode=false;
                inMainMenu=false;
                mainMenuMusic.pause();
                game.setScreen(new GameScreen(game));
            }
        });

        mainMenucreditsTexture = new Texture("creditsButton.png");
        mainMenucreditsTextureRegion = new TextureRegion(mainMenucreditsTexture);
        mainMenucreditsTextureRegionDrawable = new TextureRegionDrawable(mainMenucreditsTextureRegion);

        ImageButton.ImageButtonStyle creditsButtonStyle = new ImageButton.ImageButtonStyle();
        creditsButtonStyle.up = mainMenucreditsTextureRegionDrawable;
        mainMenuCreditsButton = new ImageButton(creditsButtonStyle);

        mainMenuCreditsButton.getImage().setScale(0.6f); // This will reduce the size
        mainMenuCreditsButton.setSize(0.63f, 0.23f);
        mainMenuCreditsButton.setPosition(0.21f * viewportWidth, 0.24f * viewportHeight);
        mainMenuStage.addActor(mainMenuCreditsButton);

        mainMenuoptionsTexture = new Texture("optionsButton.png");
        mainMenuoptionsTextureRegion = new TextureRegion(mainMenuoptionsTexture);
        mainMenuoptionsTextureRegionDrawable = new TextureRegionDrawable(mainMenuoptionsTextureRegion);

        ImageButton.ImageButtonStyle optionsButtonStyle = new ImageButton.ImageButtonStyle();
        optionsButtonStyle.up = mainMenuoptionsTextureRegionDrawable;
        mainMenuOptionsButton = new ImageButton(optionsButtonStyle);

        mainMenuOptionsButton.getImage().setScale(0.6f); // This will reduce the size
        mainMenuOptionsButton.setSize(0.63f, 0.23f);
        mainMenuOptionsButton.setPosition(0.21f * viewportWidth, 0.39f * viewportHeight);
        mainMenuStage.addActor(mainMenuOptionsButton);

        mainMenuMusic = Gdx.audio.newMusic(Gdx.files.internal("mainMenuMusic.mp3"));
        mainMenuMusic.setLooping(true);
        mainMenuMusic.setVolume(0.5f);
        mainMenuMusic.play();

        gameSelectSound = Gdx.audio.newSound(Gdx.files.internal("gameSelect.mp3"));
        mainMenuButtonSound = Gdx.audio.newSound(Gdx.files.internal("mainMenuButtonSound.mp3"));


    }

@Override
    public void render(float delta) {

    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    viewport.apply();

        if (inMainMenu) {
            // Clear the screen and draw the menu UI
            menuBatch.begin();
            menuBatch.draw(mainMenuBG, 0, 0, WORLD_WIDTH / PPM, WORLD_HEIGHT / PPM);
            menuBatch.end();

            mainMenuStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f)); // Update pause menu stage
            mainMenuStage.draw();

        } else if (inGameMode){

            gamemodeBatch.begin();
            gamemodeBatch.draw(gamemodeBG, 0, 0, WORLD_WIDTH / PPM, WORLD_HEIGHT / PPM);
            gamemodeBatch.end();

            gamemodeStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f)); // Update pause menu stage
            gamemodeStage.draw();
        } else {

        }

}

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        menuBatch.setProjectionMatrix(viewport.getCamera().combined);
        gamemodeBatch.setProjectionMatrix(viewport.getCamera().combined);

    }

    @Override
    public void pause() {
        if (mainMenuMusic.isPlaying()) {
            mainMenuMusic.pause();
        }
    }

    @Override
    public void resume() {
        if (!mainMenuMusic.isPlaying()) {
            mainMenuMusic.play();
        }
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        mainMenuStage.dispose();
        gamemodeStage.dispose();
        mainMenuMusic.dispose();
        gameSelectSound.dispose();
        mainMenuButtonSound.dispose();

    }
}
