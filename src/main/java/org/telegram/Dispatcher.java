package org.telegram;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Component;
import org.telegram.handlers.utils.UserRequestHandler;
import org.telegram.handlers.utils.WhiteListUserRequestHandler;
import org.telegram.models.UserRequest;
import org.telegram.repository.WhiteListRepository;

@Component
public class Dispatcher {

    private final List<WhiteListUserRequestHandler> whiteListUserRequestHandler;
    private final List<UserRequestHandler> handlers;
    private final WhiteListRepository whiteListRepository;

    public Dispatcher(List<UserRequestHandler> handlers, List<WhiteListUserRequestHandler> whiteListUserRequestHandler,
                      WhiteListRepository whiteListRepository) {
        this.handlers = handlers;
        this.whiteListUserRequestHandler = whiteListUserRequestHandler;
        this.whiteListRepository = whiteListRepository;
    }

    public boolean dispatch(UserRequest userRequest) {
        if(Objects.isNull(whiteListRepository.findByName(userRequest.getUpdate().getMessage().getFrom().getUserName()))) {
            whiteListUserRequestHandler.stream().findFirst().get().handle(userRequest);
            return true;
        }
        for (UserRequestHandler userRequestHandler : handlers) {
            if (Objects.nonNull(whiteListRepository.findByName(userRequest.getUpdate().getMessage().getFrom().getUserName())) &&
                    userRequestHandler.isApplicable(userRequest)) {
                userRequestHandler.handle(userRequest);
                return true;
            }
        }
        return false;
    }
}
