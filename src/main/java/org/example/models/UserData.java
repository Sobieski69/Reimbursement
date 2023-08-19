package org.example.models;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "mileage",
        "days",
        "receipts"
})
@Generated("jsonschema2pojo")
public class UserData {

    @JsonProperty("mileage")
    private BigDecimal mileage;
    @JsonProperty("days")
    private Integer days;
    @JsonProperty("receipts")
    private List<Receipt> receipts;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<>();

    @JsonProperty("mileage")
    public BigDecimal getMileage() {
        return mileage;
    }

    @JsonProperty("mileage")
    public void setMileage(String mileage) {
        this.mileage = new BigDecimal(mileage);
    }

    @JsonProperty("days")
    public Integer getDays() {
        return days;
    }

    @JsonProperty("days")
    public void setDays(Integer days) {
        this.days = days;
    }

    @JsonProperty("receipts")
    public List<Receipt> getReceipts() {
        return receipts;
    }

    @JsonProperty("receipts")
    public void setReceipts(List<Receipt> receipts) {
        this.receipts = receipts;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}