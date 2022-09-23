package es.ucm.fdi.ici.c2223.practica0.grupoIndividual;

import pacman.controllers.PacmanController;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class PacManRunAway extends PacmanController{

	
	@Override
	public MOVE getMove(Game game, long timeDue) {
		GHOST nearestGhost = null;
		int shortestDistance = -1;
		int distanceGhost = 0;
		for (GHOST a : GHOST.values()) {
			distanceGhost = (int) game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(a), DM.EUCLID);
			if(shortestDistance == -1 || distanceGhost < shortestDistance) {
				nearestGhost = a;
				shortestDistance = distanceGhost;
			}
		}
		if(game.getPacmanLastMoveMade() != null)
			return game.getApproximateNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(nearestGhost), game.getPacmanLastMoveMade(), DM.EUCLID);
		else {
			return MOVE.DOWN; 
		}
	}

}
