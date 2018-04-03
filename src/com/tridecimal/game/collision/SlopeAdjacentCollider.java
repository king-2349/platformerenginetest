package com.tridecimal.game.collision;

import com.badlogic.gdx.math.Rectangle;
import com.tridecimal.game.actors.PlatformerPhysicsActor;
import com.tridecimal.game.actors.tiles.Tile;
import com.tridecimal.game.actors.tiles.TileMap;

public class SlopeAdjacentCollider extends CollisionHandler {

	private int shift = 0;

	public SlopeAdjacentCollider(PlatformerPhysicsActor owner, Rectangle bounds, int type, int shift) {
		super(owner, bounds, type);
		this.shift = shift;
	}

	public void afterCollision(CollisionHandler other, int axis) {

	}

	public boolean fixCollision(CollisionHandler other, int axis) {
		Rectangle moveWorldBounds = other.getWorldBounds();
		Rectangle fromWorldBounds = this.getWorldBounds();

		if (axis == 0) {
			if (shift == -1) {
				if (moveWorldBounds.x + moveWorldBounds.width * .5f > fromWorldBounds.x + fromWorldBounds.width * .5f) {
					other.getOwner().setX(fromWorldBounds.x + fromWorldBounds.width - other.getBounds().x);
					other.getOwner().velocity.x = this.getOwner().velocity.x * .25f;
					other.getOwner().acceleration.x = 0;
					other.getOwner().skipX = true;
					return true;
				}
			} else if (shift == 1) {
				if (moveWorldBounds.x + moveWorldBounds.width * .5f < fromWorldBounds.x + fromWorldBounds.width * .5f) {
					other.getOwner().setX(fromWorldBounds.x - moveWorldBounds.width - other.getBounds().x - .00001f);
					other.getOwner().velocity.x = this.getOwner().velocity.x * .25f;
					other.getOwner().acceleration.x = 0;
					other.getOwner().skipX = true;
					return true;
				}
			}
		} else {
			boolean inBlockX = true;

			if ((shift == -1) && (moveWorldBounds.x + moveWorldBounds.width * .5f > fromWorldBounds.x
					+ fromWorldBounds.width * .5f)) {
				inBlockX = moveWorldBounds.x < fromWorldBounds.x + fromWorldBounds.width
						&& moveWorldBounds.x > fromWorldBounds.x;
			} else if ((shift == 1) && (moveWorldBounds.x + moveWorldBounds.width * .5f < fromWorldBounds.x
					+ fromWorldBounds.width * .5f)) {
				inBlockX = moveWorldBounds.x + moveWorldBounds.width < fromWorldBounds.x + fromWorldBounds.width
						&& moveWorldBounds.x + moveWorldBounds.width > fromWorldBounds.x;
			} else if (shift != 0){
				inBlockX = moveWorldBounds.x + moveWorldBounds.width * .5f < fromWorldBounds.x + fromWorldBounds.width
						&& moveWorldBounds.x + moveWorldBounds.width * .5f > fromWorldBounds.x;
			}

			if (inBlockX) {
				if (moveWorldBounds.y + moveWorldBounds.height * .5f < fromWorldBounds.y
						+ fromWorldBounds.height * .5f) {
					other.getOwner().setY(fromWorldBounds.y - moveWorldBounds.height - other.getBounds().y);
				} else {
					other.getOwner().setY(fromWorldBounds.y + fromWorldBounds.height - other.getBounds().y);
					other.getOwner().onGround = true;
				}

				other.getOwner().velocity.y = 0;
				other.getOwner().acceleration.y = 0;
				return true;
			}
		}
		return false;
	}

}
