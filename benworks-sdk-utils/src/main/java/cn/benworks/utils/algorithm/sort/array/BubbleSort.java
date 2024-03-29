package cn.benworks.utils.algorithm.sort.array;

public class BubbleSort implements SortUtil.Sort {

	/**
	 * (non-Javadoc)
	 * @see cn.benworks.utils.algorithm.sort.array.SortUtil.Sort#sort(int[])
	 */

	public void sort(int[] data) {
		for (int i = 0; i < data.length; i++) {
			for (int j = data.length - 1; j > i; j--) {
				if (data[j] < data[j - 1]) {
					SortUtil.swap(data, j, j - 1);
				}
			}
		}
	}
}
