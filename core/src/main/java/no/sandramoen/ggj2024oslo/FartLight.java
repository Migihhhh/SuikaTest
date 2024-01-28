package no.sandramoen.ggj2024oslo;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import no.sandramoen.ggj2024oslo.actors.Fart;

public class FartLight extends PointLight {
    public boolean isRemove;
    private float elapsedTime;
    private float originalDistance;
    private float lifeTime = 3f;

    private Fart fart;

    public FartLight(Fart fart, RayHandler rayHandler, int rays, Color color, float distance, float x, float y) {
        super(rayHandler, rays, color, distance, x, y);
        this.fart = fart;
        originalDistance = distance;
        elapsedTime = 0.0f;
    }

    public void act(float delta) {
        // Update elapsed time
        elapsedTime += delta;

        interpolateDistance();

        if (fart != null)
            setPosition(
                fart.getX() + fart.size / 2,
                fart.getY() + fart.size / 2
            );

        // Check if it's time to remove the light
        if (fart.isRemoving || rayHandler != null && elapsedTime >= lifeTime) {
            isRemove = true;
            remove();
        }
    }

    private void interpolateDistance() {
        float fadingDistance = 0f;

        if (elapsedTime < lifeTime / 2) {
            fadingDistance = Math.min(originalDistance, elapsedTime * 10.0f);
        } else {
            // Calculate the remaining time after the initial rise
            float remainingTime = lifeTime - elapsedTime;

            // Ensure fadingDistance reaches zero before the end of lifeTime
            fadingDistance = Math.max(0.0f, originalDistance - (elapsedTime - lifeTime / 2) * 10.0f);

            // System.out.println("original: " + originalDistance + ", fading: " + fadingDistance + ", life time: " + lifeTime + ", diff: " + (lifeTime - elapsedTime));
        }

        setDistance(fadingDistance);
    }



}
