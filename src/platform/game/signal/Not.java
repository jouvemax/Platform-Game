package platform.game.signal;

import java.io.Serializable;

import platform.game.World;
import platform.game.Actor;

public class Not implements Signal, Serializable {

	private static final long serialVersionUID = -555793580247120118L;
	
	/**
	 * Signal with which we calculate the NOT
	 */
	private final Signal signal;

	
	
	/**
	 * Full constructor.
	 * @param signal  Signal to negate
	 */
	public Not(Signal signal) {
		if (signal == null)
			throw new NullPointerException();
		this.signal = signal;
	}
	


	@Override
	public boolean isActive() {
		// Return the opposite of the value of signal (by definition of NOT)
		return !signal.isActive();
	}


	/**
	 * Register the Signal so it appears correctly in the World.
	 */
	@Override
	public void register(World world) {
		if(signal instanceof Actor)
			world.register((Actor)signal);
		else
			signal.register(world);
	}

	
	/**
	 * Unregister the Signal so it appears correctly in the World.
	 */
	@Override
	public void unregister(World world) {
				if(signal instanceof Actor)
					world.unregister((Actor)signal);
				else
					signal.unregister(world);
		
	}
}
