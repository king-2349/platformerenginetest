package com.tridecimal.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.tridecimal.game.screens.GameScreen;

public class GameMain extends Game {
	
	@Override
	public void create () {
		super.setScreen(new GameScreen(this));
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 1, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.render();
	}
	
	@Override
	public void dispose () {
		
	}
	
	public void resize (int width, int height) {
		
	}
}
