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

package org.ocrix.chm.extractor;

import net.sf.sevenzipjbinding.ISequentialOutStream;
import net.sf.sevenzipjbinding.SevenZipException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.sax.BodyContentHandler;
import org.ocrix.chm.extractor.common.Utility;
import org.xml.sax.SAXException;

import java.io.*;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Implementation of the {@link ISequentialOutStream}.
 * By default saves files in "extracted_files/{filename}/..."
 */
public class ChmSequentialOutStream implements ISequentialOutStream {
    private static final Logger LOG = LogManager.getLogger(ChmSequentialOutStream.class);
    private String CHM_EXTRACT_DIR = "extracted_files";
    private List<Metadata> metadataList;
    private List<byte[]> bb;
    private String path;
    private volatile boolean doSaveFiles = true;

    private ReentrantLock lifecicle = new ReentrantLock();
    final HtmlParser parser = new HtmlParser();
    //Should be set if a file is big enough
    final BodyContentHandler handler = new BodyContentHandler(10 * 1024 * 1024);
    final Metadata metadata = new Metadata();
    final ParseContext pasCon = new ParseContext();


    /**
     * Creates a {@link ChmSequentialOutStream}.
     *
     * @param extData
     * @param accMetadata
     */
    public ChmSequentialOutStream(List<byte[]> extData, List<Metadata> accMetadata) {
        this.bb = extData;
        this.metadataList = accMetadata;
    }

    public int write(byte[] data) throws SevenZipException {
        try {
            lifecicle.lock();
            parser.parse(new ByteArrayInputStream(data), handler, metadata, pasCon);
            // Meta-data accumulation
            metadataList.add(metadata);
            // Data accumulation
            bb.add(data);
            saveExtractedFile(path, data);

        } catch (IOException e) {
            LOG.error(e);
        } catch (SAXException e) {
            LOG.error(e);
        } catch (TikaException e) {
            LOG.error(e);
        } finally {
            lifecicle.unlock();
        }

        return data.length;
    }


    /**
     * @return
     */
    public String getPath() {
        return path;
    }


    /**
     * @param path
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Checks if save data files.
     *
     * @return
     */
    public boolean isDoSaveFiles() {
        return doSaveFiles;
    }

    /**
     * Sets a flag indicating either save data files or not.
     *
     * @param doSaveFiles
     */
    public void setDoSaveFiles(boolean doSaveFiles) {
        this.doSaveFiles = doSaveFiles;
    }


    public void setChmExtractDir(String folder) {
        CHM_EXTRACT_DIR = folder;
    }

    /**
     * Saves extracted file.
     *
     * @param path a file name
     * @param data an extracted context
     */
    private void saveFile(String path, byte[] data) {
        File file = new File(CHM_EXTRACT_DIR + File.separator + path);
        Utility.writeToFile(file, data);
    }

    /**
     * Saves an extracted context as a file.
     *
     * @param path
     * @param data
     */
    private void saveExtractedFile(String path, byte[] data) {
        if (doSaveFiles) {
            if (path.indexOf(File.separator) == -1) {
                saveFile(path, data);
            } else {
                saveFile(path.substring(path.lastIndexOf(File.separator) + 1), data);
            }
        }
    }
}
