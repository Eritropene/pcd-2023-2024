package main.java.pcd.lab01.ex01;

import java.util.*;

public class ThreadedSort {

	static final int VECTOR_SIZE = 200000000;
    public static List<SortUnit> threads;
    // static final ThreadedSort ts = new ThreadedSort();
	static {
        threads = new ArrayList<>();
    }
	public static void main(String[] args) {
	
		log("Generating array...");
		long[] v = genArray(VECTOR_SIZE);
		
		log("Array generated.");
		log("Sorting (" + VECTOR_SIZE + " elements)...");
	
        int avCores = Runtime.getRuntime().availableProcessors();
        int mergeTimes = 0;
        int cores = 1;
        for (; cores <= avCores; mergeTimes++) {
            cores *= 2;
        }
        cores /= 2;
        mergeTimes--;

		long t0 = System.nanoTime();
        for (int i=0; i<cores; i++) {
            // System.out.println(end);
            threads.add(new SortUnit(v, i, cores));
        }
        threads.forEach(SortUnit::start);
        threads.forEach(t -> {try {
            t.join();
        } catch (InterruptedException e) {
            // TODO: handle exception
        }  } );

		long t1 = System.nanoTime();
		log("Done. Time elapsed: " + ((t1 - t0) / 1000000) + " ms");
		
		// dumpArray(v);
	}


	private static long[] genArray(int n) {
		Random gen = new Random(System.currentTimeMillis());
		long v[] = new long[n];
		for (int i = 0; i < v.length; i++) {
			v[i] = gen.nextLong();
		}
		return v;
	}

	private static void dumpArray(long[] v) {
		for (long l:  v) {
			System.out.print(l + " ");
		}
	}

	private static void log(String msg) {
		System.out.println(msg);
	}

    private static class SortUnit extends Thread {

        public int start;
        public int end;

        public SortUnit(int start, int end, Runnable action) {
            super(action);
            this.start = start;
            this.end = end;
        }
        public SortUnit(long[] v, int id, int cores) {
            // int start = (id*v.length + cores - 1) / cores;
            // int end = ((id+1)*v.length + cores - 1) / cores;
            this((id*v.length + cores - 1) / cores, ((id+1)*v.length + cores - 1) / cores, () -> {
                Arrays.sort(v, (id*v.length + cores - 1) / cores, ((id+1)*v.length + cores - 1) / cores);

                for (int k = 1; k<cores; k*=2) {
                    if (id % (k*2) == 0) {
                        int otherId = id + k;
                        var thisT = threads.get(id);
                        var otherT = threads.get(otherId);
                        try {
                            System.out.println("Waiting... k = " + k);
                            otherT.join();
                        } catch (InterruptedException e) {
                            // TODO: handle exception
                        }
                        merge(v, thisT.start, thisT.end, otherT.end);
                    }
                }
            });
        }
        
    }

	static void merge(long arr[], int start, int mid,
                      int end)
    {
        System.out.println("merging ... " + start + " to " + end);
        mid -= 1;
        int start2 = mid + 1;
 
        // If the direct merge is already sorted
        if (arr[mid] <= arr[start2]) {
            return;
        }
 
        // Two pointers to maintain start
        // of both arrays to merge
        while (start <= mid && start2 <= end) {
 
            // If element 1 is in right place
            if (arr[start] <= arr[start2]) {
                start++;
            }
            else {
                long value = arr[start2];
                int index = start2;
 
                // Shift all the elements between element 1
                // element 2, right by 1.
                while (index != start) {
                    arr[index] = arr[index - 1];
                    index--;
                }
                arr[start] = value;
 
                // Update all the pointers
                start++;
                mid++;
                start2++;
            }
        }
        System.out.println("Done.");
    }
}
