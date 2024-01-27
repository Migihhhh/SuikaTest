package no.sandramoen.ggj2024oslo.screens.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.github.tommyettinger.textra.TypingLabel;

import box2dLight.Light;
import box2dLight.PointLight;
import no.sandramoen.ggj2024oslo.actors.Fart;
import no.sandramoen.ggj2024oslo.actors.Background;
import no.sandramoen.ggj2024oslo.actors.LoseSensor;
import no.sandramoen.ggj2024oslo.actors.map.ImpassableTerrain;
import no.sandramoen.ggj2024oslo.actors.map.TiledMapActor;
import no.sandramoen.ggj2024oslo.screens.shell.MenuScreen;
import no.sandramoen.ggj2024oslo.utils.AssetLoader;
import no.sandramoen.ggj2024oslo.utils.BaseActor;
import no.sandramoen.ggj2024oslo.utils.BaseGame;
import no.sandramoen.ggj2024oslo.utils.BaseScreen;
import no.sandramoen.ggj2024oslo.utils.MapLoader;

public class LevelScreen extends BaseScreen {
    private final float newFartDelayDuration = 1f;
    private final float fartSpawnHeight = 15f;

    private Fart droppingFart;
    private LoseSensor loseSensor;
    private Array<ImpassableTerrain> impassables;

    private TypingLabel gameOverLabel;
    private final TiledMapActor tilemap;
    private final TiledMap currentMap;

    private float countDown = 0f;
    private boolean isCountDown;
    private final float countDownTo = 3f;

    public LevelScreen(TiledMap tiledMap) {
        super(tiledMap);
        currentMap = tiledMap;
        this.tilemap = new TiledMapActor(currentMap, mainStage);
        isBox2d = true;

        initializeLights();
        initializeActors();
        initializeGUI();
        delayedMapCenterCamera();
    }

    @Override
    public void initialize() {
    }

    @Override
    public void update(float delta) {
        if (droppingFart != null)
            droppingFart.suspend();

        if (gameOverLabel.isVisible())
            return;

        checkMergeFarts();

        checkLooseCondition();
        if (isCountDown) {
            countDownToLoose(delta);
        }
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        Vector2 worldCoordinates = mainStage.screenToStageCoordinates(new Vector2(screenX, screenY));
        if (droppingFart != null) {
            droppingFart.setPosition(new Vector2(worldCoordinates.x, 15f));
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
                    if (fart.spawnNewFart != null) {
                        int currentIndex = findIndexOfSize(fart.size);
                        float nextSize = getNextSize(currentIndex);
                        new Fart(fart.getX(), fart.getY(), nextSize, mainStage, engine, world);
                    }
                    fart.remove();
                }
            }
        }
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
            isCountDown = true;
        } else {
            isCountDown = false;
            countDown = 0f; // Reset countDown if there are no collisions
        }
    }

    private void countDownToLoose(float delta) {
        countDown += delta;
        if (countDown > countDownTo)
            gameOverLabel.setVisible(true);
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
        new PointLight(rayHandler, 2048, Color.MAGENTA, 10, 3, 2);
        new PointLight(rayHandler, 2048, Color.GREEN, 10, 11, 5);
        initializeAmbientLight();
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
        new MapLoader(mainStage, engine, world, tilemap, impassables, loseSensor);
        droppingFart = new Fart(4.5f, fartSpawnHeight, BaseGame.sizes.first(), mainStage, engine, world);
    }

    private void delayedMapCenterCamera() {
        new BaseActor(0, 0, mainStage).addAction(Actions.run(() -> {
            TiledMapActor.centerPositionCamera(mainStage);
            OrthographicCamera camera = (OrthographicCamera) mainStage.getCamera();
            camera.zoom = 1f;
        }));
    }

    private void initializeGUI() {
        gameOverLabel = new TypingLabel("{SLOWER}G A M E   O V E R !", AssetLoader.getLabelStyle("Play-Bold59white"));
        gameOverLabel.setAlignment(Align.top);
        gameOverLabel.setVisible(false);

        uiTable.defaults().padTop(Gdx.graphics.getHeight() * .02f);
        uiTable.add(gameOverLabel).height(gameOverLabel.getPrefHeight() * 1.5f).expandY().top().row();
        // uiTable.setDebug(true);
    }
}
