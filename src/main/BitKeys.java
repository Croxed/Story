package main;

import java.util.BitSet;

import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.MouseListener;


public class BitKeys implements KeyListener, MouseListener {

	private BitSet keyBits = new BitSet(256);
	private boolean leftButton, rightButton = false;
	private int x, y = 0;

	@Override
	public void inputEnded() {
		// TODO Auto-generated method stub

	}

	@Override
	public void inputStarted() {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean isAcceptingInput() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void setInput(Input input) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(int key, char c) {
		// TODO Auto-generated method stub
		System.out.println( c + " = Key was pressed");
		keyBits.set(key);
	}

	@Override
	public void keyReleased(int key, char c) {
		// TODO Auto-generated method stub
		System.out.println("Key released called!");
		keyBits.clear(key);
	}

	public boolean isKeyPressed(final int keyCode)
	{
		return keyBits.get(keyCode);
	}

	@Override
	public void mouseClicked(int button, int x, int y, int clickCount) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(int oldx, int oldy, int newx, int newy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(int button, int x, int y) {
		// TODO Auto-generated method stub
		switch(button)
		{
		case 0:
			leftButton = true;
			this.x = x;
			this.y = y;
			break;
		}
	}

	@Override
	public void mouseReleased(int button, int x, int y) {
		// TODO Auto-generated method stub
		switch(button)
		{
		case 0:
			leftButton = false;
			break;
		}		
	}

	public int lastX()
	{
		return x;
	}

	public int lastY()
	{
		return y;
	}

	public boolean isRightMouseClicked()
	{
		return rightButton;
	}

	public boolean isLeftMouseClicked()
	{
		return leftButton;
	}

	@Override
	public void mouseWheelMoved(int change) {
		// TODO Auto-generated method stub

	}
}