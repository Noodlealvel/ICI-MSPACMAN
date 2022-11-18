;Facts asserted
(deftemplate BLINKY
	(slot edible (type SYMBOL))
	(slot eaten (type SYMBOL))
	(slot inLair (type SYMBOL))
	(slot lowEdibleTime (type SYMBOL))
	(slot pacmanInVecinity (type SYMBOL))
	(slot nearTunnel (type SYMBOL))
	(slot pacmanClose (type SYMBOL))
	(slot ghostsClose (type SYMBOL))
	(slot noGhostsInPath (type SYMBOL))
	(slot chaseDistance (type SYMBOL))
	(slot justBehind (type SYMBOL))
	(slot euclidPacman (type SYMBOL))
	(slot distanceToPacman (type NUMBER)))
	
(deftemplate INKY
	(slot edible (type SYMBOL))
	(slot eaten (type SYMBOL))
	(slot inLair (type SYMBOL))
	(slot lowEdibleTime (type SYMBOL))
	(slot pacmanInVecinity (type SYMBOL))
	(slot nearTunnel (type SYMBOL))
	(slot pacmanClose (type SYMBOL))
	(slot ghostsClose (type SYMBOL))
	(slot noGhostsInPath (type SYMBOL))
	(slot chaseDistance (type SYMBOL))
	(slot justBehind (type SYMBOL))
	(slot euclidPacman (type SYMBOL))
	(slot distanceToPacman (type NUMBER)))
	
(deftemplate PINKY
	(slot edible (type SYMBOL))
	(slot eaten (type SYMBOL))
	(slot inLair (type SYMBOL))
	(slot lowEdibleTime (type SYMBOL))
	(slot pacmanInVecinity (type SYMBOL))
	(slot nearTunnel (type SYMBOL))
	(slot pacmanClose (type SYMBOL))
	(slot ghostsClose (type SYMBOL))
	(slot noGhostsInPath (type SYMBOL))
	(slot chaseDistance (type SYMBOL))
	(slot justBehind (type SYMBOL))
	(slot euclidPacman (type SYMBOL))
	(slot distanceToPacman (type NUMBER)))

(deftemplate SUE
	(slot edible (type SYMBOL))
	(slot eaten (type SYMBOL))
	(slot inLair (type SYMBOL))
	(slot lowEdibleTime (type SYMBOL))
	(slot pacmanInVecinity (type SYMBOL))
	(slot nearTunnel (type SYMBOL))
	(slot pacmanClose (type SYMBOL))
	(slot ghostsClose (type SYMBOL))
	(slot noGhostsInPath (type SYMBOL))
	(slot chaseDistance (type SYMBOL))
	(slot justBehind (type SYMBOL))
	(slot euclidPacman (type SYMBOL))
	(slot distanceToPacman (type NUMBER)))

(deftemplate MSPACMAN 
    (slot pacmanInTunnel (type SYMBOL))
    (slot noPPills (type SYMBOL)) 
    (slot eatenPPill (type SYMBOL))
    (slot pacmanNearPPill (type SYMBOL))
    (slot noPPills (type SYMBOL)))
    
(deftemplate INKY_STATE
	(slot inDefense (type SYMBOL))
	(slot inAttack (type SYMBOL))
	(slot inAgressive (type SYMBOL)))
    
;Action Fact
(deftemplate ACTION
	(slot id) (slot info (default "")) (slot priority (type NUMBER))
	(slot flankstrategy (type SYMBOL))
)
	
;Rules

(defrule INKYwait
	(INKY (inLair true)) 
	=>  
	(assert 
		(ACTION (id INKYwait) (info "INKY espera en lair") (priority 100))
	)
)

(defrule INKYAgressive
	(INKY (edible false))
	(MSPACMAN (noPPills true)) 
	=>  
	(assert (INKY_STATE (inAgressive true) (inDefense false) (inAttack false)))
)

(defrule INKYAttacks
	(INKY (edible false) (chaseDistance true))
	(MSPACMAN (pacmanNearPPill false)) 
	=>  
	(assert (INKY_STATE (inAgressive false) (inDefense false) (inAttack true)))
)

(defrule INKYdefense_Edible 
	(INKY (edible true))
	=>
	(assert (INKY_STATE (inAgressive false) (inDefense true) (inAttack false)))
)

(defrule INKYdefense_PacmanNearPPill 
	(MSPACMAN (pacmanNearPPill true))
	=>
	(assert (INKY_STATE (inAgressive false) (inDefense true) (inAttack false)))
)

(defrule INKYchases_NoGhosts
	(INKY (noGhostsInPath true))
	(INKY_STATE (inAttack true))
	=>
	(assert
		(ACTION (id INKYchase) (info "INKY persigue de manera directa al estar el camino vacío") (priority 40) (flankstrategy false))
	)
)

(defrule INKYchases_Tunnel
	(INKY_STATE (inAttack true))
	(MSPACMAN (pacmanInTunnel true))
	=>
	(assert
		(ACTION (id INKYchase) (info "INKY persigue de manera directa al estar en túnel") (priority 40) (flankstrategy false))
	)
)

(defrule INKYflanks
	(INKY (noGhostsInPath false) (justBehind false))
	(INKY_STATE (inAttack true))
	=>
	(assert
		(ACTION (id INKYchase) (info "INKY flanquea") (priority 60) (flankstrategy true))
	)
)

(defrule INKYstopsChasing
	(INKY (justBehind true))
	(INKY_STATE (inAttack true))
	=>
	(assert
		(ACTION (id INKYstopChasing) (info "INKY para de perseguir") (priority 50))
	)
)

(defrule INKYdefendLastPills
	(INKY (justBehind true))
	(INKY_STATE (inAgressive true))
	=>
	(assert
		(ACTION (id INKYdefendLastPills) (info "INKY protege las últimas pills") (priority 55))
	)
)

(defrule INKYkillPacman
	(INKY (chaseDistance true))
	(INKY_STATE (inAgressive true))
	=>
	(assert
		(ACTION (id INKYkillPacman) (info "INKY va a terminar con pacman") (priority 50))
	)
)

(defrule INKYregroup
	(INKY (chaseDistance false) (edible false))
	(INKY_STATE (inAgressive false)) 
	(MSPACMAN (pacmanNearPPill true))
	=>  
	(assert 
		(ACTION (id INKYregroup) (info "INKY se acerca a pacman porque no es comestible y esta lejos") (priority 80))
	)
)

(defrule INKYflee
	(INKY (pacmanClose true)) 
	(INKY_STATE (inDefense true))
	=>  
	(assert 
		(ACTION (id INKYflee) (info "INKY huye de pacman porque esta cerca") (priority 70))
	)
)

(defrule INKYsearchForTunnel
	(INKY (pacmanInVecinity true) (nearTunnel true)) 
	(INKY_STATE (inDefense true))
	=>  
	(assert 
		(ACTION (id INKYsearchForTunnel) (info "INKY busca tunel") (priority 30))
	)
)

(defrule INKYfleeFromPPill_LowTime
	(INKY (lowEdibleTime true)) 
	(INKY_STATE (inDefense true))
	=>  
	(assert 
		(ACTION (id INKYfleeFromPPill) (info "A INKY le queda poco tiempo comestible y huye de powerpills cercanas") (priority 60))
	)
)

(defrule INKYfleeFromPPill_PacmanFar
	(INKY (pacmanClose false)) 
	(INKY_STATE (inDefense true))
	=>  
	(assert 
		(ACTION (id INKYfleeFromPPill) (info "INKY esta lejos de pacman y este huye de powerpills cercanas") (priority 50))
	)
)

(defrule INKYdisperse
	(INKY (pacmanClose false) (ghostsClose true)) 
	(INKY_STATE (inDefense true))
	=>  
	(assert 
		(ACTION (id INKYdisperse) (info "INKY se dispersa") (priority 70))
	)
)

(defrule INKYgoToPPill
	(INKY (lowEdibleTime true))
	(INKY_STATE (inDefense true)) 
	=>  
	(assert 
		(ACTION (id INKYdisperse) (info "INKY va a interceptar a pacman en PPill") (priority 75))
	)
)

