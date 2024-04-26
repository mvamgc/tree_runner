package io.twobeers.service.two.one;

import io.twobeers.service.S3Service;
import io.twobeers.service.one.OneService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class TwoOneService {
    @Inject
    S3Service s3;

    @Inject
    OneService parentService;

    public String runService() {
        System.out.println("io.twobeers.service.two.one.TwoOneService, depend on OneService");
        String content = s3.readFile();
        System.out.println("S3 file content: " + content);
        content += parentService.runService();
        System.out.println("S3 file content: " + content);

        return content;
    }

}
