package com.mygdx.game.gameObjects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by mario on 5.03.18.
 */

public class Explosion {

    private Animation<TextureRegion> animation;

    private Vector2 position;

    private boolean shouldRemove;

    public Explosion(Animation<TextureRegion> animation, Vector2 position) {
        this.animation = animation;
        this.position = position;
    }

    public Animation<TextureRegion> getAnimation() {
        return animation;
    }

    public void setAnimation(Animation<TextureRegion> animation) {
        this.animation = animation;
    }

    public Vector2 getPosition() {
        return position;
    }

    public boolean shouldRemove() {
        return shouldRemove;
    }

    public void setShouldRemove(boolean shouldRemove) {
        this.shouldRemove = shouldRemove;
    }
}
