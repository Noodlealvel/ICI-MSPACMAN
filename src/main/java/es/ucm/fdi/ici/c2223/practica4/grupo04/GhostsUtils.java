package es.ucm.fdi.ici.c2223.practica4.grupo04;

import pacman.game.Game;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.TreeMap;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;

public class GhostsUtils {
	
	static public int NearestPill(Game game, GHOST ghost) {
		int nearest = Integer.MAX_VALUE;
		int path;
		for (int pill : game.getActivePillsIndices()) {
			path = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghost), pill);
			if (path < nearest)
				return pill;
		}
		return game.getPacmanCurrentNodeIndex();
	}
	static public int NearestActivePPill(Game game, GHOST ghost) {
		int nearest = Integer.MAX_VALUE;
		int path;
		for (int powerpill : game.getActivePowerPillsIndices()) {
			path = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghost), powerpill);
			if (path < nearest)
				return powerpill;
		}
		return game.getPacmanCurrentNodeIndex();
	}
	static public int NearestTunnelNode(Game game, GHOST ghost) {
		Queue<Integer> notExpanded = new ArrayDeque<Integer>();
		List<Integer> expandedList = new ArrayList<Integer>();
		notExpanded.add(Integer.valueOf(game.getGhostCurrentNodeIndex(ghost)));
		int tunnel = -1;
		while(!notExpanded.isEmpty()) {
			int expanded = notExpanded.remove().intValue();
			for(int node : game.getNeighbouringNodes(expanded)) {
				if(expandedList.indexOf(node) == -1 && game.getGhostInitialNodeIndex() != node) {
					notExpanded.add(node);
					if(game.getDistance(expanded,node,DM.EUCLID) > 1) {
						tunnel = expanded;
						return tunnel;
					}
				}
			}
			expandedList.add(expanded);
		}
		return tunnel;
	}
	
	static public double DistancePacmanToTunnel(Game game, int pacmanPosition) {
		Queue<Integer> notExpanded = new ArrayDeque<Integer>();
		List<Integer> expandedList = new ArrayList<Integer>();
		notExpanded.add(Integer.valueOf(pacmanPosition));
		int tunnel = -1;
		while(!notExpanded.isEmpty()) {
			int expanded = notExpanded.remove().intValue();
			for(int node : game.getNeighbouringNodes(expanded)) {
				if(expandedList.indexOf(node) == -1 && game.getGhostInitialNodeIndex() != node) {
					notExpanded.add(node);
					if(game.getDistance(expanded,node,DM.EUCLID) > 1) {
						tunnel = expanded;
						return game.getDistance(pacmanPosition, tunnel, DM.EUCLID);
					}
				}
			}
			expandedList.add(expanded);
		}
		return 200;
	}
	
	static public boolean PacmanCloseToPPill(Game game, int pacmanPosition, int dist) {
		int[] indicesPowerPill = game.getActivePowerPillsIndices();
		for (int i :indicesPowerPill) {
			if(game.getDistance(pacmanPosition, i, DM.PATH) <= dist)
				return true;
		}
		return false;	
	}
	
	static public boolean PacmanCloseToGhost(Game game,GHOST ghost, int dist) {
		boolean isClose = false;
		if(game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghost)) <= dist)
			isClose = true;
		return isClose;	
	}
	public static int ClosestPointToAllGhosts(Game game, GHOST ghost) {
		int point = 0;
		double distance = 0;
		double minDistance = Integer.MAX_VALUE;
		for (int node : game.getPillIndices()) {
			distance = 0;
			for (GHOST g : GHOST.values()) {
				if (ghost != g && game.getGhostLairTime(g) == 0) {
					distance += game.getDistance(game.getGhostCurrentNodeIndex(g), node, DM.EUCLID);
				}
			}
			if (distance < minDistance) {
				minDistance = distance;
				point = node;
			}
		}
		return point;
	}
	
	static public boolean PathContainsGhosts(Game game, GHOST ghost) {
		int[] path = game.getShortestPath(game.getGhostCurrentNodeIndex(ghost), game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(ghost));
		if (path != null) {
			for (int node : path) {
				for (GHOST ghosts : GHOST.values()) {
					if (ghosts != ghost)
						if (game.getGhostCurrentNodeIndex(ghosts) == node && game.isGhostEdible(ghosts) == false) {
							return true;
						}
				}
			}
		}
		return false;
	}
	public static int getNodeBetweenPacmanAndPpill(Game game, int pacmanPosition) {
		//Búsqueda heurística según distancia, que elige el punto intemedio entre la PPill más cercana y pacman, explorando siempre los no explorados más prometedores hasta tener un márgen de error bajo.
		TreeMap<Double,Integer> distanceMap = new TreeMap<Double, Integer>();
		List<Integer> expandedList = new ArrayList<Integer>();
		int closestActivePowerPill = NearestActivePPillToPacman(game, pacmanPosition);
		distanceMap.put(game.getDistance(closestActivePowerPill,pacmanPosition, DM.EUCLID),Integer.valueOf(pacmanPosition));
		double currentDistance = Double.MAX_VALUE;
		boolean found = false;
		int middlePoint = 0;
		if (closestActivePowerPill == -1) {
			return pacmanPosition;
		}
		while(!found) {
			int expanded = 0;
			for(Double i :distanceMap.navigableKeySet()) {
				if(expandedList.indexOf(distanceMap.get(i)) == -1) {
					expanded = distanceMap.get(i);
							break;
				}
			}
			for(int node : game.getNeighbouringNodes(expanded)) {
				if(expandedList.indexOf(node) == -1 && game.getGhostInitialNodeIndex() != node) {
					currentDistance = -game.getDistance(pacmanPosition,node,DM.EUCLID);
					currentDistance += game.getDistance(closestActivePowerPill,node, DM.EUCLID);
					distanceMap.put(Double.valueOf(Math.abs(currentDistance)), Integer.valueOf(node));
				}
			}
			if (distanceMap.firstKey() < 5) {
				found = true;
				middlePoint = distanceMap.firstEntry().getValue();
			}
			expandedList.add(expanded);
		}
		return middlePoint;
	}
	public static int NearestActivePPillToPacman(Game game, int pacmanPosition) {
		int nearest = Integer.MAX_VALUE;
		int path;
		for (int powerpill : game.getActivePowerPillsIndices()) {
			path = game.getShortestPathDistance(pacmanPosition, powerpill);
			if (path < nearest)
				return powerpill;
		}
		return pacmanPosition;
		}
	public static Boolean GhostCloseToRest(Game game, GHOST ghost, int closeGhosts) {
		double distance = 0;
		for(GHOST g : GHOST.values()) {
			if (g != ghost){
				if(game.getGhostLairTime(g) <= 0)
					distance += game.getDistance(game.getGhostCurrentNodeIndex(ghost), game.getGhostCurrentNodeIndex(g), DM.EUCLID);
				else
					distance += closeGhosts/4;
			}
		}
		return distance <= closeGhosts;
	}
	public static boolean PacmanInTunnel(Game game, int pacmanPosition) {
		int toExpand = pacmanPosition;
		for (int node : game.getNeighbouringNodes(toExpand, game.getPacmanLastMoveMade())) {
			if (game.getDistance(node, toExpand, DM.EUCLID) > 1)
				return true;
		}
		return false;
	}
	public static Double numberOfGhostsCloser(Game game, int pacmanPosition, GHOST ghost) {
		double contador = 0.0;
		for (GHOST g : GHOST.values()) {
			if (g != ghost) {
				contador += Math.max((double)game.getDistance(pacmanPosition, game.getGhostCurrentNodeIndex(g), DM.EUCLID)/
						(double)game.getDistance(pacmanPosition, game.getGhostCurrentNodeIndex(ghost), DM.EUCLID), 2);
			}
		}
		return contador;
	}
	public static double CloseIndex(Game game, int pacmanPosition) {
		List<Double> distanceList = new ArrayList<Double>();
		double median = 0.0;
		double desv = 0.0;
		for(GHOST g : GHOST.values()) {
			distanceList.add(game.getDistance(game.getGhostCurrentNodeIndex(g), pacmanPosition, game.getGhostLastMoveMade(g), DM.PATH));
			median += distanceList.get(distanceList.size()-1);
		}
		median = median/4;
		for (int i = 0; i < 4 ;i++) {
			desv += (distanceList.get(i) - median) * (distanceList.get(i) - median);
		}
		desv = desv/4;
		desv = Math.sqrt(desv);
		return desv;
	}
	public static double DistancePacmanToPP(Game game, int pacmanPosition) {
		return game.getDistance(pacmanPosition, NearestActivePPillToPacman(game,pacmanPosition) , DM.EUCLID);
	}
	
}
