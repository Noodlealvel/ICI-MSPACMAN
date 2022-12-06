package es.ucm.fdi.ici.c2223.practica4.grupo04.pacman;

import java.util.HashMap;

import es.ucm.fdi.ici.fuzzy.FuzzyInput;
import pacman.game.Game;
import pacman.game.Constants.GHOST;

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
	public void parseInput() { //Inicializamos los arrays
		
		distancesNoEdible = new double[] {MAX_DISTANCE, MAX_DISTANCE, MAX_DISTANCE, MAX_DISTANCE};
		distancesEdible = new double[] {MAX_DISTANCE, MAX_DISTANCE, MAX_DISTANCE, MAX_DISTANCE};
		confidence = new double[] {MAX_CONFIDENCE, MAX_CONFIDENCE, MAX_CONFIDENCE, MAX_CONFIDENCE};
		nodesGhosts = new int[] {-1, -1, -1, -1};
		edibles = new boolean[] {false, false, false, false};
		initialGhostNode = game.getGhostInitialNodeIndex();
		
		for (GHOST ghost : GHOST.values()) {
			if (game.wasPacManEaten() || game.getCurrentLevelTime() == 0) { // Reseteamos todo si comen a Pacman o pasa de nivel
				this.nodesGhosts[ghost.ordinal()] = initialGhostNode;
				confidence[ghost.ordinal()] = MAX_CONFIDENCE;

				distancesNoEdible[ghost.ordinal()] = MAX_DISTANCE;
				distancesEdible[ghost.ordinal()] = MAX_DISTANCE;
				edibles[ghost.ordinal()] = false;
			} else if (game.wasPowerPillEaten() && confidence[ghost.ordinal()] > MAX_CONFIDENCE/2 && !edibles[ghost.ordinal()]) //Cambiamos las distancias 
			{
				distancesEdible[ghost.ordinal()] = distancesNoEdible[ghost.ordinal()];
				distancesNoEdible[ghost.ordinal()] = MAX_DISTANCE;

				edibles[ghost.ordinal()] = true;
			} else { //Si Pacman come a un ghost
				if (game.wasGhostEaten(ghost)) { //Si Pacman come a un ghost, reseteamos la pos y confidence del fantasma comido y volvemos a calcular la distancia no comestible
					nodesGhosts[ghost.ordinal()] = initialGhostNode;
					confidence[ghost.ordinal()] = MAX_CONFIDENCE / 2;

					distancesNoEdible[ghost.ordinal()] = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(),
							initialGhostNode);
					distancesEdible[ghost.ordinal()] = MAX_DISTANCE;

					edibles[ghost.ordinal()] = false;
				} else { //Si no come, calculamos la distancia al fantasma
					double distance = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(),
							game.getGhostCurrentNodeIndex(ghost));
					if (distance != 0) // Pacman ve al fantasma, confidence al máximo y guardamos pos
					{
						nodesGhosts[ghost.ordinal()] = game.getGhostCurrentNodeIndex(ghost);
						confidence[ghost.ordinal()] = MAX_CONFIDENCE;
						if (!game.isGhostEdible(ghost)) { //Si no es edible guardamos en distanciaNoEdible
							distancesNoEdible[ghost.ordinal()] = distance;
						} else {
							distancesEdible[ghost.ordinal()] = distance;
							edibles[ghost.ordinal()] = true;
						}
					} else { //No ve al fantasma, disminuye confidence
						if (confidence[ghost.ordinal()] <= MAX_CONFIDENCE / 3) { //Si la confidence es muy poca directamente la ponemos a 0
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
	
	private void fillMemory() { //Guardamos las posiciones de todos en la memoria
		if (msPacManFuzzyMemory != null) {
			msPacManFuzzyMemory.setGhostsPositions(nodesGhosts);
			msPacManFuzzyMemory.setPacManPosition(game.getPacmanCurrentNodeIndex());
		}
		else {
			msPacManFuzzyMemory = new MsPacManFuzzyMemory(nodesGhosts, game.getPacmanCurrentNodeIndex());
		}
		
	}
	
	
	
	public HashMap<String, Double> getFuzzyValues() { //Variables input fuzzy
		HashMap<String,Double> vars = new HashMap<String,Double>();
		for(GHOST g: GHOST.values()) {
			vars.put(g.name()+"_distance", distancesNoEdible[g.ordinal()]);
			vars.put(g.name()+"_edibleDistance", distancesEdible[g.ordinal()]);
			vars.put(g.name()+"_confidence", confidence[g.ordinal()]);
		}
		return vars;
	}

}
