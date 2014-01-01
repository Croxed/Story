package main.block;

import main.BoundingBox;
import main.Game;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.state.StateBasedGame;

public class WaterBlock extends Block {

	private Image waterImage;

	public WaterBlock(float x, float y) 
	{
		super(x, y, 15, 15);
		Game.renderList.add(this);
		initRes();
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) 
	{
		// TODO Auto-generated method stub - animate
		waterImage.draw(getX(), getY());
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
		waterImage = Game.tileSheet.getSprite(0, 0);
	}
}
