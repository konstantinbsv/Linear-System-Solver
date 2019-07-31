package solver;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Stack;

class Matrix {
    private int matrixNumOfVariables;
    private int matrixNumOfEquations;
    private LinearEquation[] matrix;

    public Matrix(File inputFile) {
        try (Scanner scanner = new Scanner(inputFile)) {

            String[] firstLine = scanner.nextLine().split(" ");
            matrixNumOfVariables = Integer.parseInt(firstLine[0]); // integer  of file is number of variables
            if (firstLine.length == 2) {
                matrixNumOfEquations = Integer.parseInt(firstLine[1]); // second integer is number of variables
            } else {
                matrixNumOfEquations = matrixNumOfVariables;
            }

            if (matrixNumOfVariables < 1) {
                throw new IndexOutOfBoundsException("Invalid matrix size: " + matrixNumOfVariables);
            }

            matrix = new LinearEquation[matrixNumOfEquations];

            for (int i = 0; i < matrixNumOfEquations    ; i++) {
                matrix[i] = new LinearEquation(matrixNumOfVariables); // initialize LE
                matrix[i].fillEquationFromScanner(scanner);  // fill from file
            }

        } catch (FileNotFoundException fileNotFound) {
            System.out.println("File not found: " + inputFile);
        }
    }

    public int getMatrixNumOfVariables() {
        return matrixNumOfVariables;
    }

    public int getMatrixNumOfEquations() {
        return matrixNumOfEquations;
    }

    public LinearEquation getRow(int row) {
        if (row < 1 || row > matrixNumOfEquations) {
            throw new IndexOutOfBoundsException("Invalid row index: " + row);
        }

        return matrix[row-1];
    }

    public ComplexNumber getTerm(int row, int column) {
        if (row < 1 || row > matrixNumOfEquations) {
            throw new IndexOutOfBoundsException("Invalid row index: " + row);
        }
        if (column < 1 || column > matrixNumOfVariables) {
            throw new IndexOutOfBoundsException("Invalid column index: " + column);
        }

        return matrix[row-1].getTerm(column);
    }

    public void print() {
        for (int i = 0; i < matrixNumOfEquations; i++) {
            matrix[i].print();
            System.out.println();
        }
    }

    public void printResults(boolean verticalVectorOutput) {
        for (int row = 1; row <= matrixNumOfVariables; row++) {
            ComplexNumber result = getRow(row).getConstantTerm();
            // System.out.print(result < 0 ? "-" : " ");
            // System.out.printf("%.3f", Math.abs(result));
            System.out.println(result);
            if (verticalVectorOutput) {
                System.out.println();
            }
            else {
                System.out.print(" ");
            }
        }
    }

    public ComplexNumber getConstantTermsSum() {
        ComplexNumber constantTermsSum = new ComplexNumber(0, 0);
        for (int row = 1; row < matrixNumOfEquations; row++) {
            constantTermsSum = constantTermsSum.add(getRow(row).getConstantTerm());
        }
        return constantTermsSum;
    }

    public int findNonZeroRowInCol(int column) {
        for (int row = column; row <= matrixNumOfEquations; row++) { // look below current row
            if (!getTerm(row, column).isZero()) {
                return row;
            }
        }
        return -1; // returns -1 if not row found
    }

    public int findLeadingNonZeroRow() {
        int row;
        for (int col = 1; col <= matrixNumOfVariables; col++) {
            row = findNonZeroRowInCol(col);
            if (row != -1) {
                return row;
            }
        }
        return -1; // returns -1 if not row found
    }

    public int findLeadingNonZeroCol(){
        return findLeadingNonZeroCol(1);
    }

    public int findLeadingNonZeroCol(int fromCol) {
        for (int col = fromCol; col <= matrixNumOfVariables; col++) {
            if (-1 != findNonZeroRowInCol(col)) {
                return col;
            }
        }
        return -1; // returns -1 if not row found
    }

    public void swapRows(int rowOne, int rowTwo) {
        if (rowOne < 1 || rowOne > matrixNumOfEquations) {
            throw new IndexOutOfBoundsException("Invalid row index: " + rowOne);
        }
        if (rowTwo < 1 || rowTwo > matrixNumOfEquations) {
            throw new IndexOutOfBoundsException("Invalid column index: " + rowTwo);
        }

        rowOne--;
        rowTwo--;
        LinearEquation temp = matrix[rowOne];
        matrix[rowOne] = matrix[rowTwo];
        matrix[rowTwo] = temp;
    }
    public void swapColumns(int colOne, int colTwo) {
        for (int row = 1; row < matrixNumOfVariables; row++) {
            ComplexNumber temp = matrix[row].getTerm(colOne);
            matrix[row].setCoeff(colOne, matrix[row].getTerm(colTwo));
            matrix[row].setCoeff(colTwo, temp);
        }
    }
    public boolean isInconsistent() {
        for (int row = 0; row < matrixNumOfEquations; row++) {
            if(matrix[row].isInconsistent()) {
                return true;
            }
        }
        return false;
    }

    public int numOfFreeVariables() {
        int numOfFreeVariables = matrixNumOfVariables;
        for (LinearEquation equation: matrix) {
            if (equation.hasNonZeroCoeffs()) {
                numOfFreeVariables--;
            }
        }
        return numOfFreeVariables;
    }
}


interface Command {
    boolean execute();
    boolean undo();
}

class SwapColumns implements Command {
    Matrix matrix;

    private int colOne;
    private int colTwo;

    public SwapColumns(Matrix matrix, int colOne, int colTwo) {
        this.matrix = matrix;
        this.colOne = colOne;
        this.colTwo = colTwo;
    }

    @Override
    public boolean execute() {
        matrix.swapColumns(colOne, colTwo);
        System.out.println("Command pushed executed");

        return true;
    }

    @Override
    public boolean undo() {
        matrix.swapRows(colOne, colTwo);
        return true;
    }
}

class CommandHistory {
    private static Stack<Command> commandHistory = new Stack<>();

    public static void push(Command command) {
        commandHistory.push(command);
        System.out.println("Command pushed to stack");
    }

    public static Command pop() {
        return commandHistory.pop();
    }

    public static boolean isCommandHistoryEmpty() {
        return commandHistory.isEmpty();
    }

}