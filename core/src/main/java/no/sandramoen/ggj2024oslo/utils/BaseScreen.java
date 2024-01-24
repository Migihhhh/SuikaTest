package no.sandramoen.ggj2024oslo.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import box2dLight.RayHandler;

public abstract class BaseScreen implements Screen, InputProcessor, ControllerListener {
    protected Stage mainStage;
    protected Stage uiStage;
    protected Table uiTable;

    private boolean isPause;
    protected float dtModifier = 1f;

    public World world;
    protected Box2DDebugRenderer debugRenderer;
    protected RayHandler rayHandler;
    protected boolean isBox2d = false;
    protected boolean isBox2dDebug = false;

    public BaseScreen() {
        mainStage = new Stage();
        mainStage.setViewport(new ExtendViewport(80, 45));

        uiTable = new Table();
        uiTable.setFillParent(true);
        uiStage = new Stage();
        uiStage.setViewport(new ScreenViewport());
        uiStage.addActor(uiTable);

        World.setVelocityThreshold(1.0f);
        world = new World(new Vector2(0f, -9.81f), true);
        world.setContactListener(new CollisionListener());

        debugRenderer = new Box2DDebugRenderer();

        RayHandler.setGammaCorrection(true);
        rayHandler = new RayHandler(world);
        rayHandler.setCulling(true);

        initialize();
    }

    public abstract void initialize();

    public abstract void update(float delta);

    @Override
    public void render(float delta) {
        uiStage.act(delta);
        if (!isPause) {
            mainStage.act(delta * dtModifier);
            if (isBox2d)
                rayHandler.update();
            update(delta * dtModifier);
        }

        Gdx.gl.glClearColor(.035f, .039f, .078f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        mainStage.getViewport().apply();
        mainStage.draw();

        if (isBox2d && !isPause) {
            if (isBox2dDebug)
                debugRenderer.render(world, mainStage.getCamera().combined);
            world.step(delta, 6, 2);
            rayHandler.setCombinedMatrix(mainStage.getCamera().combined, 0f, 0f, mainStage.getCamera().viewportWidth, mainStage.getCamera().viewportHeight);
            rayHandler.render();
        }

        uiStage.getViewport().apply();
        uiStage.draw();
    }

    @Override
    public void show() {
        InputMultiplexer im = (InputMultiplexer) Gdx.input.getInputProcessor();
        im.addProcessor(this);
        im.addProcessor(uiStage);
        im.addProcessor(mainStage);
    }

    @Override
    public void hide() {
        InputMultiplexer im = (InputMultiplexer) Gdx.input.getInputProcessor();
        im.removeProcessor(this);
        im.removeProcessor(uiStage);
        im.removeProcessor(mainStage);
    }

    @Override
    public void resize(int width, int height) {
        mainStage.getViewport().update(width, height, true);
        uiStage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
        isPause = true;
    }

    @Override
    public void resume() {
        isPause = false;
    }

    @Override
    public void dispose() {
        mainStage.dispose();
        uiStage.dispose();
        world.dispose();
        debugRenderer.dispose();
        rayHandler.dispose();
    }

    public void toggleWorldDebug() {
        toggleDebug(mainStage);
        isBox2dDebug = !isBox2dDebug;
    }

    public void toggleDebug(Stage stage) {
        for (Actor actor : stage.getActors()) {
            actor.setDebug(!actor.getDebug());
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {
        return false;
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode) {
        return false;
    }

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        return false;
    }

    @Override
    public void connected(Controller controller) {
    }

    @Override
    public void disconnected(Controller controller) {
    }
}
