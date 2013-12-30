package main.item;

import main.BoundingBox;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

public class Diamond extends Item {

	private int TYPE;

	public Diamond(float x, float y, float Xpx, float Ypx, int TYPE) {
		super(x, y, Xpx, Ypx);
		this.TYPE = TYPE;
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) {
		// TODO Auto-generated method stub

	}

	@Override
	public void collision(BoundingBox boundingBox) {
		// TODO Auto-generated method stub

	}

}
