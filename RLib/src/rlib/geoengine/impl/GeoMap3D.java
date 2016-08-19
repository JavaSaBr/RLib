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
import rlib.geoengine.GeoQuad;
import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.util.ArrayUtils;
import rlib.util.array.Array;

/**
 * Модель 3х мерной геокарты.
 *
 * @author JavaSaBr
 */
public final class GeoMap3D implements GeoMap {

    private static final Logger LOGGER = LoggerManager.getLogger(GeoMap3D.class);

    /**
     * Отступ по X.
     */
    private final int offsetX;

    /**
     * Отступ по Y.
     */
    private final int offsetY;

    /**
     * Размер квадрата гео.
     */
    private final int quadSize;

    /**
     * Размер гео квадрата в высоту.
     */
    private final int quadHeight;

    /**
     * Разделитель квадратов в файле.
     */
    private final int split;

    /**
     * Массив всех гео квадратов.
     */
    private GeoQuad[][][] quads;

    /**
     * Кол-во квадратов в карте.
     */
    private int size;

    public GeoMap3D(final GeoConfig config) {
        this.quadSize = config.getQuadSize();
        this.quadHeight = config.getQuadHeight();
        this.split = config.getSplit();
        this.offsetX = config.getOffsetX();
        this.offsetY = config.getOffsetY();
        this.quads = new GeoQuad[0][][];
    }

    @Override
    public void addQuad(final GeoQuad quad) {

        if (quad.getX() >= quads.length) {
            quads = ArrayUtils.copyOf(quads, quad.getX() + 1);
        }

        GeoQuad[][] yQuads = quads[quad.getX()];

        if (yQuads == null) {
            yQuads = new GeoQuad[quad.getY() + 1][];
            quads[quad.getX()] = yQuads;
        } else if (quad.getY() >= yQuads.length) {
            yQuads = ArrayUtils.copyOf(yQuads, quad.getY() + 1 - yQuads.length);
            quads[quad.getX()] = yQuads;
        }

        GeoQuad[] zQuads = yQuads[quad.getY()];

        if (zQuads == null) {
            zQuads = new GeoQuad[1];
            zQuads[0] = quad;
            yQuads[quad.getY()] = zQuads;
            size++;
        } else {

            final int z = (int) quad.getHeight() / quadHeight;

            for (int i = 0, length = zQuads.length; i < length; i++) {

                final GeoQuad target = zQuads[i];

                final int targetZ = (int) target.getHeight() / quadHeight;

                if (z == targetZ) {
                    if (target.getHeight() > quad.getHeight()) {
                        return;
                    } else {
                        zQuads[i] = quad;
                        return;
                    }
                }
            }

            yQuads[quad.getY()] = ArrayUtils.addToArray(zQuads, quad, GeoQuad.class);
            size++;
        }
    }

    @Override
    public void addQuad(final int x, final int y, final float height) {

        if (x >= quads.length) {
            quads = ArrayUtils.copyOf(quads, x + 1);
        }

        GeoQuad[][] yQuads = quads[x];

        if (yQuads == null) {
            yQuads = new GeoQuad[y + 1][];
            quads[x] = yQuads;
        } else if (y >= yQuads.length) {
            yQuads = ArrayUtils.copyOf(yQuads, y + 1 - yQuads.length);
            quads[x] = yQuads;
        }

        GeoQuad[] zQuads = yQuads[y];

        if (zQuads == null) {
            zQuads = new GeoQuad[1];
            zQuads[0] = new GeoQuad(x, y, height);
            yQuads[y] = zQuads;
            size++;
        } else {

            final int z = (int) height / quadHeight;

            for (int i = 0, length = zQuads.length; i < length; i++) {

                final GeoQuad target = zQuads[i];

                final int targetZ = (int) target.getHeight() / quadHeight;

                if (z == targetZ) {
                    if (target.getHeight() > height) {
                        return;
                    } else {
                        zQuads[i] = new GeoQuad(x, y, height);
                        return;
                    }
                }
            }

            yQuads[y] = ArrayUtils.addToArray(zQuads, new GeoQuad(x, y, height), GeoQuad.class);
            size++;
        }
    }

    @Override
    public void exportTo(final File file) {

        try (FileOutputStream out = new FileOutputStream(file)) {

            final FileChannel channel = out.getChannel();
            final ByteBuffer buffer = ByteBuffer.allocate(16).order(ByteOrder.LITTLE_ENDIAN);

            final byte split = (byte) getSplit();

            final GeoQuad[][][] quads = getQuads();

            for (int x = quads.length - 1; x >= 0; x--) {

                final GeoQuad[][] yQuads = quads[x];

                if (yQuads == null) {
                    continue;
                }

                for (int y = yQuads.length - 1; y >= 0; y--) {

                    final GeoQuad[] zQuads = yQuads[y];

                    if (zQuads == null) {
                        continue;
                    }

                    for (int z = zQuads.length - 1; z >= 0; z--) {

                        final GeoQuad quad = zQuads[z];

                        buffer.clear();
                        buffer.put(split);
                        buffer.putInt(quad.getX());
                        buffer.putInt(quad.getY());
                        buffer.putFloat(quad.getHeight());
                        buffer.flip();

                        channel.write(buffer);
                    }
                }
            }

        } catch (final IOException e) {
            LOGGER.warning(e);
        }
    }

    @Override
    public Array<GeoQuad> getAllQuads(final Array<GeoQuad> container) {

        for (final GeoQuad[][] yQuads : quads) {

            if (yQuads == null) {
                continue;
            }

            for (final GeoQuad[] zQuads : yQuads) {

                if (zQuads == null) {
                    continue;
                }

                for (final GeoQuad zQuad : zQuads) {
                    container.add(zQuad);
                }
            }
        }

        return container;
    }

    @Override
    public GeoQuad getGeoQuad(final float x, final float y, final float z) {

        final int newX = toIndex(x) + offsetX;
        final int newY = toIndex(y) + offsetY;

        GeoQuad[] quads = getQuads(newX, newY);

        if (quads == null) {
            for (int i = -2; i <= 2; i++) {
                for (int j = -2; j <= 2; j++) {

                    if (i == 0 && j == 0) {
                        continue;
                    }

                    quads = getQuads(Math.max(newX + i, 0), Math.max(newY + j, 0));

                    if (quads != null) {
                        break;
                    }
                }
            }
        }

        if (quads == null) {
            return null;
        } else if (quads.length == 1) {
            return quads[0];
        } else {

            GeoQuad target = null;

            float min = 0;

            for (final GeoQuad quad : quads) {

                if (target == null) {
                    target = quad;
                    min = Math.abs(quad.getHeight() - z);
                    continue;
                }

                final float diff = Math.abs(quad.getHeight() - z);

                if (diff < min) {
                    min = diff;
                    target = quad;
                }
            }

            return target;
        }
    }

    @Override
    public float getHeight(final float x, final float y, final float z) {

        final GeoQuad quad = getGeoQuad(x, y, z);

        if (quad == null) {
            return z;
        }

        return Math.abs(quad.getHeight() - z) > quadHeight ? z : quad.getHeight();
    }

    private GeoQuad[][][] getQuads() {
        return quads;
    }

    /**
     * Получение массива квадратов по X и Y.
     *
     * @param x индекс квадрата по X.
     * @param y индекс квадрата по Y.
     * @return массив квадратов.
     */
    public GeoQuad[] getQuads(final int x, final int y) {

        if (quads.length <= x) {
            return null;
        }

        final GeoQuad[][] yQuads = quads[x];

        if (yQuads == null || yQuads.length <= y) {
            return null;
        }

        return yQuads[y];
    }

    /**
     * @return разделитель квадратов в файле.
     */
    private int getSplit() {
        return split;
    }

    @Override
    public GeoMap3D importTo(final File file) {

        try (FileInputStream in = new FileInputStream(file)) {

            final FileChannel channel = in.getChannel();

            if (channel.size() < 100000000) {

                if (LOGGER.isEnabledInfo()) {
                    LOGGER.info("start fast import " + file.getName());
                }

                final ByteBuffer buffer = ByteBuffer.allocate((int) channel.size()).order(ByteOrder.LITTLE_ENDIAN);

                final byte split = (byte) getSplit();

                buffer.clear();
                channel.read(buffer);
                buffer.flip();

                while (buffer.remaining() > 12) {

                    final byte val = buffer.get();

                    if (val != split) {
                        LOGGER.warning("incorrect import file.");
                        break;
                    }

                    final GeoQuad quard = new GeoQuad(buffer.getInt(), buffer.getInt(), buffer.getFloat());

                    if (quard.getX() < 0 || quard.getY() < 0) {
                        LOGGER.warning("incorrect quard " + quard);
                        continue;
                    }

                    addQuad(quard);
                }

            } else {

                if (LOGGER.isEnabledInfo()) {
                    LOGGER.info("start slow import " + file.getName());
                }

                final ByteBuffer buffer = ByteBuffer.allocate(13).order(ByteOrder.LITTLE_ENDIAN);

                final byte split = (byte) getSplit();

                while (channel.size() - channel.position() > 12) {

                    buffer.clear();
                    channel.read(buffer);
                    buffer.flip();

                    final byte val = buffer.get();

                    if (val != split) {
                        LOGGER.warning("incorrect import file.");
                        break;
                    }

                    final GeoQuad quard = new GeoQuad(buffer.getInt(), buffer.getInt(), buffer.getFloat());

                    if (quard.getX() < 0 || quard.getY() < 0) {
                        LOGGER.warning("incorrect quard " + quard);
                        continue;
                    }

                    addQuad(quard);
                }
            }

        } catch (final Exception e) {
            LOGGER.warning(e);
        }

        if (LOGGER.isEnabledInfo()) {
            LOGGER.info("import ended. load " + size() + " quads.");
        }

        return this;
    }

    /**
     * @param quad удаляемый квадрат.
     */
    public void remove(final GeoQuad quad) {

        if (quads.length <= quad.getX()) {
            return;
        }

        final GeoQuad[][] yQuads = quads[quad.getX()];

        if (yQuads == null || yQuads.length <= quad.getY()) {
            return;
        }

        final GeoQuad[] quads = yQuads[quad.getY()];

        if (quads == null || quads.length < 2) {
            yQuads[quad.getY()] = null;
        } else {

            final GeoQuad[] result = new GeoQuad[quads.length - 1];

            for (int i = 0, j = 0, length = quads.length; i < length; i++) {

                final GeoQuad item = quads[i];

                if (item == quad) {
                    continue;
                }

                result[j++] = item;
            }

            yQuads[quad.getY()] = result;
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
        return (int) coord / quadSize;
    }
}
