package hsim.lib.core.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import hsim.lib.exception.ValidationLibException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;

@Slf4j
public class ValidationFileUtil {

    public static String getEncodingFileName(String fn) {
        try {
            return URLEncoder.encode(fn, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new ValidationLibException("unSupported fiel encoding : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static void initFileSendHeader(HttpServletResponse res, String filename, String contentType) {

        filename = getEncodingFileName(filename);
        if (contentType != null) {
            res.setContentType(contentType);
        } else {
            res.setContentType("applicaiton/download;charset=utf-8");
        }
        res.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\";");
        res.setHeader("Content-Transfer-Encoding", "binary");
        res.setHeader("file-name", filename);

    }

    public static void sendFileToHttpServiceResponse(File file, HttpServletResponse res, String contentType) {

        if (file == null || res == null) {
            return;
        }
        String filename = getEncodingFileName(file.getName());
        initFileSendHeader(res, filename, contentType);

        res.setContentLength((int) file.length());
        try {
            res.getOutputStream().write(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            throw new ValidationLibException("file io error :" + e.getMessage());
        }
    }

    public static void sendFileToHttpServiceResponse(String fileName, Object bodyObj, HttpServletResponse res) {

        if (fileName == null || bodyObj == null) {
            return;
        }

        String body = null;
        try {
            body = ValidationObjUtil.getDefaultObjectMapper().writeValueAsString(bodyObj);
        } catch (JsonProcessingException e) {
            log.info(e.getMessage());
        }

        String filename = getEncodingFileName(fileName);
        initFileSendHeader(res, filename, null);

        byte[] bytes = body.getBytes();
        res.setContentLength(bytes.length);
        try {
            res.getOutputStream().write(bytes);
        } catch (IOException e) {
            throw new ValidationLibException("file io error :" + e.getMessage());
        }
    }
}
