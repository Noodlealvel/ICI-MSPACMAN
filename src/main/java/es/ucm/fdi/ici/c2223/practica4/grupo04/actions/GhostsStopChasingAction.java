package es.ucm.fdi.ici.c2223.practica4.grupo04.actions;

import es.ucm.fdi.ici.Action;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class GhostsStopChasingAction implements Action {

	  GHOST ghost;
			public GhostsStopChasingAction(GHOST ghost) {
				this.ghost = ghost;
			}
		@Override
		public String getActionId() {
			return ghost.toString() + "stopChasing";
		}

		@Override
		public MOVE execute(Game game) {
			if (game.doesGhostRequireAction(ghost))       
	        {
				return game.getNextMoveAwayFromTarget(game.getGhostCurrentNodeIndex(ghost), game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(ghost), DM.EUCLID);

	        }
			else
				return MOVE.NEUTRAL;
		}

}
