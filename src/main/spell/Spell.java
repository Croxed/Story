package main.spell;

import main.BoundingBox;
import main.Game;
import main.LightSource;
import main.entity.Entity;
import main.entity.Player;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

public class Spell extends Attack implements LightSource { 

	private int damage = 1;
	private float velocityX, velocityY = 0.0f;
	private float deltaX, deltaY = 0.0f;
	private Animation sprite, leftAnimation, rightAnimation;
	private boolean collision = false;
	private Image lightImage;

	public Spell(float x, float y, float Xpx, float Ypx, int damage
			, Animation rightAnimation, Animation leftAnimation, int X, int Y) 
	{
		super(x, y, Xpx, Ypx);
		this.damage = damage;
		this.leftAnimation = leftAnimation;
		this.rightAnimation = rightAnimation;
		setDirection(X, Y, x, y);
		initRes();
		Game.lightSourceList.add(this);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
	{
		this.getBoundingBox().setX(this.getX() + velocityX);
		this.getBoundingBox().setY(this.getY() + velocityY);
		// g.drawRect(getX() + velocityX, getY() + velocityY, this.getBoundingBox().getWidth(), this.getBoundingBox().getHeight()); // DEBUG
		sprite.draw(this.getX() + velocityX, this.getY() + velocityY);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
	{
		if(Game.isBlocked(this.getMaxX() + velocityX, this.getCenterY()) || collision) 
		{
			Game.removeBoundingBoxRenderList(this);
			Game.removeLightSourceList(this);
		}

		// Accelerate velocity & sprite
		velocityX += deltaX / 1000;
		velocityY += deltaY / 1000;
		sprite.update(delta);
	}

	@Override
	public void collision(BoundingBox box)
	{
		if(box instanceof Entity && !(box instanceof Player) && !(box instanceof Spell))
		{
			Log.info("Spell hit Entity - " + box.toString()); // DEBUG
			((Entity) box).takeDamage(this.getDamage());
			this.collision = true;
		}
	}

	/*
	 * Calculates where the fireball should go
	 */
	public void setDirection(float xMouse, float yMouse, float xPos, float yPos)
	{
		deltaX = xMouse - xPos;
		deltaY = yMouse - yPos;

		if(xMouse < xPos)
		{
			this.sprite = this.rightAnimation;
		}else
		{
			this.sprite = this.leftAnimation;
		}
	}

	@Override
	public int getDamage()
	{
		return this.damage;
	}

	@Override
	public void renderLight(GameContainer container, StateBasedGame game,
			Graphics g) 
	{
		lightImage.drawCentered(getCenterX() + velocityX, getCenterY() + velocityY);
	}

	private void initRes() 
	{
		try 
		{
			lightImage = new Image("res/lighting/spheres/fireball.png");
		} catch (SlickException e)
		{
			e.printStackTrace();
		}
	}
}