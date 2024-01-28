package no.sandramoen.ggj2024oslo.actors.map;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;

import no.sandramoen.ggj2024oslo.actors.components.BodyComponent;
import no.sandramoen.ggj2024oslo.actors.components.PlayerControlComponent;
import no.sandramoen.ggj2024oslo.utils.BaseActor;
import no.sandramoen.ggj2024oslo.utils.BaseGame;

public class ImpassableTerrain extends BaseActor {
    private final Vector2 bodyOffset = new Vector2(0f, 0f);

    public ImpassableTerrain(float x, float y, float width, float height, Stage stage, Engine engine, World world, String type) {
        super(x, y, stage, engine);


        if (type.equals("s")) {
            loadImage("line0");
            if (x < 0)
                bodyOffset.x = -.5f;
            else
                bodyOffset.x = .5f;
        } else if (type.equals("b")) {
            loadImage("bottom");
        }
        setSize(width, height);
        setName(type);

        Body body = createBody(world);
        entity.add(new BodyComponent(body));
        entity.add(new PlayerControlComponent());
        engine.addEntity(entity);
    }

    private Body createBody(World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.x = (getX() + getWidth() / 2) + bodyOffset.x;
        bodyDef.position.y = (getY() + getHeight() / 2) + bodyOffset.y;

        Body body = world.createBody(bodyDef);
        body.setFixedRotation(true);
        body.setUserData(this);

        PolygonShape rectangle = new PolygonShape();
        rectangle.setAsBox(getWidth() / 2, getHeight() / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = rectangle;
        fixtureDef.density = 1f;
        fixtureDef.restitution = 0.1f;

        fixtureDef.filter.categoryBits = BaseGame.BOX2D_TWO;
        fixtureDef.filter.maskBits = BaseGame.BOX2D_ALL;

        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData("impassable");
        rectangle.dispose();

        return body;
    }
}
