package com.consul.suika;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.Game;

import java.util.Arrays;


public class GameScreen implements Screen {

    private Game game;

    public GameScreen(Game game) {
        this.game = game;
    }


    private boolean isGameActive = true; // shu change: tracks whether the game is active
    private static final float PPM = 100f; // Pixels per meter

    // World dimensions in meters
    private final int WORLD_WIDTH = 110; // in pixels
    private final int WORLD_HEIGHT = 192; // in pixels
    private FlowerContactListener contactListener = new FlowerContactListener ();

    Texture background;
    Texture daffidol;
    Texture buttercup;
    Texture marrigold;
    Texture cherryblossom;
    Texture orchid;

    FitViewport viewport;
    SpriteBatch batch;

    Vector2 touchPos;
    private boolean debugMode = false;

    Box2DDebugRenderer debugRenderer;
    OrthographicCamera camera;

    World world;
    ShapeRenderer shapeRenderer;
    Array<Body> flowers = new Array<>();
    private GameScreen.FlowerType nextFlowerType;

    private GameScreen.FlowerType currentFlowerType = GameScreen.FlowerType.DAFFODIL;

    private Sprite floatingFlowerSprite;
    private boolean isTouching = false;
    Texture sunflower;
    Texture jasmine;
    Texture freesia;
    Texture daisy;
    Texture dandelion;
    Texture hibiscus;

    //STEP 1 OF ADDING NEW FLOWER: CALL YOUR VARIABLE I.E "Texture newFlower;"


    //stages for game ui and menu ui
    private Stage gameStage;
    private Stage menuStage;
    private Stage restartStage;
    private Stage leaderboardStage;

    //texture for pause button
    private Texture pauseTexture;private TextureRegion pauseTextureRegion;private TextureRegionDrawable pauseTextureRegionDrawable;private ImageButton pauseButton;
    //texture for continue button
    private ImageButton continueButton;
    private ImageButton restartButton;
    private ImageButton yesButton;
    private ImageButton noButton;

    //Textures for buttons and kung ano
    private Texture continueTexture;private TextureRegion continueTextureRegion;private TextureRegionDrawable continueTextureRegionDrawable;
    private Texture restartTexture;private TextureRegion restartTextureRegion;private TextureRegionDrawable restartTextureRegionDrawable;
    private Texture noTexture;private TextureRegion noTextureRegion;private TextureRegionDrawable noTextureRegionDrawable;
    private Texture yesTexture;private TextureRegion yesTextureRegion;private TextureRegionDrawable yesTextureRegionDrawable;

    //for pause
    private boolean isPaused = false;
    private boolean isRestarting = false; // Flag to track if restart UI should be shown
    private boolean inLeaderboard = false;

    //for music and its buttons
    private ImageButton musicButton;
    private Music suikaMusic;
    private Texture musicButtonTexture,musicButtonPressedTexture;
    boolean isMusicPlaying = true;

    //for game pause ui
    private Texture pauseUI;

    //game over stage ui and buttons
    private Stage gameOverStage;
    private TextureRegion gameOverTextureRegion;private TextureRegionDrawable gameOverTextureRegionDrawable;

    private ImageButton retryButton;
    private ImageButton exitButton;
    private Texture retryTexture;private TextureRegion retryTextureRegion;private TextureRegionDrawable retryTextureRegionDrawable;
    private Texture gameOverTexture;private Texture exitTexture;private TextureRegion exitTextureRegion;private TextureRegionDrawable exitTextureRegionDrawable;

    //for sound effects (NOT WORKING)
    private Sound buttonSound,combineSound,collisionSound,dropSound;
    //Leaderboard menu

    private Texture leaderboardButtonTexture;private TextureRegion leaderboardButtonTextureRegion;private TextureRegionDrawable leaderboardButtonTextureRegionDrawable;private ImageButton leaderboardButton;
    private Texture leaderboardUI;

    //back button leaderboard
    private Texture leaderboardBackTexture;private TextureRegion leaderboardBackTextureRegion;private TextureRegionDrawable leaderboardBackTextureRegionDrawable;private ImageButton leaderboardBackButton;

    private boolean ifInMain = false; // set to true later on

    private Texture quitTexture;private TextureRegion quitTextureRegion;private TextureRegionDrawable quitTextureRegionDrawable;private ImageButton quitButton;
    private SpriteBatch leaderboardBatch;
    Texture leaderboardBG;
    //Makes drop sound trigger only once when clicked and dragged
    private boolean firstTouch=false;
    private boolean isSoundEnabled=true;
    private Texture soundButtonTexture,soundButtonPressedTexture;
    private ImageButton soundButton;


    private ApplicationListener currentListener;// The active game or menu

    @Override
    public void show() {
        shapeRenderer = new ShapeRenderer();
        background = new Texture("gameBackground.png");
        daffidol = new Texture("flower1.png");
        buttercup = new Texture("flower2.png");
        marrigold = new Texture("flower3.png");
        cherryblossom = new Texture("flower4.png");
        orchid = new Texture("flower5.png");
        sunflower = new Texture("flower6.png");
        jasmine = new Texture("flower7.png");
        freesia = new Texture("flower8.png");
        daisy = new Texture("flower9.png");
        dandelion = new Texture("flower10.png");
        hibiscus = new Texture("flower11.png");


        //STEP 2 OF ADDING NEW FLOWER: CREATE A TEXTURE FOR THE FLOWER AS SEEN ABOVE HERE

        batch = new SpriteBatch();

        viewport = new FitViewport(WORLD_WIDTH / PPM, WORLD_HEIGHT / PPM);
        camera = (OrthographicCamera) viewport.getCamera();

        floatingFlowerSprite = new Sprite(daffidol);
        floatingFlowerSprite.setSize(currentFlowerType.getRadius() * 2, currentFlowerType.getRadius() * 2); // Set size based on radius
        float startX = (WORLD_WIDTH / PPM - floatingFlowerSprite.getWidth()) / 6; // Center horizontally
        float startY = 135 / PPM; // Fixed height (adjust as needed)
        floatingFlowerSprite.setPosition(startX, startY);


        currentFlowerType = FlowerType.DAFFODIL;
        nextFlowerType = getNextFlowerType(currentFlowerType);

        touchPos = new Vector2();

        world = new World(new Vector2(0, -9.8f), false);
        debugRenderer = new Box2DDebugRenderer();

        world.setContactListener(contactListener);
        shapeRenderer = new ShapeRenderer();

        // CREATE THE FLOORS AND WALLS
        createPlatform(.2f / PPM, 80.5f / PPM, 1.5f / PPM, 100 / PPM); //left wall
        createPlatform(110f / PPM, 80.5f / PPM, 1.5f / PPM, 100 / PPM); //right wall
        createPlatform(56 / PPM, 35.5f / PPM, 70 / PPM, 1.1f / PPM); //floor

        buttonSound = Gdx.audio.newSound(Gdx.files.internal("suikaButtonSound.mp3"));
        dropSound = Gdx.audio.newSound(Gdx.files.internal("suikaDropSound.mp3"));


        // for button positioning
        float viewportWidth = viewport.getWorldWidth();
        float viewportHeight = viewport.getWorldHeight();

        //stage for the game ui buttons like pause and retry
        gameStage = new Stage(viewport);
        Gdx.input.setInputProcessor(gameStage);


        //pause button // size positioning
        pauseTexture = new Texture("pause.png");
        pauseTextureRegion = new TextureRegion(pauseTexture);
        pauseTextureRegionDrawable = new TextureRegionDrawable(pauseTextureRegion);
        pauseButton = new ImageButton(pauseTextureRegionDrawable);
        pauseButton.getImage().setScale(0.107f); // This will reduce the size by 90%
        pauseButton.setSize(0.01f, 0.01f);
        pauseButton.setPosition(0.041f * viewportWidth, 0.902f * viewportHeight); // Moves the button upwards

        gameStage.addActor(pauseButton);
        //menu stage // new stage for paused menu // settings / music / sound / leaderboard / exit
        menuStage = new Stage(viewport);

        //pause ui
        Texture pauseUItexture = new Texture(Gdx.files.internal("pauseMenu.png"));
        TextureRegion pauseUItextureRegion = new TextureRegion(pauseUItexture);
        Image pauseUI = new Image(pauseUItextureRegion);
        pauseUI.setScale(1f);
        pauseUI.setSize(1.1f, 1.92f);
        pauseUI.setPosition(0f * viewportWidth, 0f * viewportHeight);
        menuStage.addActor(pauseUI);


        //continue button for the pause menu
        continueTexture = new Texture(Gdx.files.internal("resume.png"));
        continueTextureRegion = new TextureRegion(continueTexture);
        continueTextureRegionDrawable = new TextureRegionDrawable(continueTextureRegion);

        ImageButton.ImageButtonStyle continueButtonStyle = new ImageButton.ImageButtonStyle();
        continueButtonStyle.up = continueTextureRegionDrawable;
        continueButton = new ImageButton(continueButtonStyle);

        continueButton.getImage().setScale(0.6f); // This will reduce the size
        continueButton.setSize(0.8f, 0.21f);
        continueButton.setPosition(0.12f * viewportWidth, 0.63f * viewportHeight);
        menuStage.addActor(continueButton);

        //restart button // size positioning //
        restartTexture = new Texture(Gdx.files.internal("restart2.png"));
        restartTextureRegion = new TextureRegion(restartTexture);
        restartTextureRegionDrawable = new TextureRegionDrawable(restartTextureRegion);

        ImageButton.ImageButtonStyle restartButtonStyle = new ImageButton.ImageButtonStyle();
        restartButtonStyle.up = restartTextureRegionDrawable;
        restartButton = new ImageButton(restartButtonStyle);

        restartButton.getImage().setScale(0.6f);
        restartButton.setSize(0.8f, 0.21f);
        restartButton.setPosition(0.12f * viewportWidth, 0.48f * viewportHeight);
        menuStage.addActor(restartButton);

        //continue button for the pause menu
        quitTexture = new Texture(Gdx.files.internal("quitButton.png"));
        quitTextureRegion = new TextureRegion(quitTexture);
        quitTextureRegionDrawable = new TextureRegionDrawable(quitTextureRegion);

        ImageButton.ImageButtonStyle quitButtonStyle = new ImageButton.ImageButtonStyle();
        quitButtonStyle.up = quitTextureRegionDrawable;
        quitButton = new ImageButton(quitButtonStyle);

        quitButton.getImage().setScale(0.6f); // This will reduce the size
        quitButton.setSize(0.8f, 0.21f);
        quitButton.setPosition(0.12f * viewportWidth, 0.13f * viewportHeight);
        menuStage.addActor(quitButton);

        // pause button action listener, will switch stage to menuStage // allows the menu ui
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("PauseButton", "Button clicked");
                if (isSoundEnabled) {
                    buttonSound.play();
                }

                if (!isPaused) {
                    isPaused = true;
                    Gdx.input.setInputProcessor(menuStage); // Switch input processor to the pause menu stage
                }
            }
        });
        // for continuing the game // continue button
        continueButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                if (isSoundEnabled) {
                    buttonSound.play();
                }

                Gdx.app.log("ContinueButton", "Button clicked");
                if (isPaused) {
                    isPaused = false;
                    isTouching = false;
                    Gdx.input.setInputProcessor(gameStage); // Switch back to the game stage

                }
            }
        });

        restartButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (isSoundEnabled) {
                    buttonSound.play();
                }
                Gdx.app.log("RestartButton", "Button clicked");
                resetGame();
                isPaused=false;
                isTouching = false;
                Gdx.input.setInputProcessor(gameStage);
            }
        });

        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (isSoundEnabled) {
                    buttonSound.play();
                }
                Gdx.app.log("quitButton", "Button clicked");
                isMusicPlaying=false;
                suikaMusic.pause();
                game.setScreen(new FirstScreen(game));
            }
        });

        //music path
        suikaMusic = Gdx.audio.newMusic(Gdx.files.internal("SuikaMusic.mp3"));
        suikaMusic.setLooping(true);
        suikaMusic.setVolume(0.5f);
        suikaMusic.play();

        //music button and its style // on and off
        musicButtonTexture = new Texture(Gdx.files.internal("MusicEnabled.png"));
        musicButtonPressedTexture = new Texture(Gdx.files.internal("MusicDisabled.png"));
        ImageButton.ImageButtonStyle musicButtonStyle = new ImageButton.ImageButtonStyle();
        musicButtonStyle.up = new TextureRegionDrawable(musicButtonTexture);
        musicButtonStyle.checked = new TextureRegionDrawable(musicButtonPressedTexture);
        musicButton = new ImageButton(musicButtonStyle);
        menuStage.addActor(musicButton);
        Gdx.app.log("MusicButton", "Button added to screen");
        musicButton.getImage().setScale(0.01f); // This will reduce the size
        musicButton.setSize(0.28f, 0.20f);
        musicButton.setPosition(0.17f * viewportWidth, 0.3f * viewportHeight); // Moves the button upwards

        musicButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (isSoundEnabled) {
                    buttonSound.play();
                }
                isMusicPlaying = !isMusicPlaying;
                musicButton.setChecked(!isMusicPlaying); // Toggle music button state
                if (isMusicPlaying) {
                    suikaMusic.play();
                } else {
                    suikaMusic.pause();
                }
            }
        });

        //music button and its style // on and off
        soundButtonTexture = new Texture(Gdx.files.internal("soundButtonEnabled.png"));
        soundButtonPressedTexture = new Texture(Gdx.files.internal("soundButtonDisabled.png"));
        ImageButton.ImageButtonStyle soundButtonStyle = new ImageButton.ImageButtonStyle();
        soundButtonStyle.up = new TextureRegionDrawable(soundButtonTexture);
        soundButtonStyle.checked = new TextureRegionDrawable(soundButtonPressedTexture);
        soundButton = new ImageButton(soundButtonStyle);
        menuStage.addActor(soundButton);
        Gdx.app.log("SoundButton", "Button added to screen");
        soundButton.getImage().setScale(0.01f); // This will reduce the size
        soundButton.setSize(0.28f, 0.20f);
        soundButton.setPosition(0.55f * viewportWidth, 0.3f * viewportHeight); // Moves the button upwards

        soundButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isSoundEnabled = !isSoundEnabled;
                soundButton.setChecked(!isSoundEnabled); // Toggle music button state
                if (!isSoundEnabled){
                    dropSound.stop();
                    buttonSound.stop();
                } else {
                    isSoundEnabled=true;
                }
            }
        });

        // GAME OVER STAGE UI AND BUTTONS
        gameOverStage = new Stage(viewport);
        gameOverTexture = new Texture("gameover3.png");
        TextureRegion gameOvertextureRegion = new TextureRegion(gameOverTexture);
        Image gameOverUI = new Image(gameOvertextureRegion);
        gameOverUI.setScale(0.7f);
        gameOverUI.setSize(1.2F, 1.5f);
        gameOverUI.setPosition(0.13f * viewportWidth, 0.25f * viewportHeight);
        gameOverStage.addActor(gameOverUI);

        retryTexture = new Texture(Gdx.files.internal("retryGameOver.png"));
        retryTextureRegion = new TextureRegion(retryTexture);
        retryTextureRegionDrawable = new TextureRegionDrawable(retryTextureRegion);
        retryButton = new ImageButton(retryTextureRegionDrawable);
        retryButton.getImage().setScale(0.3f); // This will reduce the size
        retryButton.setSize(0.01f, 0.01f);
        retryButton.setPosition(0.23f * viewportWidth, 0.26f * viewportHeight);
        gameOverStage.addActor(retryButton);

        exitTexture = new Texture(Gdx.files.internal("exitGameOver.png"));
        exitTextureRegion = new TextureRegion(exitTexture);
        exitTextureRegionDrawable = new TextureRegionDrawable(exitTextureRegion);
        exitButton = new ImageButton(exitTextureRegionDrawable);
        exitButton.getImage().setScale(0.3f); // This will reduce the size
        exitButton.setSize(0.01f, 0.01f);
        exitButton.setPosition(0.53f * viewportWidth, 0.26f * viewportHeight);
        gameOverStage.addActor(exitButton);

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (isSoundEnabled) {
                    buttonSound.play();
                }
                Gdx.app.log("ExitButton", "Button clicked");
                isMusicPlaying=false;
                suikaMusic.pause();

                game.setScreen(new FirstScreen(game));
            }
        });

        retryButton.addListener(new ChangeListener() {
                                    @Override
                                    public void changed(ChangeEvent event, Actor actor) {
                                        if (isSoundEnabled) {
                                            buttonSound.play();
                                        }
                                        resetGame();
                                        isRestarting = false;
                                        isTouching = false;
                                        isGameActive = true;
                                        Gdx.input.setInputProcessor(gameStage);

                                    }
                                }
        );
        //leaderboard stage
        leaderboardBatch = new SpriteBatch();
        leaderboardBG = new Texture("leaderboardBackground.png");

        leaderboardStage = new Stage(viewport);

        //leaderboard button
        leaderboardButtonTexture = new Texture("leaderboard.png");
        leaderboardButtonTextureRegion = new TextureRegion(leaderboardButtonTexture);
        leaderboardButtonTextureRegionDrawable = new TextureRegionDrawable(leaderboardButtonTextureRegion);

        ImageButton.ImageButtonStyle leaderboardButtonStyle = new ImageButton.ImageButtonStyle();
        leaderboardButtonStyle.up = quitTextureRegionDrawable;
        leaderboardButton = new ImageButton(leaderboardButtonStyle);

        leaderboardButton = new ImageButton(leaderboardButtonTextureRegionDrawable);
        leaderboardButton.getImage().setScale(0.107f); // This will reduce the size by 90%
        leaderboardButton.setSize(0.01f, 0.01f);
        leaderboardButton.setPosition(0.157f * viewportWidth, 0.902f * viewportHeight); // Moves the button upwards
        gameStage.addActor(leaderboardButton);

        leaderboardButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("leaderboardButton", "Button clicked");
                if (isSoundEnabled) {
                    buttonSound.play();
                }

                if (!inLeaderboard) {
                    inLeaderboard = true;
                    Gdx.input.setInputProcessor(leaderboardStage); // Switch input processor to the leaderboard stage
                }
            }
        });

        //leadeboard back button
        leaderboardBackTexture = new Texture("leaderboardBack.png");
        leaderboardBackTextureRegion = new TextureRegion(leaderboardBackTexture);
        leaderboardBackTextureRegionDrawable = new TextureRegionDrawable(leaderboardBackTextureRegion);
        leaderboardBackButton = new ImageButton(leaderboardBackTextureRegionDrawable);
        leaderboardBackButton.getImage().setScale(0.15f); // This will reduce the size by 90%
        leaderboardBackButton.setSize(0.1f, 0.1f);
        leaderboardBackButton.setPosition(0.1f * viewportWidth, 0.04f * viewportHeight); // Moves the button upwards
        leaderboardStage.addActor(leaderboardBackButton);

        leaderboardBackButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (isSoundEnabled) {
                    buttonSound.play();
                }
                Gdx.app.log("leaderboardBackButton", "Button clicked");
                inLeaderboard = false;
                isTouching = false;
                Gdx.input.setInputProcessor(gameStage); // Switch input processor to the leaderboard stage

            }
        });
    }

    @Override
    public void render(float delta) {

        input();
        logic();
        update(Gdx.graphics.getDeltaTime());

        if (isPaused) {
            // Render the pause menu stage when the game is paused
            menuStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f)); // Update pause menu stage
            menuStage.draw(); // Draw the pause menu stage

        } else if (isRestarting) {  // Check if we should show the restart UI
            //render the restart ui
            restartStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f)); // Update restart stage
            restartStage.draw(); // Draw the restart UI


        } else if (inLeaderboard) {

            leaderboardBatch.begin();
            leaderboardBatch.draw(leaderboardBG, 0, 0, WORLD_WIDTH / PPM, WORLD_HEIGHT / PPM);
            leaderboardBatch.end();

            leaderboardStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
            leaderboardStage.draw();

        } else {
            ScreenUtils.clear(Color.BLACK);
            viewport.apply();

            batch.begin();
            batch.draw(background, 0, 0, WORLD_WIDTH / PPM, WORLD_HEIGHT / PPM);

            // Draw the floating flower
            if (isGameActive) { // shu change: only draw the floating flower if the game is active
                floatingFlowerSprite.draw(batch);
            }

            // Draw existing flowers
            for (Body body : flowers) {
                if (body.getUserData() instanceof FlowerData) {
                    FlowerData flowerData = (FlowerData) body.getUserData();
                    Sprite flowerSprite = flowerData.sprite;
                    flowerSprite.setPosition(
                        (body.getPosition().x + .5f / PPM) - flowerSprite.getWidth() / 2,
                        (body.getPosition().y + .5f / PPM) - flowerSprite.getHeight() / 2
                    );
                    flowerSprite.setRotation(MathUtils.radiansToDegrees * body.getAngle());
                    flowerSprite.draw(batch);
                }
            }

            // Draw the next flower
            if (isGameActive) { // shu change: only draw the next flower if the game is active
                renderNextFlower(batch);
            }


            batch.end();

            // Only render the guideline and debug visuals if the game is active
            if (isGameActive) { // shu change: use isGameActive instead of !isGameOver()
                shapeRenderer.setProjectionMatrix(camera.combined);
                shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                shapeRenderer.setColor(Color.WHITE);
                shapeRenderer.line((floatingFlowerSprite.getX() + floatingFlowerSprite.getWidth() / 2), floatingFlowerSprite.getY(), (floatingFlowerSprite.getX() + floatingFlowerSprite.getWidth() / 2), 33.5f / PPM);
                shapeRenderer.end();

                if (debugMode) {
                    // Show the end game line
                    shapeRenderer.setProjectionMatrix(camera.combined);
                    shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                    shapeRenderer.setColor(Color.RED);
                    float gameOverHeight = WORLD_HEIGHT / PPM - 60 / PPM; // Game-over height
                    shapeRenderer.line(0, gameOverHeight, WORLD_WIDTH / PPM, gameOverHeight);
                    shapeRenderer.end();
                    debugRenderer.render(world, camera.combined);
                }
            }
            // Draw the game-over screen if the game is over
            if (!isGameActive) { // shu change: use isGameActive instead of isGameOver()
                Gdx.input.setInputProcessor(gameOverStage);
                gameOverStage.draw();
            }

            gameStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
            gameStage.draw();
        }
    }
    private void input() {

        // if the game is paused or any ui pops up, clicking buttons wont accidentally drop flowers
        if (!isGameActive || isPaused || isRestarting || inLeaderboard || ifInMain) return; // shu change: stop processing input if the game is over

        touchPos.set(Gdx.input.getX(), Gdx.input.getY());
        viewport.unproject(touchPos);
        floatingFlowerSprite.setCenterX(MathUtils.clamp(touchPos.x, 1.5f / PPM, (112.5f - floatingFlowerSprite.getWidth() * PPM) / PPM));

        //IF YOU TAP / CLICK MOUSE 1 IT DROPS FLOWERS
        if (Gdx.input.isTouched()) {
            //first touch prevents drop sound from looping
            if (isSoundEnabled) {
                if(!firstTouch) {
                    dropSound.play();
                    firstTouch = true;
                }
            }

            touchPos.set(Gdx.input.getX(), Gdx.input.getY());
            viewport.unproject(touchPos);


            float newX = MathUtils.clamp(
                touchPos.x - floatingFlowerSprite.getWidth() / 2, // Center the touch position
                23 / PPM, // Left bound
                (96 - floatingFlowerSprite.getWidth() * PPM) / PPM // Right bound
            );
            floatingFlowerSprite.setX(newX);

            isTouching = true;
        } else if (isTouching) {
            // If the screen was being touched and is no longer touched, drop the flower
            float spawnX = floatingFlowerSprite.getX() + floatingFlowerSprite.getWidth() / 2;
            float spawnY = floatingFlowerSprite.getY();
            createFlower(currentFlowerType, spawnX, spawnY, false); // Create the flower

            currentFlowerType = nextFlowerType;
            nextFlowerType = getNextFlowerType(currentFlowerType);

            Texture flowerTexture = getTextureForFlowerType(currentFlowerType);
            floatingFlowerSprite.setTexture(flowerTexture);
            floatingFlowerSprite.setSize(currentFlowerType.getRadius() * 2, currentFlowerType.getRadius() * 2);

            isTouching = false;
        } else {
            firstTouch = false;
        }
        // THIS TOGGLES THE HITBOXES
        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.F3)) {
            debugMode = !debugMode;
            Gdx.app.log("Debug", "Debug mode: " + (debugMode ? "ON" : "OFF"));
        }

    }

    public enum FlowerType {
        //STEP 3 OF ADDING NEW FLOWER: THIS WILL SET THE FLOWER LEVEL AND ITS SIZE
        DAFFODIL(1, 4 / PPM),
        BUTTERCUP(2, 6 / PPM),
        MARRIGOLD(3, 8 / PPM),
        CHERRYBLOSSOM(4, 10 / PPM),
        ORCHID(5, 12 / PPM),
        // PLEASE RENAME THESE BELOW!
        SUNFLOWER(6, 14 / PPM),
        JASMINE(7, 16 / PPM),
        FREESIA(8, 18 / PPM),
        DAISY(9, 20 / PPM),
        DANDELION(10, 22 / PPM),
        HIBISCUS(11, 24 / PPM);

        private final int level;
        private final float radius;

        FlowerType(int level, float radius) {
            this.level = level;
            this.radius = radius;
        }

        public int getLevel() {
            return level;
        }

        public float getRadius() {
            return radius;
        }

        public static FlowerType getNextType(FlowerType currentType) {
            int nextLevel = currentType.getLevel() + 1;
            for (FlowerType type : values()) {
                if (type.getLevel() == nextLevel) {
                    return type;
                }
            }
            return null;
        }
    }

    // FOR SPRITES TO LOAD ON DIFFERENT FLOWER TYPES
    public class FlowerData {
        public Sprite sprite;
        public FlowerType type;

        public FlowerData(Sprite sprite, FlowerType type) {
            this.sprite = sprite;
            this.type = type;
        }
    }


    //THIS CYCLES THE FLOWERS
    private void cycleFlowerType() {
        FlowerType[] flowerTypes = {
            FlowerType.DAFFODIL,
            FlowerType.BUTTERCUP,
            FlowerType.MARRIGOLD,
            FlowerType.CHERRYBLOSSOM
        };
        int currentIndex = Arrays.asList(flowerTypes).indexOf(currentFlowerType);
        int nextIndex = (currentIndex + 1) % flowerTypes.length; // Cycle through the 4 types
        currentFlowerType = flowerTypes[nextIndex];

        // Update the floating flower sprite
        Texture flowerTexture = getTextureForFlowerType(currentFlowerType);
        floatingFlowerSprite.setTexture(flowerTexture);
        floatingFlowerSprite.setSize(currentFlowerType.getRadius() * 2, currentFlowerType.getRadius() * 2);
    }

    private Texture getTextureForFlowerType(FlowerType type) {
        switch (type) {
            case DAFFODIL:
                return daffidol;
            case BUTTERCUP:
                return buttercup;
            case MARRIGOLD:
                return marrigold;
            case CHERRYBLOSSOM:
                return cherryblossom;
            case ORCHID: //NAME OF FLOWER
                return orchid; //THIS IS THE TEXTURE
            case SUNFLOWER:
                return sunflower;
            case JASMINE:
                return jasmine;
            case FREESIA:
                return freesia;
            case DAISY:
                return daisy;
            case DANDELION:
                return dandelion;
            case HIBISCUS:
                return hibiscus;
            //STEP 4 OF ADDING NEW FLOWER: ADD THE NAME OF THE FLOWER AND THE TEXTURE
            default:
                return hibiscus; // Default texture
        }
    }


    private Body createFlower(FlowerType type) {
        return createFlower(type, floatingFlowerSprite.getX(), floatingFlowerSprite.getY(), false);
    }

    //THIS CREATES THE FLOWERS
    private Body createFlower(FlowerType type, float x, float y, boolean isMerging) {
        if (!isMerging && !Arrays.asList(
            FlowerType.DAFFODIL,
            FlowerType.BUTTERCUP,
            FlowerType.MARRIGOLD,
            FlowerType.CHERRYBLOSSOM
        ).contains(type)) {
            Gdx.app.log("Error", "Invalid flower type for creation: " + type);
            return null;
        }

        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(x, y);
        Body flowerBody = world.createBody(def);

        CircleShape circle = new CircleShape();
        circle.setRadius(type.getRadius() * .84f); // Adjust hitbox size

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.11f;

        flowerBody.createFixture(fixtureDef);
        circle.dispose();

        Texture flowerTexture = getTextureForFlowerType(type);
        Sprite newFlowerSprite = new Sprite(flowerTexture);
        newFlowerSprite.setSize(type.getRadius() * 2, type.getRadius() * 2); // Set size based on radius
        newFlowerSprite.setOriginCenter();
        flowerBody.setUserData(new FlowerData(newFlowerSprite, type));
        flowers.add(flowerBody);

        return flowerBody;
    }
    // STOPS THE PLAYER GOING OUTSIDE
    private void logic() {
        floatingFlowerSprite.setX(MathUtils.clamp(floatingFlowerSprite.getX(), 1.5f / PPM, (112.5f - floatingFlowerSprite.getWidth() * PPM) / PPM));
    }

    // CREATES THE WALL AND FLOORS
    private void createPlatform(float posX, float posY, float width, float height) {
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.StaticBody;
        def.position.set(posX, posY);

        Body platform = world.createBody(def);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width, height);

        platform.createFixture(shape, 1);
        shape.dispose();
    }


    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        camera.update();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        leaderboardBatch.setProjectionMatrix(viewport.getCamera().combined);


    }

    @Override
    public void pause() {
        if (suikaMusic.isPlaying()) {
            suikaMusic.pause();
        }
    }

    @Override
    public void resume() {
        if (!suikaMusic.isPlaying()) {
            suikaMusic.play();
        }
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
        daffidol.dispose();
        buttercup.dispose();
        marrigold.dispose();
        cherryblossom.dispose();
        orchid.dispose();
        world.dispose();
        debugRenderer.dispose();
        shapeRenderer.dispose();;
        sunflower.dispose();
        jasmine.dispose();
        freesia.dispose();
        daisy.dispose();
        dandelion.dispose();
        hibiscus.dispose();
        gameOverTexture.dispose();
        gameStage.dispose();
        restartStage.dispose();
        suikaMusic.dispose();
        leaderboardBatch.dispose();
        dropSound.dispose();

        //STEP 5 OF ADDING NEW FLOWER: DISPOSE FLOWER FOR BETTER PERFORMANCE I.E "newFlower.dispose();"

    }
//IF GAME FAILS
private boolean isGameOver() {
    int flowersAboveLine = 0;
    for (Body body : flowers) {
        if (body.getPosition().y > WORLD_HEIGHT / PPM - 60 / PPM) { // Adjust the line height as needed
            flowersAboveLine++;
            if (flowersAboveLine >= 4) { // Game over if # flowers cross the line
                isGameActive = false; // shu change: disable the game
                return true;
            }
        }
    }
    return false;
}


//LOOK FOR A CERTAIN UPDATE
public void update(float delta) {

    //pausing// stops movement
    if (isPaused || isRestarting || !isGameActive) return;

    world.step(1 / 360f, 6, 2);
    if (isGameOver()) {
        Gdx.app.log("Game Over", "A flower reached the top!");
        return;
    }

    Array<Body> flowersToMerge = contactListener.getFlowersToMerge();
    if (flowersToMerge.size >= 2) {
        Body flowerA = flowersToMerge.get(0);
        Body flowerB = flowersToMerge.get(1);

        if (flowerA.getUserData() instanceof FlowerData && flowerB.getUserData() instanceof FlowerData) {
            FlowerData dataA = (FlowerData) flowerA.getUserData();
            FlowerData dataB = (FlowerData) flowerB.getUserData();

            FlowerType typeA = dataA.type;
            FlowerType typeB = dataB.type;

            if (typeA == typeB) {
                FlowerType nextType = FlowerType.getNextType(typeA);
                if (nextType != null) {
                    Vector2 newPosition = new Vector2(
                        (flowerA.getPosition().x + flowerB.getPosition().x) / 2,
                        (flowerA.getPosition().y + flowerB.getPosition().y) / 2
                    );

                    // CREATE NEW FLOWER
                    Body newFlower = createFlower(nextType, newPosition.x, newPosition.y, true);
                    if (newFlower != null) {
                        newFlower.setTransform(newPosition, 0);
                    } else {
                        Gdx.app.log("Error", "Failed to create flower of type: " + nextType);
                    }

                    // REMOVE OLD FLOWERS
                    world.destroyBody(flowerA);
                    world.destroyBody(flowerB);
                    flowers.removeValue(flowerA, true);
                    flowers.removeValue(flowerB, true);
                }
            }
        }

        contactListener.clearFlowersToMerge();
    }
}
//Clearing of flowers in the field

private FlowerType getNextFlowerType(FlowerType currentType) {
    FlowerType[] flowerTypes = {
        FlowerType.DAFFODIL,
        FlowerType.BUTTERCUP,
        FlowerType.MARRIGOLD,
        FlowerType.CHERRYBLOSSOM
    };
    int currentIndex = Arrays.asList(flowerTypes).indexOf(currentType);
    int nextIndex = (currentIndex + 1) % flowerTypes.length; // Cycle through the 4 types
    return flowerTypes[nextIndex];
}
private void renderNextFlower(SpriteBatch batch) {
    // Define the position for the next flower (e.g., top-right corner)
    float nextFlowerX = WORLD_WIDTH / PPM - 13.8f / PPM; // 20 pixels from the right edge
    float nextFlowerY = WORLD_HEIGHT / PPM - 17.5f / PPM; // 20 pixels from the top edge

    // Get the texture for the next flower
    Texture nextFlowerTexture = getTextureForFlowerType(nextFlowerType);

    // Create a sprite for the next flower
    Sprite nextFlowerSprite = new Sprite(nextFlowerTexture);
    nextFlowerSprite.setSize(nextFlowerType.getRadius() * 2, nextFlowerType.getRadius() * 2);

    // Center the next flower sprite
    float centeredX = nextFlowerX - nextFlowerSprite.getWidth() / 2;
    float centeredY = nextFlowerY - nextFlowerSprite.getHeight() / 2;
    nextFlowerSprite.setPosition(centeredX, centeredY);

    // Draw the next flower
    nextFlowerSprite.draw(batch);
}
    private void resetGame() {

        for (int i = 0; i < flowers.size; i++) {
            Body flower = flowers.get(i);
            if (flower != null) {
                world.destroyBody(flower); // Destroy the flower's physics body
            }
        }
        flowers.clear();
    }

}
