package com.dellemc.flink.hackthaon.pro.metrics;

import com.dellemc.flink.hackthaon.pro.ChipMetadata;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.metrics.Counter;
import org.apache.flink.metrics.Histogram;
import org.apache.flink.runtime.metrics.DescriptiveStatisticsHistogram;

public class FlinkMetricsExposingMapFunction extends RichMapFunction<ChipMetadata, Integer> {
  private static final long serialVersionUID = 1L;

  private transient Counter eventCounter;
  private transient Histogram valueHistogram;

  @Override
  public void open(Configuration parameters) {
    eventCounter = getRuntimeContext().getMetricGroup().counter("events");
    valueHistogram =
        getRuntimeContext()
            .getMetricGroup()
            .histogram("value_histogram", new DescriptiveStatisticsHistogram(10_000));
  }

  @Override
  public Integer map(ChipMetadata value) {
    eventCounter.inc();
    valueHistogram.update(value.getDefectsLen());
    return value.getDefectsLen();
  }
}