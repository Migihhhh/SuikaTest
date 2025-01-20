package com.consul.suika;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
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
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;


public class Suika implements ApplicationListener {
    Texture player;
    Texture background;

    Texture fruit;

    ExtendViewport viewport;

    SpriteBatch batch;


    Sprite playerSprite;
    Sprite fruitSprite;
    Vector2  touchPos;

    Rectangle playerHitbox;
    Rectangle ground;
    Rectangle leftLine;
    Rectangle rightLine;

    private final int WORLD_WIDTH = 110;
    private final int WORLD_HEIGHT = 192;

    Box2DDebugRenderer debugRenderer;
    OrthographicCamera camera;

    World world;
    Body player2D;


    @Override
    public void create() {
        player = new Texture("player.png");
        background = new Texture("gameBackground.png");
        fruit = new Texture("fruit.png");

        batch = new SpriteBatch();
        viewport = new ExtendViewport(86, 165);

        playerSprite = new Sprite(player);
        fruitSprite = new Sprite(fruit);

        playerSprite.setY(110f);
        playerSprite.setX(43f);

        playerSprite.setSize(15,15);
        fruitSprite.setSize(6,6);


        touchPos  = new Vector2();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 86, 165);

        world = new World(new Vector2(0, -15f), false);
        debugRenderer = new Box2DDebugRenderer();




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
        draw();

        createPlatform(17.5f,55.5f,1,45);
        createPlatform(68.5f,55.5f,1,45);
        createPlatform(43,8.5f,25,1);
        update(Gdx.graphics.getDeltaTime());
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
        fruitSprite.setX(MathUtils.clamp(playerSprite.getX(), 23f, 96f - playerSprite.getWidth()));

    }

    private void draw() {

        ScreenUtils.clear(Color.BLACK);
        viewport.apply();


        batch.begin();


        batch.draw(background, 0, -25, WORLD_WIDTH, WORLD_HEIGHT);
        playerSprite.draw(batch);






        batch.end();
    }

    public Body createFlower(){
        Body pBody;
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(playerSprite.getX(), playerSprite.getY());
        pBody = world.createBody(def);

        CircleShape circle = new CircleShape();
        circle.setRadius(2f);


        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.11f; // Make it bounce a little bit

        Fixture fixture = pBody.createFixture(fixtureDef);



        return pBody;
    }

    public Body createPlatform(float posx, float posy, float width, float height){
        Body ground;
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.StaticBody;
        def.position.set(posx,posy);
        ground = world.createBody(def);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width,height);

        ground.createFixture(shape,1);
        shape.dispose();

        return ground;
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
        fruit.dispose();

    }

    public void update(float delta){
        world.step(1/60f,6,2);
    }
}
