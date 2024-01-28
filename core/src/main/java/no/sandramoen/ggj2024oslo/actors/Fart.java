package no.sandramoen.ggj2024oslo.actors;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
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

    private Stage stage;

    public Fart(float x, float y, float size, Stage stage, Engine engine, World world) {
        super(x, y, stage, engine);
        this.stage = stage;
        this.engine = engine;
        this.world = world;

        tempPosition = new Vector2(x, y);

        this.size = size;
        loadFartImage();
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

    public void fartAnimation() {
        if (hasActions())
            return;
        shakeCamera(0.25f);
        addAction(Actions.sequence(
                Actions.scaleTo(1.2f, .8f, .4f, Interpolation.exp10Out),
                Actions.scaleTo(1f, 1f, .4f, Interpolation.bounceOut)
        ));
    }

    private final Vector2 bodyOffset = new Vector2(0f, 0f);

    private void shakeCamera(float duration) {
        shakyCamIntensity = size * size * size * .0005f;
        System.out.println("shakyCamIntensity: " + shakyCamIntensity);

        storedCameraPosition.set(stage.getCamera().position);
        isShakyCam = true;
        TemporalAction rest = new TemporalAction() {
            @Override
            protected void update(float percent) {
                stage.getCamera().position.lerp(storedCameraPosition, percent);
            }
        };
        rest.setDuration(duration * 0.25f);
        rest.setInterpolation(Interpolation.circleIn);
        new BaseActor(0f, 0f, stage).addAction(Actions.sequence(
                Actions.delay(duration * 0.75f),
                Actions.run(() -> {
                    isShakyCam = false;
                }), rest)
        );
    }

    private void loadFartImage() {
        String imagePath;

        // Check if the size is in the sizes array
        if (BaseGame.sizes.contains(size, false)) {
            // Get the index of the size in the sizes array
            int index = BaseGame.sizes.indexOf(size, false);

            // System.out.println("index: " + index);
            // Create the image path based on the index
            imagePath = "farts/fart" + index; // Assumes image filenames are like "farts/fart0", "farts/fart1", etc.
        } else {
            // Handle the case where size is not in the predefined sizes
            imagePath = "farts/fart0"; // Change this to the default image path
        }

        /*System.out.println("size: " + size + ", condition: " + BaseGame.sizes.contains(size, true));
        System.out.println("fart image path is: " + imagePath);*/
        loadImage(imagePath);
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
