package org.example;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.jet.config.JobConfig;
import com.hazelcast.jet.pipeline.*;
import com.hazelcast.jet.pipeline.test.TestSources;
import com.hazelcast.jet.python.PythonServiceConfig;

import static com.hazelcast.jet.python.PythonTransforms.mapUsingPython;

public class JetJob {
  public static void main(String[] args) {
    Pipeline pipeline = Pipeline.create();
    pipeline.readFrom(TestSources.itemStream(10, (ts, seq) -> String.valueOf(seq)))
    .withoutTimestamps()
    .apply(mapUsingPython(new PythonServiceConfig()
            .setBaseDir("src/main/python")
            .setHandlerModule("take_sqrt")))
    .setLocalParallelism(1)
    .writeTo(Sinks.logger());

    JobConfig cfg = new JobConfig().setName("python-function");
    HazelcastInstance hz = Hazelcast.bootstrappedInstance();
    hz.getJet().newJob(pipeline, cfg);
  }
}