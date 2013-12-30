package main;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

public class QuadTree {

	private int MAX_OBJECTS = 10;
	private int MAX_LEVELS = 5;

	private int level;
	private List<BoundingBox> objects;
	private Shape bounds;
	private QuadTree[] nodes;

	/*
	 * Constructor
	 */
	public QuadTree(int pLevel, Rectangle rectangle) {
		level = pLevel;
		objects = new ArrayList<BoundingBox>();
		bounds = rectangle;
		nodes = new QuadTree[4];
	}

	/*
	 *  Would really need a method that finds a Entity and removes it.
	 */

	/*
	 * Clears the quadtree
	 */
	public void clear() {
		objects.clear();

		for (int i = 0; i < nodes.length; i++) {
			if (nodes[i] != null) {
				nodes[i].clear();
				nodes[i] = null;
			}
		}
	}

	/*
	 * Splits the node into 4 subnodes
	 */
	private void split() {
		int subWidth = (int) (bounds.getWidth() / 2);
		int subHeight = (int) (bounds.getHeight() / 2);
		int x = (int)bounds.getX();
		int y = (int)bounds.getY();

		nodes[0] = new QuadTree(level+1, new Rectangle(x + subWidth, y, subWidth, subHeight));
		nodes[1] = new QuadTree(level+1, new Rectangle(x, y, subWidth, subHeight));
		nodes[2] = new QuadTree(level+1, new Rectangle(x, y + subHeight, subWidth, subHeight));
		nodes[3] = new QuadTree(level+1, new Rectangle(x + subWidth, y + subHeight, subWidth, subHeight));
	}

	/*
	 * Determine which node the object belongs to. -1 means
	 * object cannot completely fit within a child node and is part
	 * of the parent node
	 */
	private int getIndex(BoundingBox boundingBox) 
	{
		int index = -1;
		double verticalMidpoint = bounds.getX() + (bounds.getWidth() / 2);
		double horizontalMidpoint = bounds.getY() + (bounds.getHeight() / 2);

		// Object can completely fit within the top quadrants
		boolean topQuadrant = (boundingBox.getY() < horizontalMidpoint && boundingBox.getY() + boundingBox.getBoundingBox().getHeight() < horizontalMidpoint);
		// Object can completely fit within the bottom quadrants
		boolean bottomQuadrant = (boundingBox.getY() > horizontalMidpoint);

		// Object can completely fit within the left quadrants
		if (boundingBox.getX() < verticalMidpoint && boundingBox.getX() + boundingBox.getBoundingBox().getWidth() < verticalMidpoint)
		{
			if (topQuadrant) 
			{
				index = 1;
			}
			else if (bottomQuadrant)
			{
				index = 2;
			}
		}
		// Object can completely fit within the right quadrants
		else if (boundingBox.getX() > verticalMidpoint) 
		{
			if (topQuadrant) {
				index = 0;
			}
			else if (bottomQuadrant) 
			{
				index = 3;
			}
		}

		return index;
	}

	/*
	 * Insert the object into the quadtree. If the node
	 * exceeds the capacity, it will split and add all
	 * objects to their corresponding nodes.
	 */
	public void insert(BoundingBox boundingBox) {
		if (nodes[0] != null) {
			int index = getIndex(boundingBox);

			if (index != -1) {
				nodes[index].insert(boundingBox);

				return;
			}
		}

		objects.add(boundingBox);

		if (objects.size() > MAX_OBJECTS && level < MAX_LEVELS) {
			if (nodes[0] == null) { 
				split(); 
			}

			int i = 0;
			while (i < objects.size()) {
				int index = getIndex(objects.get(i));
				if (index != -1) {
					nodes[index].insert(objects.remove(i));
				}
				else {
					i++;
				}
			}
		}
	}

	/*
	 * Return all objects that could collide with the given object
	 */
	public List<BoundingBox> retrieve(List<BoundingBox> returnObjects, BoundingBox pRect) {
		int index = getIndex(pRect);
		if (index != -1 && nodes[0] != null) {
			nodes[index].retrieve(returnObjects, pRect);
		}

		returnObjects.addAll(objects);

		return returnObjects;
	}
}