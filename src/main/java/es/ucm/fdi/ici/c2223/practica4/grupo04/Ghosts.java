package es.ucm.fdi.ici.c2223.practica4.grupo04;

import java.io.File;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2223.practica4.grupo04.actions.GhostsChaseAction;
import es.ucm.fdi.ici.c2223.practica4.grupo04.actions.GhostsDisperseAction;
import es.ucm.fdi.ici.c2223.practica4.grupo04.actions.GhostsFlankAction;
import es.ucm.fdi.ici.c2223.practica4.grupo04.actions.GhostsFleeAction;
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
		
		fuzzyMemory = new GhostsFuzzyMemory();
		
		List<Action> actionList = new ArrayList<Action>();
		
		for(GHOST ghost : GHOST.values()) {
			actionList.add(new GhostsChaseAction(ghost, fuzzyMemory));
			actionList.add(new GhostsDisperseAction(ghost));
			actionList.add(new GhostsFlankAction(ghost, fuzzyMemory));
			actionList.add(new GhostsFleeAction(ghost, fuzzyMemory));
		}
		
		Action[] actions = (Action[]) actionList.toArray();
		ActionSelector actionSelector = new MaxActionSelector(actions);
		
		for(GHOST ghost : GHOST.values()) {	
			ConsoleFuzzyEngineObserver observer = new ConsoleFuzzyEngineObserver(ghost.toString(),ghost.toString()+"Rules");
			fuzzyEngine = new FuzzyEngine(ghost.toString(),RULES_PATH+"ghosts.fcl",ghost.toString()+"fuzzyGhosts",actionSelector);
			fuzzyEngine.addObserver(observer);
			engineMap.put(ghost, fuzzyEngine);
		}
	}

	@Override
	public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
		EnumMap<GHOST, MOVE> map = new EnumMap<GHOST,MOVE>(GHOST.class);
		GhostsInput input = new GhostsInput(game, fuzzyMemory.lastPacmanPosition());
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
