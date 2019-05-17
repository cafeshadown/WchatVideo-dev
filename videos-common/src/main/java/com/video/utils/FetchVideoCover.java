package com.video.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * 
 * @Description: 获取视频的信息
 */
public class FetchVideoCover {
	// 视频路径
	private String ffmpegEXE;

	public void getCover(String videoInputPath, String coverOutputPath) throws IOException, InterruptedException {
//		ffmpeg.exe -ss 00:00:01 -i spring.mp4 -vframes 1 bb.jpg
		List<String> command = new java.util.ArrayList<>();
		command.add(ffmpegEXE);
		
		// 指定截取第1秒
		command.add("-ss");
		command.add("00:00:01");
				
		command.add("-y");
		command.add("-i");
		command.add(videoInputPath);
		
		command.add("-vframes");
		command.add("1");
		
		command.add(coverOutputPath);
		for (String c : command) {
			System.out.print(c + " ");
		}

		ProcessBuilder builder = new ProcessBuilder(command);
		Process process = builder.start();

		InputStream errorStream = process.getErrorStream();
		InputStreamReader inputStreamReader = new InputStreamReader(errorStream);
		BufferedReader br = new BufferedReader(inputStreamReader);
        process.waitFor();
        if (process.exitValue() != 0) {
            System.out.println("error!");
        }
		String line = "";
		while ( (line = br.readLine()) != null ) {
		}

		if (br != null) {
			br.close();
		}
		if (inputStreamReader != null) {
			inputStreamReader.close();
		}
		if (errorStream != null) {
			errorStream.close();
		}
	}

	public FetchVideoCover(String ffmpegEXE) {
		this.ffmpegEXE = ffmpegEXE;
	}
	
//	public static void main(String[] args) {
//		// 获取视频信息。
//		FetchVideoCover videoInfo = new FetchVideoCover("ffmpeg");
//		try {
//			videoInfo.getCover("wxeb50e10d9368f3d6.o6zAJs_dgjY0xMkEFVLSoTb7u4r0.1eZN5VgpWkex2dd12c7116948c27a5b93305b5d2cc26.mp4","/dev/video/北京.jpg");
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}ffmpeg -i /Users/zhouchao/Desktop/biye/dev/180425CFA4RB6T0H/video/wxeb50e10d9368f3d6.o6zAJs_dgjY0xMkEFVLSoTb7u4r0.uV4Bbitsh8V02dd12c7116948c27a5b93305b5d2cc26.mp4 -vcodec copy -an /Users/zhouchao/Desktop/biye/dev/180425CFA4RB6T0H/video/f2fc4a64-f20a-42ac-9046-3d2691aa561a.mp4; ffmpeg -i /Users/zhouchao/Desktop/biye/dev/180425CFA4RB6T0H/video/f2fc4a64-f20a-42ac-9046-3d2691aa561a.mp4 -i /Users/zhouchao/Desktop/biye/dev/showbgm/SeeItAgain.mp3 -t 3.9447 -vcodec copy -acodec copy /Users/zhouchao/Desktop/biye/dev/180425CFA4RB6T0H/video/dc0f6d7a-3ee6-445f-991d-5e0a71c72785.mp4
}