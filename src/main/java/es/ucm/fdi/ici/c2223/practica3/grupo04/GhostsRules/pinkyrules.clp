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
	(slot euclidPacman (type SYMBOL)))
	
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
	(slot euclidPacman (type SYMBOL)))
	
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
	(slot justBehind (type SYMBOL)))

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
	(slot justBehind (type SYMBOL)))


(deftemplate MSPACMAN 
    (slot pacmanInTunnel (type SYMBOL))
    (slot noPPills (type SYMBOL)) 
    (slot eatenPPill (type SYMBOL))
    (slot pacmanNearPPill (type SYMBOL))
    (slot noPPills (type SYMBOL)))
    
(deftemplate PINKY_STATE
	(slot inDefense (type SYMBOL))
	(slot inAttack (type SYMBOL))
	(slot inAgressive (type SYMBOL)))
    
;Action Fact
(deftemplate ACTION
	(slot id) (slot info (default "")) (slot priority (type NUMBER))
	(slot flankstrategy (type SYMBOL))
)
	
;Rules

(defrule PINKYwait
	(PINKY (inLair true)) 
	=>  
	(assert 
		(ACTION (id PINKYwait) (info "PINKY espera en lair") (priority 100))
	)
)

(defrule PINKYAgressive
	(PINKY (edible false))
	(MSPACMAN (noPPills true)) 
	=>  
	(assert (PINKY_STATE (inAgressive true) (inDefense false) (inAttack false)))
)

(defrule PINKYAttacks
	(PINKY (edible false) (chaseDistance true))
	(MSPACMAN (pacmanNearPPill false)) 
	=>  
	(assert (PINKY_STATE (inAgressive false) (inDefense false) (inAttack true)))
)

(defrule PINKYdefense_Edible 
	(PINKY (edible true))
	=>
	(assert (PINKY_STATE (inAgressive false) (inDefense true) (inAttack false)))
)

(defrule PINKYdefense_PacmanNearPPill 
	(MSPACMAN (pacmanNearPPill true))
	=>
	(assert (PINKY_STATE (inAgressive false) (inDefense true) (inAttack false)))
)

(defrule PINKYchases_NoGhosts
	(PINKY (noGhostsInPath true) (justBehind false))
	(PINKY_STATE (inAttack true))
	=>
	(assert
		(ACTION (id PINKYchase) (info "PINKY persigue de manera directa al estar el camino vacío") (priority 40) (flankstrategy false))
	)
)

(defrule PINKYchases_Tunnel
	(PINKY (justBehind false))
	(PINKY_STATE (inAttack true))
	(MSPACMAN (pacmanInTunnel true))
	=>
	(assert
		(ACTION (id PINKYchase) (info "PINKY persigue de manera directa al estar en túnel") (priority 40) (flankstrategy false))
	)
)

(defrule PINKYflanks
	(PINKY (noGhostsInPath false) (justBehind false))
	(PINKY_STATE (inAttack true))
	=>
	(assert
		(ACTION (id PINKYchase) (info "PINKY flanquea") (priority 60) (flankstrategy true))
	)
)

(defrule PINKYstopsChasing
	(PINKY (justBehind true))
	(PINKY_STATE (inAttack true))
	=>
	(assert
		(ACTION (id PINKYstopChasing) (info "PINKY para de perseguir") (priority 50))
	)
)

(defrule PINKYdefendLastPills
	(PINKY (justBehind true))
	(PINKY_STATE (inAgressive true))
	=>
	(assert
		(ACTION (id PINKYdefendLastPills) (info "PINKY protege las últimas pills") (priority 55))
	)
)

(defrule PINKYkillPacman
	(PINKY (chaseDistance true))
	(PINKY_STATE (inAgressive true))
	=>
	(assert
		(ACTION (id PINKYkillPacman) (info "PINKY va a terminar con pacman") (priority 50))
	)
)

(defrule PINKYregroup
	(PINKY (chaseDistance false) (edible false))
	(MSPACMAN (pacmanNearPPill false))
	=>  
	(assert 
		(ACTION (id PINKYregroup) (info "PINKY se acerca a pacman porque no es comestible y esta lejos") (priority 80))
	)
)

(defrule PINKYflee
	(PINKY (pacmanClose true)) 
	(PINKY_STATE (inDefense true))
	=>  
	(assert 
		(ACTION (id PINKYflee) (info "PINKY huye de pacman porque esta cerca") (priority 70))
	)
)

(defrule PINKYsearchForTunnel
	(PINKY (pacmanInVecinity true) (nearTunnel true)) 
	(PINKY_STATE (inDefense true))
	=>  
	(assert 
		(ACTION (id PINKYsearchForTunnel) (info "PINKY busca tunel") (priority 30))
	)
)

(defrule PINKYfleeFromPPill_LowTime
	(PINKY (lowEdibleTime true)) 
	(PINKY_STATE (inDefense true))
	=>  
	(assert 
		(ACTION (id PINKYfleeFromPPill) (info "A PINKY le queda poco tiempo comestible y huye de powerpills cercanas") (priority 60))
	)
)

(defrule PINKYfleeFromPPill_PacmanFar
	(PINKY (pacmanClose false)) 
	(PINKY_STATE (inDefense true))
	=>  
	(assert 
		(ACTION (id PINKYfleeFromPPill) (info "PINKY esta lejos de pacman y este huye de powerpills cercanas") (priority 50))
	)
)

(defrule PINKYdisperse
	(PINKY (pacmanClose false) (ghostsClose true)) 
	(PINKY_STATE (inDefense true))
	=>  
	(assert 
		(ACTION (id PINKYdisperse) (info "PINKY se dispersa") (priority 70))
	)
)

(defrule PINKYgoToPPill
	(PINKY (lowEdibleTime true))
	(PINKY_STATE (inDefense true)) 
	=>  
	(assert 
		(ACTION (id PINKYdisperse) (info "PINKY va a interceptar a pacman en PPill") (priority 75))
	)
)

