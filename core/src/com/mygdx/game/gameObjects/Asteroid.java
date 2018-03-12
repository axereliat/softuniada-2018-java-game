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

public class Asteroid {

    private boolean shouldRemove;

    private float speed;

    private Texture texture;

    private Body body;

    private Vector2 position;

    private Rectangle rect;

    private int health;

    private int award;

    public Asteroid(float x, float speed) {
        this.award = 2;
        this.shouldRemove = false;
        this.speed = speed;
        this.texture = new Texture("asteroid.png");
        this.position = new Vector2(x, MyGame.V_HEIGHT);
        this.rect = new Rectangle(this.position.x + PlayScreen.ASTEROID_SIZE / 2, this.position.y + PlayScreen.ASTEROID_SIZE / 2, PlayScreen.ASTEROID_SIZE / 2, PlayScreen.ASTEROID_SIZE / 2);
        this.health = 2;
    }

    public void update(float delta) {
        this.position.add(0, speed);
        //this.body.setTransform(new Vector2(this.body.getPosition().x, this.body.getPosition().y + speed), 0);
        this.rect.setPosition(new Vector2(this.rect.x, this.rect.y + speed));
        if (this.position.y < -this.texture.getHeight() || !this.isAlive()) {
            this.shouldRemove = true;
        }
    }

    public boolean shouldRemove() {
        return shouldRemove;
    }

    public void setShouldRemove(boolean shouldRemove) {
        this.shouldRemove = shouldRemove;
    }

    public Texture getTexture() {
        return texture;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Body getBody() {
        return body;
    }

    public void dispose() {
        this.texture.dispose();
    }

    public Rectangle getRect() {
        return rect;
    }

    public void takeDamage(int damage) {
        this.health -= damage;
    }

    public boolean isAlive() {
        return health > 0;
    }

    public int getAward() {
        return award;
    }
}
