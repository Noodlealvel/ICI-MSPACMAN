package es.ucm.fdi.ici.c2223.practica1.grupo04;

import java.awt.Color;
import java.util.EnumMap;
import java.util.Random;

import pacman.controllers.GhostController;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.GameView;

public class Ghosts extends GhostController {

	private EnumMap<GHOST, MOVE> moves = new EnumMap<GHOST, MOVE>(GHOST.class);
	private MOVE[] allMoves = MOVE.values();
	private Random rnd = new Random();
	private int[] ppIndexes;
	int pacmanPos;
	private final static int LIMITPPCLOSE= 20;
	private final static GHOST AGGRESSIVE = GHOST.BLINKY;
	private final static GHOST PPHUNTER = GHOST.PINKY;
	private final static GHOST FLANKING = GHOST.SUE;
	private final static GHOST CENTER = GHOST.INKY;
	private static final int GHOSTCHASELIMIT = 50;
	private static final int PPILLCLOSELIMIT = 50;
	
	
	public Ghosts()	{

		 super();

		 setName("Ghosts T");

		 setTeam("Team04-2223");

}
	@Override
	public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
		moves.clear();
		int pacmanPos = game.getPacmanCurrentNodeIndex();
		ppIndexes=game.getActivePowerPillsIndices();
		
		if (game.doesGhostRequireAction(PPHUNTER)) // Fantasma que va hacia PPs excepto si queda solo una
		{
			if (game.isGhostEdible(PPHUNTER)) {
				moves.put(PPHUNTER,
						game.getNextMoveAwayFromTarget(game.getGhostCurrentNodeIndex(PPHUNTER), pacmanPos,
								game.getGhostLastMoveMade(PPHUNTER), DM.PATH));
			} else if (ppIndexes.length > 1) {
				chasePowerPill(PPHUNTER, game);
			} else {
				if (rnd.nextFloat() < 0.5) {
					chasePowerPill(PPHUNTER, game);
				} else {
					moves.put(PPHUNTER,
							game.getNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(PPHUNTER),
									pacmanPos, game.getGhostLastMoveMade(PPHUNTER), DM.PATH));
				}
			}
		}

		if (game.doesGhostRequireAction(AGGRESSIVE)) // Fantasma agresivo
		{

			if (game.isGhostEdible(AGGRESSIVE) || pacmanCloseToPPill(game)) {
				moves.put(AGGRESSIVE,
						game.getNextMoveAwayFromTarget(game.getGhostCurrentNodeIndex(AGGRESSIVE),
								pacmanPos, game.getGhostLastMoveMade(AGGRESSIVE), DM.EUCLID));
			} else {
				GameView.addLines(game, Color.red, pacmanPos, game.getGhostCurrentNodeIndex(AGGRESSIVE));
				moves.put(AGGRESSIVE,
						game.getNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(AGGRESSIVE), pacmanPos,
								game.getGhostLastMoveMade(AGGRESSIVE), DM.EUCLID));
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
			else if (game.getNumberOfActivePowerPills() > 0 &&  game.getDistance(game.getGhostCurrentNodeIndex(CENTER), nearestPPill, DM.EUCLID) <= PPILLCLOSELIMIT) {
				moves.put(CENTER,
						game.getNextMoveAwayFromTarget(game.getGhostCurrentNodeIndex(CENTER),
								nearestPPill, game.getGhostLastMoveMade(CENTER), DM.EUCLID));
			}
			else moves.put(CENTER,
					game.getNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(CENTER), pacmanPos,
							game.getGhostLastMoveMade(CENTER), DM.EUCLID));
			
		}
		
		if (game.doesGhostRequireAction(FLANKING)) //Fantasma que intenta ir alejado del resto pero tambi�n perseguir a Pacman
		{
			GHOST nearestGhost = getNearestGhost(game, game.getGhostCurrentNodeIndex(FLANKING), GHOSTCHASELIMIT);
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
			else{moves.put(FLANKING,
					game.getNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(FLANKING), pacmanPos,
							game.getGhostLastMoveMade(FLANKING), DM.EUCLID));}
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
		for(int pillNode : game.getActivePowerPillsIndices()) {
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
		int ghostPos=game.getGhostCurrentNodeIndex(ghostType);
		for (int PP: ppIndexes)
		{
			if (game.getDistance(PP, pacmanPos, DM.PATH) >LIMITPPCLOSE)
			{
				moves.put(ghostType, game.getApproximateNextMoveTowardsTarget(ghostPos, PP, game.getGhostLastMoveMade(ghostType), DM.PATH));
				break;
			}
		}
		moves.put(ghostType, allMoves[rnd.nextInt(allMoves.length)]);
	}
	
	private boolean pacmanCloseToPPill(Game game) {
		for(int PPill : game.getActivePowerPillsIndices()) {
			if(game.getDistance(PPill, game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade(), DM.EUCLID) <= LIMITPPCLOSE) {
				return true;
			}
		}
		return false;
	}
}

//Uno perseguidor (siempre va a por pacman excepto cerca de pp
//otro que evite pp y vaya siempre por el medio,
//otro que va a por pps salvo que quede una, entonces random entre pp y perseguir
//otro que evite a uno para no tenerlos a todos juntos