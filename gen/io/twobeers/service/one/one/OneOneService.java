package io.twobeers.service.one.one;

import io.twobeers.service.S3Service;
import io.twobeers.service.one.OneService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class OneOneService {
    @Inject
    S3Service s3;

    @Inject
    OneService parentService;

    public String runService() {
        System.out.println("io.twobeers.service.one.one.OneOneService, depend on OneService");
        String content = s3.readFile();
        System.out.println("S3 file content: " + content);
        content += parentService.runService();
        System.out.println("S3 file content: " + content);

        return content;
    }

}
