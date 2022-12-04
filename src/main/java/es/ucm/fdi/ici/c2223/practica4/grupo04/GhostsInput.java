package es.ucm.fdi.ici.c2223.practica4.grupo04;

import java.util.HashMap;

import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Game;
import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2122.practica1.grupo05.GameUtils;


public class GhostsInput extends Input {

	private HashMap<GHOST,HashMap<GhostsRelevantInfo, Double>> ghostsInfoMap;
	private boolean pacmanNearPPill;
	private boolean pacmanInTunnel;
	private boolean pacmanIsVisible;
	private int pacmanPosition;
	private double pacmanDistanceToTunnel;
	private Object pacmanDistanceToPPill;
	private double ghostsCloseIndex;
	private static final int SECURITY_DISTANCE = 60;
	private static final int CHASE = 50;
	private static final int LOW_TIME = 10;
	private static final int CLOSE_PACMAN_DISTANCE = 20;
	private static final int CLOSE_GHOSTS = 120;
	public GhostsInput(Game game, int lastPacmanPosition) {
		super(game);
		pacmanPosition = lastPacmanPosition;
	}
	@Override
	public void parseInput() {
		/*ghostsInfoMap = new HashMap<GHOST,HashMap<GhostsRelevantInfo, Boolean>>();
		for (GHOST ghost : GHOST.values()) {
			HashMap<GhostsRelevantInfo, Boolean> ghostMap = new HashMap<GhostsRelevantInfo, Boolean>();
			ghostMap.put(GhostsRelevantInfo.EDIBLE, game.isGhostEdible(ghost));
			ghostMap.put(GhostsRelevantInfo.IN_LAIR, game.getGhostLairTime(ghost) > 0);
			ghostMap.put(GhostsRelevantInfo.NEAR_TUNNEL, game.getGhostLairTime(ghost) <= 0 && game.getDistance(game.getGhostCurrentNodeIndex(ghost), GhostsUtils.NearestTunnelNode(game, ghost),game.getGhostLastMoveMade(ghost), DM.EUCLID) <= CLOSE_PACMAN_DISTANCE);
			ghostMap.put(GhostsRelevantInfo.GHOSTS_CLOSE,  game.getGhostLairTime(ghost) <= 0 && GhostsUtils.GhostCloseToRest(game,ghost, CLOSE_GHOSTS));
			ghostMap.put(GhostsRelevantInfo.CHASE_DISTANCE, game.getGhostLairTime(ghost) <= 0 && game.getDistance(game.getGhostCurrentNodeIndex(ghost), game.getPacmanCurrentNodeIndex(),game.getGhostLastMoveMade(ghost), DM.PATH) <= CHASE);
			ghostMap.put(GhostsRelevantInfo.LOW_EDIBLE_TIME, game.getGhostEdibleTime(ghost) <= LOW_TIME);
			ghostMap.put(GhostsRelevantInfo.PACMAN_CLOSE, game.getGhostLairTime(ghost) <= 0 && game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghost), game.getPacmanLastMoveMade() ,DM.PATH) <= CLOSE_PACMAN_DISTANCE);
			ghostMap.put(GhostsRelevantInfo.NO_GHOSTS_IN_PATH, game.getGhostLairTime(ghost) <= 0 && !GhostsUtils.PathContainsGhosts(game, ghost));
			ghostsInfoMap.put(ghost, ghostMap);
			
		}
		*/
		pacmanIsVisible = game.getPacmanCurrentNodeIndex() != -1;
		if (pacmanIsVisible) {
			pacmanPosition = game.getPacmanCurrentNodeIndex();
		}
		for (GHOST ghost : GHOST.values()) {
			HashMap<GhostsRelevantInfo, Double> ghostMap = new HashMap<GhostsRelevantInfo, Double>();
			if (game.getGhostLairTime(ghost) < 0) {
				ghostMap.put(GhostsRelevantInfo.DISTANCE_TO_PACMAN, game.getDistance(game.getGhostCurrentNodeIndex(ghost),pacmanPosition, game.getGhostLastMoveMade(ghost), DM.PATH));
				ghostMap.put(GhostsRelevantInfo.PACMAN_DISTANCE_TO_GHOST, game.getDistance(pacmanPosition, game.getGhostCurrentNodeIndex(ghost), game.getPacmanLastMoveMade(), DM.PATH));
				ghostMap.put(GhostsRelevantInfo.COLLISION_INDEX, GhostsUtils.PathContainsGhosts(game, ghost)? 200.0 : GhostsUtils.numberOfGhostsCloser(game,pacmanPosition,ghost)*25);
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
				ghostMap.put(GhostsRelevantInfo.DANGER, 0.0);
				ghostMap.put(GhostsRelevantInfo.EDIBLE_TIME,0.0);
			}
			ghostMap.put(GhostsRelevantInfo.TIME_IN_LAIR, (double)game.getGhostLairTime(ghost));
			ghostMap.put(GhostsRelevantInfo.DISTANCE_TO_TUNNEL, game.getDistance(game.getGhostCurrentNodeIndex(ghost), GhostsUtils.NearestTunnelNode(game, ghost), game.getGhostLastMoveMade(ghost), DM.PATH));
			
			ghostsInfoMap.put(ghost, ghostMap);
		}
		pacmanDistanceToTunnel = GhostsUtils.DistancePacmanToTunnel(game, pacmanPosition);
		pacmanDistanceToPPill = GhostsUtils.DistancePacmanToPP(game,pacmanPosition,CLOSE_PACMAN_DISTANCE);
		ghostsCloseIndex = GhostsUtils.CloseIndex(game, pacmanPosition);
	}
	public boolean nearPPill() {
		return pacmanNearPPill;
	}
	public HashMap<String, Double> getFuzzyValues() {
		return null;
	}
	public boolean PacmanIsVisible() {
		return pacmanIsVisible;
	}
	public Double getPacmanDistanceToPPill() {
		return ghostsInfoMap.get(ghostsInfoMap);
	}
}