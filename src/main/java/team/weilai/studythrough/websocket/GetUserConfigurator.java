package team.weilai.studythrough.websocket;


import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import team.weilai.studythrough.pojo.LoginUser;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

@Slf4j
public class GetUserConfigurator extends ServerEndpointConfig.Configurator {
    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        UsernamePasswordAuthenticationToken principal =(UsernamePasswordAuthenticationToken)request.getUserPrincipal();
        LoginUser loginUser =(LoginUser)principal.getPrincipal();
        sec.getUserProperties().put("id",String.valueOf(loginUser.getUser().getUserId()));
        super.modifyHandshake(sec, request, response);
    }
}
