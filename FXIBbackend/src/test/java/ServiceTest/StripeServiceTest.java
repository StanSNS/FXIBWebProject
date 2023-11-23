package ServiceTest;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.ChargeCollection;
import fxibBackend.dto.UserDetailsDTO.StripeTransactionDTO;
import fxibBackend.repository.TransactionEntityRepository;
import fxibBackend.service.StripeService;
import fxibBackend.util.CustomDateFormatter;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;

import java.util.List;

import static fxibBackend.constants.OtherConst.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StripeServiceTest {

    @BeforeClass
    public static void setUpStripe() {
        Stripe.apiKey = "sk_test_51IlHWpGlSGATKmQPCHQ7IkuW2JX6oYZbQaxYtclDmIFVcM2mQ6aoAWYucKJk6TV2NffBiXH6UUmZTlorAoCgAYab00THACivIs";
    }

    private StripeService stripeService;

    @Mock
    private TransactionEntityRepository transactionEntityRepository;

    @Mock
    private CustomDateFormatter customDateFormatter;

    @Before
    public void setUp() {
        stripeService = new StripeService(transactionEntityRepository, customDateFormatter);
    }

    @Test
    public void testGetAllTransactionsFromEmail() throws StripeException {
        Charge charge1 = mock(Charge.class);
        Charge.BillingDetails billingDetails = mock(Charge.BillingDetails.class);

        when(billingDetails.getEmail()).thenReturn("test@example.com");
        when(charge1.getBillingDetails()).thenReturn(billingDetails);
        when(charge1.getCreated()).thenReturn(1635768000L);
        when(charge1.getAmount()).thenReturn(1000L);
        when(charge1.getCurrency()).thenReturn("USD");
        when(charge1.getStatus()).thenReturn("succeeded");

        Charge charge2 = mock(Charge.class);

        ChargeCollection chargeCollection = mock(ChargeCollection.class);
        when(chargeCollection.getData()).thenReturn(List.of(charge1, charge2));

        List<StripeTransactionDTO> result = stripeService.getAllTransactionsFromEmail("test@example.com");

        assertEquals(0, result.size());
    }


    @Test
    public void testTransformToDuration() {
        assertEquals(PLAN_DURATION_ONE, stripeService.transformToDuration(PLAN_PRICE_ONE));
        assertEquals(PLAN_DURATION_TWO, stripeService.transformToDuration(PLAN_PRICE_TWO));
        assertEquals(PLAN_DURATION_THREE, stripeService.transformToDuration(PLAN_PRICE_THREE));
        assertEquals(PLAN_PRICE_DURATION, stripeService.transformToDuration("unknown"));
    }

}
