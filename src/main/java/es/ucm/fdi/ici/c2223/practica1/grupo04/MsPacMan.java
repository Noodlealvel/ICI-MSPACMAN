package es.ucm.fdi.ici.c2223.practica1.grupo04;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import pacman.controllers.PacmanController;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MsPacMan extends PacmanController {

	final static private int FLEELIMIT = 30;
	final static private int EATLIMIT = 80;
	final static private int EATTIMELIMIT = 20;
	final static private int PPILLDANGERRADIUS = 90;
	final static private int CHASELIMITER = 15;
	final static private int EXPLORERADIUS = 30;
	final static private DM MEASUREMETHOD = DM.EUCLID;
	private GHOST lastNearGhost;
	private int closeChaseCounter;
	public MsPacMan()	{

			 super();
			 
			 closeChaseCounter = 0;

			 setName("MsPacMan T");

			 setTeam("Team04");

	 }
	
	private int getFleeingDistance(Game game) {
		//Segun las vidas es mas agresivo o huye con mas facilidad
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
		if(lastMove == null)
			lastMove = MOVE.UP;
		int limit = getFleeingDistance(game);
		int pacmanIndex = game.getPacmanCurrentNodeIndex();
		GHOST nearestGhost = getNearestGhost(game,pacmanIndex,limit, false);
		if(nearestGhost != null) {
			//El fantasma que tiene mas cerca no es comestible
			if (game.getDistance(game.getGhostCurrentNodeIndex(nearestGhost),pacmanIndex,game.getPacmanLastMoveMade(), MEASUREMETHOD) < limit/2){
				//El fantasma est� "muy cerca"
				if(nearestGhost != lastNearGhost) {
					//Si el fantasma esta muy cerca pero aun no lleva un rato persiguiendole intenta huir
					return fleeFromNearestGhost(nearestGhost, pacmanIndex, game);
				}
				//Si el fantasma esta muy cerca y si que lleva un rato persiguiendole este va hacia una PPill
				closeChaseCounter = 0;
				if (indexOfNearestPPill(game) != -1) {
					return game.getNextMoveTowardsTarget(pacmanIndex, indexOfNearestPPill(game), game.getPacmanLastMoveMade(), MEASUREMETHOD);
				}
				//Si no hay PPill simplemente huye
				return game.getNextMoveAwayFromTarget(pacmanIndex, game.getGhostCurrentNodeIndex(nearestGhost), game.getPacmanLastMoveMade(), MEASUREMETHOD);
			}
			//Si esta cerca pero no "muy cerca" simplemente huye,
			closeChaseCounter = 0;
			return game.getNextMoveAwayFromTarget(pacmanIndex, game.getGhostCurrentNodeIndex(nearestGhost), game.getPacmanLastMoveMade(), MEASUREMETHOD);
		}
		//Resetea el chase counter porque nadie le persique
		closeChaseCounter = 0;
		GHOST nearestEdibleGhost = getNearestGhost(game,pacmanIndex,EatLimiter(game), true);
		if(nearestEdibleGhost != null && game.getGhostEdibleTime(nearestEdibleGhost) >= EatLimiterTimer(game)) {
			//Si esta cerca del fantasma comestible se lo come si le sale rentable por puntuacion y si no le queda mucho para quitarse el comestible
			return game.getNextMoveTowardsTarget(pacmanIndex, game.getGhostCurrentNodeIndex(nearestEdibleGhost), game.getPacmanLastMoveMade(), MEASUREMETHOD);
		}
		if(!isNearestPPillDangerRadiusSafe(game) && game.getShortestPathDistance(indexOfNearestPPill(game), pacmanIndex, game.getPacmanLastMoveMade()) < PPILLDANGERRADIUS) {
			//Si Pacman esta en el radio de peligro de una PPill activa y esta tiene 2+ fantasmas alrededor intenta ir hacia la PPill
			return game.getNextMoveTowardsTarget(pacmanIndex, indexOfNearestPPill(game), game.getPacmanLastMoveMade(), MEASUREMETHOD);
		}
		
		//Cuando esta en condiciones normales busca el camino con mas Pills para ir por este
		int nearPillIndex = indexOfNearestOptimalPathToPill(game, pacmanIndex, EXPLORERADIUS);
		return game.getNextMoveTowardsTarget(pacmanIndex,nearPillIndex, game.getPacmanLastMoveMade(), MEASUREMETHOD);
		
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
		//Funcion que considera si el radio alrededor de la Ppill mas cercana es seguro o no
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
				if(game.getDistance(nodeIndex, game.getGhostCurrentNodeIndex(ghost), DM.EUCLID) <= radius) {
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
			powerPillDistance = game.getDistance(game.getPacmanCurrentNodeIndex(), pillNode,MEASUREMETHOD);
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
		for(int pillNode : game.getPillIndices()) {
			if(nodeHasActivePill(game, pillNode)) {
				pillDistance = game.getDistance(pacmanIndex, pillNode,game.getPacmanLastMoveMade(),MEASUREMETHOD);
				if(pillDistance < shortestDistance || shortestDistance == -1) {
					shortestDistance = pillDistance;
					nearestPill = pillNode;
				}
			}
		}
		return nearestPill;
		
	}
	
	private boolean nodeHasActivePill(Game game, int node) {
		return game.getPillIndex(node) != -1 && game.isPillStillAvailable(game.getPillIndex(node));
	}
	
	private int indexOfNearestOptimalPathToPill(Game game, int pacmanIndex, int radius) {
		int chosenPill = indexOfNearestPill(game,pacmanIndex);
		List<Integer> nearPills = new ArrayList<Integer>();
		for(int pillNode : game.getPillIndices()) {
			if (nodeHasActivePill(game, pillNode) && game.getDistance(pacmanIndex, pillNode, game.getPacmanLastMoveMade(),MEASUREMETHOD) <= radius ) {
				nearPills.add(Integer.valueOf(pillNode));
			}
		}
		int maxScore = Integer.MIN_VALUE;
		int currentScore = 0;
		if(nearPills.isEmpty()) {
			return indexOfNearestOptimalPathToPill(game, pacmanIndex, radius *2);
		}
		for (Integer pillNode : nearPills) {
			currentScore = 0;
			int[] path = game.getShortestPath(pacmanIndex, pillNode.intValue(), game.getPacmanLastMoveMade());
			for(int node : path) {
				if(nodeHasActivePill(game, node)) {
					currentScore++;
				}
				else{
					currentScore--;
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
				distanceGhost = game.getDistance(nodeIndex, game.getGhostCurrentNodeIndex(ghost), MEASUREMETHOD);
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
		return game.getNextMoveAwayFromTarget(pacmanIndex, game.getGhostCurrentNodeIndex(nearestGhost), game.getPacmanLastMoveMade(), MEASUREMETHOD);
	}

}
