package platform.game.block;

import platform.game.Actor;
import platform.util.Box;
import platform.util.Input;
import platform.util.Output;

/**
 * Simple solid actor that does nothing.
 */
public class Block extends Actor {

	private static final long serialVersionUID = -8822563808223736615L;

	
	
	/**
	 * Full constructor.
	 * @param box     Position and size parameters of the Block.
	 * @param sprite  Sprite of the Block.
	 */
	public Block(Box box, String sprite) {
		super(box, sprite);
	}

	
	/**
	 * Constructor without specifying a Sprite. "grass.center" by default
	 * @param box  Position and size parameters of the Block.
	 */
	public Block(Box box) {
		super(box, "grass.center");
	}

	
	
	@Override
	public void draw(Input input, Output output) {
		output.drawSprite(getSprite(), getBox());
	}

	@Override
	protected int getPriority() {
		// Low priority so other Actors can easily interact with it
		return 0;
	}
	

	@Override
	public boolean isSolid() {
		return true;
	}
	

	@Override
	public Actor copie() {
		return new Block(getBox(), getSpriteName());
	}

}
