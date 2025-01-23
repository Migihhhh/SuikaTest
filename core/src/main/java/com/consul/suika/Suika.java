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

import java.util.concurrent.Flow;


public class Suika implements ApplicationListener {
    private static final float PPM = 100f; // Pixels per meter

    // World dimensions in meters
    private final int WORLD_WIDTH = 110; // in pixels
    private final int WORLD_HEIGHT = 192; // in pixels
    private FlowerContactListener contactListener = new FlowerContactListener ();

    Texture player;
    Texture background;
    Texture daffidol;
    Texture buttercup;
    Texture marrigold;
    Texture cherryblossom;
    Texture orchid;

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
        daffidol = new Texture("flower1.png");
        buttercup = new Texture("flower2.png");
        marrigold = new Texture("flower3.png");
        cherryblossom = new Texture("flower4.png");
        orchid = new Texture("flower5.png");

        batch = new SpriteBatch();

        viewport = new FitViewport(WORLD_WIDTH / PPM, WORLD_HEIGHT / PPM);
        camera = (OrthographicCamera) viewport.getCamera();

        playerSprite = new Sprite(player);
        playerSprite.setSize(15 / PPM, 15 / PPM);
        playerSprite.setPosition((WORLD_WIDTH / 2f) / PPM - playerSprite.getWidth() / 2, 135 / PPM);

        touchPos = new Vector2();

        world = new World(new Vector2(0, -9.8f), false);
        debugRenderer = new Box2DDebugRenderer();

        world.setContactListener(contactListener);

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
            if (body.getUserData() instanceof FlowerData) {
                FlowerData flowerData = (FlowerData) body.getUserData();
                Sprite flowerSprite = flowerData.sprite;
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
            createFlower(FlowerType.DAFFODIL);
        }
    }

    public enum FlowerType {
        DAFFODIL(1, 4 / PPM),
        BUTTERCUP(2, 6 / PPM),
        MARRIGOLD(3, 8 / PPM),
        CHERRYBLOSSOM(4, 10 / PPM),
        ORCHID(5, 12 / PPM),
        PEAR(6, 8 / PPM),
        PEACH(7, 9 / PPM),
        PINEAPPLE(8, 10 / PPM),
        MELON(9, 11 / PPM);

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
            return null; // No next type (e.g., melon)
        }
    }

    public class FlowerData {
        public Sprite sprite;
        public FlowerType type;

        public FlowerData(Sprite sprite, FlowerType type) {
            this.sprite = sprite;
            this.type = type;
        }
    }

    private Body createFlower(FlowerType type) {
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(playerSprite.getX() + playerSprite.getWidth() / 2, playerSprite.getY() + playerSprite.getHeight() / 2);
        Body flowerBody = world.createBody(def);

        CircleShape circle = new CircleShape();
        circle.setRadius(type.getRadius() * 0.7f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.11f;

        flowerBody.createFixture(fixtureDef);
        circle.dispose();

        Texture flowerTexture;
        switch (type) {
            case DAFFODIL:
                flowerTexture = daffidol;
                break;
            case BUTTERCUP:
                flowerTexture = buttercup;
                break;
            case MARRIGOLD:
                flowerTexture = marrigold;
                break;
            case CHERRYBLOSSOM:
                flowerTexture = cherryblossom;
                break;
            case ORCHID:
                flowerTexture = orchid;
                break;
            // Add cases for other flower types...
            default:
                flowerTexture = daffidol; // Default texture
                break;
        }

        Sprite newFlowerSprite = new Sprite(flowerTexture);
        newFlowerSprite.setSize(type.getRadius() * 2, type.getRadius() * 2 );
        newFlowerSprite.setOriginCenter();
        flowerBody.setUserData(new FlowerData(newFlowerSprite, type));
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
        world.dispose();
        debugRenderer.dispose();
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    public void update(float delta) {
        world.step(1 / 240f, 6, 2);

        // Check for flowers to merge
        Array<Body> flowersToMerge = contactListener.getFlowersToMerge();
        if (flowersToMerge.size >= 2) {
            Body flowerA = flowersToMerge.get(0);
            Body flowerB = flowersToMerge.get(1);

            if (flowerA.getUserData() instanceof FlowerData && flowerB.getUserData() instanceof FlowerData) {
                FlowerData dataA = (FlowerData) flowerA.getUserData();
                FlowerData dataB = (FlowerData) flowerB.getUserData();

                FlowerType typeA = dataA.type;
                FlowerType typeB = dataB.type;

                Gdx.app.log("Merge", "Flower A: " + typeA + ", Flower B: " + typeB);

                if (typeA == typeB) {
                    // Merge flowers
                    FlowerType nextType = FlowerType.getNextType(typeA);
                    if (nextType != null) {
                        Vector2 newPosition = new Vector2(
                            (flowerA.getPosition().x + flowerB.getPosition().x) / 2,
                            (flowerA.getPosition().y + flowerB.getPosition().y) / 2
                        );

                        Gdx.app.log("Merge", "Creating new flower: " + nextType);

                        // Remove old flowers
                        world.destroyBody(flowerA);
                        world.destroyBody(flowerB);
                        flowers.removeValue(flowerA, true);
                        flowers.removeValue(flowerB, true);

                        // Create new flower
                        createFlower(nextType).setTransform(newPosition, 0);
                    }
                }
            }

            // Clear the merge list after processing
            contactListener.clearFlowersToMerge();
        }
    }
}
