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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.ocrix.chm.extractor.ChmSevenZipExtractor;
import org.ocrix.chm.extractor.common.ExtractorType;
import org.ocrix.chm.server.mocks.MockHttpRequest;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;


public class TestCMD {
	@Mock MockHttpRequest request;
	@Mock HttpServletResponse response;
	@Mock ServletOutputStream servletOutputStream;
	private ChmSevenZipExtractor chmExtractor;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		chmExtractor = new ChmSevenZipExtractor();
	}

	@After
	public void tearDown() throws Exception {
        deleteExtractedFilesDirectory();
	}

	@Test
	public void testFire() throws IOException {
		when(request.getRequestURI()).thenReturn("http://localhost:8080/extract/directory/src/test/resources");

		CMD.bash.fire(ExtractorType.DIRECTORY, request, response, chmExtractor);
		assertTrue(true);

		when(request.getRequestURI()).thenReturn("http://localhost:8080/extract/file/src/test/resources/Open_Mobile_Client_Help.chm");
		when(response.getOutputStream()).thenReturn(servletOutputStream);

        deleteExtractedFilesDirectory();
		CMD.bash.fire(ExtractorType.FILE, request, response, chmExtractor);
		assertTrue(true);

        deleteExtractedFilesDirectory();
		CMD.bash.fire(ExtractorType.METADATA, request, response, chmExtractor);
		assertTrue(true);
	}


    private static void deleteExtractedFilesDirectory() {
        try {
            List<String> command = new ArrayList<String>();
            command.add("rm");
            command.add("-rf");
            command.add("extracted_files");

            ProcessBuilder builder = new ProcessBuilder(command);
            Map<String, String> environ = builder.environment();

            final Process process = builder.start();
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            process.waitFor();
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            System.out.println("Program terminated!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
