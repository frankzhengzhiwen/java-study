package demo;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MappedFile {

	// 文件对象
	private File file;

	private MappedByteBuffer mappedByteBuffer;
	private FileChannel fileChannel;
	private boolean boundSuccess = false;

	// 文件最大只能为50MB
	private final static long MAX_FILE_SIZE = 1024 * 1024 * 50;
	
	// 最大的脏数据量512KB,系统必须触发一次强制刷
	private long MAX_FLUSH_DATA_SIZE = 1024 * 512;

	// 最大的刷间隔,系统必须触发一次强制刷
	private long MAX_FLUSH_TIME_GAP = 1000;

	// 文件写入位置
	private long writePosition = 0;

	// 最后一次刷数据的时候
	private long lastFlushTime;

	// 上一次刷的文件位置
	private long lastFlushFilePosition = 0;
	
	public MappedFile(String path) {
		file = new File(path);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws Exception {
		MappedFile mappedFile = new MappedFile("D:/test3.ini");
		mappedFile.appendData("1不好意思呀2".getBytes());
	}

	/**
	 * 在文件末尾追加数据
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public synchronized boolean appendData(byte[] data) throws Exception {
		try {
			RandomAccessFile raf = new RandomAccessFile(file, "rw");
			this.fileChannel = raf.getChannel();
			this.mappedByteBuffer = this.fileChannel
					.map(FileChannel.MapMode.READ_WRITE, file.length(), data.length);
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.mappedByteBuffer.put(data);
		fileChannel.close();
//		fileChannel.write(mappedByteBuffer);

		// 检查是否需要把内存缓冲刷到磁盘
//		if ( (writePosition - lastFlushFilePosition > this.MAX_FLUSH_DATA_SIZE)
//			 ||
//			 (System.currentTimeMillis() - lastFlushTime > this.MAX_FLUSH_TIME_GAP
//			  && writePosition > lastFlushFilePosition) ) {
//			flush();   // 刷到磁盘
//		}
		
		return true;
	}

	public synchronized void flush() {
		this.mappedByteBuffer.force();
		this.lastFlushTime = System.currentTimeMillis();
		this.lastFlushFilePosition = writePosition;
	}

}
			