package platform.game.weapon;

import platform.game.Actor;
import platform.game.Damage;
import platform.game.character.Player;
import platform.game.misc.Particle;
import platform.util.Box;
import platform.util.Input;
import platform.util.Output;
import platform.util.Vector;

/**
 *  Bomb represents an Actor dealing EXPLOSION type Damages.
 */

public class Bomb extends Actor {

	private static final long serialVersionUID = -2158307323762410988L;
	
	/**
	 * The bomb in itself also has a health status,
	 * meaning you can destroy it (explode it earlier) in a certain manner.
	 */
	private double health;
	
	
	/**
	 * Position of the Bomb.
	 */
	private Vector position;
	
	
	/**
	 * Speed of the Bomb.
	 */
	private Vector velocity;
	
	
	/**
	 * Indicates the "owner" of the Bomb, i.e. the one who registered it in the World.
	 */
	private final Actor owner;
	
	
	/**
	 * Lifespan of the bomb
	 */
	private double lifespan;
	
	
	/**
	 * Used to vary the sprite.
	 */
	private       double variation;
	
	
	/**
	 * Final variation cycles of the Sprite.
	 */
	private final double finalVariation;
	
	
	/**
	 * Factor of rebound, meaning it doesn't bounce as hard as the Fireballs e.g.
	 */
	private final double rebound;
	
	
	/**
	 * Standard size of the bomb
	 */
	private static final double size = 0.8;
	
	
	/**
	 * Zone of explosion
	 */
	private final double sizeZone = 2.0;

	
	
	/**
	 * Full constructor.
	 * @param position  Initial position (Vector)
	 * @param velocity  Initial velocity (Vector)
	 * @param owner		Owner (Actor)
	 */
	public Bomb(Vector position, Vector velocity, Actor owner) {
		super(new Box(position, size, size), "bomb");

		if(position == null || velocity == null)
			throw new NullPointerException();

		// Health set at 0.5 so it needs 1 Fireball to set it on fire.
		this.health    = 0.5;
		
		this.position  = position;
		this.velocity  = velocity;
		this.owner     = owner;
		
		// Lifespan set at 3.0 seconds
		this.lifespan       = 3.0;
		
		// finalVariation set at 0.5 and variation at
		// the lifespan plus the finalVariation divided by 2.0.
		this.finalVariation = 0.5;
		this.variation      = (lifespan + finalVariation)/2.0;
		
		// Rebound set at 0.75 so it doesn't rebound as hard as a Fireball (e.g.)
		this.rebound        = 0.75;
	}
	
	
	
	@Override
	public void interact(Actor other) {
		super.interact(other);
		// Rebound on anything solid, applying the rebound factor so it doesn't fly as high as a Fireball
		if (other.isSolid()) {
			Vector delta = other.getBox().getCollision(getBox());
			if (delta != null) {
				position = position.add(delta);
				velocity = velocity.mirrored(delta).mul(rebound);
			}
		}
	}

	
	@Override
	public void update(Input input) {
		super.update(input);

		double delta = input.getDeltaTime();

		// Verify the status of variation after decrementing it
		variation -= delta;
		if(variation < 0.0)
			variation = finalVariation;


		lifespan -= delta;
		
		// If the bomb is destroyed or its time has elapsed, then...
		if(health < 0.0 || lifespan < 0.0) {
			// ...define a colour. White if it's coming from the player, orange for anything else.
			String colour = (owner instanceof Player)?"white":"orange";
			
			// Hurt a certain zone with EXPLOSION, 4.0 of amount
			getWorld().hurt(new Box(getBox().getCenter(), sizeZone, sizeZone)
					, owner
					, Damage.EXPLOSION
					, 4.0
					, position);
			
			// Register 3 new Particles (the smoke)
			getWorld().register(new Particle(
					getBox().getCenter()
					, 0.9					// The smallest one
					, "smoke."+colour+".1"
					, 0.0					// Without any angle given to the Sprite
					, 0.6));				// With a lifespan of 0.6
			getWorld().register(new Particle(
					getBox().getCenter()
					, 1.1					// The medium one
					, "smoke."+colour+".2"
					, 0.0					// Without any angle given to the Sprite
					, 0.7					// With a lifespan a bit superior to the previous one
					, 0.1					// But a delay of 0.1, so it appears during 0.6 seconds
					, 1));					// Lower priority, so it appears behind
			getWorld().register(new Particle(
					getBox().getCenter()
					, 1.5					// The biggest one
					, "smoke."+colour+".3"
					, 0.0					// Without any angle given to the Sprite
					, 0.85					//  With a lifespan a bit superior to the previous ones
					, 0.2					// But appearing only after 0.3 seconds, during effectively 0.65 seconds
					, 2));					// Lowest priority, so it appears completely behind.
			
			// Then unregister the Bomb
			getWorld().unregister(this);
		}		
		
		// Update the position, the velocity, and the Box.
		velocity = velocity.add(getWorld().getGravity().mul(delta));
		position = position.add(velocity.mul(delta));
		setBox(new Box(position, getBox().getWidth(), getBox().getHeight()));
	}
	
	
	@Override
	public void draw(Input input, Output output) {
		super.draw(input, output);

		// "Paint" it white under certain circumstances
		if(variation < 0.25)
			output.drawSprite(getSprite("bomb.white"), getBox());
		else
			output.drawSprite(getSprite(), getBox());
		
	}

	
	@Override
	public boolean hurt(Actor instigator, Damage type, double amount, Vector location) {
		switch(type) {
		case FIRE:
			// Fire can destroy the Bomb (or explode it ahead!)
			health -= amount;
			return true;
		default:
			return super.hurt(instigator, type, amount, location);
		}
	}

	
	@Override
	public boolean isSolid() {
		return true;
	}


	@Override
	protected int getPriority() {
		// Lower priority than the Fireballs but still a high priority
		return 650;
	}

}
