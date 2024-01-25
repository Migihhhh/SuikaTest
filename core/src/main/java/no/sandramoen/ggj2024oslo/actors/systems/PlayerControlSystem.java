package no.sandramoen.ggj2024oslo.actors.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

import no.sandramoen.ggj2024oslo.actors.components.ActorComponent;
import no.sandramoen.ggj2024oslo.actors.components.BodyComponent;
import no.sandramoen.ggj2024oslo.actors.components.PlayerControlComponent;

public class PlayerControlSystem extends IteratingSystem {
    private final ComponentMapper<BodyComponent> bodies = ComponentMapper.getFor(BodyComponent.class);
    private final float impulse = .025f;

    public PlayerControlSystem() {
        super(Family.all(ActorComponent.class, BodyComponent.class, PlayerControlComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        BodyComponent bodyComponent = bodies.get(entity);
        if (bodyComponent == null) {
            return;
        }

        if (Gdx.input.isKeyPressed(Keys.W) || Gdx.input.isKeyPressed(Keys.UP)) {
            bodyComponent.body.applyLinearImpulse(0f, impulse, bodyComponent.body.getWorldCenter().x, bodyComponent.body.getWorldCenter().y, true);
        }

        if (Gdx.input.isKeyPressed(Keys.A) || Gdx.input.isKeyPressed(Keys.LEFT)) {
            bodyComponent.body.applyLinearImpulse(-impulse, 0f, bodyComponent.body.getWorldCenter().x, bodyComponent.body.getWorldCenter().y, true);
        }

        if (Gdx.input.isKeyPressed(Keys.S) || Gdx.input.isKeyPressed(Keys.DOWN)) {
            bodyComponent.body.applyLinearImpulse(0f, -impulse, bodyComponent.body.getWorldCenter().x, bodyComponent.body.getWorldCenter().y, true);
        }

        if (Gdx.input.isKeyPressed(Keys.D) || Gdx.input.isKeyPressed(Keys.RIGHT)) {
            bodyComponent.body.applyLinearImpulse(impulse, 0f, bodyComponent.body.getWorldCenter().x, bodyComponent.body.getWorldCenter().y, true);
        }
    }
}
