package es.ucm.fdi.ici.c2223.practica2.grupo04.GhostsFSM.Actions;

import es.ucm.fdi.ici.Action;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class GhostsKillPacmanAction implements Action {

	GHOST ghost;
	public GhostsKillPacmanAction( GHOST ghost) {
		this.ghost = ghost;
	}
	
	@Override
	public String getActionId() {
		return ghost + "KillPacMan";
	}


	@Override
	public MOVE execute(Game game) {
		if (game.doesGhostRequireAction(ghost))       
        {
			return game.getNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghost), game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(ghost), DM.EUCLID);
        }
		else
			return MOVE.NEUTRAL;
	}

}
