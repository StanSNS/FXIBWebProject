package EntityObjectTest;

import fxibBackend.entity.*;
import fxibBackend.entity.Base.BaseEntity;
import fxibBackend.entity.DataEntity.AboutEntity;
import fxibBackend.entity.DataEntity.PartnerEntity;
import fxibBackend.entity.DataEntity.PricingEntity;
import fxibBackend.entity.DataEntity.TradingAccount.TradeEntity;
import fxibBackend.entity.DataEntity.TradingAccount.TradingAccountEntity;
import fxibBackend.entity.enums.TopicEnum;
import fxibBackend.exception.ResourceNotFoundException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static fxibBackend.constants.InitConst.*;
import static org.junit.jupiter.api.Assertions.*;

public class EntityTests {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Test
    public void testTradingAccountEntityProperties() {
        TradeEntity trade = new TradeEntity();
        trade.setOpenTime("2023-10-30");
        trade.setCloseTime("2023-10-31");
        trade.setSymbol("EUR/USD");
        trade.setAction("Buy");
        trade.setPips(50.0);
        trade.setProfit(100.0);
        trade.setCommission(5.0);

        assertEquals("2023-10-30", trade.getOpenTime());
        assertEquals("2023-10-31", trade.getCloseTime());
        assertEquals("EUR/USD", trade.getSymbol());
        assertEquals("Buy", trade.getAction());
        assertEquals(50.0, trade.getPips(), 0.001);
        assertEquals(100.0, trade.getProfit(), 0.001);
        assertEquals(5.0, trade.getCommission(), 0.001);

        TradingAccountEntity account = new TradingAccountEntity();
        account.setResponseIdentity(1L);
        account.setAccountIdentity(1001L);
        account.setDeposits(1000.0);
        account.setProfit(500.0);
        account.setBalance(1500.0);
        account.setEquity(1600.0);
        account.setLastUpdateDate("2023-10-30");
        account.setCreationDate("2023-01-01");
        account.setFirstTradeDate("2023-01-15");
        account.setCurrency("USD");
        account.setServer("DemoServer");
        account.setTrades(new ArrayList<>());
        account.getTrades().add(trade);

        assertEquals(1L, account.getResponseIdentity());
        assertEquals(1001L, account.getAccountIdentity());
        assertEquals(1000.0, account.getDeposits(), 0.001);
        assertEquals(500.0, account.getProfit(), 0.001);
        assertEquals(1500.0, account.getBalance(), 0.001);
        assertEquals(1600.0, account.getEquity(), 0.001);
        assertEquals("2023-10-30", account.getLastUpdateDate());
        assertEquals("2023-01-01", account.getCreationDate());
        assertEquals("2023-01-15", account.getFirstTradeDate());
        assertEquals("USD", account.getCurrency());
        assertEquals("DemoServer", account.getServer());
        assertEquals(1, account.getTrades().size());
    }

    @Test
    public void testAboutEntityProperties() {
        AboutEntity about = new AboutEntity();
        about.setTitle("About Us");
        about.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit.");

        AboutEntity aboutEntity = new AboutEntity("About Us", "Lorem ipsum dolor sit amet, consectetur adipiscing elit.");

        assertEquals(about.getTitle(), aboutEntity.getTitle());
        assertEquals(about.getDescription(), aboutEntity.getDescription());
        assertEquals("About Us", about.getTitle());
        assertEquals("Lorem ipsum dolor sit amet, consectetur adipiscing elit.", about.getDescription());
    }

    @Test
    public void testPartnerEntityProperties() {
        PartnerEntity partner = new PartnerEntity();
        partner.setTitle("Our Partners");
        partner.setFirstLine("Partner 1");
        partner.setSecondLine("Partner 2");
        partner.setThirdLine("Partner 3");
        partner.setFourthLine("Partner 4");
        partner.setFifthLine("Partner 5");
        partner.setSixthLine("Partner 6");
        partner.setSeventhLine("Partner 7");
        partner.setLinkURL("https://example.com/partners");

        PartnerEntity partnerEntity = new PartnerEntity(
                "Our Partners",
                "Partner 1",
                "Partner 2",
                "Partner 3",
                "Partner 4",
                "Partner 5",
                "Partner 6",
                "Partner 7",
                "https://example.com/partners");

        assertEquals(partner.getTitle(), partnerEntity.getTitle());
        assertEquals(partner.getFirstLine(), partnerEntity.getFirstLine());
        assertEquals(partner.getSecondLine(), partnerEntity.getSecondLine());
        assertEquals(partner.getThirdLine(), partnerEntity.getThirdLine());
        assertEquals(partner.getFourthLine(), partnerEntity.getFourthLine());
        assertEquals(partner.getFifthLine(), partnerEntity.getFifthLine());
        assertEquals(partner.getSixthLine(), partnerEntity.getSixthLine());
        assertEquals(partner.getSeventhLine(), partnerEntity.getSeventhLine());
        assertEquals(partner.getLinkURL(), partnerEntity.getLinkURL());

        assertEquals("Our Partners", partner.getTitle());
        assertEquals("Partner 1", partner.getFirstLine());
        assertEquals("Partner 2", partner.getSecondLine());
        assertEquals("Partner 3", partner.getThirdLine());
        assertEquals("Partner 4", partner.getFourthLine());
        assertEquals("Partner 5", partner.getFifthLine());
        assertEquals("Partner 6", partner.getSixthLine());
        assertEquals("Partner 7", partner.getSeventhLine());
        assertEquals("https://example.com/partners", partner.getLinkURL());
    }

    @Test
    public void testPricingEntityProperties() {
        PricingEntity pricing = new PricingEntity();
        pricing.setPrice("$99");
        pricing.setDuration("1 Month");
        pricing.setLinkURL("https://example.com/pricing");

        assertEquals("$99", pricing.getPrice());
        assertEquals("1 Month", pricing.getDuration());
        assertEquals(pricing.getFirstLine(), PRICING_FIRST_LINE);
        assertEquals(pricing.getSecondLine(), PRICING_SECOND_LINE);
        assertEquals(pricing.getThirdLine(), PRICING_THIRD_LINE);
        assertEquals(pricing.getFourthLine(), PRICING_FOURTH_LINE);
        assertEquals(pricing.getFifthLine(), PRICING_FIFTH_LINE);
        assertEquals(pricing.getSixthLine(), PRICING_SIXTH_LINE);
        assertEquals(pricing.getSeventhLine(), PRICING_SEVENTH_LINE);
        assertEquals(pricing.getEighthLine(), PRICING_EIGHTH_LINE);
        assertEquals("https://example.com/pricing", pricing.getLinkURL());
    }

    @Test
    public void testEnumOperations() {
        assertEquals("Forex Basics", TopicEnum.FOREX_BASICS.getText());
        assertEquals("Trading Strategies", TopicEnum.TRADING_STRATEGIES.getText());
        assertEquals("Other", TopicEnum.OTHER.getText());

        assertEquals(TopicEnum.FOREX_BASICS, TopicEnum.getFromText("Forex Basics"));
        assertEquals(TopicEnum.TRADING_STRATEGIES, TopicEnum.getFromText("Trading Strategies"));
        assertEquals(TopicEnum.OTHER, TopicEnum.getFromText("Other"));

        assertEquals(TopicEnum.FOREX_BASICS, TopicEnum.getFromText("forex basics"));

        assertThrows(ResourceNotFoundException.class, () -> TopicEnum.getFromText("Invalid Topic"));
    }

    private static class BaseEntityConcrete extends BaseEntity { }

    @Test
    public void teslAllEntities() {
        BaseEntityConcrete baseEntityConcrete = new BaseEntityConcrete();
        baseEntityConcrete.setId(1L);

        assertEquals(1L, baseEntityConcrete.getId());

        LocationEntity location = new LocationEntity();
        location.setContinent("Europe");
        location.setCity("London");
        location.setCountry("United Kingdom");
        location.setIp("192.168.1.1");
        location.setCountryFlagURL("https://example.com/uk-flag.png");
        location.setUsername("user123");

        assertEquals("Europe", location.getContinent());
        assertEquals("London", location.getCity());
        assertEquals("United Kingdom", location.getCountry());
        assertEquals("192.168.1.1", location.getIp());
        assertEquals("https://example.com/uk-flag.png", location.getCountryFlagURL());
        assertEquals("user123", location.getUsername());

        AnswerLikeEntity answerLikeEntity = new AnswerLikeEntity();
        answerLikeEntity.setUsername("username");
        answerLikeEntity.setAnswerID(1L);
        answerLikeEntity.setDeleted(false);
        assertEquals("username", answerLikeEntity.getUsername());
        assertEquals(1L, answerLikeEntity.getAnswerID());
        assertFalse(answerLikeEntity.getDeleted());

        TopicEntity topicEntity1 = new TopicEntity();
        topicEntity1.setTopicEnum(TopicEnum.OTHER);
        TopicEntity topicEntity2 = new TopicEntity(TopicEnum.OTHER);
        assertEquals(topicEntity1.getTopicEnum().getText(), topicEntity2.getTopicEnum().getText());

        AnswerEntity answer = new AnswerEntity();
        answer.setContent("Content");
        answer.setWriter("Writer");
        answer.setDate("Date");
        answer.setVoteCount(1L);
        answer.setDeleted(false);
        assertEquals("Content", answer.getContent());
        assertEquals("Writer", answer.getWriter());
        assertEquals("Date", answer.getDate());
        assertEquals(1L, answer.getVoteCount());
        assertFalse(answer.getDeleted());

        QuestionEntity question = new QuestionEntity();
        question.setContent("Content");
        question.setDate("Date");
        question.setWriter("Writer");
        question.setSolved(false);
        question.setDeleted(false);
        question.setTopicEntity(topicEntity1);
        question.setAnswers(new ArrayList<>());
        question.getAnswers().add(answer);

        assertEquals("Content", question.getContent());
        assertEquals("Writer", question.getWriter());
        assertEquals("Date", question.getDate());
        assertFalse(question.getSolved());
        assertFalse(question.getDeleted());
        assertNotNull(question.getTopicEntity());
        assertEquals(1, question.getAnswers().size());

        RoleEntity roleEntity1 = new RoleEntity();
        roleEntity1.setName("USER");

        RoleEntity roleEntity2 = new RoleEntity("USER");

        assertEquals(roleEntity1.getName(), roleEntity2.getName());

        UserEntity user = new UserEntity();

        user.setUsername("user123");
        user.setEmail("user@example.com");
        user.setPassword("securePassword");
        user.setSubscription("Free");
        user.setRegistrationDate("1,1,1,1,1,1");
        user.setBiography("Sample biography");
        user.setAgreedToTerms(true);

        Set<RoleEntity> roles = new HashSet<>();
        roles.add(roleEntity1);
        user.setRoles(roles);
        user.setJwtToken("jwt");
        user.setResetToken("resetToken");
        user.setNumberOfLogins(1);
        user.setTwoFactorLoginCode(1234);
        user.setQuestions(new ArrayList<>());
        user.getQuestions().add(question);
        user.setLocationEntity(location);
        user.setLikedAnswer(answerLikeEntity);

        var violations = validator.validate(user);

        assertTrue(violations.isEmpty());

        assertEquals("user123", user.getUsername());
        assertEquals("user@example.com", user.getEmail());
        assertEquals("securePassword", user.getPassword());
        assertEquals("Free", user.getSubscription());
        assertEquals("1,1,1,1,1,1", user.getRegistrationDate());
        assertEquals("Sample biography", user.getBiography());
        assertTrue(user.getAgreedToTerms());
        assertEquals(1, user.getRoles().size());
        assertEquals("jwt", user.getJwtToken());
        assertEquals("resetToken", user.getResetToken());
        assertEquals(1, user.getNumberOfLogins());
        assertEquals(1234, user.getTwoFactorLoginCode());
        assertEquals(1, user.getQuestions().size());
        assertNotNull(user.getLocationEntity());
        assertNotNull(user.getLikedAnswer());

        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setUserEmail("Email");
        transactionEntity.setBillingDate("Date");
        transactionEntity.setDuration("Duration");
        transactionEntity.setEndOfBillingDate("End of billing date");
        transactionEntity.setAmount("Amount");
        transactionEntity.setCard("Card");
        transactionEntity.setStatus("Status");
        transactionEntity.setReceipt("Receipt");
        transactionEntity.setDescription("Description");
        transactionEntity.setEmailSent(true);
        transactionEntity.setUserEntity(user);

        assertEquals("Email", transactionEntity.getUserEmail());
        assertEquals("Date", transactionEntity.getBillingDate());
        assertEquals("Duration", transactionEntity.getDuration());
        assertEquals("End of billing date", transactionEntity.getEndOfBillingDate());
        assertEquals("Amount", transactionEntity.getAmount());
        assertEquals("Card", transactionEntity.getCard());
        assertEquals("Status", transactionEntity.getStatus());
        assertEquals("Receipt", transactionEntity.getReceipt());
        assertEquals("Description", transactionEntity.getDescription());
        assertTrue(transactionEntity.isEmailSent());
        assertNotNull(transactionEntity.getUserEntity());
    }

    @Test
    public void testInquiryEntityProperties() {
        // Create a sample UserEntity for testing
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("testUser");

        // Create an InquiryEntity
        InquiryEntity inquiryEntity = new InquiryEntity();
        inquiryEntity.setCustomID("INQ-12345678");
        inquiryEntity.setTitle("Test Inquiry");
        inquiryEntity.setContent("This is a test inquiry.");
        inquiryEntity.setDate("2023-11-21");
        inquiryEntity.setUserEntity(userEntity);

        // Validate the InquiryEntity using Bean Validation
        var violations = validator.validate(inquiryEntity);
        assertTrue(violations.isEmpty(), "Validation should pass for InquiryEntity");

        // Test InquiryEntity properties
        assertEquals("INQ-12345678", inquiryEntity.getCustomID());
        assertEquals("Test Inquiry", inquiryEntity.getTitle());
        assertEquals("This is a test inquiry.", inquiryEntity.getContent());
        assertEquals("2023-11-21", inquiryEntity.getDate());
        assertNotNull(inquiryEntity.getUserEntity());
        assertEquals("testUser", inquiryEntity.getUserEntity().getUsername());
    }
}
