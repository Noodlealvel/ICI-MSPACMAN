package es.ucm.fdi.ici.c2223.practica4.grupo04.actions;

import pacman.game.Game;
import es.ucm.fdi.ici.Action;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class GhostsWaitAction implements Action{
	GHOST ghost;
	public GhostsWaitAction( GHOST ghost) {
		this.ghost = ghost;
	}
	
	@Override
	public String getActionId() {
		return ghost.toString() + "wait";
	}

	@Override
	public MOVE execute(Game game) {
			return MOVE.NEUTRAL;
	}
}
