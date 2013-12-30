package main.item;

import main.BoundingBox;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

public abstract class Item implements BoundingBox {

	private Shape boundingBox;

	public Item(float x, float y, float Xpx, float Ypx)
	{
		boundingBox = new Rectangle(x, y, Xpx, Ypx);
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