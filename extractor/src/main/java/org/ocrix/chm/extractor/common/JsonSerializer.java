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

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.MappingJsonFactory;
import org.codehaus.jackson.map.ObjectMapper;
import static org.codehaus.jackson.map.SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS;
import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;

/**
 *  JSON serializer.
 */
public class JsonSerializer {

    ObjectMapper mapperIn = new ObjectMapper();
    ObjectMapper mapperOut = new ObjectMapper();
    MappingJsonFactory jsonFactory = new MappingJsonFactory();

    /**
     * Constructor
     */
	public JsonSerializer() {
		// This outputs a ISO 8601 format like this 2013-05-09T21:11:11.928+0100 .
		mapperOut.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
		// This makes a more robust deserialization of variations of ISO 8601 formats.
		mapperIn.configure(WRITE_DATES_AS_TIMESTAMPS, false);
	}

	/**
	 * Deserializes to Message list
	 * 
	 * @param json
	 *            JSON string
	 * @return
	 * @throws IOException
	 */
    public String deserialize(String json) throws IOException {
        return mapperIn.readValue(json, String.class);
    }

    /**
     * Serializes BBL Message list.
     * 
     * @param message BBL Message
     * @return JSON string
     * @throws IOException
     */
    public String serialize(String message) throws IOException {
        StringWriter sw = new StringWriter();
        JsonGenerator jsonGenerator = jsonFactory.createJsonGenerator(sw);
        mapperOut.writeValue(jsonGenerator, message);
        sw.close();
        return sw.getBuffer().toString();
    }

    /**
     * Marshals {@link FileNode} massage.
     * 
     * @param message to be encoded.
     * 
     * @return marshaled message.
     * 
     * @throws IOException
     */
    public String serialize(FileNode message) throws IOException {
        StringWriter sw = new StringWriter();
        JsonGenerator jsonGenerator = jsonFactory.createJsonGenerator(sw);
        mapperOut.writeValue(jsonGenerator, message);
        sw.close();
        return sw.getBuffer().toString();
    }

}
