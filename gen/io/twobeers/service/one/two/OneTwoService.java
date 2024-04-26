package io.twobeers.service.one.two;

import io.twobeers.service.S3Service;
import io.twobeers.service.two.TwoService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class OneTwoService {
    @Inject
    S3Service s3;

    @Inject
    TwoService parentService;

    public String runService() {
        System.out.println("io.twobeers.service.one.two.OneTwoService, depend on TwoService");
        String content = s3.readFile();
        System.out.println("S3 file content: " + content);
        content += parentService.runService();
        System.out.println("S3 file content: " + content);

        return content;
    }

}
