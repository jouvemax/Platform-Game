package platform.game;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import platform.util.Box;
import platform.util.Input;
import platform.util.Output;
import platform.util.Sprite;
import platform.util.Vector;

/**
 * Base class of all simulated actors, attached to a world.
 */
public abstract class Actor implements Comparable<Actor>, Serializable {

	/**
	 * Present for all Serializable classes (also all subclasses of Actor).
	 * This ID is used to compare serialised data to existing classes.
	 */
	private static final long serialVersionUID = -4277942263040992706L;

	
	/**
	 * All spawned actors have a direct link with their World
	 */
	private World world;

	
	/**
	 * All Actors are characterised by a Box.
	 * The box can also be null (invisible Actors).
	 */
	private Box box;


	/**
	 * The name of the Actor's Sprite.
	 * The name of the Sprite can be null (invisible Actors)
	 */
	private String spriteName;

	

	/**
	 * Base constructor of an Actor.
	 * @param box         Indicates the position and the size of an Actor
	 * @param spriteName  Indicates which file should be used to draw it
	 */
	public Actor(Box box, String spriteName) {
		this.box        = box;
		this.spriteName = spriteName;
	}


	/**
	 * Secondary constructor of an Actor.
	 * @param box  Indicates the position and the size of an Actor
	 */
	public Actor(Box box) {
		this(box, null);
	}


	/**
	 * Secondary constructor of an Actor.
	 */
	public Actor() {
		this(null);
	}

	

	/**
	 * Returns the Actor's World.
	 * @return the World linked to the Actor
	 */
	protected World getWorld() {
		return world;
	}


	/**
	 * Registers this actor by setting its World to the World entered.
	 * @param world      Instance of world which will be linked to the Actor.
	 * @see   Simulator
	 */
	public void register(World world) {
		this.world = world;
	}


	/**
	 * Unregisters this actor by setting its World to null.
	 */
	public void unregister() {
		world = null;
	}



	/***** PRIMARY METHODS CALLED IN SIMULATOR *****/
	/*****    they all do nothing by default   *****/

	/**
	 * Used to update the Actor before it interacts with the World.
	 * @param input  Can be used to analyse input.
	 * @see Simulator
	 * @see World
	 */
	public void preUpdate(Input input) {}


	/**
	 * Used to simulate the interactions between all Actors.
	 * @param other  other Actor with which this Actor has to interact.
	 * @see Simulator
	 * @see World
	 */
	public void interact(Actor other) {}


	/**
	 * Used to update the Actor.
	 * @param input  Can be used to analyse input.
	 * @see Simulator
	 * @see World
	 */
	public void update(Input input) {}


	/**
	 * Used to draw the Actor onto the World.
	 * @param input   Can be used to analyse input.
	 * @param output  Can be used to output, e.g., the Sprite of the Actor
	 * @see Simulator
	 * @see World
	 */
	public void draw(Input input, Output output) {}


	/**
	 * Used to update the Actor after it has been drawn onto the World.
	 * @param input  Can be used to analyse input.
	 * @see Simulator
	 * @see World
	 */
	public void postUpdate(Input input) {}



	/***** ACTOR SPECIFICITIES *****/
	/**
	 * Classification criteria for all Actors.
	 * Has to be defined by each and everyone of them.
	 * This method makes this class abstract, so non instantiable.
	 * @return an integer used to classify all Actors by importance.
	 */
	abstract protected int getPriority();


	/**
	 * Determines the solidity-status of the Actor.
	 * @return  whether the Actor is solid or not (used in interactions), default false
	 */
	public boolean isSolid() {
		return false;
	}


	/**
	 * Used in interactions between Actors
	 * @param instigator  Actor responsible for the attack
	 * @param type		  type of the attack
	 * @param amount	  amount of damage dealt by the attack
	 * @param location	  location of the instigator of the attack
	 * @return            whether the Actor has been hurt by an attack, default false
	 */
	public boolean hurt(Actor instigator, Damage type, double amount, Vector location) {
		return false;
	}


	/**
	 * Returns the Actor's box.
	 * @return  the Actor's box.
	 */
	public Box getBox() { 
		if(box == null)
			return null;
		return box;
	}


	/**
	 * Sets the box of the Actor.
	 * @param box  new Box of the Actor.
	 */
	public void setBox(Box box) {
		if(box == null)
			throw new NullPointerException();

		this.box = box;
	}


	/**
	 * Returns the name of the Sprite.
	 * @return  the name of the Sprite
	 */
	public String getSpriteName() { 
		return spriteName;
	}


	/**
	 * Sets a new name for the Sprite
	 * @param spriteName  new name
	 */
	public void setSpriteName(String spriteName) {
		this.spriteName = spriteName;
	}


	/**
	 * Returns the position of the Actor.
	 * @return  the position of the Actor, which is defined by the center of its Box
	 */
	public Vector getPosition() {
		if (box == null)
			return null;
		return box.getCenter();
	}


	/**
	 * Returns the Sprite corresponding to...
	 * @param name  the name of the Sprite
	 * @return  Sprite corresponding to name
	 */
	protected Sprite getSprite(String name) {
		return world.getLoader().getSprite(name);
	}


	/**
	 * Returns the Actor's Sprite.
	 * @return  Sprite corresponding to the name of the Sprite already stored in Actor.
	 */
	protected Sprite getSprite() {
		return world.getLoader().getSprite(spriteName);
	}


	/**
	 * Returns a "copy ready" instance of Actor
	 * @return  a new instance of the same Actor. Used in button.Spawn. Null by default.
	 * @see platform.game.button.Spawn
	 */
	public Actor copie() {
		System.out.println("/!\\ method not defined for this Actor");
		return null;
	}


	@Override
	public int compareTo(Actor other) {
		if(this.getPriority() > other.getPriority())
			return -1;
		if(this.getPriority() == other.getPriority())
			return 0;
		return 1;
	}
}