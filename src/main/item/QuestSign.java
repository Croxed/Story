package main.item;

import main.BoundingBox;
import main.entity.Player;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

public class QuestSign extends Item {

	private Image image;
	private boolean displayQuestText = false;
	private float timer = 0.0f;
	private String MESSAGE = "! Welcome to Fairy Sky Temple! Its blue.";

	public QuestSign(float x, float y, float Xpx, float Ypx, Image image) 
	{
		super(x, y, Xpx, Ypx);
		this.image = image;
		// Needs to fetch its quest text from somewhere
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) 
	{
		if(displayQuestText)
		{			
			g.setColor(Color.black);
			g.fillRoundRect(getX() - 5, getY() - 50, MESSAGE.length() * 9.5f, 15, 10);
			g.setColor(Color.white);
			g.drawString(MESSAGE, getX(), getY() - 52.5f);
		}
		image.draw(getX(), getY());
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) 
	{
		if(displayQuestText)
		{
			timer += delta;
			if(timer >= 10000)
			{
				displayQuestText = false;
				timer = 0;
			}
		}
	}

	@Override
	public void collision(BoundingBox boundingBox)
	{
		if(boundingBox instanceof Player)
		{
			displayQuestText = true;
			Log.info("QuestSign ");
		}
	}
}