package es.ucm.fdi.ici.c2223.practica2.grupo04.GhostsFSM;

import es.ucm.fdi.ici.Action;
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
		return "SearchForTunnel";
	}

	@Override
	public MOVE execute(Game game) {
		
		return null;
	}

}
