package es.ucm.fdi.ici.c2223.practica3.grupo04;

import java.util.EnumMap;

import pacman.controllers.GhostController;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class Ghosts extends GhostController {

	@Override
	public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
		setName("Ghosts 04");
		setTeam("Team04");
		return null;
	}

}
