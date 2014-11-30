package test.benchmark;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ Benchmark_Runtime_0Precision.class,
		Benchmark_Runtime_10Precision.class,
		Benchmark_Runtime_16Precision.class,
		Benchmark_Runtime_5Precision.class, Benchmark_Runtime_Baseline.class })
public class AllTests {

}
