package com.icreon.configuration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.security.oauth2.resource.JwtAccessTokenConverterConfigurer;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JwtAccessTokenCustomizer extends DefaultAccessTokenConverter implements JwtAccessTokenConverterConfigurer {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAccessTokenCustomizer.class);

    private static final String CLIENT_NANE_ELEMENT_IN_JWT = "resource_access";

    private static final String ROLE_ELEMENT_IN_JWT = "roles";

    private ObjectMapper mapper;

    public JwtAccessTokenCustomizer(ObjectMapper mapper){
        this.mapper = mapper;
        LOGGER.info("Initialized {}", JwtAccessTokenCustomizer.class.getSimpleName());
    }

    @Override
    public void configure(JwtAccessTokenConverter jwtAccessTokenConverter) {
        jwtAccessTokenConverter.setAccessTokenConverter(this);
        LOGGER.info("Configured {}", JwtAccessTokenConverter.class.getSimpleName());
    }

    @Override
    public OAuth2Authentication extractAuthentication(Map<String, ?> tokenMap) {
        LOGGER.info("Begin extractAuthentication: tokenMap = {}", tokenMap);
        JsonNode token =mapper.convertValue(tokenMap, JsonNode.class);
        Set<String> audienceList = extractClients(token); // extracting client names
        LOGGER.info("Clients = {}", audienceList);
        List<GrantedAuthority> authorities = extractRoles(token); // extracting client roles
        LOGGER.info("Authorities = {}", authorities);
        OAuth2Authentication authentication = super.extractAuthentication(tokenMap);
        OAuth2Request oAuth2Request = authentication.getOAuth2Request();

        OAuth2Request request =
                new OAuth2Request(oAuth2Request.getRequestParameters(), oAuth2Request.getClientId(), authorities, true, oAuth2Request.getScope(),
                        audienceList, null, null, null);

        Authentication usernamePasswordAuthentication = new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), "N/A", authorities);
        LOGGER.debug("End extractAuthentication");
        return new OAuth2Authentication(request, usernamePasswordAuthentication);
    }

    public List<GrantedAuthority> extractRoles(JsonNode jsonNode){
        LOGGER.debug("Begin extractRoles: jwt = {}", jsonNode);
        Set<String> rolesWithPrefix = new HashSet<>();

        jsonNode.path(CLIENT_NANE_ELEMENT_IN_JWT)
                .elements()
                .forEachRemaining(e -> e.path(ROLE_ELEMENT_IN_JWT)
                        .elements()
                        .forEachRemaining(r -> rolesWithPrefix.add("ROLE_" + r.asText())));

        final List<GrantedAuthority> authorityList = AuthorityUtils.createAuthorityList(rolesWithPrefix.toArray(new String[0]));
        LOGGER.debug("End extractRoles: roles = {}", authorityList);
        return authorityList;
    }

    public Set<String>extractClients(JsonNode jsonNode){
        LOGGER.debug("Begin extract clients :: {}", jsonNode);
        if(jsonNode.has(CLIENT_NANE_ELEMENT_IN_JWT)){
            JsonNode resourceAccessJsonNode =jsonNode.path(CLIENT_NANE_ELEMENT_IN_JWT);
            final Set<String> clientNames = new HashSet<>();
            resourceAccessJsonNode.fieldNames()
                    .forEachRemaining(clientNames::add);
            LOGGER.debug("End extractClients: clients = {}", clientNames);
            return clientNames;
        } else {
            throw new IllegalArgumentException("Expected element " + CLIENT_NANE_ELEMENT_IN_JWT + " not found in token");
        }
    }

}
