package es.ucm.fdi.ici.c2223.practica2.grupo04.GhostsFSM;

import java.util.HashMap;

import es.ucm.fdi.ici.Input;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Game;
import es.ucm.fdi.ici.c2223.practica2.grupo04.GhostsFSM.GhostsUtils;


public class GhostsInput extends Input {

	private HashMap<GHOST,HashMap<GhostsRelevantInfo, Boolean>> ghostsInfoMap;
	private int currentLevel;
	private boolean eatenPPill;
	static private int storedLevel = -1;
	private boolean pacmanNearPPill;
	private GHOST nearestGhostToPacman;
	private static final int SECURITY_DISTANCE = 60;
	public GhostsInput(Game game) {
		super(game);
	}
	@Override
	public void parseInput() {
		ghostsInfoMap = new HashMap<GHOST,HashMap<GhostsRelevantInfo, Boolean>>();
		for (GHOST ghost : GHOST.values()) {
			HashMap<GhostsRelevantInfo, Boolean> ghostMap = new HashMap<GhostsRelevantInfo, Boolean>();
			ghostMap.put(GhostsRelevantInfo.EATEN, game.wasGhostEaten(ghost));
			ghostMap.put(GhostsRelevantInfo.EDIBLE, game.isGhostEdible(ghost));
			ghostMap.put(GhostsRelevantInfo.NEARTUNNEL, game.getDistance(game.getGhostCurrentNodeIndex(ghost), GhostsUtils.NearestTunnelNode(game, ghost),game.getGhostLastMoveMade(ghost), DM.EUCLID) <= SECURITY_DISTANCE);
			ghostMap.put(GhostsRelevantInfo.PACMANINVECINITY, game.getDistance(game.getPacmanCurrentNodeIndex(),game.getGhostCurrentNodeIndex(ghost), game.getPacmanLastMoveMade() , DM.PATH) <= SECURITY_DISTANCE);
			ghostsInfoMap.put(ghost, ghostMap);
			
		}
		eatenPPill = game.wasPowerPillEaten();
		currentLevel = game.getCurrentLevel();
		pacmanNearPPill = GhostsUtils.PacmanCloseToPPill(game,SECURITY_DISTANCE);
		nearestGhostToPacman = GhostsUtils.NearestGhostToPacman(game);
	}
	public boolean isGhostEdible(GHOST ghost) {
		return ghostsInfoMap.get(ghost).get(GhostsRelevantInfo.EDIBLE);
	}
	public boolean levelChanged() {
		if (currentLevel != storedLevel) {
			storedLevel = currentLevel;
			return true;
		}
		return false;
	}
	public boolean PPillEaten() {
		return eatenPPill;
	}
	public boolean wasGhostEaten(GHOST ghost) {
		return ghostsInfoMap.get(ghost).get(GhostsRelevantInfo.EATEN);
	}
	
	public boolean danger(GHOST ghost) {
		if (pacmanNearPPill || (game.isGhostEdible(ghost) && ghost==nearestGhostToPacman) )
			return true;
		else 
			return false;
	}

	public boolean isOutOfLair(GHOST ghost) {
		if (game.getGhostLairTime(ghost) > 0)
			return false;
		else 
			return true;		
	}
	public boolean isTunnelNear(GHOST ghost) {
		return ghostsInfoMap.get(ghost).get(GhostsRelevantInfo.NEARTUNNEL);
	}
	public boolean isPacmanInVecinity(GHOST ghost) {
		return ghostsInfoMap.get(ghost).get(GhostsRelevantInfo.PACMANINVECINITY);
	}
	public boolean farFromPacman(GHOST ghost) {
		return !ghostsInfoMap.get(ghost).get(GhostsRelevantInfo.PACMANINVECINITY);
	}
}

