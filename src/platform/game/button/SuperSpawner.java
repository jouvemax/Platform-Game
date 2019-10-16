package platform.game.button;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import platform.game.Actor;
import platform.game.World;
import platform.game.block.Block;
import platform.game.block.WoodenBlock;
import platform.game.item.Heart;
import platform.game.level.Menu;
import platform.game.linkable.Door;
import platform.game.linkable.Exit;
import platform.game.passive.Jumper;
import platform.game.passive.Spike;
import platform.game.signal.Constant;
import platform.game.signal.Key;
import platform.game.signal.Lever;
import platform.game.signal.Signal;
import platform.game.signal.Torch;
import platform.game.weapon.ArrowDispenser;
import platform.util.Box;
import platform.util.Input;
import platform.util.Vector;

/**
 *  SuperSpawner is a subclass of Button containing several Spawns.
 *  Used to design a menu in level.builder.
 *  <p>
 *  Some of the variables are marked as private final static because they are class-specific
 *  and used in static methods.
 */

public class SuperSpawner extends Button {

	private static final long serialVersionUID = 6528204166902097786L;

	
	/**
	 * Spawns contained in this SuperSpawner
	 */
	private ArrayList<Spawn> spawners;

	
	/**
	 * Indicates if the SuperSpawner is extended or not
	 */
	private boolean spawned;

	
	/**
	 * Used to maintain the spawn in focus in the level, even when moving
	 */
	private final Box boxOrigin;


	
	/**
	 * Full constructor. Private because it is only used in static methods
	 * contained in this very class. It also holds for other constructors.
	 * @param box        Position and size parameters of the SuperSpawner.
	 * @param spriteOff  Name of the Sprite when the mouse is off the SuperSpawner.
	 * @param spriteOn   Name of the Sprite when the mouse is on the SuperSpawner.
	 * @param spawners   Spawns linked to the SuperSpawner.
	 * @param shortcut  Shortcut of the SuperSpawner.
	 * @see java.awt.event.KeyEvent
	 */
	private SuperSpawner(Box box, String spriteOff, String spriteOn, ArrayList<Spawn> spawners, int shortcut) {
		super(box, spriteOff, spriteOn, shortcut);
		this.spawners     = spawners;
		this.spawned      = false;
		this.boxOrigin    = box;
	}

	
	/**
	 * Constructor without any shortcut
	 * @param box        Position and size parameters of the SuperSpawner.
	 * @param spriteOff  Name of the Sprite when the mouse is off the SuperSpawner.
	 * @param spriteOn   Name of the Sprite when the mouse is on the SuperSpawner.
	 * @param spawners   Spawns linked to the SuperSpawner.
	 */
	private SuperSpawner(Box box, String spriteOff, String spriteOn, ArrayList<Spawn> spawners) {
		this(box, spriteOff, spriteOn, spawners, KeyEvent.VK_UNDEFINED);
	}

	
	/**
	 * Constructor without a second Sprite
	 * @param box       Position and size parameters of the SuperSpawner.
	 * @param sprite    Shown as the menu image
	 * @param spawners  Spawns linked to the SuperSpawner.
	 * @param shortcut  Shortcut of the SuperSpawner.
	 * @see java.awt.event.KeyEvent
	 */
	private SuperSpawner(Box box, String sprite, ArrayList<Spawn> spawners, int shortcut) {
		this(box, sprite, sprite, spawners, shortcut);
	}

	
	/**
	 * Constructor without any shortcut nor a second Sprite
	 * @param box       Position and size parameters of the SuperSpawner.
	 * @param sprite    Shown as the menu image
	 * @param spawners  Spawns linked to the SuperSpawner.
	 */
	private SuperSpawner(Box box, String sprite, ArrayList<Spawn> spawners) {
		this(box, sprite, sprite, spawners);
	}



	@Override
	public void update(Input input) {
		super.update(input);

		// Used to follow the view of the user
		setBox(followView(boxOrigin));


		// If the user left-clicks or the shortcut is summoned AND the SuperSpawner is not yet spawned...
		if((input.getMouseButton(1).isPressed()
				&& getBox().isColliding(input.getMouseLocation())
				|| input.getKeyboardButton(getShortcut()).isPressed())
				&& !spawned) {
			// ... register all Spawns and mark it as spawned
			for(Spawn s: spawners)
				getWorld().register(s);
			
			spawned = true;
		}
		// If the user left-clicks or presses the shortcut AND the SupserSpawner is Spawned, then...
		else if((input.getMouseButton(1).isPressed()
				|| input.getKeyboardButton(getShortcut()).isPressed()) // FIXME why doesn't it work with KeyEvent.KEY_PRESSED?
				&& spawned) {
			// The following loop tests if the click was not caused by the user selecting a Spawn.
			// In this condition, the Spawn needs to remain registered to follow the user's movement.
			boolean condition = false;
			for(Spawn a: spawners)
				condition = condition || a.getBox().isColliding(input.getMouseLocation());
			
			// Otherwise, just delete them all.
			if(!condition) {
				for(Spawn s: spawners)
					getWorld().unregister(s);
				spawned = false;
			}
		}

	}
	
	
	
	/***** STATIC "CONSTRUCTORS" *****/
	
	
	// All following static variables were calculated with the ration of the image
	// so the appearance is optimal on screen.
	// They are marked as public so they can be reused in other level designers.
	
	/**
	 * Ratio of the image stone.5 and stone.6
	 */
	private final static double stoneFiveOrSixRatio    = 140.0/220.0;
	
	
	/**
	 * Ratio of the image stone.3 and stone.7
	 */
	private final static double stoneThreeOrSevenRatio = 70.0/220.0;
	
	
	/**
	 * Ratio of the image stone.2 and stone.8
	 */
	private final static double stoneTwoOrEightRatio   = 2.0;
	
	
	/**
	 * Standard length, set to 1.0.
	 */
	private final static double standardLength         = 1.0;
	
	
	/**
	 * Medium length, set to 0.5.
	 */
	private final static double mediumLength           = 0.5;
	
	
	/**
	 * Short length, set to 0.3.
	 */
	private final static double shortLength            = 0.3;
	
	
	/** 
	 * Standard distance, set to 1.2.
	 */
	private final static double standardDistance       = 1.2;
	
	/**
	 * Small distance, set to 0.7.
	 */
	private final static double smallDistance          = 0.7;
	
	
	
	// All following methods are use to created predesigned SuperSpawners (cf. level.Builder)

	
	/**
	 * Gives a SuperSpawner containing Spawns of horizontal Blocks.
	 * @param world     used to register new Actors
	 * @param box       Position and size parameters of the SuperSpawner.
	 * @param sprite    shown as the menu image
	 * @param shortcut  Shortcut of the SuperSpawner.
	 * @return  new SuperSpawner of horizontal Blocks
	 * @see java.awt.event.KeyEvent
	 */
	public static SuperSpawner HZ_BLOCKS(World world, Box box, String sprite, int shortcut) {
		ArrayList<Block> blocks = new ArrayList<Block>();
		ArrayList<Spawn> spawns = new ArrayList<Spawn>();
		
		double distance = -standardDistance;
		
		blocks.add(new Block(new Box(
				new Vector(box.getCenter().getX(),
						box.getCenter().getY()+distance)
				, standardLength, stoneFiveOrSixRatio)
				, "stone.5"));	

		distance -= standardLength;
		
		blocks.add(new Block(new Box(
				new Vector(box.getCenter().getX(),
						box.getCenter().getY()+distance)
				, standardLength, stoneFiveOrSixRatio)
				, "stone.broken.5"));
		
		distance -= standardLength;

		blocks.add(new Block(new Box(
				new Vector(box.getCenter().getX(),
						box.getCenter().getY()+distance)
				, standardLength, stoneThreeOrSevenRatio)
				, "stone.3"));
		
		distance -= standardLength;

		blocks.add(new Block(new Box(
				new Vector(box.getCenter().getX(),
						box.getCenter().getY()+distance)
				, standardLength, stoneThreeOrSevenRatio)
				, "stone.broken.3"));
		
		distance -= standardLength;

		blocks.add(new Block(new Box(
				new Vector(box.getCenter().getX(),
						box.getCenter().getY()+distance)
				, stoneTwoOrEightRatio*shortLength, shortLength)
				, "stone.2"));
		
		distance -= standardLength;

		blocks.add(new Block(new Box(
				new Vector(box.getCenter().getX(),
						box.getCenter().getY()+distance)
				, stoneTwoOrEightRatio*shortLength, shortLength)
				, "stone.broken.2"));

		for(int i = 0; i < blocks.size(); ++i)
			spawns.add(new Spawn(
					blocks.get(i).getBox()
					, blocks.get(i).getSpriteName()
					, blocks.get(i), World.keyboardCode(i+1)));

		return new SuperSpawner(box, sprite, spawns, shortcut);
	}

	
	/**
	 * Gives a SuperSpawner containing Spawns of horizontal Blocks without specifying a Sprite.
	 * The default Sprite used is "stone.2".
	 * @param world     used to register new Actors
	 * @param box       Position and size parameters of the SuperSpawner.
	 * @param shortcut  Shortcut of the SuperSpawner.
	 * @return  new SuperSpawner of horizontal Blocks
	 * @see java.awt.event.KeyEvent
	 */
	public static SuperSpawner HZ_BLOCKS(World world, Box box, int shortcut) {
		return HZ_BLOCKS(world, box, "stone.2", shortcut);
	}
	
	
	/**
	 * Gives a SuperSpawner containing Spawns of horizontal Blocks without specifying a Sprite
	 * nor a shortcut.
	 * The default Sprite used is "stone.2" and no shortcut is specified.
	 * @param world     used to register new Actors
	 * @param box       Position and size parameters of the SuperSpawner.
	 * @return  new SuperSpawner of horizontal Blocks
	 */
	public static SuperSpawner HZ_BLOCKS(World world, Box box) {
		return HZ_BLOCKS(world, box, KeyEvent.VK_UNDEFINED);
	}
	
	
	
	/**
	 * Gives a SuperSpawner containing Spawns of vertical Blocks.
	 * @param world     used to register new Actors
	 * @param box       Position and size parameters of the SuperSpawner.
	 * @param sprite    shown as the menu image
	 * @param shortcut  Shortcut of the SuperSpawner.
	 * @return  new SuperSpawner of vertical Blocks
	 * @see java.awt.event.KeyEvent
	 */
	public static SuperSpawner VT_BLOCKS(World world, Box box, String sprite, int shortcut) {
		ArrayList<Block> blocks = new ArrayList<Block>();
		ArrayList<Spawn> spawns = new ArrayList<Spawn>();

		double distance = standardDistance;
		
		blocks.add(new Block(new Box(
				new Vector(box.getCenter().getX()+distance,
						box.getCenter().getY()), stoneFiveOrSixRatio, standardLength)
				, "stone.6"));

		distance += standardLength;
		
		blocks.add(new Block(new Box(
				new Vector(box.getCenter().getX()+distance,
						box.getCenter().getY()), stoneFiveOrSixRatio, standardLength)
				, "stone.broken.6"));

		distance += standardLength;
		
		blocks.add(new Block(new Box(
				new Vector(box.getCenter().getX()+distance,
						box.getCenter().getY()), stoneThreeOrSevenRatio, standardLength)
				, "stone.7"));
		
		distance += standardLength;

		blocks.add(new Block(new Box(
				new Vector(box.getCenter().getX()+distance,
						box.getCenter().getY()), stoneThreeOrSevenRatio, standardLength)
				, "stone.broken.7"));
		
		distance += standardLength;

		blocks.add(new Block(new Box(
				new Vector(box.getCenter().getX()+distance,
						box.getCenter().getY()), shortLength, stoneTwoOrEightRatio*shortLength)
				, "stone.8"));
		
		distance += standardLength;

		blocks.add(new Block(new Box(
				new Vector(box.getCenter().getX()+distance,
						box.getCenter().getY()), shortLength, stoneTwoOrEightRatio*shortLength)
				, "stone.broken.8"));

		for(int i = 0; i < blocks.size(); ++i)
			spawns.add(new Spawn(
					blocks.get(i).getBox()
					, blocks.get(i).getSpriteName()
					, blocks.get(i), World.keyboardCode(i+1)));

		return new SuperSpawner(box, sprite, spawns, shortcut);
	}
	
	
	/**
	 * Gives a SuperSpawner containing Spawns of vertical Blocks without specifying a Sprite.
	 * The default Sprite used is "stone.8".
	 * @param world     used to register new Actors
	 * @param box       Position and size parameters of the SuperSpawner.
	 * @param shortcut  Shortcut of the SuperSpawner.
	 * @return  new SuperSpawner of vertical Blocks
	 * @see java.awt.event.KeyEvent
	 */
	public static SuperSpawner VT_BLOCKS(World world, Box box, int shortcut) {
		return VT_BLOCKS(world, box, "stone.8", shortcut);
	}

	
	/**
	 * Gives a SuperSpawner containing Spawns of vertical Blocks without specifying a Sprite
	 * nor a shortcut.
	 * The default Sprite used is "stone.8" and no shortcut is specified.
	 * @param world     used to register new Actors
	 * @param box       Position and size parameters of the SuperSpawner.
	 * @return  new SuperSpawner of vertical Blocks
	 */
	public static SuperSpawner VT_BLOCKS(World world, Box box) {
		return VT_BLOCKS(world, box, KeyEvent.VK_UNDEFINED);
	}
	
	

	/**
	 * Gives a SuperSpawner containing Spawns of square Blocks.
	 * @param world     used to register new Actors
	 * @param box       Position and size parameters of the SuperSpawner.
	 * @param sprite    shown as the menu image
	 * @param shortcut  Shortcut of the SuperSpawner.
	 * @return  new SuperSpawner of square Blocks
	 * @see java.awt.event.KeyEvent
	 */
	public static SuperSpawner SQ_BLOCKS(World world, Box box, String sprite, int shortcut) {
		ArrayList<Block> blocks = new ArrayList<Block>();
		ArrayList<Spawn> spawns = new ArrayList<Spawn>();

		double distance = standardDistance;

		blocks.add(new Block(new Box(
				new Vector(box.getCenter().getX()+distance,
						box.getCenter().getY()), standardLength, standardLength)
				, "stone.4"));
		
		distance += standardDistance;

		blocks.add(new Block(new Box(
				new Vector(box.getCenter().getX()+distance,
						box.getCenter().getY()), standardLength, standardLength)
				, "stone.broken.4"));

		distance += standardLength;
		
		blocks.add(new Block(new Box(
				new Vector(box.getCenter().getX()+distance,
						box.getCenter().getY()), shortLength, shortLength)
				, "stone.1"));
		
		distance += mediumLength;

		blocks.add(new Block(new Box(
				new Vector(box.getCenter().getX()+distance,
						box.getCenter().getY()), shortLength, shortLength)
				, "stone.broken.1"));
	
		distance += mediumLength;

		blocks.add(new WoodenBlock(new Box(
				new Vector(box.getCenter().getX()+distance,
						box.getCenter().getY()), shortLength, shortLength)
				, "wood.1"));

		for(int i = 0; i < blocks.size(); ++i)
			spawns.add(new Spawn(
					blocks.get(i).getBox()
					, blocks.get(i).getSpriteName()
					, blocks.get(i), World.keyboardCode(i+1)));

		return new SuperSpawner(box, sprite, spawns, shortcut);
	}

	
	/**
	 * Gives a SuperSpawner containing Spawns of square Blocks without specifying a Sprite.
	 * The default Sprite used is "stone.4".
	 * @param world     used to register new Actors
	 * @param box       Position and size parameters of the SuperSpawner.
	 * @param shortcut  Shortcut of the SuperSpawner.
	 * @return  new SuperSpawner of square Blocks
	 * @see java.awt.event.KeyEvent
	 */
	public static SuperSpawner SQ_BLOCKS(World world, Box box, int shortcut) {
		return SQ_BLOCKS(world, box, "stone.4", shortcut);
	}
	
	
	/**
	 * Gives a SuperSpawner containing Spawns of square Blocks without specifying a Sprite
	 * nor a shortcut.
	 * The default Sprite used is "stone.4" and no shortcut is specified.
	 * @param world     used to register new Actors
	 * @param box       Position and size parameters of the SuperSpawner.
	 * @return  new SuperSpawner of square Blocks
	 */
	public static SuperSpawner SQ_BLOCKS(World world, Box box) {
		return SQ_BLOCKS(world, box, KeyEvent.VK_UNDEFINED);
	}

	

	/**
	 * Gives a SuperSpawner containing Spawns of Signals.
	 * @param world     used to register new Actors
	 * @param box       Position and size parameters of the SuperSpawner.
	 * @param sprite    shown as the menu image
	 * @param shortcut  Shortcut of the SuperSpawner.
	 * @return  new SuperSpawner of Signals
	 * @see java.awt.event.KeyEvent
	 */
	public static SuperSpawner SIGNALS(World world, Box box, String sprite, int shortcut) {
		ArrayList<Actor> signals  = new ArrayList<Actor>();
		ArrayList<Spawn> spawns   = new ArrayList<Spawn>();
		ArrayList<String> sprites = new ArrayList<String>();

		sprites.add("key.yellow");
		sprites.add("key.green");
		sprites.add("key.red");
		sprites.add("key.blue");
		sprites.add("torch.lit.1");
		sprites.add("torch");
		sprites.add("lever.left");

		for(int i = 0; i < 4; ++i)
			signals.add(new Key(new Box(
					new Vector(box.getCenter().getX()+standardDistance+i*smallDistance,
							box.getCenter().getY()), mediumLength, mediumLength)
					, sprites.get(i)));
		for(int i = 4; i < 6; ++i)
			signals.add(new Torch(new Box(
					new Vector(box.getCenter().getX()+standardDistance+i*smallDistance,
							box.getCenter().getY()), mediumLength, mediumLength)
					, (i%2==0)?true:false));

		signals.add(new Lever(new Box(
				new Vector(box.getCenter().getX()+standardDistance+6*smallDistance,
						box.getCenter().getY()), mediumLength, mediumLength)));

		for(int i = 0; i < signals.size(); ++i)
			spawns.add(new Spawn(
					signals.get(i).getBox()
					, sprites.get(i), signals.get(i), World.keyboardCode(i+1)));

		return new SuperSpawner(box, sprite, spawns, shortcut);
	}
	

	/**
	 * Gives a SuperSpawner containing Spawns of Signals without specifying a Sprite.
	 * The default Sprite used is "lever.mid".
	 * @param world     used to register new Actors
	 * @param box       Position and size parameters of the SuperSpawner.
	 * @param shortcut  Shortcut of the SuperSpawner.
	 * @return  new SuperSpawner of Signals
	 * @see java.awt.event.KeyEvent
	 */
	public static SuperSpawner SIGNALS(World world, Box box, int shortcut) {
		return SIGNALS(world, box, "lever.mid", shortcut);
	}
	
	
	/**
	 * Gives a SuperSpawner containing Spawns of Signals without specifying a Sprite
	 * nor a shortcut.
	 * The default Sprite used is "lever.mid" and no shortcut is specified.
	 * @param world     used to register new Actors
	 * @param box       Position and size parameters of the SuperSpawner.
	 * @return  new SuperSpawner of Signals
	 */
	public static SuperSpawner SIGNALS(World world, Box box) {
		return SIGNALS(world, box, KeyEvent.VK_UNDEFINED);
	}



	/**
	 * Gives a SuperSpawner containing Spawns of Linkables.
	 * @param world     used to register new Actors
	 * @param box       Position and size parameters of the SuperSpawner.
	 * @param sprite    shown as the menu image
	 * @param shortcut  Shortcut of the SuperSpawner.
	 * @return  new SuperSpawner of Linkables
	 * @see java.awt.event.KeyEvent
	 */
	public static SuperSpawner UNLOCKABLE(World world, Box box, String sprite, int shortcut) {
		ArrayList<Actor> unlockable  = new ArrayList<Actor>();
		ArrayList<Spawn> spawns      = new ArrayList<Spawn>();
		ArrayList<String> sprites    = new ArrayList<String>();

		Signal constant = new Constant(false);

		sprites.add("lock.yellow");
		sprites.add("lock.green");
		sprites.add("lock.red");
		sprites.add("lock.blue");
		sprites.add("door.closed");

		for(int i = 0; i < 4; ++i)
			unlockable.add(new Door(new Box(
					new Vector(box.getCenter().getX()+standardDistance+i*smallDistance,
							box.getCenter().getY()), mediumLength, mediumLength)
					, sprites.get(i), constant));

		unlockable.add(new Exit(new Box(
				new Vector(box.getCenter().getX()+standardDistance+4*smallDistance,
						box.getCenter().getY()), mediumLength, mediumLength)
				, new Menu()
				, constant));

		for(int i = 0; i < unlockable.size(); ++i)
			spawns.add(new Spawn(
					unlockable.get(i).getBox()
					, sprites.get(i), unlockable.get(i), World.keyboardCode(i+1)));

		return new SuperSpawner(box, sprite, spawns, shortcut);
	}

	
	/**
	 * Gives a SuperSpawner containing Spawns of Linkables without specifying a Sprite.
	 * The default Sprite used is "lock.yellow".
	 * @param world     used to register new Actors
	 * @param box       Position and size parameters of the SuperSpawner.
	 * @param shortcut  Shortcut of the SuperSpawner.
	 * @return  new SuperSpawner of Linkables
	 * @see java.awt.event.KeyEvent
	 */
	public static SuperSpawner UNLOCKABLE(World world, Box box, int shortcut) {
		return UNLOCKABLE(world, box, "lock.yellow", shortcut);
	}
	
	
	/**
	 * Gives a SuperSpawner containing Spawns of Linkables without specifying a Sprite
	 * nor a shortcut.
	 * The default Sprite used is "lock.yellow" and no shortcut is specified.
	 * @param world     used to register new Actors
	 * @param box       Position and size parameters of the SuperSpawner.
	 * @return  new SuperSpawner of Linkables
	 */
	public static SuperSpawner UNLOCKABLE(World world, Box box) {
		return UNLOCKABLE(world, box, KeyEvent.VK_UNDEFINED);
	}



	/**
	 * Gives a SuperSpawner containing Spawns of Assets.
	 * @param world     used to register new Actors
	 * @param box       Position and size parameters of the SuperSpawner.
	 * @param sprite    shown as the menu image
	 * @param shortcut  Shortcut of the SuperSpawner.
	 * @return  new SuperSpawner of Assets
	 * @see java.awt.event.KeyEvent
	 */
	public static SuperSpawner ASSETS(World world, Box box, String sprite, int shortcut) {
		ArrayList<Actor> assets   = new ArrayList<Actor>();
		ArrayList<Spawn> spawns   = new ArrayList<Spawn>();
		ArrayList<String> sprites = new ArrayList<String>();

		sprites.add("heart.full");
		sprites.add("jumper.normal");
		sprites.add("spikes");
		sprites.add("arrow.disp");

		double distance = standardDistance;
		
		assets.add(new Heart(new Box(
				new Vector(box.getCenter().getX()+distance,
						box.getCenter().getY()), standardLength, standardLength)));
		
		distance += standardDistance;
		
		assets.add(new Jumper(new Box(
				new Vector(box.getCenter().getX()+distance,
						box.getCenter().getY()), standardLength, standardLength)));
		
		distance += standardDistance;
		
		assets.add(new Spike(new Box(
				new Vector(box.getCenter().getX()+distance,
						box.getCenter().getY()-0.25), standardLength, mediumLength)));
		
		distance += standardDistance;
		
		assets.add(new ArrowDispenser(new Box(
				new Vector(box.getCenter().getX()+distance,
						box.getCenter().getY()-0.25), standardLength, standardLength)));

		for(int i = 0; i < assets.size(); ++i)
			spawns.add(new Spawn(
					assets.get(i).getBox()
					, sprites.get(i), assets.get(i), World.keyboardCode(i+1)));

		return new SuperSpawner(box, sprite, spawns, shortcut);
	}

	
	/**
	 * Gives a SuperSpawner containing Spawns of Assets without specifying a Sprite.
	 * The default Sprite used is "jumper.extended".
	 * @param world     used to register new Actors
	 * @param box       Position and size parameters of the SuperSpawner.
	 * @param shortcut  Shortcut of the SuperSpawner.
	 * @return  new SuperSpawner of Assets
	 * @see java.awt.event.KeyEvent
	 */
	public static SuperSpawner ASSETS(World world, Box box, int shortcut) {
		return ASSETS(world, box, "jumper.extended", shortcut);
	}
	
	
	/**
	 * Gives a SuperSpawner containing Spawns of Assets without specifying a Sprite
	 * nor a shortcut.
	 * The default Sprite used is "jumper.extended" and no shortcut is specified.
	 * @param world     used to register new Actors
	 * @param box       Position and size parameters of the SuperSpawner.
	 * @return  new SuperSpawner of Assets
	 */
	public static SuperSpawner ASSETS(World world, Box box) {
		return ASSETS(world, box, KeyEvent.VK_UNDEFINED);
	}
}