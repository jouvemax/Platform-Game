package platform.game.passive;

import platform.game.Damage;
import platform.game.Actor;
import platform.util.Box;
import platform.util.Input;
import platform.util.Output;
import platform.util.Vector;

/**
 *  Allows an Actor to jump higher
 */

public class Jumper extends Actor {

	private static final long serialVersionUID = 8123067046162796584L;
	
	/**
	 * Current time, is decremented at each update
	 */
	private              double cooldown;
	
	
	/**
	 * Maximum cooldown, set to 0.5.
	 */
	private        final double cooldownMax;
	
	
	/**
	 *  Size of a standard Jumper, set to 1.0
	 */
	private static final double size = 1.0;

	
	
	/**
	 * Full constructor.
	 * @param box  Position and size parameters of the Jumper.
	 */
	public Jumper(Box box) {
		super(box, "jumper.normal");
		
		// Set to a negative value
		this.cooldown    = -1.0;
		this.cooldownMax = 0.5;
	}
	
	
	/**
	 * Constructor with a position instead of a Box
	 * @param position  Position of the Jumper.
	 */
	public Jumper(Vector position) {
		// Size set to the default size
		this(new Box(position, size, size));
	}
	
	

	@Override
	public void update(Input input) {
		super.update(input);
		cooldown -= input.getDeltaTime();
		
		// if the cooldown is positive, show its extended Sprite.
		if(cooldown > 0.0)
			setSpriteName("jumper.extended");
		else
			setSpriteName("jumper.normal");
	}
	

	@Override
	public void interact(Actor other) {
		super.interact(other);
		
		// If the cooldown is non positive and the Actor other is colliding with it, give AIR damage to the Actor.
		// Set the cooldown to cooldownMax.
		if (cooldown <= 0.0 && getBox().isColliding(other.getBox())) {
			Vector below = new Vector(getBox().getCenter().getX(), getBox().getCenter().getY() - 1.0);
			if (other.hurt(this, Damage.AIR, 10.0, below))
				cooldown = cooldownMax;
		}
	}
	
	
	@Override
	public void draw(Input input, Output output) {
		super.draw(input, output);
		
		// Draw the Sprite.
		output.drawSprite(getSprite(), getBox());
	}
	

	@Override
	protected int getPriority() {
		// Quite high priority to interact with some Actors.
		return 150;
	}
	

	@Override
	public Actor copie() {
		return new Jumper(getBox());
	}

}
