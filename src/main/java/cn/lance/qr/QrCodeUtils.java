package cn.lance.qr;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class QrCodeUtils {

    private static final int WIDTH = 300;
    private static final int HEIGHT = 300;
    private static final String IMG_FORMAT = "png";

    private QrCodeUtils() {
    }

    /**
     * 生成 QR Code
     *
     * @param content 内容
     * @return QR Code 图片（PNG 格式，Base64 编码）
     */
    public static String encode(String content) throws WriterException {
        Objects.requireNonNull(content, "content must not be null");

        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, StandardCharsets.UTF_8);
        hints.put(EncodeHintType.MARGIN, 2);

        BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, WIDTH, HEIGHT, hints);

        BufferedImage bufferedImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                if (bitMatrix.get(x, y)) {
                    bufferedImage.setRGB(x, y, Color.BLACK.getRGB());
                } else {
                    bufferedImage.setRGB(x, y, Color.WHITE.getRGB());
                }
            }
        }

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            ImageIO.write(bufferedImage, IMG_FORMAT, out);
            byte[] imageBytes = out.toByteArray();
            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 解析 QR Code
     *
     * @param imageBase64 QR Code 图片（Base64）
     * @return QR Code 内容
     */
    public static String decode(String imageBase64) throws NotFoundException, IOException {
        Objects.requireNonNull(imageBase64, "imageBase64 must not be null");

        byte[] imageBytes = Base64.getDecoder().decode(imageBase64);
        return decode(imageBytes);
    }

    /**
     * 解析 QR Code
     *
     * @param imageBytes QR Code 图片（字节数组）
     * @return QR Code 内容
     */
    public static String decode(byte[] imageBytes) throws NotFoundException, IOException {
        Objects.requireNonNull(imageBytes, "imageBytes must not be null");

        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes)) {
            BufferedImage bufferedImage = ImageIO.read(inputStream);

            if (bufferedImage == null) {
                throw new IOException("could not decode image from byte array");
            }

            BufferedImageLuminanceSource luminanceSource = new BufferedImageLuminanceSource(bufferedImage);
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(luminanceSource));

            Result result = new MultiFormatReader().decode(binaryBitmap);
            return result.getText();
        }
    }

}
