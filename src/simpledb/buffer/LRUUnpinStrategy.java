package simpledb.buffer;

import java.util.Deque;
import java.util.LinkedList;

public class LRUUnpinStrategy implements ChooseUnpinnedBufferStrategy, PinUnpinListener {
	private Buffer[] bufferPool;
	//Handles the unpinned buffers based on the sequence of unpin events arrived
	private Deque<Buffer> unpinnedBufferQueue;

	public void create(Buffer[] bufferPool) {
		this.bufferPool = bufferPool;
		unpinnedBufferQueue = new LinkedList<Buffer>();
		//Adds all the unpinned buffers to the queue (all should be during create)
		unpinnedQueueStartup();
	}
	
	private void unpinnedQueueStartup() {
		for(Buffer b : bufferPool) {
			if(!b.isPinned())
				unpinnedBufferQueue.addLast(b);
		}
	}

	@Override
	public void pinned(Buffer b) {
		//The buffer b is not available anymore to be pinned (or wasn't already)
		unpinnedBufferQueue.remove(b);
	}

	@Override
	public void unpinned(Buffer b) {
		//Adds the recently unpinned buffer to the tail of the queue
		if(!b.isPinned() && !unpinnedBufferQueue.contains(b))
			unpinnedBufferQueue.addLast(b);
	}

	@Override
	public Buffer chooseUnpinnedBuffer() {
		//The first buffer was unpinned less recently
		return unpinnedBufferQueue.pollFirst();
	}

}
