package es.ucm.fdi.ici.c2223.practica2.grupo04.GhostsFSM.Actions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2223.practica2.grupo04.GhostsFSM.GhostsUtils;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class GhostsDisperseAction implements Action {

	GHOST ghost;
	public GhostsDisperseAction( GHOST ghost) {
		this.ghost = ghost;
	}
	
	@Override
	public String getActionId() {
		return "GhostsDisperse";
	}

	@Override
	public MOVE execute(Game game) {
		return game.getNextMoveAwayFromTarget(game.getGhostCurrentNodeIndex(ghost), GhostsUtils.ClosestPointToAllGhosts(game), game.getGhostLastMoveMade(ghost), DM.EUCLID);
	}

}
