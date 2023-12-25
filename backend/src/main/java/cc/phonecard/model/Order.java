package cc.phonecard.model;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {

  private Integer channelId;

  private String orderNo;

  private String phoneNum;

  private Timestamp createTime;

  private Timestamp updateTime;
}
