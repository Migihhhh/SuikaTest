package no.sandramoen.ggj2024oslo.screens.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.github.tommyettinger.textra.TypingLabel;

import java.util.Iterator;

import box2dLight.Light;
import no.sandramoen.ggj2024oslo.FartLight;
import no.sandramoen.ggj2024oslo.actors.Fart;
import no.sandramoen.ggj2024oslo.actors.Background;
import no.sandramoen.ggj2024oslo.actors.map.ImpassableTerrain;
import no.sandramoen.ggj2024oslo.actors.map.TiledMapActor;
import no.sandramoen.ggj2024oslo.screens.shell.MenuScreen;
import no.sandramoen.ggj2024oslo.utils.AssetLoader;
import no.sandramoen.ggj2024oslo.utils.BaseActor;
import no.sandramoen.ggj2024oslo.utils.BaseGame;
import no.sandramoen.ggj2024oslo.utils.BaseScreen;
import no.sandramoen.ggj2024oslo.utils.GameUtils;
import no.sandramoen.ggj2024oslo.utils.MapLoader;

public class LevelScreen extends BaseScreen {
    private final float newFartDelayDuration = 1f;
    private final float fartSpawnHeight = 14.4f;
    private int score;

    private Fart droppingFart;
    private Array<ImpassableTerrain> impassables;

    private TypingLabel gameOverLabel;
    private TypingLabel restartLabel;
    private TypingLabel scoreLabel;
    private final TiledMapActor tilemap;
    private final TiledMap currentMap;

    private float countDown = 0f;
    private boolean isCountDown;
    private boolean isClockTicking;
    private long tickingSoundID;
    private final float countDownTo = 4f;

    private Array<FartLight> fartLights = new Array();

    public LevelScreen(TiledMap tiledMap) {
        super(tiledMap);
        currentMap = tiledMap;
        this.tilemap = new TiledMapActor(currentMap, mainStage);
        isBox2d = true;

        GameUtils.playLoopingMusic(AssetLoader.levelMusic);

        initializeLights();
        initializeActors();
        initializeGUI();
        delayedMapCenterCamera();

        AssetLoader.fartSounds.get(0).get(0).play(0f); // sound hack
    }

    @Override
    public void initialize() {
    }

    @Override
    public void update(float delta) {
        if (droppingFart != null)
            droppingFart.suspend();

        checkMergeFarts();

        if (gameOverLabel.isVisible())
            return;

        checkLooseCondition();
        if (isCountDown) {
            countDownToLoose(delta);
        }

        handleFartLights(delta);
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        Vector2 worldCoordinates = mainStage.screenToStageCoordinates(new Vector2(screenX, screenY));
        if (droppingFart != null) {
            droppingFart.setPosition(new Vector2(worldCoordinates.x, fartSpawnHeight));
        }
        return super.mouseMoved(screenX, screenY);
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.ESCAPE || keycode == Keys.Q) {
            Gdx.app.exit();
        } else if (keycode == Keys.R) {
            BaseGame.setActiveScreen(new LevelScreen(currentMap));
        } else if (keycode == Keys.F1) {
            toggleWorldDebug();
        } else if (keycode == Keys.F2) {
            toggleDebug(uiStage);
        } else if (keycode == Keys.F3) {
            BaseGame.setActiveScreen(new MenuScreen());
        }
        return super.keyDown(keycode);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // GameUtils.printMousePosition(screenX, screenY, mainStage);

        if (droppingFart != null && !gameOverLabel.isVisible()) {
            droppingFart = null;
            new BaseActor(0f, 0f, mainStage).addAction(Actions.sequence(
                Actions.delay(newFartDelayDuration),
                Actions.run(() -> createNewFartToDrop())
            ));
        }
        return super.touchDown(screenX, screenY, pointer, button);
    }

    private void checkMergeFarts() {
        for (Actor actor : mainStage.getActors()) {
            if (actor instanceof Fart) {
                Fart fart = (Fart) actor;
                if (fart.isRemoving) {
                    if (fart.spawnNewFart != null)
                        mergeFarts(fart);
                    fart.remove();
                }
            }
        }
    }

    private void mergeFarts(Fart fart) {
        addToScore(MathUtils.ceil(fart.size));
        int currentIndex = findIndexOfSize(fart.size);
        float nextSize = getNextSize(currentIndex);
        playFartSound(currentIndex);

        Fart newFart = new Fart(fart.getX(), fart.getY(), nextSize, mainStage, engine, world);
        newFart.collide();
        fartLights.add(new FartLight(newFart, rayHandler, 2048, Color.GOLD, nextSize * 2,
            fart.getX() + nextSize / 2,
            fart.getY() + nextSize / 2
        ));
    }

    private int findIndexOfSize(float size) {
        for (int i = 0; i < BaseGame.sizes.size; i++) {
            if (BaseGame.sizes.get(i) == size) {
                return i;
            }
        }
        return -1; // Size not found in the array
    }

    private float getNextSize(int currentIndex) {
        int nextIndex = currentIndex + 1;
        if (nextIndex < BaseGame.sizes.size) {
            return BaseGame.sizes.get(nextIndex);
        } else {
            // Return the last size if there is no next size
            return BaseGame.sizes.get(BaseGame.sizes.size - 1);
        }
    }

    private void playFartSound(int currentIndex) {
        Array<Sound> levelSounds = AssetLoader.fartSounds.get(currentIndex);
        int random = MathUtils.random(levelSounds.size - 1);
        Sound randomSound = levelSounds.get(random);

        // Calculate modifier based on the current level using a logarithmic function
        float minModifier = 0.2f;
        float maxModifier = 1.0f;

        for (int i = 0; i <= 10; i++) {
            float modifier = calculateLogarithmicModifier(i, minModifier, maxModifier);
            // System.out.println("Level " + i + ": m = " + modifier);
        }

        float modifier = calculateLogarithmicModifier(currentIndex, minModifier, maxModifier);

        modifier = MathUtils.clamp(modifier, minModifier, maxModifier);
        randomSound.play(BaseGame.soundVolume * modifier);
    }

    private float calculateLogarithmicModifier(int currentIndex, float minModifier, float maxModifier) {
        float logBase = 11f;  // You can adjust the base of the logarithm as needed
        float logValue = (float) Math.log(currentIndex + 1) / (float) Math.log(logBase);
        return MathUtils.lerp(minModifier, maxModifier, logValue);
    }

    private void checkLooseCondition() {
        boolean collisionDetected = false;

        for (Actor actor : mainStage.getActors()) {
            if (actor instanceof Fart) {
                Fart fart = (Fart) actor;
                if (fart.isSensor && fart != droppingFart) {
                    collisionDetected = true;
                    break;
                }
            }
        }

        // Set countDown based on the collisionDetected flag
        if (collisionDetected) {
            // System.out.println(isCountDown + ", " + !isClockTicking + ", " + (isCountDown && !isClockTicking));
            if (isCountDown && !isClockTicking) {
                tickingSoundID = AssetLoader.clockTickingSound.play(BaseGame.soundVolume);
                isClockTicking = true;
            }
            BaseGame.loseSensor.startCountDown();
            isCountDown = true;
        } else {
            if (BaseGame.loseSensor != null)
                BaseGame.loseSensor.stopCountDown();
            AssetLoader.clockTickingSound.stop(tickingSoundID);
            isCountDown = false;
            isClockTicking = false;
            countDown = 0f; // Reset countDown if there are no collisions
        }
    }

    private void countDownToLoose(float delta) {
        countDown += delta;
        if (countDown > countDownTo)
            setGameOver();
    }

    private void createNewFartToDrop() {
        droppingFart = new Fart(
            getMouseXInWorld(),
            fartSpawnHeight,
            BaseGame.sizes.get(MathUtils.random(0, 4)),
            mainStage, engine, world
        );
    }

    private float getMouseXInWorld() {
        return mainStage.screenToStageCoordinates(new Vector2(Gdx.input.getX(), 0f)).x;
    }

    private void initializeLights() {
        initializeAmbientLight();
    }

    private void handleFartLights(float delta) {
        Iterator<FartLight> iterator = fartLights.iterator();
        while (iterator.hasNext()) {
            FartLight fartLight = iterator.next();
            if (fartLight.isRemove) {
                iterator.remove();
            } else {
                fartLight.act(delta);
            }
        }
    }

    private void initializeAmbientLight() {
        Light.setGlobalContactFilter(
            BaseGame.BOX2D_ALL,
            BaseGame.BOX2D_ALL,
            BaseGame.BOX2D_ALL
        );
        float amount = 0.05f;
        rayHandler.setAmbientLight(amount, amount, amount, 1f);
        rayHandler.setBlurNum(1);
        rayHandler.setShadows(true);
    }

    private void initializeActors() {
        new Background(0, 0, mainStage);
        impassables = new Array();
        loadActorsFromMap();
    }

    private void loadActorsFromMap() {
        new MapLoader(mainStage, engine, world, tilemap, impassables);
        droppingFart = new Fart(4.5f, fartSpawnHeight, BaseGame.sizes.first(), mainStage, engine, world);
    }

    private void addToScore(int points) {
        score += points * points * points;
        scoreLabel.setText("{COLOR=#000000}{FAST}Score: " + score);
        scoreLabel.restart();
    }

    private void setGameOver() {
        if (droppingFart != null)
            droppingFart.remove();
        gameOverLabel.setVisible(true);
        gameOverLabel.restart();
        restartLabel.setVisible(true);
        restartLabel.restart();

        AssetLoader.levelMusic.stop();
        AssetLoader.gameOverSound.play(BaseGame.soundVolume);
        AssetLoader.clockTickingSound.stop(tickingSoundID);
    }

    private void delayedMapCenterCamera() {
        new BaseActor(0, 0, mainStage).addAction(Actions.run(() -> {
            TiledMapActor.centerPositionCamera(mainStage);
            OrthographicCamera camera = (OrthographicCamera) mainStage.getCamera();
            camera.zoom = 1f;
        }));
    }

    private void initializeGUI() {
        gameOverLabel = new TypingLabel("{COLOR=#000000}{SLOWER}G A M E   O V E R !", AssetLoader.getLabelStyle("Play-Bold59white"));
        gameOverLabel.setAlignment(Align.top);
        gameOverLabel.setVisible(false);

        scoreLabel = new TypingLabel("{COLOR=#000000}{FAST}Score: 0", AssetLoader.getLabelStyle("Play-Bold59white"));
        scoreLabel.setAlignment(Align.top);

        restartLabel = new TypingLabel("{COLOR=#000000}{SLOWER}press 'r' to restart", AssetLoader.getLabelStyle("Play-Bold40white"));
        restartLabel.setAlignment(Align.top);
        restartLabel.setVisible(false);

        Table gameOverTable = new Table();
        gameOverTable.add(gameOverLabel)
            .padBottom(Gdx.graphics.getHeight() * .02f)
            .row();
        gameOverTable.add(restartLabel);

        uiTable.defaults()
            .padTop(Gdx.graphics.getHeight() * .02f);
        uiTable.add(scoreLabel)
            .expandY()
            .top()
            .row();
        uiTable.add(gameOverTable)
            .height(gameOverLabel
                .getPrefHeight() * 1.5f)
            .expandY()
            .top()
            .padBottom(Gdx.graphics.getHeight() * .18f)
            .row();
        // uiTable.setDebug(true);
    }
}
