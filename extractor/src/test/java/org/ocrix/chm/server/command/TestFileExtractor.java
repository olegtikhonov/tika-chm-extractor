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
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.ocrix.chm.extractor.ChmSevenZipExtractor;
import org.ocrix.chm.extractor.common.JsonSerializer;


/**
 * Tests {@link FileExtractor} functionality.
 */
public class TestFileExtractor {
	@Mock HttpServletRequest request = mock(HttpServletRequest.class);
	@Mock HttpServletResponse response = mock(HttpServletResponse.class);
	@Mock ChmSevenZipExtractor chmExtractor = mock(ChmSevenZipExtractor.class);
	@Mock File file = mock(File.class);
	@Mock ServletOutputStream sos = mock(ServletOutputStream.class);

	@Before
	public void setUp() throws Exception {
		initMocks(this);
		FileExtractor.INSTANCE.setChmExtractor(chmExtractor);
		FileExtractor.INSTANCE.setReq(request);
		FileExtractor.INSTANCE.setResp(response);
	}

	@Test
	public void testExecute() throws IOException {
		when(response.getOutputStream()).thenReturn(sos);
		when(request.getRequestURI()).thenReturn("/file/target.chm");
		when(file.isDirectory()).thenReturn(true);
		FileExtractor.INSTANCE.execute();
		
		assertTrue(true);
	}

	@Test
	public void testGetReq() {
		assertEquals(request, FileExtractor.INSTANCE.getReq());
	}

	@Test
	public void testSetReq() {
		FileExtractor.INSTANCE.setReq(request);
		assertEquals(request, FileExtractor.INSTANCE.getReq());
	}

	@Test
	public void testGetResp() {
		assertEquals(response, FileExtractor.INSTANCE.getResp());
	}

	@Test
	public void testSetResp() {
		FileExtractor.INSTANCE.setResp(response);
		assertEquals(response, FileExtractor.INSTANCE.getResp());
	}

	@Test
	public void testGetSerializer() {
		JsonSerializer mySer = new JsonSerializer();
		FileExtractor.INSTANCE.setSerializer(mySer);
		assertEquals(mySer, FileExtractor.INSTANCE.getSerializer());
	}

	@Test
	public void testSetSerializer() {
		testGetSerializer();
	}

	@Test
	public void testGetChmExtractor() {
		ChmSevenZipExtractor extrtr = new ChmSevenZipExtractor();
		FileExtractor.INSTANCE.setChmExtractor(extrtr);
		assertEquals(extrtr, FileExtractor.INSTANCE.getChmExtractor());
	}

	@Test
	public void testSetChmExtractor() {
		testGetChmExtractor();
	}
}
