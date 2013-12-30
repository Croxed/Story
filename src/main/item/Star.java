package main.item;

import main.BoundingBox;
import main.Game;
import main.entity.Player;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.state.StateBasedGame;

public class Star extends Item {

	private Image image;

	public Star(float x, float y, float Xpx, float Ypx, Image image) 
	{
		super(x, y, Xpx, Ypx);
		this.image = image;
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) 
	{
		g.drawImage(this.image, this.getX(), this.getY());
		// Add some fancy sparkles
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) 
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void collision(BoundingBox box)
	{
		if(box instanceof Player)
		{
			Game.player.pickedUpStar();
			Game.removeBoundingBoxRenderList(this);
			System.out.println("Star picked up!!");
		}
	}
}