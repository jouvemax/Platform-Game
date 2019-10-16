package platform.game.level;


import platform.game.World;
import platform.game.block.Block;
import platform.game.block.WoodenBlock;
import platform.game.character.Player;
import platform.game.graphic.BackgroundImage;
import platform.game.item.Heart;
import platform.game.linkable.Door;
import platform.game.linkable.Exit;
import platform.game.linkable.Mover;
import platform.game.misc.Limits;
import platform.game.passive.Jumper;
import platform.game.passive.Spike;
import platform.game.signal.And;
import platform.game.signal.Key;
import platform.game.signal.Not;
import platform.game.signal.Torch;
import platform.game.weapon.ArrowDispenser;
import platform.util.Box;
import platform.util.Vector;

public class Level01 extends Level {

	private static final long serialVersionUID = 333635773307693809L;

	@Override
	public void register(World world) {
		super.register(world);

		// Register a new instance, to restart level automatically
		world.setNextLevel(new Level01());
		if(!isDynamicSave())
		{
			setDynamicSave();
			world.register(new Limits(new Box(Vector.ZERO, 40.0, 20.0)));
			world.register(new Player(new Vector(-1.0, 0.0), Vector.ZERO, 5.0));
			world.register(new BackgroundImage("blue.land"));

			// GRAND SOL
			world.register(new Block(
					new Box(new Vector(-5.0,-1.5), 10.0, 3.0)
					, "stone.broken.3"));

			// PETIT SOL
			world.register(new Block(
					new Box(new Vector(2.5,-1.5), 3.0, 3.0)
					, "stone.broken.4"));

			// PETIT PLAFOND
			world.register(new Block(
					new Box(new Vector(-0.5,3.5), 5.0, 1.0)
					, "stone.broken.3"));

			// GRAND PLAFOND
			world.register(new Block(
					new Box(new Vector(0.0,8.0), 10.0, 2.0)
					, "stone.broken.3"));

			// COEUR
			world.register(new Heart(new Vector(-3.0,6.5)));

			// MUR SUR TRAPPE
			world.register(new Block(
					new Box(new Vector(-5.5,5.5), 3.0, 3.0)
					, "stone.broken.4"));

			// PLAFOND SUR PORTE
			world.register(new Block(
					new Box(new Vector(-8.5,4.5), 3.0, 3.0)
					, "stone.broken.4"));

			// MUR À DROITE
			world.register(new Block(
					new Box(new Vector(5.5,2.5), 3.0, 9.0)
					, "stone.broken.7"));

			// PETIT MUR SUR PORTE
			world.register(new Block(
					new Box(new Vector(-6.5,2.0), 1.0, 2.0)
					, "stone.broken.8"));

			// JUMPER START
			world.register(new Jumper(
					new Box(new Vector(-3.5, 0.5), 1.0, 1.0)));

			// LOWER SPIKES
			for(int i=1; i < 3; ++i) {
				world.register(new Spike(
						new Box(new Vector(1.5+i,0.25), 1.0, 0.5)));
			}		

			// UPPER SPIKES
			world.register(new Spike(
					new Box(new Vector(-2.5, 4.25), 1.0, 0.5)));

			// YELLOW KEY
			Key key = new Key(
					new Box(new Vector(-1.25, 4.75), 0.3, 0.3));
			world.register(key);

			// UPPER TORCH
			Torch torchU = new Torch(
					new Box(new Vector(-1.75, 6.25), 0.6, 0.6), true);
			world.register(torchU);

			// LOWER TORCH
			Torch torchL = new Torch(
					new Box(new Vector(2.75, 3.25), 0.6, 0.6), true);
			world.register(torchL);

			// MOVER START
			world.register(new Mover(
					new Box(new Vector(-5.5, 3.5), 3.0,1.0)
					, "stone.3"
					, new Vector(-4.5, 3.5), key));

			// MOVER END
			world.register(new Mover(
					new Box(new Vector(0.5, 1.0), 1.0, 4.0)
					, "stone.7"
					, new Vector(0.5, -2.0), new And(new Not(torchU), new Not(torchL))));

			// DOOR
			world.register(new Door(
					new Box(new Vector(-6.5, 0.5), 1.0, 1.0)
					, "lock.yellow"
					, key));

			// WOODEN BLOCK
			world.register(new WoodenBlock(
					new Box(new Vector(-3.5, 3.5), 1.0,1.0)));

			// ARROW DISPENSER
			world.register(new ArrowDispenser(
					new Box(new Vector(1.5, 4.5), 1.0, 1.0)));

			// EXIT
			world.register(new Exit(
					new Box(new Vector(-8.5, 0.5), 1.0, 1.0)
					, new Level02(), torchL));
		}
	} 
}

