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
import simulation.Earth;

import com.carrotsearch.junitbenchmarks.AbstractBenchmark;

import common.EarthGridProperties;
import common.EarthGridProperties.EarthGridProperty;
import dao.EarthGridDao;
import dao.EarthGridResponse;
import dao.ResponseType;
import database.SimulationDatabase;

public class Benchmark_QueryDatabase extends AbstractBenchmark{
	
	Earth model;
	static PersistenceManager pm;
	static EarthGridDao dao;
	File dbFile;
	
	Runtime runtime = Runtime.getRuntime();
	private PrintStream original = System.out;
	
	@Test
	public void args6Pr_100GP_100TP_DB() {
		runTest(Constants.args6Pr_100GP_100TP,true);
	}
	
	@Test
	public void args6Pr_75GP_75TP_DB() {
		runTest(Constants.args6Pr_75GP_75TP,true);
	}
	
	@Test
	public void args6Pr_50GP_50TP_DB() {
		runTest(Constants.args6Pr_50GP_50TP,true);
	}
	
	@Test
	public void args6Pr_25GP_25TP_DB() {
		runTest(Constants.args6Pr_25GP_25TP,true);
	}
	
	@Test
	public void args6Pr_0GP_0TP_DB() {
		runTest(Constants.args6Pr_0GP_0TP,true);
	}
	
	
	private void runTest(EarthGridProperties simProp,boolean usePersistence){
		try {
			EarthGridResponse egr = dao.queryEarthGridSimulationByName(simProp.getPropertyString(EarthGridProperty.NAME));
			if(egr.getResult().equals(ResponseType.FOUND_MANY) || egr.getResult().equals(ResponseType.FOUND_ONE)){
				
			}else{
				fail("Did not find any results.");
			}
		} catch (Exception e){
			e.printStackTrace();
			fail("ERROR: "+e.getMessage());
		}
	}
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		//SimulationDatabase.getSimulationDatabase().resetDatabase();
		pm = new PersistenceManager();
		dao = EarthGridDao.getEarthGridDao();
		System.gc();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		System.gc();
	}

	@Before
	public void setUp() throws Exception {
		disableOutput();
		model = new Earth();
		System.gc();
		runtime.gc();
	}

	@After
	public void tearDown() throws Exception {
		enableOutput();
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
