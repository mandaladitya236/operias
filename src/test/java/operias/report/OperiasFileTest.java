package operias.report;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedList;

import operias.OperiasStatus;
import operias.cobertura.*;
import operias.diff.DiffFile;
import operias.diff.SourceDiffState;
import operias.test.general.ExitException;
import operias.test.general.NoExitSecurityManager;

import org.junit.Before;
import org.junit.Test;

import difflib.Chunk;
import difflib.DeleteDelta;
import difflib.Delta;
import difflib.InsertDelta;

public class OperiasFileTest {

	CoberturaClass originalClass, revisedClass;
	
	@Before
	public void setUp() {

		originalClass = new CoberturaClass("Simple" , "simple/Simple.java", "Simple", 1, 1);
		revisedClass = new CoberturaClass("Simple" , "simple/Simple.java", "Simple", 1, 1);
	}
	/**
	 * Increase the coverage on a line
	 */
	@Test
	public void testIncreaseCoverageWithoutSourceChange() {
				
		CoberturaLine line1 = new CoberturaLine(1, 1, false, false);
		CoberturaLine line2_1 = new CoberturaLine(2, 0, false, false);
		CoberturaLine line2_2 = new CoberturaLine(2, 5, false, false);
		
		CoberturaLine line3 = new CoberturaLine(3, 1, false, false);
		
		originalClass.addLine(line1);
		originalClass.addLine(line2_1);
		originalClass.addLine(line3);
		
		revisedClass.addLine(line1);
		revisedClass.addLine(line2_2);
		revisedClass.addLine(line3);
		
		DiffFile sourceDiff = new DiffFile("simple/Simple.java", SourceDiffState.SAME);
		
		OperiasFile oFile = new OperiasFile(originalClass, revisedClass, sourceDiff);
		
		LinkedList<OperiasChange> changes = (LinkedList<OperiasChange>) oFile.getChanges();
		
		assertEquals(1, changes.size());
		assertTrue(changes.getFirst() instanceof CoverageIncreaseChange);
		assertEquals(2, changes.getFirst().getOriginalLineNumber());
		assertEquals(2, changes.getFirst().getRevisedLineNumber());
		
	}
	
	/**
	 * Decrease the coverage on a line
	 */
	@Test
	public void testDecreaseCoverageWithoutSourceChange() {
			
		CoberturaLine line1 = new CoberturaLine(1, 1, false, false);
		CoberturaLine line2_1 = new CoberturaLine(2, 5, false, false);
		CoberturaLine line2_2 = new CoberturaLine(2, 0, false, false);
		
		CoberturaLine line3 = new CoberturaLine(3, 1, false, false);
		
		originalClass.addLine(line1);
		originalClass.addLine(line2_1);
		originalClass.addLine(line3);
		
		revisedClass.addLine(line1);
		revisedClass.addLine(line2_2);
		revisedClass.addLine(line3);
		
		DiffFile sourceDiff = new DiffFile("simple/Simple.java", SourceDiffState.SAME);
		
		OperiasFile oFile = new OperiasFile(originalClass, revisedClass, sourceDiff);
		
		LinkedList<OperiasChange> changes = (LinkedList<OperiasChange>) oFile.getChanges();
		
		assertEquals(1, changes.size());
		assertTrue(changes.getFirst() instanceof CoverageDecreaseChange);
		assertEquals(2, changes.getFirst().getOriginalLineNumber());
		assertEquals(2, changes.getFirst().getRevisedLineNumber());
	}
	
	/**
	 * Increase the coverage on a branch
	 */
	@Test
	public void testIncreaseCoverageOnBranch() {
		CoberturaClass originalClass = new CoberturaClass("Simple" , "simple/Simple.java", "Simple", 1, 1);
		CoberturaClass revisedClass = new CoberturaClass("Simple" , "simple/Simple.java", "Simple", 1, 1);
		
		CoberturaLine line1 = new CoberturaLine(1, 1, false, false);
		CoberturaLine line2_1 = new CoberturaLine(2, 5, true, false);
		CoberturaLine line2_2 = new CoberturaLine(2, 5, true, true);
		
		CoberturaLine line3 = new CoberturaLine(3, 1, false, false);
		
		originalClass.addLine(line1);
		originalClass.addLine(line2_1);
		originalClass.addLine(line3);
		
		revisedClass.addLine(line1);
		revisedClass.addLine(line2_2);
		revisedClass.addLine(line3);
		
		DiffFile sourceDiff = new DiffFile("simple/Simple.java", SourceDiffState.SAME);
		
		OperiasFile oFile = new OperiasFile(originalClass, revisedClass, sourceDiff);
		
		LinkedList<OperiasChange> changes = (LinkedList<OperiasChange>) oFile.getChanges();
		
		assertEquals(1, changes.size());
		assertTrue(changes.getFirst() instanceof CoverageIncreaseChange);
		assertEquals(2, changes.getFirst().getOriginalLineNumber());
		assertEquals(2, changes.getFirst().getRevisedLineNumber());
	}
	
	/**
	 * Decrease the coverage on a branch
	 */
	@Test
	public void testDecreaseCoverageOnBranch() {
		
		CoberturaLine line1 = new CoberturaLine(1, 1, false, false);
		CoberturaLine line2_1 = new CoberturaLine(2, 5, true, true);
		CoberturaLine line2_2 = new CoberturaLine(2, 5, true, false);
		
		CoberturaLine line3 = new CoberturaLine(3, 1, false, false);
		
		originalClass.addLine(line1);
		originalClass.addLine(line2_1);
		originalClass.addLine(line3);
		
		revisedClass.addLine(line1);
		revisedClass.addLine(line2_2);
		revisedClass.addLine(line3);
		
		DiffFile sourceDiff = new DiffFile("simple/Simple.java", SourceDiffState.SAME);
		
		OperiasFile oFile = new OperiasFile(originalClass, revisedClass, sourceDiff);
		
		LinkedList<OperiasChange> changes = (LinkedList<OperiasChange>) oFile.getChanges();
		
		assertEquals(1, changes.size());
		assertTrue(changes.getFirst() instanceof CoverageDecreaseChange);
		assertEquals(2, changes.getFirst().getOriginalLineNumber());
		assertEquals(2, changes.getFirst().getRevisedLineNumber());
	
	}
	
	@Test
	public void testUnchangedCode() {
				
		CoberturaLine line1 = new CoberturaLine(1, 0, false, false);
		CoberturaLine line2 = new CoberturaLine(2, 5, true, true);
		CoberturaLine line3 = new CoberturaLine(3, 5, true, false);
		
		CoberturaLine line4 = new CoberturaLine(4, 1, false, false);
		
		originalClass.addLine(line1);
		originalClass.addLine(line2);
		originalClass.addLine(line3);
		originalClass.addLine(line4);
		
		revisedClass.addLine(line1);
		revisedClass.addLine(line2);
		revisedClass.addLine(line3);
		revisedClass.addLine(line4);

		DiffFile sourceDiff = new DiffFile("simple/Simple.java", SourceDiffState.SAME);
		
		OperiasFile oFile = new OperiasFile(originalClass, revisedClass, sourceDiff);
		
		LinkedList<OperiasChange> changes = (LinkedList<OperiasChange>) oFile.getChanges();
		
		assertEquals(0, changes.size());
	}
	
	/**
	 * Test invalid data, such as missing lines or missing branches
	 */
	@Test
	public void testInvalidCoverageReports1() {
		
		CoberturaLine line1 = new CoberturaLine(1, 0, false, false);
		CoberturaLine line2 = new CoberturaLine(2, 0, false, false);

		originalClass.addLine(line1);
		revisedClass.addLine(line2);

		DiffFile sourceDiff = new DiffFile("simple/Simple.java", SourceDiffState.SAME);
		
		System.setSecurityManager(new NoExitSecurityManager());
		boolean exceptionThrown = false;
		
		try {
			new OperiasFile(originalClass, revisedClass, sourceDiff);
			
		} catch (ExitException e) {
	    	exceptionThrown = true;
            assertEquals("Invalid line comparison", OperiasStatus.ERROR_OPERIAS_INVALID_LINE_COMPARISON.ordinal(), e.status);
	    }
		
		assertTrue(exceptionThrown);
		System.setSecurityManager(null);
	}
	
	/**
	 * Test invalid data, such as missing lines or missing branches
	 */
	@Test
	public void testInvalidCoverageReports2() {
	
		CoberturaLine line1 = new CoberturaLine(1, 0, false, false);
		CoberturaLine line2 = new CoberturaLine(2, 0, false, false);

		originalClass.addLine(line2);
		revisedClass.addLine(line1);

		DiffFile sourceDiff = new DiffFile("simple/Simple.java", SourceDiffState.SAME);
		
		System.setSecurityManager(new NoExitSecurityManager());
		boolean exceptionThrown = false;
		
		try {
			new OperiasFile(originalClass, revisedClass, sourceDiff);
			
		} catch (ExitException e) {
	    	exceptionThrown = true;
            assertEquals("Invalid line comparison", OperiasStatus.ERROR_OPERIAS_INVALID_LINE_COMPARISON.ordinal(), e.status);
	    }
		
		assertTrue(exceptionThrown);
		System.setSecurityManager(null);
	}
	
	/**
	 * Test invalid data, such as missing lines or missing branches
	 */
	@Test
	public void testInvalidCoverageReports3() {
	
		CoberturaLine line1 = new CoberturaLine(1, 0, true, false);
		CoberturaLine line2 = new CoberturaLine(1, 0, false, false);

		originalClass.addLine(line1);
		revisedClass.addLine(line2);

		DiffFile sourceDiff = new DiffFile("simple/Simple.java", SourceDiffState.SAME);
		
		System.setSecurityManager(new NoExitSecurityManager());
		boolean exceptionThrown = false;
		
		try {
			new OperiasFile(originalClass, revisedClass, sourceDiff);
			
		} catch (ExitException e) {
	    	exceptionThrown = true;
            assertEquals("Invalid line comparison", OperiasStatus.ERROR_OPERIAS_INVALID_LINE_COMPARISON.ordinal(), e.status);
	    }
		
		assertTrue(exceptionThrown);
		System.setSecurityManager(null);
	}
	
	/**
	 * Test invalid data, such as missing lines or missing branches
	 */
	@Test
	public void testInvalidCoverageReports4() {
	
		CoberturaLine line1 = new CoberturaLine(1, 0, true, false);
		CoberturaLine line2 = new CoberturaLine(1, 0, false, false);

		originalClass.addLine(line2);
		revisedClass.addLine(line1);

		DiffFile sourceDiff = new DiffFile("simple/Simple.java", SourceDiffState.SAME);
		
		System.setSecurityManager(new NoExitSecurityManager());
		boolean exceptionThrown = false;
		
		try {
			new OperiasFile(originalClass, revisedClass, sourceDiff);
			
		} catch (ExitException e) {
	    	exceptionThrown = true;
            assertEquals("Invalid line comparison", OperiasStatus.ERROR_OPERIAS_INVALID_LINE_COMPARISON.ordinal(), e.status);
	    }
		
		assertTrue(exceptionThrown);
		System.setSecurityManager(null);
	}
	
	/**
	 * Test the insertion of some lines
	 */
	@Test
	public void testSimpleInsertion() {
		Chunk original = new Chunk(3, new ArrayList<String>());
		ArrayList<String> lines = new ArrayList<String>();
		lines.add("first line");
		lines.add("second line");
		lines.add("third line");
		Chunk revised = new Chunk(3, lines);
		InsertDelta delta = new InsertDelta(original, revised);
		
		LinkedList<Delta> changes = new LinkedList<Delta>();
		changes.add(delta);

		DiffFile sourceDiff = new DiffFile("simple/Simple.java", SourceDiffState.SAME);
		sourceDiff.setChanges(changes);
		
		CoberturaLine line1 = new CoberturaLine(1, 1, false, false);
		CoberturaLine line2 = new CoberturaLine(2, 1, false, false);
		
		CoberturaLine line3 = new CoberturaLine(3, 1, false, false);
		CoberturaLine line4 = new CoberturaLine(4, 0, false, false);
		
		CoberturaLine line6_1 = new CoberturaLine(6, 1, false, false);
		CoberturaLine line6_2 = new CoberturaLine(3, 1, false, false);


		originalClass.addLine(line1);
		originalClass.addLine(line2);
		originalClass.addLine(line6_2);
	

		revisedClass.addLine(line1);
		revisedClass.addLine(line2);
		revisedClass.addLine(line3);
		revisedClass.addLine(line4);
		revisedClass.addLine(line6_1);
		
		OperiasFile oFile = new OperiasFile(originalClass, revisedClass, sourceDiff);
		
		assertEquals(1, oFile.getChanges().size());
		assertTrue(oFile.getChanges().getFirst() instanceof InsertSourceChange);
		
		assertEquals(delta, oFile.getChanges().getFirst().getSourceDiffDelta());
		assertTrue(oFile.getChanges().getFirst().getRevisedCoverage().get(0));
		assertFalse(oFile.getChanges().getFirst().getRevisedCoverage().get(1));
		assertNull(oFile.getChanges().getFirst().getRevisedCoverage().get(2));
	}
	
	/**
	 * Test the deletion of some lines
	 */
	@Test
	public void testSimpleDeletion() {
		Chunk revised = new Chunk(3, new ArrayList<String>());
		ArrayList<String> lines = new ArrayList<String>();
		lines.add("first line");
		lines.add("second line");
		lines.add("third line");
		Chunk original = new Chunk(3, lines);
		DeleteDelta delta = new DeleteDelta(original, revised);
		
		LinkedList<Delta> changes = new LinkedList<Delta>();
		changes.add(delta);

		DiffFile sourceDiff = new DiffFile("simple/Simple.java", SourceDiffState.SAME);
		sourceDiff.setChanges(changes);
		
		CoberturaLine line1 = new CoberturaLine(1, 1, false, false);
		CoberturaLine line2 = new CoberturaLine(2, 1, false, false);
		
		CoberturaLine line3 = new CoberturaLine(3, 1, false, false);
		CoberturaLine line4 = new CoberturaLine(4, 0, false, false);
		
		CoberturaLine line6_1 = new CoberturaLine(6, 1, false, false);
		CoberturaLine line6_2 = new CoberturaLine(3, 1, false, false);


		revisedClass.addLine(line1);
		revisedClass.addLine(line2);
		revisedClass.addLine(line6_2);
	

		originalClass.addLine(line1);
		originalClass.addLine(line2);
		originalClass.addLine(line3);
		originalClass.addLine(line4);
		originalClass.addLine(line6_1);
		
		OperiasFile oFile = new OperiasFile(originalClass, revisedClass, sourceDiff);
		
		assertEquals(1, oFile.getChanges().size());
		assertTrue(oFile.getChanges().getFirst() instanceof DeleteSourceChange);
		
		assertEquals(delta, oFile.getChanges().getFirst().getSourceDiffDelta());
		assertTrue(oFile.getChanges().getFirst().getOriginalCoverage().get(0));
		assertFalse(oFile.getChanges().getFirst().getOriginalCoverage().get(1));
		assertNull(oFile.getChanges().getFirst().getOriginalCoverage().get(2));
		
	}
}
