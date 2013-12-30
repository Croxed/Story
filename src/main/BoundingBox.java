package main;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.state.StateBasedGame;

public interface BoundingBox 
{
	/*
	 * This method is called if the object is in the renderList
	 * and should thus perform it's updating here.
	 */
	abstract void render(GameContainer container, StateBasedGame game, Graphics g);

	/*
	 * Called when Entity wants to recieve ticks - to update something like 
	 * movement
	 */
	abstract void update(GameContainer container, StateBasedGame game, int delta);

	/*
	 * Called when a collision happens
	 */
	abstract void collision(BoundingBox boundingBox);

	abstract float getMinX();

	abstract float getMinY();

	abstract float getMaxX();

	abstract float getMaxY();

	abstract float getY();

	abstract float getX();

	abstract void setX(float x);

	abstract void setY(float y);

	abstract float getWidth();

	abstract float getHeight();

	abstract float getCenterX();

	abstract float getCenterY();

	abstract void setCenterX(float x);

	abstract void setCenterY(float y);

	abstract boolean intersects(BoundingBox box);

	abstract Shape getBoundingBox();
}
