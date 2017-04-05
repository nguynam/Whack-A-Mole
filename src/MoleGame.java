import java.io.Console;
import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Created by NamNguyen on 4/2/17.
 */

public class MoleGame extends Thread {

    private static int max_available = 4;
    private int index;
    private static int hit = 0;
    private static int miss = 0;
    private char letter;
    public static final Semaphore sem = new Semaphore(max_available);
    private static int rowSize = 5;
    private static int columnSize = 5;
    public static volatile List<String> board = new ArrayList<>();
    public static volatile boolean playing = true;

    MoleGame(int index, char letter) {
        this.index = index;
        this.letter = letter;
    }

    @Override
    public void run() {
        Random rand = new Random();
        while (playing){
            try{
                int popUp = rand.nextInt(1500) + 1000;
                int hide = rand.nextInt(5) + 1;

                TimeUnit.SECONDS.sleep(hide);
                sem.acquire();
                board.set(index, String.valueOf(letter));
                System.out.print("\033[H\033[2J");
                System.out.flush();
                printBoard();

                TimeUnit.MILLISECONDS.sleep(popUp);
                board.set(index, " ");
                System.out.print("\033[H\033[2J");
                System.out.flush();
                printBoard();
                sem.release();
            }
            catch (Exception e){
                System.out.println(e);
            }
        }
    }

    public static void printBoard(){
        int moleIndex = 0;
        char escCode = 0x1B;
        //Print board
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < columnSize; j++) {
                //System.out.print(String.format("%c[%d;%df",escCode,0,1));
                System.out.print(board.get(moleIndex));
                moleIndex++;
            }
            System.out.print("\n");
        }
        System.out.println("Hit: " + hit);
        System.out.println("Miss: " + miss);
        System.out.print("Enter mole to hit: ");
    }

    public static void main(String[] args) {
        int totalMoles = rowSize * columnSize;
        Scanner scanner = new Scanner(System.in);

        //Initialize empty board
        for (int i = 0; i < totalMoles; i++) {
            board.add(" ");
        }

        //Create mole threads
        char letter = 'a';
        for(int i = 0; i < totalMoles; i++){
            Thread t = new MoleGame(i, letter);
            t.start();
            letter++;
        }

        //Print empty board
        try{
            System.out.print("\033[H\033[2J");
            System.out.flush();
            printBoard();
        }
        catch (Exception e){
            System.out.print(e);
        }

        //Read user input
        while (playing){
            String str = scanner.next();
            if(board.contains(str)){
                hit++;
                board.set(board.indexOf(str), " ");
                System.out.print("\033[H\033[2J");
                System.out.flush();
                printBoard();
            }
            else if(str.equals("quit")){
                playing = false;
            }
            else{
                miss++;
            }
        }
    }
}



