package no.sandramoen.ggj2024oslo.utils;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

import no.sandramoen.ggj2024oslo.actors.Element;
import no.sandramoen.ggj2024oslo.actors.map.ImpassableTerrain;
import no.sandramoen.ggj2024oslo.actors.map.TiledMapActor;

public class MapLoader {
    public Element player;
    public Array<ImpassableTerrain> impassables;

    private final TiledMapActor tilemap;
    private final Stage mainStage;
    private final Engine engine;
    private final World world;

    public MapLoader(Stage mainStage, Engine engine, World world, TiledMapActor tilemap,
                     Element player, Array<ImpassableTerrain> impassables) {
        this.tilemap = tilemap;
        this.mainStage = mainStage;
        this.engine = engine;
        this.world = world;

        this.player = player;
        this.impassables = impassables;

        initializeActor("player");
        initializeActors("impassable");
    }

    private void initializeActors(String propertyName) {
        String layerName = "actors";

        if (tilemap.getTileList(layerName, propertyName).size() <= 0)
            return;

        for (MapObject mapObject : tilemap.getTileList(layerName, propertyName)) {
            MapProperties mapProperties = mapObject.getProperties();
            float x = mapProperties.get("x", Float.class) * BaseGame.UNIT_SCALE;
            float y = mapProperties.get("y", Float.class) * BaseGame.UNIT_SCALE;
            float width = mapProperties.get("width", Float.class) * BaseGame.UNIT_SCALE;
            float height = mapProperties.get("height", Float.class) * BaseGame.UNIT_SCALE;
            impassables.add(new ImpassableTerrain(x, y, width, height, mainStage));
        }
    }

    private void initializeActor(String propertyName) {
        String layerName = "actors";
        if (tilemap.getTileList(layerName, propertyName).size() == 1) {
            MapObject mapObject = tilemap.getTileList(layerName, propertyName).get(0);
            float x = mapObject.getProperties().get("x", Float.class) * BaseGame.UNIT_SCALE;
            float y = mapObject.getProperties().get("y", Float.class) * BaseGame.UNIT_SCALE;
            player = new Element(x, y, mainStage, engine, world);
        } else if (tilemap.getTileList(layerName, propertyName).size() > 1) {
            Gdx.app.error(getClass().getSimpleName(), "Error => found more than one property: " + propertyName + " on layer: " + layerName + "!");
        } else {
            Gdx.app.error(getClass().getSimpleName(), "Error => found no property: " + propertyName + " on layer: " + layerName + "!");
            player = null;
        }
    }
}
