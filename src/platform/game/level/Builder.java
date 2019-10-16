package platform.game.level;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import platform.game.Actor;
import platform.game.Eraser;
import platform.game.World;
import platform.game.button.Spawn;
import platform.game.button.SuperSpawner;
import platform.game.character.Player;
import platform.game.graphic.BackgroundImage;
import platform.game.linkable.Mover;
import platform.game.misc.Limits;
import platform.game.signal.Constant;
import platform.util.Box;
import platform.util.Vector;

/**
 * Level which allows the user to Build its own level via Serialization.
 */
public class Builder extends Level {

	private static final long serialVersionUID = -203385837032827128L;

	@Override
	public void register(World world) {
		super.register(world);

		// Register a new instance of the same level to build ad infinitum.
		world.setNextLevel(new Builder());

		if(!isDynamicSave()) {
			setDynamicSave();
			// Set the view to the default parameters (they're used in SuperSpawner
			// and Spawn to align themselves to the output).
			world.setView(World.defaultCenter, World.defaultRadius);

			// Register the Background Image
			world.register(new BackgroundImage("blue.land"));

			// Register the Limits to a generous amount so the user can build as big as he dreams of
			world.register(new Limits(new Box(Vector.ZERO, 100.0, 100.0)));

			// TODO put it in SuperSpawner directly!!
			// Create an array full of the standards SuperSpawners, aligned so they make up a global menu
			ArrayList<Actor> toSpawn = new ArrayList<Actor>();
			toSpawn.add(SuperSpawner.HZ_BLOCKS(world, new Box(new Vector(-9.0, 7.0), 1.0, 0.5)));
			toSpawn.add(SuperSpawner.VT_BLOCKS(world, new Box(new Vector(-10.5, 5.5), 0.5, 1.0)));
			toSpawn.add(SuperSpawner.SQ_BLOCKS(world, new Box(new Vector(-10.5, 4.0), 1.0, 1.0)));
			toSpawn.add(SuperSpawner.SIGNALS(world, new Box(new Vector(-10.5, 2.5), 1.0, 1.0)));
			toSpawn.add(SuperSpawner.UNLOCKABLE(world, new Box(new Vector(-10.5, 1.0), 1.0, 1.0)));
			toSpawn.add(SuperSpawner.ASSETS(world, new Box(new Vector(-10.5, -0.5), 1.0, 1.0)));		


			// Create the Spawn Eraser, which does not need a SuperSpawner
			Eraser eraser = new Eraser(new Box(new Vector(-10.5, -6.75), 1.0, 1.0));
			Spawn spawnEraser = new Spawn(eraser.getBox()
					, "cross"
					, eraser
					, KeyEvent.VK_DELETE
					);

			// Create the Spawn Mover
			Box moverBox = new Box(new Vector(-10.5, -2.0), 1.0, 1.0);
			Mover mover = new Mover(moverBox, "stone.4", moverBox.getCenter(), new Constant(true));
			Spawn spawnMover = new Spawn(moverBox
					, "mover"
					, mover
					);

			toSpawn.add(spawnEraser);
			toSpawn.add(spawnMover);

			// Register all Spawns
			for(int i = 0; i<toSpawn.size();++i)
				world.register(toSpawn.get(i));

			// Register the Spawn Player which contain all other Spawns.
			Player player = new Player(new Vector(-10.5, 7.0));

			world.register(new Spawn(player.getBox()
					, "blocker.sad"
					, "blocker.happy"
					, player
					, toSpawn
					, KeyEvent.VK_P));

			// TODO dynamic change of the Background!
		}

	}
}
