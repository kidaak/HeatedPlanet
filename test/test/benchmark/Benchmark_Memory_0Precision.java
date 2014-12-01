package test.benchmark;

import static org.junit.Assert.*;

import java.io.PrintStream;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import persistenceManager.PersistenceManager;

import com.carrotsearch.junitbenchmarks.AbstractBenchmark;
import com.carrotsearch.junitbenchmarks.BenchmarkOptions;

import common.EarthGridProperties;
import database.SimulationDatabase;
import simulation.Earth;
import test.benchmark.Constants;

@BenchmarkOptions(callgc = true, benchmarkRounds = Constants.numberOfBenchmarkRounds, warmupRounds = 5)
public class Benchmark_Memory_0Precision extends AbstractBenchmark{
	
	Earth model;
	static PersistenceManager pm;
	long memoryBefore, memoryAfter, maxMemoryBefore,maxMemoryAfter;
	Runtime runtime = Runtime.getRuntime();
	private PrintStream original = System.out;
	
	@Test
	public void args0PR_1GP_1TP() {
		System.out.print("*"+this.getClass().getSimpleName()+".args0PR_1GP_1TP,");
		disableOutput();
		runTest(Constants.args0PR_1GP_1TP);
		enableOutput();
	}
	//TODO Add the rest
	
	
	
	private void runTest(EarthGridProperties simProp){
		model.configure(simProp);
		model.start();
		while(true) {
			try {
				model.generate();
				pm.processMessageQueue();
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
		System.out.println("*Test,MemoryUsedBeforeKB,MemoryUsedAfterKB,MaxMemoryBeforeKB,MaxMemoryAfterKB");
		SimulationDatabase.getSimulationDatabase().resetDatabase();
		pm = new PersistenceManager();
		System.gc();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		SimulationDatabase.getSimulationDatabase().resetDatabase();
		System.gc();
	}

	@Before
	public void setUp() throws Exception {
		System.gc();
		runtime.gc();
		model = new Earth();
		memoryBefore = (runtime.totalMemory()-runtime.freeMemory())/Constants.KB;
		maxMemoryBefore = runtime.maxMemory()/Constants.KB;
	}

	@After
	public void tearDown() throws Exception {
		memoryAfter = (runtime.totalMemory()-runtime.freeMemory())/Constants.KB;
		maxMemoryAfter = runtime.maxMemory()/Constants.KB;
		System.out.println(memoryBefore+","+memoryAfter+","+maxMemoryBefore+","+maxMemoryAfter);
		model = null;
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
