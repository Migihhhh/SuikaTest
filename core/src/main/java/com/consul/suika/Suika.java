package com.consul.suika;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Suika implements ApplicationListener {
    Texture player;
    Texture background;

    ExtendViewport viewport;

    SpriteBatch batch;


    Sprite playerSprite;

    Vector2  touchPos;

    Rectangle playerHitbox;
    Rectangle ground;
    Rectangle leftLine;
    Rectangle rightLine;


    private final int WORLD_WIDTH = 110;
    private final int WORLD_HEIGHT = 192;


    @Override
    public void create() {
        player = new Texture("player.png");
        background = new Texture("gameBackground.png");

        batch = new SpriteBatch();
        viewport = new ExtendViewport(86, 165);

        playerSprite = new Sprite(player);

        playerSprite.setY(110f);
        playerSprite.setX(43f);

        playerSprite.setSize(15,15);






        touchPos  = new Vector2();



    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        batch.setProjectionMatrix(viewport.getCamera().combined);



    }

    @Override
    public void render() {
        input();
        logic();
        draw();


    }

    private void input() {
        if(Gdx.input.isTouched()){
            touchPos.set(Gdx.input.getX(), Gdx.input.getY());
            viewport.unproject(touchPos);
            playerSprite.setCenterX(touchPos.x);


        }

    }

    private void logic() {

        playerHitbox = new Rectangle(playerSprite.getX(), playerSprite.getY(), playerSprite.getWidth(), playerSprite.getHeight());
        playerSprite.setX(MathUtils.clamp(playerSprite.getX(), 21, 92.5f - playerSprite.getWidth()));

    }

    private void draw() {

        ScreenUtils.clear(Color.BLACK);
        viewport.apply();


        batch.begin();


        batch.draw(background, 0, -25, WORLD_WIDTH, WORLD_HEIGHT);
        playerSprite.draw(batch);


        batch.end();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
