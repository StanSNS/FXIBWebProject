package InitTest;

import fxibBackend.entity.DataEntity.AboutEntity;
import fxibBackend.entity.DataEntity.PartnerEntity;
import fxibBackend.entity.DataEntity.PricingEntity;
import fxibBackend.entity.RoleEntity;
import fxibBackend.entity.TopicEntity;
import fxibBackend.inits.*;
import fxibBackend.repository.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class SimpleInitTests {

    private AboutInit aboutInit;
    @Mock
    private AboutEntityRepository aboutEntityRepository;
    private PartnerInit partnerInit;
    @Mock
    private PartnerEntityRepository partnerEntityRepository;
    private PricingInit pricingInit;
    @Mock
    private PricingEntityRepository pricingEntityRepository;
    private RoleInit roleInit;
    @Mock
    private RoleEntityRepository roleEntityRepository;
    private TopicInit topicInit;
    @Mock
    private TopicEntityRepository topicEntityRepository;


    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        aboutInit = new AboutInit(aboutEntityRepository);
        partnerInit = new PartnerInit(partnerEntityRepository);
        pricingInit = new PricingInit(pricingEntityRepository);
        roleInit = new RoleInit(roleEntityRepository);
        topicInit = new TopicInit(topicEntityRepository);
    }

    @Test
    public void testAboutContentInit() {
        when(aboutEntityRepository.count()).thenReturn(0L);

        aboutInit.aboutContentInit();

        verify(aboutEntityRepository, times(2)).save(Mockito.any(AboutEntity.class));

        verify(aboutEntityRepository).count();
    }

    @Test
    public void testPartnerContentInit() {
        when(partnerEntityRepository.count()).thenReturn(0L);

        partnerInit.partnerContentInit();

        verify(partnerEntityRepository, times(6)).save(Mockito.any(PartnerEntity.class));

        verify(partnerEntityRepository).count();
    }

    @Test
    public void testPricingContentInit() {
        when(pricingEntityRepository.count()).thenReturn(0L);

        pricingInit.pricingContentInit();

        verify(pricingEntityRepository, times(3)).save(Mockito.any(PricingEntity.class));

        verify(pricingEntityRepository).count();
    }

    @Test
    public void testRoleContentInit() {
        when(roleEntityRepository.count()).thenReturn(0L);

        roleInit.rolesInit();

        verify(roleEntityRepository, times(3)).save(Mockito.any(RoleEntity.class));

        verify(roleEntityRepository).count();
    }

    @Test
    public void testTopicContentInit() {
        when(topicEntityRepository.count()).thenReturn(0L);

        topicInit.topicInit();

        verify(topicEntityRepository, times(20)).save(Mockito.any(TopicEntity.class));

        verify(topicEntityRepository).count();
    }

}

