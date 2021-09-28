package lwi.vision.web.rest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Optional;
import lwi.vision.IntegrationTest;
import lwi.vision.domain.*;
import lwi.vision.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SearchResourceTest {

    private static final String API_URL = "/search/";

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private SoftwareRepository softwareRepository;

    @Autowired
    private FirmwareRepository firmwareRepository;

    @Autowired
    private SoftwareUpdateRepository softwareUpdateRepository;

    @Autowired
    private FirmwareUpdateRepository firmwareUpdateRepository;

    private BoardEntity boardEntity;

    @Autowired
    private MockMvc restBoardMockMvc;

    @MockBean
    private DateTimeProvider dateTimeProvider;

    public static BoardEntity createEntity() {
        BoardEntity boardEntity = new BoardEntity().serial("DEFAULT_SERIAL");
        return boardEntity;
    }

    private LocalDateTime createLocalDateTime(String date) {
        return ZonedDateTime.parse(date).toLocalDateTime();
    }

    @BeforeEach
    @Transactional
    void beforeEach() {}

    private FirmwareEntity createFirmware(String version, String path, BoardEntity board) {
        FirmwareEntity entity = new FirmwareEntity().path(path).version(version).board(board);
        return entity;
    }

    private SoftwareEntity createSoftware(String version, String path, BoardEntity board) {
        SoftwareEntity entity = new SoftwareEntity().path(path).version(version).board(board);
        return entity;
    }

    @Test
    @Transactional
    void searchWithLatestVersion() throws Exception {
        BoardEntity board = this.boardRepository.save(new BoardEntity().serial("SERIAL"));

        when(dateTimeProvider.getNow()).thenReturn(Optional.of(createLocalDateTime("2021-09-26T20:49:16.487806Z")));
        board.getSoftware().add(softwareRepository.save(createSoftware("1.0", "SW_PATH", board)));
        board.getFirmware().add(firmwareRepository.save(createFirmware("1.0", "FW_PATH", board)));

        when(dateTimeProvider.getNow()).thenReturn(Optional.of(createLocalDateTime("2021-09-27T20:49:16.487806Z")));
        board.getSoftware().add(softwareRepository.save(createSoftware("2.0", "SW_PATH", board)));
        board.getFirmware().add(firmwareRepository.save(createFirmware("2.0", "FW_PATH", board)));

        HashMap<String, String> request = new HashMap<>();
        request.put("serial", "SERIAL");
        request.put("firmware", "1.0");
        request.put("software", "1.0");

        restBoardMockMvc
            .perform(post(API_URL).contentType(MediaType.APPLICATION_JSON_VALUE).content(TestUtil.convertObjectToJsonBytes(request)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.sw_update").value("2.0"))
            .andExpect(jsonPath("$.fw_update").value("2.0"));
    }

    @Test
    @Transactional
    void searchWithUpdate() throws Exception {
        BoardEntity board = this.boardRepository.save(new BoardEntity().serial("SERIAL"));

        //        when(dateTimeProvider.getNow()).thenReturn(Optional.of(createLocalDateTime("2021-09-26T20:49:16.487806Z")));
        SoftwareEntity software1 = softwareRepository.save(createSoftware("1.0", "SW_PATH", board));
        board.getSoftware().add(software1);
        FirmwareEntity firmware1 = firmwareRepository.save(createFirmware("1.0", "FW_PATH", board));
        board.getFirmware().add(firmware1);

        //        when(dateTimeProvider.getNow()).thenReturn(Optional.of(createLocalDateTime("2021-09-27T20:49:16.487806Z")));
        SoftwareEntity software2 = softwareRepository.save(createSoftware("2.0", "SW_PATH", board));
        board.getSoftware().add(software2);
        FirmwareEntity firmware2 = firmwareRepository.save(createFirmware("2.0", "FW_PATH", board));
        board.getFirmware().add(firmware2);

        softwareUpdateRepository.save(new SoftwareUpdateEntity().board(board).from(software2).to(software1));
        firmwareUpdateRepository.save(new FirmwareUpdateEntity().board(board).from(firmware2).to(firmware1));

        HashMap<String, String> request = new HashMap<>();
        request.put("serial", "SERIAL");
        request.put("firmware", "2.0");
        request.put("software", "2.0");

        restBoardMockMvc
            .perform(post(API_URL).contentType(MediaType.APPLICATION_JSON_VALUE).content(TestUtil.convertObjectToJsonBytes(request)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.sw_update").value("1.0"))
            .andExpect(jsonPath("$.fw_update").value("1.0"));
    }
}
