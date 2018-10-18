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

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ocrix.chm.extractor.common.FileNode;


/**
 * Tests {@link ChmSevenZipExtractor} functionality.
 * /auto/dev_users/otikhon/Downloads/myrepo/tika-chm-extractor/extractor/src/test/resources/Open_Mobile_Client_Help.chm
 */
public class TestChmSevenZipExtractor {
	
	private ChmSevenZipExtractor chmExtractor;
	private static final String FILE = "src/test/resources/Open_Mobile_Client_Help.chm";
	private static final String FILE_TXT = "src/test/resources/README.txt";
	private  static final String VP_PATH = "extracted_files";

	@Before
	public void setUp() throws Exception {
		chmExtractor = new ChmSevenZipExtractor();
	}

	
	@After
	public void tearDown() {
		if(new File(chmExtractor.getPathToExtractedFiles()).exists()){
			deleteFolder(new File(chmExtractor.getPathToExtractedFiles()));
		}
	}
 
	@Test
	public void testMetadata() {
		chmExtractor.metadata(new File(FILE).getAbsolutePath());
		assertTrue(true);
	}

	@Test
	public void testExtractMetadata() {
		String md = chmExtractor.extractMetadata(new File(FILE).getAbsolutePath());
		assertNotNull(md);
	}

	@Test
	public void testExtractTrue() {
		FileNode result = chmExtractor.extract(new File(FILE).getAbsolutePath(), true);
		assertNotNull(result);
	}
	
	@Test
	public void testExtractDiffMimeType() {
		FileNode result = chmExtractor.extract(new File(FILE_TXT).getAbsolutePath(), true);
		assertNull(result);
	}
	
	@Test
	public void testExtractFalse() {
		FileNode result = chmExtractor.extract(new File(FILE).getAbsolutePath(), false);
		assertNotNull(result);
		
		result = chmExtractor.extract(new File(FILE).getAbsolutePath(), false);
		assertNotNull(result);
	}

	@Test
	public void testGetPathToExtractedFiles() {
		assertEquals(VP_PATH, chmExtractor.getPathToExtractedFiles());
	}
	
	/**
	 * Deletes files from folder.
	 * 
	 * @param folder to be deleted.
	 */
	private void deleteFolder(File folder) {
		File[] files = folder.listFiles();
		if (files != null) {
			for (File f : files) {
				if (f.isDirectory()) {
					deleteFolder(f);
				} else {
					f.delete();
				}
			}
		}
		folder.delete();
	}
}
