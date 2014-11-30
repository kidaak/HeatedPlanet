package test.benchmark;

import static org.junit.Assert.*;

import java.io.File;
import java.io.PrintStream;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.AbstractBenchmark;
import com.carrotsearch.junitbenchmarks.BenchmarkOptions;

import persistenceManager.PersistenceManager;
import simulation.Earth;
import common.EarthGridProperties;
import database.SimulationDatabase;

@BenchmarkOptions(callgc = true, benchmarkRounds = Constants.numberOfBenchmarkRounds, warmupRounds = 5)
public class Benchmark_Runtime_0Precision extends AbstractBenchmark {

	Earth model;
	PersistenceManager pm;
	File dbFile;
	
	Runtime runtime = Runtime.getRuntime();
	private PrintStream original = System.out;
	
	@Test
	public void args0PR_1GP_1TP() {
		runTest(Constants.args0PR_1GP_1TP);
	}
	@Test
	public void args0PR_1GP_5TP() {
		runTest(Constants.args0PR_1GP_5TP);
	}
	@Test
	public void args0PR_1GP_10TP() {
		runTest(Constants.args0PR_1GP_10TP);
	}
	@Test
	public void args0PR_1GP_50TP() {
		runTest(Constants.args0PR_1GP_50TP);
	}
	@Test
	public void args0PR_1GP_100TP() {
		runTest(Constants.args0PR_1GP_100TP);
	}
		
	@Test
	public void args0PR_5GP_1TP() {
		runTest(Constants.args0PR_5GP_1TP);
	}
	@Test
	public void args0PR_5GP_5TP() {
		runTest(Constants.args0PR_5GP_5TP);
	}
	@Test
	public void args0PR_5GP_10TP() {
		runTest(Constants.args0PR_5GP_10TP);
	}
	@Test
	public void args0PR_5GP_50TP() {
		runTest(Constants.args0PR_5GP_50TP);
	}
	@Test
	public void args0PR_5GP_100TP() {
		runTest(Constants.args0PR_5GP_100TP);
	}
		
	@Test
	public void args0PR_10GP_1TP() {
		runTest(Constants.args0PR_10GP_1TP);
	}
	@Test
	public void args0PR_10GP_5TP() {
		runTest(Constants.args0PR_10GP_5TP);
	}
	@Test
	public void args0PR_10GP_10TP() {
		runTest(Constants.args0PR_10GP_10TP);
	}
	@Test
	public void args0PR_10GP_50TP() {
		runTest(Constants.args0PR_10GP_50TP);
	}
	@Test
	public void args0PR_10GP_100TP() {
		runTest(Constants.args0PR_10GP_100TP);
	}
		
	@Test
	public void args0PR_50GP_1TP() {
		runTest(Constants.args0PR_50GP_1TP);
	}
	@Test
	public void args0PR_50GP_5TP() {
		runTest(Constants.args0PR_50GP_5TP);
	}
	@Test
	public void defaultTest() {
		runTest(Constants.args0PR_50GP_10TP);
	}
	@Test
	public void args0PR_50GP_10TP() {
		runTest(Constants.args0PR_50GP_50TP);
	}
	@Test
	public void args0PR_50GP_100TP() {
		runTest(Constants.args0PR_50GP_100TP);
	}
		
	@Test
	public void args0PR_100GP_1TP() {
		runTest(Constants.args0PR_100GP_1TP);
	}
	@Test
	public void args0PR_100GP_5TP() {
		runTest(Constants.args0PR_100GP_5TP);
	}
	@Test
	public void args0PR_100GP_10TP() {
		runTest(Constants.args0PR_100GP_10TP);
	}
	@Test
	public void args0PR_100GP_50TP() {
		runTest(Constants.args0PR_100GP_50TP);
	}
	@Test
	public void args0PR_100GP_100TP() {
		runTest(Constants.args0PR_100GP_100TP);
	}
	
	
	/**
	 * Runs the simulation using the inputed EarthGridProperties
	 * 
	 * @param simProp EarthGridProperties object defining the simulation to run.
	 */
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
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		SimulationDatabase.getSimulationDatabase().resetDatabase();
	}

	@Before
	public void setUp() throws Exception {
		disableOutput();
		model = new Earth();
		pm = new PersistenceManager();
	}

	@After
	public void tearDown() throws Exception {
		enableOutput();
		dbFile = new File(Constants.DATABASE_FILE);
		if(dbFile.exists())
			System.out.println("#DBFILESIZE:"+dbFile.length());
		model = null;
		pm = null;
	}
	
	private void disableOutput(){
		System.setOut(Constants.outNull);
	}
	
	private void enableOutput(){
		System.setOut(original);
	}

}
