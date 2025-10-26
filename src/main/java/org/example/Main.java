package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.nio.file.Files;
import java.util.*;

public class Main {
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        try {
            ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

            File inFile = new File("src/main/resources/input/assigment_3.json");
            if (!inFile.exists()) inFile = new File("src/main/resources/input/input.json");

            if (!inFile.exists()) {
                System.err.println("Input JSON not found");
                return;
            } else {
                System.out.println("Loading data from: " + inFile.getPath());
            }

            Map<String, Object> input = mapper.readValue(inFile, Map.class);
            List<Map<String, Object>> graphs = (List<Map<String, Object>>) input.get("graphs");
            if (graphs == null || graphs.isEmpty()) {
                System.err.println("No graphs found in JSON.");
                return;
            }

            List<Map<String, Object>> results = new ArrayList<>();

            for (Map<String, Object> g : graphs) {
                String name = Objects.toString(g.getOrDefault("name", "graph"));
                List<String> nodes = (List<String>) g.get("nodes");
                List<Map<String, Object>> edges = (List<Map<String, Object>>) g.get("edges");

                Graph graph = new Graph(nodes.size());
                Map<String, Integer> index = new HashMap<>();
                for (int i = 0; i < nodes.size(); i++) index.put(nodes.get(i), i);

                for (Map<String, Object> e : edges) {
                    int u = index.get(e.get("from").toString());
                    int v = index.get(e.get("to").toString());
                    int w = ((Number) e.get("weight")).intValue();
                    graph.addEdge(u, v, w);
                }

                PerformanceTracker primTracker = new PerformanceTracker();
                PerformanceTracker kruskalTracker = new PerformanceTracker();

                PrimsAlgorithm prim = new PrimsAlgorithm(primTracker);
                KruskalsAlgorithm kruskal = new KruskalsAlgorithm(kruskalTracker);

                List<Edge> primMST = prim.findMST(graph);
                List<Edge> kruskalMST = kruskal.findMST(graph);

                int primCost = primMST.stream().mapToInt(Edge::getWeight).sum();
                int kruskalCost = kruskalMST.stream().mapToInt(Edge::getWeight).sum();

                boolean primConnected = primMST.size() == nodes.size() - 1;
                boolean kruskalConnected = kruskalMST.size() == nodes.size() - 1;

                Map<String, Object> res = new LinkedHashMap<>();
                res.put("graph_name", name);
                res.put("vertices", nodes.size());
                res.put("edges", edges.size());

                res.put("prim_cost", primCost);
                res.put("prim_mst_edges", primMST.size());
                res.put("prim_connected", primConnected);
                res.put("prim_time_ms", primTracker.getExecutionTimeMillis());
                res.put("prim_comparisons", primTracker.getComparisons());
                res.put("prim_unions", primTracker.getUnionOperations());
                res.put("prim_finds", primTracker.getFindOperations());
                res.put("prim_mst", toEdgeListForJson(primMST, nodes));

                res.put("kruskal_cost", kruskalCost);
                res.put("kruskal_mst_edges", kruskalMST.size());
                res.put("kruskal_connected", kruskalConnected);
                res.put("kruskal_time_ms", kruskalTracker.getExecutionTimeMillis());
                res.put("kruskal_comparisons", kruskalTracker.getComparisons());
                res.put("kruskal_unions", kruskalTracker.getUnionOperations());
                res.put("kruskal_finds", kruskalTracker.getFindOperations());
                res.put("kruskal_mst", toEdgeListForJson(kruskalMST, nodes));

                res.put("same_cost", primCost == kruskalCost && primConnected && kruskalConnected);

                results.add(res);

                System.out.printf("%s: Prim=%d, Kruskal=%d, same=%s%n",
                        name, primCost, kruskalCost, primCost == kruskalCost);
            }


            File outJson = new File("results/output.json");
            outJson.getParentFile().mkdirs();
            mapper.writeValue(outJson, Map.of("results", results));
            System.out.println("Saved JSON: " + outJson.getPath());

            StringBuilder csv = new StringBuilder(
                    "graph,vertices,edges," +
                            "prim_cost,prim_mst_edges,prim_connected,prim_time_ms,prim_comparisons,prim_unions,prim_finds," +
                            "kruskal_cost,kruskal_mst_edges,kruskal_connected,kruskal_time_ms,kruskal_comparisons,kruskal_unions,kruskal_finds," +
                            "same_cost\n"
            );

            for (Map<String, Object> r : results) {
                csv.append(r.get("graph_name")).append(",")
                        .append(r.get("vertices")).append(",")
                        .append(r.get("edges")).append(",")
                        .append(r.get("prim_cost")).append(",")
                        .append(r.get("prim_mst_edges")).append(",")
                        .append(r.get("prim_connected")).append(",")
                        .append(r.get("prim_time_ms")).append(",")
                        .append(r.get("prim_comparisons")).append(",")
                        .append(r.get("prim_unions")).append(",")
                        .append(r.get("prim_finds")).append(",")
                        .append(r.get("kruskal_cost")).append(",")
                        .append(r.get("kruskal_mst_edges")).append(",")
                        .append(r.get("kruskal_connected")).append(",")
                        .append(r.get("kruskal_time_ms")).append(",")
                        .append(r.get("kruskal_comparisons")).append(",")
                        .append(r.get("kruskal_unions")).append(",")
                        .append(r.get("kruskal_finds")).append(",")
                        .append(r.get("same_cost")).append("\n");
            }

            File csvFile = new File("results/performance.csv");
            Files.write(csvFile.toPath(), csv.toString().getBytes());
            System.out.println("Saved CSV: " + csvFile.getPath());

            System.out.println("Done");

        } catch (Exception e) {
            System.err.println("Execution error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static List<Map<String, Object>> toEdgeListForJson(List<Edge> mst, List<String> nodes) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (Edge e : mst) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("from", nodes.get(e.getSource()));
            row.put("to", nodes.get(e.getDestination()));
            row.put("weight", e.getWeight());
            list.add(row);
        }
        return list;
    }
}