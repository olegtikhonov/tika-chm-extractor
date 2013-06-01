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

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import net.sf.sevenzipjbinding.ISevenZipInArchive;
import net.sf.sevenzipjbinding.SevenZip;
import net.sf.sevenzipjbinding.SevenZipException;
import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream;
import net.sf.sevenzipjbinding.simple.ISimpleInArchive;
import net.sf.sevenzipjbinding.simple.ISimpleInArchiveItem;
import org.apache.log4j.Logger;
import org.apache.tika.Tika;
import org.apache.tika.metadata.Metadata;
import org.ocrix.chm.extractor.common.FileNode;


public class ChmSevenZipExtractor {
    private static final String CHM_MIME = "application/vnd.ms-htmlhelp";
    private static final String CHM_EXTRACT_DIR = "extracted_files";
	private List<byte[]> bb = new ArrayList<byte[]>();
	private List<Metadata> metadataList = new ArrayList<Metadata>();
	private static final Logger LOG = Logger.getLogger(ChmSevenZipExtractor.class);
	private final Tika tika = new Tika();
	private FileNode fn = new FileNode();
	private StringBuilder sb = new StringBuilder();
	private volatile boolean wasExtracted = false;
	
		
	public String metadata(String pathToFile) {
		return extractMetadata(pathToFile);
	}
	
	/**
	 * Extracts context & meta-data from the file.
	 * 
	 * @param pathToFile where file located.
	 */
	public String extractMetadata(String pathToFile) {
		return extract(pathToFile, false).getMetadata();
	}
	
	private String prepareFolder(String nameOfSubfolder){
		File extFile = new File(CHM_EXTRACT_DIR + File.separator + nameOfSubfolder.replace(".", ""));
		
		if(extFile.exists()){
			LOG.warn( "Folder for " + nameOfSubfolder + " exists, it means that context was extracted some time ago. Please delete folder in order to reextract context");
			wasExtracted = true;
			return null;
		} else {
			wasExtracted = false;
		}
		
		extFile.mkdirs();
		
		return extFile.getAbsolutePath();
	}
	
	public FileNode extract(String pathToFile, boolean doSaveFiles) {
		RandomAccessFile randomAccessFile = null;
		ISevenZipInArchive inArchive = null;
		
		String fileName = pathToFile.substring(pathToFile.lastIndexOf(File.separator) + 1, pathToFile.lastIndexOf("."));
		
		LOG.info("Going to extract=" + fileName);
		
		try {
			String mimeType = tika.detect(new File(pathToFile)); 
			// Wont work if mime-type is not CHM
			if(!mimeType.equals(CHM_MIME)){
				LOG.error("detected " + mimeType + " mime-type. The file " + pathToFile + " won't be processed");
				return null;
			}
			
			randomAccessFile = new RandomAccessFile(pathToFile, "r");
			inArchive = SevenZip.openInArchive(null, new RandomAccessFileInStream(randomAccessFile));
			// Getting simple interface of the archive inArchive
			ISimpleInArchive simpleInArchive = inArchive.getSimpleInterface();
			
			clearBuffers();
			String whereToExtract = prepareFolder(fileName);
			
			checkFolderAgain(whereToExtract);
			
			if(!wasExtracted){
				
				ChmSequentialOutStream stream = new ChmSequentialOutStream(bb, metadataList);
				stream.setChmExtractDir(whereToExtract);
				stream.setDoSaveFiles(doSaveFiles);
				
				for (ISimpleInArchiveItem item : simpleInArchive.getArchiveItems()) {
					if (!item.isFolder()) {
						final String path = item.getPath();
						stream.setPath(path);
						
						// Generally. html contains data, rest of the files intended to be used internally.
						if (path.contains(".htm") || path.contains(".html")) {
							item.extractSlow(stream);
						}
					}
				}
			} else {
				fn.setContext(fileName + " was extracted previously");
				
				return fn;
			}

		} catch (Exception e) {
			LOG.error(e);
		} finally {
			takeCareOf(inArchive, randomAccessFile);
		}

		prepareFileNode();

		return fn;
	}
	
	private void checkFolderAgain(String whereToExtract) {
		
		if(whereToExtract != null){
			File f = new File(whereToExtract);
			if(!f.exists()) {
				wasExtracted = false;
				fn.setContext(null);
				fn.setMetadata(null);
			}
		}else {
			clearBuffers();
			fn.setContext(null);
			fn.setMetadata(null);
			fn.setAdditionalInfo("Could not extract file, cause it was previously extracted. Delete folder & retry.");
		}
	}
	
	
	/**
	 * Clears all used buffers. All data will be lost.
	 */
	private void clearBuffers() {
		bb.clear();
		metadataList.clear();
		sb.delete(0, sb.length());
	}
	
	private void prepareFileNode() {
		// Context transformation
		for (byte[] arr : bb) {
			sb.append(new String(arr).replaceAll("\\<.*?.*\\>", ""));
		}

		fn.setContext(sb.toString());

		sb.delete(0, sb.length());

		// Meta-data transformation
		for (Metadata md : metadataList) {
			sb.append(md);
		}

		fn.setMetadata(sb.toString());
	}
	
	/**
	 * Closes the archive & rendom access file.
	 * 
	 * @param inArchive to be closed.
	 * @param randomAccessFile to be closed.
	 */
	private void takeCareOf(ISevenZipInArchive inArchive, RandomAccessFile randomAccessFile) {
		
		if (inArchive != null) {
			try {
				inArchive.close();
			} catch (SevenZipException e) {
				LOG.error(e);
			}
		}
		
		if (randomAccessFile != null) {
			try {
				randomAccessFile.close();
			} catch (IOException e) {
				LOG.error(e);
			}
		}
	}
	
	/**
	 * Gets a path to extracted files.
	 * 
	 * @return
	 */
	public String getPathToExtractedFiles() {
		return ChmSevenZipExtractor.CHM_EXTRACT_DIR;
	}
	
	
	/**
	 * @param args
	 */
//	public static void main(String[] args) {
//
//		ChmSevenZipExtractor chmExtractor = new ChmSevenZipExtractor();
//		
//		File folder = new File("../Desktop/chm");
//
//		long start = 0;
//		
//		if (folder.isDirectory()) {
//			File[] files = folder.listFiles();
//			start = System.currentTimeMillis();
//			for (File file : files) {
//				FileNode fn = chmExtractor.extract(file.getAbsolutePath(), true);
//				System.out.println(fn);
//			}
//			
//			long end = System.currentTimeMillis() - start;
//			System.out.println("took " + end + "msec");
//		}
//	}
}
