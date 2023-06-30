import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/*
=====================================================
==                  BATALHA NAVAL                  ==
==                  Programação 2                  ==
=====================================================
== Autores: Nicolas A. Hilbert                     ==
== Data: 22/06/23                                  ==
== Turma: INFO 63B                                 ==
=====================================================
*/

public class Main {
    public static void main(String[] args) {
        int gamemode;
        int randomAssign;
        int randomAssignTwo;
        int playerTurn = 1;
        boolean isRunning = true;

        Scanner read = new Scanner(System.in);

        System.out.println("=== BATALHA NAVAL - BEM VINDO ====\n");

        System.out.println("Tipo de jogo\n[1] Jogador X Jogador\n[2] Jogador X Computador\n");

        do {
            System.out.print("Digite a opção desejada: ");
            gamemode = read.nextInt();
        } while (gamemode != 1 && gamemode != 2);

        System.out.println("\nPosicionamento do Jogador 1\n[1] Manual\n[2] Automático\n");

        do {
            System.out.print("Digite a opção desejada: ");
            randomAssign = read.nextInt();
        } while (randomAssign != 1 && randomAssign != 2);

        if (gamemode == 1) {
            System.out.println("\nPosicionamento do Jogador 2\n[1] Manual\n[2] Automático\n");

            do {
                System.out.print("Digite a opção desejada: ");
                randomAssignTwo = read.nextInt();
            } while (randomAssignTwo != 1 && randomAssignTwo != 2);
        } else randomAssignTwo = 0;

        boolean isRandom = randomAssign == 2;
        boolean isRandomTwo = randomAssignTwo == 2 || gamemode == 2;

        Player playerOne = new Player(true, isRandom, 1);
        Player playerTwo = new Player(gamemode == 1, isRandomTwo, 2);

        do {
            if (playerTurn == 1) {
                System.out.println("\n========== TURNO DO JOGADOR 1 ==========");

                if (playerTwo.receiveDamage(false)) playerTurn = 2;
                else if (playerTwo.remainingBoats == 0) {
                    isRunning = false;
                }
            } else {
                System.out.println("\n========== TURNO DO JOGADOR 2 ==========");

                if (playerOne.receiveDamage(!playerTwo.isPlayer)) playerTurn = 1;
                else if (playerOne.remainingBoats == 0) {
                    isRunning = false;
                }
            }

            sleep(3);
        } while (isRunning);

        if (playerOne.remainingBoats == 0) {
            System.out.println("\n=== VITÓRIA DO JOGADOR 2! ===");
            playerTwo.drawBoard(true);
        } else {
            System.out.println("\n=== VITÓRIA DO JOGADOR 1! ===");
            playerOne.drawBoard(true);
        }

        System.out.println("\n===  OBRIGADO POR JOGAR!  ===");
    }

    static void sleep(int delay) {
        try {
            TimeUnit.SECONDS.sleep(delay);
        } catch (InterruptedException ignored) {}
    }

    static int findIndex(char letter) {
        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        letter = Character.toUpperCase(letter);

        for(int i = 0; i < letters.length(); i++) {
            if (letters.charAt(i) == letter)  {
                return i;
            }
        }

        return -1;
    }
}