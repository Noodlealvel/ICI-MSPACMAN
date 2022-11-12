package es.ucm.fdi.ici.c2223.practica3.grupo04;

import java.util.EnumMap;

import es.ucm.fdi.ici.rules.RulesInput;
import es.ucm.fdi.ici.c2223.practica3.grupo04.GhostsRules.GameMemory;
import es.ucm.fdi.ici.c2223.practica3.grupo04.GhostsRules.GhostsInput;
import es.ucm.fdi.ici.rules.RuleEngine;
import pacman.controllers.GhostController;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class Ghosts extends GhostController {
	
	EnumMap<GHOST,RuleEngine> ghostRuleEngines;

	public Ghosts(){
		setName("Ghosts 04");
		setTeam("Team04");
	}
	
	@Override
	public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
		
		GameMemory mem = new GameMemory();
		RulesInput input = new GhostsInput(game, mem);
		input.parseInput();
		for(RuleEngine engine: ghostRuleEngines.values()) {
			engine.reset();
			engine.assertFacts(input.getFacts());
		}
		
		EnumMap<GHOST,MOVE> result = new EnumMap<GHOST,MOVE>(GHOST.class);		
		for(GHOST ghost: GHOST.values())
		{
			RuleEngine engine = ghostRuleEngines.get(ghost);
			MOVE move = engine.run(game);
			result.put(ghost, move);
		}
		
		return result;
	}

}
