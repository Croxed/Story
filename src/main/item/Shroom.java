package main.item;

import main.BoundingBox;
import main.Game;
import main.entity.Player;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.state.StateBasedGame;

public class Shroom extends Item {

	private String type = "red";
	private boolean pickedUp = false;
	private Image image;

	public Shroom(float x, float y, float Xpx, float Ypx, Image image, String type) 
	{
		super(x, y, Xpx, Ypx);
		this.image = image;
		this.type = type;
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) 
	{
		if(!this.pickedUp) 
		{
			g.drawImage(this.image, this.getX(), this.getY());
			// Add some fancy sparkles
		}else
		{
			Game.removeBoundingBoxRenderList(Shroom.this);
		}
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
			Game.player.pickedUpShroom(type);
			pickedUp = true;
		}
	}
}
