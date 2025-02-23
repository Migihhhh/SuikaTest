package com.consul.suika;

import com.badlogic.gdx.Game;

public class MyGame extends Game {
    @Override
    public void create() {
        this.setScreen(new FirstScreen(this));
    }
}
