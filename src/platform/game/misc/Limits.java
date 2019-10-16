package platform.game.misc;

import platform.game.Damage;
import platform.game.Actor;
import platform.util.Box;
import platform.util.Vector;

/**
 *  Delimits a zone outside of which some Actors can be killed.
 */

public class Limits extends Actor {

	private static final long serialVersionUID = 8925960138194789591L;

	
	
	/**
	 * Constructor.
	 * @param box  Position and size parameters of the Limits.
	 */
	public Limits(Box box) {
		super(box);
	}


	
	@Override
	public void interact(Actor other) {
		super.interact(other);
		// Gives damage to any Actor outside the zone.
		if (other.getBox() != null && !other.getBox().isColliding(getBox()))
			other.hurt(this, Damage.VOID, Double.POSITIVE_INFINITY, Vector.ZERO);
	}


	@Override
	protected int getPriority() {
		// Very high priority so almost every Actor is in the range.
		return 9001;
	}

}
