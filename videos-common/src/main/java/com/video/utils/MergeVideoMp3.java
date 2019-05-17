package com.video.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
public class MergeVideoMp3 {

	private String ffmpegEXE;
	
	public MergeVideoMp3(String ffmpegEXE) {
		super();
		this.ffmpegEXE = ffmpegEXE;
	}
	
	public void convertor(String videoInputPath, String mp3InputPath,
			double seconds, String videoOutputPath) throws Exception {
//		ffmpeg.exe -i 苏州大裤衩.mp4 -i bgm.mp3 -t 7 -y 新的视频.mp4
		List<String> command = new ArrayList<>();
//		command.add(ffmpegEXE);
//
//		command.add("-i");
//		command.add(videoInputPath);
//
//		command.add("-vcodec copy -an");
//		command.add(videoInputPath+"&&");
		command.add(ffmpegEXE);
		command.add("-i");
		command.add(videoInputPath);
		
		command.add("-i");
		command.add(mp3InputPath);
		
		command.add("-t");
		command.add(String.valueOf(seconds));
		
		command.add("-vcodec copy -acodec copy");
		command.add(videoOutputPath);
		StringBuffer sb = new StringBuffer();
		for (String c : command) {
			sb.append(c+" ");
		}

		Runtime r=Runtime.getRuntime();
		Process p=r.exec(sb.toString());
		InputStream is=p.getInputStream();
		InputStreamReader ir=new InputStreamReader(is);
		BufferedReader br=new BufferedReader(ir);
		String str=null;
		while((str=br.readLine())!=null){
			System.out.println(str);
		}
		//获取进程的返回值,为0正常,为2出现问题
		int ret=p.waitFor();
		int exit_v=p.exitValue();
		System.out.println("return value:"+ret);
		System.out.println("exit value:"+exit_v);
		
	}

	public static void main(String[] args) {
		MergeVideoMp3 ffmpeg = new MergeVideoMp3("ffmpeg");
		try {
			ffmpeg.convertor("/Users/zhouchao/Desktop/biye/dev/180425CFA4RB6T0H/video/12.mp4", "/Users/zhouchao/Desktop/biye/dev/showbgm/123.mp3", 3, "/Users/zhouchao/Desktop/biye/dev/showbgm/1231.mp4");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
//	ffmpeg -i /Users/zhouchao/Desktop/biye/dev/180425CFA4RB6T0H/video/333.mp4 -vcodec copy -an /Users/zhouchao/Desktop/biye/dev/180425CFA4RB6T0H/video/334.mp4
	//ffmpeg -i /Users/zhouchao/Desktop/biye/dev/180425CFA4RB6T0H/video/333.mp4 -vcodec copy -an /Users/zhouchao/Desktop/biye/dev/180425CFA4RB6T0H/video/333.mp4
//	}ffmpeg -i /Users/zhouchao/Desktop/biye/dev/180425CFA4RB6T0H/video/334.mp4 -i /Users/zhouchao/Desktop/biye/dev/showbgm/123.mp3 -t 3 -vcodec copy -acodec copy /Users/zhouchao/Desktop/biye/dev/335.mp4
}
