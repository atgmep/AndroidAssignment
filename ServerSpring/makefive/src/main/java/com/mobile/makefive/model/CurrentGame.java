package com.mobile.makefive.model;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value="session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CurrentGame {
    public GameData gameData;
    public boolean isCancelQueue;

}
