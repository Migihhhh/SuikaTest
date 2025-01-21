package com.consul.suika;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;


public class Suika implements ApplicationListener {
    private static final float PPM = 100f; // Pixels per meter

    // World dimensions in meters
    private final int WORLD_WIDTH = 110; // in pixels
    private final int WORLD_HEIGHT = 192; // in pixels

    Texture player;
    Texture background;
    Texture flower;

    FitViewport viewport;
    SpriteBatch batch;

    Sprite playerSprite;
    Vector2 touchPos;

    Box2DDebugRenderer debugRenderer;
    OrthographicCamera camera;

    World world;
    Array<Body> flowers = new Array<>();

    @Override
    public void create() {
        player = new Texture("player.png");
        background = new Texture("gameBackground.png");
        flower = new Texture("fruit.png");

        batch = new SpriteBatch();

        // Initialize FitViewport with physics world units
        viewport = new FitViewport(WORLD_WIDTH / PPM, WORLD_HEIGHT / PPM);
        camera = (OrthographicCamera) viewport.getCamera();

        playerSprite = new Sprite(player);
        playerSprite.setSize(15 / PPM, 15 / PPM); // Convert to meters
        playerSprite.setPosition((WORLD_WIDTH / 2f) / PPM - playerSprite.getWidth() / 2, 135 / PPM); // Centered start position

        touchPos = new Vector2();

        world = new World(new Vector2(0, -9.8f), false);
        debugRenderer = new Box2DDebugRenderer();

        // Create static platforms
        createPlatform(22.5f / PPM, 80.5f / PPM, 1 / PPM, 45 / PPM);
        createPlatform(87.5f / PPM, 80.5f / PPM, 1 / PPM, 45 / PPM);
        createPlatform(56 / PPM, 33.5f / PPM, 36 / PPM, 1 / PPM);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        camera.update();
        batch.setProjectionMatrix(viewport.getCamera().combined);
    }

    @Override
    public void render() {
        input();
        logic();
        update(Gdx.graphics.getDeltaTime());

        ScreenUtils.clear(Color.BLACK);
        viewport.apply();

        batch.begin();
        batch.draw(background, 0, 0, WORLD_WIDTH / PPM, WORLD_HEIGHT / PPM);
        playerSprite.draw(batch);

        for (Body body : flowers) {
            if (body.getUserData() instanceof Sprite) {
                Sprite flowerSprite = (Sprite) body.getUserData();
                flowerSprite.setPosition(
                    (body.getPosition().x +.5f / PPM) - flowerSprite.getWidth() / 2,
                    (body.getPosition().y +.5f / PPM) - flowerSprite.getHeight() / 2
                );
                flowerSprite.setRotation(MathUtils.radiansToDegrees * body.getAngle());
                flowerSprite.draw(batch);
            }
        }

        batch.end();

        debugRenderer.render(world, camera.combined);
    }

    private void input() {
        touchPos.set(Gdx.input.getX(), Gdx.input.getY());
        viewport.unproject(touchPos);
        playerSprite.setCenterX(MathUtils.clamp(touchPos.x, 27.5f / PPM, (96 - playerSprite.getWidth() * PPM) / PPM));
        if (Gdx.input.justTouched()) {
            createFlower();
        }
    }

    private Body createFlower() {
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(playerSprite.getX() + playerSprite.getWidth() / 2, playerSprite.getY() + playerSprite.getHeight() / 2); // Adjust for sprite center
        Body flowerBody = world.createBody(def);

        CircleShape circle = new CircleShape();
        circle.setRadius(3 / PPM); // Adjust for PPM

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.11f;

        flowerBody.createFixture(fixtureDef);
        circle.dispose();

        Sprite newFlowerSprite = new Sprite(flower);
        newFlowerSprite.setSize(6 / PPM, 6 / PPM );
        newFlowerSprite.setOriginCenter();
        flowerBody.setUserData(newFlowerSprite);
        flowers.add(flowerBody);

        return flowerBody;
    }

    private void logic() {
        playerSprite.setX(MathUtils.clamp(playerSprite.getX(), 23 / PPM, (96 - playerSprite.getWidth() * PPM) / PPM));
    }

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
    public void dispose() {
        batch.dispose();
        player.dispose();
        background.dispose();
        flower.dispose();
        world.dispose();
        debugRenderer.dispose();
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    public void update(float delta) {
        world.step(1 / 230f, 6, 2);
    }
}
