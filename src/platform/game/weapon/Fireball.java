package platform.game.weapon;

import platform.game.Damage;
import platform.game.Actor;
import platform.game.signal.Torch;
import platform.util.Box;
import platform.util.Input;
import platform.util.Output;
import platform.util.Vector;

/**
 *  Fireball represents an Actor dealing FIRE type Damages.
 */

public class Fireball extends Actor {

	private static final long serialVersionUID = 4137370488669336207L;
	
	
	/**
	 * Position of the Fireball.
	 */
	private Vector position;
	
	
	/**
	 * Speed of the Fireball.
	 */
	private Vector velocity;
	
	
	/**
	 * Indicates the "owner" of the Fireball, i.e. the one who registered it in the World.
	 */
	private Actor owner;
	
	
	/**
	 * Lifespan of the Fireball
	 */
	private double lifespan;
	
	
	/**
	 * Standard size of the fireball
	 */
	private static final double size = 0.4;
	
	
	
	/**
	 * Full constructor.
	 * @param box       Position and size parameters of the Fireball.
	 * @param velocity  Initial velocity (Vector)
	 * @param owner		Owner (Actor)
	 */
	public Fireball(Box box, Vector velocity, Actor owner) {
		super(box, "fireball");

		if(velocity == null)
			throw new NullPointerException();

		this.position   = box.getCenter();
		this.velocity   = velocity;
		this.owner      = owner;
		
		// A Fireball lasts 1.0 second.
		this.lifespan   = 1.0;
	}
	
	
	/**
	 * Constructor without any Box but a position Vector.
	 * @param position  Position parameter of the Fireball.
	 * @param velocity  Speed parameter of the Fireball.
	 * @param owner     Owner (Actor).
	 */
	public Fireball(Vector position, Vector velocity, Actor owner) {
		this(new Box(position, size, size), velocity, owner);
	}

	
	
	@Override
	public void interact(Actor other) {
		super.interact(other);
		
		boolean isTorch = other instanceof Torch;
		// Check if the other Actor is Solid and not the owner OR a Torch
		if ((other.isSolid() && other != owner)
				|| isTorch) {
			Vector delta = other.getBox().getCollision(position);
			// If it's the case and both of the actors are colliding,
			// deal some FIRE type Damages (amount 1.0)
			// If successful, then unregister the Fireball
			if (other.getBox().isColliding(getBox())
					&& other.hurt(this, Damage.FIRE, 1.0, getPosition()))
					getWorld().unregister(this);
			
			// If delta exists AND the actor is not a torch,
			// then modify accordingly the position and the velocity
			if (delta != null && !isTorch) {
				position = position.add(delta);
				velocity = velocity.mirrored(delta);
			}
		}

	}
	

	@Override
	public void update(Input input) {
		super.update(input);
		
		double delta = input.getDeltaTime();
		
		lifespan -= delta;
		
		// Check if the Fireball has not finished its existence
		if(lifespan < 0.0)
			getWorld().unregister(this);
		
		// Update the position
		velocity = velocity.add(getWorld().getGravity().mul(delta));
		position = position.add(velocity.mul(delta));
		setBox(new Box(position, getBox().getWidth(), getBox().getHeight()));
	}

	
	@Override
	public void draw(Input input, Output output) {
		super.draw(input, output);
		
		// Draw the Sprite with some rotation
		output.drawSprite(getSprite(), getBox(), 15*input.getTime());
	}

	
	@Override
	protected int getPriority() {
		// High priority so it interacts with almost everything
		return 666;
	}

}
