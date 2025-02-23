package com.consul.suika;

import com.badlogic.gdx.ApplicationListener;

public class GameLauncher implements ApplicationListener{
    private  MyGame game;

    @Override
    public void create() {
        game = new MyGame();
        game.create(); // This sets the initial screen
    }

    @Override
    public void resize(int width, int height) {
        game.resize(width, height); // Handle screen resizing
    }

    @Override
    public void render() {
        game.render(); // Handle rendering
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

