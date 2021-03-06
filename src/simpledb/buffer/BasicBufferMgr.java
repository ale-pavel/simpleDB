package simpledb.buffer;

import java.util.ArrayList;
import java.util.List;

import simpledb.file.*;

/**
 * Manages the pinning and unpinning of buffers to blocks.
 * 
 * @author Edward Sciore
 *
 */
class BasicBufferMgr {
	private Buffer[] bufferpool;
	private int numAvailable;
	private List<PinUnpinListener> pinUnpinListeners;
	private ChooseUnpinnedBufferStrategy unpinStrategy;

	/**
	 * Creates a buffer manager having the specified number of buffer slots. This
	 * constructor depends on both the {@link FileMgr} and
	 * {@link simpledb.log.LogMgr LogMgr} objects that it gets from the class
	 * {@link simpledb.server.SimpleDB}. Those objects are created during system
	 * initialization. Thus this constructor cannot be called until
	 * {@link simpledb.server.SimpleDB#initFileAndLogMgr(String)} or is called
	 * first.
	 * 
	 * @param numbuffs the number of buffer slots to allocate
	 */
	BasicBufferMgr(int numbuffs) {
		bufferpool = new Buffer[numbuffs];
		numAvailable = numbuffs;
		for (int i = 0; i < numbuffs; i++)
			bufferpool[i] = new Buffer();
		
		//Creates the required strategies and listeners and instantiates them
		instantiateStrategies();
	}

	/**
	 * Flushes the dirty buffers modified by the specified transaction.
	 * 
	 * @param txnum the transaction's id number
	 */
	synchronized void flushAll(int txnum) {
		for (Buffer buff : bufferpool)
			if (buff.isModifiedBy(txnum))
				buff.flush();
	}

	/**
	 * Pins a buffer to the specified block. If there is already a buffer assigned
	 * to that block then that buffer is used; otherwise, an unpinned buffer from
	 * the pool is chosen. Returns a null value if there are no available buffers.
	 * 
	 * @param blk a reference to a disk block
	 * @return the pinned buffer
	 */
	synchronized Buffer pin(Block blk) {
		Buffer buff = findExistingBuffer(blk);
		if (buff == null) {
			buff = chooseUnpinnedBuffer();
			if (buff == null)
				return null;
			buff.assignToBlock(blk);
		}
		if (!buff.isPinned())
			numAvailable--;
		buff.pin();
		//Notify Listeners that the buffer has been pinned
		notifyPinEvent(buff);
		return buff;
	}

	/**
	 * Allocates a new block in the specified file, and pins a buffer to it. Returns
	 * null (without allocating the block) if there are no available buffers.
	 * 
	 * @param filename the name of the file
	 * @param fmtr     a pageformatter object, used to format the new block
	 * @return the pinned buffer
	 */
	synchronized Buffer pinNew(String filename, PageFormatter fmtr) {
		Buffer buff = chooseUnpinnedBuffer();
		if (buff == null)
			return null;
		buff.assignToNew(filename, fmtr);
		numAvailable--;
		buff.pin();
		//Notify Listeners that the buffer has been pinned
		notifyPinEvent(buff);
		return buff;
	}

	/**
	 * Unpins the specified buffer.
	 * 
	 * @param buff the buffer to be unpinned
	 */
	synchronized void unpin(Buffer buff) {
		buff.unpin();
		if (!buff.isPinned())
			numAvailable++;
		//Notify Listeners that the buffer has been unpinned
		notifyUnpinEvent(buff);
	}

	/**
	 * Returns the number of available (i.e. unpinned) buffers.
	 * 
	 * @return the number of available buffers
	 */
	int available() {
		return numAvailable;
	}

	private Buffer findExistingBuffer(Block blk) {
		for (Buffer buff : bufferpool) {
			Block b = buff.block();
			if (b != null && b.equals(blk))
				return buff;
		}
		return null;
	}

	private Buffer chooseUnpinnedBuffer() {
		// Asks the replacement strategy to give a free buffer
		if (this.unpinStrategy != null) {
			return unpinStrategy.chooseUnpinnedBuffer();
		}

		// The default choice is the Naive strategy
		for (Buffer buff : bufferpool)
			if (!buff.isPinned())
				return buff;
		return null;
	}

	private void setUnpinStrategy(ChooseUnpinnedBufferStrategy unpinStrategy) {
		this.unpinStrategy = unpinStrategy;
	}

	private void notifyPinEvent(Buffer buff) {
		for(PinUnpinListener listener : pinUnpinListeners) {
			listener.pinned(buff);
		}
	}
	
	private void notifyUnpinEvent(Buffer buff) {
		for(PinUnpinListener listener : pinUnpinListeners) {
			listener.unpinned(buff);
		}
	}

	private void instantiateStrategies() {
		PinUnpinListener lruListener = new LRUUnpinStrategy();
		PinUnpinListener clockListener = new ClockUnpinStrategy();
		PinUnpinListener fifoListener = new FIFOUnpinStrategy();
	
		pinUnpinListeners = new ArrayList<>();
		pinUnpinListeners.add(lruListener);
		pinUnpinListeners.add(clockListener);
		pinUnpinListeners.add(fifoListener);
		
		//Casting is necessary to be able to access create() methods
		LRUUnpinStrategy lruStrategy = (LRUUnpinStrategy) lruListener;
		ClockUnpinStrategy clockStrategy = (ClockUnpinStrategy) clockListener;
		FIFOUnpinStrategy fifoStrategy = (FIFOUnpinStrategy) fifoListener;
		
		lruStrategy.create(this.bufferpool);
		clockStrategy.create(this.bufferpool);
		fifoStrategy.create(this.bufferpool);
	
		//Sets the buffer replacement strategy for the buffer manager
		setUnpinStrategy(lruStrategy);
	}
}
