package no.sandramoen.ggj2024oslo.screens.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.github.tommyettinger.textra.TypingLabel;

import box2dLight.Light;
import box2dLight.PointLight;
import no.sandramoen.ggj2024oslo.actors.Element;
import no.sandramoen.ggj2024oslo.actors.map.ImpassableTerrain;
import no.sandramoen.ggj2024oslo.actors.map.TiledMapActor;
import no.sandramoen.ggj2024oslo.screens.shell.MenuScreen;
import no.sandramoen.ggj2024oslo.utils.AssetLoader;
import no.sandramoen.ggj2024oslo.utils.BaseActor;
import no.sandramoen.ggj2024oslo.screens.shell.LevelSelectScreen;
import no.sandramoen.ggj2024oslo.utils.BaseGame;
import no.sandramoen.ggj2024oslo.utils.BaseScreen;
import no.sandramoen.ggj2024oslo.utils.GameUtils;
import no.sandramoen.ggj2024oslo.utils.MapLoader;

public class LevelScreen extends BaseScreen {
    private TiledMap currentMap;

    private Array<ImpassableTerrain> impassables;
    private Element player;

    private TypingLabel topLabel;

    private TiledMapActor tilemap;

    public LevelScreen(TiledMap tiledMap) {
        currentMap = tiledMap;
        this.tilemap = new TiledMapActor(currentMap, mainStage);
        isBox2d = true;

        initializeLights();
        initializeActors();
        initializeGUI();
        mapCenterCamera();
    }

    @Override
    public void initialize() {
    }

    @Override
    public void update(float delta) {
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.ESCAPE || keycode == Keys.Q) {
            Gdx.app.exit();
        } else if (keycode == Keys.R) {
            BaseGame.setActiveScreen(new LevelScreen(currentMap));
        } else if (keycode == Keys.T) {
            BaseGame.setActiveScreen(new LevelSelectScreen());
        } else if (keycode == Keys.NUMPAD_0) {
            OrthographicCamera camera = (OrthographicCamera) mainStage.getCamera();
            camera.zoom += .1f;
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
        GameUtils.printMousePosition(screenX, screenY, mainStage);
        return super.touchDown(screenX, screenY, pointer, button);
    }

    private void initializeLights() {
        new PointLight(rayHandler, 2048, Color.MAGENTA, 100, 1, 1);

        initializeAmbientLight();
    }

    private void initializeAmbientLight() {
        Light.setGlobalContactFilter(
            BaseGame.BOX2D_ONE,
            BaseGame.BOX2D_ALL,
            BaseGame.BOX2D_TWO
        );
        float amount = 0.05f;
        rayHandler.setAmbientLight(amount, amount, amount, 1f);
        rayHandler.setBlurNum(1);
        rayHandler.setShadows(true);
    }

    private void initializeActors() {
        impassables = new Array();
        loadActorsFromMap();
        // new Background(0, 0, mainStage);
    }

    private void loadActorsFromMap() {
        MapLoader mapLoader = new MapLoader(mainStage, engine, world, tilemap, player, impassables);
        player = mapLoader.player;
    }

    private void mapCenterCamera() {
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
