package main;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

public interface LightSource {

	/*
	 * Called when LightSource is asked to render.
	 */
	void renderLight(GameContainer container, StateBasedGame game, Graphics g);
}
