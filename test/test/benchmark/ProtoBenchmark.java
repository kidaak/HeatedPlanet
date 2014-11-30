package test.benchmark;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.AbstractBenchmark;
import com.carrotsearch.junitbenchmarks.BenchmarkOptions;

import common.EarthGridProperties;
import simulation.Earth;
import test.benchmark.Constants;

@BenchmarkOptions(callgc = true, benchmarkRounds = Constants.numberOfBenchmarkRounds, warmupRounds = 5)
public class ProtoBenchmark  extends AbstractBenchmark{

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		runTest(Constants.args6PR_100GP_100TP);
	}
	
	private void runTest(EarthGridProperties simProp){
		Earth model = new Earth();
		
		model.configure(simProp);
		model.start();
		while(true) {
			try {
				model.generate();
			} catch (InterruptedException e) {
				fail("ERROR: "+e.getMessage());
				break;
			}		
		}
	}
	
}
