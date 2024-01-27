package no.sandramoen.ggj2024oslo.actors;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

import no.sandramoen.ggj2024oslo.actors.map.TiledMapActor;
import no.sandramoen.ggj2024oslo.utils.AssetLoader;
import no.sandramoen.ggj2024oslo.utils.BaseActor;
import no.sandramoen.ggj2024oslo.utils.BaseGame;

public class Background extends BaseActor {
    private Array<TextureAtlas.AtlasRegion> animationImages = new Array();

    public Background(float x, float y, Stage stage) {
        super(x, y, stage);
        animationImages.add(AssetLoader.textureAtlas.findRegion("grass"));
        animation = new Animation(2f, animationImages, Animation.PlayMode.LOOP);
        setAnimation(animation);
        setSize(TiledMapActor.mapTileWidth, TiledMapActor.mapTileHeight);
    }
}
