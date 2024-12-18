import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Arrays.stream;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class TasksTest {

    @Test
    void taskTest() {

        int[][] arr = new int[2][3];

        for (int i = 0; i < arr.length; i++) {
            int[] ints = arr[i];
            for (int j = 0; j < ints.length; j++) {
                arr[i][j] = 1;
            }
        }

//        for (int[] row : arr) {
//            for (int col: row) {
//                Arrays.fill(row, 1);
//            }
//        }

        for (int i = 0; i < arr.length; i++) {
            int[] ints = arr[i];

            for (int j = 0; j < ints.length; j++) {
                if (j % 2 == 0) arr[i][j] += 2;
                else arr[i][j] += 1;
            }
        }

        for (int[] row : arr) {
            for (int col : row) {
                System.out.print(col + " ");
            }
            System.out.println();
        }
//        assertEquals(2, 1+1);
    }


    @Test
    void task2() {
        String text = "Cn.EmlkE y;E/Re*H";

        String[] parts = text.split("\\W");

        int count = 0;
        for (String part : parts) {

            char i = part.charAt(0);
            if (Character.isUpperCase(i)) count++;
        }

        System.out.println(count);
    }


    @Test
    void task3() {
        int[] arr = {4, 1, 3, 2, 5, 5};

        int[] arrSort = {1, 2, 3, 4, 5, 5};

        assertArrayEquals(sort1(arr), sort3(arr));
//        assertTrue(Arrays.equals(sort1(arr), arrSort));

    }

    int[] sort1(int[] arr) {
        return IntStream.of(arr).sorted().toArray();
//        return Arrays.stream(Stream.of(arr).sorted().toArray()).mapToInt(o -> (int)o).toArray();
    }

    int[] sort2(int[] arr) {
        Arrays.sort(arr);
        return arr;
    }

    int[] sort3(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = 1; j < arr.length - i; j++) {
                if (arr[j - 1] > arr[j]) {
                    int swap = arr[j];
                    arr[j] = arr[j - 1];
                    arr[j - 1] = swap;
                }
            }
        }
        return arr;
    }
}