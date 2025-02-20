package com.woowacourse.imagestorage.strategy.resize;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.imagestorage.ImageFormatDetector;
import com.woowacourse.imagestorage.ImageTypeTransfer;
import com.woowacourse.imagestorage.domain.ChangeWidth;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class PngImageResizeStrategyTest {

    private final PngImageResizeStrategy pngImageResizeStrategy = new PngImageResizeStrategy();

    @Nested
    class resize_메소드는 {

        @Nested
        class byte값과_변경할_width를_받는_경우 {

            private static final int RESIZE_WIDTH = 500;
            private byte[] image;

            @BeforeEach
            void setUp() throws IOException {
                File imageFile = Paths.get("src/test/resources/static/images/")
                        .resolve("test-image.jpeg")
                        .toFile();
                image = IOUtils.toByteArray(new FileInputStream(imageFile));
            }

            @Test
            void width의_비율만큼_resize한_Png이미지데이터를_반환한다() {
                byte[] actual = pngImageResizeStrategy.resize(image, new ChangeWidth(RESIZE_WIDTH));
                int actualWidth = ImageTypeTransfer.toBufferedImage(actual)
                        .getWidth();

                assertAll(
                        () -> assertThat(actualWidth).isEqualTo(RESIZE_WIDTH),
                        () -> assertThat(ImageFormatDetector.isPng(actual)).isTrue()
                );
            }
        }
    }
}
