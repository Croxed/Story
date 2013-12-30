package main;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class Start extends StateBasedGame
{
	public static Game world;

	public Start()
	{
		super("Story");
	}

	public static void main(String[] arguments)
	{
		try
		{
			AppGameContainer app = new AppGameContainer(new Start());
			app.setIcon("res/icons/app_icon.png");
			app.setMultiSample(2);

			DisplayMode[] modes;
			try {
				modes = Display.getAvailableDisplayModes();

				for (int i=0;i<modes.length;i++) {
					DisplayMode current = modes[i];
					System.out.println(current.getWidth() + "x" + current.getHeight() + "x" +
							current.getBitsPerPixel() + " " + current.getFrequency() + "Hz");
				}
				System.out.println(app.getAspectRatio());

				app.setDisplayMode(1024, 768, true);
				app.start();

			} catch (LWJGLException e) 
			{
				e.printStackTrace();
			}
		}
		catch (SlickException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException 
	{
		addState(new Menu());
		addState(world = new Game());
	}
}

