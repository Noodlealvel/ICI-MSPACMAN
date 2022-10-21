package es.ucm.fdi.ici.c2223.practica2.grupo04.GhostsFSM;

import es.ucm.fdi.ici.Action;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class GhostsFleeFromPPillAction implements Action {

	GHOST ghost;
	public GhostsFleeFromPPillAction( GHOST ghost) {
		this.ghost = ghost;
	}
	
	@Override
	public String getActionId() {
		return "GhostsFleeFromPPillAction";
	}

	@Override
	public MOVE execute(Game game) {
		return null;
	}

}
