package es.ucm.fdi.ici.c2223.practica4.grupo04.actions;

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
		return ghost.toString() + "flee";
	}

	@Override
	public MOVE execute(Game game) {
		if (game.doesGhostRequireAction(ghost))       
        {
			return game.getNextMoveAwayFromTarget(game.getGhostCurrentNodeIndex(ghost), game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(ghost), DM.PATH);

        }
		else
			return MOVE.NEUTRAL;
	}

}
