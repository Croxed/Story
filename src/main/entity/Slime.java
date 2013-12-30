package main.entity;

import main.BoundingBox;
import main.Game;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.state.StateBasedGame;

public class Slime extends Enemy {

	private float velocityY, GRAVITATIONAL_CONSTANT = 0.010f;

	public Slime(float x, float y, float Xpx, float Ypx, Animation leftAnimation, Animation rightAnimation ,Image hurtImage, Image deathImage) 
	{
		super(x, y, Xpx, Ypx, leftAnimation, rightAnimation, hurtImage, deathImage);
		setSpeedX(0.1f);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) 
	{
		sprite.update(delta);
		if(atTargetX())
		{
			generateRandomCoordX();
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

		if(!Game.isBlocked(getMinX(), getMaxY() + velocityY) // Checks lower left
				&& !Game.isBlocked(getMaxX(), getMaxY() + velocityY)) // Checks lower right
		{
			setCenterY(velocityY);
			velocityY += (delta * GRAVITATIONAL_CONSTANT);
		}else
		{
			velocityY = 0;
		}

		if(Game.isBlocked(getMaxX() + SPEED_X, getMinY()) // Top right
				&& Game.isBlocked(getMaxX() + SPEED_X, getMaxY())) // Bot right 
		{
			targetX = (int) getX() - 100;
			directionX = -1;
		}

		if(Game.isBlocked(getMinX() - SPEED_X, getMinY()) // Top right
				&& Game.isBlocked(getMinX() - SPEED_X, getMaxY())) // Bot right 
		{
			targetX = (int) getX() + 100;
			directionX = 1;
		}
	}

	@Override
	public void collision(BoundingBox boundingBox)
	{
		// TODO Auto-generated method stub
	}
}