package platform.game.graphic;

import platform.game.Actor;
import platform.util.Box;
import platform.util.Input;
import platform.util.Output;

public class Image extends Actor {

	private static final long serialVersionUID = -3532223732507134558L;

	
	
	/**
	 * Full constructor
	 * @param box     Position and size parameters of the Image.
	 * @param sprite  Sprite of the Image.
	 */
	public Image(Box box, String sprite) {
		super(box, sprite);
	}

	
	
	@Override
	public void draw(Input input, Output output) {
		// Simple drawing
		output.drawSprite(getSprite(), getBox());
	}
	

	@Override
	protected int getPriority() {
		// Very high priority to get in front of almost every other Actor
		return 1000000;
	}

}
