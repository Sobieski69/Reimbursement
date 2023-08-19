package org.example.models;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
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
        "receiptType",
        "value"
})
@Generated("jsonschema2pojo")
public class Receipt {

    @JsonProperty("receiptType")
    private String receiptType;
    @JsonProperty("value")
    private BigDecimal value;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<>();

    @JsonProperty("receiptType")
    public String getReceiptType() {
        return receiptType;
    }

    @JsonProperty("receiptType")
    public void setReceiptType(String receiptType) {
        this.receiptType = receiptType;
    }

    @JsonProperty("value")
    public BigDecimal getValue() {
        return value;
    }

    @JsonProperty("value")
    public void setValue(String value) {
        this.value = new BigDecimal(value);
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