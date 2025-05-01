package com.moogsan.moongsan_backend.global.util;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.UUID;

public class ImageUtils {

    // 허용 확장자 목록
    private static final Set<String> ALLOWED_EXT = Set.of(".jpeg", ".jpg", ".png", ".webp");

    /**
     * 원본 파일명에서 확장자를 추출하고 소문자로 반환.
     */
    public static String getExtension(String originalFilename) {
        String clean = StringUtils.cleanPath(originalFilename);
        int idx = clean.lastIndexOf('.');
        if (idx == -1) return "";
        return clean.substring(idx).toLowerCase();
    }

    /**
     * 확장자가 허용 목록에 있는지 검증.
     * @throws IllegalArgumentException 허용되지 않은 확장자일 경우
     */
    public static void validateExtension(String extension) {
        if (!ALLOWED_EXT.contains(extension)) {
            throw new IllegalArgumentException("허용되지 않은 파일 형식입니다: " + extension);
        }
    }

    /**
     * UUID 기반 고유 파일명 생성.
     * @param prefix 파일명 앞에 붙일 접두사(ex: "thumbnail_")
     * @param extension ".jpg" 등
     */
    public static String generateFilename(String prefix, String extension) {
        return prefix + UUID.randomUUID() + extension;
    }

    /**
     * 디렉토리가 없으면 생성.
     */
    public static void createDirsIfNotExist(Path dir) throws IOException {
        if (Files.notExists(dir)) {
            Files.createDirectories(dir);
        }
    }

    /**
     * MultipartFile을 지정된 경로에 저장.
     */
    public static void saveFile(MultipartFile file, Path target) throws IOException {
        try (InputStream in = file.getInputStream()) {
            Files.copy(in, target, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        }
    }

    /**
     * 입력 스트림으로부터 이미지를 읽어 리사이즈 후 저장.
     * @param in       원본 이미지 InputStream
     * @param target   저장할 파일 경로
     * @param width    리사이즈할 가로 픽셀 (높이는 원본 비율 유지)
     */
    public static void resizeAndSave(InputStream in, Path target, int width) throws IOException {
        BufferedImage original = ImageIO.read(in);
        int origW = original.getWidth();
        int origH = original.getHeight();
        int height = (width * origH) / origW;

        BufferedImage resized = new BufferedImage(width, height, original.getType());
        Graphics2D g = resized.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(original, 0, 0, width, height, null);
        g.dispose();

        String ext = getExtension(target.getFileName().toString()).replaceFirst("\\.", "");
        ImageIO.write(resized, ext, target.toFile());
    }
}
