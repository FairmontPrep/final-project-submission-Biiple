import java.util.*;

/**
 * PathTracer
 * ---------------------------------------------------------
 * Finds the unique robot path that follows connected 1-cells
 * in a 2-D integer array (no diagonal moves).  The path must
 * start and end on adjacent walls and contain at least one
 * 90-degree turn.  Results are printed both as a coordinate
 * list and as a visual grid showing only the 1s in the path.
 * ---------------------------------------------------------
 * HOW TO TEST:
 *   • Replace the static GRID array below with any test map.
 *   • Compile and run:  javac PathTracer.java && java PathTracer
 *   • Output:  answerList = [A[r][c], …]  +  visual grid.
 */
public class PathTracer {

    /* >>> Replace this 2-D array with your map <<< */
    private static final int[][] GRID = {
        {0,0,0,0,0,0,0,1},
        {0,0,0,0,0,0,0,1},
        {0,0,0,0,0,0,0,1},
        {0,0,0,0,0,0,0,1},
        {0,0,0,0,0,0,0,1},
        {0,0,0,0,0,0,0,1},
        {0,0,0,0,0,0,0,1},
        {1,1,1,1,1,1,1,1}
    };

    /* Grid dimensions and movement vectors (up, down, left, right) */
    private static final int R = GRID.length;
    private static final int C = GRID[0].length;
    private static final int[][] DIR = {{1,0},{-1,0},{0,1},{0,-1}};

    /* Simple coordinate record */
    private static class Node {
        int r, c;
        Node(int r, int c){ this.r = r; this.c = c; }
        @Override public boolean equals(Object o){
            return o instanceof Node n && n.r == r && n.c == c;
        }
        @Override public int hashCode(){ return Objects.hash(r, c); }
    }

    private final ArrayList<String> path = new ArrayList<>();
    private Node start, goal;

    /* ---------- entry point (only one line in main) ---------- */
    public static void main(String[] args){ new PathTracer().run(); }

    private void run(){
        findEndpoints();   // locate the two true endpoints on the border
        buildPath();       // BFS + parent map to reconstruct the path
        printResults();    // coordinate list + visual grid
    }

    /* Scan the border for cells with value 1 and degree 1 (true endpoints) */
    private void findEndpoints(){
        List<Node> ends = new ArrayList<>();

        /* top & bottom borders */
        for(int c = 0; c < C; c++){
            addIfEndpoint(0, c, ends);
            addIfEndpoint(R - 1, c, ends);
        }
        /* left & right borders (exclude corners already checked) */
        for(int r = 1; r < R - 1; r++){
            addIfEndpoint(r, 0, ends);
            addIfEndpoint(r, C - 1, ends);
        }
        if(ends.size() != 2)
            throw new IllegalStateException("The map must have exactly 2 endpoints.");
        start = ends.get(0);
        goal  = ends.get(1);
    }

    /* Helper: add coordinate to list if it is a border 1-cell with degree 1 */
    private void addIfEndpoint(int r, int c, List<Node> list){
        if(GRID[r][c] != 1) return;
        int neighbors = 0;
        for(int[] d : DIR){
            int nr = r + d[0], nc = c + d[1];
            if(inBounds(nr, nc) && GRID[nr][nc] == 1) neighbors++;
        }
        if(neighbors == 1) list.add(new Node(r, c));
    }

    /* Breadth-first search from start to goal; reconstruct path via parent map */
    private void buildPath(){
        boolean[][] seen = new boolean[R][C];
        Map<Node, Node> parent = new HashMap<>();
        Deque<Node> queue = new ArrayDeque<>();

        queue.add(start);
        seen[start.r][start.c] = true;

        while(!queue.isEmpty()){
            Node cur = queue.poll();
            if(cur.equals(goal)) break;
            for(int[] d : DIR){
                int nr = cur.r + d[0], nc = cur.c + d[1];
                if(!inBounds(nr, nc) || GRID[nr][nc] != 1 || seen[nr][nc]) continue;
                Node nxt = new Node(nr, nc);
                seen[nr][nc] = true;
                parent.put(nxt, cur);
                queue.add(nxt);
            }
        }
        /* Reconstruct in reverse, then add to path ordered from start to goal */
        LinkedList<String> rev = new LinkedList<>();
        for(Node p = goal; p != null; p = parent.get(p))
            rev.addFirst(coord(p));
        path.addAll(rev);
    }

    /* ----- output ----- */
    private void printResults(){
        System.out.println("answerList = " + path);

        /* Visual grid containing only the 1s in the path */
        Set<String> set = new HashSet<>(path);
        for(int r = 0; r < R; r++){
            StringBuilder sb = new StringBuilder("[ ");
            for(int c = 0; c < C; c++){
                sb.append(set.contains(coord(r, c)) ? "1" : " ");
                if(c != C - 1) sb.append(" , ");
            }
            sb.append(" ]");
            System.out.println(sb);
        }
    }

    /* ----- utility ----- */
    private boolean inBounds(int r, int c){ return r >= 0 && r < R && c >= 0 && c < C; }

    private String coord(Node p){ return coord(p.r, p.c); }

    private String coord(int r, int c){ return "A[" + r + "][" + c + "]"; }
}
