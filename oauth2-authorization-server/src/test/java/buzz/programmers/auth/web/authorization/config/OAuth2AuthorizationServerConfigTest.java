package buzz.programmers.auth.web.authorization.config;

import buzz.programmers.auth.AuthorizationApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
    
@WebAppConfiguration
@SpringBootTest(classes = AuthorizationApplication.class)
@EnableConfigurationProperties
class OAuth2AuthorizationServerConfigTest {
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).apply(springSecurity()).build();
    }

    @Test
    void preflightRequestForPostOnTokenEndpoint() throws Exception {
        mockMvc.perform(options("/oauth/token")
                .header("Access-Control-Request-Method", "POST")
                .header("Access-Control-Request-Headers", "authorization")
                .header("Origin", "http://localhost:3000")
                .header("User-Agent", "Mozilla/5.0")
                .header("DNT", "1")
                .header("Accept", "*/*")
                .header("Sec-Fetch-Site", "same-site")
                .header("Sec-Fetch-Mode", "cors")
                .header("Referer", "http://localhost:3000/oAuthCallback?code=zHINgb")
                .header("Accept-Encoding", "gzip, deflate, br")
                .header("Accept-Language", "en-US,en;q=0.9,sv;q=0.8,da;q=0.7")
        ).andDo(print()).andExpect(status().isOk());
    }

}