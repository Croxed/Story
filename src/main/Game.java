package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import main.block.LavaBlock;
import main.block.WaterBlock;
import main.entity.Fly;
import main.entity.Player;
import main.entity.Slime;
import main.item.Coin;
import main.item.Diamond;
import main.item.Flag;
import main.item.Key;
import main.item.QuestSign;
import main.item.Shroom;
import main.item.Spike;
import main.item.Star;
import main.item.Torch;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.util.Log;

public class Game extends BasicGameState {

	private static TiledMap map;
	public static Player player;
	private final static int SIZE = 16;
	public static BitKeys bitKeys;
	private final int MAP_WIDTH = 64, MAP_HEIGHT = 48;
	private static boolean[][] blocked;
	private static QuadTree quadTree;
	private List<BoundingBox> returnObjects;
	public static List<BoundingBox> renderList;
	public static List<BoundingBox> updateList;
	public static List<LightSource> lightSourceList;
	public static CooldownManager cooldownManager;

	private int[] duration = {300, 300};
	private int[] spellAniDuration = {250, 200, 250, 200};

	public static SpriteSheet tileSheet, tileSheetReverse, hurt_tileSheet;
	private Image starImage, shroomImageRed, shroomImageYellow,
	shroomImageGreen, shroomImageBlue, shroomImagePurple, bronze_coin, silver_coin, gold_coin ,saveSign, helpSign, questSign, healSign, 
	fullHeart, emptyHeart, background, numberZero, numberOne, numberTwo, numberThree, numberFour, numberFive, numberSix, numberSeven, numberEight,
	numberNine, foreground, fullManaCrystal, halfManaCrystal, emptyManaCrystal;
	private float spawnX, spawnY;
	private int starsLayerIndex, shroomsLayerIndex, coinsLayerIndex, spawnLayerIndex, exitLayerIndex, flagsLayerIndex, torchesLayerIndex, signsLayerIndex, diamondLayerIndex,
	keysLayerIndex, spikesLayerIndex, fliesLayerIndex, slimesLayerIndex, waterLayerIndex, lavaLayerIndex;

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException 
			{
		// Init RenderList
		renderList = new ArrayList<BoundingBox>();

		// Init lightSourceList
		lightSourceList = new ArrayList<LightSource>();

		// Init UpdateList
		updateList = new ArrayList<BoundingBox>();

		// Load map res
		map = new TiledMap("res/levels/level_0.tmx");

		// Load spriteSheet
		tileSheet = new SpriteSheet("res/tilesets/base_tileset.png", SIZE, SIZE);

		// Load reverse spriteSheet
		tileSheetReverse = new SpriteSheet("res/tilesets/base_tileset_reverse.png", SIZE, SIZE);

		// Load spritesheet with hurt images
		hurt_tileSheet = new SpriteSheet("res/tilesets/hurt_tilesheet.png", SIZE, SIZE);

		// Init object "might collide" list - for Entites
		returnObjects = new ArrayList<BoundingBox>();

		// Init CooldownManager
		cooldownManager = new CooldownManager();

		// Init BitKeys for handling inputs
		bitKeys = new BitKeys();
		bitKeys.setInput(container.getInput());
		container.getInput().addKeyListener(bitKeys);
		container.getInput().addMouseListener(bitKeys);

		// Init ground/blocked tiles
		initGround();

		// Find & create all object layers
		initObjects();

		// Init map bg
		initMap();

		// Init player 
		initPlayer();

		// Init enemies
		// initFlies();
		initSlimes();

		// Visuals
		initHUD();

		initCollisionDetection();
			}

	public void initObjects() throws SlickException
	{
		int objectLayers = map.getObjectGroupCount();

		for(int i = 0; i < objectLayers; i++)
		{
			String objectName = map.getObjectName(i, 0);
			Log.info(objectName);
			try
			{
				if(objectName.equals("Star"))
				{
					starsLayerIndex = i;
					initStars();
					continue;
				}

				if(objectName.equals("Coin"))
				{
					coinsLayerIndex = i;
					initCoins();
					continue;
				}

				if(objectName.equals("Shroom"))
				{
					shroomsLayerIndex = i;
					initShrooms();
					continue;
				}

				if(objectName.equals("Exit"))
				{
					exitLayerIndex = i;
					initExit();
					continue;
				}

				if(objectName.equals("Flag"))
				{
					flagsLayerIndex = i;
					initFlags();
					continue;
				}

				if(objectName.equals("Spawn"))
				{
					spawnLayerIndex = i;
					initSpawn();
					continue;
				}

				if(objectName.equals("Torch"))
				{
					torchesLayerIndex = i;
					initTorches();
					continue;
				}

				if(objectName.equals("Sign"))
				{
					signsLayerIndex = i;
					initSigns();
					continue;
				}

				if(objectName.equals("Diamond"))
				{
					diamondLayerIndex = i;
					initDiamonds();
					continue;
				}

				if(objectName.equals("Key"))
				{
					keysLayerIndex = i;
					initKeys();
					continue;
				}

				if(objectName.equals("Spike"))
				{
					spikesLayerIndex = i;
					initSpikes();
					continue;
				}

				if(objectName.equals("Fly"))
				{
					fliesLayerIndex = i;
					initFlies();
					continue;
				}

				if(objectName.equals("Slime"))
				{
					slimesLayerIndex = i;
					initSlimes();
					continue;
				}

				if(objectName.equals("Lava"))
				{
					lavaLayerIndex = i;
					initLava();
					continue;
				}

				if(objectName.equals("Water"))
				{
					waterLayerIndex = i;
					initWater();
					continue;
				}
			}catch(NullPointerException e)
			{
				e.printStackTrace();
				Log.info("Couldnt find object(s) in layer(s)!");
			}
		}
	}

	public void initMap() throws SlickException
	{
		background = new Image("res/levels/level_0_bg.png");
		foreground = new Image("res/levels/fg.png"); // TEST
		foreground.setAlpha(0.8f); // TEST
	}

	public void initPlayer() throws SlickException
	{
		// Load Character res
		Image [] movementLeft = {tileSheet.getSprite(9, 26), tileSheet.getSprite(10, 26)};
		Image [] movementRight = {tileSheetReverse.getSprite(55, 26), tileSheetReverse.getSprite(54, 26)};
		Image top = tileSheet.getSprite(2, 25);
		Animation left = new Animation(movementLeft, duration, false);
		Animation right = new Animation(movementRight, duration, false);

		// Init spell images & stuffs for the player
		Image [] spellRight = { tileSheet.getSprite(4, 4), tileSheet.getSprite(5, 4), tileSheet.getSprite(6, 4), tileSheet.getSprite(7, 4)}; // RIGHT
		Image [] spellLeft = {tileSheetReverse.getSprite(59, 4), tileSheetReverse.getSprite(58, 4), tileSheetReverse.getSprite(57, 4), tileSheetReverse.getSprite(56, 4)}; // LEFT
		Animation spellAnimationRight = new Animation(spellRight, spellAniDuration, false);
		Animation spellAnimationLeft = new Animation(spellLeft, spellAniDuration, false);

		// Init the Player
		player = new Player(spawnX, spawnY - 8, SIZE - 1, SIZE, right,
				left, top,spellAnimationRight, spellAnimationLeft);

		// Add first to the renderList
		renderList.add(player);
	}

	public void initSpawn() throws SlickException
	{
		// Sets spawn
		int spawnCount = map.getObjectCount(spawnLayerIndex);
		for (int objectID = 0; objectID < spawnCount; objectID++)
		{
			int xAxis = map.getObjectX(spawnLayerIndex, objectID) / SIZE;
			int yAxis = map.getObjectY(spawnLayerIndex, objectID) / SIZE;

			spawnX = xAxis * SIZE;
			spawnY = yAxis * SIZE;
		}
	}

	public void initGround() throws SlickException
	{
		// Skriv om blocked för att tillåta förstörbara block såsom lådor & hinder
		// Detta kommer tillåta flyttbara block också!
		blocked = new boolean[MAP_WIDTH][MAP_HEIGHT];

		// Init ground
		for (int xAxis=0; xAxis < MAP_WIDTH; xAxis++)
		{
			for (int yAxis=0; yAxis < MAP_HEIGHT; yAxis++)
			{
				// Blocked (ground)
				try
				{
					int tileID = map.getTileId(xAxis, yAxis, 0);
					String value = map.getTileProperty(tileID, "Blocked", "false");

					if ("true".equals(value))
					{
						blocked[xAxis][yAxis] = true;
					}
				}catch(NullPointerException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	public static void initCollisionDetection()
	{
		// Init the quadtree & add all blocks & enitities to the quadtree
		quadTree = new QuadTree(0, new Rectangle(0, 0, map.getWidth() * SIZE, map.getHeight() * SIZE));

		for(int i = 0; i < renderList.size(); i++)
		{
			quadTree.insert(renderList.get(i));
		}
	}

	public void initStars() throws SlickException
	{
		// Load star image
		starImage = tileSheet.getSprite(5, 1);

		// Init stars				
		int starsCount= map.getObjectCount(starsLayerIndex);
		for (int objectID = 0; objectID < starsCount; objectID++)
		{
			int xAxis = map.getObjectX(starsLayerIndex, objectID);
			int yAxis = map.getObjectY(starsLayerIndex, objectID);

			// Set a star object on the map.
			System.out.println("Star info:" + xAxis + " Y: " + yAxis);
			renderList.add(new Star(xAxis, yAxis, SIZE, SIZE, starImage));
		}
	}

	public void initShrooms() throws SlickException
	{
		// Init shrooms images & objects
		shroomImageRed = tileSheet.getSprite(23, 5);
		shroomImageYellow = tileSheet.getSprite(23, 6);
		shroomImageGreen = tileSheet.getSprite(23, 7);
		shroomImageBlue = tileSheet.getSprite(23, 8);
		shroomImagePurple = tileSheet.getSprite(23, 9);

		int shroomsCount = map.getObjectCount(shroomsLayerIndex);
		for (int objectID = 0; objectID < shroomsCount; objectID++)
		{
			int xAxis = map.getObjectX(shroomsLayerIndex, objectID);
			int yAxis = map.getObjectY(shroomsLayerIndex, objectID);

			System.out.println("xAxis: " + xAxis + " yAxis: " + yAxis);

			switch(Integer.valueOf(map.getObjectProperty(3, objectID, "Color", "0")))
			{
			case 0:
				renderList.add(new Shroom(xAxis , yAxis , SIZE, SIZE, shroomImageRed, "Red"));
				break;
			case 1:
				renderList.add(new Shroom(xAxis , yAxis , SIZE, SIZE, shroomImageYellow, "Yellow"));
				break;
			case 2:
				renderList.add(new Shroom(xAxis , yAxis , SIZE, SIZE, shroomImageGreen, "Green"));
				break;
			case 3:
				renderList.add(new Shroom(xAxis , yAxis , SIZE, SIZE, shroomImageBlue, "Blue"));
				break;
			case 4:
				renderList.add(new Shroom(xAxis , yAxis , SIZE, SIZE, shroomImagePurple, "Purple"));
				break;

			}
		}
	}

	public void initCoins() throws SlickException
	{
		// Init coins
		int coinCount = map.getObjectCount(coinsLayerIndex);

		// Get coin images
		bronze_coin = tileSheet.getSprite(4, 10);
		silver_coin = tileSheet.getSprite(5, 10);
		gold_coin = tileSheet.getSprite(6, 10);

		for (int objectID = 0; objectID < coinCount; objectID++)
		{
			int xAxis = map.getObjectX(coinsLayerIndex, objectID) / SIZE;
			int yAxis = map.getObjectY(coinsLayerIndex, objectID) / SIZE;

			switch(Integer.valueOf(map.getObjectProperty(coinsLayerIndex, objectID, "Type", "0")))
			{
			case 0:
				renderList.add(new Coin((float) xAxis * SIZE,(float) yAxis * SIZE, SIZE, SIZE, bronze_coin, 5));
				break;
			case 1:
				renderList.add(new Coin((float) xAxis * SIZE,(float) yAxis * SIZE, SIZE, SIZE, silver_coin, 10));
				break;
			case 2:
				renderList.add(new Coin((float) xAxis * SIZE,(float) yAxis * SIZE, SIZE, SIZE, gold_coin, 15));
				break;
			}
		}

	}

	public void initSigns() throws SlickException
	{
		// Init signs
		int signCount = map.getObjectCount(signsLayerIndex);

		// Get images
		saveSign = tileSheet.getSprite(3, 20);
		questSign = tileSheet.getSprite(1, 18);
		healSign = tileSheet.getSprite(0, 20);
		helpSign = tileSheet.getSprite(2, 19);

		for(int objectID = 0; objectID < signCount; objectID++)
		{
			int xAxis = map.getObjectX(signsLayerIndex, objectID);
			int yAxis = map.getObjectY(signsLayerIndex, objectID);

			switch(Integer.valueOf(map.getObjectProperty(signsLayerIndex, objectID, "Type", "0")))
			{
			case 0:
				renderList.add(new QuestSign(xAxis ,yAxis , SIZE, SIZE, questSign));
				break;
			case 1:

				break;
			case 2:

				break;
			case 3:

				break;
			}
		}
	}

	public void initExit() throws SlickException
	{
		// Init exit
		int exitCount = map.getObjectCount(exitLayerIndex);

		for (int objectID = 0; objectID < exitCount; objectID++)
		{
			int xAxis = map.getObjectX(exitLayerIndex, objectID);
			int yAxis = map.getObjectY(exitLayerIndex, objectID);

			// Set a exit object in the array & on the map.
			System.out.println("Exit info: " + (float) xAxis + " Y: " + (float) yAxis);
			// Set the exit position that is activate by player key down --> next level(?)
		}
	}

	/*
	 * Loads all res for the HUDs
	 */
	public void initHUD() throws SlickException
	{
		fullHeart = tileSheet.getSprite(4, 1);
		emptyHeart = tileSheet.getSprite(0, 1);

		fullManaCrystal = new Image("res/tilesets/HUD/full_mana_crystal.png");
		halfManaCrystal = new Image("res/tilesets/HUD/half_mana_crystal.png");
		emptyManaCrystal = new Image("res/tilesets/HUD/empty_mana_crystal.png");

		numberZero = new Image("res/tilesets/HUD/hud_0.png");
		numberOne = new Image("res/tilesets/HUD/hud_1.png");
		numberTwo = new Image("res/tilesets/HUD/hud_2.png");
		numberThree = new Image("res/tilesets/HUD/hud_3.png");
		numberFour = new Image("res/tilesets/HUD/hud_4.png");
		numberFive = new Image("res/tilesets/HUD/hud_5.png");
		numberSix = new Image("res/tilesets/HUD/hud_6.png");
		numberSeven = new Image("res/tilesets/HUD/hud_7.png");
		numberEight = new Image("res/tilesets/HUD/hud_8.png");
		numberNine = new Image("res/tilesets/HUD/hud_9.png");
	}

	public void initFlies() throws SlickException
	{
		// Import Fly images and make the animations & objects
		Image [] movementRight = {tileSheet.getSprite(3, 31), tileSheet.getSprite(4, 31)};
		Image [] movementLeft = {tileSheetReverse.getSprite(60, 31), tileSheetReverse.getSprite(59, 31)};
		Animation rightAnimation = new Animation(movementLeft, duration, false);
		Animation leftAnimation = new Animation(movementRight, duration, false);

		Image hurtImage = hurt_tileSheet.getSprite(3, 32);
		Image deathImage = tileSheet.getSprite(4, 32);

		int flyCount = map.getObjectCount(fliesLayerIndex);
		for(int objectID = 0; objectID < flyCount; objectID++)
		{
			int xAxis = map.getObjectX(fliesLayerIndex, objectID);
			int yAxis = map.getObjectY(fliesLayerIndex, objectID);
			renderList.add(new Fly(xAxis, yAxis, SIZE, SIZE, leftAnimation, rightAnimation, hurtImage, deathImage));
		}
	}

	public void initSlimes() throws SlickException 
	{
		// Get Slime images & animation
		Image [] movementRightSlime = {tileSheet.getSprite(3, 29), tileSheet.getSprite(4, 29)};
		Image [] movementLeftSlime = {tileSheetReverse.getSprite(60, 29), tileSheetReverse.getSprite(59, 29)};

		Image hurtImageSlime = hurt_tileSheet.getSprite(0, 28);
		Image deathImageSlime = tileSheet.getSprite(0, 29);

		int slimeCount = map.getObjectCount(slimesLayerIndex);
		for(int objectID = 0; objectID < slimeCount; objectID++)
		{
			Animation rightAnimationSlime = new Animation(movementLeftSlime, new Random().nextInt(200) + 400, false);
			Animation leftAnimationSlime = new Animation(movementRightSlime, new Random().nextInt(200) + 400, false);

			int xAxis = map.getObjectX(slimesLayerIndex, objectID);
			int yAxis = map.getObjectY(slimesLayerIndex, objectID);
			renderList.add(new Slime(xAxis, yAxis, SIZE, SIZE, leftAnimationSlime, rightAnimationSlime, hurtImageSlime, deathImageSlime));
		}
	}

	public void initFlags() throws SlickException
	{
		// Get res
		Image[] blueFlagImages = {tileSheet.getSprite(6, 15), tileSheet.getSprite(7, 15)};
		Animation blueFlagAnimation = new Animation(blueFlagImages, new int[]{new Random().nextInt(200) + 500, new Random().nextInt(200) + 500}, false);

		int flagCount = map.getObjectCount(flagsLayerIndex);

		for(int objectID = 0; objectID < flagCount; objectID++)
		{
			int xAxis = map.getObjectX(flagsLayerIndex, objectID);
			int yAxis = map.getObjectY(flagsLayerIndex, objectID);

			switch(Integer.valueOf(map.getObjectProperty(flagsLayerIndex, objectID, "Type", "0")))
			{
			case 0:
				//renderList.add(new Flag(xAxis ,yAxis ,SIZE,SIZE, blueFlagAnimation));
				break;
			case 1:
				//renderList.add(new Flag(xAxis ,yAxis ,SIZE,SIZE, blueFlagAnimation));
				break;
			case 2:
				//renderList.add(new Flag(xAxis ,yAxis ,SIZE,SIZE, blueFlagAnimation));
				break;
			case 3:
				renderList.add(new Flag(xAxis ,yAxis ,SIZE,SIZE, blueFlagAnimation));
				break;
			}
		}
	}

	public void initTorches() throws SlickException
	{
		// Get res
		Image[] torchesImages = {tileSheet.getSprite(4, 19), tileSheet.getSprite(5, 19)};

		int torchesCount = map.getObjectCount(torchesLayerIndex);

		for(int objectID = 0; objectID < torchesCount; objectID++)
		{
			int xAxis = map.getObjectX(torchesLayerIndex, objectID);
			int yAxis = map.getObjectY(torchesLayerIndex, objectID);

			if(map.getObjectProperty(torchesLayerIndex, objectID, "Torch", "false").equals("true"))
			{
				Animation torchesAnimation = new Animation(torchesImages, new int[]{new Random().nextInt(2000) + 500, new Random().nextInt(2000) + 500}, false);
				new Torch(xAxis, yAxis, SIZE, SIZE, torchesAnimation);
			}
		}
	}

	public void initSpikes() throws SlickException
	{
		int spikeCount = map.getObjectCount(spikesLayerIndex);
		Image spikeImage = tileSheet.getSprite(6, 17);

		for(int objectID = 0; objectID < spikeCount; objectID++)
		{
			int xAxis = map.getObjectX(spikesLayerIndex, objectID);
			int yAxis = map.getObjectY(spikesLayerIndex, objectID);
			renderList.add(new Spike(xAxis, yAxis, SIZE, SIZE, spikeImage));
		}
	}

	public void initDiamonds() throws SlickException
	{
		int diamondCount = map.getObjectCount(diamondLayerIndex);

		for(int objectID = 0; objectID < diamondCount; objectID++)
		{
			int xAxis = map.getObjectX(diamondLayerIndex, objectID);
			int yAxis = map.getObjectY(diamondLayerIndex, objectID);

			switch((Integer.valueOf(map.getObjectProperty(diamondLayerIndex, objectID, "Type", "0"))))
			{
			case 0:
				renderList.add(new Diamond(xAxis, yAxis, SIZE, SIZE, 0));
				break;
			case 1:
				renderList.add(new Diamond(xAxis, yAxis, SIZE, SIZE, 1));
				break;
			case 2:
				renderList.add(new Diamond(xAxis, yAxis, SIZE, SIZE, 2));
				break;
			}
		}
	}

	public void initKeys() throws SlickException
	{
		int keyCount = map.getObjectCount(keysLayerIndex);

		for(int objectID = 0; objectID < keyCount; objectID++)
		{
			int xAxis = map.getObjectX(keysLayerIndex, objectID);
			int yAxis = map.getObjectY(keysLayerIndex, objectID);

			switch(Integer.valueOf(map.getObjectProperty(keysLayerIndex, objectID, "Type", "0")))
			{
			case 0:
				renderList.add(new Key(xAxis, yAxis, SIZE, SIZE, 0, true));
				break;
			case 1:
				renderList.add(new Key(xAxis, yAxis, SIZE, SIZE, 0, true));
				break;
			case 2:
				renderList.add(new Key(xAxis, yAxis, SIZE, SIZE, 0, true));
				break;
			}
		}
	}

	public void initLava() throws SlickException
	{
		int lavaCount = map.getObjectCount(lavaLayerIndex);

		for(int objectID = 0; objectID < lavaCount; objectID++)
		{
			int xAxis = map.getObjectX(lavaLayerIndex, objectID);
			int yAxis = map.getObjectY(lavaLayerIndex, objectID);
			new LavaBlock(xAxis, yAxis);
		}
	}

	public void initWater() throws SlickException
	{
		int waterCount = map.getObjectCount(waterLayerIndex);

		for(int objectID = 0; objectID < waterCount; objectID++)
		{
			int xAxis = map.getObjectX(waterLayerIndex, objectID);
			int yAxis = map.getObjectY(waterLayerIndex, objectID);
			new WaterBlock(xAxis, yAxis);
		}
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException 
			{
		g.drawImage(background, 0, 0);
		map.render(0,0);

		for(int i = 0; i < renderList.size(); i++)
		{
			try
			{
				renderList.get(i).render(container, game, g);
			}catch(NullPointerException e)
			{
				e.printStackTrace();
			}
		}

		/** Scaling stuff so we don't have to use big alpha map image */
		float invSize = 1f / 1;
		g.scale(1, 1);

		/** setting alpha channel ready so lights add up instead of clipping */
		g.clearAlphaMap();
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);

		// Call the light sources, lets them draw, coordinates are transformed to scaled coordinates
		for(int i = 0; i < lightSourceList.size(); i++)
		{
			try
			{
				lightSourceList.get(i).renderLight(container, game, g);
			}catch(NullPointerException e)
			{
				e.printStackTrace();
			}
		}

		// Sets the overall light on the level/scene
		Image image2 = new Image("res/lighting/maps/alpha_map_80.png");
		image2.draw(0, 0);

		// Scaling back stuff so we don't have to use big alpha map image
		g.scale(invSize, invSize);

		// setting alpha channel for clearing everything but light maps just added
		GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_DST_ALPHA);

		/** paint everything else with black */
		g.fillRect(0, 0, map.getWidth() * SIZE, map.getHeight() * SIZE);

		/** setting drawing mode back to normal */
		g.setDrawMode(Graphics.MODE_NORMAL);

		drawHearts(container, game, g);
		drawMana(container, game, g);
		drawScore(container, game, g);
		drawExp(container, game, g);
		// Objective title (which expands on mouse hover)
			}

	/*
	 * Called when a LightSource wants to be removed
	 */
	public static void removeLightSourceList(LightSource lightSource)
	{
		lightSourceList.remove(lightSourceList.indexOf(lightSource));
	}

	/*
	 * Called when the Entity does not want to get render/update anymore 
	 *  for exampel when they are destroyed in-game.
	 *  
	 *   Remakes the quad tree be initialzing it again.
	 */
	public static void removeBoundingBoxRenderList(BoundingBox box)
	{
		renderList.remove(renderList.indexOf(box));
	}

	/*
	 * Draws the fancy player health
	 */
	public void drawHearts(GameContainer container, StateBasedGame game, Graphics g) throws SlickException
	{
		int health = player.getHealth();
		switch(health)
		{
		case 0:
			g.drawImage(emptyHeart, 20, 30);
			g.drawImage(emptyHeart, 40, 30);
			g.drawImage(emptyHeart, 60, 30);
			g.drawImage(emptyHeart, 80, 30);
			g.drawImage(emptyHeart, 100, 30);
			break;
		case 1:
			g.drawImage(fullHeart,  20, 30);
			g.drawImage(emptyHeart, 40, 30);
			g.drawImage(emptyHeart, 60, 30);
			g.drawImage(emptyHeart, 80, 30);
			g.drawImage(emptyHeart, 100, 30);
			break;
		case 2:
			g.drawImage(fullHeart, 20, 30);
			g.drawImage(fullHeart, 40, 30);
			g.drawImage(emptyHeart, 60, 30);
			g.drawImage(emptyHeart, 80, 30);
			g.drawImage(emptyHeart, 100, 30);
			break;
		case 3:
			g.drawImage(fullHeart, 20, 30);
			g.drawImage(fullHeart, 40, 30);
			g.drawImage(fullHeart, 60, 30);
			g.drawImage(emptyHeart, 80, 30);
			g.drawImage(emptyHeart, 100, 30);
			break;
		case 4:
			g.drawImage(fullHeart, 20, 30);
			g.drawImage(fullHeart, 40, 30);
			g.drawImage(fullHeart, 60, 30);
			g.drawImage(fullHeart, 80, 30);
			g.drawImage(emptyHeart, 100, 30);
			break;
		case 5:
			g.drawImage(fullHeart, 20, 30);
			g.drawImage(fullHeart, 40, 30);
			g.drawImage(fullHeart, 60, 30);
			g.drawImage(fullHeart, 80, 30);
			g.drawImage(fullHeart, 100, 30);
			break;
		}
	}

	public void drawMana(GameContainer container, StateBasedGame game, Graphics g) throws SlickException
	{
		int mana = player.getMana();
		switch(mana)
		{
		case 0:
			g.drawImage(emptyManaCrystal, 20, 50);
			g.drawImage(emptyManaCrystal, 55, 50);
			g.drawImage(emptyManaCrystal, 90, 50);
			g.drawImage(emptyManaCrystal, 125, 50);
			g.drawImage(emptyManaCrystal, 160, 50);
			break;
		case 1:
			g.drawImage(halfManaCrystal,  20, 50);
			g.drawImage(emptyManaCrystal, 55, 50);
			g.drawImage(emptyManaCrystal, 90, 50);
			g.drawImage(emptyManaCrystal, 125, 50);
			g.drawImage(emptyManaCrystal, 160, 50);
			break;
		case 2:
			g.drawImage(fullManaCrystal, 20, 50);
			g.drawImage(emptyManaCrystal, 55, 50);
			g.drawImage(emptyManaCrystal, 90, 50);
			g.drawImage(emptyManaCrystal, 125, 50);
			g.drawImage(emptyManaCrystal, 160, 50);
			break;
		case 3:
			g.drawImage(fullManaCrystal, 20, 50);
			g.drawImage(halfManaCrystal, 55, 50);
			g.drawImage(emptyManaCrystal, 90, 50);
			g.drawImage(emptyManaCrystal, 125, 50);
			g.drawImage(emptyManaCrystal, 160, 50);
			break;
		case 4:
			g.drawImage(fullManaCrystal, 20, 50);
			g.drawImage(fullManaCrystal, 55, 50);
			g.drawImage(emptyManaCrystal, 90, 50);
			g.drawImage(emptyManaCrystal, 125, 50);
			g.drawImage(emptyManaCrystal, 160, 50);
			break;
		case 5:
			g.drawImage(fullManaCrystal, 20, 50);
			g.drawImage(fullManaCrystal, 55, 50);
			g.drawImage(halfManaCrystal, 90, 50);
			g.drawImage(emptyManaCrystal, 125, 50);
			g.drawImage(emptyManaCrystal, 160, 50);
			break;
		case 6:
			g.drawImage(fullManaCrystal, 20, 50);
			g.drawImage(fullManaCrystal, 55, 50);
			g.drawImage(fullManaCrystal, 90, 50);
			g.drawImage(emptyManaCrystal, 125, 50);
			g.drawImage(emptyManaCrystal, 160, 50);
			break;
		case 7:
			g.drawImage(fullManaCrystal, 20, 50);
			g.drawImage(fullManaCrystal, 55, 50);
			g.drawImage(fullManaCrystal, 90, 50);
			g.drawImage(halfManaCrystal, 125, 50);
			g.drawImage(emptyManaCrystal, 160, 50);
			break;
		case 8:
			g.drawImage(fullManaCrystal, 20, 50);
			g.drawImage(fullManaCrystal, 55, 50);
			g.drawImage(fullManaCrystal, 90, 50);
			g.drawImage(fullManaCrystal, 125, 50);
			g.drawImage(emptyManaCrystal, 160, 50);
			break;
		case 9:
			g.drawImage(fullManaCrystal, 20, 50);
			g.drawImage(fullManaCrystal, 55, 50);
			g.drawImage(fullManaCrystal, 90, 50);
			g.drawImage(fullManaCrystal, 125, 50);
			g.drawImage(halfManaCrystal, 160, 50);
			break;
		case 10:
			g.drawImage(fullManaCrystal, 20, 50);
			g.drawImage(fullManaCrystal, 55, 50);
			g.drawImage(fullManaCrystal, 90, 50);
			g.drawImage(fullManaCrystal, 125, 50);
			g.drawImage(fullManaCrystal, 160, 50);
			break;
		}
	}

	/*
	 * Draw the fancy player scores
	 */
	public void drawScore(GameContainer container, StateBasedGame game, Graphics g) throws SlickException
	{
		String score =  String.valueOf(player.getScore());
		float Y = 85;
		float X = 20;

		for(int i = 0; i < score.length(); i++)
		{
			int letter = score.charAt(i);
			switch(letter)
			{
			case 0 + 48:
				g.drawImage(numberZero, X, Y);
			break;
			case 1 + 48:
				g.drawImage(numberOne, X, Y);
			break;
			case 2 + 48:
				g.drawImage(numberTwo, X, Y);
			break;
			case 3 + 48:
				g.drawImage(numberThree, X, Y);
			break;
			case 4 + 48:
				g.drawImage(numberFour, X, Y);
			break;
			case 5 + 48:
				g.drawImage(numberFive, X, Y);
			break;
			case 6 + 48:
				g.drawImage(numberSix, X, Y);
			break;
			case 7 + 48:
				g.drawImage(numberSeven, X, Y);
			break;
			case 8 + 48:
				g.drawImage(numberEight, X, Y);
			break;
			case 9 + 48:
				g.drawImage(numberNine, X, Y);
			break;
			}
			X += 32;
		}
	}

	public void drawExp(GameContainer container, StateBasedGame game, Graphics g) throws SlickException
	{
		// TODO 
	}

	/**
	 *  Direction: RIGHT = 1, LEFT = 0
	 */
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException 
			{
		// Call the cooldownManager for it's milisecondly dose of delta
		cooldownManager.update(delta);

		for(int i = 0; i < renderList.size(); i++)
		{
			try
			{
				renderList.get(i).update(container, game, delta);
			}catch(NullPointerException e)
			{
				e.printStackTrace();
			}
		}

		// Clear quad tree & refill it
		quadTree.clear();
		for(int i = 0; i < renderList.size(); i++)
		{
			quadTree.insert(renderList.get(i));
		}

		for (int i = 0; i < renderList.size(); i++) 
		{
			returnObjects.clear();
			quadTree.retrieve(returnObjects, renderList.get(i));

			for (int x = 0; x < returnObjects.size(); x++) 
			{
				if(renderList.get(i).intersects(returnObjects.get(x)) && !renderList.get(i).equals(returnObjects.get(x)))
				{ 
					returnObjects.get(x).collision(renderList.get(i));
				}
			}
		}
			}

	/*
	 * Gets the closest X block
	 *  Rounds down.
	 */
	public static boolean isBlocked(float x, float y)
	{
		int xBlock = (int) Math.floor((double) x / SIZE);
		int yBlock = (int) Math.floor((double) y / SIZE);
		try
		{
			return blocked[xBlock][yBlock];
		}catch(ArrayIndexOutOfBoundsException e)
		{
			return true;
		}
	}

	@Override
	public int getID() {
		return 1;
	}
}