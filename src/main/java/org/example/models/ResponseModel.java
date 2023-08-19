package org.example.models;

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
        "responseType",
        "data"
})
@Generated("jsonschema2pojo")
public class ResponseModel {

    public ResponseModel(String responseType, List<String> data) {
        this.responseType = responseType;
        this.data = data;
    }

    @JsonProperty("responseType")
    private String responseType;
    @JsonProperty("data")
    private List<String> data;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("responseType")
    public String getResponseType() {
        return responseType;
    }

    @JsonProperty("responseType")
    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }

    @JsonProperty("data")
    public List<String> getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(List<String> data) {
        this.data = data;
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



