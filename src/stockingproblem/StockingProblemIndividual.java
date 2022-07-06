package stockingproblem;

import algorithms.IntVectorIndividual;
import org.jfree.util.ArrayUtilities;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StockingProblemIndividual extends IntVectorIndividual<StockingProblem, StockingProblemIndividual> {
    private int cuts;
    private int materialArea;
    private int whiteSpaces;
    private int[][] material;

    public StockingProblemIndividual(StockingProblem problem, int size) {
        super(problem, size);

        ArrayList<Integer> itemsIds = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            itemsIds.add(problem.getItems().get(i).getId());
        }

        Collections.shuffle(itemsIds);
        this.genome = itemsIds.stream().mapToInt(i -> i).toArray();
    }

    public StockingProblemIndividual(StockingProblemIndividual original) {
        super(original);
        this.cuts = original.cuts;
        this.materialArea = original.materialArea;
        this.whiteSpaces = original.whiteSpaces;
        this.material = original.material;
    }

    @Override
    public double computeFitness() {
        this.fitness = 0.0D;
        this.material = new int[problem.getMaterialHeight()][10];

        for (int gene : this.genome) {
            Item item = this.problem.getItems().get(gene);

            this.material = addItemToMaterial(this.material, item);
        }

        //TODO: After recalculate the necessary space to add to material this code will not be necessary any more
        //------------------
        int materialLength = getMaterialLength(this.material);
        if (materialLength != 0){
            this.material = resizeMaterial(this.material, materialLength);
        }
        //------------------

        this.cuts = countCuts(this.material);
        this.materialArea = (this.material.length * this.material[0].length);
        this.whiteSpaces = countWhiteSpaces(this.material);

        return this.fitness = this.cuts + this.materialArea + this.whiteSpaces;
    }

    //Get the material length without unfilled columns
    private int getMaterialLength(int[][] material) {
        int originalSize = material[0].length, newSize = originalSize;

        for (int col = originalSize - 1; col >= 0; col--) {
            for (int row = (material.length - 1); row >= 0; row--) {
                if (material[row][col] != 0) {
                    return 0;
                }
            }

            newSize--;
        }

        return newSize;
    }

    private int countWhiteSpaces(int[][] material) {
        int whiteSpaces = 0;

        for (int[] rows : material) {
            for (int cell : rows) {
                if (cell == 0) {
                    whiteSpaces++;
                }
            }
        }

        return whiteSpaces;
    }

    private int countCuts(int[][] material) {
        int cuts = 0;
        for (int row = 0; row < material.length; row++) {
            for (int col = 0; col < material[row].length; col++){
                if((row + 1) < material.length && material[row][col] != material[row + 1][col]) {
                    cuts++;
                }

                if((col + 1) < material[row].length && material[row][col] != material[row][col + 1]){
                    cuts++;
                }
            }

            if(material[row][material[row].length - 1] != 0) {
                cuts++;
            }
        }

        return cuts;
    }

    private int[][] addItemToMaterial(int[][] material, Item item) {
        Point position;

        do {
            position = getPositionToAdd(material, item);

            if(position == null) {
                //TODO
                //Recalculate the necessary size to add to material
                material = resizeMaterial(material, material[0].length + item.getColumns());
            }
            else {
                break;
            }

        } while (true);

        for (int x = 0; x < item.getLines() - 1; x++) {
            for (int y = 0; y < item.getColumns() - 1; y++) {
                material[position.x + x][position.y + y] = (int)item.getRepresentation();
            }
        }

        return material;
    }

    private int[][] resizeMaterial(int[][] material, int size) {
        int[][] auxMaterial = new int[material.length][size];

        //TODO: After recalculate the necessary space to add to material this code will not be necessary any more
        //------------------
        int length = 0;

        if (material[0].length > size){
            length = material[0].length - size;
        }
        //------------------
        for (int i = 0; i < material.length; i++) {
            System.arraycopy(material[i], 0, auxMaterial[i], 0, material[i].length - length);
        }

        return auxMaterial;
    }

    private Point getPositionToAdd(int[][] material, Item item) {
        for (int row = 0; row < material.length; row++) {
            for (int col = 0 ; col < material[row].length; col++) {
                if(checkValidPlacement(item, material, row, col)) {
                    return new Point(row, col);
                }
            }
        }

        return null;
    }

    private boolean checkValidPlacement(Item item, int[][] material, int lineIndex, int columnIndex) {
        int[][] itemMatrix = item.getMatrix();
        for (int i = 0; i < itemMatrix.length; i++) {
            for (int j = 0; j < itemMatrix[i].length; j++) {
                if (itemMatrix[i][j] != 0) {
                    if ((lineIndex + i) >= material.length
                            || (columnIndex + j) >= material[0].length
                            || material[lineIndex + i][columnIndex + j] != 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Fitness: ");
        sb.append(fitness);
        sb.append("\nCuts: ");
        sb.append(this.cuts);
        sb.append("\nWhite Spaces: ");
        sb.append(this.whiteSpaces);
        sb.append("\nMaterial size (area): ");
        sb.append(this.materialArea);
        sb.append("\nSolution:\n");
        for (int[] row : this.material) {
            sb.append("[");
            for (int elem : row)
            {
                if(elem != 0)
                {
                    sb.append((char)elem);
                }
                else
                {
                    sb.append('-');
                }
            }
            sb.append("]\n");
        }
        return sb.toString();
    }

    /**
     * @param i
     * @return 1 if this object is BETTER than i, -1 if it is WORST than I and
     * 0, otherwise.
     */
    @Override
    public int compareTo(StockingProblemIndividual i) {
        return (this.fitness == i.getFitness()) ? 0 : (this.fitness < i.getFitness()) ? 1 : -1;
    }

    @Override
    public StockingProblemIndividual clone() {
        return new StockingProblemIndividual(this);
    }
}