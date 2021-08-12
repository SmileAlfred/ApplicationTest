import org.junit.Test;

public class VolatileTest {

    private final String TAG = "\tTAG\t";

    @Test
    public void testVolatile() throws Exception {
        AoFlag aoFlag = new AoFlag();
        aoFlag.start();
        for (int i = 0; i < 4; i++) {
            synchronized (aoFlag) {
                if (aoFlag.isFlag())
                    System.out.println(TAG + "test:\t" + i);
            }
            Thread.sleep(500);
        }
        aoFlag.join();
    }

    class AoFlag extends Thread {
        private volatile boolean flag = false;

        public boolean isFlag() {
            return flag;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            flag = true;
            System.out.println("\tTAG\t" + "run:\t" + flag);
        }
    }


}