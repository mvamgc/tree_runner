package io.twobeers.service.three;

import io.twobeers.service.S3Service;
import io.twobeers.service.DefaultService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ThreeService {
    @Inject
    S3Service s3;

    @Inject
    DefaultService parentService;

    public String runService() {
        System.out.println("io.twobeers.service.three.ThreeService, depend on DefaultService");
        String content = s3.readFile();
        System.out.println("S3 file content: " + content);
        content += parentService.runService();
        System.out.println("S3 file content: " + content);

        return content;
    }

}
