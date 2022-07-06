package ga.geneticoperators.mutations;

import algorithms.IntVectorIndividual;
import algorithms.Problem;
import ga.GeneticAlgorithm;

public class MutationScramble<I extends IntVectorIndividual, P extends Problem<I>> extends Mutation<I, P> {

    public MutationScramble(double probability) {
        super(probability);
    }

    @Override
    public void mutate(I ind) {
        int index1 = GeneticAlgorithm.random.nextInt(ind.getNumGenes());
        int index2, index, tmp;
        int[] partOfGenome;

        do {
            index2 = GeneticAlgorithm.random.nextInt(ind.getNumGenes());
        } while (index1 == index2);

        if (index1 > index2) {
            int aux = index1;
            index1 = index2;
            index2 = aux;
        }

        partOfGenome = new int[(index2 - index1) + 1];

        System.arraycopy(ind.getGenome(), index1, partOfGenome, 0, partOfGenome.length);

        for(int i = 0; i < partOfGenome.length; i++) {
            index = GeneticAlgorithm.random.nextInt(partOfGenome.length - 1);
            tmp = partOfGenome[partOfGenome.length - 1 - i];
            partOfGenome[partOfGenome.length - 1 - i] = partOfGenome[index];
            partOfGenome[index] = tmp;
        }

        System.arraycopy(partOfGenome, 0, ind.getGenome(), index1, partOfGenome.length);
    }

    @Override
    public String toString(){
        return "Scramble Mutation";
    }
}