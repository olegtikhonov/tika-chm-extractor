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
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.ocrix.chm.extractor.ChmSevenZipExtractor;

public class TestDirectoryFileExtractor {
	
	@Mock HttpServletRequest request = mock(HttpServletRequest.class);
	@Mock HttpServletResponse response = mock(HttpServletResponse.class);
	@Mock ChmSevenZipExtractor chmExtractor = mock(ChmSevenZipExtractor.class);
	@Mock File file = mock(File.class);

	@Before
	public void setUp() throws Exception {
		initMocks(this);
		DirectoryFileExtractor.INSTANCE.setRequest(request);
		DirectoryFileExtractor.INSTANCE.setResponse(response);
		DirectoryFileExtractor.INSTANCE.setChmExtractor(chmExtractor);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testExecute() {
		when(request.getRequestURI()).thenReturn("/directory/target");
		when(file.isDirectory()).thenReturn(true);
		DirectoryFileExtractor.INSTANCE.execute();
	}

	@Test
	public void testGetRequest() {
        assertEquals(request, DirectoryFileExtractor.INSTANCE.getRequest());
	}

	@Test
	public void testSetRequest() {
		DirectoryFileExtractor.INSTANCE.setRequest(request);
		testGetRequest();
	}

	@Test
	public void testGetResponse() {
		assertEquals(response, DirectoryFileExtractor.INSTANCE.getResponse());
	}

	@Test
	public void testSetResponse() {
		DirectoryFileExtractor.INSTANCE.setResponse(response);
		testGetResponse();
	}

	@Test
	public void testGetChmExtractor() {
		assertEquals(chmExtractor, DirectoryFileExtractor.INSTANCE.getChmExtractor());
	}

	@Test
	public void testSetChmExtractor() {
		DirectoryFileExtractor.INSTANCE.setChmExtractor(chmExtractor);
	}
}
