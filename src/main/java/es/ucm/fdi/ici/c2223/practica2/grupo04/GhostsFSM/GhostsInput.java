package es.ucm.fdi.ici.c2223.practica2.grupo04.GhostsFSM;

import java.util.HashMap;

import es.ucm.fdi.ici.Input;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import es.ucm.fdi.ici.c2223.practica2.grupo04.GhostsFSM.GhostsUtils;


public class GhostsInput extends Input {

	private HashMap<GHOST,HashMap<GhostsRelevantInfo, Boolean>> ghostsInfoMap;
	private boolean eatenPPill;
	private boolean pacmanNearPPill;
	private GHOST nearestGhostToPacman;
	private boolean noPPills;
	private HashMap<GHOST,MOVE> ghostsLastMovementMade;
	private MOVE pacmanLastMovement;
	private boolean pacmanInTunnel;
	private GameMemory gameMemory;
	private static final int SECURITY_DISTANCE = 60;
	private static final int LOW_TIME = 10;
	private static final int CLOSE_PACMAN_DISTANCE = 40;
	public GhostsInput(Game game, GameMemory mem) {
		super(game);
		gameMemory = mem;
	}
	@Override
	public void parseInput() {
		ghostsInfoMap = new HashMap<GHOST,HashMap<GhostsRelevantInfo, Boolean>>();
		ghostsLastMovementMade = new HashMap<GHOST,MOVE>();
		for (GHOST ghost : GHOST.values()) {
			HashMap<GhostsRelevantInfo, Boolean> ghostMap = new HashMap<GhostsRelevantInfo, Boolean>();
			ghostMap.put(GhostsRelevantInfo.EATEN, game.wasGhostEaten(ghost));
			ghostMap.put(GhostsRelevantInfo.EDIBLE, game.isGhostEdible(ghost));
			ghostMap.put(GhostsRelevantInfo.NEAR_TUNNEL, game.getDistance(game.getGhostCurrentNodeIndex(ghost), GhostsUtils.NearestTunnelNode(game, ghost),game.getGhostLastMoveMade(ghost), DM.EUCLID) <= SECURITY_DISTANCE);
			ghostMap.put(GhostsRelevantInfo.PACMAN_INVECINITY, game.getDistance(game.getPacmanCurrentNodeIndex(),game.getGhostCurrentNodeIndex(ghost), game.getPacmanLastMoveMade() , DM.PATH) <= SECURITY_DISTANCE);
			ghostMap.put(GhostsRelevantInfo.GHOSTS_CLOSE, GhostsUtils.GhostCloseToRest(game,ghost));
			ghostMap.put(GhostsRelevantInfo.LOW_EDIBLE_TIME, game.getGhostEdibleTime(ghost) <= LOW_TIME);
			ghostMap.put(GhostsRelevantInfo.PACMAN_CLOSE, game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghost), game.getPacmanLastMoveMade() ,DM.PATH) <= CLOSE_PACMAN_DISTANCE);
			ghostMap.put(GhostsRelevantInfo.NO_GHOSTS_IN_PATH, GhostsUtils.PathContainsGhosts(game, game.getShortestPath(game.getGhostCurrentNodeIndex(ghost), game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(ghost))));
			ghostsInfoMap.put(ghost, ghostMap);
			ghostsLastMovementMade.put(ghost, game.getGhostLastMoveMade(ghost));
			
		}
		pacmanLastMovement = game.getPacmanLastMoveMade();
		pacmanInTunnel = GhostsUtils.PacmanInTunnel(game);
		noPPills = game.getNumberOfActivePowerPills() > 0;
		eatenPPill = game.wasPowerPillEaten();
		pacmanNearPPill = GhostsUtils.PacmanCloseToPPill(game,SECURITY_DISTANCE);
		nearestGhostToPacman = GhostsUtils.NearestGhostToPacman(game);
		fillMemory();
	}
	private void fillMemory() {
		gameMemory.setLastLevel();
		gameMemory.setCurrentLevel(game.getCurrentLevel());
		
	}
	public boolean isGhostEdible(GHOST ghost) {
		return ghostsInfoMap.get(ghost).get(GhostsRelevantInfo.EDIBLE);
	}
	public boolean levelChanged() {
		return gameMemory.getCurrentLevel() != gameMemory.getLastLevel();
	}
	public boolean PPillEaten() {
		return eatenPPill;
	}
	public boolean wasGhostEaten(GHOST ghost) {
		return ghostsInfoMap.get(ghost).get(GhostsRelevantInfo.EATEN);
	}
	
	public boolean danger(GHOST ghost) {
		return pacmanNearPPill || (game.isGhostEdible(ghost) && ghost==nearestGhostToPacman);
	}

	public boolean isOutOfLair(GHOST ghost) {
		return game.getGhostLairTime(ghost) > 0;		
	}
	public boolean isTunnelNear(GHOST ghost) {
		return ghostsInfoMap.get(ghost).get(GhostsRelevantInfo.NEAR_TUNNEL);
	}
	public boolean isPacmanInVecinity(GHOST ghost) {
		return ghostsInfoMap.get(ghost).get(GhostsRelevantInfo.PACMAN_INVECINITY);
	}
	public boolean farFromPacman(GHOST ghost) {
		return !ghostsInfoMap.get(ghost).get(GhostsRelevantInfo.PACMAN_CLOSE);
	}
	public boolean ghostsDispersed(GHOST ghost) {
		return !ghostsInfoMap.get(ghost).get(GhostsRelevantInfo.GHOSTS_CLOSE);
	}
	public boolean lowEdibleTime(GHOST ghost) {
		return ghostsInfoMap.get(ghost).get(GhostsRelevantInfo.LOW_EDIBLE_TIME);
	}
	public boolean closeToOtherGhosts(GHOST ghost) {
		return ghostsInfoMap.get(ghost).get(GhostsRelevantInfo.GHOSTS_CLOSE);
	}
	public boolean nearToPacman(GHOST ghost) {
		return ghostsInfoMap.get(ghost).get(GhostsRelevantInfo.PACMAN_CLOSE);
	}
	public boolean noPowerPills() {
		return noPPills;
	}
	public boolean noGhostsInPath(GHOST ghost){
		return ghostsInfoMap.get(ghost).get(GhostsRelevantInfo.NO_GHOSTS_IN_PATH);
	}
	public boolean ghostsInPath(GHOST ghost){
		return !ghostsInfoMap.get(ghost).get(GhostsRelevantInfo.NO_GHOSTS_IN_PATH);
	}
	public boolean sameLastMovement(GHOST ghost) {
		return ghostsLastMovementMade.get(ghost) == pacmanLastMovement;
	}
	public boolean pacmanInTunnel() {
		return pacmanInTunnel;
	}
}

