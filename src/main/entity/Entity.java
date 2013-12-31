package main.entity;

import main.BoundingBox;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

public abstract class Entity implements BoundingBox {

	private Shape boundingBox;

	public Entity(float x, float y, float Xpx, float Ypx)
	{
		boundingBox = new Rectangle(x, y, Xpx, Ypx);
	}

	public abstract int getHealth();

	public abstract void takeDamage(int damage);

	public abstract void death();

	public abstract void cooldownFinished(String cooldownName);

	public abstract void startCooldown(String cooldownName, int time);

	public abstract void registerNewEntity();

	public abstract void registerNewCooldown(String cooldownName, int time);

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