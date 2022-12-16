package es.ucm.fdi.ici.c2223.practica5.grupo04.actions;


import es.ucm.fdi.ici.Action;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class LeftAction implements Action {
    
	public LeftAction() {}
	
	@Override
	public MOVE execute(Game game) {
		return MOVE.LEFT;
    }

	@Override
	public String getActionId() {
		return "Left";
	}
}