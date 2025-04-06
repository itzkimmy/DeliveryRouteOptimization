import java.util.*;

public class DeliveryRouteOptimization {

    // Distance Matrix (Adjacency Matrix)
    static int[][] distanceMatrix = {
        {0, 12, 18, 22},
        {12, 0, 40, 30},
        {18, 40, 0, 35},
        {22, 30, 35, 0}
    };

    // Location names
    static String[] locations = {"Poslaju Warehouse", "Jnt Warehouse", "Maluri Center", "Pudu Center"};

    // Greedy DROP
    public static String greedyDROP(int[][] dist) {
        int n = dist.length;
        boolean[] visited = new boolean[n];
        int current = 0, totalDistance = 0;
        visited[current] = true;
        StringBuilder path = new StringBuilder(locations[current]);
        
        for (int i = 1; i < n; i++) {
            int next = -1, minDist = Integer.MAX_VALUE;
            for (int j = 0; j < n; j++) {
                if (!visited[j] && dist[current][j] < minDist) {
                    next = j;
                    minDist = dist[current][j];
                }
            }
            visited[next] = true;
            totalDistance += minDist;
            path.append(" -> ").append(locations[next]);
            current = next;
        }
        totalDistance += dist[current][0]; // Return to starting point
        path.append(" -> ").append(locations[0]);
        return path.toString() + " | Total Cost: " + totalDistance;
    }

    // Dynamic Programming DROP
    public static String dynamicProgrammingDROP(int[][] dist) {
        int n = dist.length;
        int VISITED_ALL = (1 << n) - 1;
        int[][] memo = new int[n][1 << n];
        String[][] paths = new String[n][1 << n];
        for (int[] row : memo) Arrays.fill(row, -1);
        for (String[] row : paths) Arrays.fill(row, null);
        
        int minCost = dynamicProgrammingDROPHelper(0, 1, dist, memo, VISITED_ALL, paths);
        StringBuilder path = new StringBuilder(locations[0]);
        int pos = 0, mask = 1;
        
        while (mask != VISITED_ALL) {
            String nextStep = paths[pos][mask];
            int nextPos = -1;
            for (int i = 0; i < n; i++) {
                if (locations[i].equals(nextStep)) {
                    nextPos = i;
                    break;
                }
            }
            path.append(" -> ").append(nextStep);
            mask |= (1 << nextPos);
            pos = nextPos;
        }
        path.append(" -> ").append(locations[0]);
        return path.toString() + " | Total Cost: " + minCost;
    }

    private static int dynamicProgrammingDROPHelper(int pos, int mask, int[][] dist, int[][] memo, int VISITED_ALL, String[][] paths) {
        if (mask == VISITED_ALL) {
            return dist[pos][0];
        }
        if (memo[pos][mask] != -1) {
            return memo[pos][mask];
        }
        
        int minCost = Integer.MAX_VALUE;
        String nextLocation = null;
        
        for (int i = 0; i < dist.length; i++) {
            if ((mask & (1 << i)) == 0) {
                int newCost = dist[pos][i] + dynamicProgrammingDROPHelper(i, mask | (1 << i), dist, memo, VISITED_ALL, paths);
                if (newCost < minCost) {
                    minCost = newCost;
                    nextLocation = locations[i];
                }
            }
        }
        
        memo[pos][mask] = minCost;
        paths[pos][mask] = nextLocation;
        return minCost;
    }

    // Backtracking DROP
    public static String backtrackingDROP(int[][] dist) {
        int n = dist.length;
        boolean[] visited = new boolean[n];
        visited[0] = true;
        StringBuilder path = new StringBuilder(locations[0]);
        int[] minCost = {Integer.MAX_VALUE};
        StringBuilder bestPath = new StringBuilder();
        
        DROPBacktracking(0, dist, visited, n, 1, 0, path, minCost, bestPath);
        return bestPath.toString() + " | Total Cost: " + minCost[0];
    }

    private static void DROPBacktracking(int pos, int[][] dist, boolean[] visited, int n, int count, int cost, StringBuilder path, int[] minCost, StringBuilder bestPath) {
        if (count == n && dist[pos][0] > 0) {
            int totalCost = cost + dist[pos][0];
            if (totalCost < minCost[0]) {
                minCost[0] = totalCost;
                bestPath.setLength(0);
                bestPath.append(path).append(" -> ").append(locations[0]);
            }
            return;
        }
        
        for (int i = 0; i < n; i++) {
            if (!visited[i] && dist[pos][i] > 0) {
                visited[i] = true;
                path.append(" -> ").append(locations[i]);
                DROPBacktracking(i, dist, visited, n, count + 1, cost + dist[pos][i], path, minCost, bestPath);
                visited[i] = false;
                path.setLength(path.lastIndexOf(" -> "));
            }
        }
    }

    // Divide and Conquer DROP
    public static String divideAndConquerDROP(int[][] dist) {
        int n = dist.length;
        boolean[] visited = new boolean[n];
        visited[0] = true;
        StringBuilder path = new StringBuilder(locations[0]);
        int[] minCost = {Integer.MAX_VALUE};
        StringBuilder bestPath = new StringBuilder();
        
        divideAndConquerHelper(0, visited, 0, dist, n, path, minCost, bestPath);
        return bestPath.toString() + " | Total Cost: " + minCost[0];
    }

    private static void divideAndConquerHelper(int pos, boolean[] visited, int currentCost, int[][] dist, int n, StringBuilder path, int[] minCost, StringBuilder bestPath) {
        if (allVisited(visited) && dist[pos][0] > 0) {
            int totalCost = currentCost + dist[pos][0];
            if (totalCost < minCost[0]) {
                minCost[0] = totalCost;
                bestPath.setLength(0);
                bestPath.append(path).append(" -> ").append(locations[0]);
            }
            return;
        }
        
        for (int i = 0; i < n; i++) {
            if (!visited[i] && dist[pos][i] > 0) {
                visited[i] = true;
                path.append(" -> ").append(locations[i]);
                divideAndConquerHelper(i, visited, currentCost + dist[pos][i], dist, n, path, minCost, bestPath);
                visited[i] = false;
                path.setLength(path.lastIndexOf(" -> "));
            }
        }
    }

    private static boolean allVisited(boolean[] visited) {
        for (boolean v : visited) {
            if (!v) return false;
        }
        return true;
    }

    // Insertion Sort
    public static String insertionSort(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            int key = arr[i];
            int j = i - 1;
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
        return Arrays.toString(arr);
    }

    // Binary Search
    public static String binarySearch(int[] arr, int target) {
        int left = 0, right = arr.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] == target) return String.valueOf(mid);
            if (arr[mid] < target) left = mid + 1;
            else right = mid - 1;
        }
        return "-1";
    }

    // Min-Heap
    static class MinHeap {
        private PriorityQueue<Integer> heap = new PriorityQueue<>();
        
        public void insert(int val) {
            heap.add(val);
        }
        
        public int extractMin() {
            return heap.poll();
        }
    }

    // Splay Tree
    static class SplayTree {
        private class Node {
            int key;
            Node left, right;
            Node(int key) { this.key = key; }
        }
        private Node root;

        private Node rightRotate(Node x) {
            Node y = x.left;
            x.left = y.right;
            y.right = x;
            return y;
        }

        private Node leftRotate(Node x) {
            Node y = x.right;
            x.right = y.left;
            y.left = x;
            return y;
        }

        private Node splay(Node root, int key) {
            if (root == null || root.key == key) return root;
            if (key < root.key) {
                if (root.left == null) return root;
                if (key < root.left.key) {
                    root.left.left = splay(root.left.left, key);
                    root = rightRotate(root);
                } else if (key > root.left.key) {
                    root.left.right = splay(root.left.right, key);
                    if (root.left.right != null)
                        root.left = leftRotate(root.left);
                }
                return root.left == null ? root : rightRotate(root);
            } else {
                if (root.right == null) return root;
                if (key > root.right.key) {
                    root.right.right = splay(root.right.right, key);
                    root = leftRotate(root);
                } else if (key < root.right.key) {
                    root.right.left = splay(root.right.left, key);
                    if (root.right.left != null)
                        root.right = rightRotate(root.right);
                }
                return root.right == null ? root : leftRotate(root);
            }
        }

        public void insert(int key) {
            root = splay(root, key);
            if (root == null) root = new Node(key);
            else if (key < root.key) {
                Node newNode = new Node(key);
                newNode.right = root;
                newNode.left = root.left;
                root.left = null;
                root = newNode;
            } else if (key > root.key) {
                Node newNode = new Node(key);
                newNode.left = root;
                newNode.right = root.right;
                root.right = null;
                root = newNode;
            }
        }

        public boolean search(int key) {
            root = splay(root, key);
            return root != null && root.key == key;
        }
    }

    // Driver method
    public static void main(String[] args) {
        System.out.println("===== Delivery Route Optimization =====\n");
    
        System.out.println(">> Greedy DROP Solution:");
        System.out.println(greedyDROP(distanceMatrix) + "\n");
    
        System.out.println(">> Dynamic Programming DROP Solution:");
        System.out.println(dynamicProgrammingDROP(distanceMatrix) + "\n");
    
        System.out.println(">> Backtracking DROP Solution:");
        System.out.println(backtrackingDROP(distanceMatrix) + "\n");
    
        System.out.println(">> Divide and Conquer DROP Solution:");
        System.out.println(divideAndConquerDROP(distanceMatrix) + "\n");
    
        // Sorting and Searching
        int[] arr = {8, 3, 5, 1, 9, 2};
        insertionSort(arr);
        System.out.println("===== Sorting and Searching =====");
        System.out.println("Sorted Array (Insertion Sort): " + Arrays.toString(arr));
        System.out.println("Binary Search Result for value 5: Index " + binarySearch(arr, 5) + "\n");
    
        // Min-Heap Test
        MinHeap heap = new MinHeap();
        heap.insert(10);
        heap.insert(3);
        heap.insert(15);
        System.out.println("===== Min-Heap Test =====");
        System.out.println("Extracted Minimum: " + heap.extractMin() + "\n");
    
        // Splay Tree Test
        SplayTree tree = new SplayTree();
        tree.insert(20);
        tree.insert(10);
        tree.insert(30);
        System.out.println("===== Splay Tree Test =====");
        System.out.println("Searching for value 10: " + (tree.search(10) ? "Found" : "Not Found"));
    }    
}