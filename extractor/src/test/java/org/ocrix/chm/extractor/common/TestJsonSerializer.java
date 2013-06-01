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
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;


/**
 * Tests {@link JsonSerializer} functionality.
 */
public class TestJsonSerializer {
	private JsonSerializer serializer;

	@Before
	public void setUp() throws Exception {
		serializer = new JsonSerializer();
	}


	@Test
	public void testJsonSerializer() {
		assertNotNull(serializer);
	}

	@Test
	public void testDeserialize() throws IOException {
		String before = "You can’t fool me. I listen to public radio!";
		String serialized = serializer.serialize(before);
		String desirialized = serializer.deserialize(serialized);
		assertEquals(before, desirialized);
	}

	@Test
	public void testSerializeString() throws IOException {
		String str = "Nonsense, my vocabulary is infinitly expanding!";
		serializer.serialize(str);
		assertTrue(true);
	}

	@Test
	public void testSerializeFileNode() throws IOException {
		String context = "Can I have everybody’s attention?… I have to use the bathroom.";
		String metadata = "Good people don’t rip other people’s arms off.";
		FileNode fn = new FileNode();
		fn.setContext(context);
		fn.setMetadata(metadata);
		String result = serializer.serialize(fn);
		assertNotNull(result);
	}
}
