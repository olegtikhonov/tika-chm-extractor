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

package org.ocrix.chm.extractor.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import static org.ocrix.chm.extractor.common.CommonConstants.JSON;

public class Utility {
	
	private Utility() {}
	
	private static final Logger LOG = Logger.getLogger(Utility.class);
	
	public static void saveFile(String pathToSave, String fileName, byte[] json) {
		File file = new File(pathToSave + File.separator + fileName + JSON.to());
		FileOutputStream bw;
		try {
			bw = new FileOutputStream(file);
			bw.write(json);
			bw.close();
		} catch (FileNotFoundException e) {
			LOG.error(e);
		} catch (IOException e) {
			LOG.error(e);
		}
	}
	
	public static void saveMetadata(String pathToSave, String fileName, String metadata) {
		File file = new File(pathToSave + File.separator + fileName + JSON.to());
		FileOutputStream bw;
		try {
			bw = new FileOutputStream(file);
			bw.write(metadata.getBytes());
			bw.close();
		} catch (FileNotFoundException e) {
			LOG.error(e);
		} catch (IOException e) {
			LOG.error(e);
		}
	}
	
	public static void writeMessage(HttpServletResponse resp, String msg) {
		try {
			OutputStream out = resp.getOutputStream();
			out.write(msg.getBytes());
			out.close();
		} catch (Exception e) {
			LOG.error(e);
		}
	}
}
