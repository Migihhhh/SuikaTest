package com.consul.suika;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;


public class FlowerContactListener implements ContactListener {
    private Array<Body> flowersToMerge = new Array<>();

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if (fixtureA.getBody().getUserData() instanceof GameScreen.FlowerData &&
            fixtureB.getBody().getUserData() instanceof GameScreen.FlowerData) {
            GameScreen.FlowerData dataA = (GameScreen.FlowerData) fixtureA.getBody().getUserData();
            GameScreen.FlowerData dataB = (GameScreen.FlowerData) fixtureB.getBody().getUserData();

            GameScreen.FlowerType typeA = dataA.type;
            GameScreen.FlowerType typeB = dataB.type;

            Gdx.app.log("Collision", "Flower A: " + typeA + ", Flower B: " + typeB);

            if (typeA == typeB) {
                flowersToMerge.add(fixtureA.getBody());
                flowersToMerge.add(fixtureB.getBody());
            }
        }
    }

    @Override
    public void endContact(Contact contact) {}

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {}

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {}

    public Array<Body> getFlowersToMerge() {
        return flowersToMerge;
    }

    public void clearFlowersToMerge() {
        flowersToMerge.clear();
    }
}


