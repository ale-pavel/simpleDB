package simpledb.buffer;

public interface PinUnpinListener {
	public void pinned(Buffer b);

	public void unpinned(Buffer b);
}
