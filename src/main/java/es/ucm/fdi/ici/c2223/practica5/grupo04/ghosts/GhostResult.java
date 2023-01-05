package es.ucm.fdi.ici.c2223.practica5.grupo04.ghosts;

import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;

public class GhostResult implements CaseComponent {

	Integer id;
	Integer score;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	@Override
	public Attribute getIdAttribute() {
		return new Attribute("id", GhostResult.class);
	}
	
	@Override
	public String toString() {
		return "GhostsResult [id=" + id + ", score=" + score + "]";
	} 
	
	

}
