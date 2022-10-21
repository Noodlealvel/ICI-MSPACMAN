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
	
	static int NearestGhostToPacman(Game game) {
		return 0;
	}
	
	static boolean PacmanCloseToPPill(Game game, int dist) {
		return false;
	}
	
	static boolean PacmanCloseToGhost(Game game,GHOST ghost, int dist) {
		return false;
	}
	
}
