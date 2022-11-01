package es.ucm.fdi.ici.c2223.practica2.grupo04.MsPacmanFSM;

import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;

public class MsPacmanUtils {

	public static int getNearestPP(Game game, int limit) {
		double powerPillDistance;
		double shortestDistance = -1;
		int nearestPPill = -1;
		for (int pillNode : game.getActivePowerPillsIndices()) {
			powerPillDistance = game.getDistance(game.getPacmanCurrentNodeIndex(), pillNode, DM.PATH);
			if ((powerPillDistance < shortestDistance && powerPillDistance < limit) || shortestDistance == -1) {
				shortestDistance = powerPillDistance;
				nearestPPill = pillNode;
			}
		}
		return nearestPPill;
	}

	public static int getNearestPP(Game game) {
		return game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), game.getActivePowerPillsIndices(), DM.EUCLID);
	}
	public static int getNearestPill(Game game) {
		return game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), game.getActivePillsIndices(), DM.EUCLID);
	}

	public static GHOST getNearestGhost(Game game, int nodeIndex, int timeLimit, boolean edible) {
		GHOST nearestGhost = null;
		double shortestDistance = -1;
		double distanceGhost = 0;
		for (GHOST ghost : GHOST.values()) {
			if((game.isGhostEdible(ghost) == edible && game.getGhostLairTime(ghost) == 0)) {
				distanceGhost = game.getDistance(nodeIndex, game.getGhostCurrentNodeIndex(ghost), DM.EUCLID);
				if((shortestDistance == -1 || distanceGhost < shortestDistance) && game.getGhostEdibleTime(ghost) <= timeLimit) {
					nearestGhost = ghost;
					shortestDistance = distanceGhost;
				}
			}
		}
		return nearestGhost;
	}

	public static GHOST getNearestGhost(Game game, int nodeIndex, boolean edible) {
		GHOST nearestGhost = null;
		double shortestDistance = -1;
		double distanceGhost = 0;
		for (GHOST ghost : GHOST.values()) {
			if((game.isGhostEdible(ghost) == edible && game.getGhostLairTime(ghost) == 0)) {
				distanceGhost = game.getDistance(nodeIndex, game.getGhostCurrentNodeIndex(ghost), DM.EUCLID);
				if((shortestDistance == -1 || distanceGhost < shortestDistance)) {
					nearestGhost = ghost;
					shortestDistance = distanceGhost;
				}
			}
		}
		return nearestGhost;
	}

	public static GHOST getNearestGhostAtDistance(Game game, int nodeIndex, int i, boolean edible) {
		GHOST nearestGhost = null;
		double shortestDistance = -1;
		double distanceGhost = 0;
		for (GHOST ghost : GHOST.values()) {
			if((game.isGhostEdible(ghost) == edible && game.getGhostLairTime(ghost) == 0)) {
				distanceGhost = game.getDistance(nodeIndex, game.getGhostCurrentNodeIndex(ghost), DM.EUCLID);
				if((shortestDistance == -1 || distanceGhost < shortestDistance) && distanceGhost <= i) {
					nearestGhost = ghost;
					shortestDistance = distanceGhost;
				}
			}
		}
		return nearestGhost;
	}
}
