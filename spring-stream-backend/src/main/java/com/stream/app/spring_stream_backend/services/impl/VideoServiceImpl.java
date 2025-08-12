package com.stream.app.spring_stream_backend.services.impl;

import com.stream.app.spring_stream_backend.entities.Video;
import com.stream.app.spring_stream_backend.repositories.VideoRepository;
import com.stream.app.spring_stream_backend.services.VideoService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;


@Service
public class VideoServiceImpl implements VideoService {

    @Value("${files.video}")
    String DIR;


    @Value("${file.video.hsl}")
    String HSL_DIR;


    private VideoRepository videoRepository;

    public VideoServiceImpl(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    @PostConstruct
    public void init() {

        File file = new File(DIR);


        try {
            Files.createDirectories(Paths.get(HSL_DIR));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (!file.exists()) {
            file.mkdir();
            System.out.println("Folder created");
        } else {
            System.out.println("Folder already created");
        }
    }

    @Override
    public Video save(Video video, MultipartFile file) {

        //original file name
        try {

            String fileName = file.getOriginalFilename();
            String contentType = file.getContentType();
            InputStream inputStream = file.getInputStream();

            //folder path : create and file path :create
            String cleanFileName = StringUtils.cleanPath(fileName);
            String cleanFolder = StringUtils.cleanPath(DIR);

            //folder path with file name
            Path path = Paths.get(cleanFolder, cleanFileName);

            System.out.println(contentType);
            System.out.println(path);


            //copy file to the folder
            Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);

            //video meta data banana hai
            video.setContentType(contentType);
            video.setFilePath(path.toString());


            Video savedVideo = videoRepository.save(video);

            //procssing of video
            processVideo(savedVideo.getVideoId());

            //delete actual video file and database if exception

            //metadata save
            return savedVideo;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Video get(String videoId) {

        Video video = videoRepository.findById(videoId).orElseThrow(() -> new RuntimeException("Video not found"));
        return video;
    }

    @Override
    public Video getByTitle(String title) {
        return null;
    }

    @Override
    public List<Video> getAll() {
        return videoRepository.findAll();
    }

    @Override
    public String processVideo(String videoId) {

        Video video = this.get(videoId);
        String filePath = video.getFilePath();

        //path jaha data store  hoga
        Path videoPath = Paths.get(filePath);

        /*String outpur360p = HSL_DIR + videoId + "/360p";
        String outpur720p = HSL_DIR + videoId + "/720p";
        String outpur1080p = HSL_DIR + videoId + "/1080p";*/

        try {
//            Files.createDirectories(Paths.get(outpur360p));
//            Files.createDirectories(Paths.get(outpur720p));
//            Files.createDirectories(Paths.get(outpur1080p));

            //ffmpeg ccommand
            Path outputPath = Paths.get(HSL_DIR, videoId);

            Files.createDirectories(outputPath);

            String ffmpegCmd = String.format(
                    "ffmpeg -i \"%s\" -c:v libx264 -c:a aac -strict -2 -f hls -hls_time 10 -hls_list_size 0 -hls_segment_filename \"%s/segment_%%3d.ts\" \"%s/master.m3u8\" ",
                    videoPath, outputPath, outputPath
            );

            System.out.println(ffmpegCmd);

            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", ffmpegCmd);
            processBuilder.inheritIO();
            Process process = processBuilder.start();
            int exit = process.waitFor();

            if (exit != 0) {
                throw new RuntimeException("video processing failed!!");
            }

            return videoId;


//            StringBuilder ffmpegCmd = new StringBuilder();
//            ffmpegCmd.append("ffmpeg -i")
//                    .append(videoPath.toString())
//                    .append("")

        } catch (IOException ex) {
            throw new RuntimeException("Video processing failed");
        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }


    }
}
