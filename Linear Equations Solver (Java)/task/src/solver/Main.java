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

        // Get Row Echelon Form through Gaussian elimination
        for (int pivot = 1; pivot <= system.getMatrixSize(); pivot++) {

            // multiply pivot by factor that makes it =1
            double rowFactor = 1/system.getTerm(pivot, pivot);
            system.getRow(pivot).multiplyRowAndSave(rowFactor);
            System.out.print(rowFactor <0 ? "-":" "); // neater formatting
            System.out.printf("%.2f * R%d -> R%d\n", Math.abs(rowFactor), pivot, pivot);


            // Perform row ops to get all terms below it =0
            for (int i = pivot+1; i <= system.getMatrixSize(); i++) {
                double factor = -system.getTerm(i, pivot)/system.getTerm(pivot, pivot);

                System.out.print(factor <0 ? "-":" "); // neater formatting
                System.out.printf("%.2f * R%d + R%d -> R%d\n", Math.abs(factor), pivot, pivot+1, pivot+1);
                system.getRow(i).addToRow(system.getRow(pivot).multiplyRowTemp(factor));
            }
        }

        System.out.println();
        system.print();

    }
}