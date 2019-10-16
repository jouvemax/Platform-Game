package platform.game;

/**
 * Designate certain types of Damages
 */

public enum Damage {
	FIRE          // Dealt by Fireballs
	, PHYSICAL    // Dealt by Spikes
	, AIR		  // Dealt by Jumper and Player
	, VOID		  // Dealt by Limits
	, ACTIVATION  // Dealt by Player
	, HEAL		  // Dealt by Heart
	, PRESENCE    // Dealt by Player
	, KEY         // Dealt by Keys
	, ARROW       // Dealt by Arrows
	, EXPLOSION;  // Dealt by Bombs
}
