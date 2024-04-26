package io.twobeers.service.one;

import io.twobeers.service.S3Service;
import io.twobeers.service.DefaultService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class OneService {
    @Inject
    S3Service s3;

    @Inject
    DefaultService parentService;

    public String runService() {
        System.out.println("io.twobeers.service.one.OneService, depend on DefaultService");
        String content = s3.readFile();
        System.out.println("S3 file content: " + content);
        content += parentService.runService();
        System.out.println("S3 file content: " + content);

        return content;
    }

}
