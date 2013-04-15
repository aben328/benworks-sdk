package cn.benworks.utils.algorithm.sort.array;

/**
 * 选择排序
 * @author Ben
 */
public class SelectionSort implements SortUtil.Sort {

	/**
	 * (non-Javadoc)
	 * @see cn.benworks.utils.algorithm.sort.array.SortUtil.Sort#sort(int[])
	 */
	public void sort(int[] data) {
		// int temp;
		for (int i = 0; i < data.length; i++) {
			int lowIndex = i;
			for (int j = data.length - 1; j > i; j--) {
				if (data[j] < data[lowIndex]) {
					lowIndex = j;
				}
			}
			SortUtil.swap(data, i, lowIndex);
		}

	}

}
