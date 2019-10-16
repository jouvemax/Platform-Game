package platform.game;


import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import platform.game.character.Player;
import platform.game.graphic.SelectGravity;
import platform.game.level.Level;
import platform.util.Box;
import platform.util.Loader;
import platform.util.Vector;

/**
 * Represents an environment populated by actors.
 */
public interface World {
	
	
	/**
	 * Default center of the View.
	 */
	public static final Vector defaultCenter = Vector.ZERO;
	
	/**
	 * Default radius of the View.
	 */
	public static final double defaultRadius = 8.0;
	
	
	/**
	 * Path to the folder containing all save folders.
	 * Path to the files in the save folder, number and "/" to add.
	 */
	public static final String pathSave  = "save/";
	public static final String pathWorld = "save/world";
	
	
	/** @return associated loader, not null */
	public Loader getLoader();

	
	/**
	 * Set viewport location and size.
	 * @param center viewport center, not null
	 * @param radius viewport radius, positive
	 */
	public void setView(Vector center, double radius);
	
	
	/**
	 * Returns the expected center of the World.
	 * @return expected center of the World
	 * @see platform.game.button.Button
	 */
	public Vector getCenter();
	
	
	/**
	 * Returns the expected radius of the World.
	 * @return expected radius of the World
	 * @see platform.game.button.Button
	 */
	public double getRadius();

	
	/**
	 * Adds the {@code actor} to the registered arrays of the World.
	 * @param actor  an Actor
	 */
	public void register(Actor actor);
	
	/**
	 * Adds the {@code actor} to the unregistered arrays of the World.
	 * @param actor  an Actor
	 */
	public void unregister(Actor actor);

	
	/**
	 * Tells to the World to load the next level. Code is handled in update.
	 */
	public void nextLevel();
	
	
	/**
	 * Sets the level to be loaded if nextLevel() is called.
	 * @param level  a Level
	 * @see platform.game.Simulator
	 */
	public void setNextLevel(Level level);
	
	
	/**
	 * Sets a "hurting zone" in the World. Each actor entering this zone will
	 * be hurt by the Damage type.
	 * @param area        it is the Box delimiting the zone
	 * @param instigator  the Actor instigating the Damage
	 * @param type		  indicates which type of Damage is dealt
	 * @param amount	  the amount (double) of damage dealt by the "attack"
	 * @param location	  indicates where it comes from (Vector)
	 * @return the number (int) of "victims", aka Actors influenced by the attack
	 */
	public int hurt(Box area, Actor instigator, Damage type, double amount, Vector location);

	
	/**
	 * @return the gravity (Vector) in effect in the World.
	 */
	public Vector getGravity();
	
	
	/**
	 * @return the angle between 0 and 2*PI, 0 representing the usual gravity
	 * 		   (downwards pointing)
	 */
	public double getGravityAngle();
	
	
	/**
	 * Sets a new angle to the gravity.
	 * @param angle        in radians (double)
	 * @see Player         Player, which is using an instance of...
	 * @see Overlay        Overlay, displaying an instance of...
	 * @see SelectGravity  SelecteGravity, that can change the angle of the gravity.
	 */
	public void setGravity(double angle);	
	
	
	/**
	 * Asks the simulator to "link" a Linkable to different Signals.
	 * @param linkable  Actor (which must be Linkable) with which we desire to link some Signals.
	 */
	public void link(Linkable linkable);
	
	
	/**
	 * Used to call save() in Simulator.
	 * Circumvents the fact that the call would be issued in update itself...
	 */
	public void saveAll();
	
	
	/**
	 * Uses the Serialized data created by save() to load a brand new level.
	 * Part of the code was inspired by the one described on the mentioned website:
	 * @param world  Indicates which save is to be loaded (from 1 to 999)
	 * @see <a href="https://web.archive.org/web/20161114083612/http://www.tutorialspoint.com/java/java_serialization.htm">Source website</a>
	 */
	public void load(int world);
	
	
	/**
	 * Deletes a file or a folder.
	 * Code inspired by the website cited.
	 * @param file          can be a file or a folder.
	 * @throws IOException  If file not found, throw the corresponding exception.
	 * @see <a href="https://web.archive.org/web/20160805221409/http://www.mkyong.com/java/how-to-delete-directory-in-java/">Source website</a>
	 */
	static void delete(File file)
			throws IOException{

		// If file is a file, then delete it
		if(file.isFile()) {
			file.delete();
			System.out.println("File is deleted : " + file.getPath());
			return;
		}
		// Else, if the file is a folder and non empty, empty everything in it
		else if(file.list().length != 0) {
			// List the directory contents
			String files[] = file.list();

			// Recursively deletes all its contents
			for (String temp : files) {
				File fileDelete = new File(file, temp);
				delete(fileDelete);
			}
		}

		// Delete the folder
		file.delete();
		System.out.println("Directory is deleted : " + file.getPath());
	}
	
	
	/**
	 * Gives the shortcut from all integers between 0 and 10.
	 * @param i  integer between 0 and 10
	 * @return   KeyEvent corresponding to the key i
	 * @see KeyEvent
	 */
	static int keyboardCode(int i) {
		switch(i) {
		case 1:
			return KeyEvent.VK_1;
		case 2:
			return KeyEvent.VK_2;
		case 3:
			return KeyEvent.VK_3;
		case 4:
			return KeyEvent.VK_4;
		case 5:
			return KeyEvent.VK_5;
		case 6:
			return KeyEvent.VK_6;
		case 7:
			return KeyEvent.VK_7;
		case 8:
			return KeyEvent.VK_8;
		case 9:
			return KeyEvent.VK_9;
		case 0:
		case 10:
			return KeyEvent.VK_0;
		default:
			return KeyEvent.VK_UNDEFINED;
		}
	}
}
