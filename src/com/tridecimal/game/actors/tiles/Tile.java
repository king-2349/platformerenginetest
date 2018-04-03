package com.tridecimal.game.actors.tiles;

import com.tridecimal.game.actors.PlatformerPhysicsActor;
import com.tridecimal.game.collision.CollisionHandler;

public class Tile extends PlatformerPhysicsActor{

	private TileMap map;
	
	public Tile(TileMap map) {
		this.map = map;
		this.weight = Float.MAX_VALUE;
	}
	
	public void setCollisionHandler(CollisionHandler collisionHandler) {
//		super.setCollisionHandler(collisionHandler);
		this.collisionHandler = collisionHandler;
	}

	public TileMap getMap() {
		return map;
	}
}
