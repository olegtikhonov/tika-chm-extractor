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

/**
 * Defines common mime type within project.
 */
public enum MimeType {
	
	HTML("text/html"),
	
	JSON("application/json");
	
	private String value;
	
    /**
     * Constructs {@link MimeType} privately.
     * 
     * @param var to be set for the enum.
     */
	private MimeType(String var) {
		this.value = var;
	}
	
	/**
	 * Gets a value of the enum.
	 * 
	 * @return a textual representation of the enum.
	 */
	public String to() {
		return this.value;
	}
}
