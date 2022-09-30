package es.ucm.fdi.ici.c2223.practica1.grupo04;

import java.awt.Color;

import pacman.controllers.PacmanController;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.GameView;

public class AlgoritmicPacman extends PacmanController {

	final static private int FLEELIMIT = 50;
	final static private int EATLIMIT = 30;
	final static private int EATTIMELIMIT = 40;
	final static private int PPILLDANGERRADIUS = 60;
	public AlgoritmicPacman()	{

			 super();

			 setName("MsPacMan01");

			 setTeam("Team04");

	 }
	
	private int getFleeingDistance(Game game) {
		switch (game.getPacmanNumberOfLivesRemaining()) {
	    case 1:
	    	return FLEELIMIT;
	    case 2:
	    	return (int) (FLEELIMIT * 0.8);
	    case 3:
	    	return (int)(FLEELIMIT / 2);
	    default:
	    	return FLEELIMIT;
		}
	}
	
	@Override
	public MOVE getMove(Game game, long timeDue) {
		int limit = getFleeingDistance(game);
		int pacmanIndex = game.getPacmanCurrentNodeIndex();
		GHOST nearestGhost = getNearestGhost(game,pacmanIndex,limit, false);
		if(nearestGhost != null) {
			if (game.getDistance(game.getGhostCurrentNodeIndex(nearestGhost),pacmanIndex, DM.PATH) < limit/2){
				GameView.addLines(game,Color.RED,pacmanIndex,game.getGhostCurrentNodeIndex(nearestGhost));
				return game.getNextMoveAwayFromTarget(pacmanIndex, indexOfNearestPPill(game), DM.PATH);
			}
			GameView.addLines(game,Color.MAGENTA,pacmanIndex,game.getGhostCurrentNodeIndex(nearestGhost));
			return game.getApproximateNextMoveAwayFromTarget(pacmanIndex, game.getGhostCurrentNodeIndex(nearestGhost), game.getPacmanLastMoveMade(), DM.EUCLID);
		}
		GHOST nearestInactiveGhost = getNearestGhost(game,pacmanIndex,EATLIMIT, true);
		if(nearestInactiveGhost != null && game.getGhostEdibleTime(nearestInactiveGhost) >= EATTIMELIMIT) {
			GameView.addLines(game,Color.GREEN,pacmanIndex,game.getGhostCurrentNodeIndex(nearestInactiveGhost));
			return game.getNextMoveTowardsTarget(pacmanIndex, game.getGhostCurrentNodeIndex(nearestInactiveGhost), game.getPacmanLastMoveMade(), DM.EUCLID);
		}
		if(!isNearestPPillDangerRadiusSafe(game) && game.getShortestPathDistance(indexOfNearestPPill(game), pacmanIndex) < PPILLDANGERRADIUS) {
			GameView.addLines(game,Color.BLUE,pacmanIndex,indexOfNearestPPill(game));
			return game.getNextMoveTowardsTarget(pacmanIndex, indexOfNearestPPill(game), game.getPacmanLastMoveMade(), DM.PATH);
		}
		
		int nearPillIndex = indexOfNearestPill(game);
		GameView.addLines(game,Color.CYAN,pacmanIndex,nearPillIndex);
		return game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(),nearPillIndex,game.getPacmanLastMoveMade(), DM.EUCLID);
		
	}
	
	private boolean isNearestPPillDangerRadiusSafe(Game game) {
		int nearestPPill = indexOfNearestPPill(game);
		if(indexOfNearestPPill(game) == -1)
			return true;
		int ghostNumber = getGhostsWithinRadius(game,nearestPPill,PPILLDANGERRADIUS);
		if(ghostNumber >= 2)
			return false;
		return true;
		
	}
	
	private int getGhostsWithinRadius(Game game, int nodeIndex, int radius) {
		int ghostNumber = 0;
		for (GHOST ghost : GHOST.values()) {
			if(game.isGhostEdible(ghost) == false && game.getGhostLairTime(ghost) == 0) {
				if(game.getDistance(nodeIndex, game.getGhostCurrentNodeIndex(ghost), DM.PATH) <= radius) {
					ghostNumber++;
				}
			}
		}
		return ghostNumber;
	}
	
	private int indexOfNearestPPill(Game game) {
		double powerPillDistance;
		double shortestDistance = -1;
		int nearestPPill = -1;
		for(int pillNode : game.getActivePowerPillsIndices()) {
			powerPillDistance = game.getDistance(game.getPacmanCurrentNodeIndex(), pillNode,DM.EUCLID);
			if(powerPillDistance < shortestDistance || shortestDistance == -1) {
				shortestDistance = powerPillDistance;
				nearestPPill = pillNode;
			}
		}
		return nearestPPill;
	}
	private int indexOfNearestPill(Game game) {
		double pillDistance;
		double shortestDistance = -1;
		int nearestPill = -1;
		for(int pillNode : game.getActivePillsIndices()) {
			pillDistance = game.getDistance(game.getPacmanCurrentNodeIndex(), pillNode,DM.EUCLID);
			if(pillDistance < shortestDistance || shortestDistance == -1) {
				shortestDistance = pillDistance;
				nearestPill = pillNode;
			}
		}
		return nearestPill;
	}
	private GHOST getNearestGhost(Game game,int nodeIndex, int limit, boolean edible) {
		GHOST nearestGhost = null;
		double shortestDistance = -1;
		double distanceGhost = 0;
		for (GHOST ghost : GHOST.values()) {
			if(game.isGhostEdible(ghost) == edible && game.getGhostLairTime(ghost) == 0) {
				distanceGhost = game.getDistance(nodeIndex, game.getGhostCurrentNodeIndex(ghost), DM.EUCLID);
				if((shortestDistance == -1 || distanceGhost < shortestDistance) && distanceGhost <= limit) {
					nearestGhost = ghost;
					shortestDistance = distanceGhost;
				}
			}
		}
		return nearestGhost;
	}

}
