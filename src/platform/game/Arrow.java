package platform.game;

import platform.game.character.Player;
import platform.util.Box;
import platform.util.Input;
import platform.util.Output;
import platform.util.Vector;

/**
 *  Arrow represents an Actor dealing ARROW type damages.
 */

public class Arrow extends Actor {

	private static final long serialVersionUID = -272816363406430018L;
	
	
	/**
	 * Position of the Arrow.
	 */
	private Vector position;
	
	
	/**
	 * Speed of the Arrow.
	 */
	private Vector velocity;
	
	
	/**
	 * Indicates whether the arrow has been shot or not (i.e. is on something)
	 */
	private boolean shot;
	
	
	/**
	 * Indicates who shot the arrow
	 */
	private Actor owner;
	
	
	/**
	 * Indicates where the arrow has landed
	 */
	private Actor target;
	
	
	/**
	 * Indicates the angle of the arrow when it flies and when it lands
	 */
	private double angle;
	
	
	/**
	 * Indicates the relative position of the Arrow (relative to the center of the target)
	 */
	private Vector relativePosition;

	
	
	/**
	 * Full constructor.
	 * @param box       Position and size parameters of the Arrow.
	 * @param velocity  Initial speed of the Arrow.
	 * @param owner     Owner of the Arrow.
	 */
	public Arrow(Box box, Vector velocity, Actor owner) {
		super(box, "arrow");

		if(velocity == null)
			throw new NullPointerException();

		this.position   = box.getCenter();
		this.velocity   = velocity;
		this.owner      = owner;
		
		// The arrow has not yet landed...
		this.shot       = false;
		
		// ... so we don't know the target either
		this.target     = null;
		
		// Base angle is calculated from the velocity
		this.angle      = velocity.getAngle();
	}

	
	/**
	 * Constructor without any box, standard size.
	 * @param position  Initial position of the Arrow
	 * @param velocity  Initial speed of the Arrow.
	 * @param owner     Owner of the Arrow.
	 */
	public Arrow(Vector position, Vector velocity, Actor owner) {
		// 0.75 is the desired width (length), and 22.5/134.0 a factor calculated
		// with the ratio of the image "arrow" in /res/.
		this(new Box(position, 0.75, 22.5/134.0), velocity, owner);
	}
	
	
	
	@Override
	public void interact(Actor other) {
		super.interact(other);

		// If:
		// - the arrow has not yet landed
		// - the other Actor has a Box
		// - the other Actor is solid OR it can get damages from arrows (2.0 damages for an Arrow)
		// - the other Actor is not the owner
		// - the other Box is colliding with the arrow
		// then...
		if (!shot 
				&& other.getBox() != null
				&&(other.isSolid() || other.hurt(this, Damage.ARROW, 2.0, getBox().getCenter()))
				&& owner != other
				&& other.getBox().isColliding(getBox())) {
			// Set the angle according to the velocity, then set this one to 0.
			angle = velocity.getAngle();
			velocity = Vector.ZERO;
			
			// Calculate the relativePosition factored by 0.5 if player (the arrow goes more inside...), 0.9 otherwise
			relativePosition = getBox().getCenter().sub(other.getBox().getCenter()).mul((other instanceof Player)?0.5:0.9);
			
			// Set the arrow as shot and the target to other
			shot = true;
			target = other;
			
			// Refresh the arrow to get the new priority
			getWorld().unregister(this);
			getWorld().register(this);
		}
		
		if(target == null)
			shot = false;
	}

	@Override
	public void update(Input input) {
		super.update(input);

		double delta = input.getDeltaTime();

		// If it's not yet landed, update the Arrow's parameters
		if(!shot) {
			velocity = velocity.add(getWorld().getGravity().mul(delta));
			position = position.add(velocity.mul(delta));
			angle = velocity.getAngle();
			setBox(new Box(position, getBox().getWidth(), getBox().getHeight()));
		}
		// Otherwise, follow the target.
		else {
			setBox(new Box(target.getBox().getCenter().add(relativePosition), getBox().getWidth(), getBox().getHeight()));
			
			// If the target stopped existing, follow its fate.
			if(target.getWorld() == null)
				getWorld().unregister(this);
		}
	}

	@Override
	public void draw(Input input, Output output) {
		super.draw(input, output);
		
		// Draw the arrow with its specific angle
		output.drawSprite(getSprite(), getBox(), angle);
	}
	
	
	/**
	 * Returns the speed of the arrow.
	 * @return velocity
	 * @see Player
	 */
	public Vector getVelocity() {
		return velocity;
	}

	

	@Override
	protected int getPriority() {
		// Change the priority according to its status, so it appears behind the player after it shot him.
		// That's why the arrow has been "refreshed" in interact().
		if(shot)
			return -1;
		return 600;
	}

}
