package DataTransferObjectTest;

import fxibBackend.dto.AdminDTOS.AdminUserDTO;
import fxibBackend.dto.AdminDTOS.RolesAdminDTO;
import fxibBackend.dto.AuthorizationDTOS.JwtAuthResponseDTO;
import fxibBackend.dto.AuthorizationDTOS.LoginDTO;
import fxibBackend.dto.AuthorizationDTOS.RegisterDTO;
import fxibBackend.dto.AuthorizationDTOS.UserRegisterUsernameAndEmailDTO;
import fxibBackend.dto.CommunityDTOS.*;
import fxibBackend.dto.InitDTOS.AboutDTO;
import fxibBackend.dto.InitDTOS.PartnerDTO;
import fxibBackend.dto.InitDTOS.PricingDTO;
import fxibBackend.dto.TradingAccountsDTOS.AccountPage.TradeDTO;
import fxibBackend.dto.TradingAccountsDTOS.AccountPage.TradingAccountDTO;
import fxibBackend.dto.TradingAccountsDTOS.Footer.TradingAccountFooterDTO;
import fxibBackend.dto.UserDetailsDTO.LocationDTO;
import fxibBackend.dto.UserDetailsDTO.StripeTransactionDTO;
import fxibBackend.dto.UserDetailsDTO.UpdateUserBiographyDTO;
import fxibBackend.dto.UserDetailsDTO.UserDetailsDTO;
import fxibBackend.entity.RoleEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


public class DTOTests {

    @Test
    public void adminUserDtoTest() {
        AdminUserDTO adminUserDTO = new AdminUserDTO();
        adminUserDTO.setUsername("username");
        adminUserDTO.setEmail("user@example.com");
        adminUserDTO.setSubscription("subscription");
        adminUserDTO.setRegistrationDate("2023-09-28");
        adminUserDTO.setRoles(Set.of(new RoleEntity()));

        assertThat(adminUserDTO.getUsername()).isEqualTo("username");
        assertThat(adminUserDTO.getEmail()).isEqualTo("user@example.com");
        assertThat(adminUserDTO.getSubscription()).isEqualTo("subscription");
        assertThat(adminUserDTO.getRegistrationDate()).isEqualTo("2023-09-28");
        assertThat(adminUserDTO.getRoles()).hasSize(1);
    }

    @Test
    public void rolesAdminDtoTest() {
        RolesAdminDTO rolesAdminDTO = new RolesAdminDTO();
        rolesAdminDTO.setId(1L);
        rolesAdminDTO.setName("ROLE_ADMIN");

        assertThat(rolesAdminDTO.getId()).isEqualTo(1L);
        assertThat(rolesAdminDTO.getName()).isEqualTo("ROLE_ADMIN");
    }

    @Test
    public void jwtAuthResponseDtoTest() {
        JwtAuthResponseDTO jwtAuthResponseDTO = new JwtAuthResponseDTO();
        jwtAuthResponseDTO.setAccessToken("sampleAccessToken");
        jwtAuthResponseDTO.setRole(Set.of("ROLE_USER"));
        jwtAuthResponseDTO.setEmail("testEmail@abv.bg");

        assertThat(jwtAuthResponseDTO.getAccessToken()).isEqualTo("sampleAccessToken");
        assertThat(jwtAuthResponseDTO.getTokenType()).isEqualTo("Bearer");
        assertThat(jwtAuthResponseDTO.getRole()).containsExactly("ROLE_USER");
        assertThat(jwtAuthResponseDTO.getEmail()).isEqualTo("testEmail@abv.bg");

    }

    @Test
    public void loginDtoTest() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("username");
        loginDTO.setPassword("password123");

        LoginDTO loginDTO1 = new LoginDTO("username", "password123");

        assertEquals(loginDTO.getUsername(), loginDTO1.getUsername());
        assertEquals(loginDTO.getPassword(), loginDTO1.getPassword());

        assertThat(loginDTO.getUsername()).isEqualTo("username");
        assertThat(loginDTO.getPassword()).isEqualTo("password123");
    }

    @Test
    public void registerDtoTest() {
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setUsername("newuser");
        registerDTO.setEmail("newuser@example.com");
        registerDTO.setPassword("newpassword123");

        assertThat(registerDTO.getUsername()).isEqualTo("newuser");
        assertThat(registerDTO.getEmail()).isEqualTo("newuser@example.com");
        assertThat(registerDTO.getPassword()).isEqualTo("newpassword123");
    }

    @Test
    public void userRegisterUsernameAndEmailDtoTest() {
        UserRegisterUsernameAndEmailDTO userDto = new UserRegisterUsernameAndEmailDTO();
        userDto.setUsername("user123");
        userDto.setEmail("user@example.com");

        assertThat(userDto.getUsername()).isEqualTo("user123");
        assertThat(userDto.getEmail()).isEqualTo("user@example.com");
    }

    @Test
    public void answerDtoTest() {
        AnswerDTO answerDTO = new AnswerDTO();
        answerDTO.setId(1L);
        answerDTO.setContent("Sample content");
        answerDTO.setVoteCount(10L);
        answerDTO.setWriter("user123");
        answerDTO.setDate("2023-09-28");
        answerDTO.setUserSubscriptionPlan("Basic");
        answerDTO.setUserBiography("User biography");
        answerDTO.setLiked(true);

        assertThat(answerDTO.getId()).isEqualTo(1L);
        assertThat(answerDTO.getContent()).isEqualTo("Sample content");
        assertThat(answerDTO.getVoteCount()).isEqualTo(10L);
        assertThat(answerDTO.getWriter()).isEqualTo("user123");
        assertThat(answerDTO.getDate()).isEqualTo("2023-09-28");
        assertThat(answerDTO.getUserSubscriptionPlan()).isEqualTo("Basic");
        assertThat(answerDTO.getUserBiography()).isEqualTo("User biography");
        assertThat(answerDTO.getLiked()).isTrue();
    }

    @Test
    public void answerLikeDtoTest() {
        AnswerLikeDTO answerLikeDTO = new AnswerLikeDTO();
        answerLikeDTO.setUsername("user123");
        answerLikeDTO.setAnswerID(1L);

        assertThat(answerLikeDTO.getUsername()).isEqualTo("user123");
        assertThat(answerLikeDTO.getAnswerID()).isEqualTo(1L);
    }

    @Test
    public void communityAgreeToTermsAndConditionsDtoTest() {
        CommunityAgreeToTermsAndConditionsDTO agreeToTermsDTO = new CommunityAgreeToTermsAndConditionsDTO();
        agreeToTermsDTO.setUsername("user123");
        agreeToTermsDTO.setAgreedToTerms(true);

        assertThat(agreeToTermsDTO.getUsername()).isEqualTo("user123");
        assertThat(agreeToTermsDTO.isAgreedToTerms()).isTrue();
    }

    @Test
    public void questionDtoTest() {
        AnswerDTO answerDTO = new AnswerDTO();

        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setId(1L);
        questionDTO.setContent("Sample question content");
        questionDTO.setTopic("Sample topic");
        questionDTO.setDate("2023-09-28");
        questionDTO.setWriter("user123");
        questionDTO.setSolved(false);
        questionDTO.setUserBiography("User biography");
        questionDTO.setSubscriptionPlan("Premium");
        questionDTO.setAnswers(new ArrayList<>());
        questionDTO.getAnswers().add(answerDTO);

        assertThat(questionDTO.getId()).isEqualTo(1L);
        assertThat(questionDTO.getContent()).isEqualTo("Sample question content");
        assertThat(questionDTO.getTopic()).isEqualTo("Sample topic");
        assertThat(questionDTO.getDate()).isEqualTo("2023-09-28");
        assertThat(questionDTO.getWriter()).isEqualTo("user123");
        assertThat(questionDTO.getSolved()).isFalse();
        assertThat(questionDTO.getUserBiography()).isEqualTo("User biography");
        assertThat(questionDTO.getSubscriptionPlan()).isEqualTo("Premium");
        assertEquals(1, questionDTO.getAnswers().size());
    }


    @Test
    public void stripeTransactionDtoTest() {
        StripeTransactionDTO stripeTransactionDTO = new StripeTransactionDTO();
        stripeTransactionDTO.setUserEmail("Email");
        stripeTransactionDTO.setBillingDate("2023-09-28");
        stripeTransactionDTO.setDuration("1 month");
        stripeTransactionDTO.setEndOfBillingDate("2023-10-28");
        stripeTransactionDTO.setAmount("$10.00");
        stripeTransactionDTO.setCard("**** **** **** 1234");
        stripeTransactionDTO.setStatus("Paid");
        stripeTransactionDTO.setReceipt("receipt123");
        stripeTransactionDTO.setDescription("Payment for subscription");

        assertThat(stripeTransactionDTO.getUserEmail()).isEqualTo("Email");
        assertThat(stripeTransactionDTO.getBillingDate()).isEqualTo("2023-09-28");
        assertThat(stripeTransactionDTO.getDuration()).isEqualTo("1 month");
        assertThat(stripeTransactionDTO.getEndOfBillingDate()).isEqualTo("2023-10-28");
        assertThat(stripeTransactionDTO.getAmount()).isEqualTo("$10.00");
        assertThat(stripeTransactionDTO.getCard()).isEqualTo("**** **** **** 1234");
        assertThat(stripeTransactionDTO.getStatus()).isEqualTo("Paid");
        assertThat(stripeTransactionDTO.getReceipt()).isEqualTo("receipt123");
        assertThat(stripeTransactionDTO.getDescription()).isEqualTo("Payment for subscription");
        assertFalse(stripeTransactionDTO.isEmailSent());
    }

    @Test
    public void updateUserBiographyDtoTest() {
        UpdateUserBiographyDTO updateUserBiographyDTO = new UpdateUserBiographyDTO();
        updateUserBiographyDTO.setBiography("Updated user biography");

        UpdateUserBiographyDTO updateUserBiographyDTO1 = new UpdateUserBiographyDTO("Updated user biography");

        assertEquals(updateUserBiographyDTO1.getBiography(), updateUserBiographyDTO.getBiography());
        assertThat(updateUserBiographyDTO.getBiography()).isEqualTo("Updated user biography");
    }

    @Test
    public void topicDtoTest() {
        TopicDTO topicDTO = new TopicDTO();
        topicDTO.setName("Topic");

        TopicDTO topicDTO1 = new TopicDTO("Topic");

        assertEquals(topicDTO.getName(), topicDTO1.getName());
        assertEquals("Topic", topicDTO.getName());
    }

    @Test
    public void aboutDtoTest() {
        AboutDTO aboutDTO = new AboutDTO();
        aboutDTO.setTitle("Title");
        aboutDTO.setDescription("Description");

        assertEquals("Title", aboutDTO.getTitle());
        assertEquals("Description", aboutDTO.getDescription());
    }

    @Test
    public void partnerDtoTest() {
        PartnerDTO partnerDTO = new PartnerDTO();
        partnerDTO.setTitle("Title");
        partnerDTO.setFirstLine("First Line");
        partnerDTO.setSecondLine("Second Line");
        partnerDTO.setThirdLine("Third Line");
        partnerDTO.setFourthLine("Fourth Line");
        partnerDTO.setFifthLine("Fifth Line");
        partnerDTO.setSixthLine("Sixth Line");
        partnerDTO.setSeventhLine("Seventh Line");
        partnerDTO.setLinkURL("https://example.com");

        assertEquals("Title", partnerDTO.getTitle());
        assertEquals("First Line", partnerDTO.getFirstLine());
        assertEquals("Second Line", partnerDTO.getSecondLine());
        assertEquals("Third Line", partnerDTO.getThirdLine());
        assertEquals("Fourth Line", partnerDTO.getFourthLine());
        assertEquals("Fifth Line", partnerDTO.getFifthLine());
        assertEquals("Sixth Line", partnerDTO.getSixthLine());
        assertEquals("Seventh Line", partnerDTO.getSeventhLine());
        assertEquals("https://example.com", partnerDTO.getLinkURL());
    }

    @Test
    public void pricingDtoTest() {
        PricingDTO pricingDTO = new PricingDTO();
        pricingDTO.setPrice("Price");
        pricingDTO.setDuration("Duration");
        pricingDTO.setFirstLine("First Line");
        pricingDTO.setSecondLine("Second Line");
        pricingDTO.setThirdLine("Third Line");
        pricingDTO.setFourthLine("Fourth Line");
        pricingDTO.setFifthLine("Fifth Line");
        pricingDTO.setSixthLine("Sixth Line");
        pricingDTO.setSeventhLine("Seventh Line");
        pricingDTO.setEighthLine("Eighth Line");
        pricingDTO.setLinkURL("https://example.com");

        assertEquals("Price", pricingDTO.getPrice());
        assertEquals("Duration", pricingDTO.getDuration());
        assertEquals("First Line", pricingDTO.getFirstLine());
        assertEquals("Second Line", pricingDTO.getSecondLine());
        assertEquals("Third Line", pricingDTO.getThirdLine());
        assertEquals("Fourth Line", pricingDTO.getFourthLine());
        assertEquals("Fifth Line", pricingDTO.getFifthLine());
        assertEquals("Sixth Line", pricingDTO.getSixthLine());
        assertEquals("Seventh Line", pricingDTO.getSeventhLine());
        assertEquals("Eighth Line", pricingDTO.getEighthLine());
        assertEquals("https://example.com", pricingDTO.getLinkURL());
    }

    @Test
    public void tradingAccountPageDtoTest() {
        TradeDTO tradeDTO = new TradeDTO();
        tradeDTO.setOpenTime("Open Time");
        tradeDTO.setCloseTime("Close Time");
        tradeDTO.setSymbol("Symbol");
        tradeDTO.setAction("Action");
        tradeDTO.setPips(10.5);
        tradeDTO.setProfit(1000.0);
        tradeDTO.setCommission(25.0);

        assertEquals("Open Time", tradeDTO.getOpenTime());
        assertEquals("Close Time", tradeDTO.getCloseTime());
        assertEquals("Symbol", tradeDTO.getSymbol());
        assertEquals("Action", tradeDTO.getAction());
        assertEquals(10.5, tradeDTO.getPips(), 0.001);
        assertEquals(1000.0, tradeDTO.getProfit(), 0.001);
        assertEquals(25.0, tradeDTO.getCommission(), 0.001);

        TradingAccountDTO tradingAccountDTO = new TradingAccountDTO();
        tradingAccountDTO.setResponseIdentity(1L);
        tradingAccountDTO.setAccountIdentity(2L);
        tradingAccountDTO.setDeposits(10000.0);
        tradingAccountDTO.setProfit(5000.0);
        tradingAccountDTO.setBalance(7000.0);
        tradingAccountDTO.setEquity(7500.0);
        tradingAccountDTO.setLastUpdateDate("2023-10-30");
        tradingAccountDTO.setCreationDate("2023-01-01");
        tradingAccountDTO.setFirstTradeDate("2023-01-15");
        tradingAccountDTO.setCurrency("USD");
        tradingAccountDTO.setServer("DemoServer");
        tradingAccountDTO.setTrades(new ArrayList<>());
        tradingAccountDTO.getTrades().add(tradeDTO);

        assertEquals(1L, tradingAccountDTO.getResponseIdentity());
        assertEquals(2L, tradingAccountDTO.getAccountIdentity());
        assertEquals(10000.0, tradingAccountDTO.getDeposits(), 0.001);
        assertEquals(5000.0, tradingAccountDTO.getProfit(), 0.001);
        assertEquals(7000.0, tradingAccountDTO.getBalance(), 0.001);
        assertEquals(7500.0, tradingAccountDTO.getEquity(), 0.001);
        assertEquals("2023-10-30", tradingAccountDTO.getLastUpdateDate());
        assertEquals("2023-01-01", tradingAccountDTO.getCreationDate());
        assertEquals("2023-01-15", tradingAccountDTO.getFirstTradeDate());
        assertEquals("USD", tradingAccountDTO.getCurrency());
        assertEquals("DemoServer", tradingAccountDTO.getServer());
    }

    @Test
    public void tradingAccountFooterDtoTest() {
        TradingAccountFooterDTO footerDTO = new TradingAccountFooterDTO();
        footerDTO.setId(1L);
        footerDTO.setGain(5000.0);

        assertEquals(1L, footerDTO.getId());
        assertEquals(5000.0, footerDTO.getGain(), 0.001);
    }

    @Test
    void testLocationDTO() {
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setContinent("Continent");
        locationDTO.setCity("City");
        locationDTO.setCountry("Country");
        locationDTO.setIp("127.0.0.1");
        locationDTO.setCountryFlagURL("https://example.com/flag.png");
        locationDTO.setUsername("User123");

        assertEquals("Continent", locationDTO.getContinent());
        assertEquals("City", locationDTO.getCity());
        assertEquals("Country", locationDTO.getCountry());
        assertEquals("127.0.0.1", locationDTO.getIp());
        assertEquals("https://example.com/flag.png", locationDTO.getCountryFlagURL());
        assertEquals("User123", locationDTO.getUsername());
    }

    @Test
    void testUserDetailsDTO() {
        UserDetailsDTO userDetailsDTO = new UserDetailsDTO();
        userDetailsDTO.setUsername("User123");
        userDetailsDTO.setEmail("user@example.com");
        userDetailsDTO.setRegistrationDate("1.1.1.1.1");
        userDetailsDTO.setSubscription("Gold");
        userDetailsDTO.setBiography("This is a test biography");

        assertEquals("User123", userDetailsDTO.getUsername());
        assertEquals("user@example.com", userDetailsDTO.getEmail());
        assertEquals("1.1.1.1.1", userDetailsDTO.getRegistrationDate());
        assertEquals("Gold", userDetailsDTO.getSubscription());
        assertEquals("This is a test biography", userDetailsDTO.getBiography());
    }

}
