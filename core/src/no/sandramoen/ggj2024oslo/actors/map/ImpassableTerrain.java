package no.sandramoen.ggj2024oslo.actors.map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;

import no.sandramoen.ggj2024oslo.actors.utils.BaseActor;

public class ImpassableTerrain extends BaseActor {
    public ImpassableTerrain(float x, float y, float width, float height, Stage stage) {
        super(x, y, stage);
        loadImage("whitePixel");
        setColor(Color.DARK_GRAY);
        setSize(width, height);
        setBoundaryRectangle();
    }
}
