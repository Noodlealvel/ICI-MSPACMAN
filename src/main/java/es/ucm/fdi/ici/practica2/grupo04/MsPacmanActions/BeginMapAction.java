package es.ucm.fdi.ici.practica2.grupo04.MsPacmanActions;

import java.util.Random;

import es.ucm.fdi.ici.Action;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class BeginMapAction implements Action{

	
	private MOVE[] allMoves = MOVE.values();
	private Random rnd = new Random();
	
	@Override
	public String getActionId() {
		return "MsPacman begins map";
	}

	@Override
	public MOVE execute(Game game) {
		return allMoves[rnd.nextInt(allMoves.length)];
	}

}
