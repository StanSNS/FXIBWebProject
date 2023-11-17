package fxibBackend.dto.UserDetailsDTO;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StripeTransactionDTO {

    @NotNull
    private String userEmail;

    @NotNull
    private String billingDate;

    @NotNull
    private String duration;

    @NotNull
    private String endOfBillingDate;

    @NotNull
    private String amount;

    @NotNull
    private String card;

    @NotNull
    private String status;

    @NotNull
    private String receipt;

    @NotNull
    private String description;

    @NotNull
    private boolean emailSent = false;
}
