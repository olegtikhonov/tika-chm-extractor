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


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.ocrix.chm.extractor.ChmSevenZipExtractor;
import org.ocrix.chm.extractor.common.ExtractorType;


public enum CMD {
	
	bash;
	
	private CMD() {}
	
	
	public void fire(ExtractorType commandType, HttpServletRequest req, HttpServletResponse resp, ChmSevenZipExtractor chmExtractor) {
		
		switch (commandType) {
		
		case FILE:
			FileExtractor.INSTANCE.setChmExtractor(chmExtractor);
			FileExtractor.INSTANCE.setReq(req);
			FileExtractor.INSTANCE.setResp(resp);
			FileExtractor.INSTANCE.execute();
			
			break;

		case DIRECTORY:
			DirectoryFileExtractor.INSTANCE.setChmExtractor(chmExtractor);
			DirectoryFileExtractor.INSTANCE.setRequest(req);
			DirectoryFileExtractor.INSTANCE.setResponse(resp);
			DirectoryFileExtractor.INSTANCE.execute();

			break;
			
		case METADATA:
			MetadataFileExtractor.INSTANCE.setRequest(req);
			MetadataFileExtractor.INSTANCE.setResponse(resp);
			MetadataFileExtractor.INSTANCE.setChmExtractor(chmExtractor);
			MetadataFileExtractor.INSTANCE.execute();
			
			break;
			
		default:
			break;
		}
	}
}
