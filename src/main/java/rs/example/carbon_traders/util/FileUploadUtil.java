package rs.example.carbon_traders.common.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.web.multipart.MultipartFile;

public class FileUploadUtil {

    private static final String BASE_UPLOAD_DIR = "uploads";
//     * Save file to disk
//     * @param file Multipart file from request
//     * @param subDir folder name (farmer, company, soil-report etc.)
//     * @return saved file path
    public static String saveFile(MultipartFile file, String subDir) {

        if (file == null || file.isEmpty()) {
            throw new RuntimeException("File is empty or missing");
        }

        try {
            // Create base upload directory if not exists
            Path uploadPath = Paths.get(BASE_UPLOAD_DIR, subDir);
            Files.createDirectories(uploadPath);

            // Create unique file name
            String originalFileName = file.getOriginalFilename();
            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

            String fileName = timestamp + "_" + originalFileName;
            Path filePath = uploadPath.resolve(fileName);

            // Save file
            Files.copy(file.getInputStream(), filePath);

            return filePath.toString();

        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }
}

