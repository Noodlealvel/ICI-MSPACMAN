package es.ucm.fdi.ici.c2223.practica2.grupo04.MsPacmanFSM.MsPacmanActions;

import es.ucm.fdi.ici.Action;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class SearchOptimalPathTowardsEdiblesAction implements Action {

	@Override
	public String getActionId() {
		return "MsPacman hunts the ghosts";
	}

	//Simplemente cambia respecto a las actions parecidas en que le da igual el tiempo que les queda para ser comidos
	@Override
	public MOVE execute(Game game) {
		GHOST nearestedibleghost;
		nearestedibleghost= getNearestGhost(game, game.getPacmanCurrentNodeIndex(), true);
		if (nearestedibleghost!=null)
		{
			return game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(nearestedibleghost), game.getPacmanLastMoveMade(), DM.EUCLID);	
		}
			return MOVE.NEUTRAL;
	}

	private GHOST getNearestGhost(Game game,int nodeIndex, boolean edible) {
		GHOST nearestGhost = null;
		double shortestDistance = -1;
		double distanceGhost = 0;
		for (GHOST ghost : GHOST.values()) {
			if((game.isGhostEdible(ghost) == edible && game.getGhostLairTime(ghost) == 0 )) {
				distanceGhost = game.getDistance(nodeIndex, game.getGhostCurrentNodeIndex(ghost), DM.EUCLID);
				if((shortestDistance == -1 || distanceGhost < shortestDistance)) {
					nearestGhost = ghost;
					shortestDistance = distanceGhost;
				}
			}
		}
		return nearestGhost;
	}

}
