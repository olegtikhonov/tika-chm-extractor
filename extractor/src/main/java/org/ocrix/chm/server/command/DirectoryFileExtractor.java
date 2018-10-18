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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ocrix.chm.extractor.ChmSevenZipExtractor;
import org.ocrix.chm.extractor.common.FileNode;
import org.ocrix.chm.extractor.common.JsonSerializer;
import org.ocrix.chm.extractor.common.MimeType;
import org.ocrix.chm.extractor.common.Utility;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.net.URLDecoder;
import java.util.concurrent.locks.ReentrantLock;
import static org.ocrix.chm.extractor.common.CommonConstants.CHM;
import static org.ocrix.chm.extractor.common.CommonConstants.DEFAULT_CHARSET;


/**
 * Intended to extract context and meta-data from directory chm files.
 */
public enum DirectoryFileExtractor implements Command{
    INSTANCE;
    private static final Logger LOG = LogManager.getLogger(DirectoryFileExtractor.class);
    
	private volatile HttpServletRequest request;
	private volatile HttpServletResponse response;
	private ChmSevenZipExtractor chmExtractor;
	private JsonSerializer serializer = new JsonSerializer();
	
	private ReentrantLock lifecicle = new ReentrantLock();
    
	
	/**
	 * Constructs {@link DirectoryFileExtractor} privately.
	 */
    private DirectoryFileExtractor() {}
	
	
	
	public void execute() {
		lifecicle.lock();
		
		response.setContentType(MimeType.JSON.to());
		response.setCharacterEncoding(DEFAULT_CHARSET.to());
		
		try {
			int index = URLDecoder.decode(request.getRequestURI(), DEFAULT_CHARSET.to()).indexOf("directory/");
			
			String pathToTheFile = URLDecoder.decode(request.getRequestURI(), DEFAULT_CHARSET.to()).substring(index + "directory/".length());
			
			File folder = new File(pathToTheFile);
			FileNode fn = new FileNode();
			
			if (folder.isDirectory()) {
				File[] files = folder.listFiles();
				for (File file : files) {
					FileNode fNode = chmExtractor.extract(file.getAbsolutePath(), true);
					Utility.saveFile(chmExtractor.getPathToExtractedFiles(), file.getName().replace(CHM.to(), ""), serializer.serialize(fNode).getBytes());
				}
			}

			fn.setAdditionalInfo("All extracted data saved here " + 
			                     new File(chmExtractor.getPathToExtractedFiles()).getAbsolutePath() + 
			                     ". Waiting for new batch of tasks.");

			
			Utility.writeMessage(response, serializer.serialize(fn));
		
		} catch (Exception e) {
			LOG.error(e);
		}
		
		lifecicle.unlock();
	}

    /**
     * Gets a request.
     * 
     * @return the request.
     */
	public HttpServletRequest getRequest() {
		return request;
	}

    /**
     * Sets request.
     * 
     * @param request to be set.
     */
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}


    /**
     * Gets response.
     * 
     * @return the response.
     */
	public HttpServletResponse getResponse() {
		return response;
	}

    /**
     * Sets a response.
     * 
     * @param response to be set.
     */
	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	/**
	 * Gets chm extractor.
	 * 
	 * @return the extractor.
	 */
	public ChmSevenZipExtractor getChmExtractor() {
		return chmExtractor;
	}

    /**
     * Sets a ChmExtractor.
     * 
     * @param chmExtractor to be set.
     */
	public void setChmExtractor(ChmSevenZipExtractor chmExtractor) {
		this.chmExtractor = chmExtractor;
	}
}
