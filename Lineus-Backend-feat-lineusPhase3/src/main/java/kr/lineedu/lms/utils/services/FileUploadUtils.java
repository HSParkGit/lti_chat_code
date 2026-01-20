package kr.lineedu.lms.utils.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@ConditionalOnBean(name = "folderClient")
public class FileUploadUtils {
    @Autowired(required = false)
    private Object folderClient; // Using Object to avoid compilation error when FolderClient doesn't exist

    @Autowired
    private RestTemplate restTemplate;

    @Value("${canvas.master-token:}")
    private String authorization;

    //using rest-template to call redirect api without confusion for feign client
    // Note: This method requires FileUploadFeignResponse and FileUploadResponse classes to exist
    // Commented out until required classes are available
    /*
    public Object completeFileUpload(MultipartFile file, Object tempUploadResponse) throws IOException {
        // Implementation requires FileUploadFeignResponse and FileUploadResponse classes
        throw new UnsupportedOperationException("FileUploadFeignResponse and FileUploadResponse classes are required");
    }

    private MultiValueMap<String, Object> createUploadRequestBody(Object tempUploadResponse, String filename, MultipartFile file) {
        // Implementation requires FileUploadFeignResponse class
        throw new UnsupportedOperationException("FileUploadFeignResponse class is required");
    }
    */
}
