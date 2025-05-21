import java.util.*;

public class Client {

    // Global static variable to hold the map
    static int[][] A;

    public static void main(String[] args) {
        ArrayList<ArrayList<Integer>> test_array_2 = new ArrayList<>();
        test_array_2.add(new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 1)));
        test_array_2.add(new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 1)));
        test_array_2.add(new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 1)));
        test_array_2.add(new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 1)));
        test_array_2.add(new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 1)));
        test_array_2.add(new ArrayList<>(Arrays.asList(1, 1, 1, 1, 1, 1, 1)));
        

        A = convertTo2DArray(test_array_2);
        run();
    }

    public static void run() {
        ArrayList<String> path = findPath(A);
        System.out.println(path);
        printPathOnMap(path, A.length, A[0].length);
    }

    public static int[][] convertTo2DArray(ArrayList<ArrayList<Integer>> list) {
        int rows = list.size();
        int cols = list.get(0).size();
        int[][] result = new int[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = list.get(i).get(j);
            }
        }
        return result;
    }

    public static ArrayList<String> findPath(int[][] map) {
        ArrayList<String> path = new ArrayList<>();
        boolean[][] visited = new boolean[map.length][map[0].length];
        int[] start = findStart(map);
        if (start == null) return path;
        dfs(map, start[0], start[1], visited, path);
        return path;
    }

    private static int[] findStart(int[][] map) {
        for (int i = 0; i < map.length; i++) {
            if (map[i][0] == 1) return new int[]{i, 0};
            if (map[i][map[0].length - 1] == 1) return new int[]{i, map[0].length - 1};
        }
        for (int j = 0; j < map[0].length; j++) {
            if (map[0][j] == 1) return new int[]{0, j};
            if (map[map.length - 1][j] == 1) return new int[]{map.length - 1, j};
        }
        return null;
    }

    public static void dfs(int[][] map, int i, int j, boolean[][] visited, ArrayList<String> path) {
        if (i < 0 || i >= map.length || j < 0 || j >= map[0].length || visited[i][j] || map[i][j] != 1) {
            return;
        }

        visited[i][j] = true;
        path.add("A[" + i + "][" + j + "]");

        dfs(map, i + 1, j, visited, path);
        dfs(map, i - 1, j, visited, path);
        dfs(map, i, j + 1, visited, path);
        dfs(map, i, j - 1, visited, path);
    }

    public static void printPathOnMap(ArrayList<String> path, int rows, int cols) {
        String[][] output = new String[rows][cols];

        for (String coordinate : path) {
            String[] parts = coordinate.replace("A[", "").replace("]", "").split("\\[|\\]");
            int r = Integer.parseInt(parts[0]);
            int c = Integer.parseInt(parts[1]);
            output[r][c] = "1";
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (output[i][j] != null) {
                    System.out.print("1 ");
                } else {
                    System.out.print("  ");
                }
            }
            System.out.println();
        }
    }
}
