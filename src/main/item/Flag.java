package main.item;

import main.BoundingBox;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

public class Flag extends Item {

	private Animation animation;

	public Flag(float x, float y, float Xpx, float Ypx, Animation blueFlagAnimation) 
	{
		super(x, y, Xpx, Ypx);
		this.animation = blueFlagAnimation;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) 
	{
		// TODO Auto-generated method stub
		animation.draw(getX() + 8, getY());
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) 
	{
		// TODO Auto-generated method stub
		animation.update(delta);
	}

	@Override
	public void collision(BoundingBox boundingBox)
	{
		// TODO Auto-generated method stub

	}
}
