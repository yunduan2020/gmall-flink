package com.tom.app;

import com.tom.bean.Bean1;
import com.tom.bean.Bean2;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.ProcessJoinFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

public class FlinkDataStreamJoinTest {
    public static void main(String[] args) throws Exception {
        // 1. 获取执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        // 2. 读取两个端口数据创建流并提取时间戳生成WaterMark
        SingleOutputStreamOperator<Bean1> stream1 = env.socketTextStream("hadoop102", 8888)
                .map(line -> {
                    String[] fields = line.split(",");
                    return new Bean1(fields[0], fields[1], Long.parseLong(fields[2]));
                }).assignTimestampsAndWatermarks(WatermarkStrategy.<Bean1>forMonotonousTimestamps()
                        .withTimestampAssigner(new SerializableTimestampAssigner<Bean1>() {
                            @Override
                            public long extractTimestamp(Bean1 element, long recordTimeStamp) {
                                return element.getTs() * 1000L;
                            }
                        }));
        SingleOutputStreamOperator<Bean2> stream2 = env.socketTextStream("hadoop102", 9999)
                .map(line -> {
                    String[] fields = line.split(",");
                    return new Bean2(fields[0], fields[1], Long.parseLong(fields[2]));
                }).assignTimestampsAndWatermarks(WatermarkStrategy.<Bean2>forMonotonousTimestamps()
                        .withTimestampAssigner(new SerializableTimestampAssigner<Bean2>() {
                            @Override
                            public long extractTimestamp(Bean2 element, long recordTimeStamp) {
                                return element.getTs() * 1000L;
                            }
                        }));

        // 3. 双流JOIN
        SingleOutputStreamOperator<Tuple2<Bean1, Bean2>> joinDS = stream1.keyBy(Bean1::getId)
                .intervalJoin(stream2.keyBy(Bean2::getId))
                .between(Time.seconds(-5), Time.seconds(5))
                // .lowerBoundExclusive()
                // .upperBoundExclusive()
                .process(new ProcessJoinFunction<Bean1, Bean2, Tuple2<Bean1, Bean2>>() {
                    @Override
                    public void processElement(Bean1 left, Bean2 right, ProcessJoinFunction<Bean1, Bean2, Tuple2<Bean1, Bean2>>.Context ctx, Collector<Tuple2<Bean1, Bean2>> out) throws Exception {
                        out.collect(new Tuple2<>(left, right));
                    }
                });

        // 4. 打印
        joinDS.print();

        // 5. 启动任务
        env.execute();
    }

}
