package es.ucm.fdi.ici.c2223.practica1.grupo04;

import es.ucm.fdi.ici.PacManParallelEvaluator;
import es.ucm.fdi.ici.Scores;

public class Evaluate {

	public static void main(String[] args) {
		PacManParallelEvaluator evaluator = new PacManParallelEvaluator();
		Scores scores = evaluator.evaluate();
		scores.printScoreAndRanking();

	}

}
