package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.screens.GameOverScreen;
import com.mygdx.game.screens.MainMenuScreen;
import com.mygdx.game.screens.PlayScreen;

public class MyGame extends Game {
	public static final int V_WIDTH = 600;
	public static final int V_HEIGHT = 700;

	public SpriteBatch batch;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor( 1, 0, 0, 1 );
		Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
