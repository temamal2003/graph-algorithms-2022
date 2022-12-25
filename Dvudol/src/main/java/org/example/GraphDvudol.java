package org.example;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.mathsystem.api.graph.model.Edge;
import com.mathsystem.api.graph.model.Graph;
import com.mathsystem.api.graph.model.Vertex;
import com.mathsystem.domain.plugin.plugintype.GraphProperty;

import javax.swing.*;

public class GraphDvudol implements GraphProperty {
	private int vertexCount;
	private int edgeCount;

	private boolean TF;
	private boolean[] used;

	public boolean execute(Graph graph) {
		vertexCount = graph.getVertexCount();
		int[] clrs = new int[vertexCount];
		TF = true;
		int c = 1;
		used = new boolean[vertexCount];
		Arrays.fill(used, false);

		List<UUID> vertexes = graph.getVertices().keySet().stream().toList();// список вершин списком
		for (int i = 0;i<vertexCount;i++)
		{
			clrs[i]= 0;
		}

		GraphAdjMatrix graphMatrix = new GraphAdjMatrix(graph);// граф заданный матрицой
		DFS(graphMatrix, 0, -1, used); //проверка графа на связность
		if (!isAllVisited(used))
			return false;
		for (int i = 0;i<vertexCount;i++)
		{
			if (clrs[i] == 0)
			{

				clrs[i]= c;
				coloring(i,graphMatrix,clrs, c);
			}
		}

		for (int i = 0; i < vertexCount; i++) {
			for (int j = 0; j < vertexCount; j++) {
				if (graphMatrix.matrix[i][j] == 1 )
				{
					TF= false;
				}
			}
		}
		return TF;

	}
	private boolean isAllVisited(boolean[] used) { // все ли вершины посещены
		for (boolean use : used) {
			if (!use)
				return false; // Несвязный граф
		}
		return true;
	}


	public static class GraphAdjMatrix {
		private final Integer[][] matrix;

		public GraphAdjMatrix(Graph graph) {
			int vertexCount = graph.getVertexCount();
			int edgeCount = graph.getEdgeCount();
			List<Edge> edges = graph.getEdges(); // список ребер
			Map<UUID, Vertex> vertexMap = graph.getVertices();// список вершин
			List<UUID> vertexes = vertexMap.keySet().stream().toList();// список вершин
			matrix = new Integer[vertexCount][vertexCount];
			for (int row = 0; row < vertexCount; row++) {
				for (int col = 0; col < vertexCount; col++) {
					matrix[row][col] = 0;
				}
			}
			for (int edgeIndex = 0; edgeIndex < edgeCount; ++edgeIndex) {
				Edge e = edges.get(edgeIndex);
				UUID from = e.getFromV();
				UUID to = e.getToV();
				int fromIndex = vertexes.indexOf(from);
				int toIndex = vertexes.indexOf(to);
				matrix[fromIndex][toIndex] = 1;
				matrix[toIndex][fromIndex] = 1;

			}
		}

	}
	public void coloring(int u, GraphAdjMatrix graphMatrix,int[] clrs, int c)
	{
		for(int i = 0;i<vertexCount;i++)
		{
			if (graphMatrix.matrix[u][i] == 1)
			{
				if (c == 1)
				{
					clrs[i] = 2;
				}
				else
				{
					clrs[i] = 1;
				}
				graphMatrix.matrix[u][i] = 3;
				graphMatrix.matrix[i][u] = 3;

			}

		}

	}
	private void DFS(GraphAdjMatrix matrix, int v, int exc, boolean[] used) { //обход в глубину
		used[v] = true;

		if (v != exc) {
			for (int j = 0; j < vertexCount; j++) {
				if (matrix.matrix[v][j] != 0

						&& !used[matrix.matrix[v][j]]) { //если путь существует, точка не "удалена" и не использована
					DFS(matrix, matrix.matrix[j][v], exc, used);
				}
			}
		}
	}

}

