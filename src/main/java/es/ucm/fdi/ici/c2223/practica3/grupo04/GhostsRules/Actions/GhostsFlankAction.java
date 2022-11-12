package es.ucm.fdi.ici.c2223.practica3.grupo04.GhostsRules.Actions;

import es.ucm.fdi.ici.c2223.practica3.grupo04.GhostsRules.GhostsUtils;
import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class GhostsFlankAction implements RulesAction {


	GHOST ghost;
	public GhostsFlankAction( GHOST ghost) {
		this.ghost = ghost;
	}
	
	@Override
	public String getActionId() {
		return ghost.toString() + "Flank";
	}

	@Override
	public MOVE execute(Game game) {
		if (game.doesGhostRequireAction(ghost))       
        {
			return game.getNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghost), GhostsUtils.getNodeBetweenPacmanAndPpill(game),game.getGhostLastMoveMade(ghost) ,DM.PATH);
        }
		else
			return MOVE.NEUTRAL;
		}

	@Override
	public void parseFact(Fact actionFact) {
		
	}

}
