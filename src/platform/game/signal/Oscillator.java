package platform.game.signal;

import platform.game.Actor;
import platform.game.World;
import platform.util.Input;

public class Oscillator extends Actor implements Signal {

	
	private static final long serialVersionUID = 1L;
	
	private final double period;
	private boolean value;
	private double time;
	
	public Oscillator(double period) {
		super(null);
		this.period = period;
		this.value  = false;
		this.time   = period;
	}
	
	


	@Override
	public void update(Input input) {
		time -= input.getDeltaTime();
		value = time < period/2.0;
		if(time < 0.0)
			time = period;
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
	public void unregister(World world) {
		super.unregister();
	}

	
	@Override
	protected int getPriority() {
		// Doesn't really matter since the oscillator is not visible
		return 0;
	}

}
