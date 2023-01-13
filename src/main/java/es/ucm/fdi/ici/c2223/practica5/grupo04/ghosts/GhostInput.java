package es.ucm.fdi.ici.c2223.practica5.grupo04.ghosts;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRQuery;
import es.ucm.fdi.ici.cbr.CBRInput;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Game;

public class GhostInput extends CBRInput {

	public GhostInput(GHOST g, Game game) {
		super(game);
		this.ghost = g;
	}
	
	GHOST ghost;
	
	Integer level;

	Double BlinkyDistance;
	Double PinkyDistance;
	Double InkyDistance;
	Double SueDistance;
	
	Double PacmanDBlinky;
	Double PacmanDPinky;
	Double PacmanDInky;
	Double PacmanDSue;
	
	Integer PPillDistance;

	Integer timeEdible;
	
	Boolean noPPills;
	
	Double ghostsCloseIndex;

	private Integer score;
	
	@Override
	public void parseInput() {
		
		if (ghost != null) {

			level = game.getCurrentLevel();

			score = game.getScore();

			BlinkyDistance = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(GHOST.BLINKY), game.getPacmanLastMoveMade(), DM.EUCLID);
			PinkyDistance = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(GHOST.PINKY), game.getPacmanLastMoveMade(), DM.EUCLID);
			InkyDistance = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(GHOST.INKY), game.getPacmanLastMoveMade(), DM.EUCLID);
			SueDistance = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(GHOST.SUE), game.getPacmanLastMoveMade(), DM.EUCLID);

			PacmanDBlinky = game.getDistance(game.getGhostCurrentNodeIndex(GHOST.BLINKY), game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade(), DM.EUCLID);
			PacmanDPinky = game.getDistance(game.getGhostCurrentNodeIndex(GHOST.PINKY), game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade(), DM.EUCLID);
			PacmanDInky = game.getDistance(game.getGhostCurrentNodeIndex(GHOST.INKY), game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade(), DM.EUCLID);
			PacmanDSue = game.getDistance(game.getGhostCurrentNodeIndex(GHOST.SUE), game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade(), DM.EUCLID);

			timeEdible = game.getGhostEdibleTime(ghost);

			noPPills = game.getActivePillsIndices().length == 0;
			PPillDistance = game.getActivePillsIndices().length == 0 ? 500 :GhostsUtils.NearestActivePPill(game, ghost);
			ghostsCloseIndex = GhostsUtils.CloseIndex(game, game.getPacmanCurrentNodeIndex());
		}
	}

	@Override
	public CBRQuery getQuery() {
		GhostDescription description = new GhostDescription();
		
		description.setLevel(level);
		description.setGhost(ghost);
		description.setBlinkyDistance(BlinkyDistance);
		description.setPinkyDistance(PinkyDistance);
		description.setInkyDistance(InkyDistance);
		description.setSueDistance(SueDistance);
		description.setPacmanDBlinky(PacmanDBlinky);
		description.setPacmanDPinky(PacmanDPinky);
		description.setPacmanDInky(PacmanDInky);
		description.setPacmanDSue(PacmanDSue);
		description.setPPillDistance(PPillDistance);
		description.setTimeEdible(timeEdible);
		description.setNoPPills(noPPills);
		description.setGhostsCloseIndex(ghostsCloseIndex);
		description.setScore(score);
		CBRQuery query = new CBRQuery();
		query.setDescription(description);
		return query;
	}
	
}
