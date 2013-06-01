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

package org.ocrix.chm.server;

import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.ocrix.chm.extractor.ChmSevenZipExtractor;
import org.ocrix.chm.extractor.common.ExtractorType;
import org.ocrix.chm.extractor.common.MimeType;
import org.ocrix.chm.extractor.common.Utility;
import org.ocrix.chm.server.command.CMD;
import org.ocrix.chm.server.error.ErrorNefertiti;
import org.ocrix.chm.server.error.ErrorPinkFloyd;


/**
 * Handles incoming requests
 */
public class ChmHandler extends HttpServlet {

	private static final long serialVersionUID = 5275745480586091893L;
	private static final Logger LOG = Logger.getLogger(ChmHandler.class);
	private ReentrantLock lifecicle = new ReentrantLock();
	private final ChmSevenZipExtractor chmExtractor = new ChmSevenZipExtractor();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String[] parts = req.getRequestURI().split("/");

		if (parts.length == 0) {
			resp.setContentType(MimeType.HTML.to());
			Utility.writeMessage(resp, new ErrorNefertiti().toString());
			return;
		}

		/* If help is needed */
		getOptions(req.getRequestURI(), resp);

		/* Extracts file */
		extract(req, resp);
	}
	
	private void getOptions(String url, HttpServletResponse resp) {
		
		if(!url.endsWith("/help")){
			return;
		}
		
		StringBuilder sb = null;
		try {
			lifecicle.lock();
			sb = new StringBuilder();
			
			resp.setContentType(MimeType.HTML.to());
			// Create HTML ART Help
			sb.append("<dl>");
			sb.append("<dt><u>Avalable options are:</u></dt>");
			sb.append("<dt><b>Options for single file</b></dt>");
			sb.append("<dd><code>/extract/file/${file}</code></dd>");
			sb.append("<dd><code>/metadata/file/${file}</code></dd>");
			sb.append("<dt><b>Options for directory</b></dt>");
			sb.append("<dd><code>/extract/directory/${path}</code></dd>");
			sb.append("</dl>");
			sb.append("\nif directory is chosen, all content will be saved as jsons.\n");
			
			Utility.writeMessage(resp, sb.toString());
			
		} catch (Exception e) {
			LOG.error(e);
		} 
		
		lifecicle.unlock();
	}
	
	/**
	 * Does major job of the extractor.
	 * 
	 * @param req an {@link HttpServletRequest}.
	 * @param resp an {@link HttpServletResponse}.
	 */
	private void extract(HttpServletRequest req, HttpServletResponse resp) {
		
		lifecicle.lock();
		
		LOG.debug("REQ_URL=" + req.getRequestURI());

		// extracts single chm file
		if(req.getRequestURI().contains("/extract/file")) {
			CMD.bash.fire(ExtractorType.FILE, req, resp, chmExtractor);
		} 
		// extracts all files from specific folder
		else if(req.getRequestURI().contains("/extract/directory")){
			CMD.bash.fire(ExtractorType.DIRECTORY, req, resp, chmExtractor);
		} 
		//Metadata of single file
		else if(req.getRequestURI().contains("/metadata/file")){
			CMD.bash.fire(ExtractorType.METADATA, req, resp, chmExtractor);
		} 	
		//if resource is not correct
		else {
			resp.setContentType(MimeType.HTML.to());
			Utility.writeMessage(resp, new ErrorPinkFloyd().toString());
		}
		
		lifecicle.unlock();
	}
}
