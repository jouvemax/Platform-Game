package platform.game.signal;

import java.io.Serializable;

import platform.game.World;

/**
 *  Implements a simple constant Signal
 */

public class Constant implements Signal, Serializable {

	private static final long serialVersionUID = -8334898046552028658L;
	
	/**
	 * Value of the Signal
	 */
	private final boolean value;
	
	
	
	/**
	 * Full constructor.
	 * @param value  Value of the Signal (true or false).
	 */
	public Constant(boolean value) {
		this.value = value;
	}

	
	@Override
	public boolean isActive() {
		return value;
	}
	

	/**
	 * Unlike other signals, can't do anything since
	 * the value is not a Signal (nor an Actor).
	 */
	@Override
	public void register(World world) {}


	/**
	 * Unlike other signals, can't do anything since
	 * the value is not a Signal (nor an Actor).
	 */
	@Override
	public void unregister(World world) {}

}
