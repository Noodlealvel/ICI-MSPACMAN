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

	double BlinkyDistance;
	double PinkyDistance;
	double InkyDistance;
	double SueDistance;
	
	double PacmanDBlinky;
	double PacmanDPinky;
	double PacmanDInky;
	double PacmanDSue;
	
	Integer PPillUp;
	Integer PPillDown;
	Integer PPillRight;
	Integer PPillLeft;
	
	Integer PillUp;
	Integer PillDown;
	Integer PillRight;
	Integer PillLeft;
	
	Integer BlinkyTimeEdible;
	Integer PinkyTimeEdible;
	Integer InkyTimeEdible;
	Integer SueTimeEdible;
	
	Integer numPills;
	Integer eatValue;
	
	@Override
	public void parseInput() {
		
		BlinkyDistance = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(GHOST.BLINKY), game.getPacmanLastMoveMade(), DM.EUCLID);
		PinkyDistance = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(GHOST.PINKY), game.getPacmanLastMoveMade(), DM.EUCLID);
		InkyDistance = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(GHOST.INKY), game.getPacmanLastMoveMade(), DM.EUCLID);
		SueDistance = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(GHOST.SUE), game.getPacmanLastMoveMade(), DM.EUCLID);
		
		PacmanDBlinky = game.getDistance(game.getGhostCurrentNodeIndex(GHOST.BLINKY), game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade(), DM.EUCLID);
		PacmanDPinky = game.getDistance(game.getGhostCurrentNodeIndex(GHOST.PINKY), game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade(), DM.EUCLID);
		PacmanDInky = game.getDistance(game.getGhostCurrentNodeIndex(GHOST.INKY), game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade(), DM.EUCLID);
		PacmanDSue = game.getDistance(game.getGhostCurrentNodeIndex(GHOST.SUE), game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade(), DM.EUCLID);
		
		PPillUp = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getNeighbour(game.getPacmanCurrentNodeIndex(), MOVE.UP));
		PPillDown = 
		PPillRight =
		PPillLeft = 
		
		PillUp = 
		PillDown = 
		PillRight =
		PillLeft = 
		
		BlinkyTimeEdible = game.getGhostEdibleTime(GHOST.BLINKY);
		PinkyTimeEdible = game.getGhostEdibleTime(GHOST.PINKY);
		InkyTimeEdible = game.getGhostEdibleTime(GHOST.INKY);
		SueTimeEdible = game.getGhostEdibleTime(GHOST.SUE);
		
		numPills = game.getActivePillsIndices().length;
		eatValue = game.getGhostCurrentEdibleScore();
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
		
		CBRQuery query = new CBRQuery();
		query.setDescription(description);
		return query;
	}
	
}
