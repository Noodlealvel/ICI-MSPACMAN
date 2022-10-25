package es.ucm.fdi.ici.practica2.grupo04.MsPacmanActions;

import es.ucm.fdi.ici.Action;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.MOVE;

public class EatLastPillsAction implements Action {

	@Override
	public String getActionId() {
		return "MsPacman eats last pills";
	}

	@Override
	public MOVE execute(Game game) {
		int pill;
		pill = game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), game.getActivePillsIndices(), DM.EUCLID);
		return game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), pill, game.getPacmanLastMoveMade(), DM.EUCLID);
	}

}
