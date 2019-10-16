package platform.game.graphic;

import platform.util.Box;
import platform.util.Input;
import platform.util.Output;
import platform.util.Vector;

/**
 *  Creates an imaged composed of a one-digit or a two-digit number.
 */
public class Digit extends Image {

	private static final long serialVersionUID = 1456946673906868646L;


	/**
	 * Width of a digit (as seen in res/): 2.6.
	 */
	private static final double widthDigit  = 2.6;


	/**
	 * Height of a digit (as seen in res/): 3.7.
	 */
	private static final double heightDigit = 3.7;


	/**
	 * Name of the Sprite of the second digit.
	 */
	private final String secondDigit;
	
	
	/**
	 * Box of the second digit.
	 */
	private final Box secondDigitBox;



	/**
	 * Asks for position, the number to be shown and a factor resizing the whole.
	 * @param position  Position of the number
	 * @param number    Number to be shown
	 * @param resize    Resizing factor
	 */
	public Digit(Vector position, int number, double resize) {
		super(new Box(position, widthDigit*resize, heightDigit*resize), "digit." + number%10);

		if(number < 0 || number > 99)
			number = 99;
		
		if(number > 9) {
			setBox(new Box(position.sub(new Vector(-getBox().getWidth()*0.5, 0.0)), getBox().getWidth(), getBox().getHeight()));
			secondDigitBox = new Box(position.sub(new Vector(getBox().getWidth()*0.5, 0.0)), getBox().getWidth(), getBox().getHeight());
			secondDigit = "digit." + number/10;
		}
		else {
			secondDigit    = null;
			secondDigitBox = null;
		}
	}




	@Override
	public void draw(Input input, Output output) {
		super.draw(input, output);
		
		if(secondDigit != null)
			output.drawSprite(getSprite(secondDigit), secondDigitBox);
	}
}
