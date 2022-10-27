package es.ucm.fdi.ici.c2223.practica2.grupo04.GhostsFSM;

import pacman.game.Game;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
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
		return -1;
	}
	static public int NearestActivePPill(Game game, GHOST ghost) {
		int nearest = Integer.MAX_VALUE;
		int path;
		for (int powerpill : game.getActivePowerPillsIndices()) {
			path = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghost), powerpill);
			if (path < nearest)
				return powerpill;
		}
		return -1;
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
	
	static public GHOST NearestGhostToPacman(Game game) {
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
	
	static public boolean PacmanCloseToPPill(Game game, int dist) {
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
	
	static public boolean PacmanCloseToGhost(Game game,GHOST ghost, int dist) {
		boolean isClose = false;
		if(game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghost)) <= dist)
			isClose = true;
		return isClose;	
	}
	public static int ClosestPointToAllGhosts(Game game) {
		int point = 0;
		double distance = 0;
		double minDistance = Integer.MAX_VALUE;
		for (int node : game.getPillIndices()) {
			distance = 0;
			for (GHOST g : GHOST.values()) {
				distance += game.getDistance(game.getGhostCurrentNodeIndex(g), node, DM.EUCLID);
			}
			if (distance < minDistance) {
				minDistance = distance;
				point = node;
			}
		}
		return point;
	}
	
	static public boolean PathContainsGhosts(Game game, int[] path) {
		boolean contains = false;
		if (path != null) {
			for (int node : path) {
				for (GHOST ghosts : GHOST.values()) {
					if (game.getGhostCurrentNodeIndex(ghosts) == node && game.isGhostEdible(ghosts) == false) {
						contains = true;
					}
				}
			}
		}
		return contains;
	}
	public static int getNodeBetweenPacmanAndPpill(Game game) {
		//Búsqueda heurística según distancia
		TreeMap<Double,Integer> distanceMap = new TreeMap<Double, Integer>();
		List<Integer> expandedList = new ArrayList<Integer>();
		int closestActivePowerPill = NearestActivePPillToPacman(game);
		distanceMap.put(game.getDistance(closestActivePowerPill,game.getPacmanCurrentNodeIndex(), DM.EUCLID),Integer.valueOf(game.getPacmanCurrentNodeIndex()));
		double currentDistance = Double.MAX_VALUE;
		boolean found = false;
		int middlePoint = 0;
		if (closestActivePowerPill == -1) {
			return game.getPacmanCurrentNodeIndex();
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
					currentDistance = -game.getDistance(game.getPacmanCurrentNodeIndex(),node,DM.EUCLID);
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
	private static int NearestActivePPillToPacman(Game game) {
		int nearest = Integer.MAX_VALUE;
		int path;
		for (int powerpill : game.getActivePowerPillsIndices()) {
			path = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), powerpill);
			if (path < nearest)
				return powerpill;
		}
		return -1;
		}
	
}
