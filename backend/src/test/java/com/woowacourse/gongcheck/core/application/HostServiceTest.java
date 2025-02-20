package com.woowacourse.gongcheck.core.application;

import static com.woowacourse.gongcheck.fixture.FixtureFactory.Host_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.gongcheck.ApplicationTest;
import com.woowacourse.gongcheck.SupportRepository;
import com.woowacourse.gongcheck.auth.application.EntranceCodeProvider;
import com.woowacourse.gongcheck.core.application.response.HostProfileResponse;
import com.woowacourse.gongcheck.core.domain.host.Host;
import com.woowacourse.gongcheck.core.presentation.request.HostProfileChangeRequest;
import com.woowacourse.gongcheck.core.presentation.request.SpacePasswordChangeRequest;
import com.woowacourse.gongcheck.exception.InfrastructureException;
import com.woowacourse.gongcheck.exception.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ApplicationTest
@DisplayName("HostService 클래스의")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class HostServiceTest {

    @Autowired
    private HostService hostService;

    @Autowired
    private SupportRepository repository;

    @Autowired
    private EntranceCodeProvider entranceCodeProvider;

    @Nested
    class changeSpacePassword_메소드는 {

        @Nested
        class 존재하는_HostId와_수정할_패스워드를_입력받는_경우 {

            private static final String CHANGING_PASSWORD = "4567";

            private final Host host = repository.save(Host_생성("1234", 1L));
            private final SpacePasswordChangeRequest spacePasswordChangeRequest = new SpacePasswordChangeRequest(
                    CHANGING_PASSWORD);

            @Test
            void 패스워드를_수정한다() {
                hostService.changeSpacePassword(host.getId(), spacePasswordChangeRequest);
                Host actual = repository.getById(Host.class, host.getId());

                assertThat(actual.getSpacePassword().getValue()).isEqualTo(CHANGING_PASSWORD);
            }
        }

        @Nested
        class 존재하지_않는_HostId를_입력받는_경우 {

            private static final String CHANGING_PASSWORD = "4567";
            private static final long NON_EXIST_HOST_ID = 0L;

            private final SpacePasswordChangeRequest spacePasswordChangeRequest = new SpacePasswordChangeRequest(
                    CHANGING_PASSWORD);

            @Test
            void 예외를_발생시킨다() {
                assertThatThrownBy(() -> hostService.changeSpacePassword(NON_EXIST_HOST_ID, spacePasswordChangeRequest))
                        .isInstanceOf(NotFoundException.class)
                        .hasMessageContaining("존재하지 않는 호스트입니다.");
            }
        }
    }

    @Nested
    class createEntranceCode_메소드는 {

        @Nested
        class 존재하는_HostId를_입력받는_경우 {

            private final Host host = repository.save(Host_생성("1234", 1L));
            private final String expected = entranceCodeProvider.createEntranceCode(host.getId());

            @Test
            void entranceCode를_반환한다() {
                String actual = hostService.createEntranceCode(host.getId());
                assertThat(actual).isEqualTo(expected);
            }
        }

        @Nested
        class 존재하지_않는_HostId를_입력받는_경우 {

            private static final long NON_EXIST_HOST_ID = 0L;

            @Test
            void 예외를_발생시킨다() {
                assertThatThrownBy(() -> hostService.createEntranceCode(NON_EXIST_HOST_ID))
                        .isInstanceOf(NotFoundException.class)
                        .hasMessageContaining("존재하지 않는 호스트입니다.");
            }
        }
    }

    @Nested
    class findProfile_메소드는 {

        @Nested
        class 존재하는_EntranceCode를_입력받는_경우 {

            private final Host host = repository.save(Host_생성("1234", 1L));

            @Test
            void profile을_반환한다() {
                String entranceCode = entranceCodeProvider.createEntranceCode(host.getId());
                HostProfileResponse actual = hostService.findProfile(entranceCode);
                assertThat(actual).extracting("imageUrl", "nickname")
                        .containsExactly("image.url", "nickname");
            }
        }

        @Nested
        class 존재하지_않는_EntranceCode를_입력받는_경우 {

            private static final String NON_EXIST_ENTRANCE_CODE = "ABCDEFGHIJKLMNOP";

            @Test
            void 예외를_발생시킨다() {
                assertThatThrownBy(() -> hostService.findProfile(NON_EXIST_ENTRANCE_CODE))
                        .isInstanceOf(InfrastructureException.class);
            }
        }
    }

    @Nested
    class changeProfile_메소드는 {

        @Nested
        class 존재하는_HostId를_입력받는_경우 {

            private static final String CHANGED_NAME = "changedName";

            private final Host host = repository.save(Host_생성("1234", 1L));

            @Test
            void profile을_변경한다() {
                hostService.changeProfile(host.getId(), new HostProfileChangeRequest(CHANGED_NAME));
                Host actual = repository.getById(Host.class, host.getId());

                assertThat(actual.getNickname().getValue()).isEqualTo(CHANGED_NAME);
            }
        }

        @Nested
        class 존재하지_않는_HostId를_입력받는_경우 {

            private static final long NON_EXIST_HOST_ID = 0L;

            @Test
            void 예외를_발생시킨다() {
                assertThatThrownBy(() -> hostService.changeProfile(NON_EXIST_HOST_ID, null))
                        .isInstanceOf(NotFoundException.class)
                        .hasMessageContaining("존재하지 않는 호스트입니다.");
            }
        }
    }
}
