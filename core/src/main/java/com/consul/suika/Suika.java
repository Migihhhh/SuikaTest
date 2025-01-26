package com.consul.suika;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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

import java.util.Arrays;
import java.util.concurrent.Flow;


public class Suika implements ApplicationListener {

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
    private FlowerType nextFlowerType;

    private FlowerType currentFlowerType = FlowerType.DAFFODIL;

    private Sprite floatingFlowerSprite;
    private boolean isTouching = false;
    Texture sunflower;
    Texture jasmine;
    Texture freesia;
    Texture daisy;
    Texture dandelion;
    Texture hibiscus;
    Texture gameOverTexture;

    //STEP 1 OF ADDING NEW FLOWER: CALL YOUR VARIABLE I.E "Texture newFlower;"

    @Override
    public void create() {
        shapeRenderer = new ShapeRenderer();
        background = new Texture("gameBackground.png");
        gameOverTexture = new Texture("gameover.png");
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
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        camera.update();
        batch.setProjectionMatrix(viewport.getCamera().combined);
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


    @Override
    public void render() {
        input();
        logic();
        update(Gdx.graphics.getDeltaTime());

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

        // Draw the game-over screen if the game is over
        if (!isGameActive) { // shu change: use isGameActive instead of isGameOver()
            batch.draw(gameOverTexture, 0, 0, WORLD_WIDTH / PPM, WORLD_HEIGHT / PPM);
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
    }
    private void input() {
        if (!isGameActive) return; // shu change: stop processing input if the game is over

        touchPos.set(Gdx.input.getX(), Gdx.input.getY());
        viewport.unproject(touchPos);
        floatingFlowerSprite.setCenterX(MathUtils.clamp(touchPos.x, 1.5f / PPM, (112.5f - floatingFlowerSprite.getWidth() * PPM) / PPM));

        //IF YOU TAP / CLICK MOUSE 1 IT DROPS FLOWERS
        if (Gdx.input.isTouched()) {
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

    //DISPOSING STUFF FOR OPTIMIZING GAME
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

        //STEP 5 OF ADDING NEW FLOWER: DISPOSE FLOWER FOR BETTER PERFORMANCE I.E "newFlower.dispose();"
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}


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
}
