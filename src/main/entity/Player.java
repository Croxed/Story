package main.entity;

import main.BoundingBox;
import main.Game;
import main.block.LavaBlock;
import main.block.WaterBlock;
import main.spell.Spell;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

public class Player extends Entity
{
	private boolean debug = false; // DEBUG
	private boolean jumping = false;
	private boolean falling = false;
	private boolean spellAttackCD, hurt, onFire;
	private int health = 5;
	private int mana = 10;
	private int exp = 0;
	private int stars = 0;
	private int score = 0;
	private int damage = 1;
	private int shrooms = 0;
	private Animation sprite, moveAnimationRight, moveAnimationLeft, spellAnimationRight, spellAnimationLeft;
	private Image top, fireCloud;
	private float velocityY = 0.0f;
	private float SPEED = 0.14f, GRAVITATIONAL_CONSTANT = 0.010f, spawnX, spawnY;

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

		// Init res
		initRes();

		registerNewEntity(this);
		registerNewCooldown("spellAttack", 500, this);
		registerNewCooldown("hurt", 500, this);
		registerNewCooldown("inLava", 500, this);
		registerNewCooldown("inWater", 500, this);
		registerNewCooldown("onFireDamage", 1000, 3, this);
		registerNewCooldown("onFire", 3000, this);
		System.out.println("Player Info: Width: " + Xpx + " Height: " + Ypx + " X: " + x + " Y: " + y);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) 
	{
		sprite.draw(getX(), getY()); // Draws the body of the player
		g.drawImage(top, getX(), getY() - 16); // Draws the image with the top over the player

		// Draw the blood decal on top of the Player
		if(hurt)
		{

		}

		// Draw the fire decal on top of the Player
		if(onFire)
		{
			fireCloud.drawCentered(getCenterX(), getCenterY());
		}

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

		// Handle input for attacks & stuffs 
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
			if(!hurt)
			{
				takeDamage(1);
				hurt = true;
				startCooldown("hurt", 500);
			}
			Log.info("Player hit by an enemy!!");
		}

		if(box instanceof LavaBlock)
		{
			onFire = true;
			SPEED = 0.08f;
			startCooldown("inLava", 500);
			startCooldown("onFireDamage", 1000, 3);
			startCooldown("onFire", 3000);
		}

		if(box instanceof WaterBlock)
		{
			SPEED = 0.08f;
			startCooldown("inWater", 500);
		}
	}

	public void attack()
	{
		if(!spellAttackCD && mana > 0)
		{
			Game.renderList.add(new Spell(getX(), getY(), 16, 16,
					damage, spellAnimationLeft, spellAnimationRight, Game.bitKeys.lastX(), Game.bitKeys.lastY()));
			mana -= 1;
			spellAttackCD = true;
			startCooldown("spellAttack", 500);
		}
	}

	@Override
	public void takeDamage(int damage) 
	{
		if(!hurt)
		{
			hurt = true;
			startCooldown("hurt", 500);
			health -= damage;
			if(getHealth() - damage < 0)
			{
				health = 0;
				death();
			}
		}
	}

	@Override
	public void death() 
	{
		// TODO: Draw the death animation, bring up the deathScreen, animate smoke at the deathscene, render the hurt animation 
		health = 5;
		score = 0;
		mana = 10;
		this.setX(spawnX);
		this.setY(spawnY);
	}

	@Override
	public void cooldownFinished(String cooldownName) 
	{
		if(cooldownName.equals("spellAttack"))
		{
			spellAttackCD = false;
		}

		if(cooldownName.equals("hurt"))
		{
			hurt = false;
		}

		if(cooldownName.equals("inLava"))
		{
			restoreSPEED();
		}

		if(cooldownName.equals("inWater"))
		{
			restoreSPEED();
		}

		if(cooldownName.equals("onFireDamage"))
		{
			takeDamage(1);
		}

		if(cooldownName.equals("onFire"))
		{
			onFire = false;
		}
	}

	public void givePlayerExp(int exp)
	{
		this.exp += exp;
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

	public int getMana()
	{
		return mana;
	}

	public int getExp()
	{
		return exp;
	}

	public void restoreSPEED()
	{
		SPEED = 0.17f;
	}

	public void initRes()
	{
		try 
		{
			// TODO Fill this with all the resource getting for the Player.
			fireCloud = new Image("res/crawl_tiles/effect/bolt04.png");
		} catch (SlickException e) 
		{
			Log.error("Failed to load res for Player.");
			e.printStackTrace();
		}
	}
}