package com.strongcom.doormate.controller;

import com.strongcom.doormate.domain.Reminder;
import com.strongcom.doormate.dto.RequestDTO;
import com.strongcom.doormate.service.impl.AlarmService;
import com.strongcom.doormate.service.impl.FirebaseCloudMessageService;
import com.strongcom.doormate.service.impl.ReminderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class FCMController {


    private final FirebaseCloudMessageService firebaseCloudMessageService;
    private final ReminderService reminderService;
    private final AlarmService alarmService;

    @PostMapping("/api/fcm")
    public ResponseEntity pushMessage(@RequestParam String userName, @RequestParam LocalDateTime now) throws IOException {
        List<Reminder> reminders = firebaseCloudMessageService.findByNow(userName, now);
        for (Reminder reminder : reminders
        ) {
            System.out.println(reminder.getUser().getTargetToken() + " "
                    +reminder.getTitle() + " " + reminder.getContent());

            firebaseCloudMessageService.sendMessageTo(
                    reminder.getUser().getTargetToken(),
                    reminder.getTitle(),
                    reminder.getContent());
        }
        return ResponseEntity.ok().build();
    }
}
