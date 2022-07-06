package ga.geneticoperators.recombinations;

import algorithms.IntVectorIndividual;
import algorithms.Problem;

import java.util.Arrays;

public class RecombinationCycle<I extends IntVectorIndividual, P extends Problem<I>> extends Recombination<I, P> {
    public RecombinationCycle(double probability) {
        super(probability);
    }

    @Override
    public void recombine(I ind1, I ind2) {
        int index = 0, offSpringSize = ind1.getNumGenes();

        int[] offSpring1 = new int[offSpringSize];
        int[] offSpring2 = new int[offSpringSize];

        Arrays.fill(offSpring1, -1);
        Arrays.fill(offSpring2, -1);

        do{
            offSpring1[index] = ind1.getGene(index);
            int auxGene = ind2.getGene(index);
            offSpring2[index] = auxGene;
            index = ind1.getIndexof(auxGene);
        } while (index != 0);

        for (int i = 0; i < offSpringSize; i++){
            if(offSpring1[i] == -1 && offSpring2[i] == -1){
                offSpring1[i] = ind2.getGene(i);
                offSpring2[i] = ind1.getGene(i);
            }
        }

        System.arraycopy(offSpring1, 0, ind1.getGenome(), 0, offSpringSize);
        System.arraycopy(offSpring2, 0, ind2.getGenome(), 0, offSpringSize);
    }

    @Override
    public String toString() {
        return "Cycle";
    }
}