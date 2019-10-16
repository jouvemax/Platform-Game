package platform.game.block;

import platform.game.Actor;
import platform.game.Damage;
import platform.game.misc.Debris;
import platform.util.Box;
import platform.util.Vector;


/**
 *  Block that can be destroyed by fire or explosions.
 */
public class WoodenBlock extends Block {

	private static final long serialVersionUID = -8804632865857305916L;
	
	
	
	/**
	 * Full constructor
	 * @param box     Position and size parameters of the WoodenBlock.
	 * @param sprite  Sprite of the WoodenBlock.
	 */
	public WoodenBlock(Box box, String sprite) {
		super(box, sprite);
	}

	
	/**
	 * Constructor without specifying the sprite, "wood.1" by default.
	 * @param box  Position and size parameters of the WoodenBlock.
	 */
	public WoodenBlock(Box box) {
		this(box, "wood.1");
	}
	

	
	@Override
	public boolean hurt(Actor instigator, Damage type, double amount, Vector location) {
		switch(type) {
		// Can be destroyed by FIRE or EXPLOSION
		case FIRE:
		case EXPLOSION:
			// Creates Debris
			String[] sprites = {"wood.debris.1", "wood.debris.2", "wood.debris.3"};
			getWorld().register(new Debris(getBox().getCenter(), sprites, getBox().getWidth()*0.4));
			getWorld().unregister(this);
			return true;
		default:
			return super.hurt(instigator, type, amount, location);
		}

	}

	
	@Override
	public Actor copie() {
		return new WoodenBlock(getBox(), getSpriteName());
	}
}
