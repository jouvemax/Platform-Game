package platform.game.button;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import platform.game.Actor;
import platform.game.Eraser;
import platform.game.World;
import platform.game.linkable.Mover;
import platform.util.Box;
import platform.util.Input;


/**
 *  Spawn is a class that Spawns actors. Used in level.Builder.
 *  It has a specificity for the Player, in that it only spawns
 *  it effectively after a second left click.
 *  <p>
 *  All Actors other than Player are instantly instantiated. It is done so
 *  because Player moves the camera, which is actively used to build the level
 *  beforehand.
 */
public class Spawn extends Button {

	private static final long serialVersionUID = -2928395054479313888L;

	/**
	 * Spawns the Actor stored in this variable
	 */
	private Actor actor;

	/**
	 * Is used to track the newly created Actor
	 */
	private Actor newActor;

	/**
	 * Is used to unregister all Actors (mainly SuperSpawners) from the level
	 */
	private ArrayList<Actor> toUnregister;

	/**
	 * Used to maintain the spawn in focus in the level, even when moving.
	 */
	private final Box boxOrigin;

	/**
	 * Sprite of an actor that is in collision with the spawn
	 */
	private Actor sourceSprite;



	/**
	 * Full constructor used to Spawn the Player.
	 * @param box           Position and size parameters of the Spawn.
	 * @param spriteOff     Sprite of the Button when the mouse is not on it.
	 * @param spriteOn      Sprite of the Button when the mouse is on it.
	 * @param actor	        Actor linked to the Spawn.
	 * @param toUnregister  List of Actors that need to be unregistered. That marks the Spawn as being the Player's.
	 * @param shortcut  Shortcut of the Spawn.
	 * @see java.awt.event.KeyEvent
	 */
	public Spawn(Box box, String spriteOff, String spriteOn, Actor actor, ArrayList<Actor> toUnregister, int shortcut) {
		super(box, spriteOff, spriteOn, shortcut);

		this.actor        = actor;

		// newActor will be used later
		this.newActor     = null;

		// Specific for the Player
		this.toUnregister = toUnregister;

		// Sets the boxOrigin, assuming that the level begins centered at default values (cf. World & level.Builder)
		this.boxOrigin    = box;

		this.sourceSprite = null;
	}


	/**
	 * Constructor used to Spawn the Player, without any shortcut.
	 * @param box           Position and size parameters of the Spawn.
	 * @param spriteOff     Sprite of the Button when the mouse is not on it.
	 * @param spriteOn      Sprite of the Button when the mouse is on it.
	 * @param actor	        Actor linked to the Spawn.
	 * @param toUnregister  List of Actors that need to be unregistered. That marks the Spawn as being the Player's.
	 */
	public Spawn(Box box, String spriteOff, String spriteOn, Actor actor, ArrayList<Actor> toUnregister) {
		this(box, spriteOff, spriteOn, actor, toUnregister, KeyEvent.VK_UNDEFINED);

	}


	/**
	 * Full constructor used to Spawn another Actor than the Player.
	 * @param box        Position and size parameters of the Spawn.
	 * @param spriteOff  Sprite of the Button when the mouse is not on it.
	 * @param spriteOn   Sprite of the Button when the mouse is on it.
	 * @param actor	     Actor linked to the Spawn.
	 * @param shortcut  Shortcut of the Spawn.
	 * @see java.awt.event.KeyEvent
	 */
	public Spawn(Box box, String spriteOff, String spriteOn, Actor actor, int shortcut) {
		this(box, spriteOff, spriteOn, actor, null, shortcut);
	}


	/**
	 * Constructor used to Spawn another Actor than the Player, without any shortcut.
	 * @param box        Position and size parameters of the Spawn.
	 * @param spriteOff  Sprite of the Button when the mouse is not on it.
	 * @param spriteOn   Sprite of the Button when the mouse is on it.
	 * @param actor	     Actor linked to the Spawn.
	 */
	public Spawn(Box box, String spriteOff, String spriteOn, Actor actor) {
		this(box, spriteOff, spriteOn, actor, null);
	}


	/**
	 *  Constructor used to Spawn another Actor than the Player, without a second Sprite.
	 * @param box     Position and size parameters of the Spawn.
	 * @param sprite  Sprite of the Spawn.
	 * @param actor	  Actor linked to the Spawn.
	 * @param shortcut  Shortcut of the Spawn.
	 * @see java.awt.event.KeyEvent
	 */
	public Spawn(Box box, String sprite, Actor actor, int shortcut) {
		this(box, sprite, sprite, actor, shortcut);
	}


	/**
	 *  Constructor used to Spawn another Actor than the Player, without any shortcut nor a second Sprite
	 * @param box     Position and size parameters of the Spawn.
	 * @param sprite  Sprite of the Spawn.
	 * @param actor	  Actor linked to the Spawn.
	 */
	public Spawn(Box box, String sprite, Actor actor) {
		this(box, sprite, sprite, actor);
	}



	@Override
	public void register(World world) {
		// Register the Actor so it is drawn, Spawn being not drawn directly.
		super.register(world);
		actor.register(world);
	}


	@Override
	public void unregister() {
		actor.unregister();
		super.unregister();
	}


	@Override
	public void preUpdate(Input input) {
		// Reset the target to null.
		sourceSprite = null;
	}


	@Override
	public void interact(Actor other) {
		super.interact(other);
		if(other.getBox() != null
				&& other.getBox().isColliding(getBox())
				&& other.getSpriteName() != null)
			sourceSprite = other;
	}


	@Override
	public void update(Input input) {
		super.update(input);

		// Used to follow the view of the user
		setBox(followView(boxOrigin));


		// We then have to observe the following 3 steps in this specific order.

		// 1. If an Actor has been previously spawned and the user left-clicks, then set newActor to null...
		if(newActor != null
				&& input.getMouseButton(1).isPressed()) {
			// ...unless toUnregister isn't null. Then, it signifies that we're dealing with the player.
			// The player isn't already spawned, merely its Spawn has moved to this position.
			if(toUnregister != null) {
				// We have to unregister all other Spawns/Actors used to build the Level
				for(int i = 0; i<toUnregister.size();++i)
					getWorld().unregister(toUnregister.get(i));

				// We finally register the Player and unregister its Spawn.
				getWorld().register(newActor);
				getWorld().unregister(this);

				// We save the map to a new save folder (see Simulator.save(), called by saveAll())
				getWorld().saveAll();
			}

			// If it isn't a player, then we merely unlink the Actor from the Spawn
			newActor = null;
		}


		// 2. If we click on the Spawn or use the shortcut...
		if(((getBox().isColliding(input.getMouseLocation())
				&& input.getMouseButton(1).isPressed())
				|| input.getKeyboardButton(getShortcut()).isPressed())
				&& !(sourceSprite instanceof Eraser)) {

			// The create a copy of the actor and place it into newActor
				newActor = actor.copie();

				// If we're dealing with a player, unregister all other SuperSpawners
				if(toUnregister != null) {
					for(int i = 0; i<toUnregister.size(); ++i)
						getWorld().unregister(toUnregister.get(i));
				}
				// Otherwise, just register the newActor
				else if(toUnregister == null)
					getWorld().register(newActor);	

				if(actor instanceof Mover
						&& sourceSprite != null) {
					newActor.setSpriteName(sourceSprite.getSpriteName());
					newActor.setBox(sourceSprite.getBox());
					getWorld().unregister(sourceSprite);
				
			}
		}


		// 3. If an Actor has been spawned...
		if(newActor != null) {

			// Then set the box of the newActor to the position of the mouse
			// Scrolling can be used to resize the Actor
			newActor.setBox(new Box(input.getMouseLocation()
					, newActor.getBox().getWidth()*((toUnregister != null)?1.0:1.0+0.1*input.getMouseScroll()) 
					, newActor.getBox().getHeight()*((toUnregister != null)?1.0:1.0+0.1*input.getMouseScroll())
					));

			// If we're dealing with the player, then also move the Spawn
			// Can't resize it either
			if(toUnregister != null)
				setBox(new Box(input.getMouseLocation()
						, 0.5 // we can't resize the player!
						, 0.5
						));
		}
	}
}