package platform.game.level;

import platform.game.World;

public class Level04 extends Level {

	
	private static final long serialVersionUID = 7845329794211509620L;

	@Override
	public void register(World world) {
		super.register(world);

		// Mark the first level as done.
		updateLevelsDone(3);

		// Register a new instance, to restart level automatically
		world.setNextLevel(new LevelSelection());
		world.nextLevel();
	}
}
