package cn.benworks.utils.algorithm.sort.array;

/**
 * 插入排序
 * @author Ben
 */
public class InsertSort implements SortUtil.Sort {
	/**
	 * (non-Javadoc)
	 * @see cn.benworks.utils.algorithm.sort.array.SortUtil.Sort#sort(int[])
	 */
	public void sort(int[] data) {
		// int temp;
		for (int i = 1; i < data.length; i++) {
			for (int j = i; (j > 0) && (data[j] < data[j - 1]); j--) {
				SortUtil.swap(data, j, j - 1);
			}
		}
	}

}