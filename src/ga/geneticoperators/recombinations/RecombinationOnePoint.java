package ga.geneticoperators.recombinations;

import algorithms.IntVectorIndividual;
import algorithms.Problem;
import ga.GeneticAlgorithm;

import java.util.ArrayList;
import java.util.Arrays;

public class RecombinationOnePoint<I extends IntVectorIndividual, P extends Problem<I>> extends Recombination<I, P>  {
    public RecombinationOnePoint(double probability) {
        super(probability);
    }

    @Override
    public void recombine(I ind1, I ind2) {
        int crossoverPoint = GeneticAlgorithm.random.nextInt(ind1.getNumGenes());
        int offSpringSize = ind1.getNumGenes();

        ArrayList<Integer> offSpring1 = new ArrayList<>();
        ArrayList<Integer> offSpring2 = new ArrayList<>();

        for (int i = 0; i <= crossoverPoint; i++){
            offSpring1.add(ind1.getGene(i));
            offSpring2.add(ind2.getGene(i));
        }

        for (int i = 0; i < offSpringSize; i++) {
            int gene = ind2.getGene(i);
            if(!offSpring1.contains(gene)){
                offSpring1.add(gene);
            }

            gene = ind1.getGene(i);
            if(!offSpring2.contains(gene)){
                offSpring2.add(gene);
            }
        }

        int[] auxOffSpring1 = offSpring1.stream().mapToInt(i -> i).toArray();
        int[] auxOffSpring2 = offSpring2.stream().mapToInt(i -> i).toArray();

        System.arraycopy(auxOffSpring1, 0, ind1.getGenome(), 0, offSpringSize);
        System.arraycopy(auxOffSpring2, 0, ind2.getGenome(), 0, offSpringSize);
    }

    @Override
    public String toString() {
        return "One-Point";
    }
}
