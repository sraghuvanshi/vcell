package org.vcell.util;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class PropertyLoaderTest {


		@Test
		public void testSystemTempDir( ) throws IOException {
			File tempDir = PropertyLoader.getSystemTemporaryDirectory();
			System.out.println(tempDir);
			File secondCall  = PropertyLoader.getSystemTemporaryDirectory();
			assertTrue(tempDir == secondCall);
		}
}
