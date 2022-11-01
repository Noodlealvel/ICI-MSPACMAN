package es.ucm.fdi.ici.c2223.practica2.grupo04.MsPacmanFSM.MsPacmanActions;

import java.util.Random;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2223.practica2.grupo04.MsPacmanFSM.MsPacmanUtils;
import es.ucm.fdi.ici.c2223.practica2.grupo04.MsPacmanFSM.MsPacmanInput;
import pacman.game.Constants.DM;
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
		int PPill = MsPacmanUtils.getNearestPP(game, MsPacmanInput.PPDistance);
		if(PPill!=-1 )
		{
			return game.getApproximateNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(), PPill, game.getPacmanLastMoveMade(), DM.EUCLID);
		}
		return allMoves[rnd.nextInt(allMoves.length)];
	}
}
