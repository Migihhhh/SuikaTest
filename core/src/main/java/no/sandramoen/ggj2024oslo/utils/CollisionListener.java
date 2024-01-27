package no.sandramoen.ggj2024oslo.utils;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

import no.sandramoen.ggj2024oslo.actors.Fart;

public class CollisionListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        String entityA = contact.getFixtureA().getUserData().toString();
        String entityB = contact.getFixtureB().getUserData().toString();

        checkFartCollidesWithSensor(entityA, entityB);
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }

    private void checkFartCollidesWithSensor(String entityA, String entityB) {
        if (entityA == "fart" && entityB == "loseSensor") {
            // Fart fart = (Fart) contact.getFixtureA().getBody().getUserData();
            System.out.println("fart crossed sensor!");
        } else if (entityA == "loseSensor" && entityB == "fart") {
            System.out.println("fart crossed sensor!");
        }
    }
}
