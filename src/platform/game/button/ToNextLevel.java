package platform.game.button;

import java.awt.event.KeyEvent;

import platform.game.level.Level;
import platform.util.Box;
import platform.util.Input;

/**
 *  LoadLevel is a Button that launches a specified Level.
 */
public class ToNextLevel extends Button {
	
	private static final long serialVersionUID = 78058326716380176L;
	
	/**
	 * The level to which it will redirect.
	 */
	private final Level level;

	
	
	/**
	 * Full constructor
	 * @param box        Position and size parameters of the ToNextLevel.
	 * @param spriteOff  Name of the Sprite when the mouse is off the ToNextLevel.
	 * @param spriteOn   Name of the Sprite when the mouse is on the ToNextLevel.
	 * @param level      Level which will be loaded if the user clicks on the ToNextLevel.
	 * @param shortcut   Shortcut of the Spawn.
	 * @see java.awt.event.KeyEvent
	 */
	public ToNextLevel(Box box, String spriteOff, String spriteOn, Level level, int shortcut) {
		super(box, spriteOff, spriteOn, shortcut);
		
		this.level     = level;
	}
	
	
	/**
	 * Constructor without any shortcut
	 * @param box        Position and size parameters of the ToNextLevel.
	 * @param spriteOff  Name of the Sprite when the mouse is off the ToNextLevel.
	 * @param spriteOn   Name of the Sprite when the mouse is on the ToNextLevel.
	 * @param level      Level which will be loaded if the user clicks on the ToNextLevel.
	 */
	public ToNextLevel(Box box, String spriteOff, String spriteOn, Level level) {
		this(box, spriteOff, spriteOn, level, KeyEvent.VK_UNDEFINED);
	}
	
	
	/**
	 * Constructor without a second Sprite
	 * @param box       Position and size parameters of the ToNextLevel.
	 * @param sprite    Sprite of the ToNextLevel.
	 * @param level     Level which will be loaded if the user clicks on the ToNextLevel.
	 * @param shortcut  Shortcut of the Spawn.
	 * @see java.awt.event.KeyEvent
	 */
	public ToNextLevel(Box box, String sprite, Level level, int shortcut) {
		this(box, sprite, sprite, level, shortcut);
	}
	
	
	/**
	 * Constructor without any shortcut nor a second Sprite
	 * @param box     Position and size parameters of the ToNextLevel.
	 * @param sprite  Sprite of the ToNextLevel.
	 * @param level   Level which will be loaded if the user clicks on the ToNextLevel.
	 */
	public ToNextLevel(Box box, String sprite, Level level) {
		this(box, sprite, sprite, level);
	}
	
	

	@Override
	public void update(Input input) {
		super.update(input);
		
		// If the shortcut is called or the user clicks on it, load next Level
		// The level must be non null!
		if(((getBox().isColliding(input.getMouseLocation())
				&& input.getMouseButton(1).isPressed())
				|| input.getKeyboardButton(getShortcut()).isPressed())
			&& level != null) {
			getWorld().setNextLevel(level);
			getWorld().nextLevel();
		}
	}	
}
