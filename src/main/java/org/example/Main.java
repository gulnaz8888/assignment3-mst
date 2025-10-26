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
                System.err.println("Input JSON not found! Check path: src/main/resources/input/");
                return;
            } else {
                System.out.println("Loading data from: " + inFile.getPath());
            }

            Map<String, Object> input = mapper.readValue(inFile, Map.class);
            List<Map<String, Object>> graphs = (List<Map<String, Object>>) input.get("graphs");
            if (graphs == null || graphs.isEmpty()) {
                System.err.println("No graphs found in JSON (key 'graphs' missing or empty).");
                return;
            }

            List<Map<String, Object>> results = new ArrayList<>();

            for (Map<String, Object> g : graphs) {
                String name = Objects.toString(g.getOrDefault("name", "graph"));
                List<String> nodes = (List<String>) g.get("nodes");
                List<Map<String, Object>> edges = (List<Map<String, Object>>) g.get("edges");

                Graph graph = new Graph(nodes.size());
                Map<String, Integer> index = new HashMap<>();
                for (int i = 0; i < nodes.size(); i++) {
                    index.put(nodes.get(i), i);
                }

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

                Map<String, Object> res = new LinkedHashMap<>();
                res.put("graph_name", name);
                res.put("vertices", nodes.size());
                res.put("edges", edges.size());
                res.put("prim_cost", primCost);
                res.put("kruskal_cost", kruskalCost);
                res.put("same_cost", primCost == kruskalCost);
                res.put("prim_time_ms", primTracker.getExecutionTimeMillis());
                res.put("kruskal_time_ms", kruskalTracker.getExecutionTimeMillis());
                res.put("prim_ops", primTracker.getTotalOperations());
                res.put("kruskal_ops", kruskalTracker.getTotalOperations());
                results.add(res);

                System.out.printf("%s: Prim=%d, Kruskal=%d, same=%s%n",
                        name, primCost, kruskalCost, primCost == kruskalCost);
            }

            File outJson = new File("results/output.json");
            outJson.getParentFile().mkdirs();
            mapper.writeValue(outJson, Map.of("results", results));
            System.out.println("Saved file: " + outJson.getPath());

            StringBuilder csv = new StringBuilder(
                    "graph,vertices,edges,prim_cost,kruskal_cost,same_cost,prim_time_ms,kruskal_time_ms,prim_ops,kruskal_ops\n"
            );
            for (Map<String, Object> r : results) {
                csv.append(r.get("graph_name")).append(",")
                        .append(r.get("vertices")).append(",")
                        .append(r.get("edges")).append(",")
                        .append(r.get("prim_cost")).append(",")
                        .append(r.get("kruskal_cost")).append(",")
                        .append(r.get("same_cost")).append(",")
                        .append(r.get("prim_time_ms")).append(",")
                        .append(r.get("kruskal_time_ms")).append(",")
                        .append(r.get("prim_ops")).append(",")
                        .append(r.get("kruskal_ops")).append("\n");
            }

            File csvFile = new File("results/performance.csv");
            Files.write(csvFile.toPath(), csv.toString().getBytes());
            System.out.println("Saved CSV: " + csvFile.getPath());

            System.out.println("Done! All results have been saved successfully.");

        } catch (Exception e) {
            System.err.println("Execution error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}