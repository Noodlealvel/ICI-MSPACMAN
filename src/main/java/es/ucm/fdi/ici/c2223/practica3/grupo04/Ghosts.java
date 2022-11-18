package es.ucm.fdi.ici.c2223.practica3.grupo04;

import java.io.File;
import java.util.EnumMap;
import java.util.HashMap;

import es.ucm.fdi.ici.c2223.practica3.grupo04.GhostsRules.GhostsInput;
import es.ucm.fdi.ici.c2223.practica3.grupo04.GhostsRules.Actions.GhostsChaseAction;
import es.ucm.fdi.ici.c2223.practica3.grupo04.GhostsRules.Actions.GhostsDefendLastPillsAction;
import es.ucm.fdi.ici.c2223.practica3.grupo04.GhostsRules.Actions.GhostsDisperseAction;
import es.ucm.fdi.ici.c2223.practica3.grupo04.GhostsRules.Actions.GhostsFleeAction;
import es.ucm.fdi.ici.c2223.practica3.grupo04.GhostsRules.Actions.GhostsFleeFromPPillAction;
import es.ucm.fdi.ici.c2223.practica3.grupo04.GhostsRules.Actions.GhostsGoToPPillAction;
import es.ucm.fdi.ici.c2223.practica3.grupo04.GhostsRules.Actions.GhostsKillPacmanAction;
import es.ucm.fdi.ici.c2223.practica3.grupo04.GhostsRules.Actions.GhostsRegroupAction;
import es.ucm.fdi.ici.c2223.practica3.grupo04.GhostsRules.Actions.GhostsSearchForTunnelAction;
import es.ucm.fdi.ici.c2223.practica3.grupo04.GhostsRules.Actions.GhostsStopChasingAction;
import es.ucm.fdi.ici.c2223.practica3.grupo04.GhostsRules.Actions.GhostsWaitAction;
import es.ucm.fdi.ici.rules.RuleEngine;
import es.ucm.fdi.ici.rules.RulesAction;
import es.ucm.fdi.ici.rules.RulesInput;
import es.ucm.fdi.ici.rules.observers.ConsoleRuleEngineObserver;
import pacman.controllers.GhostController;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class Ghosts extends GhostController {
	
	EnumMap<GHOST,RuleEngine> ghostRuleEngines;
	private static final String RULES_PATH = "es"+File.separator+"ucm"+File.separator+"fdi"+File.separator+"ici"+File.separator+"c2223"+File.separator+"practica3"+File.separator+"grupo04"+File.separator+"GhostsRules"+File.separator;
	
	public Ghosts(){
		setName("Ghosts 04");
		setTeam("Team04");
		ghostRuleEngines = new EnumMap<GHOST,RuleEngine>(GHOST.class);
		
		HashMap<String, RulesAction> map = new HashMap<String,RulesAction>();
		
		for(GHOST ghost: GHOST.values()) {
			map.put(ghost+"chase", new GhostsChaseAction(ghost));
			map.put(ghost+"defendLastPills", new GhostsDefendLastPillsAction(ghost));
			map.put(ghost+"disperse", new GhostsDisperseAction(ghost));
			map.put(ghost+"flee", new GhostsFleeAction(ghost));
			map.put(ghost+"fleeFromPPill", new GhostsFleeFromPPillAction(ghost));
			map.put(ghost+"goToPPill", new GhostsGoToPPillAction(ghost));
			map.put(ghost+"killPacman", new GhostsKillPacmanAction(ghost));
			map.put(ghost+"regroup", new GhostsRegroupAction(ghost));
			map.put(ghost+"searchForTunnel", new GhostsSearchForTunnelAction(ghost));
			map.put(ghost+"stopChasing", new GhostsStopChasingAction(ghost));
			map.put(ghost+"wait", new GhostsWaitAction(ghost));
		}
		
		for(GHOST ghost: GHOST.values())
		{
			String rulesFile = String.format("%s%srules.clp", RULES_PATH, ghost.name().toLowerCase());
			RuleEngine engine  = new RuleEngine(ghost.name(),rulesFile, map);
			ghostRuleEngines.put(ghost, engine);
			
			//add observer to every Ghost
			//ConsoleRuleEngineObserver observer = new ConsoleRuleEngineObserver(ghost.name(), true);
			//engine.addObserver(observer);
		}
		
		//add observer only to BLINKY
		ConsoleRuleEngineObserver observer = new ConsoleRuleEngineObserver(GHOST.BLINKY.name(), true);
		ghostRuleEngines.get(GHOST.BLINKY).addObserver(observer);
	}
	
	@Override
	public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
		
		RulesInput input = new GhostsInput(game);
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
