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
	(assert (ACTION (id SearchOptimalPathTowardsEdibles) (info "EDIBLE GHOSTS CLOSE") ))
)
