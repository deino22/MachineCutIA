package random;

import algorithms.Algorithm;
import algorithms.AlgorithmEvent;
import algorithms.Individual;
import algorithms.Problem;

import java.util.Random;

public class RandomAlgorithm<I extends Individual, P extends Problem<I>> extends Algorithm<I, P> {
    private I individual;

    public RandomAlgorithm(int maxIterations, Random rand) {
        super(maxIterations, rand);
    }

    @Override
    public I run(P problem) {
        this.t = 0;
        this.individual = problem.getNewIndividual();
        this.individual.computeFitness();
        this.globalBest = this.individual;
        this.fireIterationEnded(new AlgorithmEvent(this));

        while(this.t < this.maxIterations && !this.stopped) {
            this.individual = problem.getNewIndividual();
            this.individual.computeFitness();
            this.computeBestInRun(this.individual);
            ++this.t;
            this.fireIterationEnded(new AlgorithmEvent(this));
        }

        this.fireRunEnded(new AlgorithmEvent(this));
        return this.globalBest;
    }

    private void computeBestInRun(I individual) {
        if (individual.compareTo(this.globalBest) > 0) {
            this.globalBest = (I)individual.clone();
        }
    }
}
