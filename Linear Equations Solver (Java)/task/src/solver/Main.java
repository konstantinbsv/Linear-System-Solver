package solver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {

        if (args.length != 4 || !"-in".equals(args[0]) && !"-out".equals(args[2])) {
            System.out.println("Syntax: -in <inputfile.txt> -out <outputfile.txt>");
            System.exit(-1);
        }

        File inputFile = new File(args[1]);
        System.out.println(inputFile);

        Matrix system = new Matrix(inputFile);

        System.out.println("-----Input matrix----");
        system.print();
        System.out.println("-----Performing Gaussian elimination----");

        // find row with leading non-zero element
        int leadingRow = system.findLeadingNonZeroRow();

        // if all columns and rows are zeros
        if (leadingRow == -1) {
            // are all constant terms zero too?
            double constantTermsSum = Arrays.stream(system.getConstantTermsArray()).sum();
            if (constantTermsSum == 0) {
                System.out.println("Infinitely many solutions");
                writeToFile(args[3], system, SystemResult.infiniteSolutions);
            } else {
                System.out.println("No solutions");
                writeToFile(args[3], system, SystemResult.noSolution);
            }
            return;
        } else if (leadingRow > 1) {
            system.swapRows(1, leadingRow);
        }

        // Get Row Echelon Form through Gaussian elimination
        for (int pivot = 1 ; pivot <= system.getMatrixNumOfVariables(); pivot++) {

            // if pivot is zero, swap with another row that has non-zero element in that column
            if (system.getTerm(pivot, pivot) == 0) {
                int newRow = system.findNonZeroRowInCol(pivot);

                // if there are no non-zero terms in this column
                if (newRow == -1) {
                    // find element in next column and swap columns
                    int colToSwap = system.findLeadingNonZeroCol(pivot);
                    System.out.println("colToSwap = " + colToSwap);
                    if (colToSwap == -1) {
                        // no more columns with non-zero elements
                        System.out.println("No more columns with non-zero elements");
                        break;
                    } else {
                        executeCommand(new SwapColumns(system, pivot, colToSwap));
                    }
                } else {
                    system.swapRows(pivot, newRow);
                }
            }

            // multiply pivot by factor that makes it =1
            double normalizationFactor = 1/system.getTerm(pivot, pivot);
            // System.out.printf("normalizationFactor = %.2f\n", normalizationFactor);
            system.getRow(pivot).multiplyRowAndSave(normalizationFactor);
            System.out.print(normalizationFactor <0 ? "-":" "); // neater formatting
            System.out.printf("%.2f * R%d -> R%d\n", Math.abs(normalizationFactor), pivot, pivot);


            // Perform row ops to get all terms below it =0
            for (int currentRow = pivot+1; currentRow <= system.getMatrixNumOfEquations(); currentRow++) {
                double factor = -system.getTerm(currentRow, pivot)/system.getTerm(pivot, pivot);

                printRowOp(pivot, currentRow, factor);
                system.getRow(currentRow).addToRow(system.getRow(pivot).multiplyRowTemp(factor));
            }
        }

        System.out.println("-----Row Echelon----");
        system.print();
        if (system.isInconsistent()) {
            System.out.println("No solutions - inconsistent");
            writeToFile(args[3], system, SystemResult.noSolution);

            return;
        }
        if (system.numOfFreeVariables() > 0) {
            System.out.printf("Infinitely many solutions - has %d free variables\n" , system.numOfFreeVariables());
            writeToFile(args[3], system, SystemResult.infiniteSolutions);

            return;
        }
        //
        while(undoCommand());

        System.out.println("-----Performing Gauss-Jordan elimination----");

        // perform Gauss-Jordan elimination for Reduced Row Echelon Form
        for (int currentColumn = system.getMatrixNumOfVariables(); currentColumn > 0; currentColumn--) {
            for (int currentRow = currentColumn - 1; currentRow > 0; currentRow--) {
                double factor = -system.getTerm(currentRow, currentColumn);

                printRowOp(currentColumn, currentRow, factor);
                system.getRow(currentRow).addToRow( system.getRow(currentColumn).multiplyRowTemp(factor) );
            }
        }
        System.out.println("-----Reduced Row Echelon----");
        system.print();

        System.out.println("-----Final Result----");
        system.printResults(true);
        writeToFile(args[3], system, SystemResult.solved);
    }

    private static void writeToFile(String filePath, Matrix system, SystemResult result) {
        // Save results to file
        File outputFile = new File(filePath);
        try (PrintWriter printWriter = new PrintWriter(outputFile)) {

            switch (result) {
                case solved:
                    for (int row = 1; row <= system.getMatrixNumOfVariables(); row++) {
                        printWriter.print(system.getRow(row).getConstantTerm());
                        printWriter.println();
                    }
                    break;
                case noSolution:
                    printWriter.print("No solutions");
                    break;
                case infiniteSolutions:
                    printWriter.print("Infinitely many solutions");
                    break;
                default:
                    printWriter.print("Solution error");
            }
            System.out.println("Results saved to: " + filePath);
        } catch (FileNotFoundException fileNotFound) {
            System.out.println("Output file exception: " + fileNotFound.getMessage());
        }
    }

    enum SystemResult {
        solved,
        noSolution,
        infiniteSolutions,
    }

    private static void printRowOp(int currentColumn, int currentRow, double factor) {
        System.out.print(factor < 0 ? "-" : " "); // neater formatting
        System.out.printf("%.2f * R%d + R%d -> R%d\n", Math.abs(factor), currentColumn, currentRow, currentRow);
    }

    private static void executeCommand(Command command) {
        if (command.execute()) {
            CommandHistory.push(command);
        }
    }

    private static boolean undoCommand() {
        if (!CommandHistory.isCommandHistoryEmpty()) {
            return CommandHistory.pop().undo(); // true if undo successful
        }
        return false;
    }
}