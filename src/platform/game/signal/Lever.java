package platform.game.signal;

import platform.game.Damage;
import platform.game.World;
import platform.game.Actor;
import platform.game.signal.Signal;
import platform.util.Box;
import platform.util.Input;
import platform.util.Output;
import platform.util.Vector;

/**
 * Implements an Actor, Lever, which is also a Signal.
 */

public class Lever extends Actor implements Signal {

	private static final long serialVersionUID = -6830104675889492318L;

	/**
	 * Value of the Signal
	 */
	private boolean value;

	
	/**
	 * Max time the Lever can remain "true"
	 */
	private final        double duration;
	
	
	/**
	 *  The time indicates if the Lever should be on (positive value) or off (negative value)
	 */
	private              double time;
	
	
	/**
	 * The stanardNegativeValue is just a negative value.
	 */
	private final static double standardNegativeValue = -1.0;

	/**
	 * Standard size, set to 0.8.
	 */
	private final static double size = 0.8;



	/**
	 * Full constructor.
	 * @param box       Position and size parameters of the Lever.
	 * @param duration  Indicates for how long the Lever should remain on. Negative values mean infinitely long.
	 */
	public Lever(Box box,  double duration) {
		super(box);
		this.value    = false;
		this.duration = duration;
		this.time     = standardNegativeValue;
	}


	/**
	 * Constructor setting the duration to a negative value (so the lever stays on if the player tells it to)
	 * @param box  Position and size parameters of the Lever.
	 */
	public Lever(Box box) {
		this(box, standardNegativeValue);
	}


	/**
	 * Alternative of the constructor with a position Vector.
	 * @param position  Position of the Lever.
	 * @param duration  Indicates for how long the Lever should remain on. Negative values mean infinitely long.
	 */
	public Lever(Vector position, double duration) {
		this(new Box(position, size, size), duration);
	}


	/**
	 * Another alternative with a position Vector and no duration.
	 * @param position  Position of the Lever.
	 */
	public Lever(Vector position) {
		this(new Box(position, size, size));
	}



	@Override
	public void update(Input input) {
		super.update(input);
		// Decrements the time iff it is non negative.
		time -= (time >= 0.0)?input.getDeltaTime():0.0;

		// Sets the value of the lever to true iff the time is positive
		// OR [the duration in non positive AND value is true] (otherwise the Actor cannot be interacted with)
		value = (time > 0.0 || (duration <= 0.0 && value));
	}


	@Override
	public void draw(Input input, Output output) {
		// Draws the Sprite in a certain manner (depends on the value)
		output.drawSprite(getSprite(value?"lever.right":"lever.left"), getBox());
	}


	@Override
	public boolean hurt(Actor instigator, Damage type, double amount, Vector location) {
		switch(type) {
		case ACTIVATION:
			// Changes the value of the Lever if the amount is positive.
			if(amount > 0.0) {
				value = !value;
				time = duration;
				return true;
			}
			return false;
		default:
			return super.hurt(instigator, type, amount, location);
		}
	}


	@Override
	public boolean isActive() {
		return value;
	}

	
	@Override
	protected int getPriority() {
		// The player should pass in front of the lever.
		return 1;
	}
	

	@Override
	public Actor copie() {
		return new Lever(getBox());
	}


	/**
	 * No need for it, the Actor's unregister suffices.
	 */
	@Override
	public void unregister(World world) {}

}
