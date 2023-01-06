package es.ucm.fdi.ici.c2223.practica5.grupo04;

import java.util.EnumMap;
import java.util.HashMap;
import es.ucm.fdi.gaia.jcolibri.exception.ExecutionException;
import es.ucm.fdi.ici.c2223.practica5.grupo04.ghosts.GhostCBRengine;
import es.ucm.fdi.ici.c2223.practica5.grupo04.ghosts.GhostInput;
import es.ucm.fdi.ici.c2223.practica5.grupo04.ghosts.GhostStorageManager;
import pacman.controllers.GhostController;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class Ghosts extends GhostController {

	HashMap<GHOST,GhostCBRengine> cbrEngines;
	GhostStorageManager storageManager;

	EnumMap<GHOST, GhostInput> inputs;
	private EnumMap<GHOST, MOVE> moves;
	
	public Ghosts()
	{
		setName("Ghosts 04");
		setTeam("Grupo 04");
		
		moves = new EnumMap<GHOST, MOVE>(GHOST.class);
		inputs = new EnumMap<GHOST, GhostInput>(GHOST.class);
		

		this.storageManager = new GhostStorageManager();
		
		for (GHOST g : GHOST.values())
			cbrEngines.put(g, new GhostCBRengine(storageManager, g));
	}
	
	@Override
	public void preCompute(String opponent) {
		for (GHOST g : GHOST.values()) {
			try {
				cbrEngines.get(g).configure();
				cbrEngines.get(g).preCycle();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void postCompute() {
		try {
			for (GHOST g : GHOST.values())
			cbrEngines.get(g).postCycle();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
		MOVE move;
		for(GHOST g : GHOST.values()) {
			//Solo se activa cuando est� en una junction
			if (game.getGhostCurrentNodeIndex(g) == -1 || !game.isJunction(game.getGhostCurrentNodeIndex(g))) move = MOVE.NEUTRAL;
			else {
				try {
					inputs.put(g, new GhostInput(g, game));
					inputs.get(g).parseInput();
					storageManager.setGame(game);
					cbrEngines.get(g).cycle(inputs.get(g).getQuery());
					move = cbrEngines.get(g).getSolution();
				} catch (Exception e) {
					e.printStackTrace();
					move = MOVE.NEUTRAL;
				}
			}
			moves.put(g, move);
		}
		return moves;
	}
}
