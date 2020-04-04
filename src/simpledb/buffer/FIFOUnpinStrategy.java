package simpledb.buffer;

import java.util.TreeSet;

public class FIFOUnpinStrategy implements ChooseUnpinnedBufferStrategy, PinUnpinListener {
	private Buffer[] bufferPool;
	private TreeSet<TimedBuffer> bufferQueue;

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
