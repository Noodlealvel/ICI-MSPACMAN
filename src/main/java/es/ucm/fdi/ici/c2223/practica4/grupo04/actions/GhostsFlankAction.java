package es.ucm.fdi.ici.c2223.practica4.grupo04.actions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2223.practica2.grupo04.GhostsFSM.GhostsUtils;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class GhostsFlankAction implements Action {


	GHOST ghost;
	public GhostsFlankAction( GHOST ghost) {
		this.ghost = ghost;
	}
	
	@Override
	public String getActionId() {
		return ghost.toString() + "flank";
	}

	@Override
	public MOVE execute(Game game) {
		if (game.doesGhostRequireAction(ghost))       
        {
			return game.getNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghost), GhostsUtils.getNodeBetweenPacmanAndPpill(game),game.getGhostLastMoveMade(ghost) ,DM.PATH);
        }
		else
			return MOVE.NEUTRAL;
		}

}
