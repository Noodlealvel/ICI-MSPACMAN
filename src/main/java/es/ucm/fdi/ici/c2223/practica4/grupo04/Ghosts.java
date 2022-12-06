package es.ucm.fdi.ici.c2223.practica4.grupo04;

import java.io.File;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2223.practica4.grupo04.actions.GhostsChaseAction;
import es.ucm.fdi.ici.c2223.practica4.grupo04.actions.GhostsDisperseAction;
import es.ucm.fdi.ici.c2223.practica4.grupo04.actions.GhostsFlankAction;
import es.ucm.fdi.ici.c2223.practica4.grupo04.actions.GhostsFleeAction;
import es.ucm.fdi.ici.c2223.practica4.grupo04.actions.GhostsFleeFromPPillAction;
import es.ucm.fdi.ici.c2223.practica4.grupo04.actions.GhostsGoToPPillAction;
import es.ucm.fdi.ici.c2223.practica4.grupo04.actions.GhostsSearchForTunnelAction;
import es.ucm.fdi.ici.c2223.practica4.grupo04.actions.GhostsWaitAction;
import es.ucm.fdi.ici.c2223.practica4.grupo04.actions.MaxActionSelector;
import es.ucm.fdi.ici.fuzzy.ActionSelector;
import es.ucm.fdi.ici.fuzzy.FuzzyEngine;
import es.ucm.fdi.ici.fuzzy.observers.ConsoleFuzzyEngineObserver;
import pacman.controllers.GhostController;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class Ghosts extends GhostController {

	private static final String RULES_PATH = "src\\main\\java\\es\\ucm\\fdi\\ici\\c2223\\practica4\\grupo04";
	private static final int ACTION_NUMBER = 8;
	private FuzzyEngine fuzzyEngine;
	private GhostsFuzzyMemory fuzzyMemory;
	private HashMap<GHOST, FuzzyEngine> engineMap;

	public Ghosts() {
		setName("Ghosts 04");
		setTeam("Team04");
		
		fuzzyMemory = new GhostsFuzzyMemory();
		
		Action[] actions = new Action[4*ACTION_NUMBER] ;
		
		for(GHOST ghost : GHOST.values()){
			int j = 0;
			actions[ghost.ordinal()+j++] = new GhostsChaseAction(ghost , fuzzyMemory);
			actions[ghost.ordinal()+j++] = new GhostsDisperseAction(ghost);
			actions[ghost.ordinal()+j++] = new GhostsFlankAction(ghost, fuzzyMemory);
			actions[ghost.ordinal()+j++] = new GhostsFleeAction(ghost, fuzzyMemory);
			actions[ghost.ordinal()+j++] = new GhostsFleeFromPPillAction(ghost);
			actions[ghost.ordinal()+j++] = new GhostsGoToPPillAction(ghost);
			actions[ghost.ordinal()+j++] = new GhostsSearchForTunnelAction(ghost);
			actions[ghost.ordinal()+j++] = new GhostsWaitAction(ghost);
		}
		ActionSelector actionSelector = new MaxActionSelector(actions);
		
		engineMap = new HashMap<GHOST, FuzzyEngine>();
		
		for(GHOST ghost : GHOST.values()) {	
			ConsoleFuzzyEngineObserver observer = new ConsoleFuzzyEngineObserver(ghost.toString(),ghost.toString()+"Rules");
			fuzzyEngine = new FuzzyEngine(ghost.toString(), RULES_PATH+"\\ghosts.fcl",ghost.toString()+"fuzzyGhosts",actionSelector);
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
			for(Entry<String, Double> set :fvars.entrySet()) {
				if (!set.getKey().contains(ghost.name()));
					fvars.remove(set.getKey());
			}
			map.put(ghost, engineMap.get(ghost).run(fvars,game));
		}
		return map;
	}

}
