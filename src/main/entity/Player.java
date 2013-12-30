package main.entity;

import java.util.Timer;

import main.BoundingBox;
import main.Game;
import main.spell.Spell;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

public class Player extends Entity
{
	private boolean debug = false; // DEBUG
	private boolean hurt = false;
	private boolean jumping = false;
	private boolean falling = false;
	private boolean fireBallCD = false;
	private boolean healthLostCD = false;
	private int fireBallCDTime = 500;
	private int healthLostCDTime = 500;
	private int health = 5;
	private int stars = 0;
	private int score = 0;
	private int damage = 1;
	private int shrooms = 0;
	private Animation sprite, moveAnimationRight, moveAnimationLeft, spellAnimationRight, spellAnimationLeft;
	private Image top;
	private float velocityY = 0.0f;
	private float SPEED = 0.17f, GRAVITATIONAL_CONSTANT = 0.010f, spawnX, spawnY;
	private Timer timer = new Timer();

	public Player(float x, float y, float Xpx, float Ypx, Animation moveAnimationRight,
			Animation moveAnimationLeft, Image top, Animation spellAnimationRight, Animation spellAnimationLeft)
	{
		super(x, y, Xpx, Ypx);
		this.spawnX = x;
		this.spawnY = y;
		this.moveAnimationRight = moveAnimationRight;
		this.moveAnimationLeft = moveAnimationLeft;
		this.spellAnimationRight = spellAnimationRight;
		this.spellAnimationLeft = spellAnimationLeft;
		this.sprite = moveAnimationLeft;
		this.top = top;
		System.out.println("Player Info: Width: " + Xpx + " Height: " + Ypx + " X: " + x + " Y: " + y);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) 
	{
		sprite.draw(getX(), getY()); // Draws the body of the player
		g.drawImage(top, getX(), getY() - 16); // Draws the image with the top over the player

		if(debug)
		{
			g.draw(getBoundingBox());
			g.drawLine(getCenterX(), 0, getCenterX(), 30 * 16);
			g.drawLine(0, getCenterY(), 60 * 16, getCenterY());
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) 
	{
		if(!Game.isBlocked(getMinX(), getMaxY() + getVelocityY()) // Checks lower left
				&& !Game.isBlocked(getMaxX(), getMaxY() + getVelocityY())) // Checks lower right
		{
			setCenterY(getVelocityY());
			setVelocityY(delta * GRAVITATIONAL_CONSTANT);
			falling = true;
		}else 
		{
			resetVelocityY(); // Player landed - reset gravity & jumpinga
		}

		// Checks the top (left, mid, right) and if blocked it bounces back the player
		if(Game.isBlocked(getCenterX(), getMinY() + velocityY)
				|| Game.isBlocked(getMaxX(), getMinY() + velocityY)
				|| Game.isBlocked(getMinX(), getMinY() + velocityY))
		{
			velocityY = 0.1f;
		}

		container.getInput();
		if (Game.bitKeys.isKeyPressed(Input.KEY_W))
		{
			if(!jumping && !falling)
			{
				setJump(SPEED * 40);
			}
		}

		if(Game.bitKeys.isKeyPressed(Input.KEY_D))
		{
			setSprite(0);
			if(!Game.isBlocked(getMaxX() + delta * SPEED, getMinY()) // Top right
					&& !Game.isBlocked(getMaxX() + delta * SPEED, getMaxY())) // Bot right 
			{
				setCenterX(delta * SPEED);
				sprite.update(delta);
			}
		}

		if (Game.bitKeys.isKeyPressed(Input.KEY_A))
		{
			setSprite(1);
			if(!Game.isBlocked(getMinX() - delta * SPEED, getMinY()) // Top left
					&& !Game.isBlocked(getMinX() - delta * SPEED, getMaxY())) // Bot left 
			{
				setCenterX(-delta * SPEED);
				sprite.update(delta);
			}
		}

		// Activate stuffs
		if(Game.bitKeys.isKeyPressed(Input.KEY_S))
		{

		}

		// Handle cooldowns & timers for all the attacks & spells 
		if(fireBallCD)
		{
			fireBallCDTime += delta;
			if(fireBallCDTime > 500)
			{
				fireBallCD = false;
			}
		}

		if(healthLostCD)
		{
			healthLostCDTime += delta;
			if(healthLostCDTime > 500)
			{
				healthLostCD = false;
			}
		}

		// Handle input for attacks & stuffs
		// TODO: Need to have a attack cooldown/fixed firerate
		if(Game.bitKeys.isLeftMouseClicked())
		{
			attack();
		}
	}

	public void setJump(float velocity)
	{
		velocityY -= velocity;
		jumping = true;
	}

	@Override
	public void collision(BoundingBox box)
	{
		if(box instanceof Enemy)
		{
			if(!healthLostCD)
			{
				takeDamage(1);
				healthLostCD = true;
				healthLostCDTime = 0;
			}
			Log.info("Player hit by an enemy!!");
		}

		// Implement a solid cooldown system to keep track of all the things

		// HurtCD, SpellAttackCD, AttackCD, periodic fire damage, freeze effects, 

		//		if(box instanceof LavaBlock)
		//		{
		//			this.SPEED -= 0.00001f;
		//			// Start the timer for periodic damage (aka fire on the char)
		//			if(lavaTimer >= 500)
		//			{
		//				health -= 1;
		//				lavaTimer = 0;
		//			}
		//		}
		//
		//		if(box instanceof WaterBlock)
		//		{
		//			this.SPEED -= 0.00001f;
		//			if(waterTimer >= 500)
		//			{
		//				health -= 1;
		//				waterTimer = 0;
		//			}
		//		}
	}

	/*
	 * Adds a new spell to the render list (aka game world)
	 */
	public void attack()
	{
		// Set the cooldown to true
		if(!fireBallCD)
		{
			Game.renderList.add(new Spell(getX(), getY(), 16, 16,
					damage, spellAnimationLeft, spellAnimationRight, Game.bitKeys.lastX(), Game.bitKeys.lastY()));
			fireBallCD = true;
			fireBallCDTime = 0;
		}
	}

	public void setSprite(int i)
	{
		if(i == 0)
		{
			sprite = moveAnimationLeft;
		}else
		{
			sprite = moveAnimationRight;
		}
	}

	public void  setVelocityY(float change)
	{
		velocityY += change;
	}

	public float getVelocityY()
	{
		return velocityY;
	}

	public void resetVelocityY()
	{
		falling = false;
		jumping = false;
		velocityY = 0;
	}

	public int getHealth() 
	{
		return health;
	}

	public void setHealth(int health) 
	{
		this.health = health;
	}

	public void healed(int heal)
	{
		health += heal;
	}

	public int getStars() 
	{
		return stars;
	}

	public void pickedUpStar() 
	{
		stars += 1;
		score += 10;
	}

	public int getShrooms()
	{
		return shrooms;
	}

	/*
	 * Called when picked up a shroom
	 * Argument type: String "red" / "blue" / "green" / "purple" / "yellow"
	 * Based on type has a certain effect on player
	 */
	public void pickedUpShroom(String type)
	{
		shrooms += 1;
	}

	/*
	 * Called when picked up a coin
	 * Takes the string type as argument - "bronze" / "silver" / "gold"
	 * Gives player score depending on type.
	 */
	public void addScore(int value)
	{
		score += value;
	}

	public int getScore()
	{
		return score;
	}

	@Override
	public void takeDamage(int damage) 
	{
		hurt = true;
		health -= damage;
		if(getHealth() - damage < 0)
		{
			health = 0;
			death();
		}
	}

	@Override
	public void death() 
	{
		// TODO: Draw the death animation, bring up the deathScreen, animate smoke at the deathscene, render the hurt animation also ...
		health = 5;
		score = 0;
		this.setX(spawnX);
		this.setY(spawnY);
	}
}