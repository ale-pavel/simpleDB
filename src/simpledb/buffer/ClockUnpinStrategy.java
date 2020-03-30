package simpledb.buffer;

import java.util.Deque;

public class ClockUnpinStrategy implements ChooseUnpinnedBufferStrategy, PinUnpinListener {
	private Buffer[] bufferPool;
	private int currentPosition;
	private Deque<Buffer> unpinnedBufferQueue;
	
	public void create(Buffer[] bufferPool) {
		
	}

	@Override
	public void pinned(Buffer b) {
		// TODO Auto-generated method stub

	}

	@Override
	public void unpinned(Buffer b) {
		// TODO Auto-generated method stub

	}

	@Override
	public Buffer chooseUnpinnedBuffer() {
		// TODO Auto-generated method stub
		return null;
	}

}
