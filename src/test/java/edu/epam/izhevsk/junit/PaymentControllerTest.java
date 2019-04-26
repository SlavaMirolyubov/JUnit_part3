package edu.epam.izhevsk.junit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;



import static org.mockito.Mockito.*;



@RunWith(MockitoJUnitRunner.class)
public class PaymentControllerTest {

    @Mock
    private AccountService accountService;

    @Mock
    private DepositService depositService;

    @InjectMocks
    private PaymentController paymentController;

    @Before
    public void setUp() throws InsufficientFundsException {
        when(depositService.deposit(AdditionalMatchers.gt(100L), Matchers.anyLong())).thenThrow(new InsufficientFundsException());
        when(accountService.isUserAuthenticated(100L)).thenReturn(true);
    }

    @Test
    public void testAuthUserOnce() throws InsufficientFundsException {
        paymentController.deposit(50L, 100L);
        verify(accountService, times(1)).isUserAuthenticated(100L);
    }

    @Test(expected = SecurityException.class)
    public void testUserNotAuth() throws InsufficientFundsException {
        paymentController.deposit(50L, 999L);
    }

    @Test(expected = InsufficientFundsException.class)
    public void testDigAmount() throws InsufficientFundsException {
        paymentController.deposit(999L, 100L);
    }

 
}
