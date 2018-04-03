package com.tridecimal.game.actors;

import com.badlogic.gdx.math.Vector2;
import com.tridecimal.game.collision.Collider;
import com.tridecimal.game.collision.CollisionHandler;

public abstract class PlatformerPhysicsActor extends GameObject {

	protected CollisionHandler collisionHandler; //Object used for collision between actors

	public Vector2 velocity, acceleration; // basics
	public Vector2 maxVelocity, friction; // constraints
	public Vector2 tempVelocity, tempAcceleration; // for collision response
	public Vector2 positionChange;
	public float weight;
	
	public Vector2 lastNonZeroVelocity, lastNonZeroAcceleration; // for animation
	public boolean skipX, skipY;

	public boolean onGround;

	public void create() {
		//Initialize all vectors to 0 by default
		velocity = new Vector2(0f, 0f);
		acceleration = new Vector2(0f, 0f);
		maxVelocity = new Vector2(0f, 0f);
		friction = new Vector2(0f, 0f);
		lastNonZeroVelocity = new Vector2(0f, 0f);
		lastNonZeroAcceleration = new Vector2(0f, 0f);
		tempVelocity = new Vector2(0f, 0f);
		tempAcceleration = new Vector2(0f, 0f);
		positionChange = new Vector2(0f,0f);
		weight = 0;
		onGround = false;
	}

	@Override
	public void update(float delta) {
		//Used for non physics updates
	}
	
	public void updateX(float delta) {
		//Update the acceleration and velocity of x based on constraints
		if (acceleration.x != 0) {
			lastNonZeroAcceleration.x = acceleration.x;
		}

		if (acceleration.x == 0 && velocity.x != 0) {
			float frictionX = friction.x;
			if(!onGround) {
				frictionX *= 1f;
			}
			if (Math.abs(velocity.x) <= frictionX) {
				lastNonZeroVelocity.x = frictionX;
				velocity.x = 0;
			} else {
				if (velocity.x < 0)
					velocity.x += frictionX;
				else
					velocity.x -= frictionX;
			}
		} else {
			if (Math.abs(velocity.x + acceleration.x) <= maxVelocity.x) {
				velocity.x += acceleration.x;
				lastNonZeroVelocity.x = velocity.x;
			} else {
				if (velocity.x < 0)
					velocity.x = maxVelocity.x * -1;
				else
					velocity.x = maxVelocity.x;
				lastNonZeroVelocity.x = velocity.x;
			}
		}
	}
	
	public void updateY(float delta) {
		//Update the acceleration and velocity of y based on constraints
		if (acceleration.y != 0) {
			lastNonZeroAcceleration.y = acceleration.y;
		}

		if (acceleration.y == 0 && velocity.y != 0) {
			if (Math.abs(velocity.y) <= friction.y) {
				lastNonZeroVelocity.y = velocity.y;
				velocity.y = 0;
			} else {
				if (velocity.y < 0)
					velocity.y += friction.y;
				else
					velocity.y -= friction.y;
			}
		} else {
			if (Math.abs(velocity.y + acceleration.y) <= maxVelocity.y) {
				velocity.y += acceleration.y;
				lastNonZeroVelocity.y = velocity.y;
			} else {
				if (velocity.y < 0)
					velocity.y = maxVelocity.y * -1;
				else
					velocity.y = maxVelocity.y;
				lastNonZeroVelocity.y = velocity.y;
			}
		}
	}

	public CollisionHandler getCollisionHandler() {
		return collisionHandler;
	}

	public void setCollisionHandler(CollisionHandler handler) {
		Collider.addHandler(handler);
		this.collisionHandler = handler;
	}
}
