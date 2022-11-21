package com.jonghak.springbootweb;

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;

@Controller
public class FileController {

    @Autowired
    ResourceLoader resourceLoader;

    @Autowired
    Tika tika;

    /**
     * - MultipartFile
     *  ● 파일 업로드시 사용하는 메소드 아규먼트
     *  ● MultipartResolver 빈이 설정 되어 있어야 사용할 수 있다. (스프링 부트 자동 설정이 해 줌)
     *  ● POST multipart/form-data 요청에 들어있는 파일을 참조할 수 있다.
     *  ● List<MultipartFile> 아큐먼트로 여러 파일을 참조할 수도 있다.
     *
     * - 파일 업로드 관련 스프링 부트 설정
     *  ● MultipartAutoConfiguration
     *  ● MultipartProperties
     */
    @GetMapping("/file")
    public String fileUploadForm(Model model) {
        return "files/file";
    }

    @PostMapping("/file")
    public String fileUpload(@RequestParam MultipartFile file,
                             RedirectAttributes redirectAttributes) {

        // file save process

        System.out.println("file name : " + file.getName());
        System.out.println("file original name : " + file.getOriginalFilename());

        String message = file.getOriginalFilename() + " is uploaded";
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/file";

    }

    @GetMapping("/file/{filename}")
    public ResponseEntity<Resource> fileDownload(@PathVariable String filename) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:" + filename);
        File file = resource.getFile();

        // 파일의 CONTENT_TYPE을 알기 위해 사용하는 라이브러리
        String mediaType = tika.detect(file);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachement; filename=\"" + resource.getFilename()+"\"")
                .header(HttpHeaders.CONTENT_TYPE, mediaType)
                .header(HttpHeaders.CONTENT_LENGTH, file.length() + "")
                .body(resource);

    }

}
