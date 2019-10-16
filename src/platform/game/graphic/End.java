package platform.game.graphic;

import platform.game.Actor;
import platform.util.Box;
import platform.util.Input;
import platform.util.Output;


/**
 *  Proposes a smooth transition to the next Level after a violent death
 */
public class End extends Actor {

	private static final long serialVersionUID = 491137978463042822L;

	/**
	 * Variable of time, used to count down until the reload
	 */
	private double time;
	
	/**
	 * How long should the Actor be in place before restarting the level. Set to 2.5 seconds.
	 */
	private final double duration;
	
	/**
	 * Simple constructor, nothing asked.
	 */
	public End() {
		super(null, "pixel.black");

		// The value for duration has been chosen after careful
		// analysis of the players' reactions.
		this.time     = 0.0;
		this.duration = 3.5;
	}

	

	@Override
	public void update(Input input) {
		super.update(input);
		
		// Increments time
		time += input.getDeltaTime();
		
		// If the time has reached the duration, call the nextLevel
		if(time > duration)
			getWorld().nextLevel();
	}


	@Override
	public void draw(Input input, Output output) {
		double transparency = 0.0;
		if(time > 1.0) {
			output.drawSprite(getSprite("wasted"), new Box(output.getBox().getCenter(), 11.0, 3.5));
			transparency = Math.min(Math.max(0.2, time - duration+0.5), 1.0);
		}
		output.drawSprite(getSprite(), output.getBox(), 0.0, transparency);
	}

	
	@Override
	protected int getPriority() {
		// High priority so most of the elements are masked.
		return 10000;
	}

}
