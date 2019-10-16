package platform.game.level;

import java.awt.event.KeyEvent;
import java.io.File;

import platform.game.World;
import platform.game.button.LoadLevel;
import platform.game.button.ToNextLevel;
import platform.game.graphic.BackgroundImage;
import platform.game.graphic.Image;
import platform.util.Box;
import platform.util.Vector;

/**
 *  First level of the game, allows the user to chose another Level.
 */
public class Menu extends Level {

	private static final long serialVersionUID = 3046539362747284167L;

	@Override
	public void register(World world) {
		super.register(world);

		world.setNextLevel(new Menu());

		world.setView(Vector.ZERO, 10.0);

		world.register(new BackgroundImage("pixel.black"));
		world.register(new Image(
				new Box(new Vector(0.0, 5.5), 3*4.0, 3*3.0)
				, "title"));

		// Button to the LevelSelection
		world.register(new ToNextLevel(
				new Box(new Vector(0.0, 0.0), 6.0, 2.0)
				, "play.on"
				, "play.off"
				, new LevelSelection()
				, World.keyboardCode(1)));

		// Button to the selection of loadable levels
		world.register(new ToNextLevel(
				new Box(new Vector(0.0, -3.0), 6.0, 2.0)
				, "load.on"
				, "load.off"
				, new Loading()
				, World.keyboardCode(2)));

		// Button to the Builder level
		world.register(new ToNextLevel(
				new Box(new Vector(0.0, -6.0), 6.0, 2.0)
				, "build.on"
				, "build.off"
				, new Builder()
				, World.keyboardCode(3)));

		// Button to the last dynamic save
		if(new File(World.pathWorld + String.format("%03d", 0)+"/").exists())
			world.register(new LoadLevel(
					new Box(new Vector(-8.0, -1.5), 6.0, 2.0)
					, "cont.off"
					, "cont.on"
					, 0
					, World.keyboardCode(4)));
	} 
}

