package es.ucm.fdi.ici.c2223.practica3.grupo04.PacManRulesActions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2223.practica2.grupo04.MsPacmanFSM.MsPacmanUtils;
import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.MOVE;

public class EatLastPillsAction implements Action, RulesAction {

	@Override
	public String getActionId() {
		return "MsPacman eats last pills";
	}

	@Override
	public MOVE execute(Game game) {
		int pill = MsPacmanUtils.getNearestPill(game);
		return game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), pill, game.getPacmanLastMoveMade(), DM.EUCLID);
	}
	
	public void parseFact(Fact arg0) {
		// TODO Auto-generated method stub
		
	}

}
