package no.sandramoen.ggj2024oslo.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

import java.util.Objects;

import no.sandramoen.ggj2024oslo.actors.Fart;

public class CollisionListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        String entityA = contact.getFixtureA().getUserData().toString();
        String entityB = contact.getFixtureB().getUserData().toString();

        checkFartCollidesWithSensor(contact, entityA, entityB);
        checkFartsColliding(contact, entityA, entityB);
    }

    @Override
    public void endContact(Contact contact) {
        String entityA = contact.getFixtureA().getUserData().toString();
        String entityB = contact.getFixtureB().getUserData().toString();
        if (entityA == "fart" && entityB == "loseSensor") {
            Fart fart = (Fart) contact.getFixtureA().getBody().getUserData();
            fart.isSensor = false;
        } else if (entityA == "loseSensor" && entityB == "fart") {
            Fart fart = (Fart) contact.getFixtureB().getBody().getUserData();
            fart.isSensor = false;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }

    private void checkFartCollidesWithSensor(Contact contact, String entityA, String entityB) {
        if (entityA == "fart" && entityB == "loseSensor") {
            Fart fart = (Fart) contact.getFixtureA().getBody().getUserData();
            fart.isSensor = true;
        } else if (entityA == "loseSensor" && entityB == "fart") {
            Fart fart = (Fart) contact.getFixtureB().getBody().getUserData();
            fart.isSensor = true;
        }
    }

    private void checkFartsColliding(Contact contact, String entityA, String entityB) {
        if (!Objects.equals(entityA, "fart") || !Objects.equals(entityB, "fart"))
            return;

        Fart fartA = (Fart) contact.getFixtureA().getBody().getUserData();
        Fart fartB = (Fart) contact.getFixtureB().getBody().getUserData();

        if (fartA.size == BaseGame.sizes.get(BaseGame.sizes.size - 1))
            return;

        if (fartA.size == fartB.size) {
            // Calculate the midpoint between fartA and fartB
            float centerX = (fartA.getX() + fartA.getWidth() / 2 + fartB.getX() + fartB.getWidth() / 2) / 2;
            float centerY = (fartA.getY() + fartA.getHeight() / 2 + fartB.getY() + fartB.getHeight() / 2) / 2;
            fartA.spawnNewFart = new Vector2(centerX, centerY);

            // Mark both farts for removal
            fartA.isRemoving = true;
            fartB.isRemoving = true;
        }

    }
}
