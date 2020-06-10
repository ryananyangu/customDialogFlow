package com.saada.flows.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import com.saada.flows.externalServices.ExternalRequestProcessor;
import com.saada.flows.models.Flow;
import com.saada.flows.models.Journey;
import com.saada.flows.models.Screen;
import com.saada.flows.models.Session;
import com.saada.flows.models.SessionHistory;
import com.saada.flows.repositories.SessionHistoryRepository;
import com.saada.flows.repositories.SessionRepository;
import com.saada.flows.utils.Props;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;

@Service
public class SessionService {

    @Value("${paginate.page.size}")
    private int pageSize;

    @Autowired
    private Props props;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private SessionHistoryRepository sessionHistoryRepository;

    @Autowired
    private FlowService flowService;

    @Autowired
    private OrganizationService organizationService;

    // private Session latestSession;

    public String screenNavigate(String sessionId, String input, String serviceCode, String customerIdentifier) {
        Session session = new Session();
        session.setSessionId(sessionId);

        session.setDateLastModified(Calendar.getInstance().getTime());

        if (!sessionRepository.existsById(session.getSessionId()).block()) {
            return newSessionProcessor(session, serviceCode, customerIdentifier);
        }

        session = sessionRepository.findById(session.getSessionId()).block();
        session.setDateLastModified(Calendar.getInstance().getTime());
        session.setDateCreated(session.getDateCreated());
        Screen currentScreen = session.getScreen();
        String shortoode_cont = sessionHistoryRepository.findById(session.getSessionId()).block().getServiceCode();
        HashMap<String, Screen> screens;
        String nextScreen = currentScreen.getScreenNext();
        HashMap<String, Object> extraData = session.getExtraData();

        try {
            screens = flowService.getFlowInstance(shortoode_cont).getScreens();
            if ("options".equalsIgnoreCase(currentScreen.getScreenType())) {
                input = optionsInputValidate(currentScreen.getNodeOptions(), input);
                extraData.put(currentScreen.getNodeName(), input);
            } else if ("items".equalsIgnoreCase(currentScreen.getScreenType())) {
                HashMap<String, String> validateItem = itemsInputValidate(currentScreen.getNodeItems(), input);
                nextScreen = validateItem.get("nextScreen");
                input = validateItem.get("displayText");
                extraData.put(currentScreen.getNodeName(), input);
            }
            
        } catch (Exception ex) {
            HashMap<String, String> add_exit = currentScreen.getNodeExtraData();
            add_exit.put("exit_message", ex.getMessage());

            currentScreen.setNodeExtraData(add_exit);

            return existingSessionProcessor(session, currentScreen, input, "END ");
        }

        if ("end".equalsIgnoreCase(nextScreen)) {
            return existingSessionProcessor(session, currentScreen, input, "END ");
        }

        Screen screen = screens.get(nextScreen);

        return existingSessionProcessor(session, screen, input, "CON ");
    }

    public String optionsInputValidate(List<String> options, String input) throws Exception {
        String response = new String();
        for (String item : input.split(",")) {
            int convertedInput = Integer.parseInt(item);
            if (options.size() < convertedInput) {
                throw new Exception(props.getFlowError("2"));
            }
            response += "\n" + options.get(convertedInput - 1) + ",";
        }

        return response;
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
        } else if ("options".equalsIgnoreCase(screen.getScreenType())
                || "external".equalsIgnoreCase(screen.getScreenType())) {
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
            return screenText;
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

        return screenText;
    }

    public String newSessionProcessor(Session session, String input, String customerIdentifier) {

        Date currentDate = Calendar.getInstance().getTime();
        Flow flow;

        Screen screen;

        try {
            flow = flowService.getFlowInstance(input);
            screen = flow.getScreens().get("start_page");
            screen = processExternal(screen, session,customerIdentifier);
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
        sessionHistory.setOrganization(flow.getOrganization());
        sessionHistoryRepository.save(sessionHistory).block();

        return "CON " + display;

    }

    public String existingSessionProcessor(Session session, Screen screen, String input, String action) {

        String display = new String();
        HashMap<String, Object> extraData = session.getExtraData();
        SessionHistory sessionHistory = sessionHistoryRepository.findById(session.getSessionId()).block();
        extraData.put(session.getScreen().getNodeName(), input);

        try {
            screen = processExternal(screen, session,sessionHistory.getPhoneNumber());
        } catch (Exception e) {
            screen = session.getScreen();
            HashMap<String, String> add_exit = screen.getNodeExtraData();
            add_exit.put("exit_message", e.getMessage());
            screen.setNodeExtraData(add_exit);
            action = "END ";
        }
        if (session.getScreen().getNodeName().equalsIgnoreCase(screen.getNodeName())) {


            display = action + screen.getNodeExtraData().get("exit_message");
        } else {
            display = action + displayText(screen);
        }

        display = dynamicText(display, extraData);

        Journey journey = new Journey();
        journey.setInput(input);
        journey.setResponse(display);
        session.setScreen(screen);
        session.setJourney(journey);
        session.setExtraData(extraData);

        
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
        sessionHistoryRepository.save(sessionHistory).block();
        return display;

    }

    public Screen processExternal(Screen screen, Session session, String phoneNumber) throws Exception {


        ExternalRequestProcessor processor = new ExternalRequestProcessor();
        processor.setScreen(screen);
        processor.setSession(session);
        processor.setPhoneNumber(phoneNumber);
        return processor.processResponse();

    }

    public JSONObject listSessions(boolean isAdmin, Optional<Integer> page, Optional<String> organization) {
        List<SessionHistory> sessions;
        String org = organizationService.getLoggedInUserOrganization().getName();
        Pageable pageable = PageRequest.of(page.orElse(0), pageSize,
                Sort.by(Direction.DESC, "dateCreated").descending());

        if (isAdmin) {
            // sessions
            Flux<SessionHistory> sessionHist = sessionHistoryRepository.findByOrganization(organization.orElse(org),
                    pageable);
            sessions = sessionHist.collectList().block();

        }
        sessions = sessionHistoryRepository.findByOrganization(org, pageable).collectList().block();
        return props.getStatusResponse("200_SCRN", sessions);
    }

}