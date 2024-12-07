package team.weilai.studythrough.websocket.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import team.weilai.studythrough.pojo.main.LoginUser;

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
        sec.getUserProperties().put("username",loginUser.getUsername());
        sec.getUserProperties().put("role",loginUser.getUser().getStatus());
        super.modifyHandshake(sec, request, response);
    }
}
