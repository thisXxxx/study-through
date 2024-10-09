package team.weilai.studythrough.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.tika.metadata.HttpHeaders;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.helpers.DefaultHandler;

import java.io.InputStream;
import java.util.HashMap;

@Component
@Slf4j
public class FileTypeUtil {
    String pattern = "application/vnd.ms-excel";
    String pattern2 = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    public String getMimeType(MultipartFile file) throws Exception {
        AutoDetectParser parser = new AutoDetectParser();
        parser.setParsers(new HashMap<MediaType, Parser>());
        Metadata metadata = new Metadata();
        metadata.add("resourceName", file.getOriginalFilename());
        System.out.println(file.getName());
        InputStream stream = file.getInputStream();
        parser.parse(stream, new DefaultHandler(), metadata, new ParseContext());
       stream.close();
        return metadata.get(HttpHeaders.CONTENT_TYPE);
    }

    public boolean isExcel(MultipartFile file) {
        String mimeType = null;
        try {
            mimeType = getMimeType(file);
        } catch (Exception e) {
            log.error("文件判断异常，{}",e.getMessage());
            return false;
        }
        boolean matches = pattern.matches(mimeType);
        boolean matches1 = pattern2.matches(mimeType);
        return matches || matches1;
    }
}
