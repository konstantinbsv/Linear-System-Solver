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
        return matrix[row];
    }

    public void print() {
        for (int i = 0; i < matrixSize; i++) {
            matrix[i].print();
            System.out.println();
        }
    }
}
