package com.tridecimal.game.actors;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;

public abstract class GameObject extends Group {

	private Array<Animation<TextureRegion>> animations;
	private int currentAnimation;
	private float animationTime;

	public GameObject() {
		animations = new Array<Animation<TextureRegion>>();
		currentAnimation = 0;
		animationTime = 0;

		create();
	}

	public abstract void create();

	public abstract void update(float delta);

	@Override
	public void act(float delta) {
		this.animationTime += delta;

		update(delta);
		
		super.act(delta);
	}

	public void pickAnimation() {

	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		pickAnimation();
		batch.setColor(getColor().r, getColor().g, getColor().b, parentAlpha * getColor().a);
		if (animations.size > 0) {
			batch.draw(animations.get(currentAnimation).getKeyFrame(animationTime, true), getX(), getY(),
					super.getOriginX(), super.getOriginY(), super.getWidth(), super.getHeight(), super.getScaleX(),
					super.getScaleY(), super.getRotation());
		}
		super.drawChildren(batch, parentAlpha);
		
		postFrame();
	}
	
	public void postFrame() {
		
	}

	public Vector2 getWorldCoordinates() {
		Vector2 world = new Vector2(this.getX(), this.getY());
		Actor parent = this.getParent();
		while (parent != null) {
			world.add(parent.getX(), parent.getY());
			parent = parent.getParent();
		}
		return world;
	}
	
	public float getWorldX() {
		return getWorldCoordinates().x;
	}
	
	public float getWorldY() {
		return getWorldCoordinates().y;
	}

	public Vector2 getWorldOrigin() {
		return new Vector2(getWorldCoordinates().add(getOriginX(), getOriginY()));
	}

	public void setBounds(float x, float y, float width, float height) {
		super.setBounds(x, y, width, height);
		super.setOrigin(width * .5f, height * .5f);
	}

	public final int addAnimation(Animation<TextureRegion> animation) {
		this.animations.add(animation);
		return animations.size;
	}

	public final void setAnimation(int animationIndex) {
		if (animationIndex != this.currentAnimation)
			this.animationTime = 0;
		this.currentAnimation = animationIndex;
	}
}
