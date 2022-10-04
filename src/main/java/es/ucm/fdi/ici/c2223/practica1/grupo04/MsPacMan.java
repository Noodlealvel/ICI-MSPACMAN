package es.ucm.fdi.ici.c2223.practica1.grupo04;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import pacman.controllers.PacmanController;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.GameView;

public class MsPacMan extends PacmanController {

	final static private int FLEELIMIT = 40;
	final static private int EATLIMIT = 80;
	final static private int EATTIMELIMIT = 20;
	final static private int PPILLDANGERRADIUS = 90;
	final static private int CHASELIMITER = 15;
	final static private int EXPLORERADIUS = 30;
	private GHOST lastNearGhost;
	private int closeChaseCounter;
	public MsPacMan()	{

			 super();
			 
			 closeChaseCounter = 0;

			 setName("MsPacMan T");

			 setTeam("Team04");

	 }
	
	private int getFleeingDistance(Game game) {
		switch (game.getPacmanNumberOfLivesRemaining()) {
	    case 3:
	    	return FLEELIMIT;
	    case 2:
	    	return (int) (FLEELIMIT * 0.8);
	    case 1:
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
			//El fantasma que tiene más cerca no es comestible
			if (game.getDistance(game.getGhostCurrentNodeIndex(nearestGhost),pacmanIndex, DM.PATH) < limit/2){
				//El fantasma está "muy cerca"
				if(nearestGhost != lastNearGhost) {
					//Si el fantasma está muy cerca pero aún no lleva un rato persiguiéndole intenta huir
					return fleeFromNearestGhost(nearestGhost, pacmanIndex, game);
				}
				GameView.addLines(game,Color.ORANGE,pacmanIndex,game.getGhostCurrentNodeIndex(nearestGhost));
				//Si el fantasma está muy cerca y sí que lleva un rato persiguiéndole este va hacia una PPill
				closeChaseCounter = 0;
				if (indexOfNearestPPill(game) != -1) {
					GameView.addLines(game,Color.RED,pacmanIndex,game.getGhostCurrentNodeIndex(nearestGhost));
					GameView.addLines(game,Color.RED,pacmanIndex,indexOfNearestPPill(game));
					return game.getNextMoveTowardsTarget(pacmanIndex, indexOfNearestPPill(game), DM.PATH);
				}
				//Si no hay PPill simplemente huye
				return game.getNextMoveAwayFromTarget(pacmanIndex, game.getGhostCurrentNodeIndex(nearestGhost), DM.PATH);
			}
			//Si está cerca pero no "muy cerca" simplemente huye,
			closeChaseCounter = 0;
			GameView.addLines(game,Color.MAGENTA,pacmanIndex,game.getGhostCurrentNodeIndex(nearestGhost));
			return game.getNextMoveAwayFromTarget(pacmanIndex, game.getGhostCurrentNodeIndex(nearestGhost), DM.PATH);
		}
		//Resetea el chase counter porque nadie le persique
		closeChaseCounter = 0;
		GHOST nearestEdibleGhost = getNearestGhost(game,pacmanIndex,EatLimiter(game), true);
		if(nearestEdibleGhost != null && game.getGhostEdibleTime(nearestEdibleGhost) >= EatLimiterTimer(game)) {
			//Si está cerca del fantasma comestible se lo come si le sale rentable por puntuación y si no le queda mucho para quitarse el comestible
			GameView.addLines(game,Color.GREEN,pacmanIndex,game.getGhostCurrentNodeIndex(nearestEdibleGhost));
			return game.getNextMoveTowardsTarget(pacmanIndex, game.getGhostCurrentNodeIndex(nearestEdibleGhost), game.getPacmanLastMoveMade(), DM.EUCLID);
		}
		if(!isNearestPPillDangerRadiusSafe(game) && game.getShortestPathDistance(indexOfNearestPPill(game), pacmanIndex) < PPILLDANGERRADIUS) {
			GameView.addLines(game,Color.BLUE,pacmanIndex,indexOfNearestPPill(game));
			return game.getNextMoveTowardsTarget(pacmanIndex, indexOfNearestPPill(game), game.getPacmanLastMoveMade(), DM.EUCLID);
		}
		
		int nearPillIndex = indexOfNearestOptimalPathToPill(game, pacmanIndex);
		GameView.addLines(game,Color.CYAN,pacmanIndex,nearPillIndex);
		return game.getNextMoveTowardsTarget(pacmanIndex,nearPillIndex,game.getPacmanLastMoveMade(), DM.EUCLID);
		
	}
	
	private int EatLimiter(Game game) {
		switch (game.getGhostCurrentEdibleScore()) {
	    case 1600:
	    	return (int) (EATLIMIT * 1.5);
	    case 800:
	    	return (int) (EATLIMIT * 0.8);
	    case 400:
	    	return (int)(EATLIMIT * 0.7);
	    case 200:
	    	return (int)(EATLIMIT * 0.5);
	    default:
	    	return EATLIMIT;
		}
	}

	private int EatLimiterTimer(Game game) {
		switch (game.getGhostCurrentEdibleScore()) {
	    case 1600:
	    	return (int) (EATTIMELIMIT * 0.5);
	    case 800:
	    	return (int) (EATTIMELIMIT * 0.6);
	    case 400:
	    	return (int) (EATTIMELIMIT * 0.8);
	    case 200:
	    	return (int) (EATTIMELIMIT * 1.5);
	    default:
	    	return EATTIMELIMIT;
		}
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
	private int indexOfNearestPill(Game game, int pacmanIndex) {
		double pillDistance;
		double shortestDistance = -1;
		int nearestPill = -1;
		for(int pillNode : game.getActivePillsIndices()) {
			pillDistance = game.getDistance(pacmanIndex, pillNode,DM.EUCLID);
			if(pillDistance < shortestDistance || shortestDistance == -1) {
				shortestDistance = pillDistance;
				nearestPill = pillNode;
			}
		}
		return nearestPill;
		
	}
	
	private int indexOfNearestOptimalPathToPill(Game game, int pacmanIndex) {
		int chosenPill = indexOfNearestPill(game, pacmanIndex);
		List<Integer> nearPills = new ArrayList<Integer>();
		for(int pillNode : game.getActivePillsIndices()) {
			if (game.getDistance(pacmanIndex, pillNode,DM.EUCLID) <= EXPLORERADIUS ) {
				nearPills.add(Integer.valueOf(pillNode));
			}
		}
		int maxScore = 0;
		int currentScore = 0;
		for (Integer pillNode : nearPills) {
			currentScore = 0;
			int[] path = game.getShortestPath(pacmanIndex, pillNode.intValue(), game.getPacmanLastMoveMade());
			for(int node : path) {
				if(game.getPillIndex(node) != -1 && game.isPillStillAvailable(game.getPillIndex(node))) {
					currentScore++;
				}
			}
			if(currentScore > maxScore) {
				maxScore = currentScore;
				chosenPill = pillNode.intValue();
			}
		}
			
		return chosenPill;
	}
	private GHOST getNearestGhost(Game game,int nodeIndex, int limit, boolean edible) {
		GHOST nearestGhost = null;
		double shortestDistance = -1;
		double distanceGhost = 0;
		for (GHOST ghost : GHOST.values()) {
			if((game.isGhostEdible(ghost) == edible && game.getGhostLairTime(ghost) == 0 )) {
				distanceGhost = game.getDistance(nodeIndex, game.getGhostCurrentNodeIndex(ghost), DM.PATH);
				if((shortestDistance == -1 || distanceGhost < shortestDistance) && distanceGhost <= limit) {
					nearestGhost = ghost;
					shortestDistance = distanceGhost;
				}
			}
		}
		return nearestGhost;
	}
	
	private MOVE fleeFromNearestGhost(GHOST nearestGhost, int pacmanIndex, Game game) {
		closeChaseCounter++;
		if(closeChaseCounter >= CHASELIMITER) {
			lastNearGhost = nearestGhost;
		}
		GameView.addLines(game,Color.MAGENTA,pacmanIndex,game.getGhostCurrentNodeIndex(nearestGhost));
		return game.getNextMoveAwayFromTarget(pacmanIndex, game.getGhostCurrentNodeIndex(nearestGhost), game.getPacmanLastMoveMade(), DM.EUCLID);
	}

}
