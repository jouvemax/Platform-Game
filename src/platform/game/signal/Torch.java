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
 * Implements an Actor, Torch, which is also a signal.
 */

public class Torch extends Actor implements Signal {

	private static final long serialVersionUID = 3332543467883586051L;

	
	/**
	 * Indicates whether the Torch is lit or not
	 */
	private boolean lit;

	
	/**
	 * Variable decrementing after each update to change the Sprite of a lit torch
	 */
	private              double variation;
	
	
	/**
	 * Total cycle of the variation, set to 0.6 seconds.
	 */
	private static final double variationMax = 0.6;

	
	/**
	 * Standard size of a standard Torch, set to 0.5.
	 */
	private static final double size         = 0.5;



	/**
	 * Full constructor.
	 * @param box  Position and size parameters of the Torch.
	 * @param lit  indicates whether the torch is lit or not
	 */
	public Torch(Box box, boolean lit) {
		super(box, "torch");
		this.lit       = lit;
		this.variation = variationMax;
	}


	/**
	 * Constructor indicating only the box. Lit set to true by default.
	 * @param box  Position and size parameters of the Torch.
	 */
	public Torch(Box box) {
		this(box, true);
	}


	/**
	 * Constructor without any box but with a Vector called position.
	 * @param position  Position of the Torch.
	 * @param lit       Indicates whether the Torch is lit or not.
	 */
	public Torch(Vector position, boolean lit) {
		this(new Box(position, size, size), lit);
	}


	/**
	 * Constructor indicating only the position. Lit set to true by default.
	 * @param position  Position of the Torch.
	 */
	public Torch(Vector position) {
		this(position, true);
	}



	@Override
	public void update(Input input) {
		super.update(input);

		// Decrementing the time
		variation -= input.getDeltaTime();

		// If time is negative, reset it to variationMax
		if (variation < 0.0)
			variation = variationMax;
	}


	@Override
	public void draw(Input input, Output output) {
		super.draw(input, output);

		// If torch is lit, play on the variation to chose the appropriate Sprite.
		if(lit)
			output.drawSprite(getSprite("torch.lit."+((variation < variationMax/2.0)?1:2)), getBox());			
		// Else, draw the "normal" Sprite defined in the constructor.
		else
			output.drawSprite(getSprite(), getBox());

	}


	@Override
	public boolean hurt(Actor instigator, Damage type, double amount, Vector location) {
		switch(type) {
		case AIR:
			// AIR damage blows the torch
			// test if lit because we could implement an AIR attack (like Fireball)
			if(lit) {
				lit = false;
				return true;
			}
			return false;
		case FIRE:
			// FIRE damage blows the torch
			// test if not lit because a Fireball (e.g.) should not disappear on a lit Torch.
			if(!lit) {
				lit = true;
				return true;
			}
			return false;
		default :
			return super.hurt(instigator, type, amount, location);
		}
	}


	@Override
	protected int getPriority() {
		// Quite low priority so the Player can interact with it and go in front of it.
		return 34;
	}


	@Override
	public boolean isActive() {
		return lit;
	}


	@Override
	public Actor copie() {
		return new Torch(getBox(), lit);
	}


	/**
	 * No need for it, the Actor's unregister suffices.
	 */
	@Override
	public void unregister(World world) {}

}
