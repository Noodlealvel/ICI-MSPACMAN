package es.ucm.fdi.ici.c2223.practica2.grupo04.MsPacmanFSM;

import java.util.ArrayList;
import java.util.List;

import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;

public class MsPacmanUtils {

	public static boolean nodeHasActivePill(Game game, int node) {
		return game.getPillIndex(node) != -1 && game.isPillStillAvailable(game.getPillIndex(node));
	}
	
	public static int getNearestPP(Game game, int limit) {
		double powerPillDistance;
		double shortestDistance = -1;
		int nearestPPill = -1;
		for (int pillNode : game.getActivePowerPillsIndices()) {
			powerPillDistance = game.getDistance(game.getPacmanCurrentNodeIndex(), pillNode, DM.PATH);
			if ((powerPillDistance < shortestDistance && powerPillDistance < limit) || shortestDistance == -1) {
				shortestDistance = powerPillDistance;
				nearestPPill = pillNode;
			}
		}
		return nearestPPill;
	}

	public static int getNearestPP(Game game) {
		return game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), game.getActivePowerPillsIndices(), DM.EUCLID);
	}
	public static int getNearestPill(Game game) {
		return game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), game.getActivePillsIndices(), DM.EUCLID);
	}

	public static GHOST getNearestGhost(Game game, int nodeIndex, int timeLimit, boolean edible) {
		GHOST nearestGhost = null;
		double shortestDistance = -1;
		double distanceGhost = 0;
		for (GHOST ghost : GHOST.values()) {
			if((game.isGhostEdible(ghost) == edible && game.getGhostLairTime(ghost) == 0)) {
				distanceGhost = game.getDistance(nodeIndex, game.getGhostCurrentNodeIndex(ghost), DM.EUCLID);
				if((shortestDistance == -1 || distanceGhost < shortestDistance) && game.getGhostEdibleTime(ghost) <= timeLimit) {
					nearestGhost = ghost;
					shortestDistance = distanceGhost;
				}
			}
		}
		return nearestGhost;
	}

	public static GHOST getNearestGhost(Game game, int nodeIndex, boolean edible) {
		GHOST nearestGhost = null;
		double shortestDistance = -1;
		double distanceGhost = 0;
		for (GHOST ghost : GHOST.values()) {
			if((game.isGhostEdible(ghost) == edible && game.getGhostLairTime(ghost) == 0)) {
				distanceGhost = game.getDistance(nodeIndex, game.getGhostCurrentNodeIndex(ghost), DM.EUCLID);
				if((shortestDistance == -1 || distanceGhost < shortestDistance)) {
					nearestGhost = ghost;
					shortestDistance = distanceGhost;
				}
			}
		}
		return nearestGhost;
	}

	public static GHOST getNearestGhostAtDistance(Game game, int nodeIndex, int i, boolean edible) {
		GHOST nearestGhost = null;
		double shortestDistance = -1;
		double distanceGhost = 0;
		for (GHOST ghost : GHOST.values()) {
			if((game.isGhostEdible(ghost) == edible && game.getGhostLairTime(ghost) == 0)) {
				distanceGhost = game.getDistance(nodeIndex, game.getGhostCurrentNodeIndex(ghost), DM.EUCLID);
				if((shortestDistance == -1 || distanceGhost < shortestDistance) && distanceGhost <= i) {
					nearestGhost = ghost;
					shortestDistance = distanceGhost;
				}
			}
		}
		return nearestGhost;
	}
	
	
	// Cuantos fantasmas hay cerca de pacman
	public static List<GHOST> getGhostsNearPacman(Game game){

		List<GHOST> ghostsNearPacman = new ArrayList<GHOST>();
		for (GHOST ghost : GHOST.values()) {
			if (game.isGhostEdible(ghost) == false && game.getGhostLairTime(ghost) == 0) {
				double distance = game.getDistance(game.getPacmanCurrentNodeIndex(),
						game.getGhostCurrentNodeIndex(ghost), game.getPacmanLastMoveMade(), DM.PATH);
				if (distance <= MsPacmanInput.ghostCloseMedium) {
					ghostsNearPacman.add(ghost);
				}
			}
		}
		
		return ghostsNearPacman;
	}
	
	
	//Calculamos si PacMan tiene pills al lado
	public static boolean getNoPillsNear(Game game) {
		
		boolean noPillsNear = true;
		List<Integer> nearPills = new ArrayList<Integer>();
		int currentScore = 0;
		for(int pillNode : game.getPillIndices()) {
			if (MsPacmanUtils.nodeHasActivePill(game, pillNode) && game.getDistance(game.getPacmanCurrentNodeIndex(), pillNode, game.getPacmanLastMoveMade(), DM.EUCLID) <= 5 ) {
				nearPills.add(Integer.valueOf(pillNode));
			}
		}
		
		for (Integer pillNode : nearPills) {
			currentScore = 0;
			if (MsPacmanUtils.nodeHasActivePill(game, pillNode)) {
					currentScore++;
				}
			if (currentScore > 0) {
				noPillsNear = false;
			}
		}
		
		return noPillsNear;
	}
	
	
	//Para saber si esos fantasmas están flanqueando
	public static List<GHOST> getGhostsFlanking(Game game, List<GHOST> ghostsNearPacman) {
		
		List<GHOST> eliminado = new ArrayList<GHOST>();
		
		if (ghostsNearPacman != null) {
			for (GHOST ghost : ghostsNearPacman) {
				if (ghost != null && game.getGhostLairTime(ghost) == 0) {
					int[] ghostspath = game.getShortestPath(game.getPacmanCurrentNodeIndex(),
							game.getGhostCurrentNodeIndex(ghost), game.getPacmanLastMoveMade());
					for (int node1 : ghostspath) {
						for (GHOST ghosts : GHOST.values()) {
							if (game.getGhostCurrentNodeIndex(ghosts) == node1) {
								eliminado.add(ghosts);
							}
						}
					}
				}
			}
		}
		for(GHOST g : eliminado) {
			ghostsNearPacman.remove(g);
		}
		
		return ghostsNearPacman;
	}
	
	
	//Para saber si hay fantasmas comestibles juntos
	public static boolean getNearGhosts(Game game) {
		
		List<GHOST> ghostsNearghost;
		boolean edibleGhostsTogether = false;
		
		for (GHOST ghost : GHOST.values()) {
			ghostsNearghost = new ArrayList<GHOST>();
			if (game.isGhostEdible(ghost) == true && game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghost), DM.EUCLID) < MsPacmanInput.EatLimit) {
				for (GHOST ghosts : GHOST.values()) {
					if (game.isGhostEdible(ghost) == false && game.getGhostLairTime(ghost) == 0) {
						double distance = game.getDistance(game.getGhostCurrentNodeIndex(ghost),
								game.getGhostCurrentNodeIndex(ghosts), game.getGhostLastMoveMade(ghost), DM.PATH);
						if (distance <= MsPacmanInput.ghostCloseMedium) {
							ghostsNearghost.add(ghost);
						}
					}
				}

				if (ghostsNearghost.size() >= 3) {
					edibleGhostsTogether = true;
					break;
				}
			}
		}
		
		return edibleGhostsTogether;
	}
	
}
