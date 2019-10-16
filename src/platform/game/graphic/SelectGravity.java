package platform.game.graphic;

import java.awt.event.KeyEvent;

import platform.game.button.Button;
import platform.util.Box;
import platform.util.Input;
import platform.util.Output;
import platform.util.Vector;

/**
 *  Allows the user to change the Gravity.
 *  Inspired by the game VVVVVV, 2010, by Nicalis (Terry Cavanagh, Magnus Palsson)
 */

public class SelectGravity extends Button {

	private static final long serialVersionUID = -5648214742870802380L;

	/**
	 * Angle of the gravity, 0.0 being the normal one.
	 */
	private double angle;



	/**
	 * Constructor given with a start value for the gravity.
	 * @param gravityAngle
	 */
	public SelectGravity(double gravityAngle) {
		super(new Box(Vector.ZERO, 1.5, 1.5), "grav.arrow");
		this.angle = gravityAngle;
	}



	@Override
	public void update(Input input) {
		super.update(input);

		if(angle < -9)
			angle = getWorld().getGravityAngle();
		
		// Sets a new angle with the click of the user
		if(getBox().isColliding(input.getMouseLocation())
				&& input.getMouseButton(1).isDown())
			angle = input.getMouseLocation().sub(getBox().getCenter()).getAngle()+Math.PI/2;

		// Sets a new angle according to the scrolling made by the user
		angle += Math.PI*0.03*input.getMouseScroll();

		// Set a new angle according to a key stroke
		if (input.getKeyboardButton(KeyEvent.VK_DOWN).isPressed())
			angle = 0.0;
		if (input.getKeyboardButton(KeyEvent.VK_RIGHT).isPressed())
			angle = Math.PI/2;
		if (input.getKeyboardButton(KeyEvent.VK_UP).isPressed())
			angle = Math.PI;
		if (input.getKeyboardButton(KeyEvent.VK_LEFT).isPressed())
			angle = -Math.PI/2;

		// Sets the new gravity with the calculated angle
		getWorld().setGravity(angle);
	}


	@Override
	public void draw(Input input, Output output) {
		super.draw(input, output);

		// Resets the Box according to the actual center of the view
		double radius = Math.min(output.getBox().getHeight()/2.0, output.getBox().getWidth()/2.0);
		
		setBox(new Box(output.getBox().getCenter().add(
				new Vector(radius, 0.0).rotated(Math.PI/4))
				, getBox().getWidth()
				, getBox().getHeight()));
		// Draws the "compass"
		output.drawSprite(getSprite("grav.circle"), getBox());
		output.drawSprite(getSprite(), getBox(), angle);
	}



}
