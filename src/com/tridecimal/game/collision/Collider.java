package com.tridecimal.game.collision;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.tridecimal.game.actors.tiles.TileMap;

public class Collider {

	/*
	 * Static actors can not move and do not react to forces. Static actors never
	 * move in collisions. Check against dynamics and Kinematics.
	 */
	public final static int STATIC = 1;
	/*
	 * Dynamic actors move and will react to forces. Dynamic actors always move in
	 * collisions. Check against kinematics and themselves.
	 */
	public final static int DYNAMIC = 2;
	/*
	 * Kinematic actors move and will react to forces. Generally for actors that are
	 * controlled. Will only move for static in collisions. Check against dynamics
	 * and themselves.
	 */
	public final static int KINEMATIC = 4;
	/*
	 * Trigger actors cannot be moved and do not move other actors. Only there to
	 * trigger events such as cutscenes.
	 */
	public final static int TRIGGER = 8;

	private static Array<CollisionHandler> handlers = new Array<CollisionHandler>();
	public static TileMap map;

	private static int numOfStatics = 0, numOfDynamics = 0, numOfKinematics = 0, numOfTriggers = 0;
	
	public static void setTileMap(TileMap map) {
		Collider.map = map;
	}

	public static void addHandler(CollisionHandler handler) {
		if (handler.getType() == STATIC)
			handlers.insert(numOfDynamics + numOfKinematics + (numOfStatics++), handler);
		else if (handler.getType() == KINEMATIC)
			handlers.insert(numOfDynamics + (numOfKinematics++), handler);
		else if (handler.getType() == DYNAMIC)
			handlers.insert((numOfDynamics++), handler);
		else
			handlers.insert(numOfDynamics + numOfKinematics + numOfStatics + (numOfTriggers++), handler);
	}

	public static void fixAll(float delta) {
		// Update x axis velocities for all actors
		for (CollisionHandler toUpdateX : handlers) {
			toUpdateX.getOwner().updateX(delta);
			toUpdateX.getOwner().positionChange.x = (toUpdateX.getOwner().velocity.x
					+ toUpdateX.getOwner().tempVelocity.x) * delta;
			toUpdateX.getOwner().setX(toUpdateX.getOwner().getX() + toUpdateX.getOwner().positionChange.x);
		}

		for (CollisionHandler toFix : handlers) {
			//Static actors dont need to check for collisions because they can't move
			if (toFix.getType() == STATIC)
				continue;
			//Get list of all collisions
			Array<CollisionHandler> collisions = getCollisionList(toFix, 0);
			//Fix every collision and determine which actor will do the work by weight
			for (CollisionHandler toCheck : collisions) {
				if (!toFix.currentCollisionsX.contains(toCheck, true)) {
					toFix.currentCollisionsX.add(toCheck);
				}
				if (!toCheck.currentCollisionsX.contains(toFix, true)) {
					toCheck.currentCollisionsX.add(toFix);
				}
				if (toFix.getOwner().weight < toCheck.getOwner().weight) {
					toCheck.fixCollision(toFix, 0);
				} else if (toFix.getOwner().weight > toCheck.getOwner().weight) {
					toFix.fixCollision(toCheck, 0);
				}
			}
		}
		
		//Do collisions for each actor with the map
		for (CollisionHandler toFix : handlers) {
			if (toFix.getType() == STATIC)
				continue;
			map.getCollisionHandler().fixCollision(toFix, 0);
		}

		//Update y velocities for all actors
		for (CollisionHandler toUpdateY : handlers) {
			toUpdateY.getOwner().updateY(delta);
			toUpdateY.getOwner().positionChange.y = (toUpdateY.getOwner().velocity.y
					+ toUpdateY.getOwner().tempVelocity.y) * delta;
			toUpdateY.getOwner().setY(toUpdateY.getOwner().getY() + toUpdateY.getOwner().positionChange.y);
		}

		for (CollisionHandler toFix : handlers) {
			if (toFix.getType() == STATIC)
				continue;
			//Get list of all collisions
			Array<CollisionHandler> collisions = getCollisionList(toFix, 1);
			//Fix every collision and determine which actor will do the work by weight
			for (CollisionHandler toCheck : collisions) {
				if (!toFix.currentCollisionsY.contains(toCheck, true)) {
					toFix.currentCollisionsY.add(toCheck);
				}
				if (!toCheck.currentCollisionsY.contains(toFix, true)) {
					toCheck.currentCollisionsY.add(toFix);
				}
				if (toFix.getOwner().weight < toCheck.getOwner().weight) {
					toCheck.fixCollision(toFix, 1);
				} else if (toFix.getOwner().weight > toCheck.getOwner().weight) {
					toFix.fixCollision(toCheck, 1);
				}
			}
		}

		//Do collisions for each actor with the map
		for (CollisionHandler toFix : handlers) {
			if (toFix.getType() == STATIC)
				continue;
			boolean moved = map.getCollisionHandler().fixCollision(toFix, 1);
			if(!moved && toFix.currentCollisionsY.size == 0) {
				toFix.getOwner().onGround = false;
			}
		}
	}

	/**Get's the list of all colliders that the actor is currently touching. If the actor stops touching
	 * an actor from last frame then alert the actor there is no longer a collision*/
	public static Array<CollisionHandler> getCollisionList(CollisionHandler toFix, int axis) {
		Array<CollisionHandler> collisions = new Array<CollisionHandler>();

		for (int i = 0; i < handlers.size; i++) {
			CollisionHandler toCheck = handlers.get(i);
			if (toFix != toCheck) {
				if (toFix.colliding(toCheck)) {
					if (collisions.size != 0) {
						int j;
						for (j = 0; j < collisions.size; j++) {
							CollisionHandler el = collisions.get(j);
							if (el.getOwner().weight > toCheck.getOwner().weight) {
								break;
							}
						}
						collisions.insert(j, toCheck);
					} else {
						collisions.add(toCheck);
					}
				} else {
					// Not colliding
					if (axis == 0) {
						if (toFix.currentCollisionsX.contains(toCheck, true)) {
							toFix.currentCollisionsX.removeValue(toCheck, true);
							toFix.afterCollision(toCheck, axis);
						}
						if (toCheck.currentCollisionsX.contains(toFix, true)) {
							toCheck.currentCollisionsX.removeValue(toFix, true);
							toCheck.afterCollision(toFix, axis);
						}
					} else {
						if (toFix.currentCollisionsY.contains(toCheck, true)) {
							toFix.currentCollisionsY.removeValue(toCheck, true);
							toFix.afterCollision(toCheck, axis);
						}
						if (toCheck.currentCollisionsY.contains(toFix, true)) {
							toCheck.currentCollisionsY.removeValue(toFix, true);
							toCheck.afterCollision(toFix, axis);
						}
					}
				}
			}
		}

		return collisions;
	}
}
