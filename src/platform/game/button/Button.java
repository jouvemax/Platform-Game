package platform.game.button;

import java.awt.event.KeyEvent;

import platform.game.World;
import platform.game.block.Block;
import platform.util.Box;
import platform.util.Input;

/**
 *  A button is a block that changes appearance when hovered
 *  by the mouse and is mapped to a specific shortcut.
 */
public class Button extends Block {

	private static final long serialVersionUID = 5592503136839713726L;

	
	
	/**
	 * Name of the Sprite when the mouse isn't on the button.
	 */
	private final String spriteNameOff;
	
	
	/**
	 * Name of the Sprite when the mouse is on the button.
	 */
	private final String spriteNameOn;

	
	/**
	 * Value of the shortcut, given by KeyEvent.
	 */
	private final int shortcut;



	/**
	 * Full constructor for the Button
	 * @param box        Position and size parameters of the Button.
	 * @param spriteOff  Sprite of the Button when the mouse is not on it.
	 * @param spriteOn   Sprite of the Button when the mouse is on it.
	 * @param shortcut   Shortcut of the Button.
	 * @see java.awt.event.KeyEvent
	 */
	public Button(Box box, String spriteOff, String spriteOn, int shortcut) {
		super(box, spriteOff);
		this.spriteNameOff = spriteOff;
		this.spriteNameOn  = spriteOn;
		this.shortcut      = shortcut;

	}


	/**
	 * Constructor without a second Sprite
	 * @param box       Position and size parameters of the Button.
	 * @param sprite    Sprite of the Button
	 * @param shortcut  Shortcut of the Button.
	 * @see java.awt.event.KeyEvent
	 */
	public Button(Box box, String sprite, int shortcut) {
		this(box, sprite, sprite, shortcut);
	}


	/**
	 * Constructor without a shortcut
	 * @param box        Position and size parameters of the Button.
	 * @param spriteOff  Sprite of the Button when the mouse is not on it.
	 * @param spriteOn   Sprite of the Button when the mouse is on it.
	 */
	public Button(Box box, String spriteOff, String spriteOn) {
		this(box, spriteOff, spriteOn, KeyEvent.VK_UNDEFINED);

	}


	/**
	 * The button can also be considered like a simple Block.
	 * @param box     Position and size parameters of the Button.
	 * @param sprite  Sprite of the Button.
	 */
	public Button(Box box, String sprite) {
		this(box, sprite, sprite);
	}


	
	@Override
	public void update(Input input) {
		super.update(input);

		// Detects if the mouse is over the button
		if(getBox().isColliding(input.getMouseLocation())) {
			if(getSpriteName() != spriteNameOn)
				setSpriteName(spriteNameOn);
		} else if(getSpriteName() != spriteNameOff)
			setSpriteName(spriteNameOff);

		// The super class will handle the drawing.
	}


	@Override
	protected int getPriority() {
		// Very high priority so nothing tangible
		// interacts with it.
		return 100000;
	}


	@Override
	public boolean isSolid() {
		// A button is a block that is not solid
		return false;
	}


	/**
	 * @return  the shortcut associated to the button
	 */
	public int getShortcut() {
		return shortcut;
	}

	
	/**
	 * Gives a Box that follows the View. Considers zooming and dezooming. Uses world.getRadius() and world.getCenter().
	 * @param box   Gives the starting box of the 
	 * @return new  box to be set for the Actor
	 * @see Spawn
	 * @see SuperSpawner
	 */
	protected Box followView(Box box) {
		// Sets a new box that has followed the movement of the View
		// using the default values of the World
		double ratio = getWorld().getRadius() / World.defaultRadius;
		
		return new Box(
				box.getCenter().mul(ratio).add(getWorld().getCenter()).add(World.defaultCenter)
				, box.getWidth()*ratio
				, box.getHeight()*ratio);
	}

}
