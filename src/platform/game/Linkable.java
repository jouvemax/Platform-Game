package platform.game;

import platform.game.signal.Signal;

/**
 *  This interface is used to link an Actor to its corresponding Signals in
 *  the Level Building.
 */

public interface Linkable {
	
	/**
	 * Adds the signal to the Linkable.
	 * @param signal  Signal which needs to be added to the Linkable
	 */
	public void addSignal(Signal signal);
	
	
	/**
	 * Finalises the linking process by putting signalTemp in signal.
	 */
	public void endSignal();
}
