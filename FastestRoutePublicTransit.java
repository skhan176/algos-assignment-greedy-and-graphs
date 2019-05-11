/**
 * Public Transit
 * Author: sakib khan and Carolyn Yao
 * Does this compile? Y
 */

/**
 * This class contains solutions to the Public, Public Transit problem in the
 * shortestTimeToTravelTo method. There is an existing implementation of a
 * shortest-paths algorithm. As it is, you can run this class and get the solutions
 * from the existing shortest-path algorithm.
 */
public class FastestRoutePublicTransit {

  /**
   * The algorithm that could solve for shortest travel time from a station S
   * to a station T given various tables of information about each edge (u,v)
   *
   * @param S the s th vertex/station in the transit map, start From
   * @param T the t th vertex/station in the transit map, end at
   * @param startTime the start time in terms of number of minutes from 5:30am
   * @param lengths lengths[u][v] The time it takes for a train to get between two adjacent stations u and v
   * @param first first[u][v] The time of the first train that stops at u on its way to v, int in minutes from 5:30am
   * @param freq freq[u][v] How frequently is the train that stops at u on its way to v
   * @return shortest travel time between S and T
   */
  public int myShortestTravelTime(
    int S,
    int T,
    int startTime,
    int[][] lengths,
    int[][] first,
    int[][] freq
  ) {
	  
    int path[] = myShortestTime(lengths, S, T); //finds the shortest path from starting station to destination
    int index= first[0].length-1;
    //there are -1 as placeholder, so we'll traverse backwards to find the beginning of the shortest path
    while(path[index]!=S && index>0) {
      index--;
    }

    int shortestTime=0; //keeps track of the shortest time it takes to get from starting station to destination station
    int nextTrainTime;  //finds the nextTrainTime plus the time it takes for the train to travel that edge
    int currentTime=startTime;

    for(int i=index; i>=1; i--) {

      int stationNumber = path[i];
      int nextStation = path[i-1];
      int trainTimes= first[stationNumber][nextStation];
      int p = 0;
        while (trainTimes < currentTime) {
          trainTimes = first[stationNumber][nextStation] + (p * freq[stationNumber][nextStation]);
          p++;
      }

      nextTrainTime = trainTimes + lengths[stationNumber][nextStation];
      shortestTime = shortestTime + (nextTrainTime - currentTime);
      currentTime=nextTrainTime;
    }
    return shortestTime;
  }

  //Dijstras algorithms

  public int[] myShortestTime(int[][] graph, int src, int T) {
    int V = graph[0].length;    
    int[] times = new int[V]; 
    int[] prev = new int[V];    
    int[] path = new int[V];
    prev[src] = -1;    // Sets the previous station of source to -1 since it doesn't exist.

    // sptSet[i] will true if vertex i is included in shortest
    // path tree or shortest distance from src to i is finalized
    Boolean sptSet[] = new Boolean[V];

    // Initialize all distances as INFINITE and stpSet[] as false
    for (int i = 0; i < V; i++)
    {
      times[i] = Integer.MAX_VALUE;
      path[i] = -1;
      sptSet[i] = false;
    }
    // Time of source vertex from itself is always 0
    times[src] = 0;

    for (int count = 0; count < V - 1; count++) { 		// Find shortest path to all the vertices
      // Pick the minimum distance vertex from the set of vertices not yet processed.
      // u is always equal to source in first iteration.
      // Mark u as processed.
      int u = findNextToProcess(times, sptSet);
      sptSet[u] = true;

      // Update time value of all the adjacent vertices of the picked vertex.
      for (int v = 0; v < V; v++) {
        // Update time[v] only if is not in sptSet, there is an
        // edge from u to v, and total weight of path from src to
        // v through u is smaller than current value of time[v]
        if (!sptSet[v] && graph[u][v]!=0 && times[u] != Integer.MAX_VALUE && times[u]+graph[u][v] < times[v]) {
          times[v] = times[u] + graph[u][v];
          prev[v] = u; 
        }
      }
    }
    int index= T; //keep track of the stations
    int currentIndex=0; // keep track of current station
    //backtrack from the destination to the source with the help from the prev array (the array that holds the previous station)
    //at -1, we know that we hit the source or starting station
    while(prev[index]!=-1) {
       path[currentIndex]= index;
	 currentIndex++;
	 index= prev[index];
    }
    path[currentIndex]= index;//add the source station
    return path;
  }
  /**
   * Finds the vertex with the minimum time from the source that has not been
   * processed yet.
   * @param times The shortest times from the source
   * @param processed boolean array tells you which vertices have been fully processed
   * @return the index of the vertex that is next vertex to process
   */
  
  public int findNextToProcess(int[] times, Boolean[] processed) {
    int min = Integer.MAX_VALUE;
    int minIndex = -1;

    for (int i = 0; i < times.length; i++) {
      if (processed[i] == false && times[i] <= min) {
        min = times[i];
        minIndex = i;
      }
    }
    return minIndex;
  }
  
  public void printShortestTimes(int times[]) {
    System.out.println("Vertex Distances (time) from Source");
    for (int i = 0; i < times.length; i++)
        System.out.println(i + ": " + times[i] + " minutes");
  }

  /**
   * Given an adjacency matrix of a graph, implements
   * @param graph The connected, directed graph in an adjacency matrix where
   *              if graph[i][j] != 0 there is an edge with the weight graph[i][j]
   * @param source The starting vertex
   */
  public void shortestTime(int graph[][], int source) {
    int numVertices = graph[0].length;

    // This is the array where we'll store all the final shortest times
    int[] times = new int[numVertices];

    // processed[i] will true if vertex i's shortest time is already finalized
    Boolean[] processed = new Boolean[numVertices];

    // Initialize all distances as INFINITE and processed[] as false
    for (int v = 0; v < numVertices; v++) {
      times[v] = Integer.MAX_VALUE;
      processed[v] = false;
    }

    // Distance of source vertex from itself is always 0
    times[source] = 0;

    // Find shortest path to all the vertices
    for (int count = 0; count < numVertices - 1 ; count++) {
      // Pick the minimum distance vertex from the set of vertices not yet processed.
      // u is always equal to source in first iteration.
      // Mark u as processed.
      int u = findNextToProcess(times, processed);
      processed[u] = true;

      // Update time value of all the adjacent vertices of the picked vertex.
      for (int v = 0; v < numVertices; v++) {
        // Update time[v] only if is not processed yet, there is an edge from u to v,
        // and total weight of path from source to v through u is smaller than current value of time[v]
        if (!processed[v] && graph[u][v]!=0 && times[u] != Integer.MAX_VALUE && times[u]+graph[u][v] < times[v]) {
          times[v] = times[u] + graph[u][v];
        }
      }
    }
    printShortestTimes(times);
  }

  public static void main (String[] args) {
    /* length(e) */
    int lengthTimeGraph[][] = new int[][]{
      {0, 4, 0, 0, 0, 0, 0, 8, 0},
      {4, 0, 8, 0, 0, 0, 0, 11, 0},
      {0, 8, 0, 7, 0, 4, 0, 0, 2},
      {0, 0, 7, 0, 9, 14, 0, 0, 0},
      {0, 0, 0, 9, 0, 10, 0, 0, 0},
      {0, 0, 4, 14, 10, 0, 2, 0, 0},
      {0, 0, 0, 0, 0, 2, 0, 1, 6},
      {8, 11, 0, 0, 0, 0, 1, 0, 7},
      {0, 0, 2, 0, 0, 0, 6, 7, 0}
    };
    

    // You can create a test case for your implemented method for extra credit below

    int length[][] = new int[][] {
        /*length(e)*/
            { 0,  0,  3,  0,  0,  4,  0,  11, 0 },  
            { 0,  0,  8,  0,  13, 0,  0,  0,  0 },  
            { 6,  8,  0,  7,  0,  8,  0,  0,  6 }, 
            { 0,  0,  7,  0,  12, 4,  0,  2,  0 },  
            { 0,  3, 0,  7, 0,  0,  0,  0,  0 },  
            { 9,  0,  8,  4,  0,  0,  6,  0,  0 },  
            { 0,  0,  0,  0,  0,  6,  0,  5,  2 },  
            { 11, 0,  0,  2,  0,  0,  6,  0,  0 },  
            { 0,  0,  6,  0,  0,  0,  2,  0,  0 } };

    int first[][] = new int[][] {
        /*first(e)*/
            { 0,  0,  8,  0,  0,  9,  0,  15, 0 },  
            { 0,  0,  2,  0,  22, 0,  0,  0,  0 },  
            { 2,  8,  0,  7,  0,  4,  0,  0,  6 },  
            { 0,  0,  7,  0,  13, 4,  0,  6,  0 },  
            { 0,  3, 0,  22, 0,  0,  0,  0,  0 },  
            { 8,  0,  4,  9,  0,  0,  8,  0,  0 },  
            { 0,  0,  0,  0,  0,  4,  0,  9,  6 },  
            { 17, 0,  0,  4,  0,  0,  4, 0,  0 },  
            { 0,  0,  4,  0,  0,  0,  9,  0,  0 } };

    int freq[][] = new int[][] {
        /*freq(e)*/
            { 0,  0,  5,  0,  0,  4,  0,  4,  0 },  
            { 0,  0,  6,  0,  8,  0,  0,  0,  0 },  
            { 5,  6,  0,  7,  0,  4,  0,  0,  8 },  
            { 0,  0,  7,  0,  10, 4,  0,  5,  0 },  
            { 0,  8,  0,  10, 0,  0,  0,  0,  0 },  
            { 4,  0,  4,  4,  0,  0,  9,  0,  0 },  
            { 0,  0,  0,  0,  0,  7,  0,  8,  6 },  
            { 5,  0,  0,  2,  0,  0,  8,  0,  0 },  
            { 0,  0,  8,  0,  0,  0,  6,  0,  0 } };


    int shortestTime;
    int startStation = 1;
    int targetStation = 8;
    FastestRoutePublicTransit k = new FastestRoutePublicTransit();
    k.shortestTime(lengthTimeGraph, 0);
    shortestTime = k.myShortestTravelTime(startStation, targetStation, 2, length, first, freq);
    System.out.println( "The shortest time  will take  " + targetStation + " from station " + startStation + " in " + shortestTime + " minutes.");
    
  
  }
}

// notes: I have used https://www.geeksforgeeks.org/dijkstras-shortest-path-algorithm-greedy-algo-7/ to implemented dijkstras algorithms.
