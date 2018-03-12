package com.mygdx.game.gameObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.MyGame;
import com.mygdx.game.screens.PlayScreen;

/**
 * Created by mario on 24.11.17.
 */

public class Bullet {

    private boolean shouldRemove;

    private float speed;

    private Texture texture;
    private Body body;
    private Rectangle rect;

    private Vector2 position;

    public Bullet(Vector2 playerPos) {
        this.shouldRemove = false;
        this.speed = 5;
        this.texture = new Texture("bullet.png");
        this.position = new Vector2(playerPos.x + PlayScreen.PLAYER_SIZE / 2 - 15, playerPos.y + PlayScreen.PLAYER_SIZE - 30);
        this.rect = new Rectangle(this.position.x + PlayScreen.BULLET_SIZE / 2, this.position.y + PlayScreen.BULLET_SIZE / 2, PlayScreen.BULLET_SIZE, PlayScreen.BULLET_SIZE);
    }

    public void update(float delta) {
        if (position.y > MyGame.V_HEIGHT + this.texture.getHeight()) {
            shouldRemove = true;
        }
        this.position.add(0, speed);
        this.rect.setPosition(new Vector2(this.rect.x, this.rect.y + speed));
    }

    public boolean shouldRemove() {
        return shouldRemove;
    }

    public Texture getTexture() {
        return texture;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void dispose() {
        this.texture.dispose();
    }

    public Body getBody() {
        return body;
    }

    public boolean collidesWith(Rectangle o) {
        return this.rect.overlaps(o);
    }

    public void setShouldRemove(boolean shouldRemove) {
        this.shouldRemove = shouldRemove;
    }
}
