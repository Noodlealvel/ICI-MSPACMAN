package es.ucm.fdi.ici.c2223.practica1.grupo04;

import java.awt.Color;

import pacman.controllers.PacmanController;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.GameView;

public class AlgoritmicPacman extends PacmanController {

	@Override
	public MOVE getMove(Game game, long timeDue) {
		int limit = 50;
		GHOST nearestGhost = getNearestGhost(game ,limit, false);
		if(nearestGhost != null) {
			GameView.addLines(game,Color.MAGENTA,game.getPacmanCurrentNodeIndex(),game.getGhostCurrentNodeIndex(nearestGhost));
			return game.getApproximateNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(nearestGhost), game.getPacmanLastMoveMade(), DM.EUCLID);
		}
		nearestGhost = getNearestGhost(game,limit/2, true);
		if(nearestGhost != null) {
			GameView.addLines(game,Color.GREEN,game.getPacmanCurrentNodeIndex(),game.getGhostCurrentNodeIndex(nearestGhost));
			return game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(nearestGhost), game.getPacmanLastMoveMade(), DM.EUCLID);
		}
		int nearPillIndex = indexOfNearestPill(game);
		return game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(),nearPillIndex,game.getPacmanLastMoveMade(), DM.EUCLID);
	}
	private int indexOfNearestPill(Game game) {
		double pillDistance;
		double shortestDistance = -1;
		int nearestPill = 0;
		for(int pillNode : game.getActivePillsIndices()) {
			pillDistance = game.getDistance(game.getPacmanCurrentNodeIndex(), pillNode,DM.EUCLID);
			if(pillDistance < shortestDistance || shortestDistance == -1) {
				shortestDistance = pillDistance;
				nearestPill = pillNode;
			}
		}
		return nearestPill;
	}
	private GHOST getNearestGhost(Game game, int limit, boolean edible) {
		GHOST nearestGhost = null;
		double shortestDistance = -1;
		double distanceGhost = 0;
		for (GHOST ghost : GHOST.values()) {
			if(game.isGhostEdible(ghost) == edible) {
				distanceGhost = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghost), DM.EUCLID);
				if((shortestDistance == -1 || distanceGhost < shortestDistance) && distanceGhost <= limit) {
					nearestGhost = ghost;
					shortestDistance = distanceGhost;
				}
			}
		}
		return nearestGhost;
	}

}
