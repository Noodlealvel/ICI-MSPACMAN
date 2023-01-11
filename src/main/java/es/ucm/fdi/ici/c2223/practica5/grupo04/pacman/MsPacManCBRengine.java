package es.ucm.fdi.ici.c2223.practica5.grupo04.pacman;

import java.io.File;
import java.util.Collection;

import es.ucm.fdi.gaia.jcolibri.cbraplications.StandardCBRApplication;
import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCaseBase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRQuery;
import es.ucm.fdi.gaia.jcolibri.exception.ExecutionException;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.RetrievalResult;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.NNConfig;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.local.Equal;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.local.Interval;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.selection.SelectCases;
import es.ucm.fdi.gaia.jcolibri.util.FileIO;
import es.ucm.fdi.ici.c2223.practica5.grupo04.CBRengine.*;
import es.ucm.fdi.ici.c2223.practica5.grupo04.ghosts.GhostResult;
import pacman.game.Constants.MOVE;

public class MsPacManCBRengine implements StandardCBRApplication {

	private String opponent;
	private MOVE action;
	private MsPacManStorageManager storageManager;

	CustomPlainTextConnector connector;
	CBRCaseBase caseBase;
	NNConfig simConfig;
	
	
	final static String TEAM = "grupo04";  //Cuidado!! poner el grupo aquí
	
	
	final static String CONNECTOR_FILE_PATH = "es//ucm//fdi//ici//c2223//practica5//"+TEAM+"//pacman//plaintextconfig.xml";
	final static String CASE_BASE_PATH = "cbrdata"+File.separator+TEAM+File.separator+"mspacman"+File.separator;

	
	public MsPacManCBRengine(MsPacManStorageManager storageManager)
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
		
		simConfig = new NNConfig();
		simConfig.setDescriptionSimFunction(new Average());
		
		simConfig.addMapping(new Attribute("BlinkyDistance",MsPacManDescription.class), new Interval(500));
		simConfig.addMapping(new Attribute("PinkyDistance",MsPacManDescription.class), new Interval(500));
		simConfig.addMapping(new Attribute("InkyDistance",MsPacManDescription.class), new Interval(500));
		simConfig.addMapping(new Attribute("SueDistance",MsPacManDescription.class), new Interval(500));
		
		simConfig.addMapping(new Attribute("PacmanDBlinky",MsPacManDescription.class), new Interval(500));
		simConfig.addMapping(new Attribute("PacmanDPinky",MsPacManDescription.class), new Interval(500));
		simConfig.addMapping(new Attribute("PacmanDInky",MsPacManDescription.class), new Interval(500));
		simConfig.addMapping(new Attribute("PacmanDSue",MsPacManDescription.class), new Interval(500));
		
		simConfig.addMapping(new Attribute("PPillUp",MsPacManDescription.class), new Interval(500));
		simConfig.addMapping(new Attribute("PPillDown",MsPacManDescription.class), new Interval(500));
		simConfig.addMapping(new Attribute("PPillRight",MsPacManDescription.class), new Interval(500));
		simConfig.addMapping(new Attribute("PPillLeft",MsPacManDescription.class), new Interval(500));
		
		simConfig.addMapping(new Attribute("PillUp",MsPacManDescription.class), new Interval(500));
		simConfig.addMapping(new Attribute("PillDown",MsPacManDescription.class), new Interval(500));
		simConfig.addMapping(new Attribute("PillRight",MsPacManDescription.class), new Interval(500));
		simConfig.addMapping(new Attribute("PillLeft",MsPacManDescription.class), new Interval(500));
		
		simConfig.addMapping(new Attribute("BlinkyTimeEdible",MsPacManDescription.class), new Interval(255));
		simConfig.addMapping(new Attribute("PinkyTimeEdible",MsPacManDescription.class), new Interval(255));
		simConfig.addMapping(new Attribute("InkyTimeEdible",MsPacManDescription.class), new Interval(255));
		simConfig.addMapping(new Attribute("SueTimeEdible",MsPacManDescription.class), new Interval(255));
		
		simConfig.addMapping(new Attribute("numPills",MsPacManDescription.class), new Interval(200));
		simConfig.addMapping(new Attribute("eatValue",MsPacManDescription.class), new Interval(1600));
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
		}
		else {
			//Compute retrieve
			Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(caseBase.getCases(), query, simConfig);
			
			Collection<RetrievalResult> cases = SelectCases.selectTopKRR(eval, 5);
			RetrievalResult top = cases.iterator().next();
			
			CBRCase mostSimilarCase = top.get_case();
			double similarity = top.getEval();
			
			MsPacManResult result = (MsPacManResult) mostSimilarCase.getResult();
			
			
			//Compute reuse
			this.action = reuse(eval);
			
			//this.action = pruebaCasos(cases, query.getDescription());
			
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
		
		//Compute revise & retain
		CBRCase newCase = createNewCase(query);
		this.storageManager.reviseAndRetain(newCase, simConfig);
		
	}

	private MOVE reuse(Collection<RetrievalResult> eval)
	{
		// This simple implementation only uses 1NN
		// Consider using kNNs with majority voting
		RetrievalResult first = SelectCases.selectTopKRR(eval, 1).iterator().next();
		CBRCase mostSimilarCase = first.get_case();
		double similarity = first.getEval();

		MsPacManResult result = (MsPacManResult) mostSimilarCase.getResult();
		MsPacManSolution solution = (MsPacManSolution) mostSimilarCase.getSolution();
		
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
		MsPacManDescription newDescription = (MsPacManDescription) query.getDescription();
		MsPacManResult newResult = new MsPacManResult();
		MsPacManSolution newSolution = new MsPacManSolution();
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
		this.storageManager.close(simConfig);
		this.caseBase.close();
	}

}
