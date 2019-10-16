package platform.game.level;

import java.util.ArrayList;

import platform.game.World;
import platform.game.button.ToNextLevel;
import platform.game.graphic.BackgroundImage;
import platform.game.graphic.Digit;
import platform.game.graphic.Image;
import platform.util.Box;
import platform.util.Vector;

/**
 * Allows the user to chose a Level in the standard game.
 */
public class LevelSelection extends Level {

	private static final long serialVersionUID = -5924303104069238151L;

	@Override
	public void register(World world) {
		super.register(world);

		// Register a new instance, to restart level automatically
		world.setNextLevel(new Menu());
		world.setView(Vector.ZERO, 8.0);
		world.register(new BackgroundImage("pixel.black"));
		world.register(new Image(
				new Box(new Vector(-6.0, 5.0), 1.5*4.0, 1.5*3.0)
				, "title"));

		ArrayList<Level> levels = new ArrayList<Level>();
		levels.add(new Level01());
		levels.add(new Level02());
		levels.add(new Level03());
		String nameSpriteOff = "level.done.off";
		String nameSpriteOn  = "level.done.on";
		
		Vector position = new Vector(8.0, 3.0);
		
		world.register(new Digit(position, 42, 0.2));
		world.register(new ToNextLevel(
				new Box(position, 1.5, 1.5)
				, nameSpriteOff
				, nameSpriteOn
				, new Level04()
				, World.keyboardCode(0)
				));


		for(int j = 0; j < 5; ++j)
			for(int i = 0 ; i<10 && i+10*j < levels.size(); ++i) {
				if(getLevelDone() > i) {
					nameSpriteOff = "level.done.off";
					nameSpriteOn  = "level.done.on";
				} else if(getLevelDone() == i) {
					nameSpriteOff = "box.empty";
					nameSpriteOn  = "box.double";
				} else {
					nameSpriteOff = "metal.1";
					nameSpriteOn  = "metal.1";
				}
				position = new Vector(-8.0+i*1.75, 1.0-j*1.75);
				world.register(new Digit(position, 1+i+10*j, 0.2));
				world.register(new ToNextLevel(
						new Box(position, 1.5, 1.5)
						, nameSpriteOff
						, nameSpriteOn
						, (getLevelDone()>=i)?levels.get(i):null
						, World.keyboardCode(i+1)));
			}
	}

}
