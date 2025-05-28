import java.util.*;

public class PathTracer {

    // Grid input (test_array_2)
    private static final List<List<Integer>> GRID = Arrays.asList(
            Arrays.asList(1, 0, 1, 1, 0, 0),
            Arrays.asList(0, 0, 1, 0, 1, 0),
            Arrays.asList(1, 1, 1, 1, 1, 0),
            Arrays.asList(9, 0, 0, 0, 0, 0)
           
    );

    private static final int[][] DIRS = { {-1, 0}, {0, 1}, {1, 0}, {0, -1} };

    public static void main(String[] args) {
        printPathAndGraph();
    }

    private static void printPathAndGraph() {
        List<Pos> path = extractPath();

        List<String> coords = new ArrayList<>();
        for (Pos p : path) {
            coords.add(String.format("A[%d][%d]", p.r, p.c));
        }

        System.out.println("Coordinate List:");
        System.out.println(coords);

        int rows = GRID.size(), cols = GRID.get(0).size();
        List<List<Integer>> tracedGrid = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            tracedGrid.add(new ArrayList<>(Collections.nCopies(cols, 0)));
        }
        for (Pos p : path) {
            tracedGrid.get(p.r).set(p.c, 1);
        }

        System.out.println("\nPath-Only Grid:");
        tracedGrid.forEach(System.out::println);
    }

    private static List<Pos> extractPath() {
        int rows = GRID.size(), cols = GRID.get(0).size();
        List<Pos> leaves = new ArrayList<>();

        // Identify degree-1 endpoints on the wall
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (GRID.get(r).get(c) == 1 && degree(r, c) == 1) {
                    if (r == 0 || r == rows - 1 || c == 0 || c == cols - 1) {
                        leaves.add(new Pos(r, c));
                    }
                }
            }
        }

        List<Pos> best = new ArrayList<>();
        for (Pos start : leaves) {
            Pos prev = null, cur = start;
            List<Pos> candidate = new ArrayList<>();
            while (cur != null) {
                candidate.add(cur);
                Pos next = neighborExcluding(cur, prev);
                prev = cur;
                cur = next;
            }
            if (candidate.size() > best.size()) {
                best = candidate;
            }
        }
        return best;
    }

    private static int degree(int r, int c) {
        int count = 0;
        for (int[] d : DIRS) {
            int nr = r + d[0], nc = c + d[1];
            if (inBounds(nr, nc) && GRID.get(nr).get(nc) == 1) {
                count++;
            }
        }
        return count;
    }

    private static Pos neighborExcluding(Pos current, Pos exclude) {
        for (int[] d : DIRS) {
            int nr = current.r + d[0], nc = current.c + d[1];
            if (inBounds(nr, nc) && GRID.get(nr).get(nc) == 1) {
                if (exclude == null || !(exclude.r == nr && exclude.c == nc)) {
                    return new Pos(nr, nc);
                }
            }
        }
        return null;
    }

    private static boolean inBounds(int r, int c) {
        return r >= 0 && r < GRID.size() && c >= 0 && c < GRID.get(0).size();
    }

    private static final class Pos {
        final int r, c;
        Pos(int r, int c) {
            this.r = r;
            this.c = c;
        }
    }
}
