package no.sandramoen.ggj2024oslo.actors.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class BodyComponent implements Component {
    public Body body;
    public Vector2 offset;

    public BodyComponent(Body body) {
        this.body = body;
        this.offset = new Vector2();
    }

    public BodyComponent(Body body, Vector2 offset) {
        this.body = body;
        this.offset = offset;
    }
}
