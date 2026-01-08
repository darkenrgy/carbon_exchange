package rs.example.carbon_traders.ai.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Path;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import rs.example.carbon_traders.common.util.FileUploadUtil;

@Service
public class AIVerificationService {

    private static final String PYTHON_SCRIPT =
            "src/main/java/rs/example/carbon_traders/ai/python/crop_verification.py";

    public String verifyCrop(MultipartFile image, String cropType) {

        try {
            //Save image
            String imagePath = FileUploadUtil.saveFile(image, "ai-crop");

            //Run python script
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "python",
                    PYTHON_SCRIPT,
                    imagePath,
                    cropType
            );

            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            //Read output
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            String output;
            StringBuilder result = new StringBuilder();

            while ((output = reader.readLine()) != null) {
                result.append(output);
            }

            process.waitFor();
            return result.toString();

        } catch (Exception e) {
            throw new RuntimeException("AI verification failed");
        }
    }
}

