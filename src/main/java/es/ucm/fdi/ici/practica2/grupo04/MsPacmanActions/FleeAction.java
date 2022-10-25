package es.ucm.fdi.ici.practica2.grupo04.MsPacmanActions;

import es.ucm.fdi.ici.Action;
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
		nearestghost = getNearestGhost(game, game.getPacmanCurrentNodeIndex(), 30, false);
		return game.getApproximateNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(),
				game.getGhostCurrentNodeIndex(nearestghost), game.getPacmanLastMoveMade(), DM.EUCLID);
	}

	private GHOST getNearestGhost(Game game, int nodeIndex, int limit, boolean edible) {
		GHOST nearestGhost = null;
		double shortestDistance = -1;
		double distanceGhost = 0;
		for (GHOST ghost : GHOST.values()) {
			if ((game.isGhostEdible(ghost) == edible && game.getGhostLairTime(ghost) == 0)) {
				distanceGhost = game.getDistance(nodeIndex, game.getGhostCurrentNodeIndex(ghost), DM.EUCLID);
				if ((shortestDistance == -1 || distanceGhost < shortestDistance) && distanceGhost <= limit) {
					nearestGhost = ghost;
					shortestDistance = distanceGhost;
				}
			}
		}
		return nearestGhost;
	}
}
