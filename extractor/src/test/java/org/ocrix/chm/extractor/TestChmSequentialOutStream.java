/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package org.ocrix.chm.extractor;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import net.sf.sevenzipjbinding.SevenZipException;
import org.apache.tika.metadata.Metadata;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link ChmSequentialOutStream} functionality.
 */
public class TestChmSequentialOutStream {

	private ChmSequentialOutStream stream;
	private static final String path = "target";
	
	@Before
	public void setUp() throws Exception {
		List<byte[]> extData = new ArrayList<byte[]>();
		extData.add(this.getClass().getName().getBytes());
		
		List<Metadata> accMetadata = new ArrayList<Metadata>();
		Metadata m = new Metadata();
		m.set("test", "test");
		
		accMetadata.add(m);
		stream = new ChmSequentialOutStream(extData, accMetadata);
	}

	@Test
	public void testChmSequentialOutStream() {
		assertNotNull(stream);
	}

	@Test
	public void testWrite() throws SevenZipException {
		String data = "Not yet implemented";
		stream.setPath(path);
		int bytesWritten = stream.write(data.getBytes());
		assertEquals(data.length(), bytesWritten);
	}

	@Test
	public void testGetPath() {
		stream.setPath(path);
		assertEquals(path, stream.getPath());
	}

	@Test
	public void testSetPath() {
		testGetPath();
	}

	@Test
	public void testIsDoSaveFiles() {
		stream.setDoSaveFiles(true);
		assertTrue(stream.isDoSaveFiles());
		
		stream.setDoSaveFiles(false);
		assertFalse(stream.isDoSaveFiles());
	}

	@Test
	public void testSetDoSaveFiles() {
		testIsDoSaveFiles();
	}

	@Test
	public void testSetChmExtractDir() {
		stream.setChmExtractDir(path);
		assertTrue(true);
	}
}
