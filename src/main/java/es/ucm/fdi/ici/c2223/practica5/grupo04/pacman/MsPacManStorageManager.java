package es.ucm.fdi.ici.c2223.practica5.grupo04.pacman;

import java.util.Collection;
import java.util.Vector;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCaseBase;
import es.ucm.fdi.gaia.jcolibri.method.retain.StoreCasesMethod;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.RetrievalResult;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.NNConfig;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.selection.SelectCases;
import es.ucm.fdi.ici.c2223.practica5.grupo04.ghosts.GhostDescription;
import pacman.game.Game;

public class MsPacManStorageManager {

	Game game;
	CBRCaseBase caseBase;
	Vector<CBRCase> buffer;

	private final static int TIME_WINDOW = 3;
	
	public MsPacManStorageManager()
	{
		this.buffer = new Vector<CBRCase>();
	}
	
	public void setGame(Game game) {
		this.game = game;
	}
	
	public void setCaseBase(CBRCaseBase caseBase)
	{
		this.caseBase = caseBase;
	}
	
	public void reviseAndRetain(CBRCase newCase, NNConfig simConfig)
	{			
		this.buffer.add(newCase);
		
		//Buffer not full yet.
		if(this.buffer.size()<TIME_WINDOW)
			return;
		
		
		CBRCase bCase = this.buffer.remove(0);
		reviseCase(bCase);
		retainCase(bCase, simConfig);
		
	}
	
	private void reviseCase(CBRCase bCase) {
		MsPacManDescription description = (MsPacManDescription)bCase.getDescription();
		int oldScore = description.getScore();
		int currentScore = game.getScore();
		int resultValue = currentScore - oldScore;
		
		MsPacManResult result = (MsPacManResult)bCase.getResult();
		result.setScore(resultValue);	
	}
	
	private void retainCase(CBRCase bCase, NNConfig simConfig)
	{
		//Store the old case right now into the case base
		//Alternatively we could store all them when game finishes in close() method
		
		//here you should also check if the case must be stored into persistence (too similar to existing ones, etc.)
		Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(caseBase.getCases(), bCase, simConfig);
		
		// This simple implementation only uses 1NN
		// Consider using kNNs with majority voting
		Collection<RetrievalResult> cases = SelectCases.selectTopKRR(eval, 5);
		if (!cases.isEmpty()) {
			RetrievalResult [] top = cases.toArray(new RetrievalResult[cases.size()]);
			//System.out.println(top[top.length-1].getEval());
			if (top[top.length-1].getEval() <= 0.95) {
				StoreCasesMethod.storeCase(this.caseBase, bCase);
			}
		}
		else {
			StoreCasesMethod.storeCase(this.caseBase, bCase);
		}
	}

	public void close(NNConfig simConfig) {
		for(CBRCase oldCase: this.buffer)
		{
			reviseCase(oldCase);
			retainCase(oldCase, simConfig);
		}
		this.buffer.removeAllElements();
	}

	public int getPendingCases() {
		return this.buffer.size();
	}
}
