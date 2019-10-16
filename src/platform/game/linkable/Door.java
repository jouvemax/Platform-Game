package platform.game.linkable;

import platform.game.Actor;
import platform.game.Linkable;
import platform.game.World;
import platform.game.block.Block;
import platform.game.signal.And;
import platform.game.signal.Constant;
import platform.game.signal.Signal;
import platform.util.Box;
import platform.util.Input;
import platform.util.Output;

/**
 *  A Door is a block that disappears when then Signal it is linked to
 *  tells it to.
 */

public class Door extends Block implements Signal, Linkable {

	private static final long serialVersionUID = -7055335089538578186L;

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
	 * Signifies whether the Door has to begin the linking process
	 */
	private boolean linkable;



	/**
	 * Full constructor
	 * @param box       Position and size parameters of the Door.
	 * @param sprite    Sprite of the Door.
	 * @param signal    Signal to be linked to the door. If null, create a Constant.
	 * @param linkable  True if we'd like to link the door to its signal
	 */
	public Door(Box box, String sprite, Signal signal, boolean linkable) {
		super(box, sprite);

		if(signal != null)
			this.signal = signal;
		else
			this.signal = new Constant(false);

		signalTemp    = new Constant(true);
		this.linkable = linkable;
	}


	/**
	 * Constructor where no signal is given (null is sent to the full constructor)
	 * @param box       Position and size parameters of the Door.
	 * @param sprite    Sprite of the Door.
	 * @param linkable  Indicates whether the Door needs to be linked or not.
	 */
	public Door(Box box, String sprite, boolean linkable) {
		this(box, sprite, null, linkable);
	}


	/**
	 * Constructor where we do not tell whether it is linkable (then it is not linkable)
	 * @param box     Position and size parameters of the Door.
	 * @param sprite  Sprite of the Door.
	 * @param signal  Signal linked to the Door.
	 */
	public Door(Box box, String sprite, Signal signal) {
		this(box, sprite, signal, false);
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
		// Only draw it if the signal is not active
		if(!signal.isActive())
			super.draw(input, output);
	}


	@Override
	public Box getBox() {
		// Return the box iff the signal is not active
		if(signal.isActive())
			return null;
		return super.getBox();
	}


	@Override
	public boolean isSolid() {
		// The block is solid iff the signal is not active
		if(signal.isActive())
			return false;
		return super.isSolid();
	}


	@Override
	public boolean isActive() {
		// Door is a signal in itself, characterised by its signal.
		return signal.isActive();
	}


	@Override
	public Actor copie() {
		// Create a new Door ready for the linking process
		return new Door(getBox(), getSpriteName(), true);
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


	/**
	 * The following method has to be overridden to unregister
	 * all the signals it contains so it can be loaded in the
	 * new world and be visible.
	 */
	@Override
	public void addSignal(Signal signal) {
		signalTemp = new And(signal, signalTemp);
	}


	@Override
	public void endSignal() {
		signal = signalTemp;
		signalTemp = null;
	}


	/**
	 * No need for it, the Actor's unregister suffices.
	 */
	@Override
	public void unregister(World world) {}
}
