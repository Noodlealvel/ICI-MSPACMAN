package es.ucm.fdi.ici.c2223.practica4.grupo04.ghosts.actions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2223.practica4.grupo04.ghosts.GhostsUtils;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class GhostsSearchForTunnelAction implements Action {
	  GHOST ghost;
		public GhostsSearchForTunnelAction( GHOST ghost) {
			this.ghost = ghost;
		}
	@Override
	public String getActionId() {
		return ghost.toString() + "searchForTunnel";
	}

	@Override
	public MOVE execute(Game game) {
		
		if (game.doesGhostRequireAction(ghost))       
        {
			return game.getNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghost), GhostsUtils.NearestTunnelNode(game, ghost), DM.PATH);

        }
		else
			return MOVE.NEUTRAL;
		}

}
