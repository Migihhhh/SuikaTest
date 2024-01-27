package no.sandramoen.ggj2024oslo.screens.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
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
    private final float newFartDelayDuration = 2f;

    private Fart droppingFart;
    private LoseSensor loseSensor;
    private Array<ImpassableTerrain> impassables;

    private TypingLabel topLabel;
    private final TiledMapActor tilemap;
    private final TiledMap currentMap;

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
        if (droppingFart != null) {
            droppingFart = null;
            new BaseActor(0f, 0f, mainStage).addAction(Actions.sequence(
                Actions.delay(newFartDelayDuration),
                Actions.run(() -> createNewFartToDrop())
            ));
        }
        return super.touchDown(screenX, screenY, pointer, button);
    }

    private void createNewFartToDrop() {
        droppingFart = new Fart(4.5f, 15f, BaseGame.sizes.get(MathUtils.random(0, 4)), mainStage, engine, world);
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
        droppingFart = new Fart(4.5f, 15f, BaseGame.sizes.first(), mainStage, engine, world);
    }

    private void delayedMapCenterCamera() {
        new BaseActor(0, 0, mainStage).addAction(Actions.run(() -> {
            TiledMapActor.centerPositionCamera(mainStage);
            OrthographicCamera camera = (OrthographicCamera) mainStage.getCamera();
            camera.zoom = 1f;
        }));
    }

    private void initializeGUI() {
        topLabel = new TypingLabel("{SLOWER}G A M E   O V E R !", AssetLoader.getLabelStyle("Play-Bold59white"));
        topLabel.setAlignment(Align.top);

        uiTable.defaults().padTop(Gdx.graphics.getHeight() * .02f);
        uiTable.add(topLabel).height(topLabel.getPrefHeight() * 1.5f).expandY().top().row();
        // uiTable.setDebug(true);
    }
}
