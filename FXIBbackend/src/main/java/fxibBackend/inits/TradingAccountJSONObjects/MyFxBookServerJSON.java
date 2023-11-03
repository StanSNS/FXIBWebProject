package fxibBackend.inits.TradingAccountJSONObjects;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MyFxBookServerJSON {

    @NotNull
    private String name;
}
