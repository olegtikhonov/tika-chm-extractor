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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static org.mockito.Mockito.when;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.ocrix.chm.extractor.ChmSevenZipExtractor;
import org.ocrix.chm.extractor.common.ExtractorType;
import static org.mockito.MockitoAnnotations.initMocks;

public class TestCMD {

	@Mock HttpServletRequest request;
	@Mock HttpServletResponse response;
	@Mock ChmSevenZipExtractor chmExtractor;
	
	@Before
	public void setUp() throws Exception {
		initMocks(this);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testFire() {
		when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080//extract/directory/C:%5CUsers%5Cotikhono%5CDesktop%5Cchm"));
		CMD.bash.fire(ExtractorType.DIRECTORY, request, response, chmExtractor);
		assertTrue(true);
		
		CMD.bash.fire(ExtractorType.FILE, request, response, chmExtractor);
		assertTrue(true);
		
		CMD.bash.fire(ExtractorType.METADATA, request, response, chmExtractor);
		assertTrue(true);
	}

}
