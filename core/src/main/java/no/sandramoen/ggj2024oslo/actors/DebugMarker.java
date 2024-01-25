package no.sandramoen.ggj2024oslo.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Align;

import no.sandramoen.ggj2024oslo.utils.BaseActor;

public class DebugMarker extends BaseActor {
    public DebugMarker(float x, float y, Stage stage) {
        super(x, y, stage);
        init();
    }

    public DebugMarker(Vector2 position, Stage stage) {
        this(position.x, position.y, stage);
    }

    private void init() {
        loadImage("whitePixel");
        setSize(.5f, .5f);
        setColor(Color.CYAN);
        centerAtPosition(getX(), getY());
        setOrigin(Align.center);

        addAction(rotateForever());
        addAction(removeAction());
    }

    private RepeatAction rotateForever() {
        return Actions.forever(Actions.rotateBy(20f, .05f));
    }

    private SequenceAction removeAction() {
        return Actions.sequence(
            Actions.fadeOut(1f),
            Actions.removeActor()
        );
    }
}
