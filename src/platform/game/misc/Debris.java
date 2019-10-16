package platform.game.misc;

import platform.game.Damage;
import platform.game.Actor;
import platform.util.Box;
import platform.util.Input;
import platform.util.Output;
import platform.util.Vector;

/**
 *  Spawns a few "debris" around a block that exploded/has been destroyed.
 */

public class Debris extends Actor {

	private static final long serialVersionUID = -5374058770341514807L;
	
	
	/**
	 * Name of all the Sprites used.
	 */
	private String[] spriteNames;
	
	
	/**
	 * Position of all the sub-Debris.
	 */
	private Vector[] positions;
	
	
	/**
	 * Speeds of all the sub-Debris.
	 */
	private Vector[] velocities;
	
	
	/**
	 * Size of each Debris
	 */
	private double   size;
	
	
	/**
	 * Number of Debris
	 */
	private int      length;

	
	
	/**
	 * Only constructor.
	 * @param position     Position of the origin
	 * @param spriteNames  Name of the debris' sprites
	 * @param size         Size of each Debris
	 */
	public Debris(Vector position, String[] spriteNames, double size) {
		super(new Box(position, size, size));
		
		// Initial declaration of the variables
		this.spriteNames = spriteNames;
		this.size        = size;
		this.length      = spriteNames.length;
		this.positions   = new Vector[length];
		this.velocities  = new Vector[length];
		
		// Each debris has a base velocity of norm 4.0.
		Vector baseVelocity = new Vector(4.0,0.0);
		
		// We use this base angle to spawn them in a circle around the block.
		double angle = 2*Math.PI/length;
		
		// We assign to each and everyone of them a position and a velocity.
		// The velocities are not pointing in the same direction by definition.
		for(int i=0 ; i<length;++i) {
			positions[i]  = position;
			velocities[i] = baseVelocity.rotated(angle*i);
		}
	}
	

	
	@Override
	public void update(Input input) {
		super.update(input);

		// Update all positions and velocities using the gravity of the World.
		double delta = input.getDeltaTime();
		for(int i=0 ; i<length;++i) {
			velocities[i] = velocities[i].add(getWorld().getGravity().mul(delta));
			positions[i]  = positions[i].add(velocities[i].mul(delta));
		}
		
		// Set the box of the Debris in itself to the first one. The box is used
		// in hurt().
		setBox(new Box(positions[0], size, size));
	}


	@Override
	public void draw(Input input, Output output) {
		// Draw each individual Debris.
		for(int i=0 ; i<length;++i)
			output.drawSprite(getSprite(spriteNames[i]), new Box(positions[i], size, size), input.getTime());
	}

	
	@Override
	public boolean hurt(Actor instigator, Damage type, double amount, Vector location) {
		switch(type) {
		// Unregister the Debris if the box is outside the Limits of the World.
		case VOID:
			getWorld().unregister(this);
			return true;
		default:
			return super.hurt(instigator, type, amount, location);
		}
	}

	
	@Override
	protected int getPriority() {
		// High priority so it draws in front of a lot of Actors.
		return 200;
	}

}
