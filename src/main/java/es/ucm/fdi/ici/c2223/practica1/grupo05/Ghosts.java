package es.ucm.fdi.ici.c2223.practica1.grupo05;

import java.util.EnumMap;
//import java.util.Random;

import pacman.controllers.GhostController;
import pacman.game.Constants;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public final class Ghosts extends GhostController {
	private EnumMap<GHOST, MOVE> moves = new EnumMap<GHOST, MOVE>(GHOST.class);
	
	public Ghosts() {
		 super();
		 setName("Ghosts05");
		 setTeam("Team05");
	 }
	
	/*
	 * Devuelve true si hay situación de peligro, si no devuelve false
	 * 
	 * Consideramos que estan en peligro si han comido a un fantasma y el resto siguen comestibles 
	 */
	private boolean danger(Game game) {
		for (GHOST ghostType : GHOST.values()) {
			if (game.wasGhostEaten(ghostType)){ //Si un fantasma es comido
				for (GHOST ghostType2 : GHOST.values()) //Comprueba si los demás están en peligro
					if(game.isGhostEdible(ghostType2))
						return true;
			}
		}
		return false;
	}
		
	
	/*
	 * Devuelve el fantasma más cercano a MsPacMan
	 * 
	 */
	private GHOST getNearestGhost(Game game) {
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
	
	/*
	 * Devuelve el fantasma más lejano a MsPacMan
	 * 
	 */
	private GHOST getFurthestGhost(Game game) {
		GHOST furthest = null;
		int maxDist = Integer.MIN_VALUE;
		for(GHOST ghost : GHOST.values()) {
			int dist = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghost),game.getPacmanCurrentNodeIndex());
			if(dist > maxDist) {
				maxDist = dist;
				furthest = ghost;
			}
		}
		return furthest;
	}
	
	/*
	 * Devuelve la PowerPill más cercana a MsPacMan, -1 si no la encuentra
	 * 
	 */
	
	private int getNearestPowerPill(Game game) {
		int nearest = Integer.MAX_VALUE;
		int path;
		for (int powerpill : game.getActivePowerPillsIndices()) {
			path = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), powerpill, game.getPacmanLastMoveMade());
			if (path < nearest)
				return powerpill;
		}
		return -1;
	}
	
	
	/*
	 * Devuelve true si PacMan está cerca de una PowerPill, si no devuelve false
	 * 
	 * Podría servir para avisar a los fantasmas de que están en peligro
	 */
	
	private boolean pacmanIsCloseToPowerPill(Game game, int dist) {
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
	
	/*
	 * Devuelve true si el fantasma está cerca de PacMan a una determinada distancia, si no devuelve false
	 * 
	 * Podría servir para avisar a los fantasmas de que están en peligro
	 */
	
	private boolean ghostIsCloseToPacMan(Game game, GHOST me, int dist) {
		boolean isClose = false;
		if(game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(me)) <= dist)
			isClose = true;
		return isClose;		
	}
	
	// ------- MOVIMIENTOS --------
	
	/*
	 * Este movimiento hará al fantasma ir a por MsPacMan
	 * 
	 */
	private MOVE attack(Game game, GHOST me) {
		return game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(me), game.getPacmanCurrentNodeIndex(), 
				game.getGhostLastMoveMade(me), Constants.DM.PATH);
	}
	
	/*
	 * Este movimiento hará al fantasma ir al punto de intersección más cercano con el camino de MsPacMan
	 * NO SE USA
	 */
	/* private MOVE junctionPacMan(Game game, GHOST me) {
		int minDist = Integer.MAX_VALUE;
		int nearestJunc = -1;
		for (int junc : game.getJunctionIndices()) {
			if (game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), junc ,game.getPacmanLastMoveMade()) < minDist) {
				minDist = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), junc);
				nearestJunc = junc;
			}
		}
		return game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(me), nearestJunc,
				game.getGhostLastMoveMade(me), Constants.DM.PATH);
	}*/
	
	/*
	 * Este movimiento hará al fantasma intentar cortar el camino de MsPacMan a una PowerPill
	 * en caso de no haberla, irá a por MsPacMan
	 * 
	 */
	private MOVE cutPowerPill(Game game, GHOST me) {
		int nearestPowerPill = getNearestPowerPill(game);
		MOVE move = Constants.MOVE.NEUTRAL;
		if (nearestPowerPill != -1) {
			int min = Integer.MAX_VALUE;
			int correctNode = -1;
			for (int node : game.getShortestPath(game.getPacmanCurrentNodeIndex(), nearestPowerPill, move)) {
				double dist = Math.abs(game.getEuclideanDistance(game.getPacmanCurrentNodeIndex(), node)-
						game.getEuclideanDistance(game.getGhostCurrentNodeIndex(me), node));
				if (dist < min)
					correctNode = node;
			}
			return game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(me), correctNode, 
					game.getGhostLastMoveMade(me), Constants.DM.PATH);
		}
		else 	// vamos a por MsPacMan
			return game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(me), game.getPacmanCurrentNodeIndex(), 
					game.getGhostLastMoveMade(me), Constants.DM.PATH);
		
	}
	
	/*
	 * Con este movimiento huiremos de MsPacMan (nos encontramos seguramente en una situacion de peligro)
	 * o en caso de no ser así, nos alejamos del fantasma más cercano
	 * 
	 */
	private MOVE runaway(Game game, GHOST me) {
		MOVE run = Constants.MOVE.NEUTRAL;
		GHOST nearest = getNearestGhost(game);
		double extraDanger = 0.8;
		int distPacman = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(me), game.getPacmanCurrentNodeIndex());
		int distNearest = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(me), game.getGhostCurrentNodeIndex(nearest));
		//Le damos prioridad a la cercania de MsPacMan y si está cerca, huimos
		if (distPacman * extraDanger <= distNearest) 
			run = game.getApproximateNextMoveAwayFromTarget(game.getGhostCurrentNodeIndex(me), game.getPacmanCurrentNodeIndex(), 
					game.getGhostLastMoveMade(me), Constants.DM.PATH); 
		//Si no, nos alejamos del fantasma más cercano y así no favorecemos que nos coma MsPacMan
		else 
			run = game.getApproximateNextMoveAwayFromTarget(game.getGhostCurrentNodeIndex(me), game.getGhostCurrentNodeIndex(nearest),
					game.getGhostLastMoveMade(me), Constants.DM.PATH);
		
		return run;		
	}
	
	@Override
	public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
		moves.clear();
		GHOST nearest = getNearestGhost(game);
		GHOST furthest = getFurthestGhost(game);
		boolean danger = danger(game);
		for (GHOST ghostType : GHOST.values()) {
			
			if (game.doesGhostRequireAction(ghostType)) {
				//Si MsPacMan está cerca de una PP o ya está activada o estamos en estado de peligro
				if ((pacmanIsCloseToPowerPill(game, 42) && ghostIsCloseToPacMan(game, ghostType,56)|| game.isGhostEdible(ghostType) || 
						/*game.getGhostCurrentEdibleScore() >= 1200 ||*/ danger ))
					moves.put(ghostType, runaway(game, ghostType));
				else {
					// Si no soy el fantasma más cercano a MsPacMan
					if (ghostType != nearest) {
						// Si soy el más lejano intento interceptarlo en el camino a la PP
						if(ghostType == furthest) 
								// moves.put(ghostType, junctionPacMan(game, ghostType));
						// else if(game.getGhostCurrentEdibleScore() <= 800)
								moves.put(ghostType, cutPowerPill(game, ghostType));
						else
							moves.put(ghostType, attack(game, ghostType));

					}
					else 
						moves.put(ghostType, attack(game, ghostType));
					
					
				}
			}
			
		}
		return moves;
	}	
}