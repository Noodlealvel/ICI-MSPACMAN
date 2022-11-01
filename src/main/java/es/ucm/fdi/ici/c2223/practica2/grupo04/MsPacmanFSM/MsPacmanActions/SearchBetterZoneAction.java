package es.ucm.fdi.ici.c2223.practica2.grupo04.MsPacmanFSM.MsPacmanActions;

import es.ucm.fdi.ici.Action;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class SearchBetterZoneAction implements Action {

	@Override
	public String getActionId() {
		return "MsPacman goes to the best zone";
	}

	@Override
	public MOVE execute(Game game) {
		
		return game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), getBestZone(game), game.getPacmanLastMoveMade(), DM.EUCLID);	
	}
	
	
	private int getBestZone(Game game) {

		int pill=0;
		int[] powerpills = game.getActivePowerPillsIndices();
		int[] path;
		
		switch(game.getActivePowerPillsIndices().length) {
			case 1:
			case 4:
				pill = game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), game.getActivePowerPillsIndices(), DM.EUCLID);
				break;
			case 2:
				if(game.getShortestPathDistance(powerpills[0], powerpills[1]) > 100) {
					pill = game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), game.getActivePowerPillsIndices(), DM.EUCLID);
				}
				else {
					path = game.getShortestPath(powerpills[0], powerpills[1]);
					pill = path[path.length/2];
				}
				break;
			case 3:
				if(game.getShortestPathDistance(powerpills[0], powerpills[1]) < 50) {
					path = game.getShortestPath(powerpills[0], powerpills[1]);
					pill = path[path.length/2];
				}
				if(game.getShortestPathDistance(powerpills[1], powerpills[2]) < 50) {
					path = game.getShortestPath(powerpills[1], powerpills[2]);
					pill = path[path.length/2];
				}
				if(game.getShortestPathDistance(powerpills[2], powerpills[0]) < 50) {
					path = game.getShortestPath(powerpills[2], powerpills[0]);
					pill = path[path.length/2];
				}
				
				break;
				
			default:
				pill = game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), game.getActivePowerPillsIndices(), DM.EUCLID);
		}
		
		return pill;
	}

}
