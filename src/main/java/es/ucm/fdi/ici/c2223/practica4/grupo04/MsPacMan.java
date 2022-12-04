package es.ucm.fdi.ici.c2223.practica4.grupo04;

import java.io.File;
import java.util.HashMap;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2223.practica4.grupo04.pacman.MaxActionSelector;
import es.ucm.fdi.ici.c2223.practica4.grupo04.pacman.MsPacmanInput;
import es.ucm.fdi.ici.c2223.practica4.grupo04.pacman.actions.*;
import es.ucm.fdi.ici.fuzzy.ActionSelector;
import es.ucm.fdi.ici.fuzzy.FuzzyEngine;
import es.ucm.fdi.ici.fuzzy.observers.ConsoleFuzzyEngineObserver;
import pacman.controllers.PacmanController;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MsPacMan extends PacmanController {

	private static final String RULES_PATH = "src\\main\\java\\es\\ucm\\fdi\\ici\\c2223\\practica4\\grupo04\\pacman";
	FuzzyEngine fuzzyEngine;
	MsPacManFuzzyMemory fuzzyMemory;
	
	
	public MsPacMan()
	{
		setName("MsPacMan 04 Test");
		setTeam("MsPacMan 04 Test");

	 	Action[] actions = {new ChasePowerPillAction(), new RunAwayBlinky(), new RunAwayPinky(), new RunAwayInky(), new RunAwaySue(), new ChaseGhostBlinky(), new ChaseGhostPinky(), new ChaseGhostInky(), new ChaseGhostSue()};
		
		ActionSelector actionSelector = new MaxActionSelector(actions);
		 
		ConsoleFuzzyEngineObserver observer = new ConsoleFuzzyEngineObserver("MsPacMan","MsPacManRules");
		fuzzyEngine = new FuzzyEngine("MsPacMan", RULES_PATH+"\\pacman.fcl", "FuzzyMsPacMan", actionSelector);
		fuzzyEngine.addObserver(observer);
		
	}
	
	
	@Override
	public MOVE getMove(Game game, long timeDue) {
		MsPacmanInput input = new MsPacmanInput(game);
		input.parseInput();		
		
		HashMap<String, Double> fvars = input.getFuzzyValues();
		
		return fuzzyEngine.run(fvars,game);
	}

}
