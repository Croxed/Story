package main.block;

import main.BoundingBox;
import main.Game;
import main.LightSource;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class LavaBlock extends Block implements LightSource {

	private Image lavaImage, lightImage;

	public LavaBlock(float x, float y) 
	{
		super(x, y, 15, 15);
		Game.renderList.add(this);
		Game.lightSourceList.add(this);
		initRes();
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) 
	{
		// TODO Auto-generated method stub - animate
		lavaImage.draw(getX(), getY());
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) 
	{
		// TODO Auto-generated method stub - animate
	}

	@Override
	public void collision(BoundingBox boundingBox) 
	{

	}

	public void initRes()
	{
		lavaImage = Game.tileSheet.getSprite(0, 0);
		try 
		{
			lightImage = new Image("res/lighting/spheres/fireball.png");
		} catch (SlickException e) 
		{
			e.printStackTrace();
		}
	}

	@Override
	public void renderLight(GameContainer container, StateBasedGame game,
			Graphics g) 
	{
		lightImage.drawCentered(getCenterX(), getCenterY());
	}
}
