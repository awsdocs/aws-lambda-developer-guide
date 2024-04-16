package example;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class F1Car {
    
    private String team;
    private String driver;

    @JsonCreator
    public F1Car(@JsonProperty("team") String team,
                 @JsonProperty("driver") String driver) {
        this.team = team;
        this.driver = driver;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }
}
