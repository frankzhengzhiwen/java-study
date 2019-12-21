package task;

import app.FileMeta;

import java.io.File;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 文件扫描类：处理目录扫描任务
 * @ClassName FileScanner
 * @Description
 * @Author frank
 * @Date 2019/12/19 4:49 上午
 * @Version 1.0
 */
public class FileScanner {

    /**ht
     * 系统CPU核数
     */
    private static final int COUNT = Runtime.getRuntime().availableProcessors();

    /**
     * 工作队列：无边界阻塞队列
     */
    private final BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>();

    /**
     * 工作线程池：使用固定数量和无边界阻塞队列，队列满后的拒绝策略为加入当前线程中执行
     */
    private final ExecutorService exe = new ThreadPoolExecutor(
            COUNT, COUNT, 0, TimeUnit.MILLISECONDS,
            workQueue, new ThreadPoolExecutor.CallerRunsPolicy());

    /**
     * 当前待处理的任务数
     */
    private final AtomicInteger taskCount = new AtomicInteger();

    /**
     * 在main主线程中阻塞，在所有线程结束后允许向下执行
     * 1.使用Semaphore：可以释放一定数量的许可，也可以等待一定数量的许可
     * 2.使用CountDownLatch：阻塞并等待数量减为0
     */
    private final Semaphore semaphore = new Semaphore(0);
    private final CountDownLatch latch = new CountDownLatch(1);

    /**
     * 扫描目录
     * @param dir
     * @throws InterruptedException
     */
    private void scan(File dir, Callback callback) throws InterruptedException {
        exe.submit(new ScanJob(dir, this, callback));
    }

    /**
     * 扫描目录：主线程执行任务并等待所有子线程任务结束
     * @param root 根目录
     * @throws InterruptedException
     */
    public void scanWait(File root, Callback callback) throws InterruptedException {
        taskCount.incrementAndGet();
        scan(root, callback);
//        semaphore.acquire();
        latch.await();
    }

    /**
     * 结束任务
     */
    public void finish(){
        exe.shutdownNow();
//        semaphore.release();
        latch.countDown();
    }

    public void shutDownNow(){
        if(!exe.isShutdown())
            exe.shutdownNow();
    }

    public interface Callback{
        void execute(FileMeta meta);
    }

    /**
     * 扫描任务类
     */
    private class ScanJob implements Runnable{

        /**
         * 扫描的文件目录
         */
        private File dir;

        /**
         * 文件扫描对象
         */
        private FileScanner scanner;

        private Callback callback;

        public ScanJob(File dir, FileScanner scanner, Callback callback) {
            this.dir = dir;
            this.scanner = scanner;
            this.callback = callback;
        }

        @Override
        public void run(){

            try {
                try {
                    if(dir == null)
                        return;
                    FileMeta meta = new FileMeta(dir.getName(), dir.getParent(), dir.length(), dir.lastModified());
                    callback.execute(meta);
                    if(!dir.isDirectory())
                        return;
                    File[] subs = dir.listFiles();
                    if(subs == null)
                        return;
                    for(File sub : subs){
                        // 子线程还没有执行时就需要将待处理任务数+1
                        scanner.taskCount.incrementAndGet();
                        scanner.scan(sub, callback);
                    }
                } finally {
                    // 结束任务时，获取待执行任务数，如果只有当前一个线程，则关闭线程池
                    if(scanner.taskCount.decrementAndGet() == 0){
                        scanner.finish();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
