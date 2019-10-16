package platform.game.level;

import java.awt.event.KeyEvent;
import java.io.File;

import platform.game.World;
import platform.game.button.LoadLevel;
import platform.game.graphic.BackgroundImage;
import platform.game.graphic.Digit;
import platform.game.graphic.Image;
import platform.util.Box;
import platform.util.Vector;


/**
 *  Level which allows the user to load a previously created Level in Builder.
 */
public class Loading extends Level {

	private static final long serialVersionUID = -6875333864653328996L;

	@Override
	public void register(World world) {
		super.register(world);

		// Register a new instance, to restart level automatically
		world.setNextLevel(new Loading());
		world.setView(Vector.ZERO, 8.0);
		world.register(new BackgroundImage("pixel.black"));
		world.register(new Image(
				new Box(new Vector(-6.0, 5.0), 1.5*4.0, 1.5*3.0)
				, "title"));

		String nameSpriteOff = "box.empty";
		String nameSpriteOn  = "box.double";

		String path = World.pathWorld;
		int folder = 1;
		while(new File(path + String.format("%03d", folder)+"/").exists()) {
			++folder;
		}

		for(int j = 0; j < 5; ++j) {
			for(int i = 0 ; i<10 && 1+i+j*10 < folder ; ++i) {
				Vector position = new Vector(-8.0+i*1.75, 1.0-j*1.75);
				world.register(new Digit(position, 1+i+10*j, 0.2));
				world.register(new LoadLevel(
						new Box(position, 1.5, 1.5)
						, nameSpriteOff
						, nameSpriteOn
						, 1+i+10*j
						, (1+i+10*j < 10)?World.keyboardCode(i+1):KeyEvent.VK_UNDEFINED));
			}
		}
	}


}
