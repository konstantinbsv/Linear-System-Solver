package solver;

import java.io.File;

class Main {

    public static void main(String[] args) {

        if (args.length != 4 || !"-in".equals(args[0]) && !"-out".equals(args[2])) {
            System.out.println("Syntax: -in <inputfile.txt> -out <outputfile.txt>");
            System.exit(-1);
        }

        File inputFile = new File(args[1]);
        System.out.println(inputFile);

        Matrix system = new Matrix(inputFile);
        system.print();


    }
}