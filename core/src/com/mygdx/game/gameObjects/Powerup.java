package com.mygdx.game.gameObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.MyGame;
import com.mygdx.game.screens.PlayScreen;

/**
 * Created by mario on 9.03.18.
 */

public class Powerup {

    public static final int SPEED = -3;

    private Texture texture;

    private Vector2 position;

    private String type;

    private Rectangle rect;

    private boolean shouldRemove;

    public Powerup(float x, int typeIndex) {
        String[] types = new String[] { "health", "ammo" };
        this.texture = new Texture("powerup.png");
        this.position = new Vector2(x, MyGame.V_HEIGHT);
        this.type = types[typeIndex];
        this.shouldRemove = false;
        this.rect = new Rectangle(this.position.x + PlayScreen.POWERUP_SIZE / 2, this.position.y + PlayScreen.POWERUP_SIZE / 2, PlayScreen.POWERUP_SIZE / 2 + 20, PlayScreen.POWERUP_SIZE / 2 + 20);
    }

    public Vector2 getPosition() {
        return position;
    }

    public boolean shouldRemove() {
        return shouldRemove;
    }

    public void update() {
        this.position.add(0, SPEED);
        this.rect.setPosition(rect.getX(), rect.getY() + SPEED);

        if (position.y < 0) {
            this.shouldRemove = true;
        }
    }

    public Texture getTexture() {
        return texture;
    }

    public Rectangle getRect() {
        return rect;
    }

    public String getType() {
        return type;
    }

    public void setShouldRemove(boolean shouldRemove) {
        this.shouldRemove = shouldRemove;
    }
}
