import java.util.*;

public class PathTracer {

    /** 2-D grid: 1 = path cell, others are irrelevant to tracing logic. */
    private static final List<List<Integer>> GRID = Arrays.asList(
            Arrays.asList(1, 0, 0, 1, 0, 0, 0, 0),
            Arrays.asList(0, 0, 0, 1, 0, 0, 0, 0),
            Arrays.asList(0, 0, 0, 1, 0, 0, 1, 0),
            Arrays.asList(9, 0, 0, 1, 0, 0, 0, 0),
            Arrays.asList(0, 0, 0, 1, 0, 0, 0, 0),
            Arrays.asList(0, 0, 0, 1, 0, 0, 0, 0),
            Arrays.asList(0, 0, 0, 1, 2, 0, 0, 0),
            Arrays.asList(1, 0, 0, 1, 1, 1, 1, 1)
    );

    /** Four orthogonal neighbours. */
    private static final int[][] DIRS = { {-1,0}, {0,1}, {1,0}, {0,-1} };

    public static void main(String[] args) {
        printPathAndGraph();
    }

    /** High-level orchestration: derive the path, show coordinates, then show a path-only grid. */
    private static void printPathAndGraph() {
        List<Pos> path = extractPath();

        // 1) Print coordinate list in the required “A[r][c]” form
        List<String> coordinates = new ArrayList<>();
        for (Pos p : path) coordinates.add(String.format("A[%d][%d]", p.r, p.c));
        System.out.println("Coordinate List:");
        System.out.println(coordinates);

        // 2) Build and print a grid that contains only the traced path (1 = path, 0 = blank)
        int rows = GRID.size(), cols = GRID.get(0).size();
        List<List<Integer>> pathOnly = new ArrayList<>();
        for (int i = 0; i < rows; i++) pathOnly.add(new ArrayList<>(Collections.nCopies(cols, 0)));
        for (Pos p : path) pathOnly.get(p.r).set(p.c, 1);

        System.out.println("\nPath-Only Grid:");
        pathOnly.forEach(System.out::println);
    }

    /** Returns the longest simple chain of contiguous ‘1’ cells by brute-force traversal from each leaf. */
    private static List<Pos> extractPath() {
        int rows = GRID.size(), cols = GRID.get(0).size();
        List<Pos> leaves = new ArrayList<>();

        // Identify leaves (degree = 1)
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (GRID.get(r).get(c) == 1 && degree(r, c) == 1) leaves.add(new Pos(r, c));
            }
        }

        // DFS from every leaf, keep the longest chain encountered
        List<Pos> best = new ArrayList<>();
        for (Pos start : leaves) {
            Pos prev = null, cur = start;
            List<Pos> candidate = new ArrayList<>();
            while (cur != null) {
                candidate.add(cur);
                Pos next = neighbourExcept(cur, prev);
                prev = cur;
                cur = next;
            }
            if (candidate.size() > best.size()) best = candidate;
        }
        return best;
    }

    /** Degree = number of neighbour ‘1’ cells. */
    private static int degree(int r, int c) {
        int cnt = 0;
        for (int[] d : DIRS) {
            int nr = r + d[0], nc = c + d[1];
            if (inBounds(nr, nc) && GRID.get(nr).get(nc) == 1) cnt++;
        }
        return cnt;
    }

    /** Returns the next neighbour on the path, excluding the cell we just came from. */
    private static Pos neighbourExcept(Pos cur, Pos exclude) {
        for (int[] d : DIRS) {
            int nr = cur.r + d[0], nc = cur.c + d[1];
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

    /** Immutable coordinate holder. */
    private static final class Pos {
        final int r, c;
        Pos(int r, int c) { this.r = r; this.c = c; }
    }
}
