package platform.game;

import java.util.ArrayList;

import platform.game.character.Player;
import platform.game.graphic.SelectGravity;
import platform.util.Box;
import platform.util.Input;
import platform.util.Output;
import platform.util.Vector;

/**
 * Overlay prompts specific informations about the Player,
 * also adds the possibility to change the gravity through SelectGravity
 */

public class Overlay extends Actor {

	private static final long serialVersionUID = 4903339368927439893L;


	/**
	 * Player associated with the Overlay
	 */
	private Player player;


	/**
	 * SelectGravity associated with the Overlay
	 */
	private SelectGravity selector;


	/**
	 * Standard size of the little elements, set to 0.15
	 */
	private static final double size = 0.15;


	/**
	 * Names of the empty Sprites.
	 */
	private ArrayList<String> namesEmpty;


	/**
	 * Names of the half-full Sprites.
	 */
	private ArrayList<String> namesHalf;


	/**
	 * Names of the full Sprites.
	 */
	private ArrayList<String> namesFull;


	/**
	 * Default Vector of the Overlay. Points from the center of the Player to the leftmost, downmost element of the Overlay.
	 */
	private Vector baseVector;


	/**
	 * Default horizontal Vector. From one element to another of the same type.
	 */
	private Vector horizVector;


	/**
	 * Default vertical Vector. From on element to an element of another type.
	 */
	private Vector vertVector;
	
	/**
	 * Initial angle of gravity, given to SelectGravity
	 */
	private double gravityAngle;


	/**
	 * Full constructor, called by Player.
	 * @param player  Player linked to the Overlay.
	 */
	public Overlay(Player player, double gravityAngle) {
		super(new Box(player.getBox().getCenter(),
				size, size));

		this.player       = player;
		this.gravityAngle = gravityAngle;

		this.namesEmpty   = new ArrayList<String>();
		this.namesHalf    = new ArrayList<String>();
		this.namesFull    = new ArrayList<String>();

		this.namesEmpty.add("heart.empty");
		this.namesHalf.add("heart.half");
		this.namesFull.add("heart.full");

		this.namesEmpty.add("circle.empty");
		this.namesHalf.add("circle.half");
		this.namesFull.add("circle.full");

		this.namesEmpty.add("bar.empty");
		this.namesHalf.add("bar.empty");
		this.namesFull.add("bar.full");

		this.baseVector  = new Vector(-2*size, player.getSize()/2.0+size);
		this.horizVector = new Vector(size, 0.0);
		this.vertVector  = new Vector(0.0, size);
	}





	@Override
	public void draw(Input input, Output output) {
		super.draw(input, output);

		setBox(player.getBox());

		double[] parameters = {5.0 * player.getHealth() / player.getHealthMax()  // health
				, 5.0 * player.getBreath() / player.getBreathMax()	            // breath
				, Math.min(5.0 - 5.0 * player.getBombCooldown() / player.getBombCooldownMax(), 5.0)};  // bomb

		for (int i = 0; i < 5; ++i) {

			String[] names = {namesEmpty.get(0), namesEmpty.get(1), namesEmpty.get(2)};

			for(int parameter = 0; parameter < parameters.length; ++parameter) {

				if(parameters[parameter] >= i+1)
					names[parameter] = namesFull.get(parameter);
				else if(parameters[parameter] >= i+1 - 0.5)
					names[parameter] = namesHalf.get(parameter);

				output.drawSprite(getSprite(names[parameter]),
						new Box(getBox().getCenter().add(baseVector).add(horizVector.mul(i)).add(vertVector.mul(parameter))
								, size, size));
			}
		}
	}


	@Override
	protected int getPriority() {
		return 4242;
	}


	@Override
	public void register(World world) {
		super.register(world);
		selector = new SelectGravity(gravityAngle);
		getWorld().register(selector);
	}


	@Override
	public void unregister() {
		if(selector != null) {
			getWorld().unregister(selector);
			selector = null;
		}
		super.unregister();
	}
}
