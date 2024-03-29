package es.ucm.fdi.ici.c2223.practica4.grupo04.ghosts;

import java.util.HashMap;

import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Game;
import es.ucm.fdi.ici.Input;


public class GhostsInput extends Input {

	private HashMap<GHOST,HashMap<GhostsRelevantInfo, Double>> ghostsInfoMap;
	private boolean pacmanIsVisible;
	private int pacmanPosition;
	private double pacmanDistanceToTunnel;
	private double pacmanDistanceToPPill;
	private double ghostsCloseIndex;
	private int nearestPPillToPacman;
	public GhostsInput(Game game, int lastPacmanPosition) {
		super(game);
		pacmanPosition = lastPacmanPosition;
	}
	@Override
	public void parseInput() {
		ghostsInfoMap = new HashMap<GHOST,HashMap<GhostsRelevantInfo, Double>>();
		pacmanIsVisible = game.getPacmanCurrentNodeIndex() != -1;
		if (pacmanIsVisible) {
			pacmanPosition = game.getPacmanCurrentNodeIndex();
		}
		nearestPPillToPacman = GhostsUtils.NearestActivePPillToPacman(game,pacmanPosition);
		pacmanDistanceToTunnel = GhostsUtils.DistancePacmanToTunnel(game, pacmanPosition);
		pacmanDistanceToPPill = GhostsUtils.DistancePacmanToPP(game,pacmanPosition);
		ghostsCloseIndex = GhostsUtils.CloseIndex(game, pacmanPosition);
		for (GHOST ghost : GHOST.values()) {
			HashMap<GhostsRelevantInfo, Double> ghostMap = new HashMap<GhostsRelevantInfo, Double>();
			if (game.getGhostLairTime(ghost) <= 0) {
				ghostMap.put(GhostsRelevantInfo.DISTANCE_TO_PACMAN, game.getDistance(game.getGhostCurrentNodeIndex(ghost),pacmanPosition, game.getGhostLastMoveMade(ghost), DM.PATH));
				ghostMap.put(GhostsRelevantInfo.PACMAN_DISTANCE_TO_GHOST, game.getDistance(pacmanPosition, game.getGhostCurrentNodeIndex(ghost), DM.PATH));
				ghostMap.put(GhostsRelevantInfo.COLLISION_INDEX, GhostsUtils.PathContainsGhosts(game, ghost, pacmanPosition)? 200.0 : GhostsUtils.numberOfGhostsCloser(game,pacmanPosition,ghost)*25);
			}
			else {
				ghostMap.put(GhostsRelevantInfo.DISTANCE_TO_PACMAN, 200.0);
				ghostMap.put(GhostsRelevantInfo.PACMAN_DISTANCE_TO_GHOST, 200.0);
				ghostMap.put(GhostsRelevantInfo.COLLISION_INDEX, 0.0);
			}
			if(game.isGhostEdible(ghost)) {
				ghostMap.put(GhostsRelevantInfo.DANGER, Math.max(0,(double)game.getGhostEdibleTime(ghost)*2 - (double)game.getDistance(game.getGhostCurrentNodeIndex(ghost),pacmanPosition, game.getGhostLastMoveMade(ghost), DM.PATH)));
				ghostMap.put(GhostsRelevantInfo.EDIBLE_TIME, (double)game.getGhostEdibleTime(ghost));
			}
			else {
				ghostMap.put(GhostsRelevantInfo.DANGER, Math.max(0, game.getDistance(pacmanPosition, nearestPPillToPacman, DM.PATH)));
				ghostMap.put(GhostsRelevantInfo.EDIBLE_TIME,0.0);
			}
			ghostMap.put(GhostsRelevantInfo.TIME_IN_LAIR, (double)game.getGhostLairTime(ghost));
			ghostMap.put(GhostsRelevantInfo.DISTANCE_TO_TUNNEL, game.getDistance(game.getGhostCurrentNodeIndex(ghost), GhostsUtils.NearestTunnelNode(game, ghost), game.getGhostLastMoveMade(ghost), DM.PATH));
			
			ghostsInfoMap.put(ghost, ghostMap);
		}
	}
	public HashMap<String, Double> getFuzzyValues() {
		return null;
	}
	public boolean PacmanIsVisible() {
		return pacmanIsVisible;
	}
	public double getPacmanDistanceToPPill() {
		return pacmanDistanceToPPill;
	}
	public int getLastPacmanPosition() {
		return pacmanPosition;
	}
	public int getNearestPPillToPacman() {
		return nearestPPillToPacman;
	}
	public Double distancetoPacman(GHOST ghost) {
		return ghostsInfoMap.get(ghost).get(GhostsRelevantInfo.DISTANCE_TO_PACMAN);
	}
	public Double PacmanDistanceTo(GHOST ghost) {
		return ghostsInfoMap.get(ghost).get(GhostsRelevantInfo.PACMAN_DISTANCE_TO_GHOST);
	}
	public Double getDangerIndex(GHOST ghost) {
		return ghostsInfoMap.get(ghost).get(GhostsRelevantInfo.DANGER);
	}
	public Double getGhostsCloseIndex() {
		return ghostsCloseIndex;
	}
	public Double getTimeInLair(GHOST ghost) {
		return ghostsInfoMap.get(ghost).get(GhostsRelevantInfo.TIME_IN_LAIR);
	}
	public Double getDistanceToTunnel(GHOST ghost) {
		return ghostsInfoMap.get(ghost).get(GhostsRelevantInfo.DISTANCE_TO_TUNNEL);
	}
	public Double getEdibleTime(GHOST ghost) {
		return ghostsInfoMap.get(ghost).get(GhostsRelevantInfo.EDIBLE_TIME);
	}
	public Double getCollisionIndex(GHOST ghost) {
		return ghostsInfoMap.get(ghost).get(GhostsRelevantInfo.COLLISION_INDEX);
	}
	public Double getPacmanDistanceToTunnel() {
		return pacmanDistanceToTunnel;
	}
}