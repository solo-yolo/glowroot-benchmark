/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.glowroot.benchmark;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Charsets;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;

public class ResultFormatter {

    public static void main(String[] args) throws IOException {
        ArrayNode results = readResults(args[0]);

        Score baseline = readScore(results, ServletBenchmark.class.getName(), StartupBenchmark.class.getName());
        Score glowroot = readScore(results, ServletWithGlowrootBenchmark.class.getName(), StartupWithGlowrootBenchmark.class.getName());
        Score newrelic = readScore(results, ServletWithNewRelicBenchmark.class.getName(), StartupWithNewRelicBenchmark.class.getName());

        printScores(baseline, glowroot, "glowroot");
        printScores(baseline, newrelic, "newrelic");
    }

    private static Score readScore(ArrayNode results, String responseTimeBenchmarkName, String startupBenchmarkName) {
        Score score = new Score();

        for (JsonNode result : results) {
            String rawBenchmarkName = result.get("benchmark").asText();
            String benchmarkName = rawBenchmarkName.substring(0, rawBenchmarkName.lastIndexOf('.'));

            ObjectNode primaryMetric = (ObjectNode) result.get("primaryMetric");
            double primaryMetricScore = primaryMetric.get("score").asDouble();
            if (benchmarkName.equals(startupBenchmarkName)) {
                score.st = primaryMetricScore;
            } else if (benchmarkName.equals(responseTimeBenchmarkName)) {
                ObjectNode scorePercentiles = (ObjectNode) primaryMetric.get("scorePercentiles");
                score.rtAvg = primaryMetricScore;
                score.rt50 = scorePercentiles.get("50.0").asDouble();
                score.rt95 = scorePercentiles.get("95.0").asDouble();
                score.rt99 = scorePercentiles.get("99.0").asDouble();
                score.rt999 = scorePercentiles.get("99.9").asDouble();
                score.rt9999 = scorePercentiles.get("99.99").asDouble();
                score.ab = getAllocatedBytes(result);
            }
        }

        return score;
    }

    private static void printScores(Score baseline, Score apm, String name) {
        System.out.format("%25s%14s%14s%14s%n", "", "baseline", name, "delta");
        printLine("Response time (avg)", "µs", baseline.rtAvg, apm.rtAvg, apm.rtAvg - baseline.rtAvg);
        printLine("Response time (50th)", "µs", baseline.rt50, apm.rt50, apm.rt50 - baseline.rt50);
        printLine("Response time (95th)", "µs", baseline.rt95, apm.rt95, apm.rt95 - baseline.rt95);
        printLine("Response time (99th)", "µs", baseline.rt99, apm.rt99, apm.rt99 - baseline.rt99);
        printLine("Response time (99.9th)", "µs", baseline.rt999, apm.rt999, apm.rt999 - baseline.rt999);
        printLine("Response time (99.99th)", "µs", baseline.rt9999, apm.rt9999, apm.rt9999 - baseline.rt9999);
        printLine("Allocation memory per req", "kb", baseline.ab, apm.ab, apm.ab - baseline.ab);
        printLine("Startup time (avg)", "ms", baseline.st, apm.st, apm.st - baseline.st);
        System.out.println();
    }

    private static void printLine(String label, String unit, double baseline, double apm, double delta) {
        System.out.format("%-25s %10.3f %s %10.3f %s %10.3f %s%n", label, baseline, unit, apm, unit, delta, unit);
    }

    private static ArrayNode readResults(String pathname) throws IOException {
        File file = new File(pathname);
        ObjectMapper mapper = new ObjectMapper();
        String content = Files.toString(file, Charsets.UTF_8);
        content = content.replaceAll("\n", " ");
        ArrayNode results = (ArrayNode) mapper.readTree(content);
        return results;
    }

    private static double getAllocatedBytes(JsonNode result) {
        ObjectNode secondaryMetrics = (ObjectNode) result.get("secondaryMetrics");
        if (secondaryMetrics == null) {
            return -1;
        }
        ObjectNode gcAllocRateNorm = (ObjectNode) secondaryMetrics.get("\u00b7gc.alloc.rate.norm");
        if (gcAllocRateNorm == null) {
            return -1;
        }
        return gcAllocRateNorm.get("score").asDouble();
    }

    private static class Score {
        private double rtAvg;
        private double rt50;
        private double rt95;
        private double rt99;
        private double rt999;
        private double rt9999;
        private double ab;
        private double st;
    }

}
