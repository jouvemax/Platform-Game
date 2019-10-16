package platform.game;

import platform.game.button.Spawn;
import platform.game.button.SuperSpawner;
import platform.util.Box;
import platform.util.Input;
import platform.util.Output;

/**
 *  Can erase existing Actors, e.g. in the level Builder.
 */

public class Eraser extends Actor {

	private static final long serialVersionUID = -8664211171137649266L;

	/**
	 * Target of the Eraser
	 */
	private Actor target;

	
	
	/**
	 * Full constructor.
	 * @param box  Position and size parameters of the Eraser.
	 */
	public Eraser(Box box) {
		super(box, "cross");
		this.target = null;
	}



	@Override
	public void preUpdate(Input input) {
		// Reset the target to null.
		target = null;
	}


	@Override
	public void interact(Actor other) {
		// If the other Actor:
		// - has got a non null Box
		// - is colliding with the eraser
		// - the other Actor is not the Eraser
		// - the other Actor has a lower priority than the Eraser
		if(other.getBox() != null
				&& other.getBox().isColliding(getBox())
				&& other != this
				&& other.getPriority() < getPriority()
				&& !(other instanceof Spawn)
				&& !(other instanceof SuperSpawner))
			// Set the other Actor as the Target
			target = other;
	}


	@Override
	public void draw(Input input, Output output) {
		output.drawSprite(getSprite(), getBox());
	}


	@Override
	public void update(Input input) {
		// If the user left-clicks and the target is not null, then erase the Eraser...
		if(input.getMouseButton(1).isPressed()) {
			// ...and the target if it's not null
			if(target != null)
				getWorld().unregister(target);
			getWorld().unregister(this);
		}
	}


	@Override
	protected int getPriority() {
		// Must be less than Limits!
		// Otherwise can erase them.
		return 8000;
	}


	@Override
	public Actor copie() {
		return new Eraser(getBox());
	}

}
