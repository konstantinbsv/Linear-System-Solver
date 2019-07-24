package solver;

import java.io.File;
import java.io.FileNotFoundException;
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

    public void multiply(double x) {
        for (int i = 0; i < equationLength; i++) {
            equation[i] *= x;
        }
    }
    public void addRow(LinearEquation secondEquation) {
        for (int i = 0; i < equationLength; i++) {
            equation[i] += secondEquation.getTerm(i);
        }
    }

    public void subRow(LinearEquation secondEquation) {
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
            System.out.print(equation[i] + " ");
        }
    }



}
