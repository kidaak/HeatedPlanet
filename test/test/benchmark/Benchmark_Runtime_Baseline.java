package test.benchmark;

import static org.junit.Assert.*;

import java.io.PrintStream;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.AbstractBenchmark;
import com.carrotsearch.junitbenchmarks.BenchmarkOptions;

import simulation.Earth;
import common.EarthGridProperties;
import common.EarthGridProperties.EarthGridProperty;
import database.SimulationDatabase;

@BenchmarkOptions(callgc = true, benchmarkRounds = Constants.numberOfBenchmarkRounds, warmupRounds = 5)
public class Benchmark_Runtime_Baseline extends AbstractBenchmark{
	
	Earth model;
	
	Runtime runtime = Runtime.getRuntime();
	private PrintStream original = System.out;	
	
	@Test
	public void benchmark_ThreadSleep10() throws Exception {
		runTest(Constants.argsBaseline10);
	}
	
	@Test
	public void benchmark_ThreadSleep100() throws Exception {
		runTest(Constants.argsBaseline100);
	}
	
	@Test
	public void benchmark_ThreadSleep1000() throws Exception {
		runTest(Constants.argsBaseline1000);
	}
	
	private void runTest(EarthGridProperties simProp){
		int sleepCount = Integer.parseInt(simProp.getPropertyString(EarthGridProperty.NAME));
		while(true) {
			try {
				Thread.sleep(sleepCount);
				break;
			} catch (InterruptedException e) {
				assertEquals(true, e.getMessage().contains("Simulation Completed!"));
				break;
			} catch (Exception e){
				e.printStackTrace();
				fail("ERROR: "+e.getMessage());
				break;
			}
		}
	}
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		SimulationDatabase.getSimulationDatabase().resetDatabase();
		System.gc();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		SimulationDatabase.getSimulationDatabase().resetDatabase();
		System.gc();
	}

	@Before
	public void setUp() throws Exception {
		disableOutput();
		SimulationDatabase.getSimulationDatabase().resetDatabase();
		System.gc();
		runtime.gc();
	}

	@After
	public void tearDown() throws Exception {
		enableOutput();
		SimulationDatabase.getSimulationDatabase().resetDatabase();
		System.gc();
		runtime.gc();
	}
	
	private void disableOutput(){
		System.setOut(Constants.outNull);
	}
	
	private void enableOutput(){
		System.setOut(original);
	}
}
