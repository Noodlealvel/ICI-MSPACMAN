package es.ucm.fdi.ici.c2223.practica1.grupo04;
import java.util.EnumMap;
import java.util.Random;

import pacman.controllers.GhostController;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class Ghosts extends GhostController {

	private EnumMap<GHOST, MOVE> moves = new EnumMap<GHOST, MOVE>(GHOST.class);
	private MOVE[] allMoves = MOVE.values();
	private Random rnd = new Random();
	private final static GHOST AGGRESSIVE = GHOST.BLINKY;
	private final static GHOST PPHUNTER = GHOST.PINKY;
	private final static GHOST FLANKING = GHOST.SUE;
	private final static GHOST CENTER = GHOST.INKY;
	private static final int GHOSTCHASELIMIT = 60;
	private static final int PPILLCLOSELIMIT = 40;
	private static final double PACMANPPCLOSEFLEE = 40;
	private static final double SCATTERFRECUENCY = 0.1;
	private static final double CHASINGCLOSE = 30;
	
	
	public Ghosts()	{

		 super();

		 setName("Ghosts04");

		 setTeam("Team04");

}
	@Override
	public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
		moves.clear();
		int pacmanPos = game.getPacmanCurrentNodeIndex();
		
		if (game.doesGhostRequireAction(PPHUNTER)) // Fantasma que va hacia PPs excepto si queda solo una
		{
			//Si es comestible huye de pacman
			if (game.isGhostEdible(PPHUNTER)) {
				moves.put(PPHUNTER,
						game.getNextMoveAwayFromTarget(game.getGhostCurrentNodeIndex(PPHUNTER), pacmanPos,
								game.getGhostLastMoveMade(PPHUNTER), DM.PATH));
			} else if (game.getNumberOfActivePowerPills() > 1) { //Si hay mas de una PP intenta ir a la PP mas cercana
				chasePowerPill(PPHUNTER, game);
			} else {
				if (game.getNumberOfActivePowerPills() == 1 && rnd.nextFloat() < 0.5) { //Si solo hay una o va a ella o hace otra cosa 1/2 de las veces
					chasePowerPill(PPHUNTER, game);
				} else {
					chasePacman(game, pacmanPos,PPHUNTER);
				}
			}
		}

		if (game.doesGhostRequireAction(AGGRESSIVE)) // Fantasma agresivo
		{
			//Si es comestible huye
			if (game.isGhostEdible(AGGRESSIVE) || pacmanCloseToPPill(game)) {
				moves.put(AGGRESSIVE,
						game.getNextMoveAwayFromTarget(game.getGhostCurrentNodeIndex(AGGRESSIVE),
								pacmanPos, game.getGhostLastMoveMade(AGGRESSIVE), DM.EUCLID));
			} else {
				chasePacman(game, pacmanPos,AGGRESSIVE);
			}
		}
	
		
		if (game.doesGhostRequireAction(CENTER)) //Fantasma que intenta estar por el centro
		{
			int nearestPPill = getIndexOfClosestPPILL(game, CENTER);
			if (game.isGhostEdible(CENTER) || pacmanCloseToPPill(game)) {
				moves.put(CENTER,
						game.getNextMoveAwayFromTarget(game.getGhostCurrentNodeIndex(CENTER),
								pacmanPos, game.getGhostLastMoveMade(CENTER), DM.EUCLID));
			}
			else if (game.getDistance(game.getGhostCurrentNodeIndex(CENTER), nearestPPill, DM.EUCLID) <= PPILLCLOSELIMIT) {
				moves.put(CENTER,
						game.getNextMoveAwayFromTarget(game.getGhostCurrentNodeIndex(CENTER),
								nearestPPill, game.getGhostLastMoveMade(CENTER), DM.EUCLID));
			}
			else {
				chasePacman(game, pacmanPos,CENTER);}
			
		}
		
		if (game.doesGhostRequireAction(FLANKING)) //Fantasma que intenta ir alejado del resto pero tambien perseguir a Pacman
		{
			
			GHOST nearestGhost = getNearestGhost(game, game.getGhostCurrentNodeIndex(FLANKING), GHOSTCHASELIMIT);
			//Si es comestible huye
			if(game.isGhostEdible(FLANKING)|| pacmanCloseToPPill(game)) {
				moves.put(FLANKING,
						game.getNextMoveAwayFromTarget(game.getGhostCurrentNodeIndex(FLANKING), pacmanPos,
								game.getGhostLastMoveMade(FLANKING), DM.PATH));
			}
			else if(game.getGhostEdibleTime(AGGRESSIVE) > 0 && game.getShortestPathDistance(game.getGhostCurrentNodeIndex(FLANKING), game.getGhostCurrentNodeIndex(nearestGhost)) <= GHOSTCHASELIMIT) {
				moves.put(FLANKING,
						game.getNextMoveAwayFromTarget(game.getGhostCurrentNodeIndex(FLANKING), game.getGhostCurrentNodeIndex(nearestGhost),
								game.getGhostLastMoveMade(FLANKING), DM.EUCLID));
				
			}
			else if (isGhostInPath(game, pacmanPos, AGGRESSIVE)) {
				moves.put(FLANKING,
					allMoves[rnd.nextInt(3)]);
			}
			else chasePacman(game, pacmanPos,FLANKING);
		}
		
		return moves;
	}
	
	private boolean isGhostInPath(Game game, int pacmanPos, GHOST ghost) {
		int[] path= game.getShortestPath(game.getGhostCurrentNodeIndex(FLANKING), pacmanPos, game.getGhostLastMoveMade(FLANKING));
		for(int i : path) {
			if(game.getGhostCurrentNodeIndex(ghost) == i)
				return true;
		}
		return false;
	}
	private GHOST getNearestGhost(Game game,int nodeIndex, int limit) {
		GHOST nearestGhost = null;
		double shortestDistance = -1;
		double distanceGhost = 0;
		for (GHOST ghost : GHOST.values()) {
			if(game.getGhostLairTime(ghost) == 0 ) {
				distanceGhost = game.getDistance(nodeIndex, game.getGhostCurrentNodeIndex(ghost), DM.EUCLID);
				if((shortestDistance == -1 || distanceGhost < shortestDistance) && distanceGhost <= limit) {
					nearestGhost = ghost;
					shortestDistance = distanceGhost;
				}
			}
		}
		return nearestGhost;
	}
	
	private int getIndexOfClosestPPILL(Game game, GHOST ghost) {
		double powerPillDistance;
		double shortestDistance = -1;
		int nearestPPill = -1;
		for(int pillNode : game.getPowerPillIndices()) {
			powerPillDistance = game.getDistance(game.getPacmanCurrentNodeIndex(), pillNode,DM.EUCLID);
			if(powerPillDistance < shortestDistance || shortestDistance == -1) {
				shortestDistance = powerPillDistance;
				nearestPPill = pillNode;
			}
		}
		return nearestPPill;
	}
	public void chasePowerPill (GHOST ghostType, Game game)
	{
		int currentDistance = 0;
		int minDistance = Integer.MAX_VALUE;
		int chosenPP = 0;
		int ghostPos = game.getGhostCurrentNodeIndex(ghostType);
		for (int PP: game.getActivePowerPillsIndices())
		{
			currentDistance = (int) game.getDistance(PP, game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade(), DM.PATH);
			if (currentDistance < minDistance)
			{
				chosenPP = PP;
				minDistance = currentDistance;
			}
		}
		if(game.getDistance(chosenPP, ghostPos,game.getGhostLastMoveMade(ghostType), DM.PATH) > minDistance) {
			moves.put(ghostType, game.getNextMoveTowardsTarget(ghostPos, chosenPP, game.getGhostLastMoveMade(ghostType), DM.EUCLID));
		}
		else
			moves.put(ghostType, game.getNextMoveTowardsTarget(ghostPos, game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(ghostType), DM.EUCLID));

	}
	
	private boolean pacmanCloseToPPill(Game game) {
		for(int PPill : game.getActivePowerPillsIndices()) {
			if(game.getDistance(PPill, game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade(), DM.EUCLID) <= PACMANPPCLOSEFLEE) {
				return true;
			}
		}
		return false;
	}
	
	private void chasePacman(Game game, int pacmanPos, GHOST ghostType) {
		if (rnd.nextDouble() < SCATTERFRECUENCY && game.getDistance(pacmanPos, game.getGhostCurrentNodeIndex(ghostType), game.getGhostLastMoveMade(ghostType), DM.EUCLID) < CHASINGCLOSE) {
			//huye de Pacman segun el scatter frequency y si pacman esta lo suficientemente cerca
			moves.put(ghostType,
					game.getNextMoveAwayFromTarget(game.getGhostCurrentNodeIndex(ghostType), pacmanPos,
							game.getGhostLastMoveMade(ghostType), DM.PATH));
		}
		else //Persigue a pacman
		moves.put(ghostType,
				game.getNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghostType),
						pacmanPos, game.getGhostLastMoveMade(ghostType), DM.EUCLID));
	}
}