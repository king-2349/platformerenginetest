package com.tridecimal.game.actors;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.tridecimal.game.collision.BoxTileCollider;
import com.tridecimal.game.collision.Collider;
import com.tridecimal.game.tools.Constants;

public class MoveBox extends PlatformerPhysicsActor{

	public void create() {
		super.create();
		
		acceleration.set(0, -10f);
		maxVelocity.set(64, 128f);
		friction.set(10f,0f);
		
		TextureRegion[] idleTextures = {new TextureRegion(Constants.sprites,16,64,16,16)};
		super.addAnimation(new Animation<TextureRegion>(1000f,idleTextures));
		
		super.setCollisionHandler(new BoxTileCollider(this, new Rectangle(.15f,0,15.7f,16), Collider.DYNAMIC));
	}
	
	public void update(float delta) {
		acceleration.set(0,-10f);

		super.update(delta);
	}
	
}
