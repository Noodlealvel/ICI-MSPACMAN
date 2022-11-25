package es.ucm.fdi.ici.c2223.practica4.grupo04;

import java.io.File;
import java.util.EnumMap;
import java.util.HashMap;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2223.practica4.grupo04.actions.MaxActionSelector;
import es.ucm.fdi.ici.fuzzy.ActionSelector;
import es.ucm.fdi.ici.fuzzy.FuzzyEngine;
import es.ucm.fdi.ici.fuzzy.observers.ConsoleFuzzyEngineObserver;
import pacman.controllers.GhostController;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class Ghosts extends GhostController {

	private static final String RULES_PATH = "es"+File.separator+"ucm"+File.separator+"fdi"+File.separator+"ici"+File.separator+"c2223"+File.separator+"practica4"+File.separator+"grupo04"+File.separator;
	private FuzzyEngine fuzzyEngine;
	private GhostsFuzzyMemory fuzzyMemory;
	private HashMap<GHOST, FuzzyEngine> engineMap;

	public Ghosts() {
		setName("Ghosts 04");
		setTeam("Team04");
		
		Action[] actions = {};

		ActionSelector actionSelector = new MaxActionSelector(actions);
		
		for(GHOST ghost : GHOST.values()) {	
			ConsoleFuzzyEngineObserver observer = new ConsoleFuzzyEngineObserver(ghost.toString(),ghost.toString()+"Rules");
			fuzzyEngine = new FuzzyEngine(ghost.toString(),RULES_PATH+"ghosts.fcl",ghost.toString()+"fuzzyGhosts",actionSelector);
			fuzzyEngine.addObserver(observer);
			engineMap.put(ghost, fuzzyEngine);
		}
		
		fuzzyMemory = new GhostsFuzzyMemory();
	}

	@Override
	public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
		EnumMap<GHOST, MOVE> map = new EnumMap<GHOST,MOVE>(GHOST.class);
		GhostsInput input = new GhostsInput(game);
		input.parseInput();
		fuzzyMemory.getInput(input);
		
		HashMap<String, Double> fvars = input.getFuzzyValues();
		fvars.putAll(fuzzyMemory.getFuzzyValues());
		for (GHOST ghost : GHOST.values()) {
			map.put(ghost, engineMap.get(ghost).run(fvars,game));
		}
		return map;
	}

}
