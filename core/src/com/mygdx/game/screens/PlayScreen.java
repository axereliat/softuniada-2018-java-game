package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.holidaystudios.tools.GifDecoder;
import com.mygdx.game.MyGame;
import com.mygdx.game.gameObjects.Asteroid;
import com.mygdx.game.gameObjects.Bullet;
import com.mygdx.game.gameObjects.Explosion;
import com.mygdx.game.gameObjects.Player;
import com.mygdx.game.gameObjects.Powerup;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by mario on 24.11.17.
 */

public class PlayScreen implements Screen {

    public static final float ASTEROID_SIZE = 70;
    public static final float PLAYER_SIZE = 90;
    public static final float BULLET_SIZE = 30;
    private static final float EXPLOSION_SIZE = 80;
    public static final float POWERUP_SIZE = 50;

    private Game game;

    private final Sound destroySound;
    private final Sound painSound;
    private final Sound powerupSound;

    private int score;

    private BitmapFont font;

    private OrthographicCamera cam;
    private Viewport viewport;

    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private Texture spaceTexture;

    private Player player;

    private List<Asteroid> asteroids;

    private float asteroidTimer;

    private float asteroidTime;

    private Animation<TextureRegion> explosionAnim;
    private float elapsed;
    private List<Explosion> explosions;

    private float asteroidTimeElapsed;

    private float powerupTimer;
    private float powerupTime;
    private Powerup powerup;
    private float powerupEndTimer;
    private boolean powerupOn;

    private Random rnd;
    private int health;

    private Preferences prefs;

    public PlayScreen(Game game) {
        this.game = game;
        this.prefs = Gdx.app.getPreferences("My Preferences");
        this.rnd = new Random();

        this.powerup = null;
        this.powerupOn = false;
        this.powerupEndTimer = 0;
        this.powerupTimer = 0;
        this.powerupTime = rnd.nextInt(7) + 8;
        this.health = 100;
        this.destroySound = Gdx.audio.newSound(Gdx.files.internal("destroy.wav"));
        this.painSound = Gdx.audio.newSound(Gdx.files.internal("pain.mp3"));
        this.powerupSound = Gdx.audio.newSound(Gdx.files.internal("powerupSound.wav"));
        this.asteroidTimeElapsed = 0;
        this.explosions = new ArrayList<Explosion>();
        this.explosionAnim = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("explosion.gif").read());
        this.score = 0;
        this.font = new BitmapFont();
        this.shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);

        this.spaceTexture = new Texture("space.png");
        this.cam = new OrthographicCamera();
        this.cam.setToOrtho(false, MyGame.V_WIDTH, MyGame.V_HEIGHT);
        this.viewport = new FitViewport(MyGame.V_WIDTH, MyGame.V_HEIGHT, cam);

        this.player = new Player();

        this.batch = new SpriteBatch();
        this.asteroids = new ArrayList<Asteroid>();
        this.asteroidTimer = 0;
        this.asteroidTime = 2;

        this.elapsed = 0;
    }

    private void update(float delta) {
        handleInput();

        powerupTimer += delta;

        if (powerupTimer >= powerupTime) {
            powerupTimer = 0;
            powerupTime = rnd.nextInt(7) + 8;
            powerup = new Powerup(rnd.nextInt(MyGame.V_WIDTH - 200) + 100, rnd.nextInt(2));
        }
        if (powerup != null) {
            if (player.collidesWith(powerup.getRect())) {
                powerupSound.play();
                if (powerup.getType().equals("health")) {
                    this.health = 100;
                }
                if (powerup.getType().equals("ammo")) {
                    player.setFireRate(0.1f);
                    powerupOn = true;
                }
                powerup.setShouldRemove(true);
            }
            powerup.update();
            if (powerup.shouldRemove()) {
                powerup = null;
            }
        }

        if (powerupOn) {
            powerupEndTimer += delta;
            if (powerupEndTimer > 8) {
                powerupEndTimer = 0;
                powerupOn = false;
                player.setFireRate(0.5f);
            }
        }

        // checking the health
        if (health <= 0) {
            if (score > prefs.getInteger("highscore")) {
                prefs.putInteger("highscore", score);
            }
            game.setScreen(new GameOverScreen(game, score));
        }

        // more asteroids
        asteroidTimeElapsed += delta;
        if (asteroidTimeElapsed > 5) {
            asteroidTime = 1.5f;
        }
        if (asteroidTimeElapsed > 10) {
            asteroidTime = 1;
        }
        if (asteroidTimeElapsed > 15) {
            asteroidTime = 0.2f;
        }

        addAsteroid(delta);
        for (Asteroid asteroid : asteroids) {
            asteroid.update(delta);
        }

        player.update(delta);
    }

    private void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            this.player.moveLeft();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            this.player.moveRight();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            this.player.shoot();
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // time between the frames
        elapsed += delta;

        update(delta);

        batch.setProjectionMatrix(cam.combined);
        batch.begin();
        shapeRenderer.begin();
        //shapeRenderer.rect(100, 200, PlayScreen.POWERUP_SIZE / 2 + 20, PlayScreen.POWERUP_SIZE / 2 + 20);
        shapeRenderer.end();

        batch.draw(spaceTexture, 0, 0, MyGame.V_WIDTH, MyGame.V_HEIGHT);

        batch.draw(player.getTexture(), player.getPosition().x, player.getPosition().y, PLAYER_SIZE, PLAYER_SIZE);

        // draw asteroids
        for (int i = 0; i < asteroids.size(); i++) {
            Asteroid asteroid = asteroids.get(i);

            // draw the asteroid
            batch.draw(asteroid.getTexture(), asteroid.getPosition().x, asteroid.getPosition().y, ASTEROID_SIZE, ASTEROID_SIZE);

            // check the collision with the player
            if (player.collidesWith(asteroid.getRect())) {
                asteroid.setShouldRemove(true);

                this.health -= 20;
                this.painSound.play();
            }

            // increase the score if asteroid is not alive
            if (!asteroid.isAlive()) {
                score += asteroid.getAward();

                Explosion explosion = new Explosion(explosionAnim, asteroid.getPosition());
                explosions.add(explosion);

                this.destroySound.play();
            }

            // check the collision with bullets
            for (int j = 0; j < player.getBullets().size(); j++) {
                Bullet bullet = player.getBullets().get(j);
                if (bullet.collidesWith(asteroid.getRect())) {
                    player.getBullets().get(j).setShouldRemove(true);
                    asteroids.get(i).takeDamage(1);
                }
            }

            // check if the asteroid should be removed
            if (asteroid.shouldRemove()) {
                //world.destroyBody(asteroid.getBody());
                asteroids.remove(i);
                i--;
            }
        }

        // draw bullets
        for (Bullet bullet : player.getBullets()) {
            batch.draw(bullet.getTexture(), bullet.getPosition().x, bullet.getPosition().y, BULLET_SIZE, BULLET_SIZE);
        }

        // draw explosions
        for (int i = 0; i < explosions.size(); i++) {
            Explosion explosion = explosions.get(i);

            batch.draw(explosion.getAnimation().getKeyFrame(elapsed, true), explosion.getPosition().x, explosion.getPosition().y, EXPLOSION_SIZE, EXPLOSION_SIZE);
            if (explosion.getAnimation().isAnimationFinished(elapsed)) {
                explosions.remove(i);
                i--;
            }
        }

        if (powerup != null) {
            batch.draw(powerup.getTexture(), powerup.getPosition().x, powerup.getPosition().y, POWERUP_SIZE, POWERUP_SIZE);
        }

        font.draw(batch, "Score: " + score,  50, MyGame.V_HEIGHT - 50);
        font.draw(batch, "High Score: " + prefs.getInteger("highscore"),  50, MyGame.V_HEIGHT - 90);
        font.draw(batch, "HP: " + health,  50, MyGame.V_HEIGHT - 130);
        font.getData().setScale(2);

        batch.end();
    }

    private void addAsteroid(float delta) {
        asteroidTimer += Gdx.graphics.getRawDeltaTime();
        if (asteroidTimer > asteroidTime){
            asteroidTimer -= asteroidTime;
            Asteroid asteroid = new Asteroid(rnd.nextInt(MyGame.V_WIDTH), -rnd.nextInt(3) - 2);
            asteroids.add(asteroid);
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        for (Asteroid asteroid : asteroids) {
            asteroid.dispose();
        }
        player.dispose();
    }
}
