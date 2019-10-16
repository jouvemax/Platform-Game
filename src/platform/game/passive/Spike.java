package platform.game.passive;

import platform.game.Damage;
import platform.game.Actor;
import platform.util.Box;
import platform.util.Input;
import platform.util.Output;
import platform.util.Vector;

/**
 *  Gives PHYSICAL damage to an Actor.
 */

public class Spike extends Actor {

	private static final long serialVersionUID = 5434442670660582983L;


	/**
	 * Standard width of the Spikes
	 */
	private static final double width  = 1.0;


	/**
	 * Standard height of the Spikes
	 */
	private static final double height = 0.5;



	/**
	 * Full constructor.
	 * @param box  Position and size parameters of the Spike.
	 */
	public Spike(Box box) {
		super(box, "spikes");
	}


	/**
	 * Constructor with only a position.
	 * @param position  Vector
	 */
	public Spike(Vector position) {
		this(new Box(position, width, height));
	}



	@Override
	public void interact(Actor other) {
		super.interact(other);

		// Deal PHYSICAL damage to an Actor that interacts with it.
		if (getBox().isColliding(other.getBox())) {
			other.hurt(this, Damage.PHYSICAL, 2.5, Vector.ZERO);
			// 2.5 is enough to make the game challenging
		}
	}


	@Override
	public void draw(Input input, Output output) {
		super.draw(input, output);
		output.drawSprite(getSprite(), getBox());
	}


	@Override
	public boolean isSolid() {
		return true;
	}


	@Override
	protected int getPriority() {
		// Very high priority so Fireballs do not interact with it (they pass through)
		return 700;
	}


	@Override
	public Actor copie() {
		return new Spike(getBox());
	}

}
