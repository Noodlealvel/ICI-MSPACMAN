package es.ucm.fdi.ici.c2223.practica2.grupo04.GhostsFSM;

import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;

public class GhostsUtils {

	static int NearestPill(Game game, GHOST ghost) {
		int nearest = Integer.MAX_VALUE;
		int path;
		for (int pill : game.getActivePillsIndices()) {
			path = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghost), pill);
			if (path < nearest)
				return pill;
		}
		return -1;
	}
	static int NearestActivePPill(Game game, GHOST ghost) {
		int nearest = Integer.MAX_VALUE;
		int path;
		for (int powerpill : game.getActivePowerPillsIndices()) {
			path = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghost), powerpill);
			if (path < nearest)
				return powerpill;
		}
		return -1;
	}
	static int NearestTunnelNode(Game game, GHOST ghost) {
		return -1;
	}
	
	static GHOST NearestGhostToPacman(Game game) {
		GHOST nearest = null;
		int minDist = Integer.MAX_VALUE;
		for(GHOST ghost : GHOST.values()) {
			int dist = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghost),game.getPacmanCurrentNodeIndex());
			if(dist < minDist) {
				minDist = dist;
				nearest = ghost;
			}
		}
		return nearest;
	}
	
	static boolean PacmanCloseToPPill(Game game, int dist) {
		int[] indicesPowerPill = game.getActivePowerPillsIndices();
		int nPowerPill = game.getNumberOfActivePowerPills();
		int i = 0;
		boolean isClose = false;
		while((i < nPowerPill) && (isClose==false)) {
			if(game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), indicesPowerPill[i]) <= dist)
				isClose = true;
			i++;
		}
		return isClose;	
	}
	
	static boolean PacmanCloseToGhost(Game game,GHOST ghost, int dist) {
		boolean isClose = false;
		if(game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghost)) <= dist)
			isClose = true;
		return isClose;	
	}
	
}
