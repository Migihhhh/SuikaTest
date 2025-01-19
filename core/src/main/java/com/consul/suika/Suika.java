package com.consul.suika;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Suika implements ApplicationListener {
    Texture player;
    Texture background;

    ExtendViewport viewport;

    SpriteBatch batch;

    Sprite backgroundSprite;

    private final int WORLD_WIDTH = 110;
    private final int WORLD_HEIGHT = 192;

    @Override
    public void create() {
//        player = new Texture("player.png");
        background = new Texture("gameBackground.png");

        batch = new SpriteBatch();
        viewport = new ExtendViewport(86, 165);


        backgroundSprite = new Sprite(background);



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

    }

    private void logic() {

    }

    private void draw() {

        ScreenUtils.clear(Color.BLACK);
        viewport.apply();


        batch.begin();

        batch.draw(background, 0, -25, WORLD_WIDTH, WORLD_HEIGHT);

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
