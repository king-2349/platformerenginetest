package com.tridecimal.game.screens;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.tridecimal.game.GameMain;
import com.tridecimal.game.actors.PlatformerPhysicsActor;
import com.tridecimal.game.actors.Player;
import com.tridecimal.game.actors.VerticalMovingPlatform;
import com.tridecimal.game.actors.tiles.TileMap;
import com.tridecimal.game.collision.Collider;
import com.tridecimal.game.tools.Constants;

public class GameScreen extends MyScreen{

	private PlatformerPhysicsActor player,pl;
	private Image background;
	private TileMap map;
	
	public GameScreen(GameMain game) {
		super(game);
		
		background = new Image(new TextureRegion(Constants.sprites,128,0,128,128));
		
		player = new Player();
		pl = new VerticalMovingPlatform();
		
		
		super.getStage().addActor(background);
		background.setBounds(0, 0, 320, 240);
		
		super.getStage().addActor(player);
		player.setBounds(80, 96, 16, 32);
		super.getStage().addActor(pl);
		pl.setBounds(80, 64, 32, 5);
		
		map = new TileMap();
		map.createMap();
		super.getStage().addActor(map);
		Collider.setTileMap(map);
		
	}
	
	@Override
	public void render(float delta) {
		float originalDelta = delta;
		delta = Math.min(delta, 1/30f);
		
		if (letUpdate) {
			super.getStage().act(delta);
		}
		Collider.fixAll(delta);
		if (letDraw) {
			super.getStage().draw();
		}
	} 
}
