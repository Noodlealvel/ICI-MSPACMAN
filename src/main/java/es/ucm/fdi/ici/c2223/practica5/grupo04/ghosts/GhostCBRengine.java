package es.ucm.fdi.ici.c2223.practica5.grupo04.ghosts;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import es.ucm.fdi.gaia.jcolibri.cbraplications.StandardCBRApplication;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCaseBase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRQuery;
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
	private GHOST ghost;
	
	
	final static String TEAM = "grupo04";  //Cuidado!! poner el grupo aqu√≠
	
	
	final static String CONNECTOR_FILE_PATH = "es/ucm/fdi/ici/practica5/"+TEAM+"/ghosts/plaintextconfig.xml";
	final static String CASE_BASE_PATH = "cbrdata"+File.separator+TEAM+File.separator+"ghosts"+File.separator;

	
	public GhostCBRengine(GhostStorageManager storageManager, GHOST ghost)
	{
		this.storageManager = storageManager;
		this.ghost = ghost;
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
		connector.setCaseBaseFile(CASE_BASE_PATH, opponent+ghost.toString()+".csv");
		
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
			this.action = MOVE.NEUTRAL;
		}
		else {
			//Compute retrieve
			Collection<RetrievalResult> eval = NNnuevo(caseBase.getCases(), query);
			//Compute reuse
			this.action = reuse(eval);
		}
		
		//Compute revise & retain
		CBRCase newCase = createNewCase(query);
		this.storageManager.reviseAndRetain(newCase);
		
	}
	
	// Obtener m·s parecidos
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
		
		similitud  += Math.abs(des1.getBlinkyDist() - des2.getBlinkyDist()) * 0.1;
		similitud  += Math.abs(des1.getInkyDist() - des2.getInkyDist()) * 0.1;
		similitud  += Math.abs(des1.getPinkyDist() - des2.getPinkyDist()) * 0.1;
		similitud  += Math.abs(des1.getSueDist() - des2.getSueDist()) * 0.1;
		
		similitud  += Math.abs(des1.getPacBlinkyDist() - des2.getPacBlinkyDist()) * 0.05;
		similitud  += Math.abs(des1.getPacInkyDist() - des2.getPacInkyDist()) * 0.05;
		similitud  += Math.abs(des1.getPacPinkyDist() - des2.getPacPinkyDist()) * 0.05;
		similitud  += Math.abs(des1.getPacSueDist() - des2.getPacSueDist()) * 0.05;
		
		similitud += Math.abs(des1.getScore() - des2.getScore()) * 0.1;
		similitud += Math.abs(des1.getNearestPPillDist() - des2.getNearestPPillDist()) * 0.1;
		similitud += Math.abs(des1.getCloseIndex() - des2.getCloseIndex()) * 0.1;
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
