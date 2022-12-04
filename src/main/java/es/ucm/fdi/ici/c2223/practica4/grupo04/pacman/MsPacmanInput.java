package es.ucm.fdi.ici.c2223.practica4.grupo04.pacman;

import java.util.EnumMap;
import java.util.HashMap;

import es.ucm.fdi.ici.c2223.practica2.grupo04.GhostsFSM.GameMemory;
import es.ucm.fdi.ici.c2223.practica4.grupo04.MsPacManFuzzyMemory;
import es.ucm.fdi.ici.fuzzy.FuzzyInput;
import pacman.game.Game;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class MsPacmanInput extends FuzzyInput {

	private int initialGhostNode;
	private double[] distancesNoEdible;
	private double[] distancesEdible;
	private double[] confidence;
	private int[] nodesGhosts;
	private boolean[] edibles;
	
	private final static int MAX_DISTANCE=200;
	private final static int MAX_CONFIDENCE = 100;
	
	private MsPacManFuzzyMemory msPacManFuzzyMemory;
	public MsPacmanInput(Game game) {
		super(game);
	}
	
	@Override
	public void parseInput() {
		
		distancesNoEdible = new double[] {MAX_DISTANCE, MAX_DISTANCE, MAX_DISTANCE, MAX_DISTANCE};
		distancesEdible = new double[] {MAX_DISTANCE, MAX_DISTANCE, MAX_DISTANCE, MAX_DISTANCE};
		confidence = new double[] {MAX_CONFIDENCE, MAX_CONFIDENCE, MAX_CONFIDENCE, MAX_CONFIDENCE};
		nodesGhosts = new int[] {-1, -1, -1, -1};
		edibles = new boolean[] {false, false, false, false};
		initialGhostNode = game.getGhostInitialNodeIndex();

		for (GHOST ghost : GHOST.values()) {
			if (game.wasPacManEaten() || game.getCurrentLevelTime() == 0) {
				this.nodesGhosts[ghost.ordinal()] = initialGhostNode;
				confidence[ghost.ordinal()] = MAX_CONFIDENCE;

				distancesNoEdible[ghost.ordinal()] = MAX_DISTANCE;
				distancesEdible[ghost.ordinal()] = MAX_DISTANCE;
				edibles[ghost.ordinal()] = false;
			} else if (game.wasPowerPillEaten() && confidence[ghost.ordinal()] < MAX_CONFIDENCE
					&& !edibles[ghost.ordinal()]) // CAMBIAR CONFIDENCE
			{
				distancesEdible[ghost.ordinal()] = distancesNoEdible[ghost.ordinal()];
				distancesNoEdible[ghost.ordinal()] = MAX_DISTANCE;

				edibles[ghost.ordinal()] = true;
				confidence[ghost.ordinal()] = 0;
			} else {
				if (game.wasGhostEaten(ghost)) {
					nodesGhosts[ghost.ordinal()] = initialGhostNode;
					confidence[ghost.ordinal()] = MAX_CONFIDENCE / 2;

					distancesNoEdible[ghost.ordinal()] = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(),
							initialGhostNode);
					distancesEdible[ghost.ordinal()] = MAX_DISTANCE;

					edibles[ghost.ordinal()] = false;
				} else {
					double distance = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(),
							game.getGhostCurrentNodeIndex(ghost));
					if (distance != 0) // Estamos viendo al fantasma
					{
						nodesGhosts[ghost.ordinal()] = game.getGhostCurrentNodeIndex(ghost);
						confidence[ghost.ordinal()] = MAX_CONFIDENCE;
						if (game.isGhostEdible(ghost)) {
							distancesEdible[ghost.ordinal()] = distance;
							edibles[ghost.ordinal()] = true;
						} else {
							distancesNoEdible[ghost.ordinal()] = distance;
						}
					} else {
						if (confidence[ghost.ordinal()] <= MAX_CONFIDENCE / 3) {
							nodesGhosts[ghost.ordinal()] = -1;
							confidence[ghost.ordinal()] = 0;

							distancesNoEdible[ghost.ordinal()] = MAX_DISTANCE;
							distancesEdible[ghost.ordinal()] = MAX_DISTANCE;

							edibles[ghost.ordinal()] = false;
						} else
							confidence[ghost.ordinal()]--;
					}

				}

			}

		}
		
		fillMemory();
	}
	
	private void fillMemory() {
		if (msPacManFuzzyMemory != null) {
			msPacManFuzzyMemory.setGhostsPositions(nodesGhosts);
			msPacManFuzzyMemory.setPacManPosition(game.getPacmanCurrentNodeIndex());
		}
		else {
			msPacManFuzzyMemory = new MsPacManFuzzyMemory(nodesGhosts, game.getPacmanCurrentNodeIndex());
		}
		
	}
	
	
	
	public HashMap<String, Double> getFuzzyValues() { 
		HashMap<String,Double> vars = new HashMap<String,Double>();
		for(GHOST g: GHOST.values()) {
			vars.put(g.name()+"_distance", distancesNoEdible[g.ordinal()]);
			vars.put(g.name()+"_edibleDistance", distancesEdible[g.ordinal()]);
			vars.put(g.name()+"_confidence", confidence[g.ordinal()]);
		}
		return vars;
	}

}
