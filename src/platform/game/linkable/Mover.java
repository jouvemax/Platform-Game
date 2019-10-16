package platform.game.linkable;

import java.awt.event.KeyEvent;

import platform.game.Actor;
import platform.game.Linkable;
import platform.game.World;
import platform.game.block.Block;
import platform.game.signal.And;
import platform.game.signal.Constant;
import platform.game.signal.Oscillator;
import platform.game.signal.Signal;
import platform.util.Box;
import platform.util.Input;
import platform.util.Output;
import platform.util.Vector;

public class Mover extends Block implements Linkable {

	private static final long serialVersionUID = 6526755917273878338L;

	/**
	 * Vectors indicating where the mover should be when the signal is not active.
	 */
	private Vector off;

	/**
	 * Vectors indicating where the mover should be when the signal is active.
	 */
	private Vector on;

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
	 * Indicates the current position of the door:
	 * between 0 and 1, between on and off.
	 */
	private double current;

	/**
	 * Signifies whether the Mover is being placed by the user on the World.
	 */
	private boolean placeable;

	/**
	 * Signifies whether the Mover has to begin the linking process.
	 */
	private boolean linkable;

	/**
	 * Signifies whether the Mover awaits its official "on" position.
	 */
	private boolean moveable;



	/**
	 * Full constructor
	 * @param box       Position and size parameters of the Mover.
	 * @param sprite    Sprite of the Mover.
	 * @param on        Where the mover should be headed to after signal activation
	 * @param signal    Signal to be linked to the mover. If null, create a Constant.
	 * @param linkable  True if we'd like to link the door to its signal
	 */
	public Mover(Box box, String sprite, Vector on, Signal signal, boolean linkable) {
		super(box, sprite);
		this.off       = box.getCenter();
		this.on        = on;
		if(signal != null)
			this.signal = signal;
		else
			this.signal = new Constant(false);

		signalTemp     = new Constant(false);
		this.placeable = linkable;
		this.linkable  = false;
		this.moveable  = false;
	}


	/**
	 * Constructor where we do not tell whether it is linkable (then it is not linkable)
	 * @param box     Position and size parameters of the Mover.
	 * @param sprite  Sprite of the Mover.
	 * @param on      Where to mover should be headed to when the Signal is on.
	 * @param signal  Signal linked to the Mover.
	 */
	public Mover(Box box, String sprite, Vector on, Signal signal) {
		this(box, sprite, on, signal, false);
	}


	/**
	 * Constructor where no signal is given (null is sent to the full constructor)
	 * @param box       Position and size parameters of the Mover.
	 * @param sprite    Sprite of the Mover.
	 * @param on        Where to mover should be headed to when the Signal is on.
	 * @param linkable  Indicates whether the Mover needs to be linked or not.
	 */
	public Mover(Box box, String sprite, Vector on, boolean linkable) {
		this(box, sprite, on, null, linkable);
	}



	@Override
	public void update(Input input) {
		super.update(input);

		// If it's been placed, then update the on and off position and begin the linking process.
		if(placeable
				&& input.getMouseButton(1).isPressed()) {
			on  = getBox().getCenter();
			off = on;
			placeable = false;
			linkable = true;
		}

		// Begin the linking process
		if(linkable)
			getWorld().link(this);

		// Begin the "moving" process, where the user gives the final "on" position of the Mover
		if(moveable) {
			if(input.getKeyboardButton(KeyEvent.VK_S).isPressed())
				on = on.add(new Vector(0.0,-getBox().getHeight()/3.0));
			if(input.getKeyboardButton(KeyEvent.VK_D).isPressed())
				on = on.add(new Vector(getBox().getWidth()/3.0,0.0));
			if(input.getKeyboardButton(KeyEvent.VK_W).isPressed()
					|| input.getKeyboardButton(KeyEvent.VK_Z).isPressed())
				on = on.add(new Vector(0.0,getBox().getHeight()/3.0));
			if(input.getKeyboardButton(KeyEvent.VK_A).isPressed()
					|| input.getKeyboardButton(KeyEvent.VK_Q).isPressed())
				on = on.add(new Vector(-getBox().getWidth()/3.0,0.0));
			if(input.getKeyboardButton(KeyEvent.VK_ENTER).isPressed())
				moveable = false;
			if(input.getKeyboardButton(KeyEvent.VK_O).isPressed())
				signal = new Oscillator(3.0);
			if(input.getKeyboardButton(KeyEvent.VK_P).isPressed())
				signal = signalTemp;
		}	

		// If everything has been done, the operate normally.
		if(!placeable && !linkable && !moveable) {

			// Verifies if we arrived at destination.
			if (signal.isActive()) {
				current += input.getDeltaTime();
				if (current > 1.0)
					current = 1.0;
			} else {
				current -= input.getDeltaTime();
				if (current < 0.0)
					current = 0.0;
			}

			// Declares the Vector between on and off
			Vector change = on.sub(off);

			// The change has an interpolation of -2x^3 + 3x^2, as suggested in the pdf file.
			setBox(new Box(off.add(change.mul(-2*Math.pow(current,3)+3*Math.pow(current, 2)))
					, super.getBox().getWidth()
					, super.getBox().getHeight()));
		}
	}


	@Override
	public void draw(Input input, Output output) {

		// Draw the Mover according to its Box.
		output.drawSprite(getSprite(), getBox());

		// If we're in the process of moving it (giving it an "on" position,
		// then draw an arrow between the off and on positions to indicate
		// to the user where it's headed to.
		if(moveable) {
			Vector arrow = on.sub(off);
			output.drawSprite(getSprite("arrow"), new Box(off.add(arrow.mul(0.5)), arrow.getLength(), 1.0), arrow.getAngle());
		}
	}


	@Override
	public Actor copie() {
		// Create a new Mover ready for the linking process
		return new Mover(getBox(), getSpriteName(), getBox().getCenter(), true);
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
		linkable = false;
			
		// After having ended the linking process, begin the moving process.
		moveable = true;
	}
}
