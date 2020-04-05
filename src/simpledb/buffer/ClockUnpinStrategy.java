package simpledb.buffer;

import java.util.HashSet;
import java.util.Set;
//import java.util.Deque;

public class ClockUnpinStrategy implements ChooseUnpinnedBufferStrategy, PinUnpinListener {
	private Buffer[] bufferPool;
	private int currentPosition;
	//private Deque<Buffer> unpinnedBufferQueue;
	private Set<Buffer> unpinnedBufferSet;

	public void create(Buffer[] bufferPool) {
		this.bufferPool = bufferPool;
		//Starts scanning at the first element
		this.currentPosition = 0;
		//Keeps track of the unpinned buffer to change currentPosition correctly during pinned events
		this.unpinnedBufferSet = new HashSet<Buffer>();
		//Fills the set with unpinned buffers (should be all during create)
		unpinnedSetStartup();
	}
	
	private void unpinnedSetStartup() {
		for(Buffer b : bufferPool)
			if(!b.isPinned())
				unpinnedBufferSet.add(b);
	}
	
	//Lookup of a buffer in the bufferPool, returning its position
	private int bufferIndex(Buffer b) {
		for(int i=0; i<bufferPool.length; i++) {
			if(bufferPool[i].equals(b))
				return i;
		}
		return -1;
	}

	@Override
	public void pinned(Buffer b) {
		//The buffer was unpinned and is now pinned (a replacement has occurred)
		if(unpinnedBufferSet.contains(b)) {
			//Update with the index of the replaced buffer
			this.currentPosition = bufferIndex(b);
			unpinnedBufferSet.remove(b);
		}
	}

	@Override
	public void unpinned(Buffer b) {
		//If the buffer has been totally unpinned add it to the set tracking unpinned buffers
		if(!b.isPinned())
			unpinnedBufferSet.add(b);
	}

	@Override
	public Buffer chooseUnpinnedBuffer() {
		Buffer chosenBuffer = null;
		boolean foundUnpinned = false;
		boolean finishedScan = false;
		//Start scanning from the next element of the last replacement
		int i = currentPosition + 1;
		
		while(!foundUnpinned || !finishedScan) {			
			if(i < bufferPool.length) {
				if(!bufferPool[i].isPinned()) {
					//Found the first free buffer from the last replacement
					chosenBuffer = bufferPool[i];
					foundUnpinned = true;
				}
				i++;
			} else if(i == currentPosition) {
				//The circular scan ended
				finishedScan = true;
			} else
				//Resume the lookup (not finished) from the start of the bufferPool
				i = 0;
		}
		return chosenBuffer;
	}

}
