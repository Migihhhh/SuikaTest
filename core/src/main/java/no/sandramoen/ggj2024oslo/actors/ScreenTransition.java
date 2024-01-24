package no.sandramoen.ggj2024oslo.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import no.sandramoen.ggj2024oslo.utils.BaseActor;

public class ScreenTransition extends BaseActor {
    public ScreenTransition(float x, float y, Stage stage) {
        super(x, y, stage);
        loadImage("whitePixel");
        setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        setTouchable(Touchable.disabled);
        setColor(Color.BLACK);
    }

    public void fadeIn(float duration) {
        getColor().a = 0f;
        addAction(Actions.fadeIn(duration));
    }

    public void fadeOut(float duration) {
        getColor().a = 1f;
        addAction(Actions.fadeOut(duration));
    }
}
