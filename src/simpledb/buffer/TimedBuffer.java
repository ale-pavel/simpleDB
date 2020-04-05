package simpledb.buffer;

public class TimedBuffer {
	private int firstPinned;
	private Buffer buffer;
	
	public TimedBuffer(int firstPinned, Buffer buffer) {
		super();
		this.firstPinned = firstPinned;
		this.buffer = buffer;
	}

	public int getFirstPinned() {
		return firstPinned;
	}

	public void setFirstPinned(int firstPinned) {
		this.firstPinned = firstPinned;
	}

	public Buffer getBuffer() {
		return buffer;
	}

	public void setBuffer(Buffer buffer) {
		this.buffer = buffer;
	}

	@Override
	public int hashCode() {
		return this.getBuffer().hashCode();
	}

	@Override
	public boolean equals(Object buff) {
		return this.getBuffer().equals(buff);
	}
		
}
