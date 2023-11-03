package fxibBackend.inits.TradingAccountJSONObjects;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TradingAccountResponseJSON {

    @NotNull
    private List<MyFxBookAccountJSON> accounts;

}
