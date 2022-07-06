package ga.geneticoperators.mutations;

import algorithms.IntVectorIndividual;
import algorithms.Problem;
import ga.GeneticAlgorithm;

public class MutationSwap<I extends IntVectorIndividual, P extends Problem<I>> extends Mutation<I, P> {

    public MutationSwap(double probability) {
        super(probability);
    }

    @Override
    public void mutate(I ind) {
        int index1 = GeneticAlgorithm.random.nextInt(ind.getNumGenes());
        int index2;

        do {
            index2 = GeneticAlgorithm.random.nextInt(ind.getNumGenes());
        } while (index1 == index2);

        int aux = ind.getGene(index1);
        ind.setGene(index1, ind.getGene(index2));
        ind.setGene(index2, aux);
    }

    @Override
    public String toString(){
        return "Swap Mutation";
    }
}