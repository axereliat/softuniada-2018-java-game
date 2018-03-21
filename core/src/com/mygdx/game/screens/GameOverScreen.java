package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.game.MyGame;

/**
 * Created by mario on 8.03.18.
 */

public class GameOverScreen implements Screen {

    private Game game;

    private Preferences prefs;

    private BitmapFont font;

    private Skin skin;

    private Stage stage;

    private int score;

    private Texture gameOverTexture;

    private SpriteBatch sb;

    public GameOverScreen(final Game game, int score) {
        this.prefs = Gdx.app.getPreferences("My Preferences");
        this.game = game;
        this.score = score;
        this.font = new BitmapFont();
        this.gameOverTexture = new Texture("gameOver.png");
        this.sb = new SpriteBatch();

        stage = new Stage();
        Gdx.input.setInputProcessor(stage); // Make the stage consume events

        createBasicSkin();
        TextButton newGameButton = new TextButton("Try Again", skin); // Use the initialized skin
        TextButton mainMenuButton = new TextButton("Main Menu", skin); // Use the initialized skin
        newGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new PlayScreen(game));
            }
        });
        mainMenuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenuScreen(game));
            }
        });
        newGameButton.setPosition(Gdx.graphics.getWidth()/2 - Gdx.graphics.getWidth()/8 , Gdx.graphics.getHeight()/2 - 100);
        mainMenuButton.setPosition(Gdx.graphics.getWidth()/2 - Gdx.graphics.getWidth()/8 , Gdx.graphics.getHeight()/2 - 180);
        stage.addActor(newGameButton);
        stage.addActor(mainMenuButton);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        sb.begin();
        sb.draw(gameOverTexture, MyGame.V_WIDTH / 2 - 120, MyGame.V_HEIGHT - 300, 250, 250);
        font.draw(sb, "Score: " + score,  Gdx.graphics.getWidth() / 2 - 80, Gdx.graphics.getHeight() / 2 + 80);
        font.draw(sb, "High Score: " + prefs.getInteger("highscore"),  Gdx.graphics.getWidth() / 2 - 80, Gdx.graphics.getHeight() / 2 + 30);
        font.setColor(Color.BLACK);
        font.getData().setScale(3);
        sb.end();

        stage.act();
        stage.draw();
    }

    @Override
    public void dispose() {
        this.gameOverTexture.dispose();
    }

    private void createBasicSkin(){
        BitmapFont font = new BitmapFont();
        skin = new Skin();
        skin.add("default", font);

        //Create a texture
        Pixmap pixmap = new Pixmap((int) Gdx.graphics.getWidth()/4,(int)Gdx.graphics.getHeight()/10, Pixmap.Format.RGB888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("background", new Texture(pixmap));

        //Create a button style
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("background", Color.ORANGE);
        textButtonStyle.down = skin.newDrawable("background", Color.DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable("background", Color.BROWN);
        textButtonStyle.over = skin.newDrawable("background", Color.BROWN);
        textButtonStyle.font = skin.getFont("default");
        textButtonStyle.fontColor = Color.BLACK;

        skin.add("default", textButtonStyle);

    }



    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {

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
}
