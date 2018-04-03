package com.tridecimal.game.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.tridecimal.game.collision.BoxTileCollider;
import com.tridecimal.game.collision.Collider;
import com.tridecimal.game.tools.Constants;

public class Player extends PlatformerPhysicsActor{
	
	//Variables to make variable jump height
	boolean jumping = false, canJump = false;
	float ogJump = 45f;
	float jumpForce = ogJump;
	
	public void create() {	
		super.create();
		
		maxVelocity.set(64f,180f);
		friction.set(10f,0f);
		
		super.setCollisionHandler(new BoxTileCollider(this,new Rectangle(5,0,7,30),Collider.KINEMATIC));
		
		//Add all of the players animations
		TextureRegion[] idleRightTextures = {new TextureRegion(Constants.sprites,0,0,16,32),new TextureRegion(Constants.sprites,16,0,16,32),
				new TextureRegion(Constants.sprites,32,0,16,32),new TextureRegion(Constants.sprites,48,0,16,32)};
		addAnimation(new Animation<TextureRegion>(.2f, idleRightTextures));
		TextureRegion[] idleLeftTextures = {new TextureRegion(Constants.sprites,64,0,16,32),new TextureRegion(Constants.sprites,80,0,16,32),
				new TextureRegion(Constants.sprites,96,0,16,32),new TextureRegion(Constants.sprites,112,0,16,32)};
		addAnimation(new Animation<TextureRegion>(.2f, idleLeftTextures));
		TextureRegion[] walkRightTextures = {new TextureRegion(Constants.sprites,0,32,16,32),new TextureRegion(Constants.sprites,16,32,16,32),
				new TextureRegion(Constants.sprites,32,32,16,32),new TextureRegion(Constants.sprites,48,32,16,32)};
		addAnimation(new Animation<TextureRegion>(.2f, walkRightTextures));
		TextureRegion[] walkLeftTextures = {new TextureRegion(Constants.sprites,64,32,16,32),new TextureRegion(Constants.sprites,80,32,16,32),
				new TextureRegion(Constants.sprites,96,32,16,32),new TextureRegion(Constants.sprites,112,32,16,32)};
		addAnimation(new Animation<TextureRegion>(.2f, walkLeftTextures));
	}
	
	@Override
	public void update(float delta) {
		acceleration.set(0,-15f);
		
		if(Gdx.input.isKeyPressed(Keys.RIGHT)) {			
			acceleration.x += 4f;
		}
		if(Gdx.input.isKeyPressed(Keys.LEFT)) {			
			acceleration.x -= 4f;
		}
		
		//The amount of time the user presses space determines the height of the jump
		if(canJump && (onGround || jumping) && Gdx.input.isKeyPressed(Keys.SPACE)) {
			if(jumpForce <= 0f) {
				canJump = false;
				jumping = false;
			} else {
				onGround = false;
				jumping = true;
				velocity.y += jumpForce;
				jumpForce -= 5f;
			}
		} else {
			jumping = false;
			jumpForce = ogJump;
		}
		
		if(!Gdx.input.isKeyPressed(Keys.SPACE)) {
			canJump = true;
		}
		
		
		super.update(delta);
	}
	
	public void pickAnimation() {
		//Pick animation based on acceleration and velocity of player
		if(acceleration.x == 0) {
			if(lastNonZeroAcceleration.x > 0)
				super.setAnimation(0);
			else
				super.setAnimation(1);
		} else {
			if(acceleration.x > 0)
				super.setAnimation(2);
			else
				super.setAnimation(3);
		}
	}

}
