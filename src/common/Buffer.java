package common;

import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Buffer implements IBuffer {
	
//	private BlockingQueue<IGrid> buffer;
	private LinkedList<IGrid> buffer;
	
	private static int size;
	private static Buffer instance = null;
	
	public static Buffer getBuffer() {
		if (instance == null) instance = new Buffer();
		
		return instance;
	}
	
	public void create(int size) {
		
		if (size < 1 || size > Integer.MAX_VALUE) 
			throw new IllegalArgumentException("Invalid size");

		Buffer.size = size;
		buffer = new LinkedList<IGrid>();
	}
	
	private Buffer() {
		// do nothing
	}
	
	@Override
	public void add(IGrid grid) throws InterruptedException {
		
		if (grid == null)
			throw new IllegalArgumentException("IGrid is null");
		
//		buffer.offer(grid, 1, TimeUnit.SECONDS);
		buffer.offer(grid);
		System.out.printf("Add complete: %d %d %d\n", size, buffer.size(), size-buffer.size());
	}

	@Override
	public IGrid get() throws InterruptedException {
//		return buffer.poll(10, TimeUnit.MILLISECONDS);
		IGrid res = null;
		if (buffer.size() > 0) {
			res = buffer.poll();
		}
		return res;
	}
	
	@Override
	public int size() {
		return buffer.size();
	}

	@Override
	public int getCapacity() {
		return Buffer.size;
	}

	@Override
	public int getRemainingCapacity() {
//		return buffer.remainingCapacity();
//		System.out.printf("remainingCapacity: %d %d %d\n", size, buffer.size(), size-buffer.size());
		return Math.max(0,size - buffer.size());
	}
}