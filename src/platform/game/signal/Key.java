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
 * Implements an Actor, Key, which is also a Signal.
 */

public class Key extends Actor implements Signal {

	private static final long serialVersionUID = -7143926643556036410L;
	
	
	/**
	 * Indicates whether the key has been taken or not, i.e. if the Signal should be active or not.
	 */
	private boolean taken;
	
	
	/**
	 * Standard size
	 */
	private final static double size = 0.4;
	
	
	
	/**
	 * Full constructor.
	 * @param box     Position and size parameters of the Key.
	 * @param sprite  Sprite of the Key
	 */
	public Key(Box box, String sprite) {
		super(box, sprite);
		this.taken    = false;
	}


	/**
	 * Constructor with a vector instead of a Box. Standard size issued.
	 * @param position  Initial position of the Key.
	 * @param sprite    Sprite of the Key.
	 */
	public Key(Vector position, String sprite) {
		this(new Box(position, size, size), sprite);
	}
	
	
	/**
	 * Constructor with a default Sprite.
	 * @param box  Position and size parameters of the Key.
	 */
	public Key(Box box) {
		this(box, "key.yellow");
	}
	
	
	/**
	 * Constructor with a vector instead of a Box and a default Sprite. Standard size issued.
	 * @param position  Initial position of the Key.
	 */
	public Key(Vector position) {
		this(new Box(position, size, size));
	}


	
	@Override
	public void interact(Actor other) {
		// If...
		if (getBox().isColliding(other.getBox()) // the Key is colliding with an Actor...
				&& !taken						 // ...and it is not yet taken...
				&& other.hurt(this, Damage.KEY, 1.0, Vector.ZERO) //... and the Actor answers to the Damage KEY,
				) {
			// Then set taken as true and unregister the key.
			taken = true;
			getWorld().unregister(this);
		}
	}
	
	
	@Override
	public void update(Input input) {
		super.update(input);
		
		// Verifies if the taken key is really unregistered.
		// Could cause problems in the loading of a level without this security.
		if(taken && getWorld() != null)
			getWorld().unregister(this);
	}

	
	@Override
	public void draw(Input input, Output output) {
		// Draw the Key iff it is not taken.
		if(!taken)
			output.drawSprite(getSprite(), getBox());
	}	


	@Override
	public boolean isActive() {
		return taken;
	}
	

	@Override
	protected int getPriority() {
		// Little higher than the player so it can interact with it.
		return 51;
	}
	

	@Override
	public Actor copie() {
		return new Key(getBox(), getSpriteName());
	}
	

	/**
	 * No need for it, the Actor's unregister suffices.
	 */
	@Override
	public void unregister(World world) {}
}
