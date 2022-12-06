package es.ucm.fdi.ici.c2223.practica4.grupo04.ghosts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pacman.game.Constants.GHOST;

public class GhostsFuzzyMemory {

	ArrayList<HashMap<String,Double>> mem;
	
	private double confidence = 100;

	private int lastPacmanPosition;
	private int nearestPPillToPacman;

	
	public GhostsFuzzyMemory() {
		mem = new ArrayList<HashMap<String,Double>>();
		for(GHOST ghost : GHOST.values()) {
			mem.add(new HashMap<String,Double>());
		}
	}
	
	public void getInput(GhostsInput input)
	{
		if(input.PacmanIsVisible()) {
			confidence = 100;
		}
		else
			confidence = Double.max(0, confidence-5);
		lastPacmanPosition = input.getLastPacmanPosition();
		nearestPPillToPacman = input.getNearestPPillToPacman();
		for(GHOST ghost : GHOST.values()) {
			mem.get(ghost.ordinal()).put("PacmanDistanceToPPill", input.getPacmanDistanceToPPill());
			mem.get(ghost.ordinal()).put(ghost.name()+"distanceToPacman", input.distancetoPacman(ghost));
			mem.get(ghost.ordinal()).put("PacmanDistanceTo"+ghost.name(), input.PacmanDistanceTo(ghost));
			mem.get(ghost.ordinal()).put(ghost.name()+"danger", input.getDangerIndex(ghost));
			mem.get(ghost.ordinal()).put(ghost.name()+"timeInLair", input.getTimeInLair(ghost));
			mem.get(ghost.ordinal()).put(ghost.name()+"distanceToTunnel", input.getDistanceToTunnel(ghost));
			mem.get(ghost.ordinal()).put(ghost.name()+"edibleTime", input.getEdibleTime(ghost));
			mem.get(ghost.ordinal()).put(ghost.name()+"collisionIndex", input.getCollisionIndex(ghost));
			mem.get(ghost.ordinal()).put("PacmanConfidence", confidence);
			mem.get(ghost.ordinal()).put("PacmanDistanceToTunnel", input.getPacmanDistanceToTunnel());
			mem.get(ghost.ordinal()).put("GhostsCloseIndex", input.getGhostsCloseIndex());
		}

	}
	
	public HashMap<String, Double> getFuzzyValues(GHOST ghost) {
		return mem.get(ghost.ordinal());
	}

	public int lastPacmanPosition() {
		return lastPacmanPosition;
	}

	public int getLastClosePP() {
		return nearestPPillToPacman;
	}
	
}
