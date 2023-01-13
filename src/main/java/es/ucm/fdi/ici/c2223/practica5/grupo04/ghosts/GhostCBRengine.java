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
	final static String TEAM = "grupo04";  //Cuidado!! poner el grupo aquí
	
	
	final static String CONNECTOR_FILE_PATH = "es/ucm/fdi/ici/c2223/practica5/"+TEAM+"/ghosts/plaintextconfig.xml";
	final static String CASE_BASE_PATH = "data"+File.separator+TEAM+File.separator+"ghosts"+File.separator;

	
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
			
			this.action = reusarCasos(cases, query.getDescription());
			
			if(similarity < 0.4) {
				int index = (int)Math.floor(Math.random()*4);
				if(MOVE.values()[index]==action) 
					index= (index+1)%4;
				action = MOVE.values()[index];}

			else if(result.getScore() <= 0) {	// Caso muy malo (A�n siendo el m�s similar)
				int index = (int)Math.floor(Math.random()*4);
				if(MOVE.values()[index]==action) 
					index= (index+1)%4;
				action = MOVE.values()[index];
			}
		}
		CBRCase newCase = createNewCase(query);
		this.storageManager.reviseAndRetain(newCase);
		
	}
	
	private MOVE reusarCasos(Collection<RetrievalResult> cases, CaseComponent description) {
		HashMap<MOVE, Double> encuesta = new HashMap<MOVE, Double>();
		for (MOVE m : MOVE.values()) {
			encuesta.put(m, 0.0);
		}
		for (RetrievalResult r : cases) {
			MOVE movimiento = ((GhostSolution) r.get_case().getSolution()).getAction();
			//El "voto" de cada uno vale su similaridad al nuesto x la score de cada uno.
			encuesta.put(movimiento, encuesta.get(movimiento) + ((GhostResult) r.get_case().getResult()).getScore() 
					* Similaridad((GhostDescription)description, (GhostDescription) r.get_case().getDescription()));
		}
		//Buscar movimiento m�s votado
		MOVE fin = null; 
		Double mas = 0.0;
		for (Entry<MOVE, Double> par : encuesta.entrySet()) 
			if (par.getValue() > mas) {	
				fin = par.getKey(); mas = par.getValue();	
			}	
		return fin;
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
		maxDiferencia += (0.1 * 1000);
		similitud += Math.abs(des1.getPPillDistance() - des2.getPPillDistance()) * 0.1;
		maxDiferencia += (0.1 * 500);
		similitud += Math.abs(des1.getGhostsCloseIndex() - des2.getGhostsCloseIndex()) * 0.1;
		maxDiferencia += (0.1 * 500);
		similitud += Math.abs(des1.getTimeEdible() - des2.getTimeEdible())* 0.1;
		maxDiferencia += (0.1 * 500);
		return 1-(similitud/maxDiferencia);
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
