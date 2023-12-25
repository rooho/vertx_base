package cc.phonecard.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Goods implements Serializable {

  private static final long serialVersionUID = 57270453628023323L;

  @JsonProperty("channelId")
  private String channelId;

  @JsonProperty("title")
  private String title;

  @JsonProperty("detail")
  private String detail;

  @JsonProperty("price")
  private BigDecimal price;

  @JsonProperty("salesVolume")
  private Integer salesVolume;

  @JsonProperty("imgTitleUrl")
  private String imgTitleUrl;

  @JsonProperty("imgDetailPageUrl")
  private String imgDetailPageUrl;
}
