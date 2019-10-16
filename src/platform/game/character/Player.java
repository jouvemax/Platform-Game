package platform.game.character;

import java.awt.event.KeyEvent;

import platform.game.Actor;
import platform.game.Damage;
import platform.game.Overlay;
import platform.game.World;
import platform.game.graphic.End;
import platform.game.Arrow;
import platform.game.weapon.Bomb;
import platform.game.weapon.Fireball;
import platform.util.Box;
import platform.util.Input;
import platform.util.Output;
import platform.util.Vector;

public class Player extends Actor {

	private static final long serialVersionUID = -1848893426172511341L;

	/**
	 * Indicates the radius of the View
	 */
	private double viewRadius;

	/**
	 * Indicates whether the Player just spawned or not.
	 */
	private boolean spawned;

	/**
	 * Position of the Player
	 */
	private Vector position;

	/**
	 * Speed of the Player
	 */
	private Vector velocity;

	/**
	 *  Overlay associated with the player
	 */
	private Overlay overlay;


	/**
	 * Maximal health of the Player.
	 */
	private double previousHealth;

	/**
	 * Current health status of the Player
	 */
	private double health;

	/**
	 * Maximum health for the Player.
	 */
	private final double healthMax;

	/**
	 * Indicates how much time the Player has left before being
	 * able to be injured again.
	 */
	private double injuredTime;

	/**
	 * Maximum time for which the player can be invulnerable.
	 */
	private final double injuredTimeMax;

	/**
	 * Used to vary the Sprite of the Player when it's injured.
	 */
	private double variation;

	/**
	 * Indicates the maximum variation time for the Sprite.
	 */
	private final double variationMax;

	/**
	 * Size of the player
	 */
	private final double size;

	/**
	 * Indicates whether the Player touches something solid.
	 */
	private boolean colliding;


	/**
	 * Indicates how much time the Player has left before being
	 * able to be throw new bombs.
	 */
	private double bombCooldown;

	/**
	 * Maximum time for which the player cannot throw bombs again.
	 */
	private final double bombCooldownMax;

	/**
	 * Indicates how much time of breath the Player still has.
	 */
	private double breath;

	/**
	 * Maximum breath the Player can hold.
	 */
	private final double breathMax;

	/**
	 * Indicates whether the Player has recently used all of its breath at one time.
	 */
	private boolean outOfBreath;

	/**
	 * Indicates whether the Player is dead.
	 */
	private boolean dead;

	/**
	 * Angle given to the Sprite. Used when the Gravity changes angle (blocked to PI/2 angles).
	 */
	private double angleSprite;

	/**
	 * Angle of the Sprite before any change.
	 */
	private double oldAngleSprite;

	/**
	 * Indicates where the Sprite is within the angle transition.
	 */
	private double timerAngleSprite;

	/**
	 * Used to determine how much time the angle transition should last.
	 */
	private final double timeChangeAngleSprite;

	/**
	 * Registers the current angle of gravity
	 */
	private double gravityAngle;
	
	/**
	 * Indicates the parameter of the angle (in which direction it is generally
	 * going)
	 */	
	private int angleMode;

	
	
	/**
	 * Full constructor for the Player.
	 * @param position   Initial position of the Player.
	 * @param velocity   Initial speed of the Player.
	 * @param healthMax  Maximum value for the health of the Player.
	 * @param size       Size of the Player.
	 */
	public Player(Vector position, Vector velocity, double healthMax, double size) {
		super(new Box(position, size, size), "blocker.happy");

		if(position == null || velocity == null)
			throw new NullPointerException();

		// Desired radius for the View
		this.viewRadius            = 5.0;

		// Size of the Player
		this.size                  = size;

		// Set initial parameters
		this.position              = position;
		this.velocity              = velocity;
		this.spawned               = false;
		this.colliding             = false;
		this.outOfBreath           = false;
		this.dead                  = false;

		// Health related parameters
		this.healthMax             = healthMax;
		this.health                = healthMax;
		this.previousHealth        = healthMax;
		this.injuredTime           = -1.0;  // set to a negative value to ignore it for now
		this.injuredTimeMax        = 1.0;   // 1 second of invulnerability offered to the Player
		this.variationMax          = 0.2;   // The Sprite changes each 0.2 seconds
		this.variation             = 0.2;

		// Breath related parameters
		this.breathMax             = 2.0;
		this.breath                = breathMax;

		// Bomb related parameters
		this.bombCooldown          = -1.0;
		this.bombCooldownMax       = 5.0;

		// Sprite related parameters
		this.angleSprite           = 0.0;  // Begins to a normal angle
		this.oldAngleSprite        = 0.0;
		this.timerAngleSprite      = -1.0;
		this.timeChangeAngleSprite = 0.25; // Has to complete the cycle in a quarter of a second.
		
		// Starting value for the gravity
		this.gravityAngle          = 0.0;
		this.angleMode             = 0;
	}


	/**
	 * Constructor specifying no size for the Player, 0.5 by default.
	 * @param position   Initial position of the Player.
	 * @param velocity   Initial speed of the Player.
	 * @param healthMax  Maximum value for the health of the Player.
	 */
	public Player(Vector position, Vector velocity, double healthMax) {
		this(position, velocity, healthMax, 0.5);
	}


	/**
	 * Constructor specifying no maximum health for the Player, 5.0 by default.
	 * @param position  Initial position of the Player.
	 * @param velocity  Initial speed of the Player.
	 */
	public Player(Vector position, Vector velocity) {
		this(position, velocity, 5.0);
	}


	/**
	 * Constructor specifying no velocity, 0-vector by default.
	 * @param position  Initial position of the Player.
	 */
	public Player(Vector position) {
		this(position, Vector.ZERO);
	}



	@Override
	public void preUpdate(Input input) {

		// Resets colliding back to false.
		colliding = false;

		// Sets the position of the player according to its box
		// Necessary because of the level Builder.
		if(!spawned) {
			position = getBox().getCenter();
			spawned = true;
		}

		if(overlay == null) {
			// give the old gravityAngle
			overlay = new Overlay(this, gravityAngle);
			getWorld().register(overlay);
		}

		gravityAngle = getWorld().getGravityAngle();
	}


	@Override
	public void interact(Actor other) {

		// Interacts with solid Actors
		double angleLimit     = Math.PI/4;
		angleSprite           = 0.0;
		angleMode             = 0;
		
		for(int i = 2; i < 7; i += 2)
			if(gravityAngle >= (i-1)*angleLimit && gravityAngle < (i+1)*angleLimit) {
				angleSprite = i*angleLimit;		
				angleMode   = i/2;
			}
		
		if (other.isSolid()) {

			Vector delta = other.getBox().getCollision(getBox());

			if (delta != null) {
				// If the Player is colliding with a solid Actor,
				// make sure it doesn't break the limit between the two.
				position = position.add(delta);

				if (delta.getX() != 0.0)
					velocity = new Vector(0.0, velocity.getY());
				if (delta.getY() != 0.0)
					velocity = new Vector(velocity.getX(), 0.0);

				// From here on, calculates if the Player is colliding something that is
				// not considered to be the ceiling (we have to take the changeable gravity
				// into account!!)
				double angleCollision = other.getBox().getCollision(getBox()).getAngle()-Math.PI/2.0;

				if(angleCollision < 0.0)
					angleCollision   += Math.PI*2;

				double absoluteAngle  = Math.abs(angleCollision - gravityAngle);

				if(absoluteAngle > Math.PI)
					absoluteAngle    -= Math.PI;

				colliding = absoluteAngle <= angleLimit*3;
			}
		}
	}


	@Override
	public void update(Input input) {

		double delta = input.getDeltaTime();

		// Sets the maximum speed for the Player
		double maxSpeed = 5.0;

		// Before updating time parameters, check if the angle of the Sprite should be the same
		if(timerAngleSprite < 0.0 && oldAngleSprite != angleSprite)
			timerAngleSprite = timeChangeAngleSprite;

		// If the Player is invulnerable, change its health points back
		if(injuredTime > 0.0 && health < previousHealth)
			health = previousHealth;


		// Update all time parameters
		bombCooldown -= delta;
		injuredTime -= delta;
		variation -= delta;
		timerAngleSprite -= delta;

		// If the health is non positive, the player dies.
		if(health <= 0.0) {
			viewRadius += input.getDeltaTime()*0.5;
			dies();
		}

		// Check if the player lost some health points. If it's the case, allow him
		// to be invulnerable. Then immediately update the health status
		if(previousHealth > health)
			injuredTime = injuredTimeMax;

		previousHealth = health;

		// After updating the time parameters, if the time has completely elapsed,
		// reset oldAngleSprite.
		if(timerAngleSprite < 0.0 && oldAngleSprite != angleSprite)
			oldAngleSprite = angleSprite;

		// Resets variation if it needs to.
		if(variation < 0.0)
			variation = variationMax;

		/***** MOVEMENT OF THE ACTOR *****/
		// FRICTION
		if (colliding) {
			double scale = Math.pow(0.001, input.getDeltaTime());
			velocity = velocity.mul(scale);
		}

		boolean[] shortcuts = {input.getKeyboardButton(KeyEvent.VK_D).isDown() // right
				, input.getKeyboardButton(KeyEvent.VK_W).isDown()
				|| input.getKeyboardButton(KeyEvent.VK_Z).isDown() // up
				, input.getKeyboardButton(KeyEvent.VK_A).isDown()
				|| input.getKeyboardButton(KeyEvent.VK_Q).isDown() // left
		, input.getKeyboardButton(KeyEvent.VK_S).isDown() // down
		};
		
		// Can move, interact and throw things only if not dead
		// All movements follow the gravity. So the player always goes to ITS left if left is asked.
		if(!dead) {

			// RIGHT
			if (shortcuts[angleMode%4]) {
				double horizontalSpeed = velocity.rotated(-getWorld().getGravityAngle()).getX();
				if (horizontalSpeed < maxSpeed) {
					double increase = 60.0 * input.getDeltaTime();
					double speed = horizontalSpeed + increase;
					if (speed > maxSpeed)
						increase = 0;
					velocity = velocity.add(new Vector(increase, 0).rotated(getWorld().getGravityAngle()));
				}
			}


			// LEFT
			if (shortcuts[(angleMode+2)%4]) {
				double horizontalSpeed = velocity.rotated(Math.PI-getWorld().getGravityAngle()).getX();
				if (horizontalSpeed < maxSpeed) {
					double increase = 60.0 * input.getDeltaTime();
					double speed = horizontalSpeed + increase;
					if (speed > maxSpeed)
						increase = 0;
					velocity = velocity.add(new Vector(increase, 0).rotated(Math.PI+getWorld().getGravityAngle()));
				}
			}


			// JUMP
			if (shortcuts[(angleMode+1)%4]&& colliding)
				velocity = new Vector(
						velocity
						.rotated(-getWorld().getGravityAngle()).getX(), 0.0)
				.add(new Vector(0.0, 7.0))
				.rotated(getWorld().getGravityAngle());



			// THROW FIREBALL
			if (input.getKeyboardButton(KeyEvent.VK_SPACE).isPressed())
				getWorld().register(new Fireball(position, velocity.add(velocity.resized(2.0)), this));


			// THROW BOMB
			if (input.getKeyboardButton(KeyEvent.VK_B).isPressed()
					&& bombCooldown < 0.0) {
				bombCooldown = bombCooldownMax;
				getWorld().register(new Bomb(position, velocity.add(velocity.resized(2.0)), this));
			}


			// BLOW AIR
			// Only if not out of breath
			if (input.getKeyboardButton(KeyEvent.VK_SHIFT).isDown()
					&& !outOfBreath) {
				getWorld().hurt(getBox(), this, Damage.AIR, 1.0, getPosition());
				breath -= delta;
				if(breath <= 0.0)
					outOfBreath = true;
			} 
			// If nothing is asked, regain breath
			// But slower than used
			else if(breath < breathMax)
				breath += delta/5.0;

			// The player is not out of Breath once it has regained all of its breath.
			if(breath >= breathMax && outOfBreath)
				outOfBreath = false;


			// ACTIVATE
			// Used to activate e.g. the Exit
			if (input.getKeyboardButton(KeyEvent.VK_E).isPressed())
				getWorld().hurt(getBox(), this, Damage.ACTIVATION, 1.0, getPosition());

		}


		// SIGNALS PRESENCE
		// Useful for Actors such as ArrowDispenser
		getWorld().hurt(new Box(getBox().getCenter(), 10.0, 10.0), this, Damage.PRESENCE, 1.0, getPosition());


		// UPDATES SPEED PARAMETERS
		velocity = velocity.add(getWorld().getGravity().mul(delta));
		position = position.add(velocity.mul(delta));


		// UPDATES BOX
		setBox(new Box(position, size, size));

		// Set the view according to the position of the Player
		getWorld().setView(position, viewRadius);
	}


	@Override
	public void draw(Input input, Output output) {
		// If the player is injured and not dead yet, vary its Sprite
		if(!dead && injuredTime > 0.0 && variation < variationMax/2.0)
			setSpriteName("blocker.injured");
		// Else, if its Health is more than a quarter, draw it happy
		else if(health/healthMax > 0.25)
			setSpriteName("blocker.happy");
		// Else, if it's still alive, draw it sad
		else if(health > 0.0)
			setSpriteName("blocker.sad");
		// Else, draw it as dead.
		else
			setSpriteName("blocker.dead");

		// Changes the angle of the Sprite smoothly
		double deltaAngle = oldAngleSprite + ((timerAngleSprite >= 0.0?(1.0-timerAngleSprite/timeChangeAngleSprite)*(angleSprite-oldAngleSprite):0.0));

		output.drawSprite(getSprite(), getBox(), deltaAngle);
	}


	@Override
	public boolean hurt(Actor instigator, Damage type, double amount, Vector location) {
		switch (type) {
		// Used by the jumper to lift the Player in the air (up the y axis)
		// Should not be called when the player itself blows.
		case AIR :
			if(instigator != this)
				velocity = new Vector(velocity.getX(), amount);
			return true;

			// Player instantly killed when outside of range
		case VOID :
			dies();
			return true;

			// Player can be healed only if not dead yet or already at maxHealth
			// Cannot have more health than healthMax
		case HEAL :
			if(dead || health >= healthMax)
				return false;
			if(health + amount <= healthMax)
				health += amount;
			else
				health = healthMax;
			return true;

			// Can suffer fire damage, unless it comes from itself
		case FIRE:
			if(instigator != this)
				health -= amount;
			return true;

			// Can suffer explosion damages, even if it comes from itself
		case EXPLOSION:
			health -= amount;
			return true;

			// Can suffer physical damages if fall on horizontal, upwards pointing Spikes. (true for all gravities)
		case PHYSICAL :
			if(velocity.getY() < -0.5)
				health -= amount;
			return true;

			// Can suffer damage form arrows, and also be pushed backwards by it.
		case ARROW :
			if(instigator.getBox().isColliding(getBox())) {
				health -= amount;
				velocity = velocity.add((((Arrow)instigator).getVelocity()).mul(0.75));
				return true;
			}
			return false;

			// Key and activation are both accepted by the Player
		case ACTIVATION :
		case KEY:
			return true;
		default :
			return super.hurt(instigator, type, amount, location);
		}
	}


	@Override
	public Vector getPosition() {
		return position;
	}


	@Override
	protected int getPriority() {
		return 42;
	}


	@Override
	public Actor copie() {
		return new Player(position);
	}


	@Override
	public void unregister() {
		getWorld().unregister(overlay);
		overlay = null;
		super.unregister();
	}


	/**
	 * Registers the end sequence of the level, marks the Player as dead.
	 * @see End
	 */
	private void dies() {
		if(!dead) {
			health = -100.0;
			getWorld().register(new End());
			getWorld().unregister(overlay);;
			dead = true;
		}
	}


	/**
	 * Returns health.
	 * @return the current status of the Player's health-
	 * @see Overlay
	 */
	public double getHealth() {
		return health;
	}


	/**
	 * Returns healthMax.
	 * @return the max capacity of the Player's health.
	 */
	public double getHealthMax() {
		return healthMax;
	}

	/**
	 * Returns breath.
	 * @return the current status of the Player's breath.
	 * @see Overlay
	 */
	public double getBreath() {
		return breath;
	}


	/**
	 * Returns breathMax.
	 * @return the max capacity of the Player's breath.
	 * @see Overlay
	 */
	public double getBreathMax() {
		return breathMax;
	}


	/**
	 * Returns bombCooldown
	 * @return the current status of the Player's bomb cooldown
	 * @see Overlay
	 */
	public double getBombCooldown() {
		return bombCooldown;
	}


	/**
	 * Returns bombCooldownMax
	 * @return the max value for the bombCooldown
	 * @see Overlay
	 */
	public double getBombCooldownMax() { return bombCooldownMax; }


	/**
	 * Returns size.
	 * @return the size of the Player
	 * @see Overlay
	 */
	public double getSize() {
		return size;
	}
}