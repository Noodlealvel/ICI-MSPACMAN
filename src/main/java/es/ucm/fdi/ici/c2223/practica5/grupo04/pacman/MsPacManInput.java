package es.ucm.fdi.ici.c2223.practica5.grupo04.pacman;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRQuery;
import es.ucm.fdi.ici.cbr.CBRInput;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MsPacManInput extends CBRInput {

	public MsPacManInput(Game game) {
		super(game);
		
	}

	Double BlinkyDistance;
	Double PinkyDistance;
	Double InkyDistance;
	Double SueDistance;
	
	Double PacmanDBlinky;
	Double PacmanDPinky;
	Double PacmanDInky;
	Double PacmanDSue;
	
	Double PPillUp;
	Double PPillDown;
	Double PPillRight;
	Double PPillLeft;
	
	Double PillUp;
	Double PillDown;
	Double PillRight;
	Double PillLeft;
	
	Integer BlinkyTimeEdible;
	Integer PinkyTimeEdible;
	Integer InkyTimeEdible;
	Integer SueTimeEdible;
	
	Integer numPills;
	Integer eatValue;
	
	Integer score;
	
	public static int getNearestP(Game game, int limit, MOVE move) {
		double powerPillDistance;
		double shortestDistance = -1;
		int nearestPPill = -1;
		for (int pillNode : game.getActivePowerPillsIndices()) {
			powerPillDistance = game.getDistance(game.getPacmanCurrentNodeIndex(), pillNode, move, DM.PATH);
			if ((powerPillDistance < shortestDistance && powerPillDistance < limit) || shortestDistance == -1) {
				shortestDistance = powerPillDistance;
				nearestPPill = pillNode;
			}
		}
		return nearestPPill;
	}
	
	public static int getNearestPP(Game game, int limit, MOVE move) {
		double powerPillDistance;
		double shortestDistance = -1;
		int nearestPPill = -1;
		for (int pillNode : game.getActivePowerPillsIndices()) {
			powerPillDistance = game.getDistance(game.getPacmanCurrentNodeIndex(), pillNode, move, DM.PATH);
			if ((powerPillDistance < shortestDistance && powerPillDistance < limit) || shortestDistance == -1) {
				shortestDistance = powerPillDistance;
				nearestPPill = pillNode;
			}
		}
		return nearestPPill;
	}
	@Override
	public void parseInput() {
		
		BlinkyDistance = game.getGhostLairTime(GHOST.BLINKY) > 0 ? 500.0 :game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(GHOST.BLINKY), game.getPacmanLastMoveMade(), DM.PATH);
		PinkyDistance = game.getGhostLairTime(GHOST.PINKY) > 0  ? 500.0 :game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(GHOST.PINKY), game.getPacmanLastMoveMade(), DM.PATH);
		InkyDistance = game.getGhostLairTime(GHOST.INKY) > 0  ? 500.0 :game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(GHOST.INKY), game.getPacmanLastMoveMade(), DM.PATH);
		SueDistance = game.getGhostLairTime(GHOST.SUE) > 0  ? 500.0 :game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(GHOST.SUE), game.getPacmanLastMoveMade(), DM.PATH);
		
		PacmanDBlinky = game.getGhostLairTime(GHOST.BLINKY) > 0  ? 500.0 :game.getDistance(game.getGhostCurrentNodeIndex(GHOST.BLINKY), game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(GHOST.BLINKY), DM.PATH);
		PacmanDPinky = game.getGhostLairTime(GHOST.PINKY) > 0   ? 500.0 :game.getDistance(game.getGhostCurrentNodeIndex(GHOST.PINKY), game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(GHOST.PINKY), DM.PATH);
		PacmanDInky = game.getGhostLairTime(GHOST.INKY) > 0   ? 500.0 :game.getDistance(game.getGhostCurrentNodeIndex(GHOST.INKY), game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(GHOST.INKY), DM.PATH);
		PacmanDSue = game.getGhostLairTime(GHOST.SUE) > 0  ? 500.0 :game.getDistance(game.getGhostCurrentNodeIndex(GHOST.SUE), game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(GHOST.SUE), DM.PATH);
		
		PPillUp = game.getNeighbour(game.getPacmanCurrentNodeIndex(), MOVE.UP) == -1 ? -1 :game.getDistance(game.getNeighbour(game.getPacmanCurrentNodeIndex(), MOVE.UP), getNearestPP(game, 250, MOVE.UP), DM.EUCLID);
		PPillDown = game.getNeighbour(game.getPacmanCurrentNodeIndex(), MOVE.DOWN) == -1 ? -1 :game.getDistance(game.getNeighbour(game.getPacmanCurrentNodeIndex(), MOVE.DOWN), getNearestPP(game, 250, MOVE.DOWN), DM.EUCLID);
		PPillRight = game.getNeighbour(game.getPacmanCurrentNodeIndex(), MOVE.RIGHT) == -1 ? -1 :game.getDistance(game.getNeighbour(game.getPacmanCurrentNodeIndex(), MOVE.RIGHT), getNearestPP(game, 250, MOVE.RIGHT), DM.EUCLID);
		PPillLeft = game.getNeighbour(game.getPacmanCurrentNodeIndex(), MOVE.LEFT) == -1 ? -1 :game.getDistance(game.getNeighbour(game.getPacmanCurrentNodeIndex(), MOVE.LEFT), getNearestPP(game, 250, MOVE.LEFT), DM.EUCLID);
		
		PillUp = game.getNeighbour(game.getPacmanCurrentNodeIndex(), MOVE.UP) == -1 ? -1 :game.getDistance(game.getNeighbour(game.getPacmanCurrentNodeIndex(), MOVE.UP), getNearestP(game, 250, MOVE.UP), DM.EUCLID);
		PillDown = game.getNeighbour(game.getPacmanCurrentNodeIndex(), MOVE.DOWN) == -1 ? -1 :game.getDistance(game.getNeighbour(game.getPacmanCurrentNodeIndex(), MOVE.DOWN), getNearestP(game, 250, MOVE.DOWN), DM.EUCLID);
		PillRight = game.getNeighbour(game.getPacmanCurrentNodeIndex(), MOVE.RIGHT) == -1 ? -1 :game.getDistance(game.getNeighbour(game.getPacmanCurrentNodeIndex(), MOVE.RIGHT), getNearestP(game, 250, MOVE.RIGHT), DM.EUCLID);
		PillLeft = game.getNeighbour(game.getPacmanCurrentNodeIndex(), MOVE.LEFT) == -1 ? -1 :game.getDistance(game.getNeighbour(game.getPacmanCurrentNodeIndex(), MOVE.LEFT), getNearestP(game, 250, MOVE.LEFT), DM.EUCLID);
		
		BlinkyTimeEdible = game.getGhostEdibleTime(GHOST.BLINKY);
		PinkyTimeEdible = game.getGhostEdibleTime(GHOST.PINKY);
		InkyTimeEdible = game.getGhostEdibleTime(GHOST.INKY);
		SueTimeEdible = game.getGhostEdibleTime(GHOST.SUE);
		
		numPills = game.getActivePillsIndices().length;
		eatValue = game.getGhostCurrentEdibleScore();
		
		score = game.getScore();
	}

	@Override
	public CBRQuery getQuery() {
		MsPacManDescription description = new MsPacManDescription();
		
		description.setBlinkyDistance(BlinkyDistance);
		description.setPinkyDistance(PinkyDistance);
		description.setInkyDistance(InkyDistance);
		description.setSueDistance(SueDistance);
		description.setPacmanDBlinky(PacmanDBlinky);
		description.setPacmanDPinky(PacmanDPinky);
		description.setPacmanDInky(PacmanDInky);
		description.setPacmanDSue(PacmanDSue);
		description.setPPillUp(PPillUp);
		description.setPPillDown(PPillDown);
		description.setPPillRight(PPillRight);
		description.setPPillLeft(PPillLeft);
		description.setPillUp(PPillUp);
		description.setPillDown(PPillDown);
		description.setPillRight(PPillRight);
		description.setPPillLeft(PPillLeft);
		description.setBlinkyTimeEdible(BlinkyTimeEdible);
		description.setPinkyTimeEdible(PinkyTimeEdible);
		description.setInkyTimeEdible(InkyTimeEdible);
		description.setSueTimeEdible(SueTimeEdible);
		description.setEatValue(eatValue);
		description.setNumPills(numPills);
		description.setScore(score);
		
		CBRQuery query = new CBRQuery();
		query.setDescription(description);
		return query;
	}
	
}
