package es.ucm.fdi.ici.c2223.practica2.grupo04.MsPacmanFSM.MsPacmanActions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2223.practica2.grupo04.MsPacmanFSM.MsPacmanUtils;
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
		int pill = MsPacmanUtils.getNearestPill(game);
		return game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), pill, game.getPacmanLastMoveMade(), DM.EUCLID);
	}

}
