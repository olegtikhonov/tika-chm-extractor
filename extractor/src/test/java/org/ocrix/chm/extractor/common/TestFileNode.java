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

package org.ocrix.chm.extractor.common;

import static org.junit.Assert.*;

import org.apache.tika.metadata.Metadata;
import org.junit.Before;
import org.junit.Test;


/**
 * Tests {@link FileNode} functionality.
 */
public class TestFileNode {
    private static final String CONTEXT = "Hurray! That was great, Squidward! All those wrong notes you played made it sound more original.";
	private FileNode fn;
	
	@Before
	public void setUp() throws Exception {
		fn = new FileNode();
	}

	@Test
	public void testGetContext() {
		fn.setContext(CONTEXT);
		assertEquals(CONTEXT, fn.getContext());
	}

	@Test
	public void testSetContext() {
		testGetContext();
	}

	@Test
	public void testGetMetadata() {
		Metadata md = new Metadata();
		md.set("Sandy Cheeks", "Holy guacamole!");
		fn.setMetadata(md.toString());
		assertEquals(md.toString(), fn.getMetadata());
	}

	@Test
	public void testSetMetadata() {
		testGetMetadata();
	}

	@Test
	public void testToString() {
		assertNotNull(fn.toString());
	}

	@Test
	public void testGetAdditionalInfo() {
		String ADDITIONAL_INFO = "I wumbo, you wumbo, he-she-me wumbo. Wumboing, wumbology, the study of wumbo! It's first grade Spongebob";
		fn.setAdditionalInfo(ADDITIONAL_INFO);
		assertEquals(ADDITIONAL_INFO, fn.getAdditionalInfo());
	}

	@Test
	public void testSetAdditionalInfo() {
		testGetAdditionalInfo();
	}
	
	@Test
	public void testToStringNotNullCases() {
		String context = "Holographic Meatloaf? My favorite!";
		String metadata = "I am the master at Kara-tay.";
		fn.setContext(context);
		fn.setMetadata(metadata);
		assertNotNull(fn.toString());
	}
}
