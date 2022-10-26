package es.ucm.fdi.ici.c2223.practica2.grupo04.GhostsFSM;

import es.ucm.fdi.ici.Input;
import pacman.game.Constants.GHOST;
import pacman.game.Game;

public class GhostsInput extends Input {

	private boolean blinkyEdible;
	private boolean inkyEdible;
	private boolean pinkyEdible;
	private boolean sueEdible;
	private int currentLevel;
	private boolean eatenPPill;
	private boolean blinkyEaten;
	private Boolean pinkyEaten;
	private Boolean sueEaten;
	private boolean inkyEaten;
	static private int storedLevel = -1;
	public GhostsInput(Game game) {
		super(game);
	}
	@Override
	public void parseInput() {
		eatenPPill = game.wasPowerPillEaten();
		currentLevel = game.getCurrentLevel();
		blinkyEaten = game.wasGhostEaten(GHOST.BLINKY);
		blinkyEdible = game.isGhostEdible(GHOST.BLINKY);
		pinkyEaten = game.wasGhostEaten(GHOST.PINKY);
		pinkyEdible = game.isGhostEdible(GHOST.PINKY);
		sueEaten = game.wasGhostEaten(GHOST.SUE);
		sueEdible = game.isGhostEdible(GHOST.SUE);
		inkyEaten = game.wasGhostEaten(GHOST.INKY);
		inkyEdible = game.isGhostEdible(GHOST.INKY);
	}
	public boolean isGhostEdible(GHOST ghost) {
		switch(ghost) {
		case BLINKY:
			return blinkyEdible;
		case INKY:
			return inkyEdible;
		case PINKY:
			return pinkyEdible;
		case SUE:
			return sueEdible;
		default:
			return false;
	}
	}
	public boolean levelChanged() {
		if (currentLevel != storedLevel) {
			storedLevel = currentLevel;
			return true;
		}
		return false;
	}
	public boolean PPillEaten() {
		return eatenPPill;
	}
	public boolean wasGhostEaten(GHOST ghost) {
		switch(ghost) {
		case BLINKY:
			return blinkyEaten;
		case INKY:
			return inkyEaten;
		case PINKY:
			return pinkyEaten;
		case SUE:
			return sueEaten;
		default:
			return false;
	}
	}

}
