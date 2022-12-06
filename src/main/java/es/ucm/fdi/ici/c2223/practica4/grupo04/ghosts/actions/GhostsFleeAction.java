package es.ucm.fdi.ici.c2223.practica4.grupo04.ghosts.actions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2223.practica4.grupo04.ghosts.GhostsFuzzyMemory;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class GhostsFleeAction implements Action {
	  GHOST ghost;
	private GhostsFuzzyMemory fuzzyMemory;
		public GhostsFleeAction(GHOST ghost, GhostsFuzzyMemory fuzzyMemory) {
			this.ghost = ghost;
			this.fuzzyMemory = fuzzyMemory;
		}
	@Override
	public String getActionId() {
		return ghost.toString() + "flee";
	}

	@Override
	public MOVE execute(Game game) {
		if (game.doesGhostRequireAction(ghost))       
        {
			return game.getNextMoveAwayFromTarget(game.getGhostCurrentNodeIndex(ghost), fuzzyMemory.lastPacmanPosition(), game.getGhostLastMoveMade(ghost), DM.PATH);

        }
		else
			return MOVE.NEUTRAL;
	}

}
