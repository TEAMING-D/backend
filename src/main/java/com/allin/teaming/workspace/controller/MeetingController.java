import com.allin.teaming.workspace.dto.MeetingDTO;
import com.allin.teaming.workspace.service.MeetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/meetings")
@RequiredArgsConstructor
public class MeetingController {

    private final MeetingService meetingService;

    @PostMapping
    public ResponseEntity<MeetingDTO> createMeeting(@RequestBody MeetingDTO meetingDTO) {
        MeetingDTO createdMeeting = meetingService.createMeeting(meetingDTO);
        return new ResponseEntity<>(createdMeeting, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MeetingDTO> getMeetingById(@PathVariable Long id) {
        MeetingDTO meetingDTO = meetingService.getMeetingById(id);
        return new ResponseEntity<>(meetingDTO, HttpStatus.OK);
    }
}
