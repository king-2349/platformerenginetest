package com.tridecimal.game.actors;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.tridecimal.game.collision.Collider;
import com.tridecimal.game.collision.HorizontalFloatingPlatformerCollider;
import com.tridecimal.game.tools.Constants;

public class HorizontalMovingPlatform extends PlatformerPhysicsActor{

	float totalTime = 0;
	
	public void create() {
		super.create();
		
		weight = Float.MAX_VALUE/2f;
		maxVelocity.set(128, 0);
		velocity.x = 16;
		
		TextureRegion[] idleTextures = {new TextureRegion(Constants.sprites,0,112,32,5)};
		super.addAnimation(new Animation<TextureRegion>(1000f,idleTextures));
		
		super.setCollisionHandler(new HorizontalFloatingPlatformerCollider(this, new Rectangle(0,0,32,5.1f), Collider.STATIC));
	}
	
	public void update(float delta) {
		totalTime+= delta;
		
		if(totalTime > 3) {
			velocity.x *=-1;
			totalTime = 0;
		}
		
		super.update(delta);
	}
	
	public void draw(Batch batch, float alpha) {
		super.draw(batch, alpha);
	}

}
