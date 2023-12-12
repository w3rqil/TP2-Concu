import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class InvariantsManager {
    private List<Set<Integer>> invariants;
    private List<Integer> transitionsProgress;
    public InvariantsManager() {
        this.invariants = new ArrayList<>(8);
        this.transitionsProgress = new ArrayList<>(Collections.nCopies(8,0));  //Cuanto va cumpliendo el progreso de cada invariante
        // Estos son los 8 invariantes de transicion
        invariants.add(new HashSet<>(Arrays.asList(1,3,5,7,9,11,12,13)));
        invariants.add(new HashSet<>(Arrays.asList(1,3,5,7,8,10,12,13)));
        invariants.add(new HashSet<>(Arrays.asList(1,3,4,6,9,11,12,13)));
        invariants.add(new HashSet<>(Arrays.asList(1,3,4,6,8,10,12,13)));
        invariants.add(new HashSet<>(Arrays.asList(0,2,5,7,9,11,12,13)));
        invariants.add(new HashSet<>(Arrays.asList(0,2,5,7,8,10,12,13)));
        invariants.add(new HashSet<>(Arrays.asList(0,2,4,6,9,11,12,13)));
        invariants.add(new HashSet<>(Arrays.asList(0,2,4,6,8,10,12,13)));
    }
    
    public int countTransition(int transition) {
        for(int i = 0; i < invariants.size(); i++) {
            Set<Integer> requiredTransitions = this.invariants.get(i);
            int progress = transitionsProgress.get(i);
            if(requiredTransitions.contains(transition)) {
                if(transition == (progress+1)) {
                    transitionsProgress.set(i,(progress+1));
                    if(transitionsProgress.get(i) == requiredTransitions.size()) {
                        /*
                        Se completa un invariante de transicion, entonces reiniciamos el progreso 
                        y devolvemos 1, para sumar al contador de la condicion de corte.
                        */
                        transitionsProgress.set(i, 0); 
                        return 1;
                    }
                }
            }
        }
        return 0; // No se completa un invariante de transicion
    }
}
