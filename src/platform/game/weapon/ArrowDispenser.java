package platform.game.weapon;

import platform.game.Actor;
import platform.game.Damage;
import platform.game.block.Block;
import platform.game.Arrow;
import platform.util.Box;
import platform.util.Input;
import platform.util.Output;
import platform.util.Vector;

/**
 *  Block that throw an arrow each x seconds.
 */

public class ArrowDispenser extends Block {

	private static final long serialVersionUID = -1511864266482908442L;
	
	/**
	 * Indicates whether the dispenser has recently shot an arrow
	 */
	private       boolean shot;
	
	/**
	 * Cooldown set to a certain time.
	 */
	private final double  cooldown;
	
	/**
	 * Time being decremented after each upload
	 */
	private       double  time;
	
	/**
	 * Indicates where the player is
	 */
	private       double angle;

	
	
	/**
	 * Full constructor.
	 * @param box       Position and size parameters of the ArrowDispenser.
	 * @param cooldown  Set to a certain time after which the Dispenser can throw
	 *                  a new arrow.
	 */
	public ArrowDispenser(Box box,  double cooldown) {
		super(box, "metal.1");
		
		// Set to false to begin with.
		this.shot     = false;
		
		this.cooldown = cooldown;
		
		// Set to -1.0 to have a negative value.
		this.time     = -1.0;
		
		this.angle    = Math.PI/4.0;
	}

	
	/**
	 * Constructor without specifying any cooldown time.
	 * @param box  Position and size parameters of the ArrowDispenser.
	 */
	public ArrowDispenser(Box box) {
		// Default value at 5.0 seconds.
		this(box, 5.0);
	}	
	

	
	@Override
	public void update(Input input) {
		super.update(input);
		
		// Decrements time
		time -= input.getDeltaTime();
		
		// If it's shot an arrow and the time is negative, set it to "not shot"
		if(time <= 0.0 && shot)
			shot = false;
	}
	
	
	
	

	@Override
	public void draw(Input input, Output output) {
		super.draw(input, output);
		output.drawSprite(getSprite("bow.arrow"), getBox(), angle);
	}


	@Override
	public boolean hurt(Actor instigator, Damage type, double amount, Vector location) {
		switch(type) {
		case PRESENCE:
			// if the instigator indicating the PRESENCE is near (2.0) the ArrowDispenser...
			Vector distanceVector = location.sub(getBox().getCenter());
			angle = location.sub(getBox().getCenter()).getAngle();
			if(!shot && distanceVector.getLength() < 2.0) {
				// Shoot an arrow and set shot to true and time to cooldown.
				// 4.0 seems to be an acceptable factor for the speed (not too fast, not too slow)
				getWorld().register(new Arrow(getBox().getCenter(), distanceVector.mul(4.0), this));
				shot = true;
				time = cooldown;
				return true;
			}
			return false;
		default:
			return super.hurt(instigator, type, amount, location);
		}
	}

	
	@Override
	public Actor copie() {
		return new ArrowDispenser(getBox(), cooldown);
	}
	
}
