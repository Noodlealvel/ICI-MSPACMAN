package es.ucm.fdi.ici.c2223.practica4.grupo04.ghosts.actions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2223.practica4.grupo04.ghosts.GhostsFuzzyMemory;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class GhostsChaseAction implements Action {


	GHOST ghost;
	private GhostsFuzzyMemory fuzzyMemory;
	public GhostsChaseAction( GHOST ghost, GhostsFuzzyMemory fuzzyMemory) {
		this.ghost = ghost;
		this.fuzzyMemory = fuzzyMemory;
	}
	
	@Override
	public String getActionId() {
		return ghost + "chase";
	}


	@Override
	public MOVE execute(Game game) {
		if (game.doesGhostRequireAction(ghost))       
        {
			return game.getNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghost),fuzzyMemory.lastPacmanPosition() , game.getGhostLastMoveMade(ghost), DM.EUCLID);
        }
		else
			return MOVE.NEUTRAL;
	}

}
