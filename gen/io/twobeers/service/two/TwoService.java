package io.twobeers.service.two;

import io.twobeers.service.S3Service;
import io.twobeers.service.DefaultService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class TwoService {
    @Inject
    S3Service s3;

    @Inject
    DefaultService parentService;

    public String runService() {
        System.out.println("io.twobeers.service.two.TwoService, depend on DefaultService");
        String content = s3.readFile();
        System.out.println("S3 file content: " + content);
        content += parentService.runService();
        System.out.println("S3 file content: " + content);

        return content;
    }

}
