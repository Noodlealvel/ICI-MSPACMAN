package es.ucm.fdi.ici.c2223.practica4.grupo04;

import java.util.HashMap;
import java.util.Map;

import pacman.game.Constants.GHOST;

public class GhostsFuzzyMemory {

	HashMap<String,Double> mem;
	
	 private double confidence = 100;

	private int lastPacmanPosition;
	private int nearestPPillToPacman;

	
	public GhostsFuzzyMemory() {
		mem = new HashMap<String,Double>();
	}
	
	public void getInput(GhostsInput input)
	{
		if(input.PacmanIsVisible()) {
			confidence = 100;
			mem.put("PacmanDistanceToPPill", input.getPacmanDistanceToPPill());
			lastPacmanPosition = input.getLastPacmanPosition();
			nearestPPillToPacman = input.getNearestPPillToPacman();
			for(GHOST ghost : GHOST.values()) {
				mem.put(ghost.name()+"distanceToPacman", input.distancetoPacman(ghost));
				mem.put("PacmanDistanceTo"+ghost.name(), input.PacmanDistanceTo(ghost));
				mem.put(ghost.name()+"danger", input.getDangerIndex(ghost));
				mem.put(ghost.name()+"timeInLair", input.getTimeInLair(ghost));
				mem.put(ghost.name()+"distanceToTunnel", input.getDistanceToTunnel(ghost));
				mem.put(ghost.name()+"edibleTime", input.getEdibleTime(ghost));
				mem.put(ghost.name()+"collisionIndex", input.getCollisionIndex(ghost));
			}
		}
		else
			confidence = Double.max(0, confidence-5);
		for(GHOST ghost : GHOST.values())
			mem.put(ghost.name()+"confidence", confidence);
		mem.put("PacmanDistanceToTunnel", input.getPacmanDistanceToTunnel());
		mem.put("GhostsCloseIndex", input.getGhostsCloseIndex());

	}
	
	public HashMap<String, Double> getFuzzyValues() {
		return mem;
	}

	public int lastPacmanPosition() {
		return lastPacmanPosition;
	}
	
}
