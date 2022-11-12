package es.ucm.fdi.ici.c2223.practica3.grupo04.GhostsRules.Actions;
import es.ucm.fdi.ici.c2223.practica3.grupo04.GhostsRules.GhostsUtils;
import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import jess.JessException;
import jess.Value;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class GhostsChaseAction implements RulesAction {


	GHOST ghost;
	private Boolean flank;
	public GhostsChaseAction( GHOST ghost) {
		this.ghost = ghost;
	}
	
	@Override
	public String getActionId() {
		return ghost + "chase";
	}


	@Override
	public MOVE execute(Game game) {
		if (game.doesGhostRequireAction(ghost))       
        {
			if (flank == true)
				return game.getNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghost), GhostsUtils.getNodeBetweenPacmanAndPpill(game),game.getGhostLastMoveMade(ghost) ,DM.PATH);
			else
				return game.getNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghost), game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(ghost), DM.EUCLID);
        }
		else
			return MOVE.NEUTRAL;
	}

	@Override
	public void parseFact(Fact actionFact) {
		try {
			Value value = actionFact.getSlotValue("flanks");
			if(value == null)
				return;
			String strategyValue = value.stringValue(null);
			flank = Boolean.valueOf(strategyValue);
		} catch (JessException e) {
			e.printStackTrace();
		}
	}

}
