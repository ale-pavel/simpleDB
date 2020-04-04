package simpledb.buffer;

public class LRUUnpinStrategy implements ChooseUnpinnedBufferStrategy, PinUnpinListener {
	private Buffer[] bufferPool;

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
