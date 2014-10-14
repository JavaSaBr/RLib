package rlib.geoengine.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;

import rlib.geoengine.GeoConfig;
import rlib.geoengine.GeoMap;
import rlib.geoengine.GeoQuard;
import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.util.ArrayUtils;
import rlib.util.array.Array;

/**
 * Модель 3х мерной геокарты.
 *
 * @author Ronn
 */
public final class GeoMap3D implements GeoMap {

	private static final Logger LOGGER = LoggerManager.getLogger(GeoMap3D.class);

	/** отступ по X */
	private final int offsetX;
	/** отступ по Y */
	private final int offsetY;
	/** размер квадрата гео */
	private final int quardSize;
	/** размер гео квадрата в высоту */
	private final int quardHeight;
	/** разделитель квадратов в файле */
	private final int split;

	/** массив всех гео квадратов */
	private GeoQuard[][][] quards;

	/** кол-во квадратов в карте */
	private int size;

	public GeoMap3D(final GeoConfig config) {
		this.quardSize = config.getQuardSize();
		this.quardHeight = config.getQuardHeight();
		this.split = config.getSplit();
		this.offsetX = config.getOffsetX();
		this.offsetY = config.getOffsetY();
		this.quards = new GeoQuard[0][][];
	}

	@Override
	public void addQuard(final GeoQuard quard) {

		if(quard.getX() >= quards.length) {
			quards = ArrayUtils.copyOf(quards, quard.getX() + 1);
		}

		GeoQuard[][] yQuards = quards[quard.getX()];

		if(yQuards == null) {
			yQuards = new GeoQuard[quard.getY() + 1][];
			quards[quard.getX()] = yQuards;
		} else if(quard.getY() >= yQuards.length) {
			yQuards = ArrayUtils.copyOf(yQuards, quard.getY() + 1 - yQuards.length);
			quards[quard.getX()] = yQuards;
		}

		GeoQuard[] zQuards = yQuards[quard.getY()];

		if(zQuards == null) {
			zQuards = new GeoQuard[1];
			zQuards[0] = quard;
			yQuards[quard.getY()] = zQuards;
			size++;
		} else {

			final int z = (int) quard.getHeight() / quardHeight;

			for(int i = 0, length = zQuards.length; i < length; i++) {

				final GeoQuard target = zQuards[i];

				final int targetZ = (int) target.getHeight() / quardHeight;

				if(z == targetZ) {
					if(target.getHeight() > quard.getHeight()) {
						return;
					} else {
						zQuards[i] = quard;
						return;
					}
				}
			}

			yQuards[quard.getY()] = ArrayUtils.addToArray(zQuards, quard, GeoQuard.class);
			size++;
		}
	}

	@Override
	public void addQuard(final int x, final int y, final float height) {

		if(x >= quards.length) {
			quards = ArrayUtils.copyOf(quards, x + 1);
		}

		GeoQuard[][] yQuards = quards[x];

		if(yQuards == null) {
			yQuards = new GeoQuard[y + 1][];
			quards[x] = yQuards;
		} else if(y >= yQuards.length) {
			yQuards = ArrayUtils.copyOf(yQuards, y + 1 - yQuards.length);
			quards[x] = yQuards;
		}

		GeoQuard[] zQuards = yQuards[y];

		if(zQuards == null) {
			zQuards = new GeoQuard[1];
			zQuards[0] = new GeoQuard(x, y, height);
			yQuards[y] = zQuards;
			size++;
		} else {

			final int z = (int) height / quardHeight;

			for(int i = 0, length = zQuards.length; i < length; i++) {

				final GeoQuard target = zQuards[i];

				final int targetZ = (int) target.getHeight() / quardHeight;

				if(z == targetZ) {
					if(target.getHeight() > height) {
						return;
					} else {
						zQuards[i] = new GeoQuard(x, y, height);
						return;
					}
				}
			}

			yQuards[y] = ArrayUtils.addToArray(zQuards, new GeoQuard(x, y, height), GeoQuard.class);
			size++;
		}
	}

	@Override
	public void exportTo(final File file) {

		try(FileOutputStream out = new FileOutputStream(file)) {

			final FileChannel channel = out.getChannel();
			final ByteBuffer buffer = ByteBuffer.allocate(16).order(ByteOrder.LITTLE_ENDIAN);

			final byte split = (byte) getSplit();

			final GeoQuard[][][] quards = getQuards();

			for(int x = quards.length - 1; x >= 0; x--) {

				final GeoQuard[][] yQuards = quards[x];

				if(yQuards == null) {
					continue;
				}

				for(int y = yQuards.length - 1; y >= 0; y--) {

					final GeoQuard[] zQuards = yQuards[y];

					if(zQuards == null) {
						continue;
					}

					for(int z = zQuards.length - 1; z >= 0; z--) {

						final GeoQuard quard = zQuards[z];

						buffer.clear();
						buffer.put(split);
						buffer.putInt(quard.getX());
						buffer.putInt(quard.getY());
						buffer.putFloat(quard.getHeight());
						buffer.flip();

						channel.write(buffer);
					}
				}
			}

		} catch(final IOException e) {
			LOGGER.warning(e);
		}
	}

	@Override
	public Array<GeoQuard> getAllQuards(final Array<GeoQuard> container) {

		for(int x = 0, lengthX = quards.length; x < lengthX; x++) {

			final GeoQuard[][] yQuards = quards[x];

			if(yQuards == null) {
				continue;
			}

			for(int y = 0, lengthY = yQuards.length; y < lengthY; y++) {

				final GeoQuard[] zQuards = yQuards[y];

				if(zQuards == null) {
					continue;
				}

				for(int z = 0, lengthZ = zQuards.length; z < lengthZ; z++) {
					container.add(zQuards[z]);
				}
			}
		}

		return container;
	}

	@Override
	public GeoQuard getGeoQuard(final float x, final float y, final float z) {

		final int newX = toIndex(x) + offsetX;
		final int newY = toIndex(y) + offsetY;

		GeoQuard[] quards = getQuards(newX, newY);

		if(quards == null) {
			for(int i = -2; i <= 2; i++) {
				for(int j = -2; j <= 2; j++) {

					if(i == 0 && j == 0) {
						continue;
					}

					quards = getQuards(Math.max(newX + i, 0), Math.max(newY + j, 0));

					if(quards != null) {
						break;
					}
				}
			}
		}

		if(quards == null) {
			return null;
		} else if(quards.length == 1) {
			return quards[0];
		} else {

			GeoQuard target = null;

			float min = 0;

			for(int i = 0, length = quards.length; i < length; i++) {

				final GeoQuard quard = quards[i];

				if(target == null) {
					target = quard;
					min = Math.abs(quard.getHeight() - z);
					continue;
				}

				final float diff = Math.abs(quard.getHeight() - z);

				if(diff < min) {
					min = diff;
					target = quard;
				}
			}

			return target;
		}
	}

	@Override
	public float getHeight(final float x, final float y, final float z) {

		final GeoQuard quard = getGeoQuard(x, y, z);

		if(quard == null) {
			return z;
		}

		return Math.abs(quard.getHeight() - z) > quardHeight ? z : quard.getHeight();
	}

	private GeoQuard[][][] getQuards() {
		return quards;
	}

	/**
	 * Получение массива квадратов по X и Y.
	 *
	 * @param x индекс квадрата по X.
	 * @param y индекс квадрата по Y.
	 * @return массив квадратов.
	 */
	public GeoQuard[] getQuards(final int x, final int y) {

		if(quards.length <= x) {
			return null;
		}

		final GeoQuard[][] yQuards = quards[x];

		if(yQuards == null || yQuards.length <= y) {
			return null;
		}

		return yQuards[y];
	}

	/**
	 * @return разделитель квадратов в файле.
	 */
	private int getSplit() {
		return split;
	}

	@Override
	public GeoMap3D importTo(final File file) {

		try(FileInputStream in = new FileInputStream(file)) {

			final FileChannel channel = in.getChannel();

			if(channel.size() < 100000000) {

				if(LOGGER.isEnabledInfo()) {
					LOGGER.info("start fast import " + file.getName());
				}

				final ByteBuffer buffer = ByteBuffer.allocate((int) channel.size()).order(ByteOrder.LITTLE_ENDIAN);

				final byte split = (byte) getSplit();

				buffer.clear();
				channel.read(buffer);
				buffer.flip();

				while(buffer.remaining() > 12) {

					final byte val = buffer.get();

					if(val != split) {
						LOGGER.warning("incorrect import file.");
						break;
					}

					final GeoQuard quard = new GeoQuard(buffer.getInt(), buffer.getInt(), buffer.getFloat());

					if(quard.getX() < 0 || quard.getY() < 0) {
						LOGGER.warning("incorrect quard " + quard);
						continue;
					}

					addQuard(quard);
				}

			} else {

				if(LOGGER.isEnabledInfo()) {
					LOGGER.info("start slow import " + file.getName());
				}

				final ByteBuffer buffer = ByteBuffer.allocate(13).order(ByteOrder.LITTLE_ENDIAN);

				final byte split = (byte) getSplit();

				while(channel.size() - channel.position() > 12) {

					buffer.clear();
					channel.read(buffer);
					buffer.flip();

					final byte val = buffer.get();

					if(val != split) {
						LOGGER.warning("incorrect import file.");
						break;
					}

					final GeoQuard quard = new GeoQuard(buffer.getInt(), buffer.getInt(), buffer.getFloat());

					if(quard.getX() < 0 || quard.getY() < 0) {
						LOGGER.warning("incorrect quard " + quard);
						continue;
					}

					addQuard(quard);
				}
			}

		} catch(final Exception e) {
			LOGGER.warning(e);
		}

		if(LOGGER.isEnabledInfo()) {
			LOGGER.info("import ended. load " + size() + " quards.");
		}

		return this;
	}

	/**
	 * @param quard удаляемый квадрат.
	 */
	public void remove(final GeoQuard quard) {

		if(quards.length <= quard.getX()) {
			return;
		}

		final GeoQuard[][] yQuards = quards[quard.getX()];

		if(yQuards == null || yQuards.length <= quard.getY()) {
			return;
		}

		final GeoQuard[] quards = yQuards[quard.getY()];

		if(quards == null || quards.length < 2) {
			yQuards[quard.getY()] = null;
		} else {

			final GeoQuard[] result = new GeoQuard[quards.length - 1];

			for(int i = 0, j = 0, length = quards.length; i < length; i++) {

				final GeoQuard item = quards[i];

				if(item == quard) {
					continue;
				}

				result[j++] = item;
			}

			yQuards[quard.getY()] = result;
		}
	}

	@Override
	public int size() {
		return size;
	}

	/**
	 * @param coord координата.
	 * @return индекс для той координаты.
	 */
	private int toIndex(final float coord) {
		return (int) coord / quardSize;
	}
}
