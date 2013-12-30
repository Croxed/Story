package main.item;

import main.BoundingBox;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

public class Key extends Item {

	private int TYPE;
	private boolean levitating = true;

	public Key(float x, float y, float Xpx, float Ypx, int TYPE, boolean levitating) {
		super(x, y, Xpx, Ypx);
		this.TYPE = TYPE;
		this.levitating = levitating;
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		// TODO Auto-generated method stub
		// If levitating then it's stuck in place. Later it's affected by gravity just like
		// everything else
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
