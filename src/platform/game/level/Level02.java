package platform.game.level;

import platform.game.World;

/**
 *  Second level of the standard game.
 */
public class Level02 extends Level {

	private static final long serialVersionUID = 4287227971578703621L;

	@Override
	public void register(World world) {
		super.register(world);

		// Mark the first level as done.
		updateLevelsDone(1);

		// Register a new instance, to restart level automatically
		world.setNextLevel(new Level02());
		if(!isDynamicSave())
		{
			setDynamicSave();
			world.load(-1);
		}
		
	}


}
