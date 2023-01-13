package es.ucm.fdi.ici.c2223.practica5.grupo04.ghosts;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCaseBase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRQuery;
import es.ucm.fdi.gaia.jcolibri.method.retain.StoreCasesMethod;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.RetrievalResult;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.selection.SelectCases;
import pacman.game.Constants.GHOST;
import pacman.game.Game;

public class GhostStorageManager {

	Game game;
	CBRCaseBase caseBase;
	Vector<CBRCase> buffer;

	private final static int TIME_WINDOW = 5;
	
	public GhostStorageManager()
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
	
	public void reviseAndRetain(CBRCase newCase)
	{			
		this.buffer.add(newCase);
		
		//Buffer not full yet.
		if(this.buffer.size()<TIME_WINDOW)
			return;
		
		
		CBRCase bCase = this.buffer.remove(0);
		reviseCase(bCase);
		retainCase(bCase);
		
	}
	
	private void reviseCase(CBRCase bCase) {
		GhostDescription description = (GhostDescription)bCase.getDescription();
		int oldScore = description.getScore();
		int currentScore = game.getScore();
		int valuePacman = currentScore - oldScore;
		GHOST ghost = description.getGhost();
		int ghostProgress = 0;
		switch(ghost){
		case BLINKY:
			ghostProgress += description.getTimeEdible() - game.getGhostEdibleTime(ghost);
			if (game.getGhostEdibleTime(ghost) == 0) {
				ghostProgress += game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghost), game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(ghost)) - description.getBlinkyDistance();
			}
			else {
				ghostProgress += game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghost), game.getPacmanLastMoveMade()) - description.getPacmanDBlinky();
			}
			if (game.getNumberOfActivePowerPills() != 0)
				ghostProgress += game.getEuclideanDistance(GhostsUtils.NearestActivePPill(game, ghost),game.getGhostCurrentNodeIndex(ghost)) - description.getPPillDistance();
			else {
				ghostProgress += 500;
			}
			break;
		case INKY:
			ghostProgress += description.getTimeEdible() - game.getGhostEdibleTime(ghost);
			if (game.getGhostEdibleTime(ghost) == 0) {
				ghostProgress += game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghost), game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(ghost)) - description.getInkyDistance();
			}
			else {
				ghostProgress += game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghost), game.getPacmanLastMoveMade()) - description.getPacmanDInky();
			}
			if (game.getNumberOfActivePowerPills() != 0)
				ghostProgress += game.getEuclideanDistance(GhostsUtils.NearestActivePPill(game, ghost),game.getGhostCurrentNodeIndex(ghost)) - description.getPPillDistance();
			else {
				ghostProgress += 500;
			}
			break;
		case PINKY:
			ghostProgress += description.getTimeEdible() - game.getGhostEdibleTime(ghost);
			if (game.getGhostEdibleTime(ghost) == 0) {
				ghostProgress += game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghost), game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(ghost)) - description.getPinkyDistance();
			}
			else {
				ghostProgress += game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghost), game.getPacmanLastMoveMade()) - description.getPacmanDPinky();
			}
			if (game.getNumberOfActivePowerPills() != 0)
				ghostProgress += game.getEuclideanDistance(GhostsUtils.NearestActivePPill(game, ghost),game.getGhostCurrentNodeIndex(ghost)) - description.getPPillDistance();
			else {
				ghostProgress += 500;
			}
			break;
		case SUE:
			ghostProgress += description.getTimeEdible() - game.getGhostEdibleTime(ghost);
			if (game.getGhostEdibleTime(ghost) == 0) {
				ghostProgress += game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghost), game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(ghost)) - description.getSueDistance();
			}
			else {
				ghostProgress += game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghost), game.getPacmanLastMoveMade()) - description.getPacmanDSue();
			}
			if (game.getNumberOfActivePowerPills() != 0)
				ghostProgress += game.getEuclideanDistance(GhostsUtils.NearestActivePPill(game, ghost),game.getGhostCurrentNodeIndex(ghost)) - description.getPPillDistance();
			else {
				ghostProgress += 500;
			}
			break;
		}
		int resultValue = ghostProgress - valuePacman;
		GhostResult result = (GhostResult)bCase.getResult();
		result.setScore(resultValue);	
	}
	
	private void retainCase(CBRCase bCase)
	{
		//Store the old case right now into the case base
		//Alternatively we could store all them when game finishes in close() method
		
		
		//here you should also check if the case must be stored into persistence (too similar to existing ones, etc.)
		Collection<RetrievalResult> eval = NNnuevo(caseBase.getCases(),bCase);

		// This simple implementation only uses 1NN
		// Consider using kNNs with majority voting
		Collection<RetrievalResult> cases = SelectCases.selectTopKRR(eval, 5);
		if (!cases.isEmpty()) {
			RetrievalResult [] top = cases.toArray(new RetrievalResult[cases.size()]);
			CBRCase fifthMostSimilarCase = top[top.length-1].get_case();
			//System.out.println(Similaridad((GhostDescription)fifthMostSimilarCase.getDescription(), (GhostDescription)bCase.getDescription()));
			if (Similaridad((GhostDescription)fifthMostSimilarCase.getDescription(), (GhostDescription)bCase.getDescription()) <= 0.95) {
				StoreCasesMethod.storeCase(this.caseBase, bCase);
			}
		}
		else {
			StoreCasesMethod.storeCase(this.caseBase, bCase);
		}
	}

	private Collection<RetrievalResult> NNnuevo(Collection<CBRCase> casos, CBRQuery query) {
			List<RetrievalResult> res = casos.parallelStream()
					.map(c -> new RetrievalResult(c, Similaridad((GhostDescription)query.getDescription(), (GhostDescription) c.getDescription())))
			        .collect(Collectors.toList());
			
			res.sort(RetrievalResult::compareTo);
			return res;
		}
		

	private Double Similaridad(GhostDescription des1, GhostDescription des2) {
		if (des1.getGhost() != des2.getGhost()) return 0.0;
		if(des1.getLevel() != des1.getLevel()) return 0.0;
		if((des1.getTimeEdible() > 0 && des2.getTimeEdible() == 0) || (des1.getTimeEdible() == 0 && des2.getTimeEdible() > 0)) return 0.0;
		if(des1.getNoPPills() != des1.getNoPPills()) return 0.0;
		
		double maxDiferencia = 0.0;
		double similitud = 0.0;
		
		similitud  += Math.abs(des1.getBlinkyDistance() - des2.getBlinkyDistance()) * 0.1;
		similitud  += Math.abs(des1.getInkyDistance() - des2.getInkyDistance()) * 0.1;
		similitud  += Math.abs(des1.getPinkyDistance() - des2.getPinkyDistance()) * 0.1;
		similitud  += Math.abs(des1.getSueDistance() - des2.getSueDistance()) * 0.1;
		
		maxDiferencia += (0.4 * 500);
		
		similitud  += Math.abs(des1.getPacmanDBlinky() - des2.getPacmanDBlinky()) * 0.05;
		similitud  += Math.abs(des1.getPacmanDInky() - des2.getPacmanDInky()) * 0.05;
		similitud  += Math.abs(des1.getPacmanDPinky() - des2.getPacmanDPinky()) * 0.05;
		similitud  += Math.abs(des1.getPacmanDSue() - des2.getPacmanDSue()) * 0.05;
		
		maxDiferencia += (0.2 * 500);
		
		similitud += Math.abs(des1.getScore() - des2.getScore()) * 0.1;
		maxDiferencia += (0.1 * 1500);
		similitud += Math.abs(des1.getPPillDistance() - des2.getPPillDistance()) * 0.1;
		maxDiferencia += (0.1 * 500);
		similitud += Math.abs(des1.getGhostsCloseIndex() - des2.getGhostsCloseIndex()) * 0.1;
		maxDiferencia += (0.1 * 500);
		similitud += Math.abs(des1.getTimeEdible() - des2.getTimeEdible())* 0.1;
		maxDiferencia += (0.1 * 500);
		return 1-(similitud/maxDiferencia);
	}

	public void close() {
		for(CBRCase oldCase: this.buffer)
		{
			reviseCase(oldCase);
			retainCase(oldCase);
		}
		this.buffer.removeAllElements();
	}

	public int getPendingCases() {
		return this.buffer.size();
	}
}
