package main.entity;

import java.util.Random;

import main.BoundingBox;
import main.Game;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;


public class Enemy extends Entity {

	protected Animation sprite, leftAnimation, rightAnimation;
	protected Image hurtImage, deathImage;
	protected int health = 5;
	protected int directionX = 0;
	protected int directionY = 0;
	protected int targetX;
	protected int targetY;
	protected boolean hurt = false;
	protected boolean dead = false;
	protected float hurtCD = 0, SPEED_Y = 1.0f, SPEED_X = 1.5f;

	public Enemy(float x, float y, float Xpx, float Ypx, Animation leftAnimation, Animation rightAnimation, Image hurtImage, Image deathImage) 
	{
		super(x, y, Xpx, Ypx);
		this.leftAnimation = leftAnimation;
		this.rightAnimation = rightAnimation;
		this.hurtImage = hurtImage;
		this.deathImage = deathImage;
		generateRandomCoordX();
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
	{
		if(hurt)
		{
			g.drawImage(hurtImage, getX(), getY()); // Fixa reverse hurt image också
			if(hurtCD >= 100)
			{
				hurt = false;
				hurtCD = 0;
			}
		}else
		{
			sprite.draw(getX(), getY());
			g.drawLine(targetX, 0, targetX, 100);
		}

		if(dead)
		{
			deathImage.draw(getX(), getY());
			directionY = 1;
			SPEED_Y = 3.0f;
			SPEED_X = 0.5f;
			targetY = 800;
			if(atTargetY())
			{
				Game.removeBoundingBoxRenderList(this);
			}
		}
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
	}

	@Override
	public void collision(BoundingBox boundingBox) 
	{
		// TODO Auto-generated method stub

	}

	@Override
	public int getHealth() 
	{
		return health;
	}

	@Override
	public void takeDamage(int damage)
	{
		if(damage >= health)
		{
			Game.player.addScore(health);
			death();
		}else
		{
			Game.player.addScore(damage);
		}

		this.health -= damage;
		hurt = true;

		Log.info(this.toString() + " has taken a hit !!");
	}

	/*
	 * Init the death animation
	 */
	@Override
	public void death()
	{
		dead = true;
		Game.player.addScore(5);
	}


	public boolean atTargetX()
	{
		if(this.getX() > targetX - 20 && this.getX() < targetX + 20 )
		{
			return true;
		}
		return false;
	}

	public void generateRandomCoordX()
	{
		this.targetX = new Random().nextInt(700) + 50;
		if(this.targetX < this.getCenterX())
		{
			this.directionX = -1;
			sprite = leftAnimation;
		}else
		{
			this.directionX = 1;
			sprite = rightAnimation;
		}
	}

	public boolean atTargetY()
	{
		if(this.getY() > targetY - 20 && this.getY() < targetY + 20 )
		{
			return true;
		}
		return false;
	}

	public void setSpeedX(float speed)
	{
		SPEED_X = speed;
	}

	public void setSpeedY(float speed)
	{
		SPEED_Y = speed;
	}

	public void setHealth(int health)
	{
		this.health = health;
	}

	@Override
	public void cooldownFinished(String cooldownName) {}

	@Override
	public void registerNewEntity() {
		// TODO Auto-generated method stub

	}

	@Override
	public void startCooldown(String cooldownName, int time) {
		// TODO Auto-generated method stub

	}

	@Override
	public void registerNewCooldown(String cooldownName, int time) {
		// TODO Auto-generated method stub

	}
}