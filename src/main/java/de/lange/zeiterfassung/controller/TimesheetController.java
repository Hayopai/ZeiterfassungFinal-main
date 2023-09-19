package de.lange.zeiterfassung.controller;

import de.lange.zeiterfassung.dataTransferObject.TimesheetDto;
import de.lange.zeiterfassung.model.ArbeitszeitEntity;
import de.lange.zeiterfassung.model.UsersModel;
import de.lange.zeiterfassung.service.ArbeitszeitService;
import de.lange.zeiterfassung.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/timesheet")
public class TimesheetController {

    @Autowired
    private ArbeitszeitService arbeitszeitService;

    @Autowired
    private UsersService usersService;

    @PostMapping
    public ResponseEntity<?> saveTimesheet(@RequestBody TimesheetDto timesheetDto) {

        ArbeitszeitEntity arbeitszeit = arbeitszeitService.saveArbeitszeitForUser(timesheetDto);
        return ResponseEntity.ok(arbeitszeit);
    }
}
