package platform.game.signal;

import java.io.Serializable;

import platform.game.World;
import platform.game.Actor;

/**
 *  Implements a simple AND Signal
 */

public class And implements Signal, Serializable {

	private static final long serialVersionUID = -8222737616147785317L;
	
	/**
	 * First signal with which we calculate the AND.
	 */
	private final Signal left;
	
	
	/**
	 * Second signal with which we calculate the AND.
	 */
	private final Signal right;
	
	
	
	/**
	 * Full constructor, with 2 Signals.
	 * @param left   First Signal.
	 * @param right  Second Signal.
	 */
	public And(Signal left, Signal right) {
		if (left == null || right == null)
			throw new NullPointerException();
		this.left  = left;
		this.right = right;
	}

	
	
	@Override
	public boolean isActive() {
		// Return true iff both of the Signals are true (by definition of AND)
		return left.isActive() && right.isActive();
	}
	

	@Override
	public void register(World world) {
		// Register both Signals so they appear correctly in the World.
		if(left instanceof Actor)
			world.register((Actor)left);
		else
			left.register(world);
		
		if(right instanceof Actor)
			world.register((Actor)right);
		else
			right.register(world);
		
	}



	@Override
	public void unregister(World world) {
		// Register both Signals so can be saved correctly
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
