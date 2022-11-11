;FACTS ASSERTED BY GAME INPUT

(deftemplate MSPACMAN 
    (slot pacmanPos (type NUMBER)) 
    (slot PPeaten (type SYMBOL))
    (slot multiplePPsInZone (type SYMBOL))
    (slot ghostClose (type SYMBOL))
    (slot edibleGhostClose (type SYMBOL))
    (slot noPillsNear (type SYMBOL))
    (slot dontChase (type SYMBOL))
    (slot multipleGhostsClose (type SYMBOL)) 
    (slot lessGhostsClose (type SYMBOL))
    (slot PPClose (type SYMBOL))
    (slot ghostsFlanking (type SYMBOL))
    (slot levelChange (type SYMBOL))
    (slot PPBlocked (type SYMBOL))
    (slot noPPsleft (type SYMBOL))
    (slot fewPillsleft (type SYMBOL))
    (slot edibleGhostsTogether (type SYMBOL))
    (slot lessPPsInZone (type SYMBOL))
)
    
;DEFINITION OF THE ACTION FACT
(deftemplate ACTION
	(slot id) (slot info (default "")) ) 

;RULES: VAMOS MIRANDO LAS TRANSICIONES QUE TENIAMOS Y info es el toString de la
;Transicion y lo que seria "(MSPACMAN (dontChase true))" es la variable de los slots de arriba que se devuelve en el evaluate (se llaman igual). Para el defrule
;le pongo el nombre de la transicion. id es el nombre de la accion que desencadena
;esa transicion. en el primer ejemplo si dontChase es true se pasa de ataque a standard, por lo que el id es SearchOptimalPath, la accion que debe hacer ahora

;Cuando la transicion es entre macroestados CREO que hay que poner la accion que en la practica pasada era la que poniamos como "cfsm1.ready(searchOptimalPath);"
en el MsPacMan.java

;te dejo estos dos de ejemplo:

(defrule AttackToStandard
	(MSPACMAN (dontChase true))
	=> 
	(assert (ACTION (id SearchOptimalPath) (info "PACMAN TRANSITIONS TO STANDARD") ))
)

(defrule EdibleGhostsClose
	(MSPACMAN (edibleGhostClose true))
	=> 
	(assert (ACTION (id SearchOptimalPathTowardsEdibles) (info "EDIBLE GHOST CLOSE") ))
)

(defrule EdibleGhostsApart
    (MSPACMAN (edibleGhostsTogether false))
    =>
    (assert (ACTION (id ediblesButApart) (info "EDIBLE GHOSTS APART") ))
)

(defrule EdibleGhostsTogether
	(MSPACMAN (edibleGhostsTogether true))
	=> 
	(assert (ACTION (id ediblesAndTogether) (info "EDIBLE GHOSTS TOGETHER") ))
)

(defrule FewPillsAndNoPPsLeft
	(MSPACMAN (fewPillsleft true))
    (and
        MSPACMAN (NoPPsleft true)))
	=> 
	(assert (ACTION (id eatLastPills) (info "FEW PILLS AND NO PPS LEFT") ))
)

(defrule FewPillsInZone
	(MSPACMAN (lessPPsInZone true))
	=> 
	(assert (ACTION (id searchBetterZone) (info "FEW PPS IN ZONE") ))
)

(defrule GhostClose
	(MSPACMAN (ghostsClose true))
	=> 
	(assert (ACTION (id flee) (info "GHOST CLOSE") ))
)

(defrule GhostsFlanking
	(MSPACMAN (ghostsFlanking true))
	=> 
	(assert (ACTION (id searchPathWithoutGhosts) (info "GHOSTS ARE FLANKING") ))
)

(defrule GhostsTooFar
	(MSPACMAN (ghostClose false))
	=> 
	(assert (ACTION (id searchOptimalPath) (info "GHOSTS ARE OUTSIDE DANGER RADIUS") ))
)

(defrule LessGhostsClose
	(MSPACMAN (lessGhostsClose true))
	=> 
	(assert (ACTION (id flee) (info "FEW PPS IN ZONE") ))
)

(defrule LevelChange
	(MSPACMAN (levelChange true))
	=> 
	(assert (ACTION (id beginMap) (info "LEVEL CHANGED") ))
)

(defrule MultiplePPsInZone
	(MSPACMAN (multiplePPsInZone true))
	=> 
	(assert (ACTION (id searchOptimalPath) (info "MULTIPLE PPS IN ZONE") ))
)

(defrule NoPillsNearPacman
	(MSPACMAN (noPillsNear true))
	=> 
	(assert (ACTION (id searchOptimalPath) (info "NO PILLS NEXT TO PACMAN") ))
)

(defrule PowerPillBlocked
	(MSPACMAN (PPBlocked true))
	=> 
	(assert (ACTION (id flee) (info "POWER PILL IS BLOCKED") ))
)

(defrule PowerPillEaten
	(MSPACMAN (PPeaten true))
	=> 
	(assert (ACTION (id searchOptimalPathTowardsEdibles) (info "POWER PILL EATEN") ))
)

(defrule SeveralGhostsCloseAndPP
	(MSPACMAN (multipleGhostsClose true))
    (and
        MSPACMAN (PPClose true)))
	=> 
	(assert (ACTION (id chasePowerPill) (info "SEVERAL GHOSTS CLOSE AND NEAR PP") ))
)

(defrule SeveralGhostsCloseNoPP
	(MSPACMAN (multipleGhostsClose true))
    (and
        MSPACMAN (PPClose false)))
	=> 
	(assert (ACTION (id searchZoneWithPPAndNoGhosts) (info "SEVERAL GHOSTS CLOSE AND FAR FROM PP") ))
)

(defrule StandardToAttackTransition
	(MSPACMAN (PPeaten true))
    (or
        MSPACMAN (EdibleGhostClose true)))
	=> 
	(assert (ACTION (id searchOptimalPathTowardsEdibles) (info "PACMAN TRANSITIONS TO ATTACK") ))
)





