package platform.game.misc;

import platform.game.Actor;
import platform.util.Box;
import platform.util.Input;
import platform.util.Output;
import platform.util.Vector;

/**
 *  Spawns a Particle that has a limited lifespan.
 */

public class Particle extends Actor {

	private static final long serialVersionUID = -6715829161690344857L;
	
	/**
	 * Angle given to the Sprite
	 */
	private       double angle;
	
	/**
	 * Lifespan of the Particle
	 */
	private       double lifespan;
	
	/**
	 * When the particle should spawn.
	 */
	private       double delay;
	
	/**
	 * Time elapsed since the beginning.
	 */
	private       double time;
	
	/**
	 * Number given to modify the priority, so it's sure to be drawn
	 * behind/in front of other Particles.
	 */
	private final int    order;

	
	
	/**
	 * Full constructor with Box
	 * @param box       Box of the Particle
	 * @param sprite    Name of the Sprite
	 * @param angle		Angle given to the Sprite
	 * @param lifespan  Lifespan of the Particle
	 * @param delay		Delay after which the Particle should appear
	 * @param order		Order in which the particle should be compared to other Particles
	 */
	public Particle(Box box, String sprite, double angle, double lifespan, double delay, int order) {
		super(box, sprite);
		this.angle    = angle;
		this.lifespan = lifespan;
		
		// Time set to 0.0 so the delay makes sense
		this.time     = 0.0;
		
		this.delay    = delay;
		this.order    = order;
	}
	
	
	/**
	 * Full constructor with Vector and size instead of Box
	 * @param position  Position of the particle
	 * @param size		Size of the particle
	 * @param sprite    Name of the Sprite
	 * @param angle		Angle given to the Sprite
	 * @param lifespan  Lifespan of the Particle
	 * @param delay		Delay after which the Particle should appear
	 * @param order		Order in which the particle should be compared to other Particles
	 */
	public Particle(Vector position, double size, String sprite, double angle, double lifespan, double delay, int order) {
		this(new Box(position, size, size), sprite, angle, lifespan, delay, order);
	}
	
	
	/**
	 * Constructor without any delay nor order.
	 * @param position  Position of the particle
	 * @param size		Size of the particle
	 * @param sprite    Name of the Sprite
	 * @param angle		Angle given to the Sprite
	 * @param lifespan  Lifespan of the Particle
	 */
	public Particle(Vector position, double size, String sprite, double angle, double lifespan) {
		this(position, size, sprite, angle, lifespan, 0.0, 0);
	}
	
	

	@Override
	public void update(Input input) {
		super.update(input);
		
		// Increase the time
		time += input.getDeltaTime();
		
		// If the Particle has lived its life, kill it.
		if (time >= lifespan)
			getWorld().unregister(this);
	}
	
	
	@Override
	public void draw(Input input, Output output) {
		super.draw(input, output);
		
		// If a sprite is defined and the time is after the delay,
		// draw the picture.
		if(getSprite() != null && time > delay)
			output.drawSprite(getSprite(), getBox(), angle);
	}
	

	@Override
	protected int getPriority() {
		// Returns a reduces priority 
		return 1337-order;
	}

}
