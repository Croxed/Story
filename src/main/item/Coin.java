package main.item;

import main.BoundingBox;
import main.Game;
import main.entity.Player;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.state.StateBasedGame;

public class Coin extends Item {

	private Image image;
	private int value = 0;

	public Coin(float x, float y, float Xpx, float Ypx, Image image, int value) {
		super(x, y, Xpx, Ypx);
		this.image = image;
		this.value = value;
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) 
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) 
	{
		g.drawImage(this.image, this.getX(), this.getY());
		// Add some fancy sparkles
	}

	@Override
	public void collision(BoundingBox box)
	{
		if(box instanceof Player)
		{
			Game.player.addScore(value);
			Game.removeBoundingBoxRenderList(this);
		}
	}
}
