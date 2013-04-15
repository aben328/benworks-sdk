package cn.benworks.utils.algorithm.sort.object;

/**
 * 可以对对象进行排序
 * @author Ben
 * @param <E>
 */
public abstract class Sorter<E extends Comparable<E>> {

	public abstract void sort(E[] array, int from, int len);

	public final void sort(E[] array) {
		sort(array, 0, array.length);
	}

	protected final void swap(E[] array, int from, int to) {
		E tmp = array[from];
		array[from] = array[to];
		array[to] = tmp;
	}

}