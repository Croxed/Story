package main.item;

import main.BoundingBox;
import main.Game;
import main.LightSource;
import main.entity.Player;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class ExpOrb extends Item implements LightSource {

	private Image image, lightImage;
	private int expValue = 1;
	private float velocityY, GRAVITATIONAL_CONSTANT = 0.008f, scale = 0.5f;
	private int size = 1;

	public ExpOrb(float x, float y, int size) 
	{
		super(x, y, 13, 13);
		this.size = size;
		// Remove these manual addings to the Game lists
		Game.lightSourceList.add(this);
		Game.renderList.add(this);
		Game.updateList.add(this);
		initRes();
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) 
	{
		image.draw(getX(), getY(), scale);
	}

	@Override
	public void renderLight(GameContainer container, StateBasedGame game,
			Graphics g) 
	{
		lightImage.drawCentered(getCenterX(), getCenterY());
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
	{
		if(!Game.isBlocked(getMinX(), getMaxY() + velocityY) // Checks lower left
				&& !Game.isBlocked(getMaxX(), getMaxY() + velocityY)) // Checks lower right
		{
			setCenterY(velocityY);
			velocityY += (delta * GRAVITATIONAL_CONSTANT);
		}else
		{
			velocityY = 0;
		}
	}

	@Override
	public void collision(BoundingBox boundingBox) 
	{
		if(boundingBox instanceof Player)
		{
			Game.player.givePlayerExp(expValue);
			destroyItem();
		}

		if(boundingBox instanceof ExpOrb)
		{
			// Destroy this orb and the collided.
			// new ExpOrb(getX(), getY(), size + ((ExpOrb) boundingBox).getSize());
		}
	}

	public int getSize()
	{
		return size;
	}

	public void initRes()
	{
		try
		{
			image = new Image("res/crawl_tiles/item/misc/misc_crystal.png");
			lightImage = new Image("res/lighting/spheres/torch.png");
		}catch(SlickException e)
		{
			e.printStackTrace();
		}
	}
}