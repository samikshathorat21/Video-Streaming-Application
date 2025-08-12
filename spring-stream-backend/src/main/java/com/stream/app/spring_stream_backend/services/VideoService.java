package com.stream.app.spring_stream_backend.services;

import com.stream.app.spring_stream_backend.entities.Video;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VideoService {

    Video save(Video video, MultipartFile file);

    Video get(String videoId);

    Video getByTitle(String title);

    List<Video> getAll();

    //video processing ke liye
    String processVideo(String videoId);


}
