package es.ucm.fdi.ici.c2223.practica2.grupo04.GhostsFSM.Actions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2223.practica2.grupo04.GhostsFSM.GhostsUtils;
import pacman.game.Constants.DM;
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
		return ghost.toString() + "FleeFromPPill";
	}

	@Override
	public MOVE execute(Game game) {
		if (game.doesGhostRequireAction(ghost))       
        {
			return game.getNextMoveAwayFromTarget(game.getGhostCurrentNodeIndex(ghost),GhostsUtils.NearestActivePPill(game, ghost), game.getGhostLastMoveMade(ghost), DM.PATH);
        }
		else
			return MOVE.NEUTRAL;
	}
}
