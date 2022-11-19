package es.ucm.fdi.ici.c2223.practica3.grupo04;

import java.util.HashMap;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.rules.*;
import es.ucm.fdi.ici.c2223.practica3.grupo04.PacManRules.MsPacmanInput;
import es.ucm.fdi.ici.c2223.practica3.grupo04.PacManRulesActions.*;
import pacman.controllers.PacmanController;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MsPacMan extends PacmanController {

	private static final String RULES_PATH = "src\\main\\java\\es\\ucm\\fdi\\ici\\c2223\\practica3\\grupo04\\";
	HashMap<String, RulesAction> map;
	private RuleEngine pacmanEngine;
	
	public MsPacMan() {
		
		setName("MsPacMan 04 TEST");
		setTeam("Team04 TEST");
		
		map = new HashMap<String,RulesAction>();
		
		map.put("BeginMap", new BeginMapAction());
		map.put("ChasePowerPill", new ChasePowerPillAction());
		map.put("EatLastPills", new EatLastPillsAction());
		map.put("EdiblesAndTogether", new EdiblesAndTogetherAction());
		map.put("EdiblesButApart", new EdiblesButApartAction());
		map.put("Flee", new FleeAction());
		map.put("SearchBetterZone", new SearchBetterZoneAction());
		map.put("SearchOptimalPath", new SearchOptimalPathAction());
		map.put("SearchOptimalPathTowardsEdibles", new SearchOptimalPathTowardsEdiblesAction());
		map.put("SearchPathWithoutGhosts", new SearchPathWithoutGhostsAction());
		map.put("SearchZoneWithPPAndNoGhosts", new SearchZoneWithPPAndNoGhostsAction());
		
		String rulesFile = String.format("%s/%srules.clp", RULES_PATH, "pacman");
		pacmanEngine  = new RuleEngine("pacman", rulesFile, map);
	}
	
	
	@Override
	public MOVE getMove(Game game, long timeDue) {
		
		// Process input
		Input input = new MsPacmanInput(game);

		// load facts
		input.parseInput();

		// reset the rule engine
		pacmanEngine.reset();
		pacmanEngine.assertFacts(((MsPacmanInput) input).getFacts());

		MOVE move = pacmanEngine.run(game);

		return move;

	}

}
