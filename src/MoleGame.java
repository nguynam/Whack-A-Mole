import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Created by NamNguyen on 4/2/17.
 */

public class MoleGame extends Thread {

    public static int max_available = 8;
    private int index;
    public static int hit = 0;
    public static int miss = 0;
    public char letter;
    public static Semaphore sem = new Semaphore(max_available);
    public static int rowSize = 4;
    public static int columnSize = 4;
    public static int totalMoles = rowSize * columnSize;
    public static volatile List<String> board = new ArrayList<>();
    public static volatile boolean playing = true;
    public static int popUpUpper = 0;
    public static int popUpLower = 0;
    public static int hideLower = 0;
    public static int hideUpper = 0;

    MoleGame(int index, char letter) {
        this.index = index;
        this.letter = letter;
    }

    @Override
    public void run() {
        Random rand = new Random();
        while (playing){
            try{
                int popUp = rand.nextInt(popUpUpper) + popUpLower;
                int hide = rand.nextInt(hideUpper) + hideLower;

                TimeUnit.SECONDS.sleep(hide);
                sem.acquire();
                board.set(index, String.valueOf(letter));
                System.out.print("\033[H\033[2J");
                System.out.flush();

                TimeUnit.MILLISECONDS.sleep(popUp);
                board.set(index, "");
                System.out.print("\033[H\033[2J");
                System.out.flush();
                sem.release();
            }
            catch (Exception e){
                System.out.println(e);
            }
        }
    }
}



