package es.ucm.fdi.ici.c2223.practica2.grupo04.GhostsFSM.Actions;

import pacman.game.Game;
import es.ucm.fdi.ici.Action;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class GhostsWaitAction implements Action{
	GHOST ghost;
	public GhostsWaitAction( GHOST ghost) {
		this.ghost = ghost;
	}
	
	@Override
	public String getActionId() {
		return ghost.toString() + "Wait";
	}

	@Override
	public MOVE execute(Game game) {
			return MOVE.NEUTRAL;
	}
}
