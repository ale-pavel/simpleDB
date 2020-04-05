package simpledb.buffer;

import java.util.TreeSet;

public class FIFOUnpinStrategy implements ChooseUnpinnedBufferStrategy, PinUnpinListener {
	private Buffer[] bufferPool;
	private TreeSet<TimedBuffer> bufferQueue;
	//Needed to assign firstPinned values to TimedBuffers objects. Increased on pin/unpin events
	private int eventTimer;

	public void create(Buffer[] bufferPool) {
		this.bufferPool = bufferPool;
		bufferQueue = new TreeSet<TimedBuffer>();
		eventTimer = 1;
	}
	
	private void incrementTimer() {
		eventTimer++;
	}

	@Override
	public void pinned(Buffer b) {
		TimedBuffer timedBuff = new TimedBuffer(eventTimer, b);
		//If the TimedBuffer is already in the tree it shouldn't be added again
		if(!bufferQueue.contains(timedBuff))
			bufferQueue.add(timedBuff);
		incrementTimer();
	}

	@Override
	public void unpinned(Buffer b) {
		//Unpinned events do not affect FIFO strategy, unpinned blocks are searched in chooseUnpinnedBuffer() method
		incrementTimer();
	}

	@Override
	public Buffer chooseUnpinnedBuffer() {
		Buffer unpinnedBuffer = null;
		if(!bufferQueue.isEmpty()) {
			//The lowest value in the tree is the first pinned buffer
			for(TimedBuffer tb: bufferQueue) {
				if(!tb.getBuffer().isPinned()) {
					//currentBuffer is the lowest unpinned buffer in the tree
					unpinnedBuffer = tb.getBuffer();
					break;
				}
			}
		}
		return unpinnedBuffer;
	}

}
