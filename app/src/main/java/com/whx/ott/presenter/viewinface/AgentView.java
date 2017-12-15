package com.whx.ott.presenter.viewinface;

/**
 * Created by oleky on 2017/12/6.
 */

public interface AgentView {
    void agentLoginSucc(String agent,String agentname,String macAddress,String lastip,String lastAddress);

    void agentLoginFailed(String error);

    void stuLoginSucc();

    void stuLoginFailed(String error);
}
