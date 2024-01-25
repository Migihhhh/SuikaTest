package no.sandramoen.ggj2024oslo.actors.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ActorComponent implements Component {
    public Actor actor;

    public ActorComponent(Actor actor) {
        this.actor = actor;
    }
}
