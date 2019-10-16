package platform.game.button;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import platform.game.World;
import platform.util.Box;
import platform.util.Input;


/**
 *  LoadLevel is a Button that loads a specified Level.
 */
public class LoadLevel extends Button {

	private static final long serialVersionUID = 1358511439561010673L;

	/**
	 * Level specified, integer used for: Simulator.load(int)
	 */
	private int levelSelected;


	
	/**
	 * Full constructor.
	 * @param box            Position and size parameters of the LoadLevel.
	 * @param spriteOff      Sprite of the Button when the mouse is not on it.
	 * @param spriteOn       Sprite of the Button when the mouse is on it.
	 * @param levelSelected  Level to which the user will be redirected.
	 * @param shortcut       Shortcut of the LoadLevel.
	 * @see java.awt.event.KeyEvent
	 */
	public LoadLevel(Box box, String spriteOff, String spriteOn, int levelSelected, int shortcut) {
		super(box, spriteOff, spriteOn, shortcut);

		// The level cannot be outside this range
		if(levelSelected < 0 || levelSelected > 999)
			throw new NullPointerException();

		this.levelSelected = levelSelected;
	}


	/**
	 * Constructor without any shortcut.
	 * @param box            Position and size parameters of the LoadLevel.
	 * @param spriteOff      Sprite of the Button when the mouse is not on it.
	 * @param spriteOn       Sprite of the Button when the mouse is on it.
	 * @param levelSelected  Level to which the user will be redirected.
	 */
	public LoadLevel(Box box, String spriteOff, String spriteOn, int levelSelected) {
		this(box, spriteOff, spriteOn, levelSelected, KeyEvent.VK_UNDEFINED);
	}


	/**
	 * Constructor without a second Sprite.
	 * @param box            Position and size parameters of the LoadLevel.
	 * @param sprite         Sprite of the LoadLevel.
	 * @param levelSelected  Level to which the user will be redirected.
	 * @param shortcut       Shortcut of the LoadLevel.
	 * @see java.awt.event.KeyEvent
	 */
	public LoadLevel(Box box, String sprite, int levelSelected, int shortcut) {
		this(box, sprite, sprite, levelSelected, shortcut);
	}


	/**
	 * Constructor without a second Sprite nor a shortcut
	 * @param box            Position and size parameters of the LoadLevel.
	 * @param sprite         Sprite of the LoadLevel.
	 * @param levelSelected  Level to which the user will be redirected.
	 */
	public LoadLevel(Box box, String sprite, int levelSelected) {
		this(box, sprite, sprite, levelSelected);
	}


	
	@Override
	public void update(Input input) {
		super.update(input);

		// If the user left-clicks on it or use the associated shortcut,
		// load the selected level.
		if((getBox().isColliding(input.getMouseLocation())
				&& input.getMouseButton(1).isPressed())
				|| input.getKeyboardButton(getShortcut()).isPressed())
			getWorld().load(levelSelected);

		// If the user right-clicks on it, then delete the corresponding save
		// and reorganise all saves so they are all in ascending and consecutive order
		if(getBox().isColliding(input.getMouseLocation())
				&& input.getMouseButton(3).isPressed()) {

			// Instantiate all used paths
			String path         = World.pathSave;
			String pathWorld    = World.pathWorld;
			String pathToDelete = pathWorld + String.format("%03d", levelSelected)+"/";

			// Delete the folder
			try{
				File folder = new File(pathToDelete);
				World.delete(folder);
			}
			catch(IOException e){
				e.printStackTrace();
				System.exit(0);
			}

			// Then, we desire to rearrange the name of the folders
			// We have to count them,...
			int numberFiles = new File(path).list().length;

			// Then see when the cut is effective
			int folder = 1;
			while(new File(pathWorld + String.format("%03d", folder)+"/").exists()) {
				++folder;
			}

			// Finally, rename all subsequent folders
			// If the code is used as intended and no one touches
			// the folders manually, then it should do the job
			for(int i = folder; i < numberFiles+1;++i) {
				File oldFile = new File(pathWorld + String.format("%03d", i+1)+"/");
				File newFile = new File(pathWorld + String.format("%03d", i)+"/");

				oldFile.renameTo(newFile);

				System.out.println(oldFile +" renamed to " + newFile);
			}

			// Reload the world
			getWorld().nextLevel();
			
			// It should exclusively be used in level.Loading, which calls itself by default after closing.
		}
	}
}
