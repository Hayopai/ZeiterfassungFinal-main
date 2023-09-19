package de.lange.zeiterfassung.controller;

import de.lange.zeiterfassung.model.ArbeitszeitEntity;
import de.lange.zeiterfassung.model.UsersModel;
import de.lange.zeiterfassung.service.ArbeitszeitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/arbeitszeit")
public class ArbeitszeitController {

    @Autowired
    private ArbeitszeitService arbeitszeitService;

    @PostMapping("/startArbeit")
    public ArbeitszeitEntity startArbeit(@RequestBody UsersModel usersModel) {
        return arbeitszeitService.startArbeit(usersModel.getId());
    }

    @PostMapping("/endeArbeit")
    public ArbeitszeitEntity endeArbeit(@RequestBody UsersModel usersModel) {
        return arbeitszeitService.endeArbeit(usersModel.getId());
    }

    @PostMapping("/startPause")
    public ArbeitszeitEntity startPause(@RequestBody UsersModel usersModel) {
        return arbeitszeitService.startPause(usersModel.getId());
    }

    @PostMapping("/endePause")
    public ArbeitszeitEntity endePause(@RequestBody UsersModel usersModel) {
        return arbeitszeitService.endePause(usersModel.getId());
    }
}
