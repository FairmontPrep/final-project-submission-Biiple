import java.util.*;

public class Client {

    // Static 2D array representing the map
    static int[][] A;

    public static void main(String[] args) {
        // Define the test map using ArrayList of ArrayLists
        ArrayList<ArrayList<Integer>> test_array_2 = new ArrayList<>();
        test_array_2.add(new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 1)));
        test_array_2.add(new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 1)));
        test_array_2.add(new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 1)));
        test_array_2.add(new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 1)));
        test_array_2.add(new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 1)));
        test_array_2.add(new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 1)));
        test_array_2.add(new ArrayList<>(Arrays.asList(1, 1, 1, 1, 1, 1, 1)));

        // Convert to static int[][] array
        A = convertTo2DArray(test_array_2);

        // Run the pathfinding
        ArrayList<String> path = findPath(A);

        // Output results
        System.out.println(path);
        printPath(path, A.length, A[0].length);
    }

    // Converts ArrayList of ArrayLists into a 2D int array
    public static int[][] convertTo2DArray(ArrayList<ArrayList<Integer>> input) {
        int rows = input.size();
        int cols = input.get(0).size();
        int[][] result = new int[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = input.get(i).get(j);
            }
        }

        return result;
    }

    // Finds a connected path of 1s in the map
    public static ArrayList<String> findPath(int[][] map) {
        ArrayList<String> path = new ArrayList<>();
        boolean[][] visited = new boolean[map.length][map[0].length];
        int[] start = findStartingPoint(map);

        if (start != null) {
            dfs(map, start[0], start[1], visited, path);
        }

        return path;
    }

    // Finds the first 1 on the edge of the map (start point)
    public static int[] findStartingPoint(int[][] map) {
        int rows = map.length;
        int cols = map[0].length;

        for (int i = 0; i < rows; i++) {
            if (map[i][0] == 1) return new int[]{i, 0};
            if (map[i][cols - 1] == 1) return new int[]{i, cols - 1};
        }

        for (int j = 0; j < cols; j++) {
            if (map[0][j] == 1) return new int[]{0, j};
            if (map[rows - 1][j] == 1) return new int[]{rows - 1, j};
        }

        return null;
    }

    // Recursive DFS to build path through connected 1s
    public static void dfs(int[][] map, int i, int j, boolean[][] visited, ArrayList<String> path) {
        if (i < 0 || i >= map.length || j < 0 || j >= map[0].length) return;
        if (map[i][j] != 1 || visited[i][j]) return;

        visited[i][j] = true;
        path.add("A[" + i + "][" + j + "]");

        dfs(map, i + 1, j, visited, path);
        dfs(map, i - 1, j, visited, path);
        dfs(map, i, j + 1, visited, path);
        dfs(map, i, j - 1, visited, path);
    }

    // Prints the visual path map to the console
    public static void printPath(ArrayList<String> path, int rows, int cols) {
        String[][] grid = new String[rows][cols];

        for (String coord : path) {
            String[] parts = coord.replace("A[", "").replace("]", "").split("\\[|\\]");
            int r = Integer.parseInt(parts[0]);
            int c = Integer.parseInt(parts[1]);
            grid[r][c] = "1";
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(grid[i][j] != null ? "1 " : "  ");
            }
            System.out.println();
        }
    }
}
