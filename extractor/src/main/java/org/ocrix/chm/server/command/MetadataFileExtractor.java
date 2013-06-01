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

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.concurrent.locks.ReentrantLock;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.ocrix.chm.extractor.ChmSevenZipExtractor;
import org.ocrix.chm.extractor.common.CommonConstants;
import org.ocrix.chm.extractor.common.FileNode;
import org.ocrix.chm.extractor.common.JsonSerializer;
import org.ocrix.chm.extractor.common.MimeType;
import org.ocrix.chm.extractor.common.Utility;


/**
 * Extracts metadata from the chm file.
 */
public enum MetadataFileExtractor implements Command {
	INSTANCE;
	
	private volatile HttpServletRequest request;
	private volatile HttpServletResponse response;
	private ChmSevenZipExtractor chmExtractor;
	private JsonSerializer serializer = new JsonSerializer();
	private static final Logger LOG = Logger.getLogger(MetadataFileExtractor.class);
	private ReentrantLock lifecicle = new ReentrantLock();

	/**
	 * Constructs {@link MetadataFileExtractor} privately. 
	 */
	private MetadataFileExtractor(){}
	
	/**
	 * Extracts metadata from chm file.
	 */
	public void execute() {
		lifecicle.lock();
		
		
		response.setContentType(MimeType.JSON.to());
		response.setCharacterEncoding(CommonConstants.DEFAULT_CHARSET.to());
		
		try {
			int index = URLDecoder.decode(request.getRequestURI(), CommonConstants.DEFAULT_CHARSET.to()).indexOf("file/");
			
			String pathToTheFile = URLDecoder.decode(getRequest().getRequestURI(), CommonConstants.DEFAULT_CHARSET.to()).substring(index + "file/".length());
			
			String data = extractMetadata(pathToTheFile);
			
			sendResponse(data, pathToTheFile);

		} catch (Exception e) {
			LOG.error(e);
		}
		
		lifecicle.unlock();
	}

	/**
	 * Gets {@link HttpServletRequest}.
	 * 
	 * @return a request.
	 */
	public HttpServletRequest getRequest() {
		return request;
	}


	/**
	 * Sets an {@link HttpServletRequest}.
	 * 
	 * @param request to be set.
	 */
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	/**
	 * Gets an {@link HttpServletResponse}.
	 * 
	 * @return a response.
	 */
	public HttpServletResponse getResponse() {
		return response;
	}

	/**
	 * Sets an {@link HttpServletResponse}.
	 * 
	 * @param response to be set.
	 */
	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
	
	/**
	 * Gets a {@link ChmSevenZipExtractor}.
	 * 
	 * @return the extractor.
	 */
	public ChmSevenZipExtractor getChmExtractor() {
		return chmExtractor;
	}

	/**
	 * Sets {@link ChmSevenZipExtractor}.
	 * 
	 * @param chmExtractor to be set.
	 */
	public void setChmExtractor(ChmSevenZipExtractor chmExtractor) {
		this.chmExtractor = chmExtractor;
	}

	/**
	 * Extracts metadata.
	 * 
	 * @param pathToFile where file located.
	 * 
	 * @return extracted metadata for each html page inside chm.
	 */
	private String extractMetadata(String pathToFile){
		String fn = "N/A";
		
		if(pathToFile != null){
			fn = chmExtractor.extractMetadata(pathToFile);
		}

		return fn;
	}
	
	/**
	 * Checks data consistency.
	 * 
	 * @param data to be checked.
	 * @param filePath where to save data.
	 */
	private void sendResponse(String data, String filePath) {
		try {
			
			String abPath = new File(getChmExtractor().getPathToExtractedFiles()).getAbsolutePath();
			
			String fileName = "";

			
			if(filePath.contains(File.separator)){
				fileName = filePath.substring(filePath.lastIndexOf(File.separator)).replace(CommonConstants.CHM.to(), "");
			} else {
				fileName = filePath.replace(CommonConstants.CHM.to(), "");
			}
			
			
			if(data == null) {
				data = "Could not extract metadata because previously it was extracted. Delete " + abPath + fileName;
				FileNode fn = new FileNode();
				fn.setAdditionalInfo(data);
				Utility.writeMessage(getResponse(), serializer.serialize(fn));
			} else {
				String json = serializer.serialize(data);
				Utility.writeMessage(getResponse(), json);
				Utility.saveMetadata(abPath + fileName, fileName, data);
				
			}
		} catch (IOException e) {
			LOG.error(e);
		}
	}
}
