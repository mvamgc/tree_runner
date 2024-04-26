package io.twobeers.service.two.three;

import io.twobeers.service.S3Service;
import io.twobeers.service.three.ThreeService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class TwoThreeService {
    @Inject
    S3Service s3;

    @Inject
    ThreeService parentService;

    public String runService() {
        System.out.println("io.twobeers.service.two.three.TwoThreeService, depend on ThreeService");
        String content = s3.readFile();
        System.out.println("S3 file content: " + content);
        content += parentService.runService();
        System.out.println("S3 file content: " + content);

        return content;
    }

}
