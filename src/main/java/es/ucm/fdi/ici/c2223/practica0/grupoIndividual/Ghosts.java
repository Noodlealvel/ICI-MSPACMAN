package es.ucm.fdi.ici.c2223.practica0.grupoIndividual;

import java.util.EnumMap;
import java.util.Random;

import pacman.controllers.GhostController;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class Ghosts extends GhostController {
	private EnumMap<GHOST, MOVE> moves = new EnumMap<GHOST, MOVE>(GHOST.class);
	private Random rnd = new Random();
	private final int LIMITPPILL = 20;
	private MOVE[] allMoves = MOVE.values();

	@Override
	public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
		int pacman = game.getPacmanCurrentNodeIndex();
		moves.clear();
		for (GHOST ghostType : GHOST.values()) {
			if (game.doesGhostRequireAction(ghostType)) {
				if (game.isGhostEdible(ghostType) || pacmanClosePPill(game)) {
					moves.put(ghostType, game.getApproximateNextMoveAwayFromTarget(
							game.getGhostCurrentNodeIndex(ghostType), pacman, game.getGhostLastMoveMade(ghostType), DM.EUCLID));
				}
			} else {
				if (rnd.nextFloat() < 0.9) {
					moves.put(ghostType, game.getApproximateNextMoveTowardsTarget(
							game.getGhostCurrentNodeIndex(ghostType), pacman, game.getGhostLastMoveMade(ghostType), DM.EUCLID));
				} else {
					moves.put(ghostType, getNextRandomMove());
				}
			}
		}
		return moves;
	}

	private boolean pacmanClosePPill(Game game) {
		for(int PPill : game.getActivePowerPillsIndices()) {
			if(game.getDistance(PPill, game.getPacmanCurrentNodeIndex(), DM.EUCLID) <= LIMITPPILL) {
				return true;
			}
		}
		return false;
	}

	private MOVE getNextRandomMove() {
		return allMoves[rnd.nextInt(allMoves.length)];
	}

}
