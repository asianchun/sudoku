import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class SudokuFrame extends JFrame {

    private SudokuPanel mainPanel;
    private JTextField[] numberFields;
    private int[][] sudokuNines;
    private int[] solutions;
    private boolean pencil;

    public SudokuFrame(){

        mainPanel = new SudokuPanel();
        numberFields = new JTextField[81];
        solutions = new int[81];
        sudokuNines = new int[27][9];
        pencil = false;

        //Setting up the playing fields
        for (int i = 0; i < numberFields.length; i++){
            numberFields[i] = new JTextField();
            numberFields[i].setFont(new Font("Berlin Sans FB", Font.PLAIN, 30));
            numberFields[i].setHorizontalAlignment(JTextField.CENTER);
            numberFields[i].setBorder(BorderFactory.createBevelBorder(1, Color.GRAY, Color.GRAY));
            numberFields[i].setCaretColor(Color.white);

            numberFields[i].addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    super.keyTyped(e);
                    char c = e.getKeyChar();

                    if (!(Character.isDigit(c)) || c == 48){
                        if (c == 32){
                            showWin(); //Spacebar
                        } else if (c == 45){
                            checkWin(); //Minus
                        } else if (c == 43){
                            if (!pencil){
                                pencil = true; //Plus
                                System.out.println("PENCIL ON");
                            } else {
                                pencil = false;
                                System.out.println("PENCIL OFF");
                            }
                        }

                        e.consume();
                    } else {
                        checkValidity();
                    }
                }
            });

            mainPanel.add(numberFields[i]);
        }

        //Frame Setup
        this.add(mainPanel);
        this.setTitle("SUDOKU");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setLocationRelativeTo(null);

        //Start the game setup
        startSudoku();
    }

    //Make sure only one digit can be inserted
    public void checkValidity(){
        for (JTextField textField : numberFields){
            if (textField.isFocusOwner() && textField.isEditable()){
                if (pencil){
                    textField.setFont(new Font("Berlin Sans FB", Font.PLAIN, 13));
                } else {
                    textField.setFont(new Font("Berlin Sans FB", Font.PLAIN, 30));

                    if (!textField.getText().equals("")){
                        textField.setText("");
                    }
                }

                textField.setForeground(Color.black);
            }
        }
    }

    public void startSudoku(){
        Random random = new Random();
        int preFilledAmount = 20;
        int[] threeCells;

        //Put the starting numbers
        for (int i = 0; i < preFilledAmount; i++){
            int randomCell;
            int randomNumber = random.nextInt(9) + 1;

            do {
                randomCell = random.nextInt(81);
            } while (!numberFields[randomCell].isEditable());

            do {
                threeCells = putInSudoku(randomCell, randomNumber);

                if (!validInput(threeCells)){
                    if (randomNumber == 9){
                        randomNumber = 1;
                    } else {
                        randomNumber++;
                    }
                }

            } while (!validInput(threeCells));

            numberFields[randomCell].setText(Integer.toString(randomNumber));
            numberFields[randomCell].setEditable(false);
        }

        solveSudoku();
    }

    public int[] putInSudoku(int cell, int number){
        int tempCell = 0;
        int index = 0;
        int[] threeCells = {0,0,0};

        //Square and horizontal nines
        if (cell >= 0 && cell < 9){
            tempCell = cell;
            sudokuNines[0][cell] = number;

            if (cell == 0 || cell == 1 || cell == 2){
                sudokuNines[9][cell] = number;
                threeCells[1] = 9;
            } else if (cell == 3 || cell == 4 || cell == 5){
                sudokuNines[10][cell-3] = number;
                threeCells[1] = 10;
            } else {
                sudokuNines[11][cell-6] = number;
                threeCells[1] = 11;
            }
        } else if (cell >= 9 && cell < 18) {
            tempCell = cell - 9;
            index = 1;
            sudokuNines[1][tempCell] = number;
            threeCells[0] = 1;

            if (cell == 9 || cell == 10 || cell == 11){
                sudokuNines[9][tempCell+3] = number;
                threeCells[1] = 9;
            } else if (cell == 12 || cell == 13 || cell == 14){
                sudokuNines[10][tempCell] = number;
                threeCells[1] = 10;
            } else {
                sudokuNines[11][tempCell-3] = number;
                threeCells[1] = 11;
            }
        } else if (cell >= 18 && cell < 27) {
            tempCell = cell - 18;
            index = 2;
            sudokuNines[2][tempCell] = number;
            threeCells[0] = 2;

            if (cell == 18 || cell == 19 || cell == 20){
                sudokuNines[9][tempCell+6] = number;
                threeCells[1] = 9;
            } else if (cell == 21 || cell == 22 || cell == 23){
                sudokuNines[10][tempCell+3] = number;
                threeCells[1] = 10;
            } else {
                sudokuNines[11][tempCell] = number;
                threeCells[1] = 11;
            }
        } else if (cell >= 27 && cell < 36) {
            tempCell = cell - 27;
            index = 3;
            sudokuNines[3][tempCell] = number;
            threeCells[0] = 3;

            if (cell == 27 || cell == 28 || cell == 29){
                sudokuNines[12][tempCell] = number;
                threeCells[1] = 12;
            } else if (cell == 30 || cell == 31 || cell == 32){
                sudokuNines[13][tempCell-3] = number;
                threeCells[1] = 13;
            } else {
                sudokuNines[14][tempCell-6] = number;
                threeCells[1] = 14;
            }
        } else if (cell >= 36 && cell < 45) {
            tempCell = cell - 36;
            index = 4;
            sudokuNines[4][tempCell] = number;
            threeCells[0] = 4;

            if (cell == 36 || cell == 37 || cell == 38){
                sudokuNines[12][tempCell+3] = number;
                threeCells[1] = 12;
            } else if (cell == 39 || cell == 40 || cell == 41){
                sudokuNines[13][tempCell] = number;
                threeCells[1] = 13;
            } else {
                sudokuNines[14][tempCell-3] = number;
                threeCells[1] = 14;
            }
        } else if (cell >= 45 && cell < 54) {
            tempCell = cell - 45;
            index = 5;
            sudokuNines[5][tempCell] = number;
            threeCells[0] = 5;

            if (cell == 45 || cell == 46 || cell == 47){
                sudokuNines[12][tempCell+6] = number;
                threeCells[1] = 12;
            } else if (cell == 48 || cell == 49 || cell == 50){
                sudokuNines[13][tempCell+3] = number;
                threeCells[1] = 13;
            } else {
                sudokuNines[14][tempCell] = number;
                threeCells[1] = 14;
            }
        } else if (cell >= 54 && cell < 63) {
            tempCell = cell - 54;
            index = 6;
            sudokuNines[6][tempCell] = number;
            threeCells[0] = 6;

            if (cell == 54 || cell == 55 || cell == 56){
                sudokuNines[15][tempCell] = number;
                threeCells[1] = 15;
            } else if (cell == 57 || cell == 58 || cell == 59){
                sudokuNines[16][tempCell-3] = number;
                threeCells[1] = 16;
            } else {
                sudokuNines[17][tempCell-6] = number;
                threeCells[1] = 17;
            }
        } else if (cell >= 63 && cell < 72) {
            tempCell = cell - 63;
            index = 7;
            sudokuNines[7][tempCell] = number;
            threeCells[0] = 7;

            if (cell == 63 || cell == 64 || cell == 65){
                sudokuNines[15][tempCell+3] = number;
                threeCells[1] = 15;
            } else if (cell == 66 || cell == 67 || cell == 68){
                sudokuNines[16][tempCell] = number;
                threeCells[1] = 16;
            } else {
                sudokuNines[17][tempCell-3] = number;
                threeCells[1] = 17;
            }
        } else if (cell >= 72 && cell < 81) {
            tempCell = cell - 72;
            index = 8;
            sudokuNines[8][tempCell] = number;
            threeCells[0] = 8;

            if (cell == 72 || cell == 73 || cell == 74){
                sudokuNines[15][tempCell+6] = number;
                threeCells[1] = 15;
            } else if (cell == 75 || cell == 76 || cell == 77){
                sudokuNines[16][tempCell+3] = number;
                threeCells[1] = 16;
            } else {
                sudokuNines[17][tempCell] = number;
                threeCells[1] = 17;
            }
        }

        //Vertical nines
        if (tempCell == 0){
            sudokuNines[18][index] = number;
            threeCells[2] = 18;
        } else if (tempCell == 1){
            sudokuNines[19][index] = number;
            threeCells[2] = 19;
        } else if (tempCell == 2){
            sudokuNines[20][index] = number;
            threeCells[2] = 20;
        } else if (tempCell == 3){
            sudokuNines[21][index] = number;
            threeCells[2] = 21;
        } else if (tempCell == 4){
            sudokuNines[22][index] = number;
            threeCells[2] = 22;
        } else if (tempCell == 5){
            sudokuNines[23][index] = number;
            threeCells[2] = 23;
        } else if (tempCell == 6){
            sudokuNines[24][index] = number;
            threeCells[2] = 24;
        } else if (tempCell == 7){
            sudokuNines[25][index] = number;
            threeCells[2] = 25;
        } else {
            sudokuNines[26][index] = number;
            threeCells[2] = 26;
        }

        return threeCells;
    }

    //Checks whether the number passes the sudoku rules
    public boolean validInput(int[] ninesCells){
        boolean valid = true;

        for (int i = 0; i < 3; i++){
            int currentCell = ninesCells[i];

            for (int n = 0; n < 9; n++){
                if (sudokuNines[currentCell][n] != 0){
                    for (int t = 0; t < 9; t++){
                        if (n != t){
                            if (sudokuNines[currentCell][n] == sudokuNines[currentCell][t]){
                                valid = false;
                            }
                        }
                    }
                }
            }
        }

        return valid;
    }

    public void showWin(){
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < numberFields.length; i++){
            if (!numberFields[i].getText().equals("")){
                if (solutions[i] != Integer.parseInt(numberFields[i].getText())){
                    numberFields[i].setForeground(new Color(0x3dad00));
                }
            } else {
                numberFields[i].setForeground(new Color(0x3dad00));
            }

            numberFields[i].setText(Integer.toString(solutions[i]));
        }
    }

    public void checkWin(){
        boolean win = true;

        for (int i = 0; i < solutions.length; i++){
            if (!numberFields[i].getText().equals("")){
                if (solutions[i] != Integer.parseInt(numberFields[i].getText())){
                    numberFields[i].setForeground(Color.red);
                    win = false;
                } else {
                    numberFields[i].setForeground(Color.black);
                }
            }
        }

        if (win){
            System.out.println("CONGRATS!! YOU BEAT SUDOKU!!");
        }
    }

    public void solveSudoku(){
        int[] threeCells;
        int i = 0;
        boolean moveForward = true;

        while (i >= 0 && i < 81){
            if (numberFields[i].isEditable()){
                int number = solutions[i] + 1;

                if (number <= 9) {
                    boolean exit = false;

                    do {
                        threeCells = putInSudoku(i, number);

                        if (!validInput(threeCells)){
                            number++;
                        } else {
                            exit = true;
                            solutions[i] = number;
                            moveForward = true;
                        }

                        if (number == 10){
                            exit = true;
                            solutions[i] = 0;
                            threeCells = putInSudoku(i,0);
                        }
                    } while (!exit);

                } else {
                    solutions[i] = 0;
                }

                if (solutions[i] == 0){
                    moveForward = false;
                }

            } else {
                solutions[i] = Integer.parseInt(numberFields[i].getText());
            }

            if (!moveForward){
                i--;
            } else {
                i++;
            }
        }

        if (i == -1) {
            System.out.println("SUDOKU FAILED");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            newSudoku();
        } else {
            System.out.println("SUDOKU SOLVED");
            this.setVisible(true);
        }

    }

    public void newSudoku(){
        for (JTextField numberField : numberFields){
            numberField.setEditable(true);
            numberField.setText("");
        }

        for (int solution : solutions) {
            solution = 0;
        }

        for (int i = 0; i < sudokuNines.length; i++){
            for (int n = 0; n < 9; n++){
                sudokuNines[i][n] = 0;
            }
        }

        startSudoku();
    }
}
