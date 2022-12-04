package es.ucm.fdi.ici.c2223.practica4.grupo04.pacman.actions;

import es.ucm.fdi.ici.Action;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.MOVE;

public class ChasePowerPillAction implements Action {

	@Override
	public String getActionId() {
		return "goToPPill";
	}

	@Override
	public MOVE execute(Game game) {
		int powerpill;
		powerpill = game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), game.getActivePowerPillsIndices(), DM.EUCLID);
		return game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), powerpill, game.getPacmanLastMoveMade(), DM.EUCLID);
	}

}