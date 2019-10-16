package platform.game.level;

import platform.game.Actor;
import platform.util.Input;
import platform.util.Output;

/**
 * Base class for level factories, which provides fade in transition. Subclasses
 * are requires to override <code>register</code>.
 */
public abstract class Level extends Actor {

	private static final long serialVersionUID = 9102237298770135451L;
	private double fadein;

	private static int levelDone;

	private boolean dynamicSave;

	public Level() {
		super(null);
		fadein = 1.0;
		dynamicSave = false;
	}

	protected void updateLevelsDone(int i) {
		if(levelDone < i)
			levelDone = i;
	}


	protected int getLevelDone() {
		return levelDone;
	}

	@Override
	public int getPriority() {
		return Integer.MAX_VALUE;
	}

	@Override
	public void update(Input input) {
		fadein -= input.getDeltaTime();
		//		if (fadein <= 0.0)
		//			getWorld().unregister(this);
	}

	@Override
	public void draw(Input input, Output output) {
		output.drawSprite(getSprite("pixel.black"), output.getBox(), 0.0, fadein);
	}

	/** @return a new instance of default level */
	public static Level createDefaultLevel() {
		return new Menu();
	}

	public boolean isDynamicSave() {
		return dynamicSave;
	}

	public void setDynamicSave() {
		dynamicSave = true;
	}
}
