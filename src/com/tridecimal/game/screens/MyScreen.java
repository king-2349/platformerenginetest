package com.tridecimal.game.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.tridecimal.game.GameMain;
import com.tridecimal.game.collision.Collider;
import com.tridecimal.game.tools.Constants;

public abstract class MyScreen implements Screen {

	private GameMain game;
	private Stage stage;

	protected boolean letUpdate = true;
	protected boolean letDraw = true;

	public MyScreen(GameMain game) {
		this.game = game;
		stage = new Stage(new ScalingViewport(Scaling.stretch, Constants.WIDTH, Constants.HEIGHT));
	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
		if (letUpdate) {
			stage.act(delta);
		}
		if (letDraw) {
			stage.draw();
		}
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

	@Override
	public void dispose() {

	}

	public Stage getStage() {
		return stage;
	}

	public GameMain getGame() {
		return game;
	}

}
