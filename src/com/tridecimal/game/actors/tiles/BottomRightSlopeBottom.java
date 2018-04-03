package com.tridecimal.game.actors.tiles;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.tridecimal.game.actors.PlatformerPhysicsActor;
import com.tridecimal.game.collision.Collider;
import com.tridecimal.game.collision.SlopeCollider;
import com.tridecimal.game.tools.Constants;

public class BottomRightSlopeBottom extends Tile{

	public BottomRightSlopeBottom(TileMap map) {
		super(map);
	}

	public void create() {
		super.create();
		
		TextureRegion[] idleTextures = {new TextureRegion(Constants.sprites,0,80,16,16)};
		super.addAnimation(new Animation<TextureRegion>(1000f,idleTextures));
		
		super.setCollisionHandler(new SlopeCollider(this, new Rectangle(0,0,16,16), Collider.STATIC,.5f,0f));
	}
	
}
