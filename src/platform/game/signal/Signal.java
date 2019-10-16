package platform.game.signal;

import platform.game.World;

/**
 *  Interface that forces a class to signal if they are active or not.
 */

public interface Signal {
	
	/**
	 * @return  whether the Signal is active or not
	 */
	public boolean isActive();
	
	
	/**
	 * Registers all sub Signals (if they exist) in the world so the level can be
	 * reloaded without any problem.
	 * @param world  the World used by the Level.
	 */
	public void register(World world);
	
	/**
	 * Unregisters all sub Signals (if they exist) off the world so the level can be
	 *  without any problem.
	 * @param world  the World used by the Level.
	 */
	public void unregister(World world);
}
