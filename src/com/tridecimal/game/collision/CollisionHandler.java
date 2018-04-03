package com.tridecimal.game.collision;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.tridecimal.game.actors.PlatformerPhysicsActor;

public abstract class CollisionHandler {

	//list of current collisions
	private PlatformerPhysicsActor owner;
	/*Rectangle for AABB collision*/
	private Rectangle bounds;
	private int type;
	
	public Array<CollisionHandler> currentCollisionsX;
	public Array<CollisionHandler> currentCollisionsY;
		
	public CollisionHandler(PlatformerPhysicsActor owner, Rectangle bounds, int type) {
		this.owner = owner;
		this.bounds = bounds;
		this.type = type;
		this.currentCollisionsX = new Array<CollisionHandler>();
		this.currentCollisionsY = new Array<CollisionHandler>();
	}
	
	public boolean colliding(CollisionHandler other) {
		return this.getWorldBounds().overlaps(other.getWorldBounds());
	}
	public abstract void afterCollision(CollisionHandler other, int axis);
	public abstract boolean fixCollision(CollisionHandler other, int axis);
	
	public Rectangle getWorldBounds() {
		Vector2 stageCoordinates = owner.getWorldCoordinates();
		return new Rectangle(bounds.x+stageCoordinates.x,bounds.y+stageCoordinates.y,bounds.width,bounds.height);
	}
	
	public PlatformerPhysicsActor getOwner() {
		return owner;
	}
	
	public Rectangle getBounds() {
		return bounds;
	}
	
	public int getType() {
		return type;
	}
}
