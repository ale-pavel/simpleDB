package simpledb.buffer;

import java.util.Comparator;

public class TimedBufferComparator implements Comparator<TimedBuffer> {

	@Override
	public int compare(TimedBuffer t1, TimedBuffer t2) {
		//A lower value of firstPinned means the buffer comes first
		return t1.getFirstPinned() - t2.getFirstPinned();
	}

}
