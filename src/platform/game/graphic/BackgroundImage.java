package platform.game.graphic;

import platform.game.Actor;
import platform.util.Box;
import platform.util.Input;
import platform.util.Output;


/**
 *  Sets an image in the background of the world.
 */
public class BackgroundImage extends Actor {

	private static final long serialVersionUID = -6120930910778315381L;

	/**
	 * Only constructor proposed, no need for a box
	 * @param sprite  Sprite of the BackgroundImage
	 */
	public BackgroundImage(String sprite) {
		super(null, sprite);
	}

	
	
	@Override
	public void draw(Input input, Output output) {
		double maximum = Math.max(output.getBox().getHeight(), output.getBox().getWidth());
		// Draw the colour using the Box of the output directly
		output.drawSprite(getSprite(), new Box(output.getBox().getCenter(), maximum, maximum));
	}

	
	@Override
	protected int getPriority() {
		// Very low priority so nothing gets behind it, except for the BackgroundColour
		return -8;
	}

}
