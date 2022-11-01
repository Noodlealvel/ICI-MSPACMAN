package es.ucm.fdi.ici.c2223.practica2.grupo04.MsPacmanFSM.MsPacmanActions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2223.practica2.grupo04.MsPacmanFSM.MsPacmanUtils;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class FleeAction implements Action {

	@Override
	public String getActionId() {
		return "MsPacman flees";
	}

	@Override
	public MOVE execute(Game game) {
		GHOST nearestghost;
		nearestghost = MsPacmanUtils.getNearestGhost(game, game.getPacmanCurrentNodeIndex(), 30, false);
		if(nearestghost!=null)
		{
			return game.getApproximateNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(),
					game.getGhostCurrentNodeIndex(nearestghost), game.getPacmanLastMoveMade(), DM.EUCLID);
		}
		return MOVE.NEUTRAL;
	}
}
