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


import static org.ocrix.chm.extractor.common.CommonConstants.DEFAULT_CHARSET;
import java.net.URLDecoder;
import java.util.concurrent.locks.ReentrantLock;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.ocrix.chm.extractor.ChmSevenZipExtractor;
import org.ocrix.chm.extractor.common.FileNode;
import org.ocrix.chm.extractor.common.JsonSerializer;
import org.ocrix.chm.extractor.common.MimeType;
import org.ocrix.chm.extractor.common.Utility;

/**
 * 
 * Deals with single chm file extraction.
 *
 */
public enum FileExtractor implements Command {
	INSTANCE;
	
	private static final Logger LOG = Logger.getLogger(FileExtractor.class);
	
	private HttpServletRequest req; 
	private HttpServletResponse resp;
	private JsonSerializer serializer = new JsonSerializer();
	private ChmSevenZipExtractor chmExtractor;
	private ReentrantLock lifecicle = new ReentrantLock();
	
	
	/**
	 * Constructs a chm file extractor. 
	 * Deals with single file extracting.
	 */
	private FileExtractor() {
	}

	
	/**
	 * Extracts context and meta-data from chm file.
	 */
	public void execute() {
		lifecicle.lock();

		resp.setContentType(MimeType.JSON.to());
		resp.setCharacterEncoding(DEFAULT_CHARSET.to());

		try {
			resp.getOutputStream().write("going to extract chm file, Please wait for a while. ".getBytes());
			
			int index = URLDecoder.decode(req.getRequestURI(), DEFAULT_CHARSET.to()).indexOf("file/");

			String pathToTheFile = URLDecoder.decode(req.getRequestURI(), DEFAULT_CHARSET.to()).substring(index + "file/".length());
			FileNode data = extractFile(pathToTheFile);
			String json = serializer.serialize(data);
			
			resp.setContentType(MimeType.JSON.to());
			Utility.writeMessage(resp, json);

		} catch (Exception e) {
			LOG.error(e);
		}

		lifecicle.unlock();
	}

	/**
	 * Gets an {@link HttpServletRequest}.
	 * 
	 * @return the request.
	 */
	public HttpServletRequest getReq() {
		return req;
	}

    /**
     * Sets {@link HttpServletRequest}.
     * 
     * @param req to be set.
     */
	public void setReq(HttpServletRequest req) {
		this.req = req;
	}

    /**
     * Gets an {@link HttpServletResponse}.
     * 
     * @return the response.
     */
	public HttpServletResponse getResp() {
		return resp;
	}

    /**
     * Sets an {@link HttpServletResponse}.
     * 
     * @param resp to be set.
     */
	public void setResp(HttpServletResponse resp) {
		this.resp = resp;
	}

    /**
     * Gets a json serializer.
     * 
     * @return the seralizer.
     */
	public JsonSerializer getSerializer() {
		return serializer;
	}

    /**
     * Sets a {@link JsonSerializer}.
     * 
     * @param serializer to be set.
     */
	public void setSerializer(JsonSerializer serializer) {
		this.serializer = serializer;
	}

    /**
     * Gets a {@link ChmSevenZipExtractor}.
     * 
     * @return the chm extractor.
     */
	public ChmSevenZipExtractor getChmExtractor() {
		return chmExtractor;
	}

    /**
     * Sets a {@link ChmSevenZipExtractor}.
     * 
     * @param chmExtractor to be set.
     */
	public void setChmExtractor(ChmSevenZipExtractor chmExtractor) {
		this.chmExtractor = chmExtractor;
	}

    /**
     * Extracts a chm file.
     * 
     * @param pathToFile to be atken from.
     * 
     * @return a {@link FileNode}.
     */
	private FileNode extractFile(String pathToFile){
		FileNode fn = null;
		if(pathToFile != null){
			fn = chmExtractor.extract(pathToFile, true);
		}

		return fn;
	}
}
