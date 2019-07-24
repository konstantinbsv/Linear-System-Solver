package solver;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Scanner;

class LinearEquation {
    private double[] equation;
    private int equationLength;

    public LinearEquation(int numOfCoeffs) {
        equationLength = numOfCoeffs + 1; // +1 for constant
        equation = new double[equationLength];
    }

    public double[] getEquation() {
        return equation;
    }

    public double getTerm(int i) {
        if (i < 1 || i > equationLength) {
            throw new IndexOutOfBoundsException("Invalid term index: " + i);
        }

        return equation[i-1];
    }

    public void multiplyRowAndSave(double x) {
        if (x != 1) {
            for (int i = 0; i < equationLength; i++) {
                equation[i] *= x;
            }
        }
    }

    public double[] multiplyRowTemp(double x) {
        double[] result = new double[equationLength];
        for (int i = 0; i < equationLength; i++) {
            result[i] = equation[i] *x;
        }

        return result;
    }

    public void addToRow(LinearEquation secondEquation) {
        if (secondEquation.equationLength != equationLength) {
            throw new InputMismatchException("Linear equations are of unequal size");
        }

        for (int i = 0; i < equationLength; i++) {
            equation[i] += secondEquation.getTerm(i);
        }
    }

    public void addToRow(double[] secondEquation) {
        if (secondEquation.length != equationLength) {
            throw new InputMismatchException("Linear equations are of unequal size");
        }

        for (int i = 0; i < equationLength; i++) {
            equation[i] += secondEquation[i];
        }
    }

    public void subFromRow(LinearEquation secondEquation) {
        for (int i = 0; i < equationLength; i++) {
            equation[i] -= secondEquation.getTerm(i);
        }
    }

    public void fillEquationFromScanner(Scanner scanner) {
        int i = 0;
        while (scanner.hasNext() && i < equationLength) {
            equation[i] = scanner.nextDouble();
            i++;
        }
    }

    public void print() {
        for (int i = 0; i < equationLength; i++) {
            System.out.print(equation[i] < 0 ? "-":" "); // neater formatting
            System.out.print(Math.abs(equation[i]) + " ");
        }
    }



}
