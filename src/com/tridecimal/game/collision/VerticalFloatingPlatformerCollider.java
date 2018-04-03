package com.tridecimal.game.collision;

import com.badlogic.gdx.math.Rectangle;
import com.tridecimal.game.actors.PlatformerPhysicsActor;

public class VerticalFloatingPlatformerCollider extends CollisionHandler {

	private float offset = .9f;

	public VerticalFloatingPlatformerCollider(PlatformerPhysicsActor owner, Rectangle bounds, int type) {
		super(owner, bounds, type);
		bounds.height += offset;
	}

	public void afterCollision(CollisionHandler other, int axis) {
		
	}

	public boolean fixCollision(CollisionHandler other, int axis) {
		Rectangle moveWorldBounds = other.getWorldBounds();
		Rectangle fromWorldBounds = this.getWorldBounds();

		if (axis == 0) {
			if (moveWorldBounds.y < fromWorldBounds.y + fromWorldBounds.height - offset) {
				if (moveWorldBounds.x + moveWorldBounds.width * .5f < fromWorldBounds.x + fromWorldBounds.width * .5f) {
					other.getOwner().setX(fromWorldBounds.x - moveWorldBounds.width - other.getBounds().x - .00001f);
				} else {
					other.getOwner().setX(fromWorldBounds.x + fromWorldBounds.width - other.getBounds().x);
				}
				other.getOwner().velocity.x = this.getOwner().velocity.x;
			}
		}
		if (axis == 1) {
			if (moveWorldBounds.y >= fromWorldBounds.y + fromWorldBounds.height - offset
					&& moveWorldBounds.y <= fromWorldBounds.y + fromWorldBounds.height) {
				if (other.getOwner().onGround) {
					other.getOwner().setY(fromWorldBounds.y + fromWorldBounds.height - other.getBounds().y - offset);
					other.getOwner().onGround = true;
				}
			} else {
				if (moveWorldBounds.y + moveWorldBounds.height * .5f < fromWorldBounds.y
						+ fromWorldBounds.height * .5f) {
					other.getOwner().setY(fromWorldBounds.y - moveWorldBounds.height - other.getBounds().y - .00001f);
				} else {
					other.getOwner().setY(fromWorldBounds.y + fromWorldBounds.height - other.getBounds().y - offset);
					other.getOwner().onGround = true;
				}
			}
			other.getOwner().velocity.y = this.getOwner().velocity.y;
		}

		return true;
	}

}
