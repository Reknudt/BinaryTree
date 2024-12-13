import org.junit.jupiter.api.Test;


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
                else arr[i][j] +=1;
            }
        }

        for (int[] row : arr) {
            for (int col: row) {
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



    }
}
