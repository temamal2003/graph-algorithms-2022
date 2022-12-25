package org.example;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

import com.mathsystem.api.graph.model.Edge;
import com.mathsystem.api.graph.model.Graph;
import com.mathsystem.api.graph.model.Vertex;
import com.mathsystem.domain.graph.repository.GraphType;
import com.mathsystem.domain.plugin.plugintype.GraphProperty;

public class GraphTranzit implements GraphProperty {
	private int vertexCount;
	private int edgeCount;

	private boolean[] used;

	public boolean execute(Graph graph) {

		GraphAdjMatrix graphMatrix = new GraphAdjMatrix(graph);

		vertexCount = graph.getVertexCount();
		edgeCount = graph.getEdgeCount();
		int res =0;
		for (int i=0; i <vertexCount; i++ )
			for(int j =0; j<vertexCount;j++)
				for (int k=0; k < vertexCount; k++ ) {
					if (k == i || k == j) continue;
					if (((graphMatrix.matrix[i][k] == 1) && (graphMatrix.matrix[k][j]==1))){
						res = graphMatrix.matrix[i][j];
						if (res!=1) return false;

					}
				}


		return true;




	}



	private void createAdjacencyMatrix(Graph graph, Map<UUID, Map<UUID, Integer>> adjacencyMatrix) {
		for (UUID vertex1 : graph.getVertices().keySet()) {
			Map<UUID, Integer> vertexes = new HashMap<>();

			for (UUID vertex2 : graph.getVertices().keySet()) {
				vertexes.put(vertex2, 0);
			}

			adjacencyMatrix.put(vertex1, vertexes);
		}

		for (Edge edge : graph.getEdges()) {
			UUID vertex1 = edge.getFromV();
			UUID vertex2 = edge.getToV();

			adjacencyMatrix.get(vertex1).put(vertex2, 1);
			if (graph.getDirectType() == GraphType.UNDIRECTED) {
				adjacencyMatrix.get(vertex2).put(vertex1, 1);
			}
		}
	}

	public static class GraphAdjMatrix {
		private final Integer[][] matrix;

		public GraphAdjMatrix(Graph graph) {
			int vertexCount = graph.getVertexCount();
			int edgeCount = graph.getEdgeCount();
			List<Edge> edges = graph.getEdges();
			Map<UUID, Vertex> vertexMap = graph.getVertices();
			List<UUID> vertexes = vertexMap.keySet().stream().toList();// список вершин
			matrix = new Integer[vertexCount][edgeCount];
			for (int row = 0; row < vertexCount; row++) {
				for (int col = 0; col < vertexCount; col++) {
					matrix[row][col] = -1;
				}
			}
			for (int edgeIndex = 0; edgeIndex < edgeCount; ++edgeIndex) {
				Edge e = edges.get(edgeIndex);
				UUID from = e.getFromV();
				UUID to = e.getToV();
				int fromIndex = vertexes.indexOf(from);
				int toIndex = vertexes.indexOf(to);
				matrix[fromIndex][toIndex] = 1;


			}
		}

	}


}