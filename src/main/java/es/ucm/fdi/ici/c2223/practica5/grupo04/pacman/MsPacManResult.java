package es.ucm.fdi.ici.c2223.practica5.grupo04.pacman;

import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;

public class MsPacManResult implements CaseComponent {

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
		return new Attribute("id", MsPacManResult.class);
	}
	
	@Override
	public String toString() {
		return "MsPacManResult [id=" + id + ", score=" + score + "]";
	} 
	
	

}
