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
import platform.game.passive.Spike;
import platform.game.signal.And;
import platform.game.signal.Key;
import platform.game.signal.Lever;
import platform.game.signal.Not;
import platform.game.signal.Torch;
import platform.game.weapon.ArrowDispenser;
import platform.util.Box;
import platform.util.Vector;

public class Level03 extends Level {


	private static final long serialVersionUID = -2202709386961154628L;


	@Override
	public void register(World world) {
		super.register(world);

		// Mark the first level as done.
		updateLevelsDone(2);

		// Register a new instance, to restart level automatically
		world.setNextLevel(new Level03());

		if(!isDynamicSave())
		{
			setDynamicSave();
			world.register(new Limits(new Box(Vector.ZERO, 100.0, 100.0)));
			
			world.register(new BackgroundImage("blue.land"));

			world.register(new Player(new Vector (-5, 4)));

			world.register(new Block(
					new Box(new Vector (-5, 3), 5, 2)
					, "stone.broken.3"));

			world.register(new Block(
					new Box(new Vector (-5, 8), 5, 2)
					, "stone.broken.3"));

			world.register(new Block(
					new Box(new Vector (0, 8), 5, 2)
					, "stone.broken.3"));

			world.register(new Block(
					new Box(new Vector (1.5, 4.5), 2, 5)
					, "stone.broken.7"));

			world.register(new Block(
					new Box(new Vector (1.5, -0.5), 2, 5)
					, "stone.broken.7"));

			world.register(new Block(
					new Box(new Vector (-3.5, -0.5), 2, 5)
					, "stone.broken.7"));

			world.register(new Block(
					new Box(new Vector (-3.5, -5.5), 2, 5)
					, "stone.broken.7"));

			world.register(new Block(
					new Box(new Vector (1.5, -7.5), 2, 5)
					, "stone.broken.7"));

			world.register(new Block(
					new Box(new Vector (-2, -9), 5, 2)
					, "stone.broken.3"));

			world.register(new Block(
					new Box(new Vector (5, -7), 5, 2)
					, "stone.broken.3"));

			world.register(new Block(
					new Box(new Vector (5, -1), 5, 2)
					, "stone.broken.3"));

			world.register(new WoodenBlock(
					new Box(new Vector(8, -1), 1.0,1.0)));

			world.register(new WoodenBlock(
					new Box(new Vector(9, -1), 1.0,1.0)));

			world.register(new WoodenBlock(
					new Box(new Vector(10, -1), 1.0,1.0)));

			world.register(new WoodenBlock(
					new Box(new Vector(11, -1), 1.0,1.0)));

			world.register(new WoodenBlock(
					new Box(new Vector(8, -7), 1.0,1.0)));

			world.register(new WoodenBlock(
					new Box(new Vector(9, -7), 1.0,1.0)));

			world.register(new WoodenBlock(
					new Box(new Vector(10, -7), 1.0,1.0)));

			world.register(new WoodenBlock(
					new Box(new Vector(11, -7), 1.0,1.0)));

			world.register(new Block(
					new Box(new Vector (5, -1), 5, 2)
					, "stone.broken.3"));

			world.register(new Block(
					new Box(new Vector (14, -1), 5, 2)
					, "stone.broken.3"));

			world.register(new Block(
					new Box(new Vector (14, -7), 5, 2)
					, "stone.broken.3"));
			world.register(new Block(
					new Box(new Vector (19, -7), 5, 2)
					, "stone.broken.3"));

			world.register(new Block(
					new Box(new Vector (19, -1), 5, 2)
					, "stone.broken.3"));

			world.register(new Block(
					new Box(new Vector (21, -2.5), 1, 1)
					, "stone.broken.3"));

			world.register(new Block(
					new Box(new Vector (21, -5.5), 1, 1)
					, "stone.broken.3"));

			world.register(new WoodenBlock(
					new Box(new Vector(21, -3.5), 1.0,1.0)));
			world.register(new WoodenBlock(
					new Box(new Vector(21, -4.5), 1.0,1.0)));

			world.register(new Block(
					new Box(new Vector (1.5, 11.5), 2, 5)
					, "stone.broken.7"));

			world.register(new Block(
					new Box(new Vector (1.5, 16.5), 2, 6)
					, "stone.broken.7"));

			world.register(new Block(
					new Box(new Vector (20.5, 2.5), 2, 5)
					, "stone.broken.7"));
			world.register(new Block(
					new Box(new Vector (20.5, 7.5), 2, 5)
					, "stone.broken.7"));

			world.register(new Block(
					new Box(new Vector (20.5, 12.5), 2, 5)
					, "stone.broken.7"));
			world.register(new Block(
					new Box(new Vector (20.5, 17), 2, 5)
					, "stone.broken.7"));

			world.register(new Block(
					new Box(new Vector (17, 18.5), 5, 2)
					, "stone.broken.3"));

			world.register(new Block(
					new Box(new Vector (12, 18.5), 5, 2)
					, "stone.broken.3"));

			world.register(new Block(
					new Box(new Vector (7, 18.5), 5, 2)
					, "stone.broken.3"));

			world.register(new Block(
					new Box(new Vector (3, 18.5), 5, 2)
					, "stone.broken.3"));


			Torch torch1 = new Torch(
					new Box(new Vector(3, 17), 0.6, 0.6), true);
			world.register(torch1);



			world.register(new Block(
					new Box(new Vector (18.5, 15), 2, 1)
					, "stone.broken.3"));

			world.register(new Block(
					new Box(new Vector (16.5, 15), 2, 1)
					, "stone.broken.3"));


			world.register(new Door(
					new Box(new Vector(16.5, 16.5), 2.0, 2.0)
					, "lock.yellow"
					,new Not(torch1)));

			for(int i=1; i < 6; ++i) {
				world.register(new Spike(
						new Box(new Vector(2+i,0.25), 1.0, 0.5)));
			}

			for(int i=1; i < 9; ++i) {
				world.register(new Spike(
						new Box(new Vector(11+i,0.25), 1.0, 0.5)));
			}

			for(int i=1; i < 4; ++i) {
				world.register(new Spike(
						new Box(new Vector(-3.05+i,-7.7), 1.0, 0.5)));
			}

			Key key1 = new Key(
					new Box(new Vector(18.5, 16.5), 0.3, 0.3));
			world.register(key1);

			world.register(new Block(
					new Box(new Vector (24, -1), 5, 2)
					, "stone.broken.3"));

			world.register(new Block(
					new Box(new Vector (27.5, -2.25), 2, 5)
					, "stone.broken.7"));

			world.register(new Block(
					new Box(new Vector (27.5, -7.25), 2, 5)
					, "stone.broken.7"));

			world.register(new Block(
					new Box(new Vector (27.5, -12.25), 2, 5)
					, "stone.broken.7"));

			world.register(new Block(
					new Box(new Vector (20.5, -10.5), 2, 5)
					, "stone.broken.7"));

			world.register(new Block(
					new Box(new Vector (20.5, -15.5), 2, 5)
					, "stone.broken.7"));

			world.register(new Block(
					new Box(new Vector (20.5, -20.5), 2, 5)
					, "stone.broken.7"));

			world.register(new Block(
					new Box(new Vector (27.5, -20.5), 2, 5)
					, "stone.broken.7"));

			world.register(new Block(
					new Box(new Vector (24, -24), 5, 2)
					, "stone.broken.3"));

			world.register(new Block(
					new Box(new Vector (31, -19), 5, 2)
					, "stone.broken.3"));

			world.register(new Block(
					new Box(new Vector (36, -19), 5, 2)
					, "stone.broken.3"));

			world.register(new Block(
					new Box(new Vector (31, -13.75), 5, 2)
					, "stone.broken.3"));

			world.register(new Block(
					new Box(new Vector (36, -13.75), 5, 2)
					, "stone.broken.3"));

			world.register(new Block(
					new Box(new Vector (38, -16.40), 1, 3.5)
					, "stone.broken.7"));

			Lever lever1 = new Lever(
					new Vector (22, -22.6));
			world.register(lever1);

			world.register(new Door(
					new Box(new Vector(32, -16.5), 3.2, 3.2)
					, "lock.blue"
					, lever1));

			Key key2 = new Key(
					new Box(new Vector(36, -16.5), 0.3, 0.3), "key.blue");
			world.register(key2);

			for(int i=1; i < 5; ++i) {
				world.register(new Spike(
						new Box(new Vector(22.2+i,-22.6), 1.0, 0.8)));
			}

			world.register(new Block(
					new Box(new Vector (25, -20), 1, 1)
					, "stone.1"));
			world.register(new Block(
					new Box(new Vector (24, -20), 1, 1)
					, "stone.1"));
			world.register(new Block(
					new Box(new Vector (23, -20), 1, 1)
					, "stone.1"));

			world.register(new Block(
					new Box(new Vector (22, -20), 1, 1)
					, "stone.1"));

			for(int i=1; i < 5; ++i) {
				world.register(new Spike(
						new Box(new Vector(21+i,-19.2), 1.0, 0.8)));
			}

			world.register(new Block(
					new Box(new Vector (25, -7), 1, 1)
					, "stone.1"));
			world.register(new Block(
					new Box(new Vector (24, -7), 1, 1)
					, "stone.1"));
			world.register(new Block(
					new Box(new Vector (23, -7), 1, 1)
					, "stone.1"));

			world.register(new Block(
					new Box(new Vector (22, -7), 1, 1)
					, "stone.1"));

			for(int i=1; i < 5; ++i) {
				world.register(new Spike(
						new Box(new Vector(21+i,-6.2), 1.0, 0.8)));
			}

			world.register(new Block(
					new Box(new Vector (25, -10), 1, 1)
					, "stone.1"));
			world.register(new Block(
					new Box(new Vector (24, -10), 1, 1)
					, "stone.1"));
			world.register(new Block(
					new Box(new Vector (23, -10), 1, 1)
					, "stone.1"));

			world.register(new Block(
					new Box(new Vector (26, -10), 1, 1)
					, "stone.1"));

			for(int i=1; i < 5; ++i) {
				world.register(new Spike(
						new Box(new Vector(22+i,-9.2), 1.0, 0.8)));
			}

			world.register(new Block(
					new Box(new Vector (25, -13), 1, 1)
					, "stone.1"));
			world.register(new Block(
					new Box(new Vector (24, -13), 1, 1)
					, "stone.1"));
			world.register(new Block(
					new Box(new Vector (23, -13), 1, 1)
					, "stone.1"));

			world.register(new Block(
					new Box(new Vector (22, -13), 1, 1)
					, "stone.1"));

			for(int i=1; i < 5; ++i) {
				world.register(new Spike(
						new Box(new Vector(21+i,-12.2), 1.0, 0.8)));
			}

			world.register(new Block(
					new Box(new Vector (8.5, 5.5), 4.5, 1)
					, "stone.broken.3"));

			world.register(new Block(
					new Box(new Vector (4, 5.5), 4.5, 1)
					, "stone.broken.3"));

			//			world.register(new Block(
			//					new Box(new Vector (13, 7.5), 4.5, 1)
			//					, "stone.broken.3"));
			Torch torch2 = new Torch(
					new Box(new Vector(3, 4.5), 0.6, 0.6), true);
			world.register(torch2);

			Torch torch3 = new Torch(
					new Box(new Vector(6, 1), 0.6, 0.6), true);
			world.register(torch3);

			Torch torch4 = new Torch(
					new Box(new Vector(16, 3), 0.6, 0.6), true);
			world.register(torch4);

			world.register(new Mover(
					new Box(new Vector(13, 5.5), 4.5,1)
					, "stone.3"
					, new Vector(13, 8.5), new And( new Not(torch2), new And(new Not(torch3), new Not(torch4)))));

			world.register(new Block(
					new Box(new Vector (17.5, 5.5), 4.5, 1)
					, "stone.broken.3"));

			world.register(new Block(
					new Box(new Vector (10.2, 6.5), 1, 1)
					, "stone.broken.3"));

			world.register(new Block(
					new Box(new Vector (10.2, 7.5), 1, 1)
					, "stone.broken.3"));

			world.register(new Block(
					new Box(new Vector (10.2, 8.5), 1, 1)
					, "stone.broken.3"));

			for (int i = 0; i <17; ++i){
				world.register(new WoodenBlock(new Box(new Vector (3+i, -9), 1, 1)));
			}

			for (int i = 0; i <15; ++i){
				world.register(new WoodenBlock(new Box(new Vector (4+i, -10), 1, 1)));
			}

			for (int i = 0; i <13; ++i){
				world.register(new WoodenBlock(new Box(new Vector (5+i, -11), 1, 1)));
			}

			for (int i = 0; i <11; ++i){
				world.register(new WoodenBlock(new Box(new Vector (6+i, -12), 1, 1)));
			}
			for (int i = 0; i <9; ++i){
				world.register(new WoodenBlock(new Box(new Vector (7+i, -13), 1, 1)));
			}
			for (int i = 0; i <7; ++i){
				world.register(new WoodenBlock(new Box(new Vector (8+i, -14), 1, 1)));
			}

			for (int i = 0; i <5; ++i){
				world.register(new WoodenBlock(new Box(new Vector (9+i, -14), 1, 1)));
			}

			for (int i = 0; i <5; ++i){
				world.register(new WoodenBlock(new Box(new Vector (9+i, -15), 1, 1)));
			}
			for (int i = 0; i <8; ++i){
				world.register(new Block(new Box(new Vector (2+i, -9-i), 1, 1), "stone.broken.1"));
			}
			for (int i = 0; i <8; ++i){
				world.register(new Block(new Box(new Vector (20-i, -9-i), 1, 1), "stone.broken.1"));
			}

			world.register(new Block(new Box(new Vector (11, -30), 10, 1), "stone.3"));

			for(int i=1; i < 4; ++i) {
				world.register(new Spike(
						new Box(new Vector(7+i,-19), 1.0, 0.8)));
			}

			for(int i=1; i < 4; ++i) {
				world.register(new Spike(
						new Box(new Vector(11+i,-23), 1.0, 0.8)));
			}

			for(int i=1; i < 4; ++i) {
				world.register(new Spike(
						new Box(new Vector(7+i,-27), 1.0, 0.8)));
			}

			world.register(new Exit(new Box(new Vector(15, -28.5), 2, 2), new Level04(), new And(key1, key2)));

			world.register(new Block(new Box(new Vector (18, 10), 1, 6), "stone.broken.7"));

			world.register(new Block(new Box(new Vector (16, 14), 1, 2), "stone.broken.7"));

			world.register(new Block(new Box(new Vector (12, 13.4), 1, 5), "stone.broken.7"));

			world.register(new Block(new Box(new Vector (14.5, 11.5), 6, 1), "stone.broken.3"));

			world.register(new Block(new Box(new Vector (14, 16.5), 1, 3), "stone.broken.7"));

			world.register(new Block(new Box(new Vector (10, 12.4), 1, 4.5), "stone.broken.7"));

			world.register(new Block(new Box(new Vector (10.5, 15.8), 6, 1), "stone.broken.3"));

			world.register(new Block(new Box(new Vector (8, 14), 5, 1), "stone.broken.3"));

			world.register(new Block(new Box(new Vector (6, 16), 1, 4.5), "stone.broken.7"));

			world.register(new Block(new Box(new Vector (6, 7.5), 5, 1), "stone.broken.3"));

			world.register(new Block(new Box(new Vector (4, 11), 1, 6), "stone.broken.7"));

			world.register(new Block(new Box(new Vector (3, 13.5), 1, 1), "stone.broken.3"));

			world.register(new Block(new Box(new Vector (7, 9.5), 3, 1), "stone.broken.3"));

			world.register(new Block(new Box(new Vector (6, 12), 4, 1), "stone.broken.3"));

			world.register(new Block(new Box(new Vector (9, 10), 1, 1), "stone.broken.3"));

			world.register(new Heart(new Vector(13, 17)));

			world.register(new Heart(new Vector(13, 14.5)));

			world.register(new Heart(new Vector(3, 16)));

			world.register(new ArrowDispenser(
					new Box(new Vector(3, 12.5), 1.0, 1.0)));

			world.register(new ArrowDispenser(
					new Box(new Vector(3, 14.5), 1.0, 1.0)));

			world.register(new ArrowDispenser(
					new Box(new Vector(13.1, 12.5), 1.0, 1.0)));

			for(int i=1; i < 4; ++i) {
				world.register(new Spike(
						new Box(new Vector(7.5+i,16.5), 1.0, 0.5)));
			}

			for(int i=1; i < 3; ++i) {
				world.register(new Spike(
						new Box(new Vector(5.5+i, 10.2), 1.0, 0.5)));
			}

			for(int i=1; i < 4; ++i) {
				world.register(new Spike(
						new Box(new Vector(11.5+i, 9.2), 1.0, 0.5)));
			}
		}
	}
}
