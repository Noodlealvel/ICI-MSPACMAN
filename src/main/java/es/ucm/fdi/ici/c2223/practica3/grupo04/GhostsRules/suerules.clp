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
    
(deftemplate SUE_STATE
	(slot inDefense (type SYMBOL))
	(slot inAttack (type SYMBOL))
	(slot inAgressive (type SYMBOL)))
    
;Action Fact
(deftemplate ACTION
	(slot id) (slot info (default "")) (slot priority (type NUMBER))
	(slot flankstrategy (type SYMBOL))
)
	
;Rules

(defrule SUEwait
	(SUE (inLair true)) 
	=>  
	(assert 
		(ACTION (id SUEwait) (info "SUE espera en lair") (priority 100))
	)
)

(defrule SUEAgressive
	(SUE (edible false))
	(MSPACMAN (noPPills true)) 
	=>  
	(assert (SUE_STATE (inAgressive true) (inDefense false) (inAttack false)))
)

(defrule SUEAttacks
	(SUE (edible false) (chaseDistance true))
	(MSPACMAN (pacmanNearPPill false)) 
	=>  
	(assert (SUE_STATE (inAgressive false) (inDefense false) (inAttack true)))
)

(defrule SUEdefense_Edible 
	(SUE (edible true))
	=>
	(assert (SUE_STATE (inAgressive false) (inDefense true) (inAttack false)))
)

(defrule SUEdefense_PacmanNearPPill 
	(MSPACMAN (pacmanNearPPill true))
	=>
	(assert (SUE_STATE (inAgressive false) (inDefense true) (inAttack false)))
)

(defrule SUEchases_NoGhosts
	(SUE (noGhostsInPath true))
	(SUE_STATE (inAttack true))
	=>
	(assert
		(ACTION (id SUEchase) (info "SUE persigue de manera directa al estar el camino vacío") (priority 40) (flankstrategy false))
	)
)

(defrule SUEchases_Tunnel
	(SUE_STATE (inAttack true))
	(MSPACMAN (pacmanInTunnel true))
	=>
	(assert
		(ACTION (id SUEchase) (info "SUE persigue de manera directa al estar en túnel") (priority 40) (flankstrategy false))
	)
)

(defrule SUEflanks
	(SUE (noGhostsInPath false) (justBehind false))
	(SUE_STATE (inAttack true))
	=>
	(assert
		(ACTION (id SUEchase) (info "SUE flanquea") (priority 60) (flankstrategy true))
	)
)

(defrule SUEstopsChasing
	(SUE (justBehind true))
	(SUE_STATE (inAttack true))
	=>
	(assert
		(ACTION (id SUEstopChasing) (info "SUE para de perseguir") (priority 50))
	)
)

(defrule SUEdefendLastPills
	(SUE (justBehind true))
	(SUE_STATE (inAgressive true))
	=>
	(assert
		(ACTION (id SUEdefendLastPills) (info "SUE protege las últimas pills") (priority 55))
	)
)

(defrule SUEkillPacman
	(SUE (chaseDistance true))
	(SUE_STATE (inAgressive true))
	=>
	(assert
		(ACTION (id SUEkillPacman) (info "SUE va a terminar con pacman") (priority 50))
	)
)

(defrule SUEregroup
	(SUE (chaseDistance false) (edible false))
	(SUE_STATE (inAgressive false)) 
	(MSPACMAN (pacmanNearPPill true))
	=>  
	(assert 
		(ACTION (id SUEregroup) (info "SUE se acerca a pacman porque no es comestible y esta lejos") (priority 80))
	)
)

(defrule SUEflee
	(SUE (pacmanClose true)) 
	(SUE_STATE (inDefense true))
	=>  
	(assert 
		(ACTION (id SUEflee) (info "SUE huye de pacman porque esta cerca") (priority 70))
	)
)

(defrule SUEsearchForTunnel
	(SUE (pacmanInVecinity true) (nearTunnel true)) 
	(SUE_STATE (inDefense true))
	=>  
	(assert 
		(ACTION (id SUEsearchForTunnel) (info "SUE busca tunel") (priority 30))
	)
)

(defrule SUEfleeFromPPill_LowTime
	(SUE (lowEdibleTime true)) 
	(SUE_STATE (inDefense true))
	=>  
	(assert 
		(ACTION (id SUEfleeFromPPill) (info "A SUE le queda poco tiempo comestible y huye de powerpills cercanas") (priority 60))
	)
)

(defrule SUEfleeFromPPill_PacmanFar
	(SUE (pacmanClose false)) 
	(SUE_STATE (inDefense true))
	=>  
	(assert 
		(ACTION (id SUEfleeFromPPill) (info "SUE esta lejos de pacman y este huye de powerpills cercanas") (priority 50))
	)
)

(defrule SUEdisperse
	(SUE (pacmanClose false) (ghostsClose true)) 
	(SUE_STATE (inDefense true))
	=>  
	(assert 
		(ACTION (id SUEdisperse) (info "SUE se dispersa") (priority 70))
	)
)

(defrule SUEgoToPPill
	(SUE (lowEdibleTime true))
	(SUE_STATE (inDefense true)) 
	=>  
	(assert 
		(ACTION (id SUEdisperse) (info "SUE va a interceptar a pacman en PPill") (priority 75))
	)
)

