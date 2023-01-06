package es.ucm.fdi.ici.c2223.practica5.grupo04.ghosts;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import es.ucm.fdi.gaia.jcolibri.cbraplications.StandardCBRApplication;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCaseBase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRQuery;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;
import es.ucm.fdi.gaia.jcolibri.exception.ExecutionException;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.RetrievalResult;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.NNConfig;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.selection.SelectCases;
import es.ucm.fdi.gaia.jcolibri.util.FileIO;
import es.ucm.fdi.ici.c2223.practica5.grupo04.CBRengine.CachedLinearCaseBase;
import es.ucm.fdi.ici.c2223.practica5.grupo04.CBRengine.CustomPlainTextConnector;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class GhostCBRengine implements StandardCBRApplication {

	private String opponent;
	private MOVE action;
	private GhostStorageManager storageManager;

	CustomPlainTextConnector connector;
	CBRCaseBase caseBase;
	NNConfig simConfig;
	final static String TEAM = "grupo04";  //Cuidado!! poner el grupo aquÃ­
	
	
	final static String CONNECTOR_FILE_PATH = "es//ucm//fdi//ici//c2223//practica5//"+TEAM+"//ghosts//plaintextconfig.xml";
	final static String CASE_BASE_PATH = "cbrdata"+File.separator+TEAM+File.separator+"ghosts"+File.separator;

	
	public GhostCBRengine(GhostStorageManager storageManager, GHOST ghost)
	{
		this.storageManager = storageManager;
	}
	
	public void setOpponent(String opponent) {
		this.opponent = opponent;
	}
	
	@Override
	public void configure() throws ExecutionException {
		connector = new CustomPlainTextConnector();
		caseBase = new CachedLinearCaseBase();
		
		connector.initFromXMLfile(FileIO.findFile(CONNECTOR_FILE_PATH));
		
		//Do not use default case base path in the xml file. Instead use custom file path for each opponent.
		//Note that you can create any subfolder of files to store the case base inside your "cbrdata/grupoXX" folder.
		connector.setCaseBaseFile(CASE_BASE_PATH, opponent+".csv");
		
		this.storageManager.setCaseBase(caseBase);
		
	}

	@Override
	public CBRCaseBase preCycle() throws ExecutionException {
		caseBase.init(connector);
		return caseBase;
	}

	@Override
	public void cycle(CBRQuery query) throws ExecutionException {
		if(caseBase.getCases().isEmpty()) {
			int index = (int)Math.floor(Math.random()*4);
			if(MOVE.values()[index]==action) 
				index= (index+1)%4;
			action = MOVE.values()[index];
		}else {
			//Compute NN
			//Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(caseBase.getCases(), query, simConfig);
			Collection<RetrievalResult> eval = NNnuevo(caseBase.getCases(),query);
			
			// This simple implementation only uses 1NN
			// Consider using kNNs with majority voting
			Collection<RetrievalResult> cases = SelectCases.selectTopKRR(eval, 5);
			RetrievalResult top = cases.iterator().next();
			
			//-----
			CBRCase mostSimilarCase = top.get_case();
			double similarity = top.getEval();
			//------
	
			GhostResult result = (GhostResult) mostSimilarCase.getResult();
			
			this.action = pruebaCasos(cases, query.getDescription());
			
			if(similarity < 0.4) {
				int index = (int)Math.floor(Math.random()*4);
				if(MOVE.values()[index]==action) 
					index= (index+1)%4;
				action = MOVE.values()[index];}

			else if(result.getScore() <= 0) {	// Caso muy malo (Aún siendo el más similar)
				int index = (int)Math.floor(Math.random()*4);
				if(MOVE.values()[index]==action) 
					index= (index+1)%4;
				action = MOVE.values()[index];
			}
		}
		CBRCase newCase = createNewCase(query);
		this.storageManager.reviseAndRetain(newCase);
		
	}
	
	private MOVE pruebaCasos(Collection<RetrievalResult> cases, CaseComponent description) {
		HashMap<MOVE, Double> poll = new HashMap<MOVE, Double>();
		for (MOVE m : MOVE.values()) {
			poll.put(m, 0.0);
		}
		for (RetrievalResult r : cases) {
			CBRCase c = r.get_case();
			MOVE a = ((GhostSolution) c.getSolution()).getAction();
			poll.put(a, poll.getOrDefault(a, 0.) + ((GhostResult) c.getResult()).getScore() * Similaridad((GhostDescription)description, (GhostDescription) c.getDescription()));
		}
		MOVE fin = null; 
		Double mas = 0.0;
		for (Entry<MOVE, Double> e : poll.entrySet()) if (e.getValue() > mas) {	fin = e.getKey(); mas = e.getValue();	}	
		return fin;
	}

	// Obtener mï¿½s parecidos
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
		
		double similitud = 0.0;
		
		similitud  += Math.abs(des1.getBlinkyDistance() - des2.getBlinkyDistance()) * 0.1;
		similitud  += Math.abs(des1.getInkyDistance() - des2.getInkyDistance()) * 0.1;
		similitud  += Math.abs(des1.getPinkyDistance() - des2.getPinkyDistance()) * 0.1;
		similitud  += Math.abs(des1.getSueDistance() - des2.getSueDistance()) * 0.1;
		
		similitud  += Math.abs(des1.getPacmanDBlinky() - des2.getPacmanDBlinky()) * 0.05;
		similitud  += Math.abs(des1.getPacmanDInky() - des2.getPacmanDInky()) * 0.05;
		similitud  += Math.abs(des1.getPacmanDPinky() - des2.getPacmanDPinky()) * 0.05;
		similitud  += Math.abs(des1.getPacmanDSue() - des2.getPacmanDSue()) * 0.05;
		
		similitud += Math.abs(des1.getScore() - des2.getScore()) * 0.1;
		similitud += Math.abs(des1.getPPillDistance() - des2.getPPillDistance()) * 0.1;
		similitud += Math.abs(des1.getGhostsCloseIndex() - des2.getGhostsCloseIndex()) * 0.1;
		similitud += Math.abs(des1.getTimeEdible() - des2.getTimeEdible())* 0.1;
		return similitud;
	}

	private MOVE reuse(Collection<RetrievalResult> eval)
	{
		// This simple implementation only uses 1NN
		// Consider using kNNs with majority voting
		RetrievalResult first = SelectCases.selectTopKRR(eval, 1).iterator().next();
		CBRCase mostSimilarCase = first.get_case();
		double similarity = first.getEval();

		GhostResult result = (GhostResult) mostSimilarCase.getResult();
		GhostSolution solution = (GhostSolution) mostSimilarCase.getSolution();
		
		//Now compute a solution for the query
		
		//Here, it simply takes the action of the 1NN
		MOVE action = solution.getAction();
		
		//But if not enough similarity or bad case, choose another move randomly
		if((similarity<0.7)||(result.getScore()<100)) {
			int index = (int)Math.floor(Math.random()*4);
			if(MOVE.values()[index]==action) 
				index= (index+1)%4;
			action = MOVE.values()[index];
		}
		return action;
	}
	
	
	

	/**
	 * Creates a new case using the query as description, 
	 * storing the action into the solution and 
	 * setting the proper id number
	 */
	private CBRCase createNewCase(CBRQuery query) {
		CBRCase newCase = new CBRCase();
		GhostDescription newDescription = (GhostDescription) query.getDescription();
		GhostResult newResult = new GhostResult();
		GhostSolution newSolution = new GhostSolution();
		int newId = this.caseBase.getCases().size();
		newId+= storageManager.getPendingCases();
		newDescription.setId(newId);
		newResult.setId(newId);
		newSolution.setId(newId);
		newSolution.setAction(this.action);
		newCase.setDescription(newDescription);
		newCase.setResult(newResult);
		newCase.setSolution(newSolution);
		return newCase;
	}
	
	public MOVE getSolution() {
		return this.action;
	}

	@Override
	public void postCycle() throws ExecutionException {
		this.storageManager.close();
		this.caseBase.close();
	}

}
