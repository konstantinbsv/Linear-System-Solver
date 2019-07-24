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

    public void multiply(double x) {
        for (int i = 0; i < equationLength; i++) {
            equation[i] *= x;
        }
    }
    public void add(double x) {
        for (int i = 0; i < equationLength; i++) {
            equation[i] += x;
        }
    }

    public void fillEquationFromFile(File file) {
        try (Scanner scanner = new Scanner(file)) {
            int i = 0;
            while (scanner.hasNext() && i < equationLength) {
                equation[i] = scanner.nextDouble();
                i++;
            }
        } catch (FileNotFoundException fileException) {
            System.out.print("File not found" + file);
        }
    }



}
