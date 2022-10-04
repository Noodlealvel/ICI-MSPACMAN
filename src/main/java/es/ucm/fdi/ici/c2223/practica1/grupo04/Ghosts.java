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
	private int[] ppIndexes;
	int pacmanPos;
	private final int LimitPPClose= 10;
	
	
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
		
		if (game.doesGhostRequireAction(GHOST.PINKY)) // Fantasma que va hacia PPs excepto si queda solo una
		{
			if (game.isGhostEdible(GHOST.PINKY)) {
				moves.put(GHOST.PINKY,
						game.getApproximateNextMoveAwayFromTarget(game.getGhostCurrentNodeIndex(GHOST.PINKY), pacmanPos,
								game.getGhostLastMoveMade(GHOST.PINKY), DM.PATH));
			} else if (ppIndexes.length > 1) {
				chasePowerPill(GHOST.PINKY, game);
			} else {
				if (rnd.nextFloat() < 0.5) {
					chasePowerPill(GHOST.PINKY, game);
				} else {
					moves.put(GHOST.PINKY,
							game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(GHOST.PINKY),
									pacmanPos, game.getGhostLastMoveMade(GHOST.PINKY), DM.PATH));
				}
			}
		}

		if (game.doesGhostRequireAction(GHOST.BLINKY)) // Fantasma agresivo
		{
			if (game.isGhostEdible(GHOST.BLINKY) || pacmanCloseToPPill(game)) {
				moves.put(GHOST.BLINKY,
						game.getApproximateNextMoveAwayFromTarget(game.getGhostCurrentNodeIndex(GHOST.BLINKY),
								pacmanPos, game.getGhostLastMoveMade(GHOST.BLINKY), DM.EUCLID));
			} else {
				moves.put(GHOST.BLINKY,
						game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(GHOST.BLINKY), pacmanPos,
								game.getGhostLastMoveMade(GHOST.BLINKY), DM.EUCLID));
			}
		}
	
		
		if (game.doesGhostRequireAction(GHOST.INKY))
		{
			
		}
		
		if (game.doesGhostRequireAction(GHOST.SUE))
		{
			
		}
		
		return moves;
	}
	
	public void chasePowerPill (GHOST ghostType, Game game)
	{
		int ghostPos=game.getGhostCurrentNodeIndex(ghostType);
		for (int PP: ppIndexes)
		{
			if (game.getDistance(PP, pacmanPos, DM.PATH) >LimitPPClose)
			{
				moves.put(ghostType, game.getApproximateNextMoveTowardsTarget(ghostPos, PP, game.getGhostLastMoveMade(ghostType), DM.PATH));
				break;
			}
		}
		moves.put(ghostType, allMoves[rnd.nextInt(allMoves.length)]);
	}
	
	private boolean pacmanCloseToPPill(Game game) {
		for(int PPill : game.getActivePowerPillsIndices()) {
			if(game.getDistance(PPill, game.getPacmanCurrentNodeIndex(), DM.EUCLID) <= LimitPPClose) {
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