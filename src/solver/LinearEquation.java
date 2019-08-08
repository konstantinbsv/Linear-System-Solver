package solver;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Scanner;

class LinearEquation {
    private ComplexNumber[] equation;
    private int equationLength;

    public LinearEquation(int numOfCoeffs) {
        equationLength = numOfCoeffs + 1; // +1 for constant
        equation = new ComplexNumber[equationLength];
    }

    public ComplexNumber[] getEquation() {
        return equation;
    }

    public ComplexNumber getTerm(int i) {
        if (i < 1 || i > equationLength) {
            throw new IndexOutOfBoundsException("Invalid term index: " + i);
        }

        return equation[i-1];
    }

    protected void setCoeff(int i, ComplexNumber newTerm) {
        if (i < 1 || i > equationLength-1) { // can't set constants
            throw new IndexOutOfBoundsException("Invalid term index: " + i);
        }

        equation[i-1] = newTerm;
    }

    protected void setTerm(int i, ComplexNumber newTerm) {
        if (i < 1 || i > equationLength) { // can set constants
            throw new IndexOutOfBoundsException("Invalid term index: " + i);
        }

        equation[i-1] = newTerm;
    }

    public ComplexNumber getConstantTerm() {
        return equation[equationLength-1];
    }

    public void multiplyRowAndChange(ComplexNumber x) {
        //if (x != 1) {
            for (int i = 0; i < equationLength; i++) {
                equation[i] = equation[i].multiplyBy(x);
            //}
        }
    }

    public LinearEquation multiplyRowTemp(ComplexNumber x) {
        LinearEquation result = new LinearEquation(equationLength-1);
        for (int i = 1; i <= equationLength; i++) {
            result.setTerm(i, equation[i-1].multiplyBy(x));
        }

        return result;
    }

    public void addToRow(LinearEquation secondEquation) {
        if (secondEquation.equationLength != equationLength) {
            throw new InputMismatchException("Linear equations are of unequal size");
        }

        for (int i = 0; i < equationLength; i++) {
            equation[i] = equation[i].add(secondEquation.getTerm(i+1));
        }
    }

    /*
    public void addToRow(double[] secondEquation) {
        if (secondEquation.length != equationLength) {
            throw new InputMismatchException("Linear equations are of unequal size");
        }

        for (int i = 0; i < equationLength; i++) {
            equation[i] += secondEquation[i];
        }
    }

     */

    public void subFromRow(LinearEquation secondEquation) {
        for (int i = 0; i < equationLength; i++) {
            equation[i] = equation[i].subtract(secondEquation.getTerm(i));
        }
    }

    public void fillEquationFromScanner(Scanner scanner) {
        int i = 0;
        while (scanner.hasNext() && i < equationLength) {
            equation[i] = new ComplexNumber(scanner.next());
            i++;
        }
    }

    public void print() {
        for (int i = 0; i < equationLength; i++) {
            System.out.print(equation[i] + " ");
        }
    }

    public boolean isInconsistent() {
        if (getConstantTerm().isZero()) { // e.g. 1 0 4 | 0
            return false;
        } else {
            if(hasNonZeroCoeffs()) { // e.g. 0 3 0 | 5
                    return false;
            }
        }
        return true; // e.g. 0 0 0 | 5
    }

    public boolean hasNonZeroCoeffs() {
        for (int i = 0; i < equationLength-1; i++) { // equationLength-1, do not include constant term
            if (!equation[i].isZero()) {
                return true;
            }
        }
        return false;
    }
}
