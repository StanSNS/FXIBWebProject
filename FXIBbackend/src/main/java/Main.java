import io.ipgeolocation.api.Geolocation;
import io.ipgeolocation.api.GeolocationParams;
import io.ipgeolocation.api.IPGeolocationAPI;
import io.ipgeolocation.api.Timezone;

import static fxibBackend.constants.ConfigConst.GEOLOCATION_API_KEY;

public class Main {
    public static void main(String[] args) {
        IPGeolocationAPI api = new IPGeolocationAPI(GEOLOCATION_API_KEY);


        // Get geolocation for IP address (1.1.1.1) and fields (geo, time_zone and currency)
        GeolocationParams geoParams = new GeolocationParams(
                "1.1.1.1",
                "",
                "geo,time_zone,currency",
                "",
                false,
                false,
                false,
                false,
                false);

        Geolocation geolocation = api.getGeolocation(geoParams);


        System.out.println(geolocation);


    }
}
