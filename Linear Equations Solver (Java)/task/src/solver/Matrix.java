package solver;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class Matrix {
    private int matrixSize;
    private LinearEquation[] matrix;

    public Matrix(File inputFile) {
        try (Scanner scanner = new Scanner(inputFile)) {
            matrixSize = scanner.nextInt(); // first line of file is matrix size
            if (matrixSize < 1) {
                throw new IndexOutOfBoundsException("Invalid matrix size: " + matrixSize);
            }

            matrix = new LinearEquation[matrixSize];

            for (int i = 0; i < matrixSize; i++) {
                matrix[i] = new LinearEquation(matrixSize); // initialize LE
                matrix[i].fillEquationFromScanner(scanner);  // fill from file
            }

        } catch (FileNotFoundException fileNotFound) {
            System.out.println("File not found: " + inputFile);
        }
    }

    public int getMatrixSize() {
        return matrixSize;
    }

    public LinearEquation getRow(int row) {
        if (row < 1 || row > matrixSize) {
            throw new IndexOutOfBoundsException("Invalid row index: " + row);
        }

        return matrix[row-1];
    }

    public double getTerm(int row, int column) {
        if (row < 1 || row > matrixSize) {
            throw new IndexOutOfBoundsException("Invalid row index: " + row);
        }
        if (column < 1 || column > matrixSize) {
            throw new IndexOutOfBoundsException("Invalid column index: " + column);
        }

        return matrix[row-1].getTerm(column);
    }

    public void print() {
        for (int i = 0; i < matrixSize; i++) {
            matrix[i].print();
            System.out.println();
        }
    }

    public void printConstantTerms(boolean verticalVectorOutput) {
        for (int row = 1; row <= matrixSize; row++) {
            System.out.print(this.getRow(row).getConstantTerm());
            if (verticalVectorOutput) {
                System.out.println();
            }
            else {
                System.out.print(" ");
            }
        }
    }
}
