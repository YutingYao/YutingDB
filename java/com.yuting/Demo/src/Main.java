import java.util.concurrent.Semaphore;

public class Main {
    public static void main(String[] args) {
        MyThread t1 = new MyThread("线程A");
        t1.start();
        t1.start();
        new MyThread("线程B").run();
        new MyThread("线程C").run();

    }
}
class MyThread extends Thread {
    private String title;

    public MyThread(String title) {
        this.title = title;
    }

    @Override
    public void run() {
        for (int x = 0; x < 5; x++) {
            System.out.println(this.title + "运行，x = " + x);
        }
    }
}

