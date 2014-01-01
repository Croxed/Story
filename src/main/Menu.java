package main;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

public class Menu extends BasicGameState
{
	private Image menu_screen, menu_logo;
	private	BitKeys bitKeys = new BitKeys();

	@Override
	public void init(GameContainer arg0, StateBasedGame arg1)
			throws SlickException 
			{
		arg0.setTargetFrameRate(60);
		arg0.setVSync(true); 

		Input input = arg0.getInput();
		bitKeys.setInput(input);
		input.addKeyListener(bitKeys);

		menu_screen = new Image("res/menu/menu_screen.png");
		menu_logo = new Image("res/menu/logo_story.png");

		arg1.enterState(1, new FadeOutTransition(), new FadeInTransition());
			}

	@Override
	public void render(GameContainer arg0, StateBasedGame arg1, Graphics arg2)
			throws SlickException 
			{
		arg2.drawImage(menu_screen, 0, 0);
		arg2.drawString(new String("Press ENTER to Play ..."), arg0.getWidth() / 2 - 100, arg0.getHeight() - 150);
		arg2.drawImage(menu_logo, arg0.getWidth() / 2 - 135, arg0.getHeight() / 5);
			}

	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2)
			throws SlickException 
			{
		// Handle the button click detections
		if(bitKeys.isKeyPressed(Input.KEY_ENTER))
		{
			arg1.enterState(1, new FadeOutTransition(), new FadeInTransition());
		}
			}

	@Override
	public int getID() 
	{
		return 0;
	}
}
