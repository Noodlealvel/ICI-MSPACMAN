package es.ucm.fdi.ici.c2223.practica4.grupo04.pacman;

import java.util.EnumMap;
import java.util.HashMap;

import es.ucm.fdi.ici.fuzzy.FuzzyInput;
import pacman.game.Game;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class MsPacmanInput extends FuzzyInput {

	private double[] distance = {50,50,50,50};
	private double[] confidence = {100,100,100,100};
	private EnumMap <GHOST, EnumMap<MOVE, Integer>> movements;
	
	
	
	public MsPacmanInput(Game game) {
		super(game);
	}
	
	@Override
	public void parseInput() {
		movements = new EnumMap<GHOST, EnumMap<MOVE, Integer>>(GHOST.class);
		for (GHOST g : GHOST.values()) movements.put(g, new EnumMap<MOVE, Integer>(MOVE.class));
		
	}
	
	
	public HashMap<String, Double> getFuzzyValues() { 
		HashMap<String,Double> vars = new HashMap<String,Double>();
		for(GHOST g: GHOST.values()) {
			vars.put(g.name()+"distance",   distance[g.ordinal()]);
			vars.put(g.name()+"confidence", confidence[g.ordinal()]);
		}
		return vars;
	}

}
