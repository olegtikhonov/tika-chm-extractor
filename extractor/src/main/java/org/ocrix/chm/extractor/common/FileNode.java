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
 * Holds context & metadata of the chm file.
 */
public class FileNode {
	private String context;
	private String metadata;
	private String additionalInfo;

	/**
	 * Gets a context of the file.
	 * 
	 * @return
	 */
	public String getContext() {
		return context;
	}
	
	/**
	 * Sets a context of the file.
	 * 
	 * @param context
	 */
	public void setContext(String context) {
		this.context = context;
	}
	
	/**
	 * Gets metadata of chm file.
	 * 
	 * @return
	 */
	public String getMetadata() {
		return metadata;
	}
	
	/**
	 * Sets metadata of the chm file.
	 * 
	 * @param metadata
	 */
	public void setMetadata(String metadata) {
		this.metadata = metadata;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getName());
		builder.append(" [context=");
		
		if(context != null) {
			builder.append(context.substring(0, 10).replaceAll("\n", "").replaceAll("\r", "").replaceAll(" ", ""));
		}
		
		builder.append(", ");
		
		if(metadata != null) {
			builder.append(metadata.substring(0, 10));
		}
		
		builder.append("]");
		return builder.toString();
	}

	/**
	 * Optional field.
	 * 
	 * @return
	 */
	public String getAdditionalInfo() {
		return additionalInfo;
	}

	/**
	 * Optional filed.
	 * 
	 * @param additionalInfo
	 */
	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}
}
