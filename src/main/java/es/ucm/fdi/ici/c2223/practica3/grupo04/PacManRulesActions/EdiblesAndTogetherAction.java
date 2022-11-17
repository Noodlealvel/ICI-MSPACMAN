package es.ucm.fdi.ici.c2223.practica3.grupo04.PacManRulesActions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2223.practica2.grupo04.MsPacmanFSM.MsPacmanInput;
import es.ucm.fdi.ici.c2223.practica2.grupo04.MsPacmanFSM.MsPacmanUtils;
import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class EdiblesAndTogetherAction implements Action, RulesAction {

	@Override
	public String getActionId() {
		return "MsPacman chases nearest ghost";
	}

	@Override
	public MOVE execute(Game game) {
		GHOST nearestedibleghost;
		nearestedibleghost= MsPacmanUtils.getNearestGhost(game, game.getPacmanCurrentNodeIndex(), MsPacmanInput.EatLimit, true);
		if (nearestedibleghost!=null)
		{
			return game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(nearestedibleghost), game.getPacmanLastMoveMade(), DM.EUCLID);	
		}
			return MOVE.NEUTRAL;
	}
	
	public void parseFact(Fact arg0) {
		// TODO Auto-generated method stub
		
	}
}
