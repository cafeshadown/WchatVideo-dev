package com.video.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class outMusic {

    private String ffmpegEXE;

    public outMusic(String ffmpegEXE) {
        super();
        this.ffmpegEXE = ffmpegEXE;
    }

    public void convertor(String videoInputPath,String videoInputPat) throws Exception {
//		ffmpeg.exe -i 苏州大裤衩.mp4 -i bgm.mp3 -t 7 -y 新的视频.mp4
        List<String> command = new ArrayList<>();
        command.add(ffmpegEXE);

        command.add("-i");
        command.add(videoInputPath);

        command.add("-vcodec copy -an");
        command.add(videoInputPat);
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

//	public static void main(String[] args) {
//		MergeVideoMp3 ffmpeg = new MergeVideoMp3("ffmpeg");
//		try {
//			ffmpeg.convertor("wxeb50e10d9368f3d6.o6zAJs_dgjY0xMkEFVLSoTb7u4r0.1eZN5VgpWkex2dd12c7116948c27a5b93305b5d2cc26.mp4", "C:\\music.mp3", 7.1, "wxeb50e10d9368f3d6.o6zAJs_dgjY0xMkEFVLSoTb7u4r0.1eZN5VgpWkex2dd12c7116948c27a5b93305b5d2cc26.mp4");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	ffmpeg -i /Users/zhouchao/Desktop/biye/dev/180425CFA4RB6T0H/video/333.mp4 -vcodec copy -an /Users/zhouchao/Desktop/biye/dev/180425CFA4RB6T0H/video/334.mp4
    //ffmpeg -i /Users/zhouchao/Desktop/biye/dev/180425CFA4RB6T0H/video/333.mp4 -vcodec copy -an /Users/zhouchao/Desktop/biye/dev/180425CFA4RB6T0H/video/333.mp4
//	}ffmpeg -i /Users/zhouchao/Desktop/biye/dev/180425CFA4RB6T0H/video/334.mp4 -i /Users/zhouchao/Desktop/biye/dev/showbgm/123.mp3 -t 3 -vcodec copy -acodec copy /Users/zhouchao/Desktop/biye/dev/335.mp4

}
