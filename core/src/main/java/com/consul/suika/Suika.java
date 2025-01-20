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


public class Suika implements ApplicationListener {
    Texture player;
    Texture background;

    Texture flower;

    ExtendViewport viewport;

    SpriteBatch batch;


    Sprite playerSprite;
    Sprite flowerSprite;
    Vector2  touchPos;
    Rectangle playerHitbox;

    private final int WORLD_WIDTH = 110;
    private final int WORLD_HEIGHT = 192;
    private static final float PPM = 100f;

    Box2DDebugRenderer debugRenderer;
    OrthographicCamera camera;

    World world;


    Array<Body> flowers = new Array<Body>();


    @Override
    public void create() {
        player = new Texture("player.png");
        background = new Texture("gameBackground.png");
        flower = new Texture("fruit.png");

        batch = new SpriteBatch();
        viewport = new ExtendViewport(86, 165);

        playerSprite = new Sprite(player);
        playerSprite.setSize(15, 15);
        playerSprite.setPosition(43f, 110f); // Centered start position

        touchPos = new Vector2();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 86, 165);

        world = new World(new Vector2(0, -15f), false);

        debugRenderer = new Box2DDebugRenderer();

        // Create static platforms once
        createPlatform(17.5f, 55.5f, 1, 45);
        createPlatform(68.5f, 55.5f, 1, 45);
        createPlatform(43, 8.5f, 25, 1);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        batch.setProjectionMatrix(viewport.getCamera().combined);
        camera.setToOrtho(false, 86, 165);

    }

    @Override
    public void render() {
        input();
        logic();
        update(Gdx.graphics.getDeltaTime());
        draw();

        debugRenderer.render(world, camera.combined);
    }

    private void input() {
        touchPos.set(Gdx.input.getX(), Gdx.input.getY());
        viewport.unproject(touchPos);
        playerSprite.setCenterX(touchPos.x);

        if(Gdx.input.justTouched()){
            createFlower();
        }

    }

    private void logic() {

        playerHitbox = new Rectangle(playerSprite.getX(), playerSprite.getY(), playerSprite.getWidth(), playerSprite.getHeight());
        playerSprite.setX(MathUtils.clamp(playerSprite.getX(), 23f, 96f - playerSprite.getWidth()));

    }




    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();

        batch.begin();
        batch.draw(background, 0, -25, WORLD_WIDTH, WORLD_HEIGHT);
        playerSprite.draw(batch);

        for (Body body : flowers) {
            if (body.getUserData() instanceof Sprite) {
                Sprite flowerSprite = (Sprite) body.getUserData();
                System.out.println("Flower Sprite Position: " + flowerSprite.getX() + ", " + flowerSprite.getY());

                // Adjust the sprite's position to align with the body's center
                // We can keep this as is for now
                flowerSprite.setPosition(
                    body.getPosition().x - flowerSprite.getWidth() / 2, // Centering sprite based on body
                    body.getPosition().y - flowerSprite.getHeight() / 2 // Same for Y-axis
                );

                System.out.println("Flower Body Position: " + body.getPosition());

                // Update the sprite's rotation to match the body's rotation
                flowerSprite.setRotation(MathUtils.radiansToDegrees * body.getAngle());

                flowerSprite.draw(batch);

                if (body.getPosition().y < -250) {
                    flowers.removeValue(body, true);
                    world.destroyBody(body);
                    System.out.println("Flower destroyed");
                }
            }
        }

        batch.end();
    }

    private Body createFlower() {
        Body flowerBody;
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        // Adjust initial position by 3f to correct the misalignment
        def.position.set(playerSprite.getX(), playerSprite.getY());  // Adjusted for alignment
        flowerBody = world.createBody(def);
        System.out.println("Flower created at: " + flowerBody.getPosition());

        // Create a circular hitbox
        CircleShape circle = new CircleShape();
        circle.setRadius(3f); // Adjust radius to match the flower's size

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.11f;

        Fixture fixture = flowerBody.createFixture(fixtureDef);
        circle.dispose();

        // Create flower sprite
        Sprite newFlowerSprite = new Sprite(flower);
        newFlowerSprite.setSize(6, 6); // Adjust size as needed
        newFlowerSprite.setOriginCenter(); // Ensure the sprite's origin is centered

        // Attach the sprite as userData
        flowerBody.setUserData(newFlowerSprite);

        // Add the flower body to the flowers array for rendering
        flowers.add(flowerBody);

        return flowerBody;

    }


    public void createPlatform(float posx, float posy, float width, float height){
        Body ground;
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.StaticBody;
        def.position.set(posx,posy);
        ground = world.createBody(def);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width,height);

        ground.createFixture(shape,1);
        shape.dispose();

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        debugRenderer.dispose();
        world.dispose();
        batch.dispose();
        player.dispose();
        background.dispose();
        flower.dispose();

    }

    public void update(float delta){
        world.step(1/60f,6,2);
    }
}
