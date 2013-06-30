package rlib.geom;

/**
 * Модель матрицы.
 * 
 * @author Ronn
 */
public final class Matrix3f
{
	public static final Matrix3f ZERO = new Matrix3f(0, 0, 0, 0, 0, 0, 0, 0, 0);
	
	public static final Matrix3f IDENTITY = new Matrix3f();

	public static Matrix3f newInstance(float val_0_0, float val_0_1, float val_0_2, float val_1_0, float val_1_1, float val_1_2, float val_2_0, float val_2_1, float val_2_2)
	{
		return new Matrix3f(val_0_0, val_0_1, val_0_2, val_1_0, val_1_1, val_1_2, val_2_0, val_2_1, val_2_2);
	}
	
	public static Matrix3f newInstance()
	{
		return new Matrix3f();
	}
	
	/** значения матрицы */
	protected float val_0_0, val_0_1, val_0_2;
	protected float val_1_0, val_1_1, val_1_2;
	protected float val_2_0, val_2_1, val_2_2;
	
	private Matrix3f(float val_0_0, float val_0_1, float val_0_2, float val_1_0, float val_1_1, float val_1_2, float val_2_0, float val_2_1, float val_2_2)
	{
		this.val_0_0 = val_0_0;
		this.val_0_1 = val_0_1;
		this.val_0_2 = val_0_2;
		this.val_1_0 = val_1_0;
		this.val_1_1 = val_1_1;
		this.val_1_2 = val_1_2;
		this.val_2_0 = val_2_0;
		this.val_2_1 = val_2_1;
		this.val_2_2 = val_2_2;
	}

	public Matrix3f()
	{
		val_0_1 = val_0_2 = val_1_0 = val_1_2 = val_2_0 = val_2_1 = 0;
		val_0_0 = val_1_1 = val_2_2 = 1;
	}
	
	/**
     * Приведение всех значений матрицы в абсолютные.
     */
    public void absoluteLocal() 
    {
    	val_0_0 = Math.abs(val_0_0);
    	val_0_1 = Math.abs(val_0_1);
    	val_0_2 = Math.abs(val_0_2);
    	val_1_0 = Math.abs(val_1_0);
    	val_1_1 = Math.abs(val_1_1);
    	val_1_2 = Math.abs(val_1_2);
    	val_2_0 = Math.abs(val_2_0);
    	val_2_1 = Math.abs(val_2_1);
    	val_2_2 = Math.abs(val_2_2);
    }
	
	/**
     * Приминение разворота матрицы на вектор.
     * 
     * @param vector изначальный вектор.
     * @param result контейнер результата.
     * @return результат приминения матрицы.
     */
    public Vector mult(Vector vector, Vector result)
    {
    	// получаем значения начального вектора
        float x = vector.x;
        float y = vector.y;
        float z = vector.z;

        // получаем значения матрицы
        // применяем матрицу и записываем результат
        result.x = val_0_0 * x + val_0_1 * y + val_0_2 * z;
        result.y = val_1_0 * x + val_1_1 * y + val_1_2 * z;
        result.z = val_2_0 * x + val_2_1 * y + val_2_2 * z;
        
        // возвращаем результат
        return result;
    }
    
    /**
	 * Формирование матрицы в соответствии с указанным разворотом.
	 * 
	 * @param rotation целевой разворот.
	 * @return соответствующая матрица.
	 */
    public Matrix3f set(Rotation rotation) 
    {
        return rotation.toRotationMatrix(this);
    }
    
    public void set(float val_0_0, float val_0_1, float val_0_2, float val_1_0, float val_1_1, float val_1_2, float val_2_0, float val_2_1, float val_2_2)
	{
		this.val_0_0 = val_0_0;
		this.val_0_1 = val_0_1;
		this.val_0_2 = val_0_2;
		this.val_1_0 = val_1_0;
		this.val_1_1 = val_1_1;
		this.val_1_2 = val_1_2;
		this.val_2_0 = val_2_0;
		this.val_2_1 = val_2_1;
		this.val_2_2 = val_2_2;
	}
}
