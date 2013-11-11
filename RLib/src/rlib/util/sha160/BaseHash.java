package rlib.util.sha160;

public abstract class BaseHash {

	protected String name;

	protected int hashSize;

	protected int blockSize;

	protected long count;

	protected byte buffer[];

	protected BaseHash(String name, int hashSize, int blockSize) {
		this.name = name;
		this.hashSize = hashSize;
		this.blockSize = blockSize;
		buffer = new byte[blockSize];
		resetContext();
	}

	public abstract Object clone();

	public byte[] digest() {
		byte tail[] = padBuffer();
		update(tail, 0, tail.length);
		byte result[] = getResult();
		reset();
		return result;
	}

	protected abstract byte[] getResult();

	public String name() {
		return name;
	}

	protected abstract byte[] padBuffer();

	public void reset() {
		count = 0L;
		for(int i = 0; i < blockSize;)
			buffer[i++] = 0;

		resetContext();
	}

	protected abstract void resetContext();

	protected abstract void transform(byte abyte0[], int i);

	public void update(byte b[], int offset, int len) {
		int n = (int) (count % (long) blockSize);
		count += len;
		int partLen = blockSize - n;
		int i = 0;
		if(len >= partLen) {
			System.arraycopy(b, offset, buffer, n, partLen);
			transform(buffer, 0);
			for(i = partLen; (i + blockSize) - 1 < len; i += blockSize)
				transform(b, offset + i);

			n = 0;
		}
		if(i < len)
			System.arraycopy(b, offset + i, buffer, n, len - i);
	}
}