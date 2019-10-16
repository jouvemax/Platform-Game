package platform.game;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import platform.game.character.Player;
import platform.game.graphic.BackgroundImage;
import platform.game.level.Builder;
import platform.game.level.Level;
import platform.game.level.Loading;
import platform.game.level.Menu;
import platform.game.signal.Not;
import platform.game.signal.Signal;
import platform.util.Box;
import platform.util.Input;
import platform.util.Loader;
import platform.util.Output;
import platform.util.SortedCollection;
import platform.util.Vector;
import platform.util.View;

/**
 * Basic implementation of world, managing a complete
 * collection of actors.
 */
public class Simulator implements World {

	private Loader loader;


	/**
	 * Indicates the standard gravity.
	 */
	private final Vector standardGravity;


	/**
	 * Indicates the current gravity.
	 */
	private       Vector gravity;


	/**
	 * Current center of the View.
	 */
	private       Vector currentCenter;


	/**
	 * Current radius of the View.
	 */
	private       double currentRadius;


	/**
	 * Expected center of the View.
	 */
	private       Vector expectedCenter;


	/**
	 * Expected radius of the View.
	 */
	private       double expectedRadius;


	/**
	 * List containing all Actors waiting to be registered
	 */
	private ArrayList<Actor>        registered;


	/**
	 * List containing all current Actors.
	 */
	private SortedCollection<Actor> actors;


	/**
	 * List containing all Actors waiting to be unregistered.
	 */
	private ArrayList<Actor>        unregistered;


	/**
	 * Indicates the current Level.
	 */
	private Level   current;


	/**
	 * Indicates the next Level.
	 */
	private Level   next;


	/**
	 * Indicates whether the transition to the next Level has begun
	 */
	private boolean transition;


	/**
	 * True if an Actor Player is present in the World
	 */
	private boolean playerPresent;


	/**
	 * Linkable with which we'd like to link a Signal
	 * @see Linkable
	 * @see platform.game.button.Spawn
	 */
	private Linkable toLink;


	/**
	 * Used to indicate the desire of an Actor to save the World
	 */
	private boolean toSave;

	/**
	 * Image bank of backgrounds, to change with the shortcut I
	 */
	ArrayList<String> backgroundImages;



	/**
	 * Create a new simulator.
	 * @param loader associated loader, not null
	 * @param args   level arguments, not null
	 */
	public Simulator(Loader loader, String[] args) {
		if (loader == null) {
			throw new NullPointerException();
		}

		this.loader          = loader;

		// The starting gravity is downwards pointing
		this.standardGravity = new Vector(0.0, -9.81);
		this.gravity         = standardGravity;

		// Default starting position for the View
		this.currentCenter   = defaultCenter;
		this.currentRadius   = defaultRadius ;
		this.expectedCenter  = currentCenter;
		this.expectedRadius  = currentRadius;

		// Creating the arrays containing all Actors
		this.registered      = new ArrayList<Actor>();
		this.actors          = new SortedCollection<Actor>();
		this.unregistered    = new ArrayList<Actor>();

		// The World has to load a Level to begin!
		this.current         = null;
		this.next            = null;
		this.transition      = true;

		// By default, the player is not present in the world
		this.playerPresent   = false;


		this.toLink          = null;

		// By default, the World doesn't need to be saved... (take that Super Heroes movies)
		this.toSave          = false;

		backgroundImages = new ArrayList<String>();
		backgroundImages.add("blue.land");
		backgroundImages.add("pixel.light.blue");
		backgroundImages.add("pixel.black");
	}


	/**
	 * Simulate a single step of the simulation.
	 * @param input input object to use, not null
	 * @param output output object to use, not null
	 */
	public void update(Input input, Output output) {

		/***** LEVEL TRANSITION *****/
		// If transition is set to true, either by an
		// Actor or the constructor, begin the transition
		// to a new level.
		if (transition) {
			// If no new level is set, create the
			// default level set by the class Level
			if (next == null)
				next = Level.createDefaultLevel();

			current = next;

			// Reset next back to null after saving
			// the level in a new variable
			Level level = next;
			transition = false;
			next = null;

			// Clear all Actors contained in the World
			actors.clear();
			registered.clear();
			unregistered.clear();

			// After deleting all Actors, register level
			register(level);

			/***** ACTOR MAINTENANCE *****/
			maintainActors();
		}


		/***** SETTING THE VIEW *****/
		// Smooth transition of the view
		double factor = 0.1 ;
		currentCenter = currentCenter.mul(1.0 -
				factor).add(expectedCenter.mul(factor));
		currentRadius = currentRadius * (1.0 - factor) +
				expectedRadius * factor;

		// Sets the view
		View view = new View(input, output);
		view.setTarget(currentCenter, currentRadius);


		/***** ALL ACTOR UPDATES *****/

		playerPresent = false;
		//**** PREUPDATE
		for (Actor a : actors) {
			if(!playerPresent && a instanceof Player)
				playerPresent = true;
			a.preUpdate(view);
		}

		//**** INTERACTION
		for (Actor actor : actors)
			for (Actor other : actors)
				if (actor.getPriority() > other.getPriority())
					actor.interact(other);

		//**** UPDATE
		for (Actor a : actors)
			a.update(view);

		//**** DRAWING
		for (Actor a : actors.descending())
			a.draw(view, view);

		//**** POSTUPDATE
		for (Actor a : actors)
			a.postUpdate(view);


		// If an Actor asked to be linked to another Signal...
		if(toLink != null) {
			// Loops on all actors to find the corresponding signal:
			for(Actor a: actors)
				if(view.getMouseButton(1).isPressed()   // the user has to left-click...
						&& a instanceof Signal			// ...on a signal...
						&& a.getBox() != null			// ..that has a non null Box...
						&& a.getBox().isColliding(view.getMouseLocation())  // ...colliding with the mous...
						&& toLink != a					// ...and is not itself.
						) {
					// Then, add the signal to the Linkable
					Signal signal = (Signal)a;
					if(signal.isActive())
						toLink.addSignal(new Not(signal));	
					else
						toLink.addSignal(signal);	
				}

			// If the user right-clicks anywhere, then end the linking process.
			if(input.getMouseButton(3).isPressed()) {
				((Linkable)toLink).endSignal();
				toLink = null;
			}
		}

		/***** USER INTERACTION WITH THE WORLD *****/
		// Return to Menu with ESC iff not in Menu itself
		// This is added so the user can not call a new Menu
		// in Menu itself
		if(input.getKeyboardButton(KeyEvent.VK_ESCAPE).isPressed()) {
			setNextLevel(new Menu());
			nextLevel();
		}

		// This part is only used if :
		// 1. The level Builder is setting the World
		// 2. The player is not present and therefore not playable
		// It is used so the user can move in the Builder.
		if(!playerPresent
				&& current instanceof Builder) {

			//**** GOING RIGHT, by small steps
			if(input.getKeyboardButton(KeyEvent.VK_RIGHT).isDown())

				expectedCenter =  expectedCenter.add(new Vector(0.01, 0.0));

			//**** GOING UP, by small steps
			if(input.getKeyboardButton(KeyEvent.VK_UP).isDown())
				expectedCenter =  expectedCenter.add(new Vector(0.0, 0.01));

			//**** GOING LEFT, by small steps
			if(input.getKeyboardButton(KeyEvent.VK_LEFT).isDown())
				expectedCenter =  expectedCenter.add(new Vector(-0.01, 0.0));

			//**** GOING DOWN, by small steps
			if(input.getKeyboardButton(KeyEvent.VK_DOWN).isDown())
				expectedCenter =  expectedCenter.add(new Vector(0.0, -0.01));

			//**** DEZOOMING, to a certain extent (25.0)
			if(input.getKeyboardButton(KeyEvent.VK_N).isDown()
					&& expectedRadius < 25.0)
				expectedRadius += 0.01;

			//**** ZOOMING, to a certain extent (2.0)
			if(input.getKeyboardButton(KeyEvent.VK_M).isDown()
					&& expectedRadius > 2.0)
				expectedRadius -= 0.01;

			//**** RESETTING the center to the default center
			if(input.getKeyboardButton(KeyEvent.VK_V).isDown())
				expectedCenter = defaultCenter;

			//**** RESETTING the radius to the default radius
			if(input.getKeyboardButton(KeyEvent.VK_B).isDown())
				expectedRadius = defaultRadius;
		}

		// If the world needs to be saved, then call the right people...
		if(toSave) {
			toSave = false;
			save(false);
		}

		// Dynamically saves the world
		if(input.getKeyboardButton(KeyEvent.VK_X).isPressed()
				&& playerPresent)
			save(true);
		
		// Dynamically loads the world
		if(input.getKeyboardButton(KeyEvent.VK_Y).isPressed())
			load(0);

		// Respawns the building mode to begin remodifying an existing level
		if(input.getKeyboardButton(KeyEvent.VK_C).isPressed()
				&& playerPresent
				&& (current instanceof Builder
						|| current instanceof Loading)) {
			Actor player = null;
			for(Actor a: actors)
				if(a instanceof Player)
					player = a;
			unregister(player);
			Level builder = new Builder();
			register(builder);
			current = builder;
		}

		// Changes the background
		if(input.getKeyboardButton(KeyEvent.VK_I).isPressed()
				&& !playerPresent
				&& current instanceof Builder) {
			String background = null;

			for(Actor a: actors)
				if(a instanceof BackgroundImage) {
					background = a.getSpriteName();
					unregister(a);
				}

			int index = 0;

			if(backgroundImages.contains(background))
				index = backgroundImages.indexOf(background);

			register(new BackgroundImage(backgroundImages.get((index+1)%backgroundImages.size())));
		}

		/***** ACTOR MAINTENANCE *****/
		maintainActors();

	}


	/**
	 * Is used to remove and add Actors which are situated
	 * in the queue.
	 */
	private void maintainActors() {
		// Remove unregistered actors
		for (int i = 0; i < unregistered.size(); ++i) {
			Actor actor = unregistered.get(i);
			if(actor != null)
				actor.unregister();
			actors.remove(actor);
		}
		unregistered.clear();

		// Add registered actors
		for (int i = 0; i < registered.size(); ++i) {
			Actor actor = registered.get(i);
			if (!actors.contains(actor)) {
				actor.register(this);
				actors.add(actor);
			}
		}
		registered.clear();
	}


	@Override
	public Loader getLoader() {
		return loader;
	}


	@Override
	public void setView(Vector center, double radius) {
		if (center == null)
			throw new NullPointerException();
		if (radius <= 0.0)
			throw new IllegalArgumentException("radius must be positive");
		expectedCenter = center;
		expectedRadius = radius;
	}


	@Override
	public Vector getCenter() {
		return currentCenter;
	}


	@Override
	public double getRadius() {
		return currentRadius;
	}


	@Override
	public void register(Actor actor) {
		registered.add(actor);
	}


	@Override
	public void unregister(Actor actor) {
		unregistered.add(actor);
	}


	@Override
	public void nextLevel() {
		transition = true;
	}


	@Override
	public void setNextLevel(Level level) {
		next = level;		
	}


	@Override
	public int hurt(Box area, Actor instigator, Damage type, double amount, Vector location) {
		int victims = 0;
		for (Actor actor : actors)
			if (area.isColliding(actor.getBox()))
				if (actor.hurt(instigator, type, amount, location))
					++victims;
		return victims;
	}


	@Override
	public Vector getGravity() {
		return gravity;
	}	


	@Override
	public double getGravityAngle() {
		return -standardGravity.getAngle() + // needs to be subtracted because the
				// standard gravity has not an angle of 0,
				// which is highly desired by other classes.
				((gravity.getAngle() >= -Math.PI && gravity.getAngle() < -Math.PI/2)
						?gravity.getAngle()+2*Math.PI:gravity.getAngle());
		// need to exist because we desire a
		// value between 0 and 2*Pi.
		// Is there to correct the fact that
		// negative values can be returned.
	}


	@Override
	public void setGravity(double angle) {
		gravity = standardGravity.rotated(angle);
	}


	@Override
	public void link(Linkable linkable) {
		if(toLink == null)
			toLink = linkable;		
	}


	@Override
	public void saveAll() {
		toSave = true;
	}


	/**
	 * Uses the fact that the classes are Serializable to save all Actors in a
	 * new folder located in "save/".
	 * <p>
	 * Dynamically creates a new folder. Its numbering follows
	 * the others (increasing order).
	 * Part of the code was inspired by the one described on the mentioned website.
	 * @see <a href="https://web.archive.org/web/20161114083612/http://www.tutorialspoint.com/java/java_serialization.htm">Source website</a>
	 */
	public void save(boolean dynamicSave) {
		try {
			// Remove the Overlay, necessary for the dynamic save
			Actor overlay = null;
			for(Actor a: actors)
				if(a instanceof Overlay)
					overlay = a;
			unregister(overlay);


			// Add and remove all Actors
			// that need to be wiped out.
			// Reason of that is related to the code
			// situated in Spawn
			maintainActors();

			// Finds the next available folder
			// format : "worldxxx", where "xxx" is a number
			// between 001 and 999
			// The dynamic save saves the World in world000
			int folder = 0;
			String path = pathWorld + String.format("%03d", folder)+"/";

			if(dynamicSave){
				if((new File(path)).exists())
					World.delete(new File(path));
			} else {
				do{
					++folder;
					if(folder >= 1000)
						throw new FileNotFoundException("WARNING: too much saves, please delete some before saving another one");
					path = pathWorld + String.format("%03d", folder)+"/";
				}while(new File(path).exists());
			}

			// If the creation of the new file was not a success,
			// throw a new exception.
			if(!(new File(path)).mkdirs())
				throw new IOException();

			// Unregisters all present Actors. The reason to that is
			// so that Simulator and all used variables in the class
			// do not have to be Serializable
			for(Actor a : actors) {
				if(a instanceof Overlay)
					unregister(a);
				else
					a.unregister();
			}

			// Needed to make sure the Overlay is gone				
			maintainActors();

			// The files in the folder represent each Actor of this World.
			// So we begin by calling it "actor001", then "actor002".
			// That's why we set the variable numberFile to 1.
			int numberFile = 1;

			for(Actor a : actors) {
				// Code inspired by the mentioned website
				String pathToFile = path + "actor" + String.format("%03d", numberFile) + ".ser";
				FileOutputStream fileOut = new FileOutputStream(pathToFile);
				ObjectOutputStream out = new ObjectOutputStream(fileOut);
				out.writeObject(a);
				out.close();
				fileOut.close();
				System.out.printf("Serialized data is saved in "+pathToFile+"\n");
				++numberFile;
			}

			// We re-register actors so we can continue playing the level.
			for(Actor a : actors) 
				a.register(this);

		}
		catch(IOException i) {
			i.printStackTrace();
			return;
		}
	}



	@Override
	public void load(int world) {

		int signifNumbers = 3;

		// A negative number indicates a default level (=created by the developers)
		// to be loaded. Used in LevelSelection
		if(world < 0) {
			signifNumbers = 2;
			world = -world;
		}

		try {			
			// We remove all present Actors so we can
			// start playing anew
			if(actors.size() != 0)
				for(Actor a: actors)
					unregister(a);

			maintainActors();

			// Make sure the overlay disappears
			actors.clear();

			String path = pathWorld + String.format("%0"+signifNumbers+"d", world)+"/";

			// If the folder does not exist,
			// throw an exception.
			if(!(new File(path)).exists())
				throw new IOException();

			// We iterate on all files present in the folder
			int numberFiles = new File(path).list().length;
			for(int i = 1; i <= numberFiles; ++i) {

				// Code inspired by the mentioned website			
				FileInputStream fileIn = new FileInputStream(path + "actor" + String.format("%03d", i)+".ser");
				ObjectInputStream in = new ObjectInputStream(fileIn);
				Actor actor = (Actor) in.readObject();
				if(!(actor instanceof Overlay))
					registered.add(actor);
				in.close();
				fileIn.close();
			}
		}
		catch(IOException i) {
			i.printStackTrace();
			return;
		}
		catch(ClassNotFoundException c) {
			System.out.println("Actor class not found\n");
			c.printStackTrace();
			return;
		}
	}

}
