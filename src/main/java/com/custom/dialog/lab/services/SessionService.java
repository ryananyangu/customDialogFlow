package com.custom.dialog.lab.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.custom.dialog.lab.models.Journey;
import com.custom.dialog.lab.models.Screen;
import com.custom.dialog.lab.models.Session;
import com.custom.dialog.lab.models.SessionHistory;
import com.custom.dialog.lab.repositories.SessionHistoryRepository;
import com.custom.dialog.lab.repositories.SessionRepository;
import com.custom.dialog.lab.utils.Props;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SessionService {

    @Autowired
    private Props props;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private SessionHistoryRepository sessionHistoryRepository;

    @Autowired
    private FlowService flowService;

    public String screenNavigate(String sessionId, String input, String serviceCode, String customerIdentifier) {
        Session session = new Session();
        session.setSessionId(sessionId);

        session.setDateLastModified(Calendar.getInstance().getTime());

        if (!sessionRepository.existsById(session.getSessionId()).block()) {
            return newSessionProcessor(session, serviceCode,customerIdentifier);
        }

        Session latestSession = sessionRepository.findById(session.getSessionId()).block();
        session.setDateCreated(latestSession.getDateCreated());
        Screen currentScreen = latestSession.getScreen();
        String shortoode_cont = sessionHistoryRepository.findById(session.getSessionId()).block().getServiceCode();
        HashMap<String, Screen> screens;
        String nextScreen = currentScreen.getScreenNext();
        try {
            screens = flowService.getFlowInstance(shortoode_cont).getScreens();
            if ("options".equalsIgnoreCase(currentScreen.getScreenType())) {
                input = optionsInputValidate(currentScreen.getNodeOptions(), input);
            } else if ("items".equalsIgnoreCase(currentScreen.getScreenType())) {
                HashMap<String, String> validateItem = itemsInputValidate(currentScreen.getNodeItems(), input);
                nextScreen = validateItem.get("nextScreen");
                input = validateItem.get("displayText");
            }
        } catch (Exception ex) {
            String display = "END " + ex.getMessage();
            return existingSessionProcessor(session, display, currentScreen);
        }

        if ("end".equalsIgnoreCase(nextScreen)) {
            String display = dynamicText("END " + currentScreen.getNodeExtraData().get("exit_message"),
                    latestSession.getExtraData());

            return existingSessionProcessor(session, display, currentScreen);
        }

        latestSession.extraDataBuilder(currentScreen.getNodeName(), input);
        Screen screen = screens.get(nextScreen);
        String display = displayText(screen);

        display = dynamicText(display, latestSession.getExtraData());

        return existingSessionProcessor(session, display, screen);
    }

    public String optionsInputValidate(List<String> options, String input) throws Exception {
        int convertedInput = Integer.parseInt(input);
        if (options.size() < convertedInput) {
            throw new Exception(props.getFlowError("2"));
        }
        return options.get(convertedInput - 1);
    }

    public HashMap<String, String> itemsInputValidate(List<HashMap<String, String>> items, String input)
            throws Exception {
        int convertedInput = Integer.parseInt(input);
        if (items.size() < convertedInput) {
            throw new Exception(props.getFlowError("2"));
        }
        return items.get(convertedInput - 1);

    }

    public String displayText(Screen screen) {

        String screenText = screen.getScreenText();
        if ("items".equalsIgnoreCase(screen.getScreenType())) {

            int count = 1;
            for (HashMap<String, String> item : screen.getNodeItems()) {
                screenText += "\n" + count + ". " + String.valueOf(item.get("displayText"));
                count++;
            }
        } else if ("options".equalsIgnoreCase(screen.getScreenType())) {
            int count = 1;

            for (String opt : screen.getNodeOptions()) {
                screenText += "\n" + count + ". " + opt;
                count++;
            }
        }
        return screenText;
    }

    public String dynamicText(String screenText, HashMap<String, Object> sessionDetails) {
        if (!screenText.contains("^")) {
            return "CON " + screenText;
        }
        for (String key : sessionDetails.keySet()) {
            String placeholder = "^" + key + "^";
            if (screenText.contains(placeholder)) {
                screenText = screenText.replace(placeholder, sessionDetails.get(key).toString());
            }
        }

        if (screenText.contains("^")) {
            return "END " + props.getFlowError("6");
        }

        return "CON " + screenText;
    }

    public String newSessionProcessor(Session session, String input, String customerIdentifier) {
        
        
        Date currentDate = Calendar.getInstance().getTime();
        
        Screen screen;

        try {
            screen = flowService.getFlowInstance(input).getScreens().get("start_page");
        } catch (Exception ex) {
            return "END " + ex.getMessage();
        }
        String display = displayText(screen);

        // Setting up the sessionDetails
        Journey journey = new Journey();
        journey.setInput(input);
        journey.setResponse(display);
        session.setScreen(screen);
        session.setJourney(journey);
        session.setDateCreated(currentDate);
        session.setDateLastModified(currentDate);
        sessionRepository.save(session).block();

        // Starting the history of the session
        SessionHistory sessionHistory = new SessionHistory();
        sessionHistory.setDateCreated(currentDate);
        sessionHistory.setDateLastModified(currentDate);
        sessionHistory.setServiceCode(input);
        sessionHistory.setSessionId(session.getSessionId());
        sessionHistory.setStatus("INCOMPLETE");
        sessionHistory.setPhoneNumber(customerIdentifier);
        List<Session> sessions = new ArrayList<>();
        sessions.add(session);
        sessionHistory.setSessions(sessions);
        sessionHistoryRepository.save(sessionHistory).block();

        return display;

    }

    public String existingSessionProcessor(Session session, String display, Screen screen) {
        Journey journey = new Journey();
        journey.setResponse(display);
        session.setScreen(screen);
        session.setJourney(journey);

        SessionHistory sessionHistory = sessionHistoryRepository.findById(session.getSessionId()).block();
        sessionHistory.setDateLastModified(Calendar.getInstance().getTime());
        if (display.startsWith("END")) {
            sessionHistory.setStatus("COMPLETE");
            sessionRepository.deleteById(session.getSessionId()).block();
        } else {
            sessionHistory.setStatus("INCOMPLETE");
            sessionRepository.save(session).block();
        }

        List<Session> sessions = sessionHistory.getSessions();
        sessions.add(session);
        sessionHistory.setSessions(sessions);
        return display;

    }

}