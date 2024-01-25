package no.sandramoen.ggj2024oslo.actors.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef;

import no.sandramoen.ggj2024oslo.actors.components.ActorComponent;
import no.sandramoen.ggj2024oslo.actors.components.BodyComponent;

public class ActorFollowsBodySystem extends IteratingSystem {
    private final ComponentMapper<ActorComponent> actors = ComponentMapper.getFor(ActorComponent.class);
    private final ComponentMapper<BodyComponent> bodies = ComponentMapper.getFor(BodyComponent.class);
    private final ComponentMapper<BodyComponent> offsets = ComponentMapper.getFor(BodyComponent.class);

    public ActorFollowsBodySystem() {
        super(Family.all(ActorComponent.class, BodyComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        System.out.println("banana");

        ActorComponent actorComponent = actors.get(entity);
        BodyComponent bodyComponent = bodies.get(entity);
        BodyComponent offsetComponent = offsets.get(entity);

        float actorWidth = actorComponent.actor.getWidth();
        float actorHeight = actorComponent.actor.getHeight();
        float offsetX = offsetComponent.offset.x;
        float offsetY = offsetComponent.offset.y;

        if (bodyComponent.body.getType() == BodyDef.BodyType.StaticBody)
            return;

        actorComponent.actor.setPosition(
            (bodyComponent.body.getPosition().x - actorWidth / 2) + offsetX,
            (bodyComponent.body.getPosition().y - actorHeight / 2) + offsetY
        );

        if (!bodyComponent.body.isFixedRotation())
            actorComponent.actor.setRotation(bodyComponent.body.getAngle() * MathUtils.radiansToDegrees);
    }
}
