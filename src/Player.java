import java.util.Random;
import java.util.Scanner;

public class Player {
    private final static Random rand = new Random();
    private final static Scanner read = new Scanner(System.in);

    private final static String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final static char[] status = {' ', 'X', '~', '*'};

    // Mínimo de 5, recomendado 6. Máximo de 26.
    private final static int BOARD_SIZE = 10;

    private final static int STATUS_EMPTY = 0;
    private final static int STATUS_OCCUPIED = 1;
    private final static int STATUS_EMPTY_HIT = 2;
    private final static int STATUS_HIT = 3;

    int[][] area = new int[BOARD_SIZE][BOARD_SIZE];
    public int remainingBoats = 0;
    public int playerNumber;
    public boolean randomAssign;
    public boolean isPlayer;

    public Player(boolean isPlayer, boolean randomAssign, int playerNumber) {
        this.randomAssign = randomAssign;
        this.isPlayer = isPlayer;
        this.playerNumber = playerNumber;

        definePositions();
    }

    void drawBoard(boolean showBoats) {
        if (!showBoats) {
            status[1] = ' ';
        }

        System.out.print("\n\t");
        for(int i = 0; i < BOARD_SIZE; i++) System.out.print(letters.charAt(i) + "\t");
        System.out.println();

        for(int i = 0; i < BOARD_SIZE; i++) {
            System.out.print(i + "\t");
            for(int j = 0; j < BOARD_SIZE; j++) {
                System.out.print(status[this.area[j][i]] + "\t");
            }
            System.out.println();
        }

        System.out.println();
    }

    boolean receiveDamage(boolean random) {
        int xPos;
        int yPos;

        if (random) {
            drawBoard(false);

            System.out.print("Digite a posição no eixo X [A-J]: ");
            xPos = rand.nextInt(0, BOARD_SIZE - 1);
            Main.sleep(1);
            System.out.println(letters.charAt(xPos));

            System.out.print("Digite a posição no eixo Y [0-9]: ");
            yPos = rand.nextInt(0, BOARD_SIZE - 1);
            Main.sleep(1);
            System.out.println(yPos);
        } else {
            drawBoard(false);

            do {
                System.out.print("Digite a posição no eixo X [A-J]: ");
                char input = read.next().charAt(0);

                xPos = Main.findIndex(input);
            } while (xPos < 0 || xPos > BOARD_SIZE - 1);

            do {
                System.out.print("Digite a posição no eixo Y [0-9]: ");
                yPos = read.nextInt();
            } while (yPos < 0 || yPos > BOARD_SIZE - 1);
        }

        if (this.area[xPos][yPos] == STATUS_EMPTY) {
            this.area[xPos][yPos] = STATUS_EMPTY_HIT;

            drawBoard(false);
            System.out.println("Não acertou nada!");

            return true;
        } else if (this.area[xPos][yPos] == STATUS_OCCUPIED) {
            this.area[xPos][yPos] = STATUS_HIT;
            this.remainingBoats--;

            drawBoard(false);
            System.out.println("Acertou em cheio!");
        } else {
            drawBoard(false);
            System.out.println("Nada aconteceu...");
        }

        return false;
    }

    private void definePositions() {
        int[] boats = {1, 2, 3, 4};

        for(int i = 0; i < boats.length; i++) {
            int size = 0;

            switch (i) {
                case 0 -> size = 4;
                case 1 -> size = 3;
                case 2 -> size = 2;
                case 3 -> size = 1;
            }

            this.remainingBoats += size * boats[i];

            for(int j = 0; j < boats[i]; j++) {
                int xPos;
                int yPos;
                int direction;

                do {
                    if (this.randomAssign) {
                        xPos = rand.nextInt(0, BOARD_SIZE - 1);
                        yPos = rand.nextInt(0, BOARD_SIZE - 1);
                        direction = rand.nextInt(0, 3);
                    } else {
                        drawBoard(true);

                        do {
                            System.out.print("Digite a posição no eixo X [A-J]: ");
                            char input = read.next().charAt(0);

                            xPos = Main.findIndex(input);
                        } while (xPos < 0 || xPos > BOARD_SIZE - 1);

                        do {
                            System.out.print("Digite a posição no eixo Y [0-9]: ");
                            yPos = read.nextInt();
                        } while (yPos < 0 || yPos > BOARD_SIZE - 1);

                        if (size != 1) {
                            System.out.println("[0] Direita");
                            System.out.println("[1] Esquerda");
                            System.out.println("[2] Baixo");
                            System.out.println("[3] Cima");

                            do {
                                System.out.print("Digite a direção do barco: ");
                                direction = read.nextInt();
                            } while (direction < 0 || direction > 3);
                        } else direction = 0;

                        if (invalidPositions(xPos, yPos, size, direction)) {
                            System.out.println("Posição inválida ou ocupada!");
                        }
                    }
                } while(invalidPositions(xPos, yPos, size, direction));

                for(int k = 0; k < size; k++) {
                    if (direction == 0)
                        this.area[xPos + k][yPos] = 1;
                    else if (direction == 1)
                        this.area[xPos - k][yPos] = 1;
                    else if (direction == 2)
                        this.area[xPos][yPos + k] = 1;
                    else
                        this.area[xPos][yPos - k] = 1;
                }
            }
        }

        if (this.isPlayer) {
            System.out.printf("\nSeu campo (Jogador %d):\n", this.playerNumber);

            drawBoard(true);

            System.out.print("Pressione Enter para continuar...");
            read.nextLine();
        }
    }

    private boolean invalidPositions(int x, int y, int size, int direction) {
        if (direction == 0) {
            for(int i = 0; i < size; i++) {
                if (x + i >= BOARD_SIZE) return true;
                if (this.area[x + i][y] != 0) return true;
            }
        } else if (direction == 1) {
            for(int i = 0; i < size; i++) {
                if (x - i < 0) return true;
                if (this.area[x - i][y] != 0) return true;
            }
        } else if (direction == 2) {
            for(int i = 0; i < size; i++) {
                if (y + i >= BOARD_SIZE) return true;
                if (this.area[x][y + i] != 0) return true;
            }
        } else {
            for(int i = 0; i < size; i++) {
                if (y - i < 0) return true;
                if (this.area[x][y - i] != 0) return true;
            }
        }

        return false;
    }
}

