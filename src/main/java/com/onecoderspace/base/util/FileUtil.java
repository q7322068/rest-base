package com.onecoderspace.base.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

public class FileUtil {
	private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

	/**
	 * 读取文件内的所有行
	 * @author yangwk
	 * @time 2017年9月12日 上午9:46:33
	 * @param filePath 文件完整路径
	 * @return
	 */
	public static List<String> getLines(String filePath){
		FileInputStream inputStream = null;
		List<String> lines = Lists.newArrayList();
		try {
			inputStream = new FileInputStream(new File(filePath));
			lines = IOUtils.readLines(inputStream);
		} catch (Exception e) {
			logger.error("read file due to error",e);
		}finally {
			try {
				if(inputStream != null){
					inputStream.close();
				}
			} catch (IOException e) {
				logger.error("close inputStream due to error",e);
			}
		}
		return lines;
	}

	/**
	 * 写入文件
	 * @author yangwk
	 * @time 2017年9月14日 下午1:44:23
	 * @param file
	 * @param lines
	 */
	public static void writeLines(File file, List<String> lines) {
		try {
			org.apache.commons.io.FileUtils.writeLines(file, lines, "\n");
		} catch (IOException e) {
			logger.error("write lines due to error",e);
		}
		
	}
	
}
