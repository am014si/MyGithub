import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TheadTest {

    private ExecutorService executor = Executors.newFixedThreadPool(5);

    public void startTenThreads() {
        for (int i = 0; i < 10; i++) {
            executor.execute(new FooWorker(i));
        }
        executor.shutdown();
    }

    private final class FooWorker implements Runnable {
        private Integer threadNum;

        public FooWorker(int threadNum) {
            this.threadNum = threadNum;
        }

        public void run() {
            try {
                System.out.println("Thread " + threadNum + " starting");
                //Thread.sleep(6000);
                //synchronized(this.threadNum){
                printText(threadNum);
                
               // }
                System.out.println("Thread " + threadNum + " finished");
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    private void printText(int threadNum){
    	try {
			//Thread.sleep(3000);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	System.out.println("ABC Thread Num "+threadNum);
    	printSomeElse(threadNum);
    }
    private void printSomeElse(int threadNum){
    	System.out.println("123 Thread Num "+threadNum);
    }

    public static void main(String[] args) {
        TheadTest tt = new TheadTest();
        tt.startTenThreads();
        Set<Integer> set = new TreeSet<Integer>();
        set.iterator();
    }

}