package main.item;

import main.BoundingBox;
import main.entity.Player;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.state.StateBasedGame;

public class Spike extends Item {

	private int damage = 1;
	private Image spikeImage;

	public Spike(float x, float y, float Xpx, float Ypx, Image spikeImage) 
	{
		super(x, y, Xpx, Ypx);
		this.spikeImage = spikeImage;
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) 
	{
		spikeImage.draw(getX(), getY());
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void collision(BoundingBox boundingBox) 
	{
		if(boundingBox instanceof Player)
		{
			((Player) boundingBox).takeDamage(damage);
		}
	}
}
