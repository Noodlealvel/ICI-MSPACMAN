package es.ucm.fdi.ici.practica2.grupo04.MsPacmanActions;

import es.ucm.fdi.ici.Action;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.MOVE;

public class ChasePowerPillAction implements Action {

	@Override
	public String getActionId() {
		return "MsPacman chases Power Pill";
	}

	@Override
	public MOVE execute(Game game) {
		int powerpill;
		powerpill = game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), game.getActivePowerPillsIndices(), DM.EUCLID);
		return game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), powerpill, game.getPacmanLastMoveMade(), DM.EUCLID);
	}

}