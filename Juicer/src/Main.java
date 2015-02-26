/**
 * Created by home on 11.02.15.
 */
public class Main {
    public static void main(String[] args) {
        Juicer juice = new Juicer();
        juice = juice.create();
        juice.getListMentioned();
        final Juicer finalJuice = juice;
        Thread myThread = new Thread(new Runnable() {
            @Override
            public void run() {
                finalJuice.sortList();
            }
        });
        myThread.start();
        juice.getNumberMin();
    }
}
