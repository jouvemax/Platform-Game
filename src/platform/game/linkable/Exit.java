package platform.game.linkable;

import platform.game.Actor;
import platform.game.Damage;
import platform.game.Linkable;
import platform.game.World;
import platform.game.level.Level;
import platform.game.signal.And;
import platform.game.signal.Constant;
import platform.game.signal.Signal;
import platform.util.Box;
import platform.util.Input;
import platform.util.Output;
import platform.util.Vector;

public class Exit extends Actor implements Linkable {

	private static final long serialVersionUID = 2082520112827747338L;

	/**
	 * Level to be loaded when the Exit is activated
	 */
	private Level level;

	/**
	 * Used to store the Signal
	 */
	private Signal signal;

	/**
	 * Used to store the temporary signal while the Actor is
	 * in linking process with other Signals.
	 */
	private Signal signalTemp;

	/**
	 * Signifies whether the Exit has to begin the linking process
	 */
	private boolean linkable;



	/**
	 * Full constructor
	 * @param box       Position and size parameters of the Exit.
	 * @param level		Level where which the Exit leads. If the level is null, create a default level
	 * @param signal	Signal linked to the Exit.
	 * @param linkable  Indicates whether the Exit needs to be linked or not.
	 */
	public Exit(Box box, Level level, Signal signal, boolean linkable) {
		super(box, "door.open");

		if(level != null)
			this.level  = level;
		else
			this.level  = Level.createDefaultLevel();

		if(signal != null)
			this.signal = signal;
		else
			this.signal = new Constant(false);

		signalTemp    = new Constant(true);
		this.linkable = linkable;
	}


	/**
	 * Constructor proposing a vector and a size instead of a Box
	 * @param position  Position of the Exit
	 * @param size      Size of the Exit
	 * @param level     Level where which the the Exit leads.
	 * @param signal    Signal linked to the Exit.
	 */
	public Exit(Vector position, double size, Level level, Signal signal) {
		this(new Box(position, size, size), level, signal);
	}


	/**
	 * Constructor where no signal is given (null is sent to the full constructor)
	 * @param box     Position and size parameters of the Exit.
	 * @param level   Level where which the the Exit leads.
	 * @param signal  Signal linked to the Exit.
	 */
	public Exit(Box box, Level level, Signal signal) {
		this(box, level, signal, false);
	}


	/**
	 *  Constructor where we do not tell whether it is linkable (then it is not linkable)
	 * @param box       Position and size parameters of the Exit.
	 * @param level     Level where which the the Exit leads.
	 * @param linkable  Indicates whether the Exit needs to be linked or not.
	 */
	public Exit(Box box, Level level, boolean linkable) {
		this(box, level, null, linkable);
	}



	@Override
	public void update(Input input) {
		super.update(input);
		
		// Begin the linking process by calling World.link(this) (cf. Simulator)
		if(linkable) {
			getWorld().link(this);
			linkable = false;
		}
	}


	@Override
	public void draw(Input input, Output output) {
		// If active, then the door is open
		if(signal.isActive())
			output.drawSprite(getSprite("door.open"), getBox());
		else
			output.drawSprite(getSprite("door.closed"), getBox());
	}


	@Override
	public boolean hurt(Actor instigator, Damage type, double amount, Vector location) {
		switch(type) {
		case ACTIVATION :
			// Change the level only if the player tells it to do,
			// is in front of it and the signal is active.
			if(signal.isActive() && amount > 0.0) {
				getWorld().setNextLevel(level);
				getWorld().nextLevel();
				return true;
			}
			return false;
		default:
			return super.hurt(instigator, type, amount, location);
		}
	}


	@Override
	protected int getPriority() {
		// Same level as a Block, so the Player is in front of it.
		return 0;
	}


	@Override
	public Actor copie() {
		// Create a new Exit ready for the linking process
		return new Exit(getBox(), level, true);
	}


	/**
	 * The following method has to be overridden to register
	 * all the signals it contains so it can be loaded in the
	 * new world and be visible.
	 */
	@Override
	public void register(World world) {
		super.register(world);
		if(signal instanceof Actor)
			world.register((Actor)signal);
		else
			signal.register(world);
	}
	
	
	/**
	 * The following method has to be overridden to unregister
	 * all the signals it contains so it can be saved.
	 */
	@Override
	public void unregister() {
		if(signal instanceof Actor)
			((Actor)signal).unregister();
		else
			signal.unregister(getWorld());
		super.unregister();
	}


	@Override
	public void addSignal(Signal signal) {
		signalTemp = new And(signal, signalTemp);
	}


	@Override
	public void endSignal() {
		signal = signalTemp;
		signalTemp = null;
	}

}
