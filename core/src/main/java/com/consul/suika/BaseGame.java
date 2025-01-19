package com.consul.suika;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class BaseGame implements ApplicationListener {
    Texture player;
    Texture background;

    FitViewport viewport;

    SpriteBatch batch;

    Sprite backgroundSprite;

    @Override
    public void create() {
//        player = new Texture("player.png");
        background = new Texture("background.png");

        batch = new SpriteBatch();
        viewport = new FitViewport(600, 800);


        backgroundSprite = new Sprite(background);
        backgroundSprite.setSize(viewport.getWorldWidth(), viewport.getWorldHeight());



    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);


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

        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        batch.begin();

        batch.draw(background, 0, 0, worldWidth, worldHeight);

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
