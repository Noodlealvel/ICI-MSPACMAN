package es.ucm.fdi.ici.c2223.practica4.grupo04.ghosts.actions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2223.practica4.grupo04.ghosts.GhostsFuzzyMemory;
import es.ucm.fdi.ici.c2223.practica4.grupo04.ghosts.GhostsUtils;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class GhostsFlankAction implements Action {


	GHOST ghost;
	private GhostsFuzzyMemory fuzzyMemory;
	public GhostsFlankAction( GHOST ghost, GhostsFuzzyMemory fuzzyMemory) {
		this.ghost = ghost;
		this.fuzzyMemory = fuzzyMemory;
	}
	
	@Override
	public String getActionId() {
		return ghost.toString() + "flank";
	}

	@Override
	public MOVE execute(Game game) {
		if (game.doesGhostRequireAction(ghost))       
        {
			return game.getNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghost), GhostsUtils.getNodeBetweenPacmanAndPpill(game, fuzzyMemory.lastPacmanPosition(), fuzzyMemory.getLastClosePP()),game.getGhostLastMoveMade(ghost) ,DM.PATH);
        }
		else
			return MOVE.NEUTRAL;
		}

}
