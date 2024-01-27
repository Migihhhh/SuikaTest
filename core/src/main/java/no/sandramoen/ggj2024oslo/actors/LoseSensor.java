package no.sandramoen.ggj2024oslo.actors;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import no.sandramoen.ggj2024oslo.actors.components.BodyComponent;
import no.sandramoen.ggj2024oslo.actors.components.PlayerControlComponent;
import no.sandramoen.ggj2024oslo.utils.BaseActor;
import no.sandramoen.ggj2024oslo.utils.BaseGame;

public class LoseSensor extends BaseActor {
    private final Vector2 bodyOffset = new Vector2(0f, 0f);

    public LoseSensor(float x, float y, float width, float height, Stage stage, Engine engine, World world) {
        super(x, y, stage, engine);

        loadImage("whitePixel");
        setColor(Color.RED);
        getColor().a = 0f;
        setSize(width, height);

        Body body = createBody(world);
        entity.add(new BodyComponent(body));
        entity.add(new PlayerControlComponent());
        engine.addEntity(entity);
    }

    public void startCountDown() {
        if (hasActions())
            return;

        addAction(Actions.forever(Actions.sequence(
            Actions.alpha(.25f, .5f),
            Actions.alpha(0f, .5f)
        )));
    }

    public void stopCountDown() {
        if (!hasActions())
            return;

        System.out.println("banana");

        clearActions();
        addAction(Actions.alpha(0f, .5f));
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
        fixtureDef.isSensor = true;

        fixtureDef.filter.categoryBits = BaseGame.BOX2D_ALL;
        fixtureDef.filter.maskBits = BaseGame.BOX2D_ALL;

        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData("loseSensor");
        rectangle.dispose();

        return body;
    }
}
