package test.benchmark;

import static org.junit.Assert.*;

import java.io.File;
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

//TODO Add the @BenchmarkOptions to each TestCase
@BenchmarkOptions(callgc = true, benchmarkRounds = Constants.numberOfBenchmarkRounds, warmupRounds = 5)
//TODO Add "extends AbstractBenchmark" to each TestCase
public class ProtoBenchmark extends AbstractBenchmark{
	
	//TODO Example Test Case. Note the test/method name is the same as the argument name
	// Need one of these for each argument
	@Test
	public void args6PR_100GP_100TP() {
		runTest(Constants.args6PR_100GP_100TP);
	}
	
	//Copy-Paste this body into each TestCase
	//TODO Start Copy
	Earth model;
	PersistenceManager pm;
	File dbFile;
	
	Runtime runtime = Runtime.getRuntime();
	private PrintStream original = System.out;
	
	
	
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
		model = new Earth();
		pm = new PersistenceManager();
		System.gc();
		runtime.gc();
	}

	@After
	public void tearDown() throws Exception {
		enableOutput();
		dbFile = new File(Constants.DATABASE_FILE);
		if(dbFile.exists())
			System.out.println("#DBFILESIZE:"+dbFile.length());
		model = null;
		pm = null;
		System.gc();
		runtime.gc();
	}
	
	private void disableOutput(){
		System.setOut(Constants.outNull);
	}
	
	private void enableOutput(){
		System.setOut(original);
	}
	//TODO End Copy
}
