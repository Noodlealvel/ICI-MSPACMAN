package es.ucm.fdi.ici.c2223.practica3.grupo04.GhostsRules;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Vector;

import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import es.ucm.fdi.ici.rules.RulesInput;


public class GhostsInput extends RulesInput {

	private HashMap<GHOST,HashMap<GhostsRelevantInfo, Boolean>> ghostsInfoMap;
	private boolean eatenPPill;
	private boolean pacmanNearPPill;
	private boolean noPPills;
	private MOVE pacmanLastMovement;
	private boolean pacmanInTunnel;
	private GHOST nearestGhostToPacman;
	private static final int SECURITY_DISTANCE = 60;
	private static final int CHASE = 50;
	private static final int LOW_TIME = 10;
	private static final int CLOSE_PACMAN_DISTANCE = 20;
	private static final int CLOSE_GHOSTS = 120;
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
			ghostMap.put(GhostsRelevantInfo.IN_LAIR, game.getGhostLairTime(ghost) > 0);
			ghostMap.put(GhostsRelevantInfo.NEAR_TUNNEL, game.getGhostLairTime(ghost) <= 0 && game.getDistance(game.getGhostCurrentNodeIndex(ghost), GhostsUtils.NearestTunnelNode(game, ghost),game.getGhostLastMoveMade(ghost), DM.EUCLID) <= CLOSE_PACMAN_DISTANCE);
			ghostMap.put(GhostsRelevantInfo.PACMAN_INVECINITY, game.getGhostLairTime(ghost) <= 0 && game.getDistance(game.getPacmanCurrentNodeIndex(),game.getGhostCurrentNodeIndex(ghost), game.getPacmanLastMoveMade() , DM.PATH) <= SECURITY_DISTANCE);
			ghostMap.put(GhostsRelevantInfo.GHOSTS_CLOSE,  game.getGhostLairTime(ghost) <= 0 && GhostsUtils.GhostCloseToRest(game,ghost, CLOSE_GHOSTS));
			ghostMap.put(GhostsRelevantInfo.CHASE_DISTANCE, game.getGhostLairTime(ghost) <= 0 && game.getDistance(game.getGhostCurrentNodeIndex(ghost), game.getPacmanCurrentNodeIndex(),game.getGhostLastMoveMade(ghost), DM.PATH) <= CHASE);
			ghostMap.put(GhostsRelevantInfo.LOW_EDIBLE_TIME, game.getGhostEdibleTime(ghost) <= LOW_TIME);
			ghostMap.put(GhostsRelevantInfo.PACMAN_CLOSE, game.getGhostLairTime(ghost) <= 0 && game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghost), game.getPacmanLastMoveMade() ,DM.PATH) <= CLOSE_PACMAN_DISTANCE);
			ghostMap.put(GhostsRelevantInfo.NO_GHOSTS_IN_PATH, game.getGhostLairTime(ghost) <= 0 && !GhostsUtils.PathContainsGhosts(game, ghost));
			ghostMap.put(GhostsRelevantInfo.JUST_BEHIND, game.getGhostLairTime(ghost) <= 0 && GhostsUtils.JustBehindPacman(game,ghost));
			ghostsInfoMap.put(ghost, ghostMap);
			
		}
		pacmanLastMovement = game.getPacmanLastMoveMade();
		pacmanInTunnel = GhostsUtils.PacmanInTunnel(game);
		noPPills = game.getNumberOfActivePowerPills() <= 0;
		eatenPPill = game.wasPowerPillEaten();
		pacmanNearPPill = GhostsUtils.PacmanCloseToPPill(game,CLOSE_PACMAN_DISTANCE);
		nearestGhostToPacman = GhostsUtils.NearestGhostToPacman(game);
	}
	
	public boolean PPillEaten() {
		return eatenPPill;
	}
	public boolean wasGhostEaten(GHOST ghost) {
		return ghostsInfoMap.get(ghost).get(GhostsRelevantInfo.EATEN);
	}
	
	public boolean danger(GHOST ghost) {
		return ghostsInfoMap.get(ghost).get(GhostsRelevantInfo.EDIBLE) || (pacmanNearPPill && GhostsUtils.PacmanCloseToGhost(game, ghost, SECURITY_DISTANCE));
	}

	public boolean isOutOfLair(GHOST ghost) {
		return !ghostsInfoMap.get(ghost).get(GhostsRelevantInfo.IN_LAIR);		
	}
	public boolean isTunnelNear(GHOST ghost) {
		return ghostsInfoMap.get(ghost).get(GhostsRelevantInfo.NEAR_TUNNEL);
	}
	public boolean isPacmanInVecinity(GHOST ghost) {
		return ghostsInfoMap.get(ghost).get(GhostsRelevantInfo.PACMAN_INVECINITY);
	}
	public boolean farFromPacmanPath(GHOST ghost) {
		return !ghostsInfoMap.get(ghost).get(GhostsRelevantInfo.PACMAN_CLOSE);
	}
	public boolean lowEdibleTime(GHOST ghost) {
		return ghostsInfoMap.get(ghost).get(GhostsRelevantInfo.LOW_EDIBLE_TIME);
	}
	public boolean closeToOtherGhosts(GHOST ghost) {
		return ghostsInfoMap.get(ghost).get(GhostsRelevantInfo.GHOSTS_CLOSE);
	}
	public boolean nearToPacmanPath(GHOST ghost) {
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
	public boolean pacmanInTunnel() {
		return pacmanInTunnel;
	}
	public boolean chaseableDistance(GHOST ghost) {
		return ghostsInfoMap.get(ghost).get(GhostsRelevantInfo.CHASE_DISTANCE);
	}
	public boolean isJustBehindPacman(GHOST ghost) {
		return ghostsInfoMap.get(ghost).get(GhostsRelevantInfo.JUST_BEHIND);
	}
	public boolean isNearestGhost(GHOST ghost) {
		return ghost == nearestGhostToPacman;
	}
	public boolean nearPPill() {
		return pacmanNearPPill;
	}
	public boolean isGhostEdible(GHOST ghost) {
		return ghostsInfoMap.get(ghost).get(GhostsRelevantInfo.EDIBLE);
	}
	@Override
	public Collection<String> getFacts() {
		Vector<String> facts = new Vector<String>();
		facts.add(String.format("(BLINKY (edible %s) (eaten %s) (inLair %s) (lowEdibleTime %s) (pacmanInVecinity %s) (nearTunnel %s) (pacmanClose %s) (ghostsClose %s) (noGhostsInPath %s) (chaseDistance %s) (justBehind %s) )", 
				ghostsInfoMap.get(GHOST.BLINKY).get(GhostsRelevantInfo.EDIBLE), 
				ghostsInfoMap.get(GHOST.BLINKY).get(GhostsRelevantInfo.EATEN), 
				ghostsInfoMap.get(GHOST.BLINKY).get(GhostsRelevantInfo.IN_LAIR), 
				ghostsInfoMap.get(GHOST.BLINKY).get(GhostsRelevantInfo.LOW_EDIBLE_TIME), 
				ghostsInfoMap.get(GHOST.BLINKY).get(GhostsRelevantInfo.PACMAN_INVECINITY), 
				ghostsInfoMap.get(GHOST.BLINKY).get(GhostsRelevantInfo.NEAR_TUNNEL), 
				ghostsInfoMap.get(GHOST.BLINKY).get(GhostsRelevantInfo.PACMAN_CLOSE),
				ghostsInfoMap.get(GHOST.BLINKY).get(GhostsRelevantInfo.GHOSTS_CLOSE),
				ghostsInfoMap.get(GHOST.BLINKY).get(GhostsRelevantInfo.NO_GHOSTS_IN_PATH),
				ghostsInfoMap.get(GHOST.BLINKY).get(GhostsRelevantInfo.CHASE_DISTANCE),
				ghostsInfoMap.get(GHOST.BLINKY).get(GhostsRelevantInfo.JUST_BEHIND)));
		facts.add(String.format("(INKY (edible %s) (eaten %s) (inLair %s) (lowEdibleTime %s) (pacmanInVecinity %s) (nearTunnel %s) (pacmanClose %s) (ghostsClose %s) (noGhostsInPath %s) (chaseDistance %s) (justBehind %s) )", 
				ghostsInfoMap.get(GHOST.INKY).get(GhostsRelevantInfo.EDIBLE), 
				ghostsInfoMap.get(GHOST.INKY).get(GhostsRelevantInfo.EATEN), 
				ghostsInfoMap.get(GHOST.INKY).get(GhostsRelevantInfo.IN_LAIR), 
				ghostsInfoMap.get(GHOST.INKY).get(GhostsRelevantInfo.LOW_EDIBLE_TIME), 
				ghostsInfoMap.get(GHOST.INKY).get(GhostsRelevantInfo.PACMAN_INVECINITY), 
				ghostsInfoMap.get(GHOST.INKY).get(GhostsRelevantInfo.NEAR_TUNNEL), 
				ghostsInfoMap.get(GHOST.INKY).get(GhostsRelevantInfo.PACMAN_CLOSE),
				ghostsInfoMap.get(GHOST.INKY).get(GhostsRelevantInfo.GHOSTS_CLOSE),
				ghostsInfoMap.get(GHOST.INKY).get(GhostsRelevantInfo.NO_GHOSTS_IN_PATH),
				ghostsInfoMap.get(GHOST.INKY).get(GhostsRelevantInfo.CHASE_DISTANCE),
				ghostsInfoMap.get(GHOST.INKY).get(GhostsRelevantInfo.JUST_BEHIND)));
		facts.add(String.format("(PINKY (edible %s) (eaten %s) (inLair %s) (lowEdibleTime %s) (pacmanInVecinity %s) (nearTunnel %s) (pacmanClose %s) (ghostsClose %s) (noGhostsInPath %s) (chaseDistance %s) (justBehind %s) )", 
				ghostsInfoMap.get(GHOST.PINKY).get(GhostsRelevantInfo.EDIBLE), 
				ghostsInfoMap.get(GHOST.PINKY).get(GhostsRelevantInfo.EATEN), 
				ghostsInfoMap.get(GHOST.PINKY).get(GhostsRelevantInfo.IN_LAIR), 
				ghostsInfoMap.get(GHOST.PINKY).get(GhostsRelevantInfo.LOW_EDIBLE_TIME), 
				ghostsInfoMap.get(GHOST.PINKY).get(GhostsRelevantInfo.PACMAN_INVECINITY), 
				ghostsInfoMap.get(GHOST.PINKY).get(GhostsRelevantInfo.NEAR_TUNNEL), 
				ghostsInfoMap.get(GHOST.PINKY).get(GhostsRelevantInfo.PACMAN_CLOSE),
				ghostsInfoMap.get(GHOST.PINKY).get(GhostsRelevantInfo.GHOSTS_CLOSE),
				ghostsInfoMap.get(GHOST.PINKY).get(GhostsRelevantInfo.NO_GHOSTS_IN_PATH),
				ghostsInfoMap.get(GHOST.PINKY).get(GhostsRelevantInfo.CHASE_DISTANCE),
				ghostsInfoMap.get(GHOST.PINKY).get(GhostsRelevantInfo.JUST_BEHIND)));
		facts.add(String.format("(SUE (edible %s) (eaten %s) (inLair %s) (lowEdibleTime %s) (pacmanInVecinity %s) (nearTunnel %s) (pacmanClose %s) (ghostsClose %s) (noGhostsInPath %s) (chaseDistance %s) (justBehind %s) )", 
				ghostsInfoMap.get(GHOST.SUE).get(GhostsRelevantInfo.EDIBLE), 
				ghostsInfoMap.get(GHOST.SUE).get(GhostsRelevantInfo.EATEN), 
				ghostsInfoMap.get(GHOST.SUE).get(GhostsRelevantInfo.IN_LAIR), 
				ghostsInfoMap.get(GHOST.SUE).get(GhostsRelevantInfo.LOW_EDIBLE_TIME), 
				ghostsInfoMap.get(GHOST.SUE).get(GhostsRelevantInfo.PACMAN_INVECINITY), 
				ghostsInfoMap.get(GHOST.SUE).get(GhostsRelevantInfo.NEAR_TUNNEL), 
				ghostsInfoMap.get(GHOST.SUE).get(GhostsRelevantInfo.PACMAN_CLOSE),
				ghostsInfoMap.get(GHOST.SUE).get(GhostsRelevantInfo.GHOSTS_CLOSE),
				ghostsInfoMap.get(GHOST.SUE).get(GhostsRelevantInfo.NO_GHOSTS_IN_PATH),
				ghostsInfoMap.get(GHOST.SUE).get(GhostsRelevantInfo.CHASE_DISTANCE),
				ghostsInfoMap.get(GHOST.SUE).get(GhostsRelevantInfo.JUST_BEHIND)));
		
		facts.add(String.format("(MSPACMAN (pacmanInTunnel %s) (noPPills %s) (eatenPPill %s) (pacmanNearPPill %s) )", 
				pacmanInTunnel, noPPills, eatenPPill, pacmanNearPPill));
		return facts;
	}
}

