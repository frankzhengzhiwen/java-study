package task;

import service.FileService;

import java.io.File;

/**
 * 扫描任务类：只处理文件夹及下一级文件、文件夹
 */
class ScanJob implements Runnable{

    /**
     * 扫描的文件目录
     */
    private File dir;

    /**
     * 文件扫描对象
     */
    private FileScanner scanner;

    public ScanJob(File dir, FileScanner scanner) {
        this.dir = dir;
        this.scanner = scanner;
    }

    @Override
    public void run(){

        try {
            try {
                if(dir == null || !dir.isDirectory())
                    return;
                FileService.process(dir);
                File[] subs = dir.listFiles();
                if(subs == null)
                    return;
                for(File sub : subs){
                    if(sub.isDirectory()) {
                        // 子线程还没有执行时就需要将待处理任务数+1
                        scanner.taskCount.incrementAndGet();
                        scanner.scan(sub);
                    }
                }
            } finally {
                // 结束任务时，获取待执行任务数，如果只有当前一个线程，则关闭线程池
                if(scanner.taskCount.decrementAndGet() == 0){
                    scanner.finish();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
