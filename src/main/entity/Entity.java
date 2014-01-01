package main.entity;

import main.BoundingBox;
import main.Game;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.util.Log;

public abstract class Entity implements BoundingBox {

	private Shape boundingBox;

	public Entity(float x, float y, float Xpx, float Ypx)
	{
		boundingBox = new Rectangle(x, y, Xpx, Ypx);
		// TODO: Remove all the manual adding to the update & render list from the code. Goal is to rely on these two lines alone.
		// Game.renderList.add(this);
		// Game.updateList.add(this);
	}

	public abstract int getHealth();

	public abstract void takeDamage(int damage);

	public abstract void death();

	public abstract void cooldownFinished(String cooldownName);

	void startCooldown(String cooldownName, int time) 
	{
		Game.cooldownManager.activateCooldown(this, cooldownName, time);
	}

	public void startCooldown(String cooldownName, int time, int repeat) 
	{
		Game.cooldownManager.activateCooldown(this, cooldownName, time, repeat);
	}

	void registerNewEntity(Entity ownerEntity)
	{
		Game.cooldownManager.registerNewEntity(ownerEntity);
	}

	void registerNewCooldown(String cooldownName, int time, Entity ownerEntity)
	{
		Game.cooldownManager.registerNewCooldown(cooldownName, time, ownerEntity);
	}

	void registerNewCooldown(String cooldownName, int time, int repeat, Entity ownerEntity)
	{
		Game.cooldownManager.registerNewRepeatingCooldown(cooldownName, time, ownerEntity, repeat);
	}

	void destroyEntity()
	{
		try
		{
			Game.renderList.remove(this);
			Game.updateList.remove(this);
			Game.lightSourceList.remove(this);
		}catch(NullPointerException e)
		{
			Log.error("Tried to remove " + this.toString() + " from a list which there was none. No harm done.");
			e.printStackTrace();
		}
	}

	@Override
	public float getMinX()
	{
		return boundingBox.getMinX();
	}

	@Override
	public float getMinY()
	{
		return boundingBox.getMinY();
	}

	@Override
	public float getMaxX()
	{
		return boundingBox.getMaxX();
	}

	@Override
	public float getMaxY()
	{
		return boundingBox.getMaxY();
	}

	@Override
	public float getY()
	{
		return this.boundingBox.getY();
	}

	@Override
	public float getX()
	{
		return this.boundingBox.getX();
	}

	@Override
	public void setX(float x)
	{
		this.boundingBox.setX(x);
	}

	@Override
	public void setY(float y)
	{
		this.boundingBox.setY(y);
	}

	@Override
	public float getWidth()
	{
		return this.boundingBox.getWidth();
	}

	@Override
	public float getHeight()
	{
		return this.boundingBox.getHeight();
	}

	@Override
	public float getCenterX()
	{
		return this.boundingBox.getCenterX();
	}

	@Override
	public float getCenterY()
	{
		return this.boundingBox.getCenterY();
	}

	@Override
	public void setCenterY(float change)
	{
		float y = this.boundingBox.getCenterY() + change;
		this.boundingBox.setCenterY(y);
	}

	@Override
	public void setCenterX(float change)
	{
		float x = this.boundingBox.getCenterX() + change;
		this.boundingBox.setCenterX(x);	
	}

	@Override
	public Shape getBoundingBox()
	{
		return this.boundingBox;
	}

	@Override
	public boolean intersects(BoundingBox object)
	{
		if(this.getBoundingBox() == null)
		{
			return false;
		}
		return this.boundingBox.intersects(object.getBoundingBox());
	}
}