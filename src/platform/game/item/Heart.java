package platform.game.item;

import platform.game.Damage;
import platform.game.Actor;
import platform.util.Box;
import platform.util.Input;
import platform.util.Output;
import platform.util.Vector;

/**
 *  Heals the Player.
 */

public class Heart extends Actor {

	private static final long serialVersionUID = 5200044905607487286L;

	/**
	 * Cooldown, reappears after x seconds.
	 */
	private double cooldown;
	
	/**
	 * Default size.
	 */
	private static final double size = 0.3;


	/**
	 * Full constructor
	 * @param box  Position and size parameters of the Heart.
	 */
	public Heart(Box box) {
		super(box, "heart.full");

		// Set to -1.0 to have a negative value
		this.cooldown = -1.0;
	}


	/**
	 * Constructor with only a Vector as a parameter.
	 * @param position  Position of the Heart.
	 */
	public Heart(Vector position) {
		this(new Box(position, size, size));
	}

	
	
	@Override
	public void interact(Actor other) {
		super.interact(other);
		
		// Set cooldown to 10.0 if and only if the cooldown has elpased,
		// an Actor is colliding with the heart and it answers to the HEAL
		if (cooldown < 0
				&& getBox().isColliding(other.getBox())
				&& other.hurt(this, Damage.HEAL, 1.0, Vector.ZERO))
				cooldown = 10.0;
	}
	

	@Override
	public void update(Input input) {
		super.update(input);

		// Decrements the cooldown
		if(cooldown >= 0)
			cooldown -= input.getDeltaTime();
	}


	@Override
	public void draw(Input input, Output output) {
		super.draw(input, output);

		// Draw the heart only if the cooldown has elapsed
		if(cooldown < 0.0)
			output.drawSprite(getSprite(), getBox());
	}


	@Override
	protected int getPriority() {
		// Higher priority than the player
		return 50;
	}

	
	@Override
	public Actor copie() {
		return new Heart(getBox());
	}

}
