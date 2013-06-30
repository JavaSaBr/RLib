package rlib.geoengine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;

import rlib.logging.Loggers;
import rlib.util.array.Array;
import rlib.util.array.Arrays;


/**
 * Модель 3х мерной геокарты.
 *
 * @author Ronn
 */
public final class GeoMap3D implements GeoMap
{
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

	public GeoMap3D(GeoConfig config)
	{
		this.quardSize = config.getQuardSize();
		this.quardHeight = config.getQuardHeight();
		this.split = config.getSplit();
		this.offsetX = config.getOffsetX();
		this.offsetY = config.getOffsetY();
		this.quards = new GeoQuard[0][][];
	}

	@Override
	public void addQuard(GeoQuard quard)
	{
		// если по X для квадрата нет места
		if(quard.getX() >= quards.length)
			// расширяем массив
			quards = Arrays.copyOf(quards, quard.getX() + 1);

		// получаем массив по Y
		GeoQuard[][] yQuards = quards[quard.getX()];

		// если его нет
		if(yQuards == null)
		{
			// создаем
			yQuards = new GeoQuard[quard.getY() + 1][];
			// вносим
			quards[quard.getX()] = yQuards;
		}
		// если в нем нет места
		else if(quard.getY() >= yQuards.length)
		{
			// расширяем
			yQuards = Arrays.copyOf(yQuards, quard.getY() + 1 - yQuards.length);
			// обновляем
			quards[quard.getX()] = yQuards;
		}

		// получаем массив по Z
		GeoQuard[] zQuards = yQuards[quard.getY()];

		// если массива нет
		if(zQuards == null)
		{
			// создаем новый
			zQuards = new GeoQuard[1];
			// вносим в него новый квадрат
			zQuards[0] = quard;
			// обновляем в массиве по Y
			yQuards[quard.getY()] = zQuards;
			// увеличиваем счетчик размера
			size++;
		}
		else
		{
			// рассчитываем Z индекс
			int z = ((int) quard.getHeight()) / quardHeight;

			// смотрим есть ли уже с таким индексом квадрат
			for(int i = 0, length = zQuards.length; i < length; i++)
			{
				// получаем уже имеющийся квадраwт
				GeoQuard target = zQuards[i];

				// расчитываем его индекс по Z
				int targetZ = ((int) target.getHeight()) / quardHeight;

				//System.out.println("[" + z + "]  new " + quard + " [" + targetZ + "] old " + target);

				// если находим с таким же индексом
				if(z == targetZ)
				{
					// если он вышел, его и оставляем
					if(target.getHeight() > quard.getHeight())
						return;
					// иначе
					else
					{
						// заменяе его на новый
						zQuards[i] = quard;
						return;
					}
				}
			}

			// если с таким индексом квадратов небыло, добавляем в массив новый
			yQuards[quard.getY()] = Arrays.addToArray(zQuards, quard, GeoQuard.class);

			// увеличиваем счетчик размера
			size++;
		}
	}

	private GeoQuard[][][] getQuards()
	{
		return quards;
	}
	
	@Override
	public void exportTo(File file)
	{
		try(FileOutputStream out = new FileOutputStream(file))
		{
			// получаем канал файла
			FileChannel channel = out.getChannel();

			// создаем буфер для записи
			ByteBuffer buffer = ByteBuffer.allocate(16).order(ByteOrder.LITTLE_ENDIAN);

			// получаем разделитель
			byte split = (byte) getSplit();

			GeoQuard[][][] quards = getQuards();
			
			// перебираем все квадраты
			for(int x = quards.length - 1; x >= 0; x--)
			{
				GeoQuard[][] yQuards = quards[x];

				if(yQuards == null)
					continue;

				for(int y = yQuards.length - 1; y >= 0; y--)
				{
					GeoQuard[] zQuards = yQuards[y];

					if(zQuards == null)
						continue;

					for(int z = zQuards.length - 1; z >= 0; z--)
					{
						// получаем квадрат
						GeoQuard quard = zQuards[z];

						// очищаем буффер
						buffer.clear();

						// вносим квадрат
						buffer.put(split);
						buffer.putInt(quard.getX());
						buffer.putInt(quard.getY());
						buffer.putFloat(quard.getHeight());

						// подготавливаем к записи буфер
						buffer.flip();

						// записываем
						channel.write(buffer);
					}
				}
			}
		}
		catch(IOException e)
		{
			Loggers.warning(this, e);
		}
	}

	@Override
	public Array<GeoQuard> getAllQuards(Array<GeoQuard> container)
	{
		// перебираем все квадраты
		for(int x = 0, lengthX = quards.length; x < lengthX; x++)
		{
			GeoQuard[][] yQuards = quards[x];

			if(yQuards == null)
				continue;

			for(int y = 0, lengthY = yQuards.length; y < lengthY; y++)
			{
				GeoQuard[] zQuards = yQuards[y];

				if(zQuards == null)
					continue;

				for(int z = 0, lengthZ = zQuards.length; z < lengthZ; z++)
					container.add(zQuards[z]);
			}
		}

		return container;
	}

	@Override
	public GeoQuard getGeoQuard(float x, float y, float z)
	{
		int newX = toIndex(x) + offsetX;
		int newY = toIndex(y) + offsetY;

		// получаем массив квадратов по Z
		GeoQuard[] quards = getQuards(newX, newY);

		// если нет квадратов, пробуем найти соседние
		if(quards == null)
		{
			// перебираем соседние индексы
			for(int i = -2; i <= 2; i ++)
				for(int j = -2; j <= 2; j++)
				{
					// если это не найденый, пропускаем
					if(i == 0 && j == 0)
						continue;

					// смотрим наличие соседних
					quards = getQuards(Math.max(newX + i, 0), Math.max(newY + j, 0));

					// если нашли, выходим
					if(quards != null)
						break;
				}
		}

		// если их нет, вовращаем принятую Z координату
		if(quards == null)
			return null;
		// если там 1 квадрат, его и возвращаем
		else if(quards.length == 1)
			return quards[0];
		else
		{
			// целевой квадрат
			GeoQuard target = null;

			// минимальное расхождение с принятым Z
			float min = 0;

			// перебираем квадраты
			for(int i = 0, length = quards.length; i < length; i++)
			{
				// получаем квадрат
				GeoQuard quard = quards[i];

				// если еще не один квадрат не брался
				if(target == null)
				{
					// берем этот
					target = quard;
					// рассчитываем расхождение с присланным Z
					min = Math.abs(quard.getHeight() - z);
					continue;
				}

				// рассчитываем расхождение
				float diff = Math.abs(quard.getHeight() - z);

				// если оно меньше текущего минимального
				if(diff < min)
				{
					// обновляем
					min = diff;
					// обновляем квадрат
					target = quard;
				}
			}

			// возвращаем высоту самого подходящего квадрата
			return target;
		}
	}

	@Override
	public float getHeight(float x, float y, float z)
	{
		// получаем гео квадрат
		GeoQuard quard = getGeoQuard(x, y, z);

		// если квадрата нет
		if(quard == null)
			return z;

		// если квадрат не относится к этому уровню, возвращаем изначальную высоту
		return Math.abs(quard.getHeight() - z) > quardHeight? z : quard.getHeight();
	}

	/**
	 * Получение массива квадратов по X и Y.
	 *
	 * @param x индекс квадрата по X.
	 * @param y индекс квадрата по Y.
	 * @return массив квадратов.
	 */
	public GeoQuard[] getQuards(int x, int y)
	{
		// если нет по такому X
		if(quards.length <= x)
			return null;

		// получаем массив квадратов по Y
		GeoQuard[][] yQuards = quards[x];

		// если нет массива или же в нем нет квадратов по такому Y
		if(yQuards == null || yQuards.length <= y)
			return null;

		// возвращаем массив квадратов по такому X и Y
		return yQuards[y];
	}

	/**
	 * @return разделитель квадратов в файле.
	 */
	private int getSplit()
	{
		return split;
	}

	@Override
	public GeoMap3D importTo(File file)
	{
		try(FileInputStream in = new FileInputStream(file))
		{
			// получаем канал файла
			FileChannel channel = in.getChannel();

			if(channel.size() < 100000000)
			{
				Loggers.info(this, "start fast import " + file.getName());

				// создаем буфер для чтения
				ByteBuffer buffer = ByteBuffer.allocate((int) channel.size()).order(ByteOrder.LITTLE_ENDIAN);

				// получаем разделитель квадратов
				byte split = (byte) getSplit();

				// очищаем буфер
				buffer.clear();

				// читаем в буфер
				channel.read(buffer);
				
				// подготавливаем дял обработки
				buffer.flip();
				
				while(buffer.remaining() > 12)
				{
					// получаем разделить
					byte val = buffer.get();

					// если не подходит, пропускаем
					if(val != split)
					{
						Loggers.warning(this, "incorrect import file.");
						break;
					}

					// читаем квадрат
					GeoQuard quard = new GeoQuard(buffer.getInt(), buffer.getInt(), buffer.getFloat());

					// если некорректный квадрат, то пропускам его
					if(quard.getX() < 0 || quard.getY() < 0)
					{
						Loggers.warning(this, "incorrect quard " + quard);
						continue;
					}
					
					// добавляем
					addQuard(quard);
				}
			}
			else
			{
				Loggers.info(this, "start slow import " + file.getName());
				
				// создаем буфер для чтения
				ByteBuffer buffer = ByteBuffer.allocate(13).order(ByteOrder.LITTLE_ENDIAN);

				// получаем разделитель квадратов
				byte split = (byte) getSplit();

				// читаем файл
				while(channel.size() - channel.position() > 12)
				{
					// очищаем буфер
					buffer.clear();

					// читаем в буфер
					channel.read(buffer);

					// подготавливаем дял обработки
					buffer.flip();

					// получаем разделить
					byte val = buffer.get();

					// если не подходит, пропускаем
					if(val != split)
					{
						Loggers.warning(this, "incorrect import file.");
						break;
					}

					// читаем квадрат
					GeoQuard quard = new GeoQuard(buffer.getInt(), buffer.getInt(), buffer.getFloat());

					// если некорректный квадрат, то пропускам его
					if(quard.getX() < 0 || quard.getY() < 0)
					{
						Loggers.warning(this, "incorrect quard " + quard);
						continue;
					}
					
					// добавляем
					addQuard(quard);
				}
			}
		}
		catch(Exception e)
		{
			Loggers.warning(this, e);
		}

		Loggers.info(this, "import ended. load " + size() + " quards.");

		return this;
	}

	/**
	 * @param quard удаляемый квадрат.
	 */
	public void remove(GeoQuard quard)
	{
		// если нет по такому X
		if(quards.length <= quard.getX())
			return;

		// получаем массив квадратов по Y
		GeoQuard[][] yQuards = quards[quard.getX()];

		// если нет массива или же в нем нет квадратов по такому Y
		if(yQuards == null || yQuards.length <= quard.getY())
			return;

		// возвращаем массив квадратов по такому X и Y
		GeoQuard[] quards =  yQuards[quard.getY()];
		
		// если это последний квадрат, зануляем
		if(quards == null || quards.length < 2)
			yQuards[quard.getY()] = null;
		else
		{
			// создаем массив на 1 элемент меньше
			GeoQuard[] result = new GeoQuard[quards.length - 1];
			
			// переносим туда оставшиеся квадраты
			for(int i = 0, j = 0, length = quards.length; i < length; i++)
			{
				GeoQuard item = quards[i];
				
				if(item == quard)
					continue;
				
				result[j++] = item;
			}
			
			// заменяем массив
			yQuards[quard.getY()] = result;
		}
	}

	@Override
	public int size()
	{
		return size;
	}
	
	/**
	 * @param coord координата.
	 * @return индекс для той координаты.
	 */
	private int toIndex(float coord)
	{
		return (int) coord / quardSize;
	}
	
	@Override
	public void addQuard(int x, int y, float height)
	{
		// если по X для квадрата нет места
		if(x >= quards.length)
			// расширяем массив
			quards = Arrays.copyOf(quards, x + 1);

		// получаем массив по Y
		GeoQuard[][] yQuards = quards[x];

		// если его нет
		if(yQuards == null)
		{
			// создаем
			yQuards = new GeoQuard[y + 1][];
			// вносим
			quards[x] = yQuards;
		}
		// если в нем нет места
		else if(y >= yQuards.length)
		{
			// расширяем
			yQuards = Arrays.copyOf(yQuards, y + 1 - yQuards.length);
			// обновляем
			quards[x] = yQuards;
		}

		// получаем массив по Z
		GeoQuard[] zQuards = yQuards[y];

		// если массива нет
		if(zQuards == null)
		{
			// создаем новый
			zQuards = new GeoQuard[1];
			// вносим в него новый квадрат
			zQuards[0] = new GeoQuard(x, y, height);
			// обновляем в массиве по Y
			yQuards[y] = zQuards;
			// увеличиваем счетчик размера
			size++;
		}
		else
		{
			// рассчитываем Z индекс
			int z = ((int) height) / quardHeight;

			// смотрим есть ли уже с таким индексом квадрат
			for(int i = 0, length = zQuards.length; i < length; i++)
			{
				// получаем уже имеющийся квадраwт
				GeoQuard target = zQuards[i];

				// расчитываем его индекс по Z
				int targetZ = ((int) target.getHeight()) / quardHeight;

				// если находим с таким же индексом
				if(z == targetZ)
				{
					// если он вышел, его и оставляем
					if(target.getHeight() > height)
						return;
					// иначе
					else
					{
						// заменяе его на новый
						zQuards[i] = new GeoQuard(x, y, height);
						return;
					}
				}
			}

			// если с таким индексом квадратов небыло, добавляем в массив новый
			yQuards[y] = Arrays.addToArray(zQuards, new GeoQuard(x, y, height), GeoQuard.class);

			// увеличиваем счетчик размера
			size++;
		}
	}
}
