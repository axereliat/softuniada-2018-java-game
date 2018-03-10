package com.mygdx.game.gameObjects;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.MyGame;
import com.mygdx.game.handlers.B2DVars;
import com.mygdx.game.screens.PlayScreen;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mario on 26.11.17.
 */

public class Player {

    private static final int START_POSITION_X = MyGame.V_WIDTH / 2;
    private static final int START_POSITION_Y = 20;

    private float movementSpeed;
    private float bulletTimer;
    private float fireRate;
    private Rectangle rect;

    private List<Bullet> bullets;

    private World world;

    private Texture texture;
    private Body body;

    private Vector2 position;

    private boolean readyToFire;

    private Sound shootSound;

    public Player(World world) {
        this.shootSound = Gdx.audio.newSound(Gdx.files.internal("shot.wav"));
        this.bullets = new ArrayList<Bullet>();
        this.movementSpeed = 5;
        this.bulletTimer = 0;
        this.fireRate = 0.5f;
        this.readyToFire = false;
        this.texture = new Texture("ship.png");
        this.position = new Vector2(START_POSITION_X, START_POSITION_Y);
        //defineCollision(world);
        this.world = world;

        this.rect = new Rectangle(this.position.x + PlayScreen.PLAYER_SIZE / 2, this.position.y + PlayScreen.PLAYER_SIZE / 2, PlayScreen.PLAYER_SIZE / 2, PlayScreen.PLAYER_SIZE / 2);

    }

    private void defineCollision(World world) {
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();

        bdef.position.set(new Vector2(this.position.x + PlayScreen.PLAYER_SIZE / 2, this.position.y + PlayScreen.PLAYER_SIZE / 2));
        bdef.type = BodyDef.BodyType.DynamicBody;
        shape.setAsBox(PlayScreen.PLAYER_SIZE / 2 - 15, PlayScreen.PLAYER_SIZE / 2 - 10);
        fdef.filter.categoryBits = B2DVars.BITS_PLAYER;
        fdef.filter.maskBits = B2DVars.BITS_ASTEROID;
        fdef.shape = shape;
        this.body = world.createBody(bdef);
        this.body.createFixture(fdef).setUserData("player");
    }

    public void update(float delta, World world) {
        for (int i = 0; i < bullets.size(); i++) {
            if (bullets.get(i).shouldRemove()) {
                //world.destroyBody(bullets.get(i).getBody());
                bullets.remove(i);
                i--;
            }
        }
        for (Bullet bullet : bullets) {
            bullet.update(delta);
        }
        bulletTimer += Gdx.graphics.getRawDeltaTime();
        if (bulletTimer > fireRate){
            this.readyToFire = true;
        }
    }

    public void shoot() {
        if (readyToFire) {
            bullets.add(new Bullet(this.position, world));
            bulletTimer = 0;
            readyToFire = false;

            this.shootSound.play();
        }
    }

    public void moveLeft() {
        this.position.set(this.position.x - movementSpeed, START_POSITION_Y);
        //this.body.setTransform(body.getPosition().x - movementSpeed, body.getPosition().y, 0);
        this.rect.setPosition(rect.x - movementSpeed, rect.y);
    }

    public void moveRight() {
        this.position.set(this.position.x + movementSpeed, START_POSITION_Y);
        //this.body.setTransform(body.getPosition().x + movementSpeed, body.getPosition().y, 0);
        this.rect.setPosition(rect.x + movementSpeed, rect.y);
    }

    public void setFireRate(float fireRate) {
        this.fireRate = fireRate;
    }

    public Texture getTexture() {
        return texture;
    }

    public List<Bullet> getBullets() {
        return bullets;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void dispose() {
        this.texture.dispose();
        for (Bullet bullet : bullets) {
            bullet.dispose();
        }
    }

    public boolean collidesWith(Rectangle o) {
        return this.rect.overlaps(o);
    }
}
