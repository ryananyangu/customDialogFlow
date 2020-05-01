package com.custom.dialog.lab.controller;

import com.custom.dialog.lab.pojo.ScreenNode;
import com.custom.dialog.lab.pojo.Session;
import com.custom.dialog.lab.utils.Props;
import java.util.Map;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class DialogFlowController {

    private static final Props SETTINGS = new Props();

    @GetMapping(path = "/get/atussd/flow", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public String sessionNavigator(@RequestParam String msisdn,
            @RequestParam String session, @RequestParam String input) {
        Session session_menu = new Session(msisdn, session, input);
        Map<String, Object> nextScreenData = session_menu.screenNavigate();
        return session_menu.displayText(nextScreenData);
    }

    @ResponseBody
    @PostMapping(path = "/screen/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String createScreen(@RequestBody Object screen) {
        ScreenNode screenNode = new ScreenNode();
        return screenNode.saveRedisData(screen).toString();
    }

    @ResponseBody
    @PostMapping(path = "/screen/delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String deleteScreen(@RequestBody Object screen) {
        ScreenNode screenNode = new ScreenNode();
        return screenNode.deleteData(screen).toString();
    }

    @ResponseBody
    @PostMapping(path = "/screen/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String updateScreen(@RequestBody Object screen) {
        ScreenNode screenNode = new ScreenNode();
        return screenNode.updateScreen(screen).toString();
    }

    @ResponseBody
    @PostMapping(path = "/screen/bulk/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String bulkCreateScreen(@RequestBody Object screens) {
        ScreenNode screenNode = new ScreenNode();
        JSONObject validation = screenNode.isValidFlow(screens);

        if (!validation.isEmpty()) {
            return validation.toString();
        }
        return screenNode.bulkSave().toString();
    }

    @ResponseBody
    @PostMapping(path = "/flow/validate", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String validateFlow(@RequestBody Object screens) {
        ScreenNode screenNode = new ScreenNode();
        JSONObject validation = screenNode.isValidFlow(screens);

        if (!validation.isEmpty()) {
            return validation.toString();
        }
        return SETTINGS.getStatusResponse("200_SCRN", screens).toString();
    }

    @GetMapping(path = "/",produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public String getFlow(@RequestParam String shortcode) {
        ScreenNode screenNode = new ScreenNode();
        return screenNode.getFlow(shortcode).toString();
    }

}
