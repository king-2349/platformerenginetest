package com.tridecimal.game.collision;

import com.badlogic.gdx.math.Rectangle;
import com.tridecimal.game.actors.PlatformerPhysicsActor;

public class SlopeCollider extends CollisionHandler {

	private float m = 0, b = 0, max = 0; //y = mx + b

	public SlopeCollider(PlatformerPhysicsActor owner, Rectangle bounds, int type, float m, float b) {
		super(owner, bounds, type);
		this.m = m;
		this.b = b;
		this.max = Math.max(b, m * 16 + b);
	}

	@Override
	public void afterCollision(CollisionHandler other, int axis) {

	}

	@Override
	public boolean fixCollision(CollisionHandler other, int axis) {
		Rectangle moveWorldBounds = other.getWorldBounds();
		Rectangle fromWorldBounds = this.getWorldBounds();

		//Only need a collision in the y axis
		if (axis == 1) {
			float side = moveWorldBounds.x;
			side += moveWorldBounds.width*.5f;
			if (side >= fromWorldBounds.x && side < fromWorldBounds.x + fromWorldBounds.width) {
				//Calculate the new y using y = mx+b
				float newY = m * (side - fromWorldBounds.x) + fromWorldBounds.y + b;

				if (moveWorldBounds.y < newY || other.getOwner().onGround) {
					other.getOwner().setY(newY);
					other.getOwner().velocity.y = 0;
					other.getOwner().acceleration.y = 0;
					other.getOwner().onGround = true;
					return true;
				}
			}
		}
		return false;
	}

}
