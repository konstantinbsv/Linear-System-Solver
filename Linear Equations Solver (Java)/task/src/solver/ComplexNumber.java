package solver;

import java.text.DecimalFormat;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ComplexNumber {

    private double real;
    private double imaginary;

    public ComplexNumber(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    public ComplexNumber(ComplexNumber other) {
        this.real      = other.real;
        this.imaginary = other.imaginary;
    }

    public ComplexNumber(String input) {
        String pattern = "^(?=[iI.\\d+-\\\\(])[\\(]?([+]?([-]?\\d*\\.?\\d*))??(([+])?([-]?\\d*\\.?\\d*)([i]))??[\\)]?$";

        Pattern cn = Pattern.compile(pattern);
        Matcher matcher  = cn.matcher(input);

        if (matcher.find()) {
            // real number
            if (matcher.group(2) == null) {
                this.real = 0;
            } else {
                this.real = Double.parseDouble(matcher.group(2));
            }

            // imaginary number
            if (matcher.group(6) == null) { // if no imaginary number
                this.imaginary = 0;
            }
            else if ("".equals(matcher.group(5)) && matcher.group(6) != null) { // i
                this.imaginary = 1;
            } else if ("-".equals(matcher.group(5))) { // -i
                this.imaginary = -1;
            } else {
                this.imaginary = Double.parseDouble(matcher.group(5));
            }
        }
    }

    public double getReal() {
        return real;
    }

    public double getImaginary() {
        return imaginary;
    }

    public ComplexNumber add(ComplexNumber other) {
        double resultReal      = this.real      + other.real;
        double resultImaginary = this.imaginary + other.imaginary;
        return new ComplexNumber(resultReal, resultImaginary);
    }

    public ComplexNumber subtract(ComplexNumber other) {
        double resultReal      = this.real      - other.real;
        double resultImaginary = this.imaginary - other.imaginary;
        return new ComplexNumber(resultReal, resultImaginary);
    }

    // (a+bi)(c+di) = ac + adi + bci + bdi^2
    public ComplexNumber multiplyBy(ComplexNumber other) {
        double resultReal      = this.real*other.real - this.imaginary*other.imaginary;
        double resultImaginary = this.real*other.imaginary + this.imaginary*other.real;
        return new ComplexNumber(resultReal, resultImaginary);
    }

    public ComplexNumber divideBy(ComplexNumber other) {
        ComplexNumber numerator = this.multiplyBy(other.conjugate()); // multiply numerator by conjugate of denominator
        ComplexNumber denominator = other.multiplyBy(other.conjugate()); // multiply denominator by its conjugate
        double denominatorReal= denominator.getReal(); // get real part of denom. imaginary should be zero

        return numerator.divideBy(denominatorReal); // divide both real and imaginary part of numerator by denomiator
    }

    public ComplexNumber divideBy(double otherReal) {
        double resultReal      = this.real / otherReal;
        double resultImaginary = this.imaginary / otherReal;
        return new ComplexNumber(resultReal, resultImaginary);
    }

    // 1/(8+4i) = (8-4i)/((8+4i)*(8-4i)) = (8-4i)/(8^2+4^2)
    public ComplexNumber inverse() {
        ComplexNumber numerator = this.conjugate();
        double denominator = this.real*this.real + this.imaginary*this.imaginary;
        return numerator.divideBy(denominator);
    }

    public ComplexNumber conjugate() {
        double resultReal      =    this.real;
        double resultImaginary =  - this.imaginary;
        return new ComplexNumber(resultReal, resultImaginary);
    }

    public ComplexNumber negative() {
        double resultReal      =  - this.real;
        double resultImaginary =  - this.imaginary;
        return new ComplexNumber(resultReal, resultImaginary);
    }

    public boolean isZero() {
        return this.real == 0 && this.imaginary == 0;
    }

    public boolean isOne() {
        return this.real == 1 && this.imaginary == 0;
    }

    public boolean equals(ComplexNumber other) {
        return (this.real == other.real) && (this.imaginary == other.imaginary);
    }

    @Override
    public String toString() {
        String realString      = this.real == 0 ? ""  : Double.toString(this.real);
        String imaginaryString;
        if (this.imaginary == -1 || this.imaginary == 1) {
            imaginaryString = this.imaginary == 1 ? "i" : "-i";
        } else if (this.imaginary == 0) {
            imaginaryString = "";
        } else {
            imaginaryString = this.imaginary == 0 ? "" : (Double.toString(this.imaginary) + "i");
        }

        String positiveSign = this.imaginary > 0 ? "+" : "";

        return realString + positiveSign + imaginaryString;
    }

    public String toString(boolean printZeroTerms) {
        DecimalFormat df = new DecimalFormat("#0.0");
        String realString;
        String imaginaryString;


        realString =  df.format(this.real);
        if (this.imaginary == -1 || this.imaginary == 1) {
            imaginaryString = this.imaginary == 1 ? "i" : "-i";
        } else {
            imaginaryString = df.format(this.imaginary) + "i";
        }
        String positiveSign = (this.imaginary >= 0) ? "+" : "";

        if (!printZeroTerms) {
            if (this.real == 0) {
                realString = "";
            }
            if (this.imaginary == 0) {
                imaginaryString = "";
                positiveSign = "";
            }
        }

        return realString + positiveSign + imaginaryString;
    }
}
class test {
    public static void main(String[] args) {
        // ComplexNumber num1 = new ComplexNumber(3, 2);
        ComplexNumber num1 = new ComplexNumber("i");
        ComplexNumber num2 = new ComplexNumber(4, -3);

        System.out.println("num1 = " + num1);
        System.out.println("num2 = " + num2);
        System.out.println();

        System.out.println("num1.add(num2) = " + num1.add(num2));
        System.out.println("num1.subtract(num2) = " + num1.subtract(num2));
        System.out.println("num1.multiplyBy(num2) = " + num1.multiplyBy(num2));
        System.out.println("num1.divideBy(num2) = " + num1.divideBy(num2));
        System.out.println("num1.conjugate() = " + num1.conjugate());
        System.out.println("num1.inverse() = " + num1.inverse());
        
    }
}