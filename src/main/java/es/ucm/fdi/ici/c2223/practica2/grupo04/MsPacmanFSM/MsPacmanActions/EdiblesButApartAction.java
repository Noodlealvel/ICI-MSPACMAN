package es.ucm.fdi.ici.c2223.practica2.grupo04.MsPacmanFSM.MsPacmanActions;

import es.ucm.fdi.ici.Action;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class EdiblesButApartAction implements Action {

	final static private int EATTIMELIMIT = 20;
	
	@Override
	public String getActionId() {
		return "MsPacman chases ghost carefully";
	}

	@Override
	public MOVE execute(Game game) {
		GHOST nearestedibleghost;
		nearestedibleghost= getNearestGhost(game, game.getPacmanCurrentNodeIndex(), EatLimiterTimer(game), true);
		if (nearestedibleghost!=null)
		{
			return game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(nearestedibleghost), game.getPacmanLastMoveMade(), DM.EUCLID);	
		}
			return MOVE.NEUTRAL;
	}

	private GHOST getNearestGhost(Game game,int nodeIndex, int limit, boolean edible) {
		GHOST nearestGhost = null;
		double shortestDistance = -1;
		double distanceGhost = 0;
		for (GHOST ghost : GHOST.values()) {
			if((game.isGhostEdible(ghost) == edible && game.getGhostLairTime(ghost) == 0 )) {
				distanceGhost = game.getDistance(nodeIndex, game.getGhostCurrentNodeIndex(ghost), DM.EUCLID);
				if((shortestDistance == -1 || distanceGhost < shortestDistance) && game.getGhostEdibleTime(ghost) <= limit) {
					nearestGhost = ghost;
					shortestDistance = distanceGhost;
				}
			}
		}
		return nearestGhost;
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
}