package es.ucm.fdi.ici.c2223.practica2.grupo04.GhostsFSM;

import es.ucm.fdi.ici.Input;
import pacman.game.Constants.GHOST;
import pacman.game.Game;

public class GhostsInput extends Input {

	private boolean blinkyEdible;
	private boolean inkyEdible;
	private boolean pinkyEdible;
	private boolean sueEdible;
	public GhostsInput(Game game) {
		super(game);
	}
	@Override
	public void parseInput() {
		blinkyEdible = game.isGhostEdible(GHOST.BLINKY);
		pinkyEdible = game.isGhostEdible(GHOST.PINKY);
		sueEdible = game.isGhostEdible(GHOST.SUE);
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

}
