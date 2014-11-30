package test.benchmark;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import common.EarthGridProperties;

import simulation.Earth;

public class ProtoBenchmark {

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
		Earth model = new Earth();
		EarthGridProperties simProp = new EarthGridProperties();
		
		model.configure(simProp);
		model.start();
		while(true) {
			try {
				model.generate();
			} catch (InterruptedException e) {
				break;
			}		
		}
		fail("Not yet implemented");
	}

}
