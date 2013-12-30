package main.entity;

import java.util.Random;

import main.BoundingBox;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.state.StateBasedGame;

public class Fly extends Enemy {

	protected int targetY;
	protected int directionY;

	public Fly(float x, float y, float Xpx, float Ypx, Animation leftAnimation, Animation rightAnimation, Image hurtImage, Image deathImage) 
	{
		super(x, y, Xpx, Ypx, leftAnimation, rightAnimation, hurtImage, deathImage);
		generateRandomCoordY();
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
	{		
		sprite.update(delta);
		if(atTargetX())
		{
			generateRandomCoordX();
		}

		if(atTargetY())
		{
			generateRandomCoordY();
		}


		if(directionX == 1)
		{
			sprite = rightAnimation;
		}else
		{
			sprite = leftAnimation;
		}

		this.setCenterY(SPEED_Y * directionY);
		this.setCenterX(SPEED_X * directionX);

		if(hurt)
		{
			hurtCD += delta;
		}

	}

	@Override
	public void collision(BoundingBox box)
	{

	}

	public boolean atTargetY()
	{
		if(this.getY() > targetY - 20 && this.getY() < targetY + 20 )
		{
			return true;
		}
		return false;
	}

	public void generateRandomCoordY()
	{
		this.targetY = new Random().nextInt(70) + 10;
		if(this.targetY < this.getCenterY())
		{
			this.directionY = -1;
		}else
		{
			this.directionY = 1;
		}
	}
}