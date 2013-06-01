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

package org.ocrix.chm.server.command;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.ocrix.chm.extractor.ChmSevenZipExtractor;


public class TestMetadataFileExtractor {

	@Mock HttpServletRequest request = mock(HttpServletRequest.class);
	@Mock HttpServletResponse response = mock(HttpServletResponse.class);
	@Mock ChmSevenZipExtractor chmExtractor = mock(ChmSevenZipExtractor.class);
	@Mock ServletOutputStream out = mock(ServletOutputStream.class);
	@Mock File file = mock(File.class);
	
	
	@Before
	public void setUp() throws Exception {
		initMocks(this);
		MetadataFileExtractor.INSTANCE.setChmExtractor(chmExtractor);
		MetadataFileExtractor.INSTANCE.setRequest(request);
		MetadataFileExtractor.INSTANCE.setResponse(response);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testExecute() throws IOException {
		when(request.getRequestURI()).thenReturn("/file/target.chm");
		String str = "home" + File.separator + "user" + File.separator + "files" + File.separator + "test.chm"; 
		when(chmExtractor.getPathToExtractedFiles()).thenReturn(str);
		when(response.getOutputStream()).thenReturn(out);
		MetadataFileExtractor.INSTANCE.execute();
		assertTrue(true);
	}

	@Test
	public void testGetRequest() {
		assertEquals(request, MetadataFileExtractor.INSTANCE.getRequest());
	}

	@Test
	public void testSetRequest() {
		MetadataFileExtractor.INSTANCE.setRequest(request);
		assertEquals(request, MetadataFileExtractor.INSTANCE.getRequest());
	}

	@Test
	public void testGetResponse() {
		MetadataFileExtractor.INSTANCE.setResponse(response);
		assertEquals(response, MetadataFileExtractor.INSTANCE.getResponse());
	}

	@Test
	public void testSetResponse() {
		testGetResponse();
	}

	@Test
	public void testGetChmExtractor() {
		MetadataFileExtractor.INSTANCE.setChmExtractor(chmExtractor);
		assertEquals(chmExtractor, MetadataFileExtractor.INSTANCE.getChmExtractor());
	}

	@Test
	public void testSetChmExtractor() {
		testGetChmExtractor();
	}
}
