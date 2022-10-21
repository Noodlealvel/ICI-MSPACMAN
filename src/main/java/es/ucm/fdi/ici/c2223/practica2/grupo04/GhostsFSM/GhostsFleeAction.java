package es.ucm.fdi.ici.c2223.practica2.grupo04.GhostsFSM;

import es.ucm.fdi.ici.Action;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class GhostsFleeAction implements Action {
	  GHOST ghost;
		public GhostsFleeAction( GHOST ghost) {
			this.ghost = ghost;
		}
	@Override
	public String getActionId() {
		return "GhostsFleeAction";
	}

	@Override
	public MOVE execute(Game game) {
		return game.getNextMoveAwayFromTarget(game.getGhostCurrentNodeIndex(ghost), game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(ghost), DM.PATH);
	}

}
