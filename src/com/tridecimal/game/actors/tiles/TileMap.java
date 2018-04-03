package com.tridecimal.game.actors.tiles;

import java.util.LinkedList;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.tridecimal.game.actors.PlatformerPhysicsActor;
import com.tridecimal.game.collision.Collider;
import com.tridecimal.game.collision.CollisionHandler;
import com.tridecimal.game.tools.Constants;

public class TileMap extends PlatformerPhysicsActor {

	//Store an array of tiles for easy access
	private Tile[][] tiles;

	public void create() {
		super.create();
		
		this.weight = Float.MAX_VALUE;
		
		maxVelocity.set(0, 0);

		//Give collider but don't add it to the list of colliders
		//Collisions will be checked separetly 
		super.collisionHandler = new MapCollider(this);
	}

	public void createMap() {
		//Create a basic map for testing purposes
		//In an actual game this could be automated from tile map editor
		
		tiles = new Tile[15][11];
		super.setBounds(0, 0, 16 * tiles.length, 16 * tiles[0].length);

		for (int i = 0; i < 15; i++) {
			Wall wall = new Wall(this);
			wall.setBounds(i * 16, 0, 16, 16);
			this.addActor(wall);
			tiles[i][0] = wall;
		}

		LeftOpenWall saw = new LeftOpenWall(this);
		saw.setBounds(4 * 16, 16, 16, 16);
		this.addActor(saw);
		tiles[4][1] = saw;

		RightOpenWall saw2 = new RightOpenWall(this);
		saw2.setBounds(10 * 16, 16, 16, 16);
		this.addActor(saw2);
		tiles[10][1] = saw2;

		for (int i = 0; i < 11; i++) {
			Wall wall = new Wall(this);
			wall.setBounds(0, i * 16, 16, 16);
			this.addActor(wall);
			tiles[0][i] = wall;
		}
		for (int i = 0; i < 11; i++) {
			Wall wall = new Wall(this);
			wall.setBounds(Constants.WIDTH - 16, i * 16, 16, 16);
			this.addActor(wall);
			tiles[14][i] = wall;
		}
		
		Wall wall = new Wall(this);
		wall.setBounds(8*16, 64, 16, 16);
		this.addActor(wall);
		tiles[8][4] = wall;
		
		wall = new Wall(this);
		wall.setBounds(10*16, 32, 16, 16);
		this.addActor(wall);
		tiles[10][2] = wall;

		BottomRightSlopeBottom bottom = new BottomRightSlopeBottom(this);
		bottom.setBounds(2 * 16, 16, 16, 16);
		this.addActor(bottom);
		tiles[2][1] = bottom;
		//
		BottomRightSlopeBottom bottom4 = new BottomRightSlopeBottom(this);
		bottom4.setBounds(13 * 16, 16, 16, 16);
		this.addActor(bottom4);
		tiles[13][1] = bottom4;
		//
		BottomRightSlopeTop top = new BottomRightSlopeTop(this);
		top.setBounds(3 * 16, 16, 16, 16);
		this.addActor(top);
		tiles[3][1] = top;

		BottomLeftSlopeBottom bottom2 = new BottomLeftSlopeBottom(this);
		bottom2.setBounds(12 * 16, 16, 16, 16);
		this.addActor(bottom2);
		tiles[12][1] = bottom2;
		//
		BottomLeftSlopeBottom bottom3 = new BottomLeftSlopeBottom(this);
		bottom3.setBounds(1 * 16, 16, 16, 16);
		this.addActor(bottom3);
		tiles[1][1] = bottom3;
		//
		BottomLeftSlopeTop top2 = new BottomLeftSlopeTop(this);
		top2.setBounds(11 * 16, 16, 16, 16);
		this.addActor(top2);
		tiles[11][1] = top2;
	}

	public Tile getTile(int x, int y) {
		//Make sure tile requested isn't out of bounds
		if (x < 0 || x >= tiles.length || y < 0 || y >= tiles[0].length) {
			return null;
		}
		return tiles[x][y];
	}

	class MapCollider extends CollisionHandler {

		public MapCollider(PlatformerPhysicsActor owner) {
			super(owner, new Rectangle(), Collider.STATIC);
		}

		@Override
		public void afterCollision(CollisionHandler other, int axis) {

		}

		@Override
		public boolean fixCollision(CollisionHandler other, int axis) {		
			//Get list of all current tiles being collided with
			Rectangle moveWorldBounds = other.getWorldBounds();
			LinkedList<Tile> handlers = new LinkedList<Tile>();
			for (int y = (int) (moveWorldBounds.y / 16); y <= (int) ((moveWorldBounds.y + moveWorldBounds.height)
					/ 16); y++) {
				for (int x = (int) (moveWorldBounds.x / 16); x <= (int) ((moveWorldBounds.x + moveWorldBounds.width)
						/ 16); x++) {
					Tile t = getTile(x, y);
					if (t != null) {
						handlers.add(t);
					}
				}
			}

			//Essentially the rest of this code finds finds the tile that pushes the actor farthest back
			//This will make it so the actor is not left in a tile after the collision handling
			boolean anyMoved = false;
			Vector2 originalVelocity = other.getOwner().velocity.cpy();
			Vector2 bestPosition = other.getOwner().getWorldCoordinates().cpy(); 
			boolean firstCheck = true;
			
			for (Tile t : handlers) {
				Rectangle fromWorldBounds = t.getCollisionHandler().getWorldBounds();
				moveWorldBounds = other.getWorldBounds();
				if (fromWorldBounds.overlaps(moveWorldBounds)) {
					boolean thisMoved = false;
					thisMoved = t.getCollisionHandler().fixCollision(other, axis);
					anyMoved |= thisMoved;
					if (thisMoved) {
						if (firstCheck) {
							bestPosition.set(other.getOwner().getWorldCoordinates().cpy());
							firstCheck = false;
						} else if (axis == 0) {
							if (originalVelocity.x > 0 && bestPosition.x < other.getOwner().getWorldX()) {
								other.getOwner().setX(bestPosition.x);
							} else if (originalVelocity.x < 0 && bestPosition.x > other.getOwner().getWorldX()) {
								other.getOwner().setX(bestPosition.x);
							} else {
								bestPosition.set(other.getOwner().getWorldCoordinates().cpy());
							}
						} else {
							if (originalVelocity.y > 0 && bestPosition.y < other.getOwner().getWorldY()) {
								other.getOwner().setY(bestPosition.y);
							} else if (originalVelocity.y < 0 && bestPosition.y > other.getOwner().getWorldY()) {
								other.getOwner().setY(bestPosition.y);
							} else {
								bestPosition.set(other.getOwner().getWorldCoordinates().cpy());
							}
						}
					}
				}
			}

			return anyMoved;
		}

	}
}
