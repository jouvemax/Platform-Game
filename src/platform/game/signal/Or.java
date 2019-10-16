package platform.game.signal;

import java.io.Serializable;

import platform.game.World;
import platform.game.Actor;

/**
 *  Implements a simple OR Signal
 */

public class Or implements Signal, Serializable {

	private static final long serialVersionUID = -248532986662620065L;
	
	/**
	 * First signal with which we calculate the OR.
	 */
	private final Signal left;
	
	
	/**
	 * Second signal with which we calculate the OR
	 */
	private final Signal right;
	
	
	
	/**
	 * Full constructor, with 2 Signals.
	 * @param left   First Signal.
	 * @param right  Second Signal.
	 */
	public Or(Signal left, Signal right) {
		if (left == null || right == null)
			throw new NullPointerException();
		this.left  = left;
		this.right = right;
	}

	
	
	@Override
	public boolean isActive() {
		// Return true iff at least one of the Signal is true (by definition of OR)
		return left.isActive() || right.isActive();
	}

	
	/**
	 * Register both Signals so they appear correctly in the World.
	 */
	@Override
	public void register(World world) {
		if(left instanceof Actor)
			world.register((Actor)left);
		else
			left.register(world);
		
		if(right instanceof Actor)
			world.register((Actor)right);
		else
			right.register(world);
	}


	/**
	 * Unregister both Signals so they appear correctly in the World.
	 */
	@Override
	public void unregister(World world) {
		if(left instanceof Actor)
			world.unregister((Actor)left);
		else
			left.unregister(world);
		
		if(right instanceof Actor)
			world.unregister((Actor)right);
		else
			right.unregister(world);		
	}

}
