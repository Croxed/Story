package main.item;

import main.BoundingBox;
import main.Game;
import main.LightSource;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class Torch extends Item implements LightSource {

	private Animation animation;
	private Image lightImage;

	public Torch(float x, float y, float Xpx, float Ypx, Animation animation) 
	{
		super(x, y, Xpx, Ypx);
		this.animation = animation;
		initLightRes();
		Game.renderList.add(this);
		Game.lightSourceList.add(this);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) 
	{
		// TODO Auto-generated method stub
		animation.draw(getX(), getY());
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) 
	{
		// TODO Auto-generated method stub
		animation.update(delta);
	}

	@Override
	public void collision(BoundingBox boundingBox) {
		// TODO Auto-generated method stub

	}

	@Override
	public void renderLight(GameContainer container, StateBasedGame game,
			Graphics g)
	{
		lightImage.drawCentered(getCenterX(), getCenterY());
	}

	public void initLightRes()
	{
		try
		{
			lightImage = new Image("res/lighting/spheres/torch.png");
		}catch(SlickException e)
		{
			e.printStackTrace();
		}
	}
}
