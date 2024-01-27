package no.sandramoen.ggj2024oslo.actors;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;

import no.sandramoen.ggj2024oslo.actors.components.BodyComponent;
import no.sandramoen.ggj2024oslo.actors.components.PlayerControlComponent;
import no.sandramoen.ggj2024oslo.actors.map.TiledMapActor;
import no.sandramoen.ggj2024oslo.utils.BaseActor;
import no.sandramoen.ggj2024oslo.utils.BaseGame;

public class Fart extends BaseActor {
    public Body body;
    public boolean isRemoving;
    public boolean isSensor;
    public Vector2 spawnNewFart;
    public float size;
    public enum Type {RED, YELLOW, BLUE}
    public Engine engine;
    public World world;

    private Vector2 tempPosition;

    public Fart(float x, float y, float size, Stage stage, Engine engine, World world) {
        super(x, y, stage, engine);
        this.engine = engine;
        this.world = world;

        tempPosition = new Vector2(x, y);

        loadImage("farts/fart1");
        this.size = size;
        setSize(size, size);
        setOrigin(Align.center);

        body = createBody(world);
        entity.add(new BodyComponent(body));
        entity.add(new PlayerControlComponent());
        engine.addEntity(entity);
    }

    @Override
    public boolean remove() {
        body.setTransform(1_000f, 1_000f, 0f);
        body.setActive(false);
        return super.remove();
    }

    public void setPosition(Vector2 vector2) {
        tempPosition = vector2;
        body.setTransform(vector2, 0f);
        body.setLinearVelocity(0, 0);
    }

    public void suspend() {
        body.setTransform(tempPosition, 0f);
        body.setLinearVelocity(0, 0);
    }

    private final Vector2 bodyOffset = new Vector2(0f, 0f);

    private void shakeCamera(float duration) {
        isShakyCam = true;
        new BaseActor(0f, 0f, getStage()).addAction(Actions.sequence(
            Actions.delay(duration),
            Actions.run(() -> {
                isShakyCam = false;
                TiledMapActor.centerPositionCamera(getStage());
            })
        ));
    }

    private Body createBody(World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.x = (getX() + getWidth() / 2) + bodyOffset.x;
        bodyDef.position.y = (getY() + getHeight() / 2) + bodyOffset.y;

        Body body = world.createBody(bodyDef);
        body.setUserData(this);

        CircleShape circle = new CircleShape();
        circle.setRadius(getWidth() / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 1f;
        fixtureDef.restitution = .175f;
        fixtureDef.friction = .025f;

        fixtureDef.filter.categoryBits = BaseGame.BOX2D_ONE;
        fixtureDef.filter.maskBits = BaseGame.BOX2D_ALL;

        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData("fart");
        circle.dispose();

        return body;
    }
}
